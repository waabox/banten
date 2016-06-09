package org.waabox.banten.hibernate;

import org.apache.commons.lang3.Validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.*;

import org.springframework.transaction.*;
import org.springframework.transaction.support.*;

/** Allows programmatic transactions handling.
 *
 * <code>
 *  Transaction tx = new Transaction(...);
 *  try {
 *    tx.startSerializable();
 *    ...
 *    if (everythingIsOk) {
 *      tx.commit();
 *    } else {
 *      tx.rollback();
 *    }
 *  } finally {
 *    tx.cleanup();
 *  }
 * </code>
 *
 * This class also lets you register actions to perform in case the transaction
 * is rolled back. See registerRollbackAction.
 *
 * WARNING: we do not make any guarantee on the order in which we execute
 * rollback actions.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class Transaction {

  /** The class logger. */
  private static Logger log = LoggerFactory.getLogger(Transaction.class);

  /** The transaction manager, never null. */
  private final PlatformTransactionManager transactionManager;

  /** Holds the transaction bound for each thread, never null.*/
  private static ThreadLocal<TransactionInternalStatus> transaction
      = new ThreadLocal<Transaction.TransactionInternalStatus>();

  /** Creates a new instance of the Transaction Factory.
   *
   * @param manager Spring platform transaction manager. It cannot be null.
   */
  @Autowired
  public Transaction(final PlatformTransactionManager manager) {
    Validate.notNull(manager, "The transaction manager cannot be null");
    transactionManager = manager;
  }

  /** Creates a new transaction (and starts it) at the given isolation level.
   *
   * <p>
   * To find more information about isolation level please see:
   * {@link org.springframework.transaction.TransactionDefinition}
   * </p>
   *
   * @param propagation the propagation method, an int from
   * TransactionDefinition.
   *
   * @param isolation the isolation level, an int from TransactionDefinition.
   */
  public void start(final int propagation, final int isolation) {
    log.trace("Entering start({})", isolation);
    TransactionInternalStatus tx = transaction.get();
    if (tx != null || TransactionSynchronizationManager
        .isActualTransactionActive()) {
      throw new IllegalStateException("Could not open a new transaction,"
          + "please close the active transaction first");
    }
    transaction.set(new TransactionInternalStatus(transactionManager,
        propagation, isolation));

    log.trace("Leaving start", isolation);
  }

  /** Commits the current transaction.
   *
   * You must call start or startSerializable to start a new transaction,
   * before committing.
   */
  public void commit() {
    log.trace("Entering commit");
    TransactionInternalStatus tx = transaction.get();
    if (tx == null) {
      throw new IllegalStateException("Could not commit an empty transaction.");
    }
    tx.commit();
    transaction.remove();
    log.trace("Leaving commit");
  }

  /** Determines whether we are in an active transaction.
   *
   * @return true if we are inside a transaction, false otherwise.
   */
  public boolean insideTransaction() {
    log.trace("Entering insideTransaction");
    TransactionInternalStatus tx = transaction.get();
    log.trace("Leaving insideTransaction");
    return (tx != null);
  }

  /** Rolls back the current transaction.
   *
   * You must call start or startSerializable to start a new transaction,
   * before calling this.
   */
  public void rollback() {
    log.trace("Entering rollback");
    TransactionInternalStatus tx = transaction.get();
    if (tx == null) {
      throw new IllegalStateException(
          "Could not rollback an empty transaction.");
    }
    tx.rollback();
    transaction.remove();
    log.trace("Leaving rollback");
  }

  /** Cleans up the current transaction, if any.
   *
   * This operation rolls back any in-progress transaction, if any, and cleans
   * up.
   *
   * Be careful: this is intended to be called in a finally block. Cleanup
   * never throws an exception. Any exception is logged at the WARN level.
   */
  public void cleanup() {
    log.trace("Entering cleanup");
    TransactionInternalStatus tx = transaction.get();
    if (tx != null) {
      try {
        tx.rollback();
      } catch (RuntimeException e) {
        log.warn("Error rolling back transaction during cleanup.", e);
      } finally {
        transaction.remove();
      }
    }
    log.trace("Leaving cleanup");
  }

  /** Creates a new transaction (and starst it) at the serializable isolation
   * level.
   *
   * <p>
   * To find more information about isolation level please see:
   * {@link org.springframework.transaction.TransactionDefinition}
   * </p>
   */
  public void startSerializable() {
    start(TransactionDefinition.PROPAGATION_REQUIRED,
        TransactionDefinition.ISOLATION_SERIALIZABLE);
  }

  /** Creates a new transaction (and starts it) at the default isolation level.
   *
   * <p>
   * To find more information about isolation level please see:
   * {@link org.springframework.transaction.TransactionDefinition}
   * </p>
   */
  public void start() {
    start(TransactionDefinition.PROPAGATION_REQUIRED,
        TransactionDefinition.ISOLATION_DEFAULT);
  }

  /** Holds the transaction manager and transaction status.*/
  private static final class TransactionInternalStatus {

    /** The transaction manager, never null. */
    private final PlatformTransactionManager manager;

    /** The transaction status, never null. */
    private final TransactionStatus status;

    /** Creates a new instance of the transaction wrapper.
     *
     * @param theManager the transaction manager. Cannot be null.
     * @param propagation the propagation level.
     * @param isolation the isolation level.
     */
    private TransactionInternalStatus(
        final PlatformTransactionManager theManager,
        final int propagation, final int isolation) {
      Validate.notNull(theManager, "The transaction manager cannot be null");

      manager = theManager;

      DefaultTransactionDefinition transactionDefinition;
      transactionDefinition = new DefaultTransactionDefinition();
      transactionDefinition.setPropagationBehavior(propagation);
      transactionDefinition.setIsolationLevel(isolation);

      status = manager.getTransaction(transactionDefinition);
    }

    /** Commits the transaction.
     */
    public void commit() {
      manager.commit(status);
    }

    /** Rollbacks the transaction.
     */
    public void rollback() {
      manager.rollback(status);
    }
  }

  /** An action to perform is the transaction is rolled back.
   */
  public interface RollbackAction {

    /** Performs the rollback action. */
    void rollback();
  }

  /** Registers a rollback action in the transaction manager.
   *
   * This is used to perform additional actions on non-transactional resources
   * if the current platform transaction finishes with a rollback.
   *
   * The first use of this operation was in the StockService: if an order
   * reserves stock and the database transaction is rollbacked, we also need
   * to un-reserve the stock.
   *
   * If the transaction isn't started, it throws an IllegalStateException.
   *
   * WARNING: the order that the rollback actions are executed is random.
   *
   * @param action the rollback action to perform.
   */
  public static void registerRollbackAction(final RollbackAction action) {

    if(!TransactionSynchronizationManager.isActualTransactionActive()) {
      throw new IllegalStateException("Could not register the rollback "
          + "action if there's no actual transaction active.");
    }

    TransactionSynchronizationManager.registerSynchronization(
        new TransactionSynchronization() {

          /** {@inheritDoc}.*/
          @Override
          public void afterCompletion(final int status) {
            if (status != TransactionSynchronization.STATUS_COMMITTED) {
              action.rollback();
            }
          }

          /** {@inheritDoc}.*/
          @Override
          public void suspend() {
          }

          /** {@inheritDoc}.*/
          @Override
          public void resume() {
          }

          /** {@inheritDoc}.*/
          @Override
          public void flush() {
          }

          /** {@inheritDoc}.*/
          @Override
          public void beforeCommit(final boolean readOnly) {
          }

          /** {@inheritDoc}.*/
          @Override
          public void beforeCompletion() {
          }

          /** {@inheritDoc}.*/
          @Override
          public void afterCommit() {
          }
        });
  }
}
