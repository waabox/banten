package org.waabox.banten.core;

import org.springframework.context.*;

/** Service locator that interacts with the Spring's root application context.
 *
 * NEVER USE THIS IN YOUR CODE!!!!
 *
 * This is sometimes necessary when integrating a 3rd party that is not
 * 'spring friendly'.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class ModuleServiceLocator implements ApplicationContextAware {

  /** The service locator. */
  private static ModuleServiceLocator serviceLocator;

  /** The spring application context, it's never null once the
   * Spring App context has been initialized.
   */
  private ApplicationContext context;

  /** {@inheritDoc}.*/
  @Override
  public void setApplicationContext(final ApplicationContext ctx) {
    context = ctx;
    serviceLocator = this;
  }

  /** Retrieves a bean given by its class.
   * @param bean class<T> the class name for the requested bean,
   *  cannot be null.
   * @param <T> the type of bean.
   * @return the requested bean instance.
   */
  public static <T> T getBean(final Class<T> bean) {
    return serviceLocator.context.getBean(bean);
  }

  /** Retrieves a bean given by its name.
   * @param bean the name for the requested bean, cannot be null.
   * @param <T> the type of bean.
   * @return the requested bean instance.
   */
  @SuppressWarnings("unchecked")
  public static <T> T getBean(final String bean) {
    return ((T) serviceLocator.context.getBean(bean));
  }

}
