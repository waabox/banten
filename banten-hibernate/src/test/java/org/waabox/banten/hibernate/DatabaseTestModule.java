package org.waabox.banten.hibernate;

import org.waabox.banten.core.ConfigurationApi;
import org.waabox.banten.core.ConfigurationApiRegistry;
import org.waabox.banten.core.Module;
import org.waabox.banten.hibernate.HibernateConfigurationApi;
import org.waabox.banten.hibernate.PersistenceUnit;

public class DatabaseTestModule implements Module {

  @Override
  public String getName() {
    return "database-test-module";
  }

  @Override
  public Class<?> getPublicConfiguration() {
    return DatabaseTestModuleConfiguration.class;
  }

  /** {@inheritDoc}.*/
  @Override
  public void init(final ConfigurationApiRegistry registry) {
    HibernateConfigurationApi api;
    api = registry.get(HibernateConfigurationApi.class);
    api.persistenceUnits(
        new PersistenceUnit(MockEntity.class),
        new PersistenceUnit(MockEntityWithCollectionWithTuplizers.class),
        new PersistenceUnit(MockEntityWithTuplizer.class,
            MockEntityWithTuplizerFactory.class)
    );
  }

  @Override
  public ConfigurationApi getConfigurationApi() {
    return null;
  }

}
