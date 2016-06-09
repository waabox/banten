package org.waabox.banten.core;

import java.util.*;

import org.apache.commons.lang3.*;

import org.springframework.core.env.*;

/** Holds properties that match the given prefix and returns a map that remove
 * that prefix.
 * For example, given the defined property
 * platformElasticsearch.cloud.aws.access_key=<value>
 * will return
 * cloud.aws.access_key=<value>
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class Configurator {

  /** The prefix. */
  private final String prefix;

  /** The spring environment. */
  private final Environment environment;

  /** Map that holds the properties. */
  private final Map<String, String> properties;

  /** Creates a new instance of the Configurator.
   * @param thePrefix the subfix to search, cannot be null.
   * @param env the environment, cannot be null.
   */
  public Configurator(final String thePrefix, final Environment env) {
    Validate.notNull(thePrefix, "The subfix cannot be null");
    Validate.notNull(env, "The Environment cannot be null");
    prefix = thePrefix;
    environment = env;
    properties = new LinkedHashMap<>();
    AbstractEnvironment realEnvironment = (AbstractEnvironment) environment;
    MutablePropertySources sources = realEnvironment.getPropertySources();
    for (PropertySource<?> pSource : sources) {
      if (pSource instanceof EnumerablePropertySource) {
        EnumerablePropertySource<?> pps;
        pps = (EnumerablePropertySource<?>) pSource;
        for (String propertyName : pps.getPropertyNames()) {
          if (StringUtils.isNotBlank(propertyName)) {
            if (propertyName.startsWith(prefix)) {
              properties.put(propertyName.replace(prefix + ".", ""),
                  (String) env.getProperty(propertyName));
            }
          }
        }
      }
    }
  }

  /** Adds a new value within the configuration.
   * @param key the key.
   * @param value the value.
   */
  protected void put(final String key, final String value) {
    properties.put(key, value);
  }

  /** Retrieves the properties that matches with the given prefix.
   * @return the map of properties.
   */
  public Map<String, String> get() {
    return properties;
  }

  /** Retrieves the environment property
   * @param key the key to lookup.
   * @return the value for the given key.
   */
  protected String get(final String key) {
    return environment.getProperty(key);
  }

  /** Retrieves the environment property
   * @param key the key to lookup.
   * @return the value for the given key.
   */
  protected <T> T get(final String key, final Class<T> type) {
    return environment.getProperty(key, type);
  }

  /** Retrieves the environment property as boolean.
   * @param key the key.
   * @return the boolean value for the given key.
   */
  protected Boolean getBoolean(final String key) {
    return get(key, Boolean.class);
  }

  /** Retrieves the environment property as Integer.
   * @param key the key.
   * @return the Integer value for the given key.
   */
  protected Integer getInt(final String key) {
    return get(key, Integer.class);
  }

  /** Removes the given key.
   * @param key the key to remove.
   */
  protected void remove(final String key) {
    properties.remove(key);
  }

  /** Retrieves the Spring's environment.
   * @return the Spring's environment.
   */
  protected Environment env() {
    return environment;
  }

}
