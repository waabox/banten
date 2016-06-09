package org.waabox.banten.elasticsearch;

import org.waabox.banten.core.ConfigurationApi;
import org.waabox.banten.core.ConfigurationApiRegistry;
import org.waabox.banten.core.Module;

/** Elasticsearch Module.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class ElasticsearchModule implements Module {

  /** {@inheritDoc}.*/
  @Override
  public String getName() {
    return "Elasticsearch-Module";
  }

  /** {@inheritDoc}.*/
  @Override
  public Class<?> getPublicConfiguration() {
    return ElasticsearchConfiguration.class;
  }

  /** {@inheritDoc}.*/
  @Override
  public ConfigurationApi getConfigurationApi() {
    return new ElasticsearchConfigurationApi();
  }

  /** {@inheritDoc}.*/
  @Override
  public void init(final ConfigurationApiRegistry registry) {
  }

}
