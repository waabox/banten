/* vim: set et sw=2 cindent fo=qroca: */

package org.waabox.banten.core;

/** The module description.
 *
 * Banten adds this to the module private spring application context. This
 * description includes the module name, the module classpath and the module
 * namespace.
 */
public class ModuleDescription {

  /** The module name, never null. */
  private String name;

  /** The module classpath, the package of the module class, never null. */
  private String classpath;

  /** The module namespace, never null. */
  private String namespace;

  /** The relative path of this module, never null. */
  private String relativePath;

  /** Creates a module description.
   *
   * @param theName the module name. It cannot be null.
   *
   * @param theClasspath the module classpath, ie: the package of the module
   * class. It cannot be null.
   *
   * @param theNamespace the module namespace. It cannot be null.
   *
   * @param theRelativePath the file system relative path of this module, to
   * find static resources from the file system in debug mode.
   */
  ModuleDescription(final String theName, final String theClasspath,
      final String theNamespace, final String theRelativePath) {
    name = theName;
    classpath = theClasspath;
    namespace = theNamespace;
    relativePath = theRelativePath;
  }

  /** Obtains the module name.
   *
   * @return the module name, never null.
   */
  public String getName() {
    return name;
  }

  /** Obtains the module classpath, the package of the module class.
   *
   * @return the module classpath, never null.
   */
  public String getClasspath() {
    return classpath;
  }

  /** Obtains the module namespace.
   *
   * @return the module namespace, never null.
   */
  public String getNamespace() {
    return namespace;
  }

  /** Obtains the module file system relative path.
   *
   * @return the module relative path, never null.
   */
  public String getRelativePath() {
    return relativePath;
  }
}

