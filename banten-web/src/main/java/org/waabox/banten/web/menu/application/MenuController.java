package org.waabox.banten.web.menu.application;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.servlet.ModelAndView;
import org.waabox.banten.web.menu.Menu;
import org.waabox.banten.web.menu.MenuSecurityFilter;

/** The {@link MenuBar} controller.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
@RestController
@RequestMapping("/menu/")
public class MenuController {

  /** The {@link Menu}, it's never null.*/
  private final Menu menu;

  /** The {@link MenuSecurityFilter}, it's never null.*/
  private final MenuSecurityFilter menuFilter;

  /** Creates a new instance of the Controller.
   *
   * @param theMenu the menu.
   * @param theFilter the menu filter.
   */
  public MenuController(final Menu theMenu,
      final MenuSecurityFilter theFilter) {
    menu = theMenu;
    menuFilter = theFilter;
  }

  /** Renders the {@link Menu}.
   * @return the Spring's {@link ModelAndView} with the 'menu'.
   */
  @RequestMapping(value = "/index.html", method = RequestMethod.GET)
  public ModelAndView index() {
    ModelAndView mav = new ModelAndView("menu");
    mav.addObject("menu", menuFilter.filter(menu));
    return mav;
  }

}
