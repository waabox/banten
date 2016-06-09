package org.waabox.banten.sample.time;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.transaction.annotation.EnableTransactionManagement;

import org.waabox.banten.sample.time.controllers.TimeController;
import org.waabox.banten.sample.time.domain.TimeRepository;

import org.waabox.banten.web.freemarker.FreemarkerMvc;
import org.waabox.banten.web.freemarker.FreemarkerPaths;

/** The MVC configuration.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
@Configuration
@EnableTransactionManagement
public class TimeMvc extends FreemarkerMvc {

  @Bean public TimeController timeController(final TimeRepository repository) {
    return new TimeController(repository);
  }

  /** {@inheritDoc}.*/
  @Override
  protected FreemarkerPaths paths() {
    return new FreemarkerPaths(
        "classpath:org/waabox/banten/sample/time/templates",
        "../banten-sample"
    );
  }
}
