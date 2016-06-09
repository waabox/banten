package org.waabox.banten.shiro;
import java.io.IOException;

import javax.servlet.*;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.servlet.AdviceFilter;

import org.springframework.web.util.ContentCachingResponseWrapper;

/** Filter that Banten adds to the shiro filter chain that sends the session
 * cookie to the browser.
 *
 * This filter buffers the response so that cookies can be sent as late as
 * possible.
 */
public class BantenSessionStorerFilter extends AdviceFilter {

  /** Flag that indicates that the {@link Cookie} has been written.*/
  private static final String BANTEN_COOKIE_WRITTEN = "banten.cookieWritten";

  /** {@inheritDoc}.*/
  @Override
  protected void executeChain(final ServletRequest request,
      final ServletResponse response, final FilterChain chain)
          throws Exception {

    HttpServletResponse httpResponse;
    httpResponse = (HttpServletResponse) response;

    ContentCachingResponseWrapper wrappedResponse;
    wrappedResponse = new ContentCachingResponseWrapper(httpResponse) {

      /** Sends the cookies before writing the content. */
      @Override
      protected void copyBodyToResponse(final boolean complete)
          throws IOException {
        if (request.getAttribute(BANTEN_COOKIE_WRITTEN) == null) {
          ((BantenSession) SecurityUtils.getSubject().getSession()).save();
          request.setAttribute(BANTEN_COOKIE_WRITTEN, true);
        }
        super.copyBodyToResponse(complete);
      }
    };
    chain.doFilter(request, wrappedResponse);
    wrappedResponse.copyBodyToResponse();
  }
}
