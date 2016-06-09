package org.waabox.banten.core;

import org.slf4j.*;
import org.springframework.context.ApplicationContext;

import java.util.*;

import org.apache.commons.lang3.Validate;

/** BantenContext holds the initialized {@link Module}s and also its
 * {@link ConfigurationApi}.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class BantenContext {

  /** The log. */
  private final Logger log = LoggerFactory.getLogger(BantenContext.class);

  /** The list of initialized modules, it's never null.*/
  private final List<Module> modules;

  /** The module registry, it's never null. */
  private final ConfigurationApiRegistry moduleRegistry;

  /** Holds a key value with the registered web modules.*/
  private final Map<Class<?>, BantenWebApplicationContext> contexts;

  /** The parent context.*/
  private ApplicationContext parentContext;

  /** Creates a new instance of the {@link BantenContext}.
   * @param bootstrap the {@link Bootstrap} that holds the modules.
   */
  public BantenContext(final Bootstrap bootstrap) {

    Validate.notNull(bootstrap, "The list of modules cannot be null");
    Validate.notEmpty(bootstrap, "The list of modules cannot be empty");

    moduleRegistry = new ConfigurationApiRegistry();
    modules = new LinkedList<>();
    contexts = new HashMap<>();

    for(Class<? extends Module> moduleClass : bootstrap) {
      try {
        modules.add(moduleClass.newInstance());
      } catch (Exception e) {
        log.error("Cannot instantiate the module: {}", moduleClass);
        log.error("{} should have an empty constructor", moduleClass);
        throw new Error(e);
      }
    }

  }

  /** Retrieves the list of {@link Module}s.
   * @return an unmodifiable list of {@link Module}s.
   */
  public List<Module> getModules() {
    return Collections.unmodifiableList(modules);
  }

  /** Retrieves the moduleRegistry.
   * @return the moduleRegistry
   */
  public ConfigurationApiRegistry getModuleRegistry() {
    return moduleRegistry;
  }

  /** Initializes the {@link ConfigurationApiRegistry}.*/
  void initRegistry() {
    moduleRegistry.init();
  }

  /** Registers the parent {@link ApplicationContext}.
   * @param ctx the {@link ApplicationContext} cannot be null.
   */
  void registerParentApplicationContext(final ApplicationContext ctx) {
    parentContext = ctx;
  }

  /** Registers the {@link BantenWebApplicationContext} into this context.
   * @param context the {@link BantenWebApplicationContext}, cannot be null.
   */
  void register(final BantenWebApplicationContext context) {
    contexts.put(context.getModule().getClass(), context);
  }

  /** Registers a new {@link ConfigurationApi} into the {@link BantenContext}.
   * @param api the {@link ConfigurationApi}, cannot be null.
   */
  void register(final ConfigurationApi api) {
    moduleRegistry.register(api);
  }

  /** Retrieves the {@link BantenWebApplicationContext} for the given
   * {@link Module} class.
   * @param module the {@link Module} class, cannot be null.
   * @return the {@link BantenWebApplicationContext} or null.
   */
  public BantenWebApplicationContext getWebApplicationContext(
      final Class<? extends Module> module) {
    return contexts.get(module);
  }

  /** Retrieves a bean declared in {@link WebModule#getMvcConfiguration()} by
   * its type.
   * @param module the {@link Module}, cannot be null.
   * @param type the bean type, cannot be null.
   * @return the bean instance, or null.
   */
  public <T> T getBean(
      final Class<? extends Module> module, final Class<T> type) {
    Validate.notNull(module, "The module class cannot be null");
    Validate.notNull(type, "The type class cannot be null");

    BantenWebApplicationContext ctx = contexts.get(module);

    Validate.notNull(ctx, "Context for: " + module.getName() + " not found");

    return (T) ctx.getBean(type);
  }

  /** Retrieves a bean declared in {@link WebModule#getMvcConfiguration()} by
   * its name.
   * @param module the {@link Module}, cannot be null.
   * @param type the bean type, cannot be null.
   * @return the bean instance, or null.
   */
  @SuppressWarnings("unchecked")
  public <T> T getBean(
      final Class<? extends Module> module,
      final String beanName,
      final Class<?> type) {
    Validate.notNull(module, "The module class cannot be null");
    Validate.notNull(type, "The type class cannot be null");

    BantenWebApplicationContext ctx = contexts.get(module);

    Validate.notNull(ctx, "Context for: " + module.getName() + " not found");

    return (T) ctx.getBean(beanName);
  }

  /** Retrieves a bean declared in the parent {@link ApplicationContext} by
   * its type.
   * @param module the {@link Module}, cannot be null.
   * @param type the bean type, cannot be null.
   * @return the bean instance, or null.
   */
  public <T> T getBean(final Class<T> type) {
    Validate.notNull(type, "The type class cannot be null");
    return (T) parentContext.getBean(type);
  }

  /** Retrieves a bean declared in the parent {@link ApplicationContext} by
   * its name.
   * @param type the bean type, cannot be null.
   * @return the bean instance, or null.
   */
  @SuppressWarnings("unchecked")
  public <T> T getBean(final String beanName, final Class<?> type) {
    Validate.notNull(type, "The type class cannot be null");
    return (T) parentContext.getBean(beanName);
  }

}
