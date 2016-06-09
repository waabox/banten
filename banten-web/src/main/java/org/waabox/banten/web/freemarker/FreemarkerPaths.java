package org.waabox.banten.web.freemarker;

/** Holds the information where Freemarker templates are stored and its
 * debug relative-path (when we are in debug mode).
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class FreemarkerPaths {

  /** The classpath location, it's never null.*/
  private final String classpathLocation;

  /** The template path, it's never null.*/
  private final String templatePath;

  /** Creates a new instance of the
   * @param theClasspathLocation
   * @param theTemplatePath
   */
  public FreemarkerPaths(final String theClasspathLocation,
      final String theTemplatePath) {
    classpathLocation = theClasspathLocation;
    templatePath = theTemplatePath;
  }

  /** Retrieves the classpathLocation.
   * @return the classpath location.
   */
  public String getClasspathLocation() {
    return classpathLocation;
  }

  /** Retrieves the templatePath.
   * @return the template path.
   */
  public String getTemplatePath() {
    return templatePath;
  }

}
