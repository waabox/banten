/* vim: set et ts=2 sw=2 cindent fo=qroca: */

package org.waabox.banten.testsupport;

import java.io.*;
import java.util.*;

import org.springframework.beans.BeanUtils;

import org.springframework.boot.context.web.*;
import org.springframework.boot.context.web
  .ServerPortInfoApplicationContextInitializer;

import org.springframework.boot.test.*;

import org.springframework.context.*;

import static org.springframework.core.annotation.AnnotationUtils.*;
import org.springframework.core.env.*;

import org.springframework.core.io.FileSystemResourceLoader;

import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.*;

import org.springframework.test.context.support.*;

import org.springframework.test.context.web.*;

import org.springframework.util.*;

import org.springframework.web.context.support.GenericWebApplicationContext;
import org.waabox.banten.core.BantenApplication;

/** Spring application loader.
 *
 * See BantenTest for more information.
 *
 * This is used only during testing. Do not use this in your production code.
 * It is in org.waabox.banten.test so that BantenApplication.getApplication is
 * kept package access.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class BantenTestContextLoader extends AbstractContextLoader {

  @Override
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public ApplicationContext loadContext(
      final MergedContextConfiguration config) throws Exception {

    Class testClass = config.getTestClass();
    BantenTest test = (BantenTest) testClass.getAnnotation(BantenTest.class);
    Class<? extends BantenApplication> applicationClass;
    applicationClass = test.applicationClass();

    BantenApplication application =  applicationClass.newInstance();

    ConfigurableEnvironment environment = new StandardEnvironment();
    if (!ObjectUtils.isEmpty(config.getActiveProfiles())) {
      setActiveProfiles(environment, config.getActiveProfiles());
    }

    Map<String, Object> properties = getEnvironmentProperties(config);
    addProperties(environment, properties);

    List<ApplicationContextInitializer<?>> initializers;
    initializers = getInitializers(config, application);

    if (config instanceof WebMergedContextConfiguration) {
      new WebConfigurer().configure(config, application, initializers);
    } else {
      application.getApplication().setWebEnvironment(false);
    }

    application.getApplication().setInitializers(
        initializers);
    ConfigurableApplicationContext applicationContext;
    applicationContext = application.getApplication().run();

    return applicationContext;
  }

  private void setActiveProfiles(final ConfigurableEnvironment environment,
      final String[] profiles) {
    EnvironmentTestUtils.addEnvironment(environment, "spring.profiles.active="
        + StringUtils.arrayToCommaDelimitedString(profiles));
  }

  protected Map<String, Object> getEnvironmentProperties(
      final MergedContextConfiguration config) {
    Map<String, Object> properties = new LinkedHashMap<String, Object>();
    disableJmx(properties);
    properties.putAll(extractEnvironmentProperties(
        config.getPropertySourceProperties()));
    if (!isIntegrationTest(config.getTestClass())) {
      properties.putAll(getDefaultEnvironmentProperties());
    }
    return properties;
  }

  private void disableJmx(final Map<String, Object> properties) {
    properties.put("spring.jmx.enabled", "false");
  }

  final Map<String, Object> extractEnvironmentProperties(
      final String[] values) {
    // Instead of parsing the keys ourselves, we rely on standard handling
    if (values == null) {
      return Collections.emptyMap();
    }
    String content = StringUtils.arrayToDelimitedString(values,
        System.getProperty("line.separator"));
    Properties properties = new Properties();
    try {
      properties.load(new StringReader(content));
      return asMap(properties);
    } catch (IOException ex) {
      throw new IllegalStateException(
          "Unexpected could not load properties from '" + content + "'", ex);
    }
  }

  private Map<String, Object> asMap(final Properties properties) {
    Map<String, Object> map = new LinkedHashMap<String, Object>();
    for (String name : properties.stringPropertyNames()) {
      map.put(name, properties.getProperty(name));
    }
    return map;
  }

  private Map<String, String> getDefaultEnvironmentProperties() {
    return Collections.singletonMap("server.port", "-1");
  }

  private void addProperties(final ConfigurableEnvironment environment,
      final Map<String, Object> properties) {
    /* @IntegrationTest properties go before external
     * configuration and after system
     */
    environment.getPropertySources().addAfter(
        StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME,
        new MapPropertySource("integrationTest", properties));
  }

  private List<ApplicationContextInitializer<?>> getInitializers(
      final MergedContextConfiguration cfg,
      final BantenApplication application) {
    List<ApplicationContextInitializer<?>> initializers;
    initializers = new ArrayList<ApplicationContextInitializer<?>>();
    initializers.add(new PropertySourceLocationsInitializer(
        cfg.getPropertySourceLocations()));
    initializers.add(new ServerPortInfoApplicationContextInitializer());
    initializers.addAll(application.getApplication()
        .getInitializers());
    for (Class<? extends ApplicationContextInitializer<?>> initializerClass
            : cfg.getContextInitializerClasses()) {
      initializers.add(BeanUtils.instantiate(initializerClass));
    }
    return initializers;
  }

  @Override
  public void processContextConfiguration(
      final ContextConfigurationAttributes configAttributes) {
    if (!configAttributes.hasLocations() && !configAttributes.hasClasses()) {
      Class<?>[] defaultConfigClasses = detectDefaultConfigurationClasses(
          configAttributes.getDeclaringClass());
      configAttributes.setClasses(defaultConfigClasses);
    }
  }

  /**
   * Detect the default configuration classes for the supplied test class. By
   * default simply delegates to
   * {@link AnnotationConfigContextLoaderUtils
   *  #detectDefaultConfigurationClasses}
   * .
   *
   * @param declaringClass
   *          the test class that declared {@code @ContextConfiguration}
   * @return an array of default configuration classes, potentially empty but
   *         never {@code null}
   * @see AnnotationConfigContextLoaderUtils
   */
  protected Class<?>[] detectDefaultConfigurationClasses(
      final Class<?> declaringClass) {
    return AnnotationConfigContextLoaderUtils
        .detectDefaultConfigurationClasses(declaringClass);
  }

  @Override
  public ApplicationContext loadContext(
      final String... locations) throws Exception {
    throw new UnsupportedOperationException("SpringApplicationContextLoader "
        + "does not support the loadContext(String...) method");
  }

  @Override
  protected String getResourceSuffix() {
    return "-context.xml";
  }

  /**
   * Inner class to configure {@link WebMergedContextConfiguration}.
   */
  private static class WebConfigurer {

    private static final Class<GenericWebApplicationContext>
      WEB_CONTEXT_CLASS = GenericWebApplicationContext.class;

    void configure(final MergedContextConfiguration configuration,
        final BantenApplication application,
        final List<ApplicationContextInitializer<?>> initializers) {
      WebMergedContextConfiguration webConfiguration;
      webConfiguration = (WebMergedContextConfiguration) configuration;
      if (!isIntegrationTest(webConfiguration.getTestClass())) {
        addMockServletContext(initializers, webConfiguration);
        application.getApplication()
          .setApplicationContextClass(WEB_CONTEXT_CLASS);
      }
    }

    private void addMockServletContext(
        final List<ApplicationContextInitializer<?>> initializers,
        final WebMergedContextConfiguration webConfiguration) {
      MockServletContext servletContext = new MockServletContext(
          webConfiguration.getResourceBasePath(),
          new FileSystemResourceLoader());
      initializers.add(0,
          new ServletContextApplicationContextInitializer(servletContext));
    }

  }

  private static boolean isIntegrationTest(final Class<?> testClass) {
    return ((findAnnotation(testClass, IntegrationTest.class) != null)
        || (findAnnotation(testClass, WebIntegrationTest.class) != null));
  }

  /** {@link ApplicationContextInitializer} to setup test property
   *  source locations.
   */
  private static class PropertySourceLocationsInitializer
      implements
      ApplicationContextInitializer<ConfigurableApplicationContext> {

    private final String[] propertySourceLocations;

    public PropertySourceLocationsInitializer(
        final String[] aPropertySourceLocations) {
      propertySourceLocations = aPropertySourceLocations;
    }

    @Override
    public void initialize(
        final ConfigurableApplicationContext ctx) {
      TestPropertySourceUtils.addPropertiesFilesToEnvironment(ctx,
          propertySourceLocations);
    }
  }
}
