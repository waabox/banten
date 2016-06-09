package org.waabox.banten.camel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang3.Validate;

import org.apache.camel.Exchange;

/** The message request interface describes the way that the message is sent
 * into the message bus.
 *
 * The message responsibilities are:
 * a) knowledge related to the route.
 * b) Holds the message's body.
 * c) Handle the response.
 * d) Create the response.
 *
 * TODO [Use generics instead of Object within the 'body'].
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class MessageRequest {

  /** The log. */
  private static Logger log = LoggerFactory.getLogger(MessageRequest.class);

  /** The body of to send to the event-bus, it's never null.*/
  private final Object body;

  /** The Camel's end-point, it's never null.*/
  private final String endpoint;

  /** Flag that check if should log errors or not, default true.*/
  private boolean logOnError = true;

  /** Creates a new instance of the message request.
   * @param theEndpoint the end-point, cannot be null.
   * @param theBody the body to send, cannot be null.
   */
  public MessageRequest(final String theEndpoint, final Object theBody) {
    Validate.notNull(theEndpoint, "The endpoint cannot be null");
    Validate.notNull(theBody, "The body cannot be null");
    endpoint = theEndpoint;
    body = theBody;
  }

  /** Process the response based on the Camel's exchange.
   * @param exchange the Camels's exchange, it's never null.
   * @return the message response, never null.
   */
  final void processResponse(final Exchange exchange) {
    log.trace("Entering processResponse");
    if (!exchange.isFailed()) {
      log.debug("Exchange without failures.");
      doProcess(exchange);
    } else {
      if(logOnError) {
        log.error("Cannot process the response, exchange with errors",
            exchange.getException());
      }
    }
    log.trace("Leaving processResponse");
  }

  /** Template method to be overridden by specific implementation in order
   * to operate with the exchange.
   * @param exchange the Camel's exchange, it's never null.
   */
  protected void doProcess(final Exchange exchange) {
  }

  /** Retrieves the end-point name.
   * @return the end-point  name.
   */
  public String endpoint() {
    return endpoint;
  }

  /** Retrieves the body of the message.
   * @return the message's body.
   */
  public Object body() {
    return body;
  }

  /** Creates a new instance of the message response.
   * @param exchange the Camel's exchange, cannot be null.
   * @return the message response, never null.
   */
  public MessageResponse createResponse(final Exchange exchange) {
    return new MessageResponse(exchange);
  }

  /** Disables the error log.*/
  public MessageRequest disableErrorLogging() {
    logOnError = false;
    return this;
  }

  /** Enables the error log.*/
  public MessageRequest enableErrorLogging() {
    logOnError = true;
    return this;
  }

}
