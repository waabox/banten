package org.waabox.banten.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Holds the current {@link HttpServletRequest} & {@link HttpServletResponse}
 * for the current active request.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class Context {

  /** The thread local storage, it's never null. */
  private static final ThreadLocal<ContextData> CTX = new ThreadLocal<>();

  /** Initializes the context.
   * @param request the servlet request, cannot be null.
   * @param response the servlet response, cannot be null.
   */
  public static void init(final HttpServletRequest request,
      final HttpServletResponse response) {
    CTX.set(new ContextData(request, response));
  }

  /** Destroys the current context.*/
  public static void destroy() {
    CTX.remove();
  }

  /** Retrieves the current {@link HttpServletRequest}
   * @return the request or null if it has been destroyed or not initialized.
   */
  public static HttpServletRequest request() {
    if (CTX.get() == null) {
      return null;
    }
    return CTX.get().getRequest();
  }

  /** Retrieves the current {@link HttpServletResponse}
   * @return the response or null if it has been destroyed or not initialized.
   */
  public static HttpServletResponse response() {
    if (CTX.get() == null) {
      return null;
    }
    return CTX.get().getResponse();
  }


  /** Holds the {@link HttpServletRequest} & {@link HttpServletResponse}.
   *
   * @author waabox (waabox[at]gmail[dot]com)
   */
  private static final class ContextData {

    /** The request. */
    private final HttpServletRequest request;

    /** The response. */
    private final HttpServletResponse response;

    /** Creates a new instance.
     * @param theRequest the request.
     * @param theResponse the response.
     */
    private ContextData(final HttpServletRequest theRequest,
        final HttpServletResponse theResponse) {
      request = theRequest;
      response = theResponse;
    }

    /** Retrieves the request.
     * @return the request
     */
    public HttpServletRequest getRequest() {
      return request;
    }

    /** Retrieves the response.
     * @return the response
     */
    public HttpServletResponse getResponse() {
      return response;
    }
  }

}
