package org.waabox.banten.web;

import static org.easymock.EasyMock.*;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.waabox.banten.web.WebletResponseWrapper;

import static org.junit.Assert.*;

/* Tests the weblet response wrapper.
 */
public class WebletResponseWrapperTest {

  /* Tests that the writer correctly writes to the output stream.
   */
  @Test
  public final void testWriter() throws Exception {
    HttpServletResponse response = createNiceMock(HttpServletResponse.class);
    expect(response.getCharacterEncoding()).andReturn("utf-8");
    replay(response);

    WebletResponseWrapper wrapper = new WebletResponseWrapper(response);
    wrapper.getWriter().print("test");
    wrapper.flushBuffer();

    assertEquals("test", wrapper.getResponseAsString());
  }
}

