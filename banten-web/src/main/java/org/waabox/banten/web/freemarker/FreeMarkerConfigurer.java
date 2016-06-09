package org.waabox.banten.web.freemarker;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.waabox.banten.web.WebletDirective;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/** Banten freemarker configurer.
 */
public class FreeMarkerConfigurer extends
    org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer {

  /** True when runnig in debug mode.
   */
  private boolean debugMode;

  /** The file system relative path of the templates, never null.
   */
  private String relativePath;

  /** The base path to look for templates.
   */
  private String[] templatePaths;

  /** Constructor, creates a FreeMarkerConfigurer.
   *
   * @param theDebugMode true if the application was run in debug mode.
   *
   * @param theRelativePath the file system relative path of the templates. It
   * cannot be null.
   *
   * @param theTemplatePaths the base paths where to look for templates. It
   * cannot be null.
   */
  public FreeMarkerConfigurer(final boolean theDebugMode,
      final String theRelativePath, final String ... theTemplatePaths) {
    Validate.notNull(theTemplatePaths, "The template paths cannot be null.");
    Validate.notEmpty(theTemplatePaths, "You must specify at least one path");
    debugMode = theDebugMode;
    relativePath = theRelativePath;
    templatePaths = theTemplatePaths;
  }

  @Override
  public Configuration createConfiguration()
      throws IOException, TemplateException {

    List<String> allPaths = new LinkedList<String>();
    for (String path : templatePaths) {
      if (path.startsWith("classpath:")) {
        String suffix = path.substring("classpath:".length());
        // The suffix cannot start with "/".
        Validate.isTrue(!suffix.startsWith("/"),
            "Classpath resource cannot start with /");

        if (debugMode) {
          // Only add the file system relative path in debug mode.
          String fsPath =  "file:" + relativePath;
          if (!relativePath.endsWith("/")) {
            fsPath += "/";
          }
          fsPath += "src/main/resources/" + suffix;
          allPaths.add(fsPath);
        }
      }
    }
    allPaths.addAll(Arrays.asList(templatePaths));
    setTemplateLoaderPaths(allPaths.toArray(new String[0]));
    setDefaultEncoding("UTF-8");
    setPreferFileSystemAccess(false);
    HashMap<String, Object> variables = new HashMap<>();
    variables.put("weblet", new WebletDirective());
    setFreemarkerVariables(variables);

    return super.createConfiguration();
  }
}

