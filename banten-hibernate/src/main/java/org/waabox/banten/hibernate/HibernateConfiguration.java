package org.waabox.banten.hibernate;

import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.tomcat.jdbc.pool.DataSource;

import org.hibernate.EntityMode;
import org.hibernate.SessionFactory;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.MetadataSources;

import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import org.hibernate.mapping.PersistentClass;

import org.springframework.boot.context.properties.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.env.Environment;

import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.waabox.banten.core.BantenContext;
import org.waabox.banten.core.Configurator;

/** Hibernate's configuration.
 *
 * The property files expect the Hibernate's documentation
 * with a prefix: dbHibernate + <Hibernate's property>
 *
 * [Required]
 * dbHibernate.hibernate.dialect
 *
 * Also, this configuration configures the data-source.
 *
 * [Required]
 * db.url
 * db.password
 * db.user
 * db.driver
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
@Configuration
@EnableConfigurationProperties
public class HibernateConfiguration {

  /** The log. */
  private static Logger log = getLogger(HibernateConfiguration.class);

  /** DataSource bean definition.
   * @return the DataSource to be used by the application.
   * */
  @Bean(name = "banten.dataSource")
  @ConfigurationProperties(prefix = "db")
  public DataSource dataSource() {
    return new DataSource();
  }

  /** Defines the transaction manager to use.
   *
   * @param sessionFactory the session factory.
   * @return the Hibernate Transaction manager.
   * */
  @Bean(name = "banten.transactionManager")
  public HibernateTransactionManager bantenTransactionManager(
      final SessionFactory sessionFactory) {
    HibernateTransactionManager manager = new HibernateTransactionManager();
    manager.setSessionFactory(sessionFactory);
    return manager;
  }

  /** Hibernate's LocalSession Factory.
   *
   * @param dataSource the data source.
   * @return the Hibernate's SessionFactory.
   * */
  @Bean(name = "banten.sessionFactory")
  public SessionFactory bantenSessionFactory(
      final DataSource dataSource,
      final Environment environment,
      final BantenContext bantenContext,
      final HibernateConfigurationApi api) throws Exception {

    List<PersistenceUnit> persistenceUnits = api.getPersistenceUnits();

    Validate.notNull(persistenceUnits, "The list cannot be null");
    Validate.notNull(dataSource, "The datasource cannot be null");

    Map<String, String> hibernateProperties;
    hibernateProperties = new Configurator("dbHibernate", environment).get();

    Validate.isTrue(hibernateProperties.size() > 0,
        "Hibernate properties has not been defined");

    StandardServiceRegistryBuilder builder;
    builder = new StandardServiceRegistryBuilder();
    builder.applySetting("hibernate.connection.datasource", dataSource);
    builder.applySetting("hibernate.current_session_context_class",
            "org.springframework.orm.hibernate5.SpringSessionContext");

    builder.addService(this.getClass(), new HibernateTuplizerService(
        persistenceUnits, bantenContext));

    builder.applySettings(hibernateProperties);

    StandardServiceRegistry registry = builder.build();
    MetadataSources sources = new MetadataSources(registry);

    for(PersistenceUnit pu : persistenceUnits) {
      log.info("Adding persistence class:[{}]", pu.getPersistenceClass());
      sources.addAnnotatedClass(pu.getPersistenceClass());
    }

    MetadataBuilder metadataBuilder = sources.getMetadataBuilder();

    Metadata metadata = metadataBuilder.build();

    for (PersistentClass pc : metadata.getEntityBindings()) {
      if (api.hasCustomFactory(Class.forName(pc.getClassName()))) {
        pc.addTuplizer(EntityMode.POJO, PlatformTuplizer.class.getName());
      }
    }

    return metadata.getSessionFactoryBuilder().build();
  }

  /** Retrieves the transaction handler for manually TXs handling.
   *
   * @param tm the {@link HibernateTransactionManager}.
   * @return the transaction handler, it's never null.
   */
  @Bean(name = "banten.transaction")
  public Transaction bantenTransaction(final HibernateTransactionManager tm) {
    return new Transaction(tm);
  }

}
