package org.waabox.banten.sample.user;

import org.hibernate.SessionFactory;

import org.springframework.context.annotation.*;

import org.springframework.core.env.Environment;
import org.waabox.banten.sample.time.domain.TimeRepository;
import org.waabox.banten.sample.user.domain.UserFactory;
import org.waabox.banten.sample.user.domain.UserRepository;

/** The user configuration.
 * @author waabox (efinal mi[at]avenida[dot]com)
 */
@Configuration
@PropertySource("classpath:user.properties")
public class UserConfiguration {

  @Bean(name = "user.userFactory")
  public UserFactory userFactory(final TimeRepository repository,
      final Environment environment) {
    return new UserFactory(repository, environment);
  }

  @Bean(name = "user.userRepository")
  public UserRepository userRepository(final SessionFactory sessionFactory) {
    return new UserRepository(sessionFactory);
  }

}
