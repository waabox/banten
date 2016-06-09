package org.waabox.banten.elasticsearch;

import org.springframework.core.env.Environment;
import org.waabox.banten.core.Configurator;

/** Elasticsearch's {@link Configurator}.
 *
 * Adds extra keys:
 *  cloud.aws.access_key
 *  cloud.aws.secret_key
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class ElasticsearchConfigurator extends Configurator {

  /** Creates a new instance of the configurator.
   *
   * @param prefix the prefix for configurations.
   * @param env the spring environment.
   */
  public ElasticsearchConfigurator(final String prefix, final Environment env) {

    super(prefix, env);

    if(env().getProperty("es.cloud.active", Boolean.class, Boolean.FALSE)) {
      put("cloud.aws.access_key", env.getProperty("awsAccessKey"));
      put("cloud.aws.secret_key", env.getProperty("awsSecretKey"));
    } else {
      remove("cloud.aws.access_key");
      remove("cloud.aws.secret_key");
      remove("discovery.type");
      remove("cloud.aws.region");
      remove("discovery.ec2.groups");
      remove("cloud.aws.region");
    }

  }

  /** Retrieves the server host name.
   * @return the serverHostName
   */
  public String getServerHostName() {
    return env().getProperty("elasticsearch.server", String.class, "");
  }

  /** Retrieves the server port number.
   * @return the serverPortNumber
   */
  public Integer getServerPortNumber() {
    return env().getProperty("elasticsearch.port", Integer.class);
  }

  /** Checks whether or not should delete the index on startup.
   * @return true if should delete the index, by default false.
   */
  public boolean dropIndexOnStartup() {
    return env().getProperty("elasticsearch.dropIndexOnStartup",
        Boolean.class, Boolean.FALSE);
  }

  /** Checks whether or not this will be a local Elasticsearch's node.
   * @return true if will be a local node.
   */
  public boolean isLocalNode() {
    return env().getProperty("elasticsearch.nodelocal", Boolean.class,
        Boolean.TRUE);
  }

}
