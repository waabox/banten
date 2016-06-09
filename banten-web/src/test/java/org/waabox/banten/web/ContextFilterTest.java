package org.waabox.banten.web;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.waabox.banten.web.Context;
import org.waabox.banten.web.ContextFilter;

public class ContextFilterTest {

  private boolean called = false;

  HttpServletRequest request = createMock(HttpServletRequest.class);
  HttpServletResponse response = createMock(HttpServletResponse.class);

  FilterChain chain = new MockFilterChain() {
    public void doFilter(final ServletRequest req, final ServletResponse res)
        throws IOException , ServletException {
      assertThat(Context.request(), is(request));
      assertThat(Context.response(), is(response));
      called = true;
    };
  };

  @Test public void doFilter() throws Exception {
    ContextFilter filter = new ContextFilter();

    expect(request.getAttribute(ContextFilter.ALREADY_FILTERED))
      .andReturn(null);

    request.setAttribute(ContextFilter.ALREADY_FILTERED, "true");

    replay(request, response);

    filter.doFilter(request, response, chain);

    assertThat(Context.request(), nullValue());
    assertThat(Context.response(), nullValue());
    assertThat(called, is(true));

    verify(request, response);
  }

}
