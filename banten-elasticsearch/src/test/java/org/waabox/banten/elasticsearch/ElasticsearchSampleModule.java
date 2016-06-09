package org.waabox.banten.elasticsearch;

import java.io.IOException;

import org.waabox.banten.core.ConfigurationApi;
import org.waabox.banten.core.ConfigurationApiRegistry;
import org.waabox.banten.core.Module;
import org.waabox.banten.elasticsearch.ElasticsearchConfigurationApi;
import org.waabox.banten.elasticsearch.FileMappingDefinition;
import org.waabox.banten.elasticsearch.MappingDefinition;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/** Just a sample module that maps an Elasticsearch's index.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class ElasticsearchSampleModule implements Module {

  /** {@inheritDoc}.*/
  @Override
  public String getName() {
    return "Elasticsearch-sample-module";
  }

  /** {@inheritDoc}.*/
  @Override
  public Class<?> getPublicConfiguration() {
    return Object.class;
  }

  /** {@inheritDoc}.*/
  @Override
  public ConfigurationApi getConfigurationApi() {
    return null;
  }

  /** {@inheritDoc}.*/
  public void init(final ConfigurationApiRegistry registry) {
    registry.get(ElasticsearchConfigurationApi.class)
    .mappings(

        new MappingDefinition() {

          @Override
          public String indexType() {
            return "test";
          }

          @Override
          public String indexName() {
            return "test";
          }

          @Override
          public String getSettings() throws IOException {
            return null;
          }

          @Override
          public String getMapping() throws IOException {
            return
              jsonBuilder()
                .startObject()
                  .startObject(indexType())
                    .field("dynamic").value("strict")
                    .startObject("properties")
                      .startObject("id")
                        .field("type").value("string")
                        .field("store").value(true)
                      .endObject()
                      .startObject("description")
                        .field("type").value("string")
                        .field("store").value(true)
                      .endObject()
                    .endObject()
                  .endObject()
                .endObject()
              .prettyPrint().string();
          }
        },

        new FileMappingDefinition("test2", "test2", "test2-mappings.json")

    );
  }

}
