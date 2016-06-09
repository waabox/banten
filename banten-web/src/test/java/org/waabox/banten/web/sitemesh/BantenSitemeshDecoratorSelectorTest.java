package org.waabox.banten.web.sitemesh;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.easymock.EasyMock.*;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.sitemesh.content.Content;
import org.sitemesh.webapp.WebAppContext;
import org.springframework.http.MediaType;
import org.waabox.banten.web.sitemesh.BantenSitemeshDecoratorSelector;

public class BantenSitemeshDecoratorSelectorTest {

  Content content;
  WebAppContext context;
  HttpServletRequest request;

  @Before public void before() {
    content = createMock(Content.class);
    context = createMock(WebAppContext.class);
    request = createMock(HttpServletRequest.class);
  }

  @Test public void selectDecoratorPaths_isAjaxContentType() throws Exception {

    expect(context.getRequest()).andReturn(request);
    expect(request.getHeader("X-Requested-With")).andReturn(null);
    expect(context.getContentType()).andReturn(
        MediaType.APPLICATION_JSON_VALUE);

    replay(content, context, request);

    BantenSitemeshDecoratorSelector selector;
    selector = new BantenSitemeshDecoratorSelector();
    String[] out = selector.selectDecoratorPaths(content, context);

    assertThat(out.length, is(0));

    verify(content, context, request);
  }

  @Test public void selectDecoratorPaths_isAjaxHeader() throws Exception {

    expect(context.getRequest()).andReturn(request);
    expect(request.getHeader("X-Requested-With")).andReturn("XMLHttpRequest");

    replay(content, context, request);

    BantenSitemeshDecoratorSelector selector;
    selector = new BantenSitemeshDecoratorSelector();
    String[] out = selector.selectDecoratorPaths(content, context);

    assertThat(out.length, is(0));

    verify(content, context, request);
  }

  @Test public void selectDecoratorPaths_isModal() throws Exception {

    expect(context.getRequest()).andReturn(request);
    expect(request.getHeader("X-Requested-With")).andReturn(null);
    expect(context.getContentType()).andReturn(
        MediaType.TEXT_HTML_VALUE);

    expect(context.getPath()).andReturn("/modal/hours.html").anyTimes();

    replay(content, context, request);

    BantenSitemeshDecoratorSelector selector;
    selector = new BantenSitemeshDecoratorSelector();
    String[] out = selector.selectDecoratorPaths(content, context);

    assertThat(out.length, is(0));

    verify(content, context, request);
  }

  @Test public void selectDecoratorPaths_isApi() throws Exception {

    expect(context.getRequest()).andReturn(request);
    expect(request.getHeader("X-Requested-With")).andReturn(null);
    expect(context.getContentType()).andReturn(
        MediaType.TEXT_HTML_VALUE);

    expect(context.getPath()).andReturn("/api/hours.json").anyTimes();

    replay(content, context, request);

    BantenSitemeshDecoratorSelector selector;
    selector = new BantenSitemeshDecoratorSelector();
    String[] out = selector.selectDecoratorPaths(content, context);

    assertThat(out.length, is(0));

    verify(content, context, request);
  }

  @Test public void selectDecoratorPaths_explicitNotDecorate() throws Exception {

    expect(context.getRequest()).andReturn(request);
    expect(request.getHeader("X-Requested-With")).andReturn(null);
    expect(context.getContentType()).andReturn(
        MediaType.TEXT_HTML_VALUE);

    expect(context.getPath()).andReturn("/happy/hours.html").anyTimes();
    expect(context.getRequest()).andReturn(request);
    expect(request.getAttribute("::decoratePage")).andReturn(false);

    replay(content, context, request);

    BantenSitemeshDecoratorSelector selector;
    selector = new BantenSitemeshDecoratorSelector();
    String[] out = selector.selectDecoratorPaths(content, context);

    assertThat(out.length, is(0));

    verify(content, context, request);
  }
}
