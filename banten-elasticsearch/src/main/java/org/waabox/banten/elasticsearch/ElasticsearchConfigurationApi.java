package org.waabox.banten.elasticsearch;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.waabox.banten.core.ConfigurationApi;

/** Elasticsearch's configuration API.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class ElasticsearchConfigurationApi extends ConfigurationApi {

  /** The list of mapping definitions.*/
  private static List<MappingDefinition> mappingDefinitions;

  static {
    mappingDefinitions = new LinkedList<>();
  }

  /** Register a list of {@link MappingDefinition}s.
   * @param definitions the {@link MappingDefinition}s, cannot be null.
   */
  public void mappings(final MappingDefinition...definitions) {
    Validate.notNull(definitions,
        "The list of mapping definitions cannot be null");
    mappingDefinitions.addAll(Arrays.asList(definitions));
  }

  /** Retrieves the mapping definitions.
   * @return the mapping definitions
   */
  public static List<MappingDefinition> getMappingDefinitions() {
    return mappingDefinitions;
  }

}
