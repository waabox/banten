package org.waabox.banten.web.menu;

import java.util.List;

import org.junit.Test;
import org.waabox.banten.web.menu.Menu;
import org.waabox.banten.web.menu.MenuConfigurationApi;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class MenuTest {

  @Test public void test() {

    MenuConfigurationApi api = new MenuConfigurationApi()

      .root("Admin", "/Admin")
      .root("Time", "/Time")

      .node("Users-Admin", "/users/admin.html", "/Admin/users")
      .node("Time-Admin", "/Time/admin.html", "/Admin/time")
      .node("Time View", "/Time/view.html", "/Time")
      .node("Time View 2", "/Time/view.html", "/Time/view");

    api.init();

    Menu m = api.get();
    List<Menu> childNodes = m.getChildNodes();

    Menu adminNodes = childNodes.get(0);
    assertThat(childNodes.size(), is(2));
    assertThat(adminNodes.getName(), is("Admin"));
    assertThat(adminNodes.getChildNodes().size(), is(2));
    assertThat(adminNodes.getChildNodes().get(0).getDisplayName(),
        is("Time-Admin"));
    assertThat(adminNodes.getChildNodes().get(1).getDisplayName(),
        is("Users-Admin"));

    assertThat(childNodes.size(), is(2));

    Menu timeNodes = childNodes.get(1);
    assertThat(timeNodes.getName(), is("Time"));
    assertThat(timeNodes.getChildNodes().size(), is(2));
    assertThat(timeNodes.getChildNodes().get(0).getDisplayName(),
        is("Time View"));
    assertThat(timeNodes.getChildNodes().get(1).getDisplayName(),
        is("Time View 2"));

  }

}
