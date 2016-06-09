package org.waabox.banten.web;

import org.waabox.banten.core.ConfigurationApi;
import org.waabox.banten.core.ConfigurationApiRegistry;
import org.waabox.banten.core.Module;

/** Web module configuration.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class WebAppModule implements Module {

  /** {@inheritDoc}.*/
  @Override
  public String getName() {
    return "banten-web-module";
  }

  /** {@inheritDoc}.*/
  @Override
  public Class<?> getPublicConfiguration() {
    return WebAppConfiguration.class;
  }

  /** {@inheritDoc}.*/
  @Override
  public ConfigurationApi getConfigurationApi() {
    return new WebAppConfigurationApi();
  }

  /** {@inheritDoc}.*/
  @Override
  public void init(final ConfigurationApiRegistry registry) {
  }

}
