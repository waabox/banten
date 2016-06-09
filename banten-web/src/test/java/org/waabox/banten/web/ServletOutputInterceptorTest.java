package org.waabox.banten.web;


import static org.easymock.EasyMock.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

import java.io.PrintWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.IOException;

import org.junit.Test;
import org.waabox.banten.web.ServletOutputInterceptor;
import org.waabox.banten.web.WebletResponseWrapper;

import static org.junit.Assert.*;

public class ServletOutputInterceptorTest {

  @Test public  void testWriter() throws Exception {
    HttpServletResponse response = createMock(HttpServletResponse.class);
    expect(response.getCharacterEncoding()).andReturn("utf-8");
    expectLastCall().times(2);
    replay(response);

    final OutputStream stream = new ByteArrayOutputStream();
    ServletOutputInterceptor wrapper = new ServletOutputInterceptor(response) {
      protected OutputStream createOutputStream() {
        return stream;
      }
    };
    wrapper.getWriter().print("test");
    wrapper.flushBuffer();

    assertEquals("test", stream.toString());
  }

  @Test public void testWriter_writeThrough() throws Exception {

    final OutputStream original = new ByteArrayOutputStream();
    ServletOutputStream outputStream = new ServletOutputStream() {
      public void write( final int theByte) throws IOException {
        original.write(theByte);
      }

      @Override
      public boolean isReady() {
        return false;
      }

      @Override
      public void setWriteListener( final WriteListener writeListener) {
      }
    };

    HttpServletResponse response = createMock(HttpServletResponse.class);
    expect(response.getCharacterEncoding()).andReturn("utf-8");
    expect(response.getOutputStream()).andReturn(outputStream);
    response.flushBuffer();
    replay(response);

    final OutputStream stream = new ByteArrayOutputStream();
    ServletOutputInterceptor wrapper = new ServletOutputInterceptor(response,
        true) {
      protected OutputStream createOutputStream() {
        return stream;
      }
    };
    wrapper.getWriter().print("test");
    wrapper.flushBuffer();

    assertEquals("test", stream.toString());
    assertEquals(original.toString(), stream.toString());
    verify(response);
  }

  @Test public void testGetOutpuStream() throws Exception {
    HttpServletResponse response = createNiceMock(HttpServletResponse.class);
    expect(response.getCharacterEncoding()).andReturn("utf-8");
    replay(response);

    WebletResponseWrapper wrapper = new WebletResponseWrapper(response);
    ServletOutputStream stream;
    stream = wrapper.getOutputStream();
    assertNotNull(stream);
    stream = wrapper.getOutputStream();
    assertNotNull(stream);
  }

  @Test public void testGetWriter() throws Exception {
    HttpServletResponse response = createNiceMock(HttpServletResponse.class);
    expect(response.getCharacterEncoding()).andReturn("utf-8");
    replay(response);

    WebletResponseWrapper wrapper = new WebletResponseWrapper(response);
    PrintWriter writer;
    writer = wrapper.getWriter();
    assertNotNull(writer);
    writer = wrapper.getWriter();
    assertNotNull(writer);
  }

  @Test(expected = IllegalStateException.class)
  public  void testGetWriter_afterOutputStream() throws Exception {
    HttpServletResponse response = createNiceMock(HttpServletResponse.class);
    expect(response.getCharacterEncoding()).andReturn("utf-8");
    replay(response);

    WebletResponseWrapper wrapper = new WebletResponseWrapper(response);
    wrapper.getOutputStream();
    wrapper.getWriter();
  }

  @Test(expected = IllegalStateException.class)
  public  void testGetOutpuStream_afterGetWriter() throws Exception {
    HttpServletResponse response = createNiceMock(HttpServletResponse.class);
    expect(response.getCharacterEncoding()).andReturn("utf-8");
    replay(response);

    WebletResponseWrapper wrapper = new WebletResponseWrapper(response);
    wrapper.getWriter();
    wrapper.getOutputStream();
  }
}


