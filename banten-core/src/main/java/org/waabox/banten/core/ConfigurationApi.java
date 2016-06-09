package org.waabox.banten.core;

/** Provides hooks in order to allow {@link Module}s to extends
 * its configuration to another modules.
 *
 * @see Module#getConfigurationApi()
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public abstract class ConfigurationApi {

  /** Registers a Bean into global Spring's Application context.
   * @param builder the bean builder, cannot be null.
   */
  protected void registerBean(final BeanBuilder builder) {
    builder.register(InitContext.beanDefinitionRegistry());
  }

  /** Initialize the configuration API.*/
  protected void init() {
  }

}

