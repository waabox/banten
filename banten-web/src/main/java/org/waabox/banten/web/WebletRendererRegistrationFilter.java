package org.waabox.banten.web;

import org.apache.commons.lang3.Validate;

import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.*;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/** Register the {@link WebletRenderer} within the current Request in a
 * request attribute called: "::webletRenderer".
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class WebletRendererRegistrationFilter implements Filter {

  /** The log. */
  private static Logger log = getLogger(
      WebletRendererRegistrationFilter.class);

  /** The {@link WebletRenderer}, it's never null. */
  private final WebletRenderer renderer;

  /** The {@link WebletRenderer} parameter name. */
  public final static String WEBLET_RENDERER_PARAMETER = "::webletRenderer";

  /** Creates a new instance of the Filter.
   *
   * @param aRenderer the {@link WebletRenderer}, cannot be null.
   */
  public WebletRendererRegistrationFilter(final WebletRenderer aRenderer) {
    Validate.notNull(aRenderer, "The renderer cannot be null");
    renderer = aRenderer;
  }

  /** {@inheritDoc}.*/
  @Override
  public void init(final FilterConfig filterConfig) throws ServletException {
  }

  /** Register the {@link WebletRenderer} within a
   * {@link javax.servlet.http.HttpServletRequest#setAttribute(String, Object)}.
   *
   * @param servletRequest the HttpServletRequest.
   * @param servletResponse the httpServletResponse.
   * @param chain the FilterChain.
   *
   * @throws IOException
   * @throws ServletException
   */
  @Override
  public void doFilter(
      final ServletRequest servletRequest,
      final ServletResponse servletResponse,
      final FilterChain chain) throws IOException, ServletException {

    log.trace("Entering doFilter");

    log.debug("Attaching the WebletRenderer into the request");

    HttpServletRequest request = (HttpServletRequest) servletRequest;
    request.setAttribute(WEBLET_RENDERER_PARAMETER, renderer);

    chain.doFilter(servletRequest, servletResponse);

    log.trace("Leaving doFilter");
  }

  /** {@inheritDoc}.*/
  @Override
  public void destroy() {
  }
}
