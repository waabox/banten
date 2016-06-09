package org.waabox.banten.web.freemarker;

import org.junit.Test;
import org.waabox.banten.web.freemarker.FreeMarkerConfigurer;

/** This test is just to verify how we configure freemarker, that's all.*/
public class FreeMarkerConfigurerTest {

  @Test public void test() throws Exception {
    FreeMarkerConfigurer configurer = new FreeMarkerConfigurer(true,
        "../banten-core", "", "classpath:org.waabox/banten");
    configurer.createConfiguration();
  }

}
