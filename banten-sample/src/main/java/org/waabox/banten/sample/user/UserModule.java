package org.waabox.banten.sample.user;

import java.util.Arrays;

import org.waabox.banten.core.ConfigurationApi;
import org.waabox.banten.core.ConfigurationApiRegistry;
import org.waabox.banten.core.WebModule;
import org.waabox.banten.hibernate.HibernateConfigurationApi;
import org.waabox.banten.hibernate.PersistenceUnit;
import org.waabox.banten.sample.user.domain.User;
import org.waabox.banten.sample.user.domain.UserFactory;
import org.waabox.banten.shiro.ShiroConfigurationApi;
import org.waabox.banten.shiro.UrlToRoleMapping;
import org.waabox.banten.web.WebAppConfigurationApi;
import org.waabox.banten.web.Weblet;
import org.waabox.banten.web.menu.MenuConfigurationApi;

/** The user module.
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class UserModule implements WebModule {

  /** {@inheritDoc}.*/
  @Override
  public String getName() {
    return "User-Module";
  }

  /** {@inheritDoc}.*/
  @Override
  public String getNamespace() {
    return "users";
  }

  /** {@inheritDoc}.*/
  @Override
  public String getRelativePath() {
    return "../banten-sample";
  }

  /** {@inheritDoc}.*/
  @Override
  public Class<?> getMvcConfiguration() {
    return UserMvc.class;
  }

  /** {@inheritDoc}.*/
  @Override
  public Class<?> getPublicConfiguration() {
    return UserConfiguration.class;
  }

  /** {@inheritDoc}.*/
  @Override
  public ConfigurationApi getConfigurationApi() {
    return null;
  }

  /** {@inheritDoc}.*/
  @Override
  public void init(final ConfigurationApiRegistry registry) {

    registry.get(HibernateConfigurationApi.class)
      .persistenceUnits(
          new PersistenceUnit(User.class, UserFactory.class)
     );

    registry.get(WebAppConfigurationApi.class)
      .addWeblets(
          Arrays.asList(
              new Weblet("samplepicture", "users/samplePicture.html")
          ), this);

    registry.get(MenuConfigurationApi.class)
      .root("Users", "/users")
      .node("List", "/users/users/list.html", "/users");

    registry.get(ShiroConfigurationApi.class)
      .register(new UrlToRoleMapping("/users/users/list.html", "admin"));

  }

}
