package org.waabox.banten.hazelcast;

import java.util.Arrays;

import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;

import org.springframework.context.annotation.*;
import org.springframework.core.env.*;

import com.hazelcast.client.*;
import com.hazelcast.client.config.*;
import com.hazelcast.config.*;
import com.hazelcast.core.*;
import com.hazelcast.spring.cache.HazelcastCacheManager;

/** The Hazelcast configuration.
 *
 * Enables the {@link EnableCaching} and also declares the bean that expose
 * the {@link CacheManager}.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
@Configuration
@EnableCaching
public class HazelcastConfiguration {

  /** The log. */
  private static Logger log = getLogger(HazelcastConfiguration.class);

  /** Hazelcast Default Port. */
  private static final int DEFAULT_PORT = 5701;

  /** The environment with the properties loaded. */
  @Autowired private Environment environment;

  /** Creates a new instance of the {@link CacheManager}.
   * @param hz the Hazelcast's instance.
   * @return the {@link CacheManager}, never null.
   */
  @Bean public CacheManager cacheManager(final HazelcastInstance hz) {
    return new HazelcastCacheManager(hz);
  }

  /** Returns the HazelcastInstance. */
  @Bean(name = "banten.hazelcastInstance")
  public synchronized HazelcastInstance hazelcastInstance() {
    if (environment.getProperty("hz.node.client", Boolean.class, false)) {
      return HazelcastClient.newHazelcastClient(clientConfig());
    }
    return Hazelcast.getOrCreateHazelcastInstance(localConfig());
  }

  /** Return the Hazelcast Configuration instance. */
  private Config localConfig() {
    log.trace("Starting hazelcast local configuration");

    Config config = new Config(value("hz.instance.name",
        "defaultInstance", String.class));

    NetworkConfig netConfig = config.getNetworkConfig();

    netConfig.setPortAutoIncrement(
        booleanValue("hz.network.port.autoincrement", true));

    netConfig.setPort(port());

    JoinConfig join = netConfig.getJoin();
    join.getMulticastConfig().setEnabled(false);

    if (!Boolean.valueOf(value("hz.tcp.ip.enabled"))) {
      join.getTcpIpConfig().setEnabled(true);
      join.getTcpIpConfig().setMembers(Arrays.asList("127.0.0.1"));
    } else {
      join.getTcpIpConfig().setEnabled(false);
    }

    if (Boolean.valueOf(value("hz.cloud.active"))) {

      log.trace("Configuring hazelcast for AWS");

      join.getTcpIpConfig().setEnabled(false);

      AwsConfig awsConfig = new AwsConfig();
      awsConfig.setAccessKey(value("awsAccessKey"));
      awsConfig.setSecretKey(value("awsSecretKey"));
      awsConfig.setRegion(value("hz.cloud.region"));
      awsConfig.setTagKey("Cluster");
      awsConfig.setTagValue(value("hz.cloud.awsTag"));
      awsConfig.setEnabled(true);
      join.setAwsConfig(awsConfig);
    }

    log.trace("Leaving hazelcast configuration");
    return config;
  }

  /** Configuration for hazelcast client.
   * @return the hazelcast client config.
   */
  private ClientConfig clientConfig() {

    log.trace("Starting hazelcast client configuration");

    ClientConfig config = new ClientConfig();

    if (booleanValue("hz.cloud.active")) {

      ClientAwsConfig awsConfig = new ClientAwsConfig();
      awsConfig.setAccessKey(value("awsAccessKey"));
      awsConfig.setSecretKey(value("awsSecretKey"));
      awsConfig.setRegion(value("hz.cloud.region"));
      awsConfig.setTagKey("Cluster");
      awsConfig.setTagValue(value("hz.cloud.awsTag"));
      awsConfig.setEnabled(true);
      awsConfig.setHostHeader("ec2.amazonaws.com");
      awsConfig.setInsideAws(true);
      config.getNetworkConfig().setAwsConfig(awsConfig);

      GroupConfig gc;
      gc = new GroupConfig(value("hz.group.name"), value("hz.group.password"));
      config.setGroupConfig(gc);

    } else {
      if(Boolean.valueOf(value("hz.node.local"))) {
        Hazelcast.getOrCreateHazelcastInstance(localConfig());
        config.getNetworkConfig().addAddress("127.0.0.1:" + port());
      }
    }

    return config;
  }

  /** Retrieves a boolean value for the given key.
   * @param key the key.
   * @return true or false.
   */
  private Boolean booleanValue(final String key) {
    return value(key, Boolean.class);
  }

  /** Retrieves a boolean value for the given key.
   * @param key the key.
   * @return true or false.
   */
  private Boolean booleanValue(final String key, final boolean defaultValue) {
    return value(key, defaultValue, Boolean.class);
  }

  /** Retrieves the environment value for the given key.
   * @param key the key.
   * @return the value or null.
   */
  private String value(final String key) {
    return environment.getProperty(key);
  }

  /** Retrieves a key casted to the given type.
   * @param key the key.
   * @param type the type.
   * @return the value or null.
   */
  private <T> T value(final String key, final Class<T> type) {
    return environment.getProperty(key, type);
  }

  /** Retrieves a key casted to the given type with a default value if not set.
   * @param key the key.
   * @param defaultValue the default value.
   * @param type the type.
   * @return the value or null.
   */
  private <T> T value(final String key, final T defaultValue,
      final Class<T> type) {
    return environment.getProperty(key, type, defaultValue);
  }

  /** Returns the port configuration property. */
  public Integer port() {
    return value("hz.network.port", DEFAULT_PORT, Integer.class);
  }

}
