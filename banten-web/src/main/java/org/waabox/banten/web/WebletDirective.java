package org.waabox.banten.web;

import static org.waabox.banten.web.WebletRendererRegistrationFilter.*;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.*;

import freemarker.core.Environment;
import freemarker.template.*;
import freemarker.template.utility.DeepUnwrap;

/** Weblet renderer freemarker template directive.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class WebletDirective implements TemplateDirectiveModel {

  /** {@inheritDoc}.*/
  @Override @SuppressWarnings({ "rawtypes", "unchecked" })
  public void execute(final Environment env, final Map parameters,
      final TemplateModel[] loopVars, final TemplateDirectiveBody body)
      throws TemplateException, IOException {

    HttpServletRequest request = Context.request();
    HttpServletResponse response = Context.response();

    String moduleName = ((SimpleScalar) parameters.get("module")).getAsString();
    String webletName = ((SimpleScalar) parameters.get("name")).getAsString();

    Set<Entry> entries = parameters.entrySet();
    for (Entry entry : entries) {
      if (!(entry.getKey().equals("module")
          || entry.getKey().equals("name"))) {
        request.setAttribute((String) entry.getKey(),
            DeepUnwrap.unwrap((TemplateModel) entry.getValue()));
      }
    }

    WebletRenderer renderer;
    renderer = (WebletRenderer) request.getAttribute(WEBLET_RENDERER_PARAMETER);

    try {
      String html = renderer.render(moduleName, webletName, request, response);
      env.getOut().append(html);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
