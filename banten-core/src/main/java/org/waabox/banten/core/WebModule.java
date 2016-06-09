package org.waabox.banten.core;

/** A web module has a private configuration in order to declare endpoints,
 * repositories, etc that will be used within its own context.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public interface WebModule extends Module {

  /** Retrieves the module namespace, typically this will be the Servlet's
   * context path.
   * @return the name-space, never null.
   */
  String getNamespace();

  /** Retrieves the file system relative path of this module.
  *
  * Bootstrap should return a value of the form "../banten-sample". The banten
  * application searches for resources exposed as static resources from the
  * file system location: <relativePath>/src/main/resources. This makes
  * it possible, for example, to refresh static content sent to the browser.
  *
  * @return the file system relative path, never null.
  */
  String getRelativePath();

  /** Retrieves the MVC configuration, this will be the
   * private Module configuration.
   *
  * @return the configuration, never null.
  */
  Class<?> getMvcConfiguration();

}
