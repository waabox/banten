package org.waabox.banten.sample.time;

import org.waabox.banten.core.ConfigurationApi;
import org.waabox.banten.core.ConfigurationApiRegistry;
import org.waabox.banten.core.WebModule;
import org.waabox.banten.hibernate.HibernateConfigurationApi;
import org.waabox.banten.hibernate.PersistenceUnit;
import org.waabox.banten.sample.time.domain.Time;
import org.waabox.banten.shiro.ShiroConfigurationApi;
import org.waabox.banten.shiro.UrlToRoleMapping;
import org.waabox.banten.web.menu.MenuConfigurationApi;

/** A simple time module.
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class TimeModule implements WebModule {

  /** {@inheritDoc}.*/
  @Override
  public String getName() {
    return "Time-Module";
  }

  /** {@inheritDoc}.*/
  @Override
  public String getNamespace() {
    return "time";
  }

  /** {@inheritDoc}.*/
  @Override
  public String getRelativePath() {
    return "../banten-sample";
  }

  /** {@inheritDoc}.*/
  @Override
  public Class<?> getMvcConfiguration() {
    return TimeMvc.class;
  }

  /** {@inheritDoc}.*/
  @Override
  public Class<?> getPublicConfiguration() {
    return TimeConfiguration.class;
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
          new PersistenceUnit(Time.class)
      );

    registry.get(MenuConfigurationApi.class)
      .root("Time", "/time")
      .node("List", "/time/time/view.html", "/time");

    registry.get(ShiroConfigurationApi.class)
      .register(new UrlToRoleMapping("/time/time/view.html", "time"));

  }

}
