package org.waabox.banten.web.sitemesh;

import java.io.IOException;

import org.apache.commons.lang3.Validate;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.sitemesh.content.ContentProcessor;

import org.sitemesh.webapp.SiteMeshFilter;
import org.sitemesh.webapp.WebAppContext;
import org.sitemesh.webapp.contentfilter.ResponseMetaData;
import org.waabox.banten.web.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/** Sitemesh filter configuration.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class BantenSiteMeshFilter extends ConfigurableSiteMeshFilter {

  /** The debug mode. */
  private final boolean debugMode;

  /** The sitemesh's configuration, cannot be null.*/
  private final BantenSitemeshDecoratorSelector bantenSelector;

  /** The freemarker configuration, can be null.*/
  private final Configuration freemarkerConfiguration;

  /** Creates a new instance of the Filter.
   * @param selector the decorator selector.
   * @param configuration the decorator configuration.
   */
  public BantenSiteMeshFilter(
      final boolean theDebugMode,
      final FreeMarkerConfigurer theFreeMarkerConfigurer,
      final BantenSitemeshDecoratorSelector selector) {
    Validate.notNull(selector, "The bantenSelector cannot be null");
    Validate.notNull(theFreeMarkerConfigurer,
        "The freemarker configuration cannot be null");
    debugMode = theDebugMode;
    try {
      freemarkerConfiguration = theFreeMarkerConfigurer.createConfiguration();
    } catch (IOException | TemplateException e) {
      throw new RuntimeException("Could not initialiaze freeMarker.", e);
    }
    bantenSelector = selector;
  }

  /** {@inheritDoc}.*/
  @Override
  protected void applyCustomConfiguration(
      final SiteMeshFilterBuilder builder) {
    builder.setCustomDecoratorSelector(bantenSelector);
  }

  /** {@inheritDoc}.*/
  @Override
  protected boolean reloadRequired() {
    return debugMode;
  }

  /** {@inheritDoc}.*/
  @Override
  protected Filter setup() throws ServletException {

    SiteMeshFilterBuilder builder;
    builder = new SiteMeshFilterBuilder() {

      // Begin inner class of SiteMeshFilterBuilder.
      /** {@inheritDoc}.*/
      public Filter create() {

        final ContentProcessor processor = getContentProcessor();
        final boolean includeErrorPages = isIncludeErrorPages();

        SiteMeshFilter filter = new SiteMeshFilter(
            getSelector(),
            getContentProcessor(),
            getDecoratorSelector(),
            isIncludeErrorPages()) {

          // begin inner class of SiteMeshFilter
          /** {@inheritDoc}.*/
          protected WebAppContext createContext(
              final String contentType,
              final HttpServletRequest request,
              final HttpServletResponse response,
              final ResponseMetaData metaData) {

            return new FreemarkerWebAppContext(
                contentType,
                request,
                response,
                request.getServletContext(),
                processor,
                metaData,
                includeErrorPages,
                freemarkerConfiguration
             );
          }
        };
        // end inner class of SiteMeshFilter
        return filter;
      }
    };
    // end inner class of SiteMeshFilterBuilder

    applyCustomConfiguration(builder);

    return builder.create();
  }
}
