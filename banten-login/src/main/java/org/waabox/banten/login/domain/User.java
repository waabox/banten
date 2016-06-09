package org.waabox.banten.login.domain;

import java.util.Collections;
import java.util.Set;

import javax.persistence.*;

import org.apache.commons.lang3.Validate;

/** The user.
 * @author waabox (waabox[at]gmail[dot]com)
 */
@Entity
@Table(
    name = "banten_users",
    indexes = @Index(columnList = "email")
)
public class User {

  /** The id.*/
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  /** The email for this user, it's null and unique.*/
  @Column(name = "email", unique = true)
  private String email;

  /** The password for this user, it's never null.*/
  @Column(name = "password")
  private String password;

  /** The list of roles for this user, it's never null.*/
  @ManyToMany(fetch = FetchType.EAGER)
  private Set<Role> roles;

  /** Creates an empty instance, for Hibernate.*/
  User() {
  }

  /** Creates a new instance of the User.
   * @param theEmail the email, cannot be null.
   * @param thePassword the password, cannot be null.
   * @param thePermissions the set of roles, cannot be null.
   */
  public User(final String theEmail, final String thePassword,
      final Set<Role> thePermissions) {
    Validate.notNull(theEmail, "The email cannot be null");
    Validate.notNull(thePassword, "The password cannot be null");
    Validate.notNull(thePermissions, "The roles cannot be null");
    email = theEmail;
    password = thePassword;
    roles = thePermissions;
  }

  /** Retrieves the id.
   * @return the id
   */
  public long getId() {
    return id;
  }

  /** Retrieves the email.
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /** Retrieves the password.
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /** Retrieves the roles.
   * @return an unmodifiable set of roles.
   */
  public Set<Role> getRoles() {
    return Collections.unmodifiableSet(roles);
  }

  /** Assigns a new {@link Role} to this user.
   * @param role the role to assign, cannot be null.
   */
  public void assignRole(final Role role) {
    Validate.notNull(role, "The role cannot be null");
    roles.add(role);
  }

  /** Unassigns the given {@link Role} from this user.
   * @param role the role to unassign, cannot be null.
   */
  public void unassignRole(final Role role) {
    Validate.notNull(role, "The role cannot be null");
    roles.remove(role);
  }

}
