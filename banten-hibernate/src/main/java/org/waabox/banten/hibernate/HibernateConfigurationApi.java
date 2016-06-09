package org.waabox.banten.hibernate;

import java.util.*;

import org.waabox.banten.core.ConfigurationApi;

/** Hibernate's configuration.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class HibernateConfigurationApi extends ConfigurationApi {

  /** The list of persistence units. */
  private final List<PersistenceUnit> persistenceUnits = new LinkedList<>();

  /** Register the persistence units for a {@link Module}.
   * @param units the persistence units to register.
   */
  public void persistenceUnits(final PersistenceUnit ... units) {
    persistenceUnits.addAll(Arrays.asList(units));
  }

  /** Retrieves the list of persistence units.
   * @return the list of persistence units.
   */
  public List<PersistenceUnit> getPersistenceUnits() {
    return persistenceUnits;
  }

  /** Checks if the give class has a custom {@link Factory} defined.
   *
   * @param persistenceClass the persistence class, cannot be null.
   * @return true if has a custom factory.
   */
  public boolean hasCustomFactory(final Class<?> persistenceClass) {
    for (PersistenceUnit pu : persistenceUnits) {
      if (pu.getPersistenceClass().getName().equals(
          persistenceClass.getName())) {
        return pu.hasCustomFactory();
      }
    }
    return false;
  }

}
