package org.waabox.banten.camel;

import org.apache.camel.*;
import org.apache.commons.lang3.Validate;

/** Holds operations to operate with the Camel's context.
 *
 * Right now it only provides one operation: request.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class ServiceBus {

  /** The camel context. */
  private final CamelContext camelContext;

  /** Creates a new instance of the service bus.
   * @param theCamelContext the camel context, cannot be null.
   */
  public ServiceBus(final CamelContext theCamelContext) {
    Validate.notNull(theCamelContext, "The camel context cannot be null");
    camelContext = theCamelContext;
  }

  /** Performs an invocation using the Camel's
   * {@link ProducerTemplate#request(Endpoint, Processor)}.
   *
   * @param request the message request, cannot be null.
   *
   * @return the exchange with the response.
   */
  public MessageResponse request(final MessageRequest request) {
    Validate.notNull(request, "The request cannot be null");
    ProducerTemplate template = camelContext.createProducerTemplate();
    Endpoint endpoint = camelContext.getEndpoint(request.endpoint());
    Exchange exchange = template.request(endpoint, new Processor() {
      @Override
      public void process(final Exchange exchange) throws Exception {
        exchange.getIn().setBody(request.body());
      }
    });
    request.processResponse(exchange);
    return request.createResponse(exchange);
  }

}
