package org.waabox.banten.web.menu;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Arrays;

import static org.easymock.EasyMock.*;

import org.junit.Test;
import org.waabox.banten.web.menu.Menu;
import org.waabox.banten.web.menu.MenuSecurityFilter;
import org.waabox.banten.web.menu.RoleVoter;
import org.waabox.banten.web.menu.SecuredUrlService;

public class MenuSecurityFilterTest {

  @Test
  public void testFilter_withAdmin() {
    RoleVoter voter = createMock(RoleVoter.class);
    SecuredUrlService service = createMock(SecuredUrlService.class);

    Menu menu = MenuObjectMother.createFooMenu();

    expect(service.rolesFor("/foo.html")).andReturn(Arrays.asList("admin"));
    expect(service.rolesFor("/foo2.html")).andReturn(Arrays.asList("admin"));

    expect(voter.hasRole("admin")).andReturn(true).times(2);

    replay(voter, service);

    MenuSecurityFilter filter = new MenuSecurityFilter(voter, service, true);

    Menu filtered = filter.filter(menu);

    assertThat(filtered.getChildNodes().size(), is(2));

    verify(voter, service);
  }

  @Test
  public void testFilter_withNoRoles() {
    RoleVoter voter = createMock(RoleVoter.class);
    SecuredUrlService service = createMock(SecuredUrlService.class);

    Menu menu = MenuObjectMother.createFooMenu();

    expect(service.rolesFor("/foo.html")).andReturn(Arrays.asList("admin"));
    expect(service.rolesFor("/foo2.html")).andReturn(Arrays.asList("admin"));

    expect(voter.hasRole("admin")).andReturn(false).times(2);

    replay(voter, service);

    MenuSecurityFilter filter = new MenuSecurityFilter(voter, service, true);

    Menu filtered = filter.filter(menu);

    assertThat(filtered.getChildNodes().size(), is(0));

    verify(voter, service);
  }

  @Test
  public void testFilter_withOneRole() {
    RoleVoter voter = createMock(RoleVoter.class);
    SecuredUrlService service = createMock(SecuredUrlService.class);

    Menu menu = MenuObjectMother.createFooMenu();

    expect(service.rolesFor("/foo.html")).andReturn(Arrays.asList("admin"));
    expect(service.rolesFor("/foo2.html")).andReturn(Arrays.asList("time"));

    expect(voter.hasRole("admin")).andReturn(false).times(1);
    expect(voter.hasRole("time")).andReturn(true).times(1);

    replay(voter, service);

    MenuSecurityFilter filter = new MenuSecurityFilter(voter, service, true);

    Menu filtered = filter.filter(menu);

    assertThat(filtered.getChildNodes().size(), is(1));
    assertThat(filtered.getChildNodes().get(0).getLink(), is("/foo2.html"));

    verify(voter, service);
  }

}
