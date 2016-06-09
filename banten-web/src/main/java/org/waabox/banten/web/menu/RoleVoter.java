package org.waabox.banten.web.menu;

import org.apache.shiro.SecurityUtils;

/** Vote if the current active user has the necessary roles in order to
 * perform an action.
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class RoleVoter {

  /** Checks if the current active user has the specified role.
   *
   * @param roleName the application-specific role identifier, cannot be null.
   * @return true if this Subject has the specified role, false otherwise.
   */
  public boolean hasRole(final String roleName) {
    return SecurityUtils.getSubject().hasRole(roleName);
  }

}
