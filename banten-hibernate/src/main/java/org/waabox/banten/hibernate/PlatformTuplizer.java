package org.waabox.banten.hibernate;

import org.hibernate.bytecode.spi.ReflectionOptimizer;
import org.hibernate.mapping.PersistentClass;

import org.hibernate.tuple.*;
import org.hibernate.tuple.entity.*;

import org.springframework.beans.DirectFieldAccessor;
import org.waabox.banten.core.*;

/** Platform Tuplizer that use the Factories declared within
 * the PersistenceUnit.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class PlatformTuplizer extends PojoEntityTuplizer {

  /** Creates a new instance of the Tuplizer.
   * @param entityMetamodel the entity model.
   * @param mappedEntity the mapped entity.
   */
  public PlatformTuplizer(final EntityMetamodel entityMetamodel,
      final PersistentClass mappedEntity) {
    super(entityMetamodel, mappedEntity);
  }

  /** {@inheritDoc}.*/
  @Override
  protected Instantiator buildInstantiator(
      final EntityMetamodel entityMetamodel,
      final PersistentClass persistentClass) {

    HibernateTuplizerService service = entityMetamodel.getSessionFactory()
        .getServiceRegistry().getService(HibernateTuplizerService.class);

    Instantiator instantiator;
    instantiator = super.buildInstantiator(entityMetamodel, persistentClass);

    try {
      return new BantenInstantiator(persistentClass, instantiator, service);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  /** Instantiator that serch within the PersistenceUnits the ones that
   * has been declared a Factory and use it as Instantiator.
   *
   * @author waabox (waabox[at]gmail[dot]com)
   */
  public static class BantenInstantiator extends PojoInstantiator {

    /** The serial version. */
    private static final long serialVersionUID = 1L;

    /** The Hibernate's persistent class, it's never null. */
    private final PersistentClass persistentClass;

    /** The hibernate service. */
    private final HibernateTuplizerService service;

    /** Creates a new instance of the {@link Instantiator}.
     * @param aPersistentClass the {@link PersistentClass}, cannot be null.
     * @param instantiator the {@link Instantiator}, cannot be null.
     * @throws ClassNotFoundException
     */
    public BantenInstantiator(final PersistentClass aPersistentClass,
        final Instantiator instantiator,
        final HibernateTuplizerService tuplizerService)
            throws ClassNotFoundException {
      super(
          Class.forName(aPersistentClass.getClassName()),
          optimizer(instantiator),
          aPersistentClass.hasEmbeddedIdentifier()
      );
      persistentClass = aPersistentClass;
      service = tuplizerService;
    }

    /** Extract the InstantiationOptimizer from the given {@link Instantiator}.
     * @param instantiator the {@link Instantiator}, cannot be null.
     * @return the {@link Instantiator} or null.
     */
    private static ReflectionOptimizer.InstantiationOptimizer optimizer(
        final Instantiator instantiator) {
      return (ReflectionOptimizer.InstantiationOptimizer)
          new DirectFieldAccessor(instantiator).getPropertyValue("optimizer");
    }

    /** {@inheritDoc}.*/
    @SuppressWarnings("rawtypes")
    @Override
    public Object instantiate() {
      Factory factory = service.factoryFor(persistentClass);
      if(factory != null) {
        return factory.create();
      }
      return super.instantiate();
    }
  }

}
