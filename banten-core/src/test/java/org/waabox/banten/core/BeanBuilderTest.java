package org.waabox.banten.core;

import static org.easymock.EasyMock.*;

import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.waabox.banten.core.BeanBuilder;

public class BeanBuilderTest {

  @Test public void test_genericBean() {
    BeanBuilder builder = new BeanBuilder(Object.class, false);

    BeanDefinitionRegistry registry = createMock(BeanDefinitionRegistry.class);

    registry.registerBeanDefinition("waabox", builder.getBean());

    replay(registry);

    builder.withConstructorArguments(
        new BeanBuilder.ConstructorArgument(0, new Object())
    );
    builder.withName("waabox");

    builder.withScope(BeanDefinition.SCOPE_PROTOTYPE);

    builder.register(registry);

    verify(registry);
  }

  @Test public void test_annotated() {
    BeanBuilder builder = new BeanBuilder(Object.class, true);

    BeanDefinitionRegistry registry = createMock(BeanDefinitionRegistry.class);

    registry.registerBeanDefinition("waabox", builder.getBean());

    replay(registry);

    builder.withConstructorArguments(
        new BeanBuilder.ConstructorArgument(0, new Object())
    );
    builder.withName("waabox");
    builder.register(registry);

    verify(registry);
  }

}
