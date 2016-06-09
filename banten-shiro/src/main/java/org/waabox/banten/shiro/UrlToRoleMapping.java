package org.waabox.banten.shiro;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.*;

/** Holds the association between URL and Roles. This association will be used
 * in order to inject the ACLs to Apache ShiroConfiguration.
 *
 * Created by jordan.cabral on 02/02/16.
 */
public class UrlToRoleMapping {

  /** The url path, it's never null. */
  private final String url;

  /** The roles list, it's never null. */
  private final List<String> roles;

  /** Creates a new instance of the mapping.
   *
   * @param theUrl the URL that will be affected by the given roles,
   * cannot be null.
   * @param roleList the list of roles that will allow access to the given
   * URL, cannot be null.
   */
  public UrlToRoleMapping(final String theUrl,
      final String... roleList) {
    Validate.notNull(theUrl, "The URL cannot be null");
    Validate.notNull(roleList, "The list of roles cannot be null");
    url = theUrl;
    roles = Arrays.asList(roleList);
  }

  /** Retrieves the Roles associated to this URL.
   *
   * @return an unmodifiable list of roles.
   */
  public List<String> getRoles() {
    return Collections.unmodifiableList(roles);
  }

  /** Gets all Roles for this URL in a comma separated string.
   *
   * @return an string with the concatenated roles, never null.
   */
  public String rolesAsString() {
    return StringUtils.join(roles, ",");
  }

  /** Retrieves the URL for this Access.
   *
   * @return the URL, never null.
   */
  public String getUrl() {
    return url;
  }

}
