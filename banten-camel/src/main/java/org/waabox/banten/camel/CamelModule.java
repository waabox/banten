package org.waabox.banten.camel;

import org.waabox.banten.core.ConfigurationApi;
import org.waabox.banten.core.ConfigurationApiRegistry;
import org.waabox.banten.core.Module;

/** Camel Module.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class CamelModule implements Module {

  /** {@inheritDoc}.*/
  @Override
  public String getName() {
    return "Camel-Module";
  }

  /** {@inheritDoc}.*/
  @Override
  public Class<?> getPublicConfiguration() {
    return CamelConfiguration.class;
  }

  /** {@inheritDoc}.*/
  @Override
  public ConfigurationApi getConfigurationApi() {
    return null;
  }

  /** {@inheritDoc}.*/
  @Override
  public void init(final ConfigurationApiRegistry registry) {
  }

}
