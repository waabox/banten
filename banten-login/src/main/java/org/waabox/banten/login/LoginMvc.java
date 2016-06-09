package org.waabox.banten.login;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.waabox.banten.login.application.LoginController;
import org.waabox.banten.login.domain.CreateRolesFromContextTask;
import org.waabox.banten.login.domain.UserRepository;
import org.waabox.banten.shiro.ShiroConfigurationApi;
import org.waabox.banten.web.freemarker.FreeMarkerConfigurer;
import org.waabox.banten.web.freemarker.FreeMarkerViewResolver;

/** Login Module MVC configuration.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
@Configuration
@EnableTransactionManagement
public class LoginMvc {

  /** The login form controller.
   * @return the {@link LoginController} never null.
   */
  @Bean public LoginController loginController() {
    return new LoginController();
  }

  @Bean public CreateRolesFromContextTask uploadRolesTask(
      final UserRepository repository,
      final ShiroConfigurationApi api) {
    return new CreateRolesFromContextTask(repository, api);
  }

  @Bean public ViewResolver viewResolver() {
    return new FreeMarkerViewResolver();
  }

  @Bean public FreeMarkerConfigurer freemarkerConfig(
    @Value("${debugMode:false}") final boolean debugMode) {
    return new FreeMarkerConfigurer(debugMode, "../banten-login",
        "classpath:org/waabox/banten/login/templates");
  }

}
