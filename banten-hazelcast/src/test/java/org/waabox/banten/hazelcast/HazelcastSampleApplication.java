package org.waabox.banten.hazelcast;

import org.springframework.context.annotation.Bean;
import org.waabox.banten.core.BantenApplication;
import org.waabox.banten.core.Bootstrap;
import org.waabox.banten.core.ConfigurationApiRegistry;
import org.waabox.banten.hazelcast.HazelcastModule;

import com.hazelcast.core.HazelcastInstance;

/** Just a sample hazelcast application.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class HazelcastSampleApplication extends BantenApplication {

  /** {@inheritDoc}.*/
  protected Bootstrap bootstrap() {
    return new Bootstrap(HazelcastModule.class);
  }

  @Bean public SampleCacheableBean sampleCacheableBean(
      final HazelcastInstance hz) {
    return new SampleCacheableBean(hz);
  }

  @Override
  public void init(final ConfigurationApiRegistry registry) {
  }

}
