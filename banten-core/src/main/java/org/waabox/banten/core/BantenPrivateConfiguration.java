package org.waabox.banten.core;

import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.*;
import org.springframework.context.support.*;

import org.springframework.core.env.Environment;

import org.springframework.web.servlet.config.annotation.*;

/** The banten base configuration for the module private spring application
 * context.
 *
 * This configuration defines beans that are available in the dispatcher
 * servlet application context. See BantenApplication for more information.
 */
@Configuration
public class BantenPrivateConfiguration extends WebMvcConfigurationSupport {

  /** The log. */
  private static Logger log = getLogger(BantenPrivateConfiguration.class);

  /** The spring environment, never null.*/
  @Autowired private Environment environment;

  /** The module description bean that 'owns' the current application
   * context, never null.
   */
  @Autowired private ModuleDescription moduleDescription;

  /** Bean that supports ${property} in @Value annotations.
   *
   * @return the configurer that replaces ${property} with the corresponding
   * environment values. Never returns null.
   */
  @Bean public static PropertySourcesPlaceholderConfigurer
      propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }

  /** {@inheritDoc}
   *
   * This implementation makes the module serve static content from the
   * module classpath.
   */
  @Override
  protected void addResourceHandlers(final ResourceHandlerRegistry registry) {
    boolean debugMode;
    debugMode = environment.getProperty("debugMode", Boolean.class, false);

    ResourceHandlerRegistration registration;
    registration = registry.addResourceHandler("/static/**");

    if (debugMode) {
      registration.addResourceLocations(
          "file:" + moduleDescription.getRelativePath()
          + "/src/main/resources/" + moduleDescription.getClasspath()
          + "/static/");
    }

    registration.addResourceLocations("classpath:/"
        + moduleDescription.getClasspath() + "/static/");

    log.info("classpath:/" + moduleDescription.getClasspath() + "/static/");
  }
}
