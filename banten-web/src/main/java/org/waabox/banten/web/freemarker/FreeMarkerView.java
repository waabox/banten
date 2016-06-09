package org.waabox.banten.web.freemarker;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/** Freemarker view that does not expose the model as request attributes.
 *
 * The original freemarker view copies all model objects to the request object.
 * This was used to support taglibs as freemarker macros. But it makes weblets
 * fail if the configurer uses 'exposeRequestAttrubutes'.
 */
public class FreeMarkerView extends
    org.springframework.web.servlet.view.freemarker.FreeMarkerView {

  /** Overrides AbstractView#exposeModelAsRequestAttributes, removing all
   *  behavior.
   *
   *  {@inheritDoc}
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  protected void exposeModelAsRequestAttributes(final Map model,
      final HttpServletRequest request) throws Exception {
    model.put("request", request);
  }
}
