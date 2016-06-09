package org.waabox.banten.sample.time;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.waabox.banten.sample.time.domain.TimeRepository;

/** The configuration.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
@Configuration
public class TimeConfiguration {

  @Bean
  public TimeRepository timeRepository(final SessionFactory sessionFactory) {
    return new TimeRepository(sessionFactory);
  }

}
