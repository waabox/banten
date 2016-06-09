package org.waabox.banten.core;

import java.util.UUID;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.*;

import org.springframework.beans.factory.config.ConstructorArgumentValues;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;

/** Builder for Spring's Beans.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class BeanBuilder {

  /** The Spring's bean, it's never null. */
  private final GenericBeanDefinition bean;

  /** The bean name, by default it's the class name + UUID random value.*/
  private String name;

  /** Whether or not has been registered. */
  private boolean registered = false;

  /** Creates a new instance of the {@link BeanBuilder}.
   * @param beanClass the bean class, cannot be null.
   * @param annotated whether or not we need an
   * {@link AnnotatedBeanDefinition} or not.
   * @return this.
   */
  public BeanBuilder(final Class<?> beanClass, final Boolean annotated) {
    Validate.notNull(beanClass, "The bean class cannot be null");
    if (annotated) {
      bean = new AnnotatedGenericBeanDefinition(beanClass);
    } else {
      bean = new GenericBeanDefinition();
      bean.setBeanClass(beanClass);
    }

    name = beanClass.getName() + "[" +  UUID.randomUUID().toString() + "]";
  }

  /** Sets an specific name for the bean.
   * @param beanName the bean name, cannot be null.
   * @return this.
   */
  public BeanBuilder withName(final String beanName) {
    Validate.notNull(beanName, "The bean name cannot be null");
    name = beanName;
    return this;
  }

  /** Sets the Scope for this bean by default will be default Singleton.
   *
   * @see BeanDefinition#SCOPE_PROTOTYPE
   * @see BeanDefinition#SCOPE_SINGLETON
   *
   * @param scope the Spring's bean scope, cannot be null.
   * @return this.
   */
  public BeanBuilder withScope(final String scope) {
    bean.setScope(scope);
    return this;
  }

  /** Adds the constructor arguments for this bean.
   * @param values a key value that holds the constructor index as key and
   * the object as the constructor value.
   * @return this.
   */
  public BeanBuilder withConstructorArguments(
    final ConstructorArgument... arguments) {

    Validate.notNull(arguments, "The constructor values cannot be null");

    ConstructorArgumentValues constructorArgumentValues;
    constructorArgumentValues = new ConstructorArgumentValues();

    for (ConstructorArgument arg : arguments) {
      constructorArgumentValues.addIndexedArgumentValue(arg.getKey(),
          arg.getValue());
    }

    bean.setConstructorArgumentValues(constructorArgumentValues);
    return this;
  }

  /** Retrieves the bean.
   * @return the bean, never null.
   */
  public GenericBeanDefinition getBean() {
    return bean;
  }

  /** Registers this bean into the Spring's context.
   * @param registry the {@link BeanDefinitionRegistry}, cannot be null.
   */
  protected void register(final BeanDefinitionRegistry registry) {
    Validate.notNull(registry, "The registry cannot be null");

    Validate.isTrue(registered == false, "Bean already registered!");

    registry.registerBeanDefinition(name, getBean());

    registered = true;
  }

  /** Marks this bean as Lazy.
   * @return this.
   */
  public BeanBuilder markAsLazy() {
    bean.setLazyInit(true);
    return this;
  }

  /** Set dependencies for this bean.
   * @param depends the list of dependency beans.
   * @return this.
   */
  public BeanBuilder dependsOn(final String ... depends) {
    bean.setDependsOn(depends);
    return this;
  }

  /** A simple key->value store for the constructor parameter.*/
  public static class ConstructorArgument {

    /** The key value 'key'. */
    private final int key;

    /** The value. */
    private final Object value;

    /** Creates a new instance of the
     * @param position the constructor position.
     * @param theValue the value.
     */
    public ConstructorArgument(final int position, final Object theValue) {
      key = position;
      value = theValue;
    }

    /** Retrieves the key.
     * @return the key.
     */
    public int getKey() {
      return key;
    }

    /** Retrieves the value, can be null.
     * @return the value or null.
     */
    public Object getValue() {
      return value;
    }
  }
}
