package org.waabox.banten.web;

import java.util.List;

import org.apache.commons.lang3.Validate;
import org.waabox.banten.core.ConfigurationApi;
import org.waabox.banten.core.InitContext;
import org.waabox.banten.core.ObjectFactoryBean;
import org.waabox.banten.core.WebModule;

/** The web configuration API.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class WebAppConfigurationApi extends ConfigurationApi {

  /** Register a weblet instance for a Module.
   * @param weblets the list of weblets to register, cannot be null.
   * @param module the module that this weblet belogns to, cannot be null.
   * @return this.
   */
  public WebAppConfigurationApi addWeblets(final List<Weblet> weblets,
      final WebModule module) {
    Validate.notNull(weblets, "The list of weblets cannot be null");
    Validate.notNull(module, "The module cannot be null");
    WebletContainer.instance().register(module, weblets);
    return this;
  }

  /** Sets the landing URL.
   * @param landingUrl the landing URL, cannot be null.
   * @return this.
   */
  public WebAppConfigurationApi setLandingUrl(final String landingUrl) {
    Validate.notNull(landingUrl, "The landing url cannot be null");
    ObjectFactoryBean.register(
        InitContext.beanDefinitionRegistry(), String.class, landingUrl,
        "banten.landingUrl");
    return this;
  }

}
