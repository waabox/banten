package org.waabox.banten.sample.user.domain;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.waabox.banten.hibernate.Repository;

/** The user repository.
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class UserRepository extends Repository {

  /** Creates a new instance of the repository.
   * @param sessionFactory the session factory.
   */
  public UserRepository(final SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  /** Saves the given user.
   * @param user the user to save.
   */
  public void save(final User user) {
    getCurrentSession().saveOrUpdate(user);
  }

  /** List the users.
   * @return the list of users.
   */
  @SuppressWarnings("unchecked")
  public List<User> list() {
    Criteria criteria = createCriteria(User.class);
    return criteria.list();
  }

}
