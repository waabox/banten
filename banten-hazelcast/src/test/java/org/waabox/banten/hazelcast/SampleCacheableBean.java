package org.waabox.banten.hazelcast;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import com.hazelcast.config.CacheSimpleConfig;
import com.hazelcast.core.HazelcastInstance;

/** Just a sample singleton bean that counts the number of times that the
 * method called has been called.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class SampleCacheableBean {

  /** Number of times that the method {@link SampleCacheableBean#call()}
   * has been called.*/
  private int called = 0;

  @Autowired
  public SampleCacheableBean(final HazelcastInstance hz) {
    Validate.notNull(hz, "Hazelcast cannot be null.");
    hz.getConfig().addCacheConfig(
        new CacheSimpleConfig().setName("default"));
  }

  @Cacheable(key = "#key", value = "default")
  public String call(final String key) {
    called++;
    return "call" + key;
  }

  /** Retrieves the called.
   * @return the called
   */
  public int getCalled() {
    return called;
  }

}
