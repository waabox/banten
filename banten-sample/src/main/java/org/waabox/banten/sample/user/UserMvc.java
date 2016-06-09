package org.waabox.banten.sample.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import org.waabox.banten.sample.user.controllers.UserController;
import org.waabox.banten.sample.user.domain.UserFactory;
import org.waabox.banten.sample.user.domain.UserRepository;
import org.waabox.banten.web.freemarker.FreemarkerMvc;
import org.waabox.banten.web.freemarker.FreemarkerPaths;

/** The user MVC.
 * @author waabox (waabox[at]gmail[dot]com)
 */
@Configuration
@EnableTransactionManagement
public class UserMvc extends FreemarkerMvc {

  @Bean public UserController userController(
      final UserRepository repository,
      final UserFactory userFactory) {
    return new UserController(repository, userFactory);
  }

  /** {@inheritDoc}.*/
  @Override
  protected FreemarkerPaths paths() {
    return new FreemarkerPaths(
        "classpath:org/waabox/banten/sample/user/templates",
        "../banten-sample");
  }
}
