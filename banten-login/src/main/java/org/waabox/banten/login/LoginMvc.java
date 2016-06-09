package org.waabox.banten.login;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.transaction.annotation.EnableTransactionManagement;

import org.waabox.banten.login.application.LoginController;
import org.waabox.banten.login.domain.CreateRolesFromContextTask;
import org.waabox.banten.login.domain.UserRepository;

import org.waabox.banten.shiro.ShiroConfigurationApi;

import org.waabox.banten.web.freemarker.FreemarkerMvc;
import org.waabox.banten.web.freemarker.FreemarkerPaths;

/** Login Module MVC configuration.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
@Configuration
@EnableTransactionManagement
public class LoginMvc extends FreemarkerMvc {

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

  /** {@inheritDoc}.*/
  @Override
  protected FreemarkerPaths paths() {
    return new FreemarkerPaths(
        "classpath:org/waabox/banten/login/templates", "../banten-login");
  }

}
