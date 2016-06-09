package org.waabox.banten.web.sitemesh;

import org.apache.commons.lang3.Validate;
import org.waabox.banten.web.freemarker.FreeMarkerConfigurer;

/** Sitemesh decorator configuration.
 *
 * Banten users use this class to configure sitemesh when adding the sitemesh
 * module to their application. Simply add a bean of this type to the banten
 * application subclass or any of the modules.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class SitemeshConfiguration {

  /** The file system relative path of the decorators, never null.
   */
  private String relativePath;

  /** The freemarker template loader paths where sitemesh will look for the
   * decorators.
   *
   * This is never null.
   */
  private String[] templateLoaderPaths;

  /** Creates an sitemesh decorator configuration.
   *
   * @param theRelativePath the relative path of the module that contains the
   * decorators in the file system. This assumes a standard maven file system
   * layout, ie: if a decorator is in classpath:org.waabox/d/x.ftl,
   * sitemesh will look for it in
   * relativePath/src/main/resources/org.waabox/d/x.ftl. This cannot be null.
   *
   * @param paths the paths to look for decorators for. It cannot be null.
   */
  SitemeshConfiguration(final String theRelativePath,
      final String ... paths) {
    Validate.notNull(theRelativePath, "The relative path cannot be null");
    Validate.notNull(paths, "The paths cannot be null");
    relativePath = theRelativePath;
    templateLoaderPaths = paths;
  }

  /** Creates the freemarker configurer from the information contained in this
   * sitemesh configuration.
   *
   * @param debugMode true if the application is launched in debug mode. This
   * configuration uses this flag to tell freemarker whether to try to load
   * the templates from the file system or only from the classpath.
   *
   * @return the freemarker configurer, never returns null.
   */
  FreeMarkerConfigurer createFreeMarkerConfigurer(final boolean debugMode) {
    FreeMarkerConfigurer configurer;
    configurer = new FreeMarkerConfigurer(debugMode, relativePath,
        templateLoaderPaths);
    return configurer;
  }
}

