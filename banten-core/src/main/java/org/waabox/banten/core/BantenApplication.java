package org.waabox.banten.core;

import org.slf4j.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.support.*;

import org.springframework.boot.*;

import org.springframework.context.*;

/** Bootstrap the Banten's Application.
 *
 * The application creates a {@link SpringApplication} using the {@link Module}
 * as {@link Configuration} as entry points.
 *
 * Each {@link Module} defines public beans within the
 * {@link Module#getModuleConfiguration}, those one will be merge into the
 * main application context. However, configuration declared within the
 * {@link Module#getMvcConfiguration()} will be isolated; The main or
 * parent {@link ApplicationContext} is the same where {@link Module}s has
 * been declared.
 *
 * The {@link ApplicationContext} looks like:
 *
 *<code>
 *                /[>>>Parent Context<<<]\
 *               |                        |
 *               |                        |
 *       [Module A(public)]       [Module B (public)] ...
 *           |                                  |
 *  [MVC Context (private)]            [MVC Context (private)]
 *
 *</code>
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public abstract class BantenApplication implements Registry {

  /** The log. */
  private final Logger log = LoggerFactory.getLogger(BantenApplication.class);

  /** The spring boot application, null until the method
   * {@link BantenApplication#getApplication()} is called.
   */
  private SpringApplication application = null;

  /** The {@link BantenContext}, it's never null. */
  private final BantenContext bantenContext;

  /** Creates a new instance of the {@link BantenApplication}.*/
  public BantenApplication() {
    bantenContext = new BantenContext(bootstrap());
  }

  /** Gets the currently wrapped spring boot application, creating one if not
   * yet created.
   *
   * This is not serialized, so don't attempt to call this from a
   * multi-threaded context. This is package access so that it can be used from
   * BantenApplicationContextLoader, a test utility.
   *
   * @return the Spring Application, never null.
   */
  public SpringApplication getApplication() {

    if (application != null) {
      return application;
    }

    application = new SpringApplication(getClass());

    application.addInitializers(
        new ApplicationContextInitializer<ConfigurableApplicationContext>() {

      /** Initializes the application context.
       * @param parent the parent {@link ConfigurableApplicationContext}.
       */
      @Override
      public void initialize(final ConfigurableApplicationContext parent) {

        bantenContext.registerParentApplicationContext(parent);

        BeanDefinitionRegistry registry;
        registry = (BeanDefinitionRegistry) parent;

        registerCoreBeans(registry);
        registerModules(registry);
      }

    });

    log.info("Finish application configuration, starting Spring's context");

    return application;
  }

  /** Run the Spring's Application.
   *
   * This action creates and refresh the new application context.
   *
   * @param args the command line arguments.
   * @return the new Spring's {@link ApplicationContext}.
   */
  public final ConfigurableApplicationContext run(final String[] args) {
    return getApplication().run(args);
  }

  /** Register the modules within the Spring's root application context, the
   * performs the {@link ConfigurationApi}'s initialization.
   *
   * @param registry the {@link BeanDefinitionRegistry}.
   * @throws Error if cannot create the {@link Module} instance,
   */
  private void registerModules(final BeanDefinitionRegistry registry) {
    log.info("Registering BantenContext Bootstrap");

    InitContext.init(registry);

    // Register the beans into the Application Context.
    for (Module aModule : bantenContext.getModules()) {
      log.info("Registering: {}" , aModule.getName());
      registerPublicConfiguration(aModule, registry);
      registerPrivateConfiguration(aModule, registry);
      registerApi(registry, aModule);
    }

    // Initializes the modules
    for (Module aModule : bantenContext.getModules()) {
      log.info("Initializing the module: {}" , aModule.getName());
      aModule.init(bantenContext.getModuleRegistry());
    }

    // Callback for configure modules at 'application level'.
    init(bantenContext.getModuleRegistry());

    // Initialize the registry for each module.
    bantenContext.initRegistry();

    InitContext.destroy();

    log.info("Finish the registration of the Banten's modules");
  }

  /** Register the Module.
   *
   * @param registry the bean definition registry. It cannot be null.
   *
   * @param module the module to register. It cannot be null.
   */
  private void registerPublicConfiguration(
      final Module module,
      final BeanDefinitionRegistry registry) {
    if (module.getPublicConfiguration() != null) {
      BeanDefinition moduleDef = new AnnotatedGenericBeanDefinition(
          module.getPublicConfiguration());
      moduleDef.setLazyInit(true);
      registry.registerBeanDefinition(module.getName(), moduleDef);
    }
  }

  /** Register the Module's configuration API if any.
   * @param registry the bean definition registry.
   * @param module the module.
   */
  private void registerApi(final BeanDefinitionRegistry registry,
      final Module module) {
    ConfigurationApi api = module.getConfigurationApi();
    if (api != null) {
      bantenContext.register(api);
      ObjectFactoryBean.register(
          registry,
          api.getClass(),
          api, module.getName() + "." + api.getClass().getName()
      );
    }
  }

  /** Register the module private configuration.
   *
   * This creates a dispatcher Servlet with a spring context configured with
   * the module private configuration if the module defines such private
   * configuration.
   *
   * @param registry the bean definition registry. It cannot be null.
   *
   * @param module the module to register. It cannot be null.
   */
  private void registerPrivateConfiguration(
      final Module module, final BeanDefinitionRegistry registry) {

    if (!(module instanceof WebModule)) {
      return;
    }

    BantenWebApplicationContext context;
    context = new BantenWebApplicationContext((WebModule) module);
    context.register(registry);

    bantenContext.register(context);
  }

  /** Register the BantenContext's core beans in the provided bean definition
   * registry.
   *
   * The provided registry should correspond to the public application context,
   * so this operation adds the BantenContext's core beans to the public
   * application context.
   *
   * @param registry the bean definition registry. It cannot be null.
   */
  private void registerCoreBeans(final BeanDefinitionRegistry registry) {
    log.info("Registering core beans");

    ObjectFactoryBean.register(registry, BantenContext.class,
        bantenContext, "banten.bantenContext");

    GenericBeanDefinition coreBean = new GenericBeanDefinition();
    coreBean.setBeanClass(CoreBeansConfiguration.class);
    registry.registerBeanDefinition(
        CoreBeansConfiguration.class.getName(), coreBean);
  }

  /** Retrieves the {@link Bootstrap} for this application.
   *
   * @return the {@link Bootstrap}, never null.
   */
  protected abstract Bootstrap bootstrap();

}
