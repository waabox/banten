package org.waabox.banten.sample.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import org.springframework.web.servlet.ViewResolver;
import org.waabox.banten.sample.user.controllers.UserController;
import org.waabox.banten.sample.user.domain.UserFactory;
import org.waabox.banten.sample.user.domain.UserRepository;
import org.waabox.banten.web.freemarker.FreeMarkerConfigurer;
import org.waabox.banten.web.freemarker.FreeMarkerViewResolver;

/** The user MVC.
 * @author waabox (waabox[at]gmail[dot]com)
 */
@Configuration
@EnableTransactionManagement
public class UserMvc {

  @Bean public UserController userController(
      final UserRepository repository,
      final UserFactory userFactory) {
    return new UserController(repository, userFactory);
  }

  @Bean public ViewResolver viewResolver() {
    return new FreeMarkerViewResolver();
  }

  @Bean public FreeMarkerConfigurer freemarkerConfig(
    @Value("${debugMode:false}") final boolean debugMode) {
    return new FreeMarkerConfigurer(debugMode, "../banten-sample",
        "classpath:org/waabox/banten/sample/user/templates");
  }
}
