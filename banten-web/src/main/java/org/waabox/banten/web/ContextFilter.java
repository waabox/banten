package org.waabox.banten.web;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

/** Populates the {@link Context}.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class ContextFilter implements Filter {

  /** Request attribute that indicates that the context has been initialized.*/
  public static final String ALREADY_FILTERED = "::bantenContentInitialized";

  /** {@inheritDoc}.*/
  @Override
  public void init(final FilterConfig filterConfig) throws ServletException {
  }

  /** {@inheritDoc}.*/
  @Override
  public void doFilter(final ServletRequest request,
      final ServletResponse response, final FilterChain chain)
      throws IOException, ServletException {

    if (request.getAttribute(ALREADY_FILTERED) == null) {
      try {
        Context.init((HttpServletRequest) request,
            (HttpServletResponse) response);

        request.setAttribute(ALREADY_FILTERED, "true");

        chain.doFilter(request, response);

      } finally {
        Context.destroy();
      }
    } else {
      chain.doFilter(request, response);
    }
  }

  /** {@inheritDoc}.*/
  @Override
  public void destroy() {
  }

}

