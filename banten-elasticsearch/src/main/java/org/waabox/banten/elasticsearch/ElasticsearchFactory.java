package org.waabox.banten.elasticsearch;

import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

import static org.elasticsearch.common.settings.ImmutableSettings.*;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;

import org.elasticsearch.common.lang3.Validate;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

import org.springframework.beans.factory.DisposableBean;

/** Factory object for Elasticsearch's client.
 *
 * [Implementation note]
 * Once the factory has been created will start the Elasticsearch's node,
 * if we are within the production environment.
 * Will try to discover the cluster using the AWS API.
 *
 * @see 'https://github.com/elasticsearch/elasticsearch-cloud-aws'
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class ElasticsearchFactory implements DisposableBean {

  /** The log. */
  private static Logger log = getLogger(ElasticsearchFactory.class);

  /** The Elasticsearch's client, it's never null. */
  private final Client client;

  /** The Elasticsearch's node. */
  private final Node node;

  /** Creates a new instance of factory.
   *
   * @param config the {@link Configurator}.
   * @param theMappings the list of mappings.
   */
  @SuppressWarnings("resource")
  public ElasticsearchFactory(final ElasticsearchConfigurator config,
     final List<MappingDefinition> theMappings) {

    Validate.notNull(config, "The configurator cannot be null");
    Validate.notNull(theMappings, "The mappings cannot be null");

    log.trace("Creating a new ElasticSearch instance");

    ImmutableSettings.Builder settings = settingsBuilder();

    Map<String, String> properties = config.get();
    for(Entry<String, String> entry : properties.entrySet()) {
      settings.put(entry.getKey(), entry.getValue());
    }

    if (config.isLocalNode()) {

      log.trace("Starting local ElasticSearch instance.");

      node = NodeBuilder.nodeBuilder().settings(settings.build()).build();
      node.start();

      try {
        node.client().admin().cluster().prepareHealth()
          .setWaitForYellowStatus().execute().get();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }

      client = node.client();

      mappings(theMappings, config);

    } else {
      String host = config.getServerHostName();
      Integer port = config.getServerPortNumber();

      log.trace("Connecting to elasticsearch at: {}:{}", host, port);

      client = new TransportClient(settings.build())
        .addTransportAddress(new InetSocketTransportAddress(host, port));

      node = null;
    }

    log.trace("Leaving ElasticSearch creation");
  }

  /** Generates the mappings for each type of document.
   * @param theMappings the list of mappings.
   * @param config the Elasticsearch configuration.
   */
  private void mappings(final List<MappingDefinition> mappings,
      final ElasticsearchConfigurator config) {
    log.trace("Entering generateMappings");

    for (MappingDefinition mapping : mappings) {
      String indexName = mapping.indexName();
      log.debug("Working with the index: {}", indexName);

      if (config.dropIndexOnStartup()) {
        log.debug("Deleting the index: {}", indexName);
        IndexManager.delete(indexName, client);
      }

      if (!IndexManager.indexExists(indexName, client)) {
        log.debug("The index: {} does not exist", indexName);
        if(IndexManager.create(indexName, client)) {
          IndexManager.mapping(indexName, mapping, false, client);
          log.debug("The index: {} has been created!", indexName);
        }
      }
    }

    log.trace("Leaving generateMappings");
  }

  /** Retrieves the client.
   * @return the Elasticsearch's client, never null.
   */
  Client client() {
    return client;
  }

  /** {@inheritDoc}.*/
  @Override
  public void destroy() throws Exception {
    synchronized (this) {
      try {
        client.close();
      } catch (Exception e) {
        log.error("Error closing client", e);
      }
      try {
        if (node != null) {
          node.close();
        }
      } catch (Exception e) {
        log.error("Error closing node", e);
      }
    }
  }

}
