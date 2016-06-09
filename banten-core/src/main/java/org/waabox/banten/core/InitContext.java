package org.waabox.banten.core;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/** Initialization context that holds within a {@link ThreadLocal} values
 * that will be used in the Spring's initialization.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class InitContext {

  /** Thread local storage that holds the {@link BeanDefinitionRegistry}. */
  private static ThreadLocal<BeanDefinitionRegistry> registry;

  static {
    registry = new ThreadLocal<>();
  }

  /** Register the {@link BeanDefinitionRegistry} within the context.
   * @param aRegistry the {@link BeanDefinitionRegistry}, cannot be null.
   */
  public static void init(
      final BeanDefinitionRegistry aRegistry) {
    Validate.notNull(aRegistry, "the registry cannot be null");
    registry.set(aRegistry);
  }

  /** Retrieves the {@link BeanDefinitionRegistry}.
   * @return the {@link BeanDefinitionRegistry}, never null.
   */
  public static BeanDefinitionRegistry beanDefinitionRegistry() {
    return registry.get();
  }

  /** Cleans up the context.*/
  public static void destroy() {
    registry.remove();
  }

}
