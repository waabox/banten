package org.waabox.banten.web.menu;

import org.waabox.banten.web.menu.Menu;

/** Object mother for menus.
 *
 * @see http://martinfowler.com/bliki/ObjectMother.html
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class MenuObjectMother {

  public static Menu createFooMenu() {
    Menu menu = new Menu("/", "root");
    menu.add(new Menu("foo", "/foo.html", "/foo-path"));
    menu.add(new Menu("foo2", "/foo2.html", "/foo-path"));
    return menu;
  }

}
