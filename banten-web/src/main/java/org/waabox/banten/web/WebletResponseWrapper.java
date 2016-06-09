package org.waabox.banten.web;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.Validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** A response wrapper that intercepts the data sent by the servlets and makes
 * it available as a string.
 */
public class WebletResponseWrapper extends ServletOutputInterceptor {

  /** The class logger.
   */
  private static Logger log = LoggerFactory.getLogger(
      WebletResponseWrapper.class);

  /** A stream that gets the data and buffers it in a byte array.
   *
   * It is never null.
   */
  private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

  /** Creates a new WebletResponseWrapper.
   *
   * @param response The wrapped response. It cannot be null.
   */
  public WebletResponseWrapper(final HttpServletResponse response) {
    super(response);
    Validate.notNull(response, "The response cannot be null");
  }

  /** {@inheritDoc}
   */
  protected OutputStream createOutputStream() {
    return outputStream;
  }

  /** {@inheritDoc}
   */
  @Override
  public void resetBuffer() {
    outputStream.reset();
  }

  /** {@inheritDoc}
   */
  @Override
  public boolean isCommitted() {
    return false;
  }

  /** Obtains the data written by the servlets as a string.
   *
   * @return a string with the data written by the servlets.
   */
  public String getResponseAsString() {
    log.trace("Entering toString");
    String result = null;
    try {
      flushBuffer();
      // I cannot assume that encoding cannot be null (it is not explicit in
      // the javadocs).
      String encoding = getResponse().getCharacterEncoding();
      if (encoding == null) {
        result = outputStream.toString();
      } else {
        result = outputStream.toString(encoding);
      }
    } catch (Exception e) {
      throw new RuntimeException("Error obtaining string from output stream",
          e);
    }
    if (log.isDebugEnabled()) {
      log.debug("Weblet output:" + result);
    }
    log.trace("Leaving toString");
    return result;
  }
}
