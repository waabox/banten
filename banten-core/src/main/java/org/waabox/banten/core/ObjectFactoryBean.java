package org.waabox.banten.core;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;

/** Utility class to add a concrete instance of an object as a bean in a bean
 * factory.
 *
 * This is used, for example, to make module information available to the
 * private spring application context (see ModuleDescription) and to create
 * a bean with the web application root context.
 */
public class ObjectFactoryBean implements FactoryBean<Object> {

  /** The instance of the module description to expose as a spring bean.*/
  private Object instance;

  /** The type for this factory bean.*/
  private Class<?> type;

  /** Register within the {@link BeanDefinitionRegistry} the given bean
   * instance.
   * @param registry the {@link BeanDefinitionRegistry}, cannot be null.
   * @param type the type of the bean, cannot be null.
   * @param instance the instance of the bean, should be instance of the
   * given type, cannot be null.
   * @param beanName the name for the bean.
   */
  public static void register(final BeanDefinitionRegistry registry,
      final Class<?> type, final Object instance, final String beanName) {

    MutablePropertyValues descriptionValues = new MutablePropertyValues();
    descriptionValues.add("instance", instance);
    descriptionValues.add("type", type);

    GenericBeanDefinition descriptionDefinition;
    descriptionDefinition = new GenericBeanDefinition();
    descriptionDefinition.setBeanClass(ObjectFactoryBean.class);
    descriptionDefinition.setPropertyValues(descriptionValues);
    descriptionDefinition.setLazyInit(true);

    registry.registerBeanDefinition(beanName, descriptionDefinition);
  }

  /** Sets the instance for the bean.
   * @param theInstance the instance, can be null.
   */
  public void setInstance(final Object theInstance) {
    instance = theInstance;
  }

  /** Sets the type.
   * @param theType the type, can be null.
   */
  public void setType(final Class<?> theType) {
    type = theType;
  }

  /** {@inheritDoc}.*/
  @Override
  public Object getObject() throws Exception {
    return instance;
  }

  /** {@inheritDoc}.*/
  @Override
  public Class<?> getObjectType() {
    return type;
  }

  /** {@inheritDoc}.*/
  @Override
  public boolean isSingleton() {
    return true;
  }
}
