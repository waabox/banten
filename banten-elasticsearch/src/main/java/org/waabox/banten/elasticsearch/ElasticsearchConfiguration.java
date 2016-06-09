package org.waabox.banten.elasticsearch;

import java.util.*;

import org.elasticsearch.client.Client;

import org.springframework.boot.context.properties.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.env.Environment;

/** The Elasticsearch's Module Configuration.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
@Configuration
@EnableConfigurationProperties
public class ElasticsearchConfiguration {

  /** The Elasticsearch's {@link Configurator}.
   *
   * @param environment the Spring's {@link Environment}.
   * @return the {@link ElasticsearchConfigurator}, never null.
   */
  @Bean
  public ElasticsearchConfigurator elasticsearchConfigurator(
      final Environment environment) {
    return new ElasticsearchConfigurator("es", environment);
  }

  /** Retrieves the {@link ElasticsearchFactory}.
   *
   * @param configurator the {@link ElasticsearchConfigurator}.
   * @return the factory, never null.
   */
  @Bean
  public ElasticsearchFactory elasticsearchFactory(
      final ElasticsearchConfigurator configurator) {
    return new ElasticsearchFactory(configurator,
        ElasticsearchConfigurationApi.getMappingDefinitions());
  }

  /** Retrieves the {@link Client}.
   *
   * @param factory the {@link ElasticsearchFactory}, cannot be null.
   * @return the {@link Client}, never null.
   */
  @Bean
  public Client elasticsearchClient(final ElasticsearchFactory factory) {
    return factory.client();
  }

}
