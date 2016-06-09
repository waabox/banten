package org.waabox.banten.sample.user.domain;

import org.springframework.core.env.Environment;
import org.waabox.banten.core.Factory;
import org.waabox.banten.sample.time.domain.TimeRepository;

/** Factory class for Users.
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class UserFactory implements Factory<User> {

  /** The time repository. */
  private final TimeRepository timeRepository;

  /** The Spring environment. */
  private final Environment environment;

  /** Creates a new instance of the Factory.
   * @param theTimeRepository the time repository.
   * @param theEnvironment the spring's env.
   */
  public UserFactory(final TimeRepository theTimeRepository,
      final Environment theEnvironment) {
    timeRepository = theTimeRepository;
    environment = theEnvironment;
  }

  /** {@inheritDoc}.*/
  @Override
  public User create() {
    return new User(timeRepository);
  }

  /** Creates a user with a name.
   * @param name the user name.
   * @param gmt the gmt.
   * @return the new user.
   */
  public User create(final String name, final String gmt) {
    String aName = name + "_" + environment.getProperty("user.prefix");
    User user = new User(timeRepository, aName, gmt);
    return user;
  }

}
