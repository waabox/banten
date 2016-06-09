package org.waabox.banten.login.domain;

import javax.persistence.*;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.*;

/** Represents a Role that a user has.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
@Entity
@Table(name = "banten_role")
public class Role {

  /** The id. */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  /** The permission name. */
  @Column(name = "name", unique = true)
  private String name;

  /** The description for this permission, it's never null.*/
  @Column(name = "description")
  private String description;

  /** Creates an empty instance for Hibernate.*/
  Role() {
  }

  /** Creates a new instance of the Role.
   * @param theName the role name, cannot be null.
   */
  public Role(final String theName) {
    Validate.notNull(theName, "The name cannot be null");
    name = theName;
  }

  /** Creates a new instance of the Role with its description.
   * @param theName the role name, cannot be null.
   * @param theDescription the description, cannot be null.
   */
  public Role(final String theName, final String theDescription) {
    this(theName);
    Validate.notNull(theDescription, "The description cannot be null");
    description = theDescription;
  }

  /** Retrieves the id.
   * @return the id
   */
  public Integer getId() {
    return id;
  }

  /** Retrieves the name.
   * @return the name, never null.
   */
  public String getName() {
    return name;
  }

  /** Retrieves the description.
   * @return the description, can be null.
   */
  public String getDescription() {
    return description;
  }

  /** {@inheritDoc}.*/
  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(id).append(name).hashCode();
  }

  /** {@inheritDoc}.*/
  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj instanceof Role) {
      Role anotherP = (Role) obj;
      return new EqualsBuilder()
          .append(name, anotherP.getName())
          .append(id, anotherP.getId()).isEquals();
    } else {
      return false;
    }
  }

}
