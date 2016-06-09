package org.waabox.banten.elasticsearch;

import org.waabox.banten.core.BantenApplication;
import org.waabox.banten.core.Bootstrap;
import org.waabox.banten.core.ConfigurationApiRegistry;
import org.waabox.banten.elasticsearch.ElasticsearchModule;

/** Just a Sample Application for testing purposes.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class ElasticsearchSampleApplication extends BantenApplication {

  @Override
  protected Bootstrap bootstrap() {
    return new Bootstrap(
        ElasticsearchModule.class,
        ElasticsearchSampleModule.class
    );
  }

  @Override
  public void init(final ConfigurationApiRegistry registry) {
  }

}
