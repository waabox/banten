package org.waabox.banten.core;

import java.util.*;

import org.apache.commons.lang3.Validate;

/** Holds the {@link ConfigurationApi} implementations.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public final class ConfigurationApiRegistry {

  /** Map that holds the class and implementation of the
   * {@link ConfigurationApi}s.
   */
  private final Map<Class<? extends ConfigurationApi>, ConfigurationApi> apis;

  /** Creates a new instance of the Registry.*/
  ConfigurationApiRegistry() {
    apis = new HashMap<>();
  }

  /** Registers the configuration API.
   *
   * @param api the {@link ConfigurationApi}'s implementation.
   */
  void register(final ConfigurationApi api) {
    apis.put(api.getClass(), api);
  }

  /** Initializes the APIs.*/
  void init() {
    Collection<ConfigurationApi> cfgApis = apis.values();
    for(ConfigurationApi cfg : cfgApis) {
      cfg.init();
    }
  }

  /** Retrieves the configuration API instance.
   *
   * @param configurationApiClass the configuration API to get.
   * @return the configuration API instance, never null.
   */
  @SuppressWarnings("unchecked")
  public <T> T get(final Class<T> configurationApiClass) {
    T api = (T) apis.get(configurationApiClass);
    Validate.notNull(api, "Api not found: "
        + configurationApiClass.getName() + " "
        + "check you application Bootstrap configuration");
    return api;
  }

}
