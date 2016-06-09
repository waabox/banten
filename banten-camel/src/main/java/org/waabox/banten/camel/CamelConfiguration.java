package org.waabox.banten.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.boot.CamelAutoConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/** Camel public configuration.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
@Configuration
@Import({
  CamelAutoConfiguration.class
})
public class CamelConfiguration {

  /** Retrieves the Banten's {@link ServiceBus}.
   * @param camelContext the Camel's context, cannot be null.
   * @return the {@link ServiceBus}, never null.
   */
  @Bean public ServiceBus bantenServiceBus(final CamelContext camelContext) {
    return new ServiceBus(camelContext);
  }

}
