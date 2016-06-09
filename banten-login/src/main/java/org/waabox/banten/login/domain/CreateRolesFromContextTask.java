package org.waabox.banten.login.domain;

import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

import org.apache.commons.lang3.Validate;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.waabox.banten.shiro.ShiroConfigurationApi;

/** This task creates the defined Roles within the application.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class CreateRolesFromContextTask
  implements ApplicationListener<ContextRefreshedEvent> {

  /** The log. */
  private final Logger log = getLogger(CreateRolesFromContextTask.class);

  /** The {@link UserRepository}, it's never null.*/
  private final UserRepository userRepository;

  /** The {@link ShiroConfigurationApi}, it's never null. */
  private final ShiroConfigurationApi shiroApi;

  /** Creates a new instance of the Task.
   * @param repository the {@link UserRepository}, cannot be null.
   * @param api the {@link ShiroConfigurationApi}, cannot be null.
   */
  public CreateRolesFromContextTask(final UserRepository repository,
      final ShiroConfigurationApi api) {
    Validate.notNull(repository, "The repository cannot be null");
    Validate.notNull(api, "The api cannot be null");
    userRepository = repository;
    shiroApi = api;
  }

  /** {@inheritDoc}.*/
  @Override
  @Transactional
  public void onApplicationEvent(final ContextRefreshedEvent event) {

    log.info("Creating roles taken from the application");

    List<Role> databaseRoles = userRepository.getRoles();

    for (String roleName : shiroApi.getDefinedRoles()) {
      log.info("Checking role: {}", roleName);

      boolean exist = false;
      for(Role role : databaseRoles) {
        if(!role.getName().equals(roleName)) {
          exist = true;
        }
      }

      if (!exist) {
        userRepository.save(new Role(roleName));
      }

    }

    log.info("Finish the creation of roles");

  }

}
