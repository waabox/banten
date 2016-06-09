package org.waabox.banten.camel;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;

import org.springframework.context.annotation.Bean;
import org.waabox.banten.camel.CamelModule;
import org.waabox.banten.core.BantenApplication;
import org.waabox.banten.core.Bootstrap;
import org.waabox.banten.core.ConfigurationApiRegistry;

/** Just a sample application for camel.
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class SampleCamelApplication extends BantenApplication {

  @Override
  protected Bootstrap bootstrap() {
    return new Bootstrap(CamelModule.class);
  }

  @Bean public RoutesBuilder builder() {
    return new RouteBuilder() {
      @Override
      public void configure() throws Exception {
        from("direct:hello").bean("helloEndpoint", "hello(${body})");
      }
    };
  }

  @Bean(name = "helloEndpoint") public HelloBean hello() {
    return new HelloBean();
  }

  public class HelloBean {

    public String hello(final String tellMe) {
      return tellMe;
    }

  }

  @Override
  public void init(final ConfigurationApiRegistry registry) {
  }

}
