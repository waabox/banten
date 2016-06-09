package org.waabox.banten.core;

import static org.easymock.EasyMock.*;

import org.junit.Test;
import static org.junit.Assert.*;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.waabox.banten.core.ObjectFactoryBean;

public class ObjectFactoryBeanTest {

  @Test public void testRegister() {

    BeanDefinitionRegistry registry = createMock(BeanDefinitionRegistry.class);

    registry.registerBeanDefinition(eq("test"),
        isA(GenericBeanDefinition.class));

    replay(registry);

    ObjectFactoryBean.register(registry, Object.class, new Object(), "test");

    verify(registry);
  }

  @Test public void test() throws Exception {
    ObjectFactoryBean factoryBean = new ObjectFactoryBean();

    Object instance = new Object();

    factoryBean.setInstance(instance);
    factoryBean.setType(Object.class);

    assertTrue(factoryBean.isSingleton());
    assertTrue(factoryBean.getObject() == instance);
    assertTrue(factoryBean.getObjectType() == Object.class);
  }

}
