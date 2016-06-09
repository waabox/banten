package org.waabox.banten.core;

/** Describes a Module.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public interface Module extends Registry {

  /** Retrieves the Module's name.
   * @return the name, never null.
   */
  String getName();

  /** Retrieves the public Module configuration.
   * @return the public configuration, can be null.
   */
  Class<?> getPublicConfiguration();

  /** Retrieves the configuration API.
   * @return the configuration API or null.
   */
  ConfigurationApi getConfigurationApi();
}
