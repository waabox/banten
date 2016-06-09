package org.waabox.banten.web.sitemesh;

import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import javax.servlet.http.*;

import org.apache.commons.io.*;

import org.sitemesh.content.ContentProcessor;
import org.sitemesh.webapp.contentfilter.ResponseMetaData;
import org.springframework.http.MediaType;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.waabox.banten.web.sitemesh.FreemarkerWebAppContext;

import freemarker.template.Configuration;

public class FreemarkerWebAppContextTest {

  Configuration freemarkerConfig;
  ContentProcessor contentProcessor;
  HttpServletRequest request;
  HttpServletResponse response;
  ResponseMetaData metaData;
  HttpSession session;
  PrintWriter writer;
  ByteArrayOutputStream stream;

  @Before public void before() throws Exception {
    FreeMarkerConfigurationFactory factory;
    factory = new FreeMarkerConfigurationFactory();
    factory.setTemplateLoaderPaths("classpath:testDecorators");
    factory.setDefaultEncoding("UTF-8");
    freemarkerConfig = factory.createConfiguration();

    contentProcessor = createMock(ContentProcessor.class);
    request = createMock(HttpServletRequest.class);
    response = createMock(HttpServletResponse.class);
    metaData = createMock(ResponseMetaData.class);
    session = createMock(HttpSession.class);
    stream = new ByteArrayOutputStream();
    writer = new PrintWriter(stream);
  }

  @Test public void dispatch() throws Exception {

    expect(request.getSession(false)).andReturn(session);
    expect(response.getWriter()).andReturn(writer);

    replay(contentProcessor, request, response, metaData, session);

    FreemarkerWebAppContext ctx;
    ctx = new FreemarkerWebAppContext(MediaType.TEXT_HTML_VALUE,
        request, response, null, contentProcessor, metaData, true,
        freemarkerConfig);
    ctx.dispatch(request, response, "default.html");

    verify(contentProcessor, request, response, metaData, session);


    String fromFile = String.copyValueOf(
        IOUtils.toCharArray(
            FreemarkerWebAppContextTest.class.getResource(
                "/testDecorators/default.html").openStream(), Charsets.UTF_8));

    assertTrue(stream.toString("UTF-8").equals(fromFile));
  }

}
