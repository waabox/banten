package org.waabox.banten.web;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.easymock.EasyMock.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.waabox.banten.web.Context;

public class ContextTest {

  @Test public void test() {
    HttpServletRequest request = createMock(HttpServletRequest.class);
    HttpServletResponse response = createMock(HttpServletResponse.class);

    replay(request, response);

    Context.init(request, response);

    assertThat(Context.request(), is(request));
    assertThat(Context.response(), is(response));

    Context.destroy();

    assertThat(Context.request(), nullValue());
    assertThat(Context.response(), nullValue());

    verify(request, response);
  }

}
