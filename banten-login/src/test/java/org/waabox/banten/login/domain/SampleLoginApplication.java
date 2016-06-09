package org.waabox.banten.login.domain;

import org.waabox.banten.core.BantenApplication;
import org.waabox.banten.core.Bootstrap;
import org.waabox.banten.core.ConfigurationApiRegistry;
import org.waabox.banten.hibernate.HibernateModule;
import org.waabox.banten.login.LoginModule;
import org.waabox.banten.shiro.ShiroConfigurationApi;
import org.waabox.banten.shiro.ShiroModule;
import org.waabox.banten.shiro.UrlToRoleMapping;

public class SampleLoginApplication extends BantenApplication {

  @Override
  protected Bootstrap bootstrap() {
    return new Bootstrap(
        HibernateModule.class,
        ShiroModule.class,
        LoginModule.class
    );
  }

  @Override
  public void init(final ConfigurationApiRegistry registry) {
    registry.get(ShiroConfigurationApi.class).register(
        new UrlToRoleMapping("/foo", "foo", "another"));
  }

}
