package org.waabox.banten.web.menu;

import java.util.LinkedList;
import java.util.List;

/** Filters the menu based on the active logged in user and its roles.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class MenuSecurityFilter {

  /** The role vote, it's never null.*/
  private final RoleVoter roleVoter;

  /** The url service, it's never null. */
  private final SecuredUrlService urlService;

  /** Whether or not should filter the application. */
  private final boolean isSecured;

  /** Creates a new instance of the {@link MenuSecurityFilter}.
   * @param voter the {@link RoleVoter}, cannot be null.
   * @param theUrlService {@link SecuredUrlService}, cannot be null.
   * @param isSecuredApplication whether or not should filter based on this
   * application need to filter the menu.
   */
  public MenuSecurityFilter(
      final RoleVoter voter,
      final SecuredUrlService theUrlService,
      final boolean isSecuredApplication) {
    roleVoter = voter;
    urlService = theUrlService;
    isSecured = isSecuredApplication;
  }

  /** Retrieves a filtered {@link Menu}.
   * @return the {@link Menu}, never null.
   */
  public Menu filter(final Menu menu) {

    if(!isSecured) {
      return menu;
    }

    Menu filteredMenu = new Menu(menu.getPath(), menu.getDisplayName());
    filteredMenu.add(doFilter(menu));
    return filteredMenu;
  }

  /** Performs a recursive action in order to detect nodes that the current
   * logged in user can access.
   * @param theMenu the menu.
   * @return the list of {@link Menu}s that the user is allowed to access.
   */
  private List<Menu> doFilter(final Menu theMenu) {
    List<Menu> toAdd = new LinkedList<>();
    for(Menu current : theMenu.getChildNodes()) {
      if (current.isLeaf()) {
        List<String> roles = urlService.rolesFor(current.getLink());
        boolean hasRole = false;
        for (String role : roles) {
          if(roleVoter.hasRole(role)) {
            hasRole = true;
          }
        }
        if (hasRole) {
          toAdd.add(current);
        }
      }
      if (!current.getChildNodes().isEmpty()) {
        List<Menu> fromChild = doFilter(current);
        if (!fromChild.isEmpty()) {
          Menu container = current.emptyCopy();
          container.add(fromChild);
          toAdd.add(container);
        }
      }
    }
    return toAdd;
  }

}
