package org.waabox.banten.hazelcast;

import org.waabox.banten.core.ConfigurationApi;
import org.waabox.banten.core.ConfigurationApiRegistry;
import org.waabox.banten.core.Module;

/** Hazelcast Module.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class HazelcastModule implements Module {

  /** {@inheritDoc}.*/
  @Override
  public String getName() {
    return "Hazelcast-Module";
  }

  /** {@inheritDoc}.*/
  @Override
  public Class<?> getPublicConfiguration() {
    return HazelcastConfiguration.class;
  }

  /** {@inheritDoc}.*/
  @Override
  public void init(final ConfigurationApiRegistry registry) {
  }

  /** {@inheritDoc}.*/
  @Override
  public ConfigurationApi getConfigurationApi() {
    return null;
  }

}
