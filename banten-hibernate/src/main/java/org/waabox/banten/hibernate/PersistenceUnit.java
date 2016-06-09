package org.waabox.banten.hibernate;

import static org.apache.commons.lang3.Validate.*;

import org.waabox.banten.core.Factory;

/** A persistence unit defines a Hibernate's persistence class and also its
 * factory class in order to customize the standard Hibernate's object
 * creation.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class PersistenceUnit {

  /** The persistence class, it's never null.*/
  private final Class<?> persistenceClass;

  /** The factory class for this persistence class, can be null.*/
  @SuppressWarnings("rawtypes")
  private final Class<? extends Factory> factory;

  /** Creates a new instance of the persistence unit with an empty factory.
   * @param thePersistenceClass the persistence class, cannot be null.
   */
  public PersistenceUnit(final Class<?> thePersistenceClass) {
    notNull(thePersistenceClass, "The persistence class cannot be null");
    persistenceClass = thePersistenceClass;
    factory = null;
  }

  /** Creates a new instance of the persistence unit with an empty factory.
   * @param thePersistenceClass the persistence class, cannot be null.
   * @param theFactory the factory class for the given persistence unit,
   *  cannot be null.
   */
  @SuppressWarnings("rawtypes")
  public PersistenceUnit(final Class<?> thePersistenceClass,
      final Class<? extends Factory> theFactory) {
    notNull(thePersistenceClass, "The persistence class cannot be null");
    notNull(theFactory, "The factory class cannot be null");
    persistenceClass = thePersistenceClass;
    factory = theFactory;
  }

  /** Retrieves the persistence class that will be mapped by Hibernate.
   * @return the persistence class, it's never null.
   */
  public Class<?> getPersistenceClass() {
    return persistenceClass;
  }

  /** Retrieves the persistence class name that will be mapped by Hibernate.
   * @return the persistence class name, it's never null.
   */
  public String getPersistenceClassName() {
    return persistenceClass.getName();
  }

  /** Retrieves the factory for the current persistence class.
   * @return the factory or null.
   */
  @SuppressWarnings("rawtypes")
  public Class<? extends Factory> getFactory() {
    return factory;
  }

  /** Checks if the current persistence unit has a custom factory or not.
   * @return true if this persistence unit has a custom factory.
   */
  public boolean hasCustomFactory() {
    return factory != null;
  }

}
