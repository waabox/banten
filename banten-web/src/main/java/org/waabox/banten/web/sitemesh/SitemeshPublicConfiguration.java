package org.waabox.banten.web.sitemesh;

import java.io.IOException;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.waabox.banten.web.freemarker.FreeMarkerConfigurer;

import freemarker.template.TemplateException;

/** Banten sitemesh module public configuration.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
@Configuration
public class SitemeshPublicConfiguration {

  @Bean(name = "banten.sitemesh.mainFilter")
  public FilterRegistrationBean sitemeshFilter(
      @Value("${debugMode:false}") final boolean debugMode,
      final SitemeshConfiguration configuration)
          throws IOException, TemplateException {

    Validate.notNull(configuration,
        "in order to use sitemesh in your application you must extend the "
        + "method init and configure sitemesh using: \n"
        + "registry.get(SitemeshConfigurationApi.class).configure()");

    BantenSitemeshDecoratorSelector selector;
    selector = new BantenSitemeshDecoratorSelector();

    FreeMarkerConfigurer freemarkerConfigurer;
    freemarkerConfigurer = configuration.createFreeMarkerConfigurer(debugMode);

    FilterRegistrationBean filterBean = new FilterRegistrationBean();
    filterBean.setFilter(new BantenSiteMeshFilter(debugMode,
        freemarkerConfigurer, selector));
    filterBean.addUrlPatterns("/*");

    return filterBean;
  }
}
