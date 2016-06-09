package org.waabox.banten.core;

import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;

import org.springframework.beans.MutablePropertyValues;

import org.springframework.beans.factory.config.ConstructorArgumentValues;

import org.springframework.beans.factory.support.*;

import org.springframework.boot.context.embedded.ServletRegistrationBean;

import org.springframework.core.env.MapPropertySource;

import org.springframework.web.context.support.*;
import org.springframework.web.servlet.DispatcherServlet;

/** Specialization of the {@link AnnotationConfigWebApplicationContext} that
 * configures the {@link WebModule} in order to set the module classpath and
 * its private configuration.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class BantenWebApplicationContext
  extends AnnotationConfigWebApplicationContext {

  private final Logger log = getLogger(BantenWebApplicationContext.class);

  /** The web module for this web application context, it's never null. */
  private final WebModule module;

  /** The module classpath, it's never null.*/
  private final String classpath;

  /** Creates a new instance of the
   *  {@link AnnotationConfigWebApplicationContext}.
   * @param webModule the BantenContext's web module, cannot be null.
   */
  BantenWebApplicationContext(final WebModule webModule) {
    Validate.notNull(webModule, "The module cannot be null");

    Validate.notNull(webModule.getMvcConfiguration(),
        "WebModules must declare a private configuration.");
    Validate.notNull(webModule.getNamespace(),
        "WebModules must declare a namespace.");
    Validate.notNull(webModule.getRelativePath(),
        "WebModules must declare a relative path.");

    log.info("Registering Private configuration for: {}", webModule.getName());

    module = webModule;
    classpath = webModule.getClass().getPackage().getName().replace(".", "/");

    register(BantenPrivateConfiguration.class,
        module.getMvcConfiguration());

    Map<String, Object> properties = new HashMap<>();
    properties.put("banten.moduleClasspath", classpath);

    MapPropertySource source;
    source = new MapPropertySource("module.properties", properties);
    getEnvironment().getPropertySources().addFirst(source);
  }


  /** {@inheritDoc}.
   *
   * Hack to create the BantenContext's module description.
   *
   * The {@link BantenWebApplicationContext} is not a
   * {@link BeanDefinitionRegistry}, so we override the loadDefinitions
   * to create a singleton for the module description.
   */
  @Override
  protected void loadBeanDefinitions(final DefaultListableBeanFactory factory) {
   super.loadBeanDefinitions(factory);
   ModuleDescription description = new ModuleDescription(
       module.getName(),
       classpath,
       module.getNamespace(),
       module.getRelativePath()
   );
   factory.registerSingleton("banten.moduleDescription", description);
  }

  /** Retrieves the name for the private Spring context.
   * @return the name, never null.
   */
  private String getName() {
    return "private-" + module.getName();
  }

  /** Retrieves the {@link Module}.
   * @return the {@link Module}, never null.
   */
  public WebModule getModule() {
    return module;
  }

  /** Creates a new {@link GenericBeanDefinition} that holds a
   * {@link ServletRegistrationBean} that configures the
   * {@link DispatcherServlet} that works for this {@link WebModule}.
   *
   * Then, register the generated {@link GenericBeanDefinition} into the
   * Spring's context.
   */
  public void register(final BeanDefinitionRegistry registry) {
    /*
     * Let the dispatcher Servlet initialize the context.
     * The dispatcher Servlet will set the parent.
     * Then will do its magic on the context and call refresh.
     */
    DispatcherServlet dispatcherServlet;
    dispatcherServlet = new DispatcherServlet(this);

    /*
     * Create a bean definition for a bean of type ServletRegistrationBean
     * that registers the dispatcherServlet in the web context. We cannot
     * add this to the BantenPrivateConfiguration because we manually create
     * the dispatcher Servlet.
     */
    ConstructorArgumentValues constructor;
    constructor = new ConstructorArgumentValues();
    constructor.addIndexedArgumentValue(0, dispatcherServlet);
    constructor.addIndexedArgumentValue(1, "/" + module.getNamespace() + "/*");

    MutablePropertyValues beanNamePropertyValue = new MutablePropertyValues();
    beanNamePropertyValue.add("name", getName());

    GenericBeanDefinition servletRegistrationBean;
    servletRegistrationBean = new GenericBeanDefinition();
    servletRegistrationBean.setBeanClass(ServletRegistrationBean.class);
    servletRegistrationBean.setConstructorArgumentValues(constructor);
    servletRegistrationBean.setPropertyValues(beanNamePropertyValue);
    servletRegistrationBean.setLazyInit(true);

    registry.registerBeanDefinition(getName(), servletRegistrationBean);
  }

}
