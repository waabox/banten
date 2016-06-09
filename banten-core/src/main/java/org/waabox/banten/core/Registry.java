package org.waabox.banten.core;

/** A module registry is the ability to interacts with the
 * {@link ConfigurationApiRegistry} in order to work with
 * another {@link ConfigurationApi}.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public interface Registry {

  /** Register within the context this module.
   *
   * This is not part of Spring, so you cannot inject beans here, or something
   * related to the spring context.
   *
   * This is just a hook between {@link ConfigurationApiRegistry} in order
   * to share specific configuration.
   *
   * @param registry the {@link ConfigurationApiRegistry}, never null.
   */
  void init(final ConfigurationApiRegistry registry);

}