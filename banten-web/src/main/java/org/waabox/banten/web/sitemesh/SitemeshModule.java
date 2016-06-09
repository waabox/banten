package org.waabox.banten.web.sitemesh;

import org.waabox.banten.core.ConfigurationApi;
import org.waabox.banten.core.ConfigurationApiRegistry;
import org.waabox.banten.core.Module;

/** Banten module to integrate sitemesh in your application.
 *
 * For information on sitemesh, see http://www.sitemesh.org.
 *
 * This module defines two decorators, one for anonymous users and another one
 * for logged in users. It does not decorate modals and api calls (see
 * BantenSitemeshDecoratorSelector for more information on the decorator
 * selection). You must implement two decorators, as freemarker files named
 * decorator.ftl and decoratorAnonymous.ftl.
 *
 * To configure this module, create a bean of type SitemeshConfiguration. You
 * can create that bean in your application or in the public configuration of a
 * module. The SitemeshConfiguration specifies the location of the decorators
 * and the file system relative location of that location.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class SitemeshModule implements Module {

  /** {@inheritDoc}.*/
  @Override
  public String getName() {
    return "Sitemesh-Module";
  }

  /** {@inheritDoc}.*/
  @Override
  public Class<?> getPublicConfiguration() {
    return SitemeshPublicConfiguration.class;
  }

  /** {@inheritDoc}.*/
  @Override
  public void init(final ConfigurationApiRegistry registry) {
  }

  /** {@inheritDoc}.*/
  @Override
  public ConfigurationApi getConfigurationApi() {
    return new SitemeshConfigurationApi();
  }
}
