package org.waabox.banten.web.freemarker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.ViewResolver;

/** Base MVC configuration for Freemarker.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public abstract class FreemarkerMvc {

  @Bean public ViewResolver viewResolver() {
    return new FreeMarkerViewResolver();
  }

  @Bean public FreeMarkerConfigurer freemarkerConfig(
    @Value("${debugMode:false}") final boolean debugMode) {
    return new FreeMarkerConfigurer(debugMode, paths().getTemplatePath(),
        paths().getClasspathLocation());
  }

  /** Retrieves the {@link FreemarkerPaths}.
   * @return the Freemarker configuration, never null.
   */
  protected abstract FreemarkerPaths paths();

}
