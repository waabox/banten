package org.waabox.banten.hibernate;

import org.waabox.banten.core.BantenApplication;
import org.waabox.banten.core.Bootstrap;
import org.waabox.banten.core.ConfigurationApiRegistry;
import org.waabox.banten.hibernate.HibernateModule;

/** Database test module application.
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class DatabaseTestModuleApplication extends BantenApplication {

  @Override
  protected Bootstrap bootstrap() {
    return new Bootstrap(
        HibernateModule.class,
        DatabaseTestModule.class
     );
  }

  @Override
  public void init(final ConfigurationApiRegistry registry) {
  }

}

