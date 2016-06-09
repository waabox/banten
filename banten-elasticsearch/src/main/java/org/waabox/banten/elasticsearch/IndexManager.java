package org.waabox.banten.elasticsearch;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

import org.apache.commons.lang3.Validate;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;

import org.elasticsearch.action.admin.indices.close.*;
import org.elasticsearch.action.admin.indices.create.*;
import org.elasticsearch.action.admin.indices.delete.*;
import org.elasticsearch.action.admin.indices.exists.types.*;
import org.elasticsearch.action.admin.indices.mapping.put.*;
import org.elasticsearch.action.admin.indices.open.*;
import org.elasticsearch.action.admin.indices.refresh.*;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;

import org.elasticsearch.indices.IndexAlreadyExistsException;

/** Helper class that holds methods that interact with elasticsearch in order
 * to provide: creation, delete and exists methods.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public final class IndexManager {

  /** The log.*/
  private static Logger log = getLogger(IndexManager.class);

  /** Creates a new instance of the manager.*/
  private IndexManager() {
  }

  /** Creates a new index if it does not exist.
   *
   * @param indexName the name of the index to create.
   *
   * @return true if this operation created the index, false if the index
   * already existed.
   */
  public static boolean create(final String indexName, final Client client) {
    log.trace("Entering create");

    IndicesAdminClient indicesAdmin = client.admin().indices();

    if (!indexExists(indexName, client)) {
      CreateIndexResponse response;
      try {
        response = indicesAdmin.prepareCreate(indexName).execute().get();
      } catch (IndexAlreadyExistsException e) {
        // Elastic search told me that the index already existed, just return
        // false.
        log.trace("Leaving create with false");
        return false;
      } catch (Exception e) {
        log.error("Error creating index " + indexName, e);
        log.trace("Leaving create with exception");
        throw new RuntimeException("Error creating index " + indexName, e);
      }
      if (!response.isAcknowledged()) {
        log.warn("The creation of the index {} has not been acknowledged by"
            + " all nodes", indexName);
      }
      waitFor(indexName, client);
      log.trace("Leaving create with true");
      return true;
    }
    log.trace("Leaving create with false");
    return false;
  }

  /** Deletes an existing index and all its content.
   *
   * @param indexName the name of the index to delete. It must exist, otherwise
   * this operation fails with an exception.
   */
  public static void delete(final String indexName, final Client client) {
    log.trace("Entering delete({})", indexName);

    IndicesAdminClient adminClient = client.admin().indices();
    if (indexExists(indexName, client)) {
      try {
        DeleteIndexRequest request = new DeleteIndexRequest(indexName);
        DeleteIndexResponse response = adminClient.delete(request).get();
        if (!response.isAcknowledged()) {
          log.error("The delete index {} has not been aknowledged", indexName);
        } else {
          log.debug("Index {} was deleted", indexName);
        }
      } catch (Exception e) {
        throw new RuntimeException("Unable to delete index " + indexName, e);
      }
    }
    log.trace("Leaving delete({})", indexName);
  }

  /** Determines if the index exists.
   * @param indexName the name of the index.
   * @return true if the index exists, false otherwise.
   */
  public static boolean indexExists(final String indexName,
      final Client client) {
    log.trace("Entering indexExists");
    try {
      return client.admin().indices().prepareExists(indexName)
          .execute().get().isExists();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /** Creates the mapping within the given index name..
  *
  * @param indexName the name of the index to check, cannot be null.
  *
  * @param definition the mapping definition, cannot be null.
  *
  * @param mappingOnly only update the mappings. This is intended to add
  * simple changes to an existing index with no down-time.
  */
  public static synchronized void mapping(final String indexName,
      final MappingDefinition definition, final boolean mappingOnly,
      final Client client) {
    Validate.notNull(indexName, "The index name cannot be null");
    Validate.notNull(definition, "The definition cannot be null");

    log.trace("Entering mapping for: {}", indexName);

    try {
      IndicesAdminClient indicesAdmin = client.admin().indices();

      if (definition.getSettings() != null && !mappingOnly) {
        CloseIndexRequest closeRequest = new CloseIndexRequest();
        closeRequest.indices(definition.indexName());
        indicesAdmin.close(closeRequest).get();
        indicesAdmin.prepareUpdateSettings(definition.indexName())
          .setSettings(definition.getSettings()).execute().get();

        OpenIndexRequest openRequest = new OpenIndexRequest();
        openRequest.indices(definition.indexName());
        indicesAdmin.open(openRequest).get();
      }

      PutMappingRequest putMappingRequest = new PutMappingRequest(indexName);
      putMappingRequest.source(definition.getMapping());
      putMappingRequest.type(definition.indexType());
      if (mappingOnly) {
        putMappingRequest.ignoreConflicts(true);
      }

      PutMappingResponse response;
      response = indicesAdmin.putMapping(putMappingRequest).get();

      if (!response.isAcknowledged()) {
        throw new RuntimeException("Mapping definition over " + indexName
          + " has not been acknowledged by all nodes");
      }

      waitFor(indexName, client);
      log.trace("Leaving mapping");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /** Refresh the index.
   * @param index the name of the index to check, cannot be null.
   */
  public static synchronized void refreshIndex(final String index,
      final Client client) {
    Validate.notNull(index, "The index name cannot be null");
    try {
      client.admin().indices().refresh(new RefreshRequest(index)).get();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /** Checks whether or not if the given type exists within the given index
   * name.
   * @param index the index name, cannot be null.
   * @param type the type, cannot be null.
   * @return the truth.
   */
  public static boolean typeExist(final String index, final String type,
      final Client client) {
    Validate.notNull(index, "The index name cannot be null");
    Validate.notNull(type, "The type cannot be null");
    try {
      return client.admin().indices().typesExists(
          new TypesExistsRequest(new String[] {index}, type)).get().isExists();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /** Waits until the given index finish its initialization.
   * @param indexName the index, cannot be null.
   */
  private static void waitFor(final String indexName, final Client client) {
    log.debug("waiting...");
    ClusterHealthRequest healthRequest = new ClusterHealthRequest(indexName);
    healthRequest.waitForYellowStatus();
    try {
      client.admin().cluster().health(healthRequest).get();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    log.debug("done!, index named:" + indexName + " created");
  }
}
