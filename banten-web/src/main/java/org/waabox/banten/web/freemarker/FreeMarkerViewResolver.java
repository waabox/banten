package org.waabox.banten.web.freemarker;

/** View resolver that uses banten FreeMarkerView class.
 *
 * By default, this view resolver sets the content type to text/html in utf-8,
 * exposes the request attributes to the template, and expects .ftl sufix.
 */
public class FreeMarkerViewResolver extends
    org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver {

  /** FreeMarkerViewResolver constructor.
   */
  public FreeMarkerViewResolver() {
    setSuffix(".ftl");
    setExposeRequestAttributes(true);
    setContentType("text/html; charset=utf-8");
  }

  /** Sets the view name to katari's FreemarkerView.
   */
  @Override
  protected Class<FreeMarkerView> requiredViewClass() {
    return FreeMarkerView.class;
  }
}

