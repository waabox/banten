package org.waabox.banten.elasticsearch;
import java.io.IOException;

/** Holds the neccesary information in order to: create and map an index.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public interface MappingDefinition {

  /** Retrieves the index name.
   * @return the name of the index,never null.
   */
  String indexName();

  /** Retrieves the index type.
   * @return the type of the index, never null.
   */
  String indexType();

  /** Retrieves the JSON that holds the mapping information.
   *
   * See: http://www.elasticsearch.org/guide/en/elasticsearch/reference/
   * current/indices-put-mapping.html
   *
   * @return the mapping structure, never null or empty.
   * @throws IOException because of the JSON exceptions.
   */
  String getMapping() throws IOException;

  /** Retrieves the JSON that with the settings to use to create the index.
   *
   * See http://www.elastic.co/guide/en/elasticsearch/reference/current/
   * indices-create-index.html.
   *
   * NOTE: this is not a nice place to put this. We could probably 'collapse'
   * the type and index, so that each type has its own index.
   *
   * @return the index settings. Implementations may return null to let
   * elastic search configure the index with its default values.
   *
   * @throws IOException in case of error.
   */
  String getSettings() throws IOException;
}
