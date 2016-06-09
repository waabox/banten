package org.waabox.banten.sample;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.waabox.banten.hibernate.Transaction;
import org.waabox.banten.login.domain.Role;
import org.waabox.banten.login.domain.User;
import org.waabox.banten.login.domain.UserRepository;

/** Creates a sample login account for development.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class SampleLoginAccount
  implements ApplicationListener<ApplicationEvent> {

  /** The user repository. */
  @Autowired private UserRepository userRepository;

  /** The platform transaction. */
  @Autowired private Transaction transaction;

  @Override
  public void onApplicationEvent(final ApplicationEvent event) {

    if (event.getClass() != ApplicationReadyEvent.class) {
      return;
    }

    try {
      transaction.start();

      Role roleAdmin = userRepository.getRoleByName("admin");
      if (roleAdmin == null) {
        roleAdmin = new Role("admin");
        userRepository.save(roleAdmin);
      }

      Role roleTime = userRepository.getRoleByName("time");
      if(roleTime == null) {
        roleTime = new Role("time");
        userRepository.save(roleTime);
      }

      Set<Role> rolesForRoot = new HashSet<>();
      rolesForRoot.add(roleAdmin);
      rolesForRoot.add(roleTime);

      Set<Role> rolesForTime = new HashSet<>();
      rolesForTime.add(roleTime);

      userRepository.save(new User("root@banten.org", "root", rolesForRoot));

      userRepository.save(new User("time@banten.org", "root", rolesForTime));

      userRepository.save(new User("noroles@banten.org", "root",
          new HashSet<Role>()));

      transaction.commit();
    } finally {
      transaction.cleanup();
    }

  }

}
