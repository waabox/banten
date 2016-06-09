package org.waabox.banten.web;

import org.apache.commons.lang3.Validate;
import org.jsoup.Jsoup;

import javax.servlet.*;
import javax.servlet.http.*;

/** Weblet Renderer.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class WebletRenderer {

  /** The Weblet container, it's never null.*/
  private final WebletContainer container;

  /** Create a new instance of the Weblet Dispatcher.
   *
   * @param webletContainer the weblet container, cannot be null.
   */
  public WebletRenderer(final WebletContainer webletContainer) {
    Validate.notNull(webletContainer, "The weblet container cannot be null");
    container = webletContainer;
  }

  /** Renders the given {@link Weblet} by its name.
   *
   * @param moduleName the {@link Module} name, cannot be null.
   * @param webletName the {@link Weblet} name, cannot be null.
   * @param request the current {@link HttpServletRequest}, cannot be null.
   * @param response the current {@link HttpServletResponse}, cannot be null.
   *
   * @return the String with the Weblet's execution output.
   *
   * @throws ServletException for the request dispatcher.
   * @throws IOException for the request dispatcher.
   */
  public String render(final String moduleName, final String webletName,
    final HttpServletRequest request, final HttpServletResponse response)
      throws Exception {

    WebletContainer.ModuleWeblet moduleWeblet;
    moduleWeblet = container.get(webletName, moduleName);

    if (moduleWeblet == null) {
      throw new RuntimeException("Weblet: " + webletName + " not found!");
    }

    WebletResponseWrapper wrappedResponse;
    wrappedResponse = new WebletResponseWrapper(response);

    RequestDispatcher dispatcher;
    dispatcher = request.getRequestDispatcher(moduleWeblet.endpoint());

    dispatcher.include(request, wrappedResponse);

    String page = wrappedResponse.getResponseAsString();

    return Jsoup.parse(page).body().html();
  }
}
