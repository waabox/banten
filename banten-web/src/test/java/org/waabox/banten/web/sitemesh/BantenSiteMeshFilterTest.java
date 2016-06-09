package org.waabox.banten.web.sitemesh;

import static org.junit.Assert.*;

import org.junit.Test;
import org.waabox.banten.web.freemarker.FreeMarkerConfigurer;
import org.waabox.banten.web.sitemesh.BantenSiteMeshFilter;
import org.waabox.banten.web.sitemesh.BantenSitemeshDecoratorSelector;

public class BantenSiteMeshFilterTest {

  @Test
  public void test() throws Exception {
    BantenSitemeshDecoratorSelector selector =
        new BantenSitemeshDecoratorSelector();
    FreeMarkerConfigurer cfg = new FreeMarkerConfigurer(
        true, "../banten-web", "BantenSiteMeshFilter");
    BantenSiteMeshFilter filter =
        new BantenSiteMeshFilter(false, cfg, selector);

    assertNotNull(filter.setup());
  }

}
