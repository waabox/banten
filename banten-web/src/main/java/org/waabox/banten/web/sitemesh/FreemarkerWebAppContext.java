package org.waabox.banten.web.sitemesh;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.sitemesh.content.ContentProcessor;
import org.sitemesh.webapp.WebAppContext;
import org.sitemesh.webapp.contentfilter.ResponseMetaData;

import freemarker.ext.servlet.*;
import static freemarker.ext.servlet.FreemarkerServlet.*;

import freemarker.template.*;

/** Freemarker's web application context for Sitemesh.
 *
 * This context use Freemarker instead of the request dispatcher implemented
 * within the Sitemesh's {@link WebAppContext}.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class FreemarkerWebAppContext extends WebAppContext {

  /** The Freemarker's configuration, it's never null. */
  private final Configuration freemarkerConfig;

  /** Creates a new instance of the Freemarker context.
   * @param contentType the content type.
   * @param request the current request.
   * @param response the current response.
   * @param contentProcessor the content processor.
   * @param metaData the metadata.
   * @param includeErrorPages if include error pages.
   * @param configuration the Freemarker's configuration.
   */
  public FreemarkerWebAppContext(
      final String contentType,
      final HttpServletRequest request,
      final HttpServletResponse response,
      final ServletContext context,
      final ContentProcessor contentProcessor,
      final ResponseMetaData metaData,
      final boolean includeErrorPages,
      final Configuration configuration) {

    super(contentType, request, response, context, contentProcessor,
        metaData, includeErrorPages);

    freemarkerConfig = configuration;
  }

  /** {@inheritDoc}.*/
  @Override
  protected void dispatch(final HttpServletRequest request,
      final HttpServletResponse response, final String path)
      throws ServletException, IOException {
    Template template = freemarkerConfig.getTemplate(path);
    TemplateModel model = templateModel(request, response);
    try {
      template.process(model, response.getWriter());
    } catch (TemplateException e) {
      throw new RuntimeException(e);
    }
  }

  /** Builds a FreeMarker's template model for the current request & response.
   *
   * @param request current HTTP request
   * @param response current HTTP response
   * @return the FreeMarker's template model.
   */
  private SimpleHash templateModel(final HttpServletRequest request,
      final HttpServletResponse response) {
    AllHttpScopesHashModel model = buildModel(request);

    model.put(KEY_APPLICATION, keyApplicationModel(request));
    model.put(KEY_SESSION, sessionModel(request, response));
    model.put(KEY_REQUEST, requestModel(request, response));
    model.put(KEY_REQUEST_PARAMETERS, requestParametersModel(request));
    model.put("request", request);
    model.put("response", response);

    return model;
  }

  /** Builds the Freemarker's model container for the given HTTP request.
   * @param request the HTTP request.
   * @return the Freemarker's AllHttpScopesHashModel.
   */
  private AllHttpScopesHashModel buildModel(final HttpServletRequest request) {
    return new AllHttpScopesHashModel(
        objectWrapper(), getServletContext(), request);
  }

  /** Returns the configured FreeMarker's {@link ObjectWrapper}.*/
  private ObjectWrapper objectWrapper() {
    return freemarkerConfig.getObjectWrapper();
  }

  /** Builds a Freemarker's {@link HttpRequestHashModel} the identifies the
   * request, needed by Freemarker.
   * @param request current HTTP request.
   * @return the Freemarker's HttpRequestHashModel
   */
  private HttpRequestHashModel keyApplicationModel(
      final HttpServletRequest request) {
    return new HttpRequestHashModel(request, objectWrapper());
  }

  /** Builds a Freemarker's {@link HttpRequestParametersHashModel} for the given
   * request.
   * @param request current HTTP request.
   * @return the Freemarker's HttpRequestParametersHashModel
   */
  private HttpRequestParametersHashModel requestParametersModel(
      final HttpServletRequest request) {
    return new HttpRequestParametersHashModel(request);
  }

  /** Builds a Freemarker's {@link HttpRequestHashModel} for the given request
   * and response.
   * @param request current HTTP request.
   * @param response current HTTP response.
   * @return the Freemarker's HttpRequestHashModel
   */
  private HttpRequestHashModel requestModel(
      final HttpServletRequest request,
      final HttpServletResponse response) {
    return new HttpRequestHashModel(request, response, objectWrapper());
  }

  /** Builds a Freemarker's {@link HttpSessionHashModel} for the given request,
   * detecting whether a session already exists and reacting accordingly.
   *
   * @param req current HTTP request.
   * @param res current Servlet's response.
   * @return the Freemarker's HttpSessionHashModel.
   */
  private HttpSessionHashModel sessionModel(
      final HttpServletRequest req, final HttpServletResponse res) {
    HttpSession session = req.getSession(false);
    if (session != null) {
      return new HttpSessionHashModel(session, objectWrapper());
    } else {
      return new HttpSessionHashModel(null, req, res, objectWrapper());
    }
  }
}
