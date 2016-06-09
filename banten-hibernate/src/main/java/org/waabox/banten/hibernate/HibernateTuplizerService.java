package org.waabox.banten.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.Validate;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.service.Service;
import org.waabox.banten.core.BantenContext;
import org.waabox.banten.core.Factory;

/** Hibernate service for Hibernate's Tuplizers.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class HibernateTuplizerService implements Service {

  /** The serial version. */
  private static final long serialVersionUID = 1L;

  /** The list of {@link PersistenceUnit}, it's never null.*/
  private final List<PersistenceUnit> persistenceUnits;

  /** The {@link BantenContext}, it's never null. */
  private final BantenContext context;

  /** Creates a new instance of the
   * @param thePersistenceUnits the list of {@link PersistenceUnit}, cannot
   * be null.
   * @param theBantenContext the {@link BantenContext}, cannot be null.
   */
  public HibernateTuplizerService(
      final List<PersistenceUnit> thePersistenceUnits,
      final BantenContext theBantenContext) {

    Validate.notNull(thePersistenceUnits, "The list cannot be null");
    Validate.notNull(theBantenContext, "The context cannot be null");

    persistenceUnits = thePersistenceUnits;
    context = theBantenContext;
  }

  /** Retrieves the {@link Factory} for the given {@link PersistentClass}.
   * @param pc the {@link PersistentClass}, cannot be null.
   * @return the {@link Factory} or null.
   */
  @SuppressWarnings("rawtypes")
  Factory factoryFor(final PersistentClass pc) {
    Class<? extends Factory> fClass = FactoryCache.get(persistenceUnits, pc);
    if (fClass == null) {
      return null;
    }
    return context.getBean(fClass);
  }

  /** Cache for the Factory classes.*/
  @SuppressWarnings("rawtypes")
  private static class FactoryCache {

    /** The instance. */
    private static FactoryCache instance = new FactoryCache();

    /** Whether or not has been initialized. */
    private static AtomicBoolean initialized = new AtomicBoolean(false);

    /** The cache. */
    private Map<String, Class<? extends Factory>> factories = new HashMap<>();

    /** Retrieves the factory class by its class name.
     * @param name the class name.
     * @return the factory if exists.
     */
    public static Class<? extends Factory> get(
        final List<PersistenceUnit> pu, final PersistentClass pc) {
      if (!initialized.get()) {
        synchronized (initialized) {
          for(PersistenceUnit persistenceUnit : pu) {
            String name = persistenceUnit.getPersistenceClassName();
            instance.factories.put(name, persistenceUnit.getFactory());
          }
          initialized.set(true);
        }
      }
      return instance.factories.get(pc.getClassName());
    }
  }

}
