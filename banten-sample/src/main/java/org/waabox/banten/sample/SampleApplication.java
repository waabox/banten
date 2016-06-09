package org.waabox.banten.sample;

import org.springframework.boot.context.embedded.jetty
  .JettyEmbeddedServletContainerFactory;

import org.springframework.context.annotation.Bean;
import org.waabox.banten.core.BantenApplication;
import org.waabox.banten.core.Bootstrap;
import org.waabox.banten.core.ConfigurationApiRegistry;
import org.waabox.banten.hibernate.HibernateModule;
import org.waabox.banten.login.LoginConfigurationApi;
import org.waabox.banten.login.LoginModule;
import org.waabox.banten.sample.time.TimeModule;
import org.waabox.banten.sample.user.UserModule;
import org.waabox.banten.shiro.ShiroModule;
import org.waabox.banten.web.WebAppConfigurationApi;
import org.waabox.banten.web.WebAppModule;
import org.waabox.banten.web.menu.MenuModule;
import org.waabox.banten.web.sitemesh.SitemeshConfigurationApi;
import org.waabox.banten.web.sitemesh.SitemeshModule;

/** The Sample application Factory.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class SampleApplication extends BantenApplication {

  /** The application's port.*/
  private static final int APPLICATION_PORT = 8080;

  /** {@inheritDoc}.*/
  @Override
  protected Bootstrap bootstrap() {
    return new Bootstrap(
        HibernateModule.class,
        WebAppModule.class,
        SitemeshModule.class,
        MenuModule.class,
        LoginModule.class,
        ShiroModule.class,
        // Domain Bootstrap.
        TimeModule.class,
        UserModule.class
    );
  }

  /** Retrieves the Jetty Factory. This factory can be used to configure the
   * Jetty Server.
   * @return the factory, never null.
   */
  @Bean public JettyEmbeddedServletContainerFactory jetty() {
    return new JettyEmbeddedServletContainerFactory("", APPLICATION_PORT);
  }

  /** Creates a test account for development.
   * @return a {@link SampleLoginAccount}
   */
  @Bean public SampleLoginAccount sampleLoginAccount() {
    return new SampleLoginAccount();
  }

  /** {@inheritDoc}.*/
  @Override
  public void init(final ConfigurationApiRegistry registry) {
    registry.get(LoginConfigurationApi.class)
      .successUrl("/users/users/list.html");

    registry.get(WebAppConfigurationApi.class)
      .setLandingUrl("/users/users/list.html");

    registry.get(SitemeshConfigurationApi.class)
      .configure("../banten-sample", "classpath:decorators/");
  }

}
