package org.waabox.banten.web.menu;

import java.util.*;

import org.slf4j.Logger;
import org.waabox.banten.core.ConfigurationApi;

import static org.slf4j.LoggerFactory.getLogger;

/** Menu configuration Api.
 *
 * TODO[waabox] please doc me!.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class MenuConfigurationApi extends ConfigurationApi {

  /** The log. */
  private static Logger log = getLogger(MenuConfigurationApi.class);

  /** The list of nodes. */
  private final List<Menu> nodes = new LinkedList<>();

  /** The root menu. */
  private final Menu menu = new Menu("/", "root");

  /** Register a menu node.
   * @param node the menu node.
   */
  private void register(final Menu node) {
    nodes.add(node);
  }

  /** Retrieves the menu.
   *
   * @return the menu, never never null.
   */
  public Menu get() {
    return menu;
  }

  /** {@inheritDoc}.*/
  @Override
  protected void init() {

    // Sort the node list by path.
    Collections.sort(nodes, new Comparator<Menu>() {
      @Override
      public int compare(final Menu menuA, final Menu menuB) {
        return menuA.getPath().compareTo(menuB.getPath());
      }
    });

    for (Menu aMenu : nodes) {

      String path = aMenu.getPath();

      log.debug("Adding to the menu: " + path);

      Menu parentMenu = menu.searchFor(path);
      if (parentMenu == null) {
        String parentPath = path.substring(0, path.lastIndexOf('/'));
        if (parentPath.equals("")) {
          menu.add(aMenu);
        } else {
          Menu newParent = menu.searchFor(parentPath);
          if (newParent == null) {
            throw new IllegalStateException(
                "Please declare the root menu for the path:" + parentPath);
          }
          newParent.add(aMenu);
        }
      } else {
        parentMenu.add(aMenu);
      }
    }
  }

  /** Registers a new menu entry as a leaf.
   *
   * @param aName the name, cannot be null.
   * @param aLink the link, cannot be null.
   * @param aPath the path, cannot be null & must start with: '/';
   * @return this.
   */
  public MenuConfigurationApi node(final String aName, final String aLink,
      final String aPath) {
    register(new Menu(aName, aLink, aPath));
    return this;
  }

  /** Registers a root node.
   *
   * @param aName the name for the root menu, cannot be null.
   * @param path the path for this root menu,
   *  cannot be null & must start with: '/';
   * @return this.
   */
  public MenuConfigurationApi root(final String aName, final String path) {
    register(new Menu(path, aName));
    return this;
  }

}
