package org.waabox.banten.camel;

import org.apache.camel.Exchange;
import org.apache.commons.lang3.Validate;

/** Holds the response resulting of the execution of
 * any {@link MessageRequest}.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class MessageResponse {

  /** The Camel's exchange, it's never null.*/
  private final Exchange exchange;

  /** Creates a new instance of the response.
   * @param camelExchange the Camel's exchange, cannot be null.
   */
  public MessageResponse(final Exchange camelExchange) {
    Validate.notNull(camelExchange, "The camel's exchange cannot be null");
    exchange = camelExchange;
  }

  /** Retrieves the response casted to the given type if the exchange has not
   * failures.
   *
   * Before calling this method, call the method {@link #isFailed()} first in
   * order to avoid NPEs, however, in order to read the body no-matter what
   * use the method {@link #exchange()}.
   *
   * @return the object result, null if the exchange
   *  {@link Exchange#isFailed()}.
   */
  public Object response() {
    if(isFailed()) {
      return null;
    }
    return exchange.getOut().getBody();
  }

  /** Returns true if this exchange failed due to either an exception or fault.
   * @return true if this exchange failed due to either an exception or fault
   */
  public boolean isFailed() {
    return exchange.isFailed();
  }

  /** Returns the exception bounded to this execution.
   * @return the exception, can be null.
   */
  public Exception getException() {
    return exchange.getException();
  }

  /** Retrieves the Camel's exchange.
   * @return the Camel's exchange, never null.
   */
  public Exchange exchange() {
    return exchange;
  }

}
