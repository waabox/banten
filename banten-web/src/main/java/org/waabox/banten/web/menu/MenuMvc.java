package org.waabox.banten.web.menu;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.waabox.banten.shiro.ShiroConfigurationApi;
import org.waabox.banten.web.freemarker.FreeMarkerViewResolver;
import org.waabox.banten.web.menu.application.MenuController;

/** Menu MVC configuration.
 * @author waabox (waabox[at]gmail[dot]com)
 */
@Configuration
public class MenuMvc {

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

  @Bean public ViewResolver viewResolver() {
    return new FreeMarkerViewResolver();
  }

  @Bean public FreeMarkerConfigurer freemarkerConfig(
      @Value("${debugMode:false}") final boolean debugMode) {
    return new org.waabox.banten.web.freemarker.FreeMarkerConfigurer(
        debugMode, "../banten-web",
        "classpath:org/waabox/banten/web/menu/templates");
  }

}
