package org.waabox.banten.login.domain;

import java.util.List;

import org.apache.commons.lang3.Validate;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.waabox.banten.hibernate.Repository;

/** Root of the aggregate for the {@link User} entity.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class UserRepository extends Repository {

  /** Creates a new instance of the repository.
   * @param theSessionFactory the session factory, cannot be null.
   */
  public UserRepository(final SessionFactory theSessionFactory) {
    super(theSessionFactory);
  }

  /** Retrieves a {@link User} by its id.
   * @param email the id.
   * @return the {@link User} or null.
   */
  public User getById(final long id) {
    return getCurrentSession().get(User.class, id);
  }

  /** Retrieves a {@link User} by its email.
   * @param email the email, cannot be null.
   * @return the {@link User} or null.
   */
  public User getByEmail(final String email) {
    Validate.notNull(email, "The email cannot be null");
    Criteria criteria = createCriteria(User.class);
    criteria.add(Restrictions.eq("email", email));
    return (User) criteria.uniqueResult();
  }

  /** Stores a {@link User}.
   * @param user the {@link User} to store, cannot be null.
   */
  public void save(final User user) {
    Validate.notNull(user, "The user cannot be null");
    getCurrentSession().saveOrUpdate(user);
  }

  /** Stores a {@link Role}.
   * @param role the {@link Role} to store, cannot be null.
   */
  public void save(final Role role) {
    Validate.notNull(role, "The role cannot be null");
    getCurrentSession().saveOrUpdate(role);
  }

  /** Retrieves a {@link Role} by its name.
   * @param name the {@link Role} name.
   * @return the {@link Role} or null.
   */
  public Role getRoleByName(final String name) {
    Validate.notNull(name, "The role name cannot be null");
    Criteria criteria = createCriteria(Role.class);
    criteria.add(Restrictions.eq("name", name));
    return (Role) criteria.uniqueResult();
  }

  /** Retrieves the defined roles.
   * @return the list of roles.
   */
  @SuppressWarnings("unchecked")
  public List<Role> getRoles() {
    Criteria criteria = createCriteria(Role.class);
    return criteria.list();
  }

}
