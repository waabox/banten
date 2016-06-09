package org.waabox.banten.web.menu;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.waabox.banten.shiro.ShiroConfigurationApi;

import org.waabox.banten.web.freemarker.FreemarkerMvc;
import org.waabox.banten.web.freemarker.FreemarkerPaths;

import org.waabox.banten.web.menu.application.MenuController;

/** Menu MVC configuration.
 * @author waabox (waabox[at]gmail[dot]com)
 */
@Configuration
public class MenuMvc extends FreemarkerMvc {

  /** Retrieves the {@link MenuController}.
   * @return the {@link MenuController}, never null.
   */
  @Bean public MenuController menuController(
      final MenuConfigurationApi api,
      final MenuSecurityFilter filter) {
    return new MenuController(api.get(), filter);
  }

  @Bean public SecuredUrlService urlService(final ShiroConfigurationApi api) {
    return new SecuredUrlService(api);
  }

  @Bean public RoleVoter roleVoter() {
    return new RoleVoter();
  }

  @Bean public MenuSecurityFilter filter(
      final SecuredUrlService urlService,
      final RoleVoter voter,
      @Value("${menu.filterBySecurity}") final boolean isSecured) {
    return new MenuSecurityFilter(voter, urlService, isSecured);
  }

  /** {@inheritDoc}.*/
  @Override
  protected FreemarkerPaths paths() {
    return new FreemarkerPaths(
        "classpath:org/waabox/banten/web/menu/templates", "../banten-web");
  }

}
