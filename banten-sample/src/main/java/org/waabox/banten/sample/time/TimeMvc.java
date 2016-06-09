package org.waabox.banten.sample.time;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.transaction.annotation.*;

import org.springframework.web.servlet.*;
import org.waabox.banten.sample.time.controllers.TimeController;
import org.waabox.banten.sample.time.domain.TimeRepository;
import org.waabox.banten.web.freemarker.FreeMarkerConfigurer;
import org.waabox.banten.web.freemarker.FreeMarkerViewResolver;

/** The MVC configuration.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
@Configuration
@EnableTransactionManagement
public class TimeMvc {

  @Bean public TimeController timeController(final TimeRepository repository) {
    return new TimeController(repository);
  }

  @Bean public ViewResolver viewResolver() {
    return new FreeMarkerViewResolver();
  }

  @Bean public FreeMarkerConfigurer freemarkerConfig(
    @Value("${debugMode:false}") final boolean debugMode) {
    return new FreeMarkerConfigurer(debugMode, "../banten-sample",
        "classpath:org/waabox/banten/sample/time/templates");
  }
}
