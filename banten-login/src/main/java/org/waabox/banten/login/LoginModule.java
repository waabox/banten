package org.waabox.banten.login;

import org.waabox.banten.core.ConfigurationApi;
import org.waabox.banten.core.ConfigurationApiRegistry;
import org.waabox.banten.core.WebModule;
import org.waabox.banten.hibernate.HibernateConfigurationApi;
import org.waabox.banten.hibernate.PersistenceUnit;
import org.waabox.banten.login.domain.Role;
import org.waabox.banten.login.domain.User;
import org.waabox.banten.login.shiro.BantenLoginRealm;
import org.waabox.banten.shiro.ShiroConfigurationApi;

/** Login Module description.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class LoginModule implements WebModule {

  /** {@inheritDoc}.*/
  @Override
  public String getName() {
    return "Login-Module";
  }

  /** {@inheritDoc}.*/
  @Override
  public Class<?> getPublicConfiguration() {
    return LoginConfiguration.class;
  }

  /** {@inheritDoc}.*/
  @Override
  public ConfigurationApi getConfigurationApi() {
    return new LoginConfigurationApi();
  }

  /** {@inheritDoc}.*/
  @Override
  public void init(final ConfigurationApiRegistry registry) {

    registry.get(ShiroConfigurationApi.class)

      .configureViews(
          "/login/web/form.html",
          registry.get(LoginConfigurationApi.class).getSuccessUrl(),
          "/login/web/unauthorized.html")

      .registerRealm(BantenLoginRealm.class);

    registry.get(HibernateConfigurationApi.class)
      .persistenceUnits(
          new PersistenceUnit(User.class),
          new PersistenceUnit(Role.class)
       );
  }

  /** {@inheritDoc}.*/
  @Override
  public String getNamespace() {
    return "login";
  }

  /** {@inheritDoc}.*/
  @Override
  public String getRelativePath() {
    return "../banten-login";
  }

  /** {@inheritDoc}.*/
  @Override
  public Class<?> getMvcConfiguration() {
    return LoginMvc.class;
  }

}
