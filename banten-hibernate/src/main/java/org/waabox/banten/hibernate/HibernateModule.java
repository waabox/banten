package org.waabox.banten.hibernate;

import org.waabox.banten.core.ConfigurationApi;
import org.waabox.banten.core.ConfigurationApiRegistry;
import org.waabox.banten.core.Module;

/** Hibernate's Module.
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class HibernateModule implements Module {

  /** {@inheritDoc}.*/
  @Override
  public String getName() {
    return "Hibernate-Module";
  }

  /** {@inheritDoc}.*/
  @Override
  public Class<?> getPublicConfiguration() {
    return HibernateConfiguration.class;
  }

  @Override
  public void init(final ConfigurationApiRegistry registry) {
  }

  /** {@inheritDoc}.*/
  @Override
  public ConfigurationApi getConfigurationApi() {
    return new HibernateConfigurationApi();
  }

}
