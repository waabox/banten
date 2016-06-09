package org.waabox.banten.core;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;

import org.junit.Test;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.support.GenericApplicationContext;
import org.waabox.banten.core.ModuleServiceLocator;

public class ModuleServiceLocatorTest {

  ModuleServiceLocator serviceLocator;
  GenericApplicationContext ctx = new GenericApplicationContext();

  @Before public void before() {
    serviceLocator = new ModuleServiceLocator();
    serviceLocator.setApplicationContext(ctx);
    BeanDefinition beanDefinition = new GenericBeanDefinition();
    beanDefinition.setBeanClassName(SimpleObject.class.getName());
    ctx.registerBeanDefinition("simpleObject", beanDefinition);
    ctx.refresh();
  }

  @After public void after() {
    ctx.close();
  }

  @Test public void getBean_byClass() {
    SimpleObject returned = new SimpleObject();
    ModuleServiceLocator.getBean(SimpleObject.class);

    assertTrue(returned instanceof SimpleObject);
  }

  @Test public void getBean_byName() {
    SimpleObject returned = new SimpleObject();
    ModuleServiceLocator.getBean("simpleObject");

    assertTrue(returned instanceof SimpleObject);
  }

  private static class SimpleObject {
  }

}
