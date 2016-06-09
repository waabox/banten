package org.waabox.banten.web.menu.application;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.easymock.EasyMock.*;

import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;
import org.waabox.banten.web.menu.Menu;
import org.waabox.banten.web.menu.MenuObjectMother;
import org.waabox.banten.web.menu.MenuSecurityFilter;
import org.waabox.banten.web.menu.application.MenuController;

public class MenuControllerTest {

  @Test public void index() {

    Menu menu = MenuObjectMother.createFooMenu();
    MenuSecurityFilter filter = createMock(MenuSecurityFilter.class);

    expect(filter.filter(menu)).andReturn(menu);

    replay(filter);

    MenuController controller = new MenuController(menu, filter);

    ModelAndView mav = controller.index();

    assertThat((Menu) mav.getModelMap().get("menu"), is(menu));

    verify(filter);
  }

}
