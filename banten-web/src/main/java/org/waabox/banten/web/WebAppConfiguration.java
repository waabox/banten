package org.waabox.banten.web;

import java.io.IOException;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.*;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.view.RedirectView;

/** Web application module public configuration.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class WebAppConfiguration {

  /** Creates the weblet dispatcher.
   *
   * @return the weblet dispatcher.
   */
   @Bean(name = "banten.webletDispatcher")
   public WebletRenderer webletDispatcher() {
     return new WebletRenderer(WebletContainer.instance());
   }

   @Bean(name = "banten.contextFilter")
   public FilterRegistrationBean bantenContextFilter() {
     FilterRegistrationBean filterBean = new FilterRegistrationBean();
     filterBean.setFilter(new ContextFilter());
     filterBean.addUrlPatterns("/*");
     return filterBean;
   }

   /** Retrieves the {@link WebletRendererRegistrationFilter}.
    * @param renderer the {@link WebletRenderer}, cannot be null.
    * @return a {@link FilterRegistrationBean} instance, never null.
    */
   @Bean(name = "banten.webletRenderer")
   public FilterRegistrationBean webletRendererFilter(
       final WebletRenderer renderer) {
     FilterRegistrationBean filterBean = new FilterRegistrationBean();
     filterBean.setFilter(new WebletRendererRegistrationFilter(renderer));
     filterBean.addUrlPatterns("/*");
     return filterBean;
   }

  /** Servlet mapped to the root web application context to redirect to a
   * configurable URL.
   *
   * Banten's maps this servlet to the '/' path. This Servlet just redirects to
   * the configured URL.
   *
   * Banten creates and configures this Servlet obtaining the home URL from
   * the value passed to BantenApplication.setLandingUrl() operation.
   *
   * Note: this Servlet is very naive. We should allow the application to
   * configure a more complex strategy to select the landing URL.
   */
  private static class HomeServlet extends HttpServlet {

    /** The serial version id. */
    private static final long serialVersionUID = 1L;

    /** The configured landing url, never null. */
    private final String landingUrl;

    /** Creates the home Servlet.
     *
     * @param theLandingUrl the landing URL. It cannot be null.
     */
    public HomeServlet(final String theLandingUrl) {
      Validate.notNull(theLandingUrl, "The landing page cannot be null.");
      landingUrl = theLandingUrl;
    }

    /** {@inheritDoc}.*/
    @Override
    public void service(final HttpServletRequest request,
        final HttpServletResponse response)
            throws ServletException, IOException {
      RedirectView redirectView = new RedirectView(landingUrl);
      try {
        redirectView.render(null, request, response);
      } catch (IOException | ServletException e) {
        throw e;
      } catch (Exception e) {
        throw new ServletException("Error redirecting to home page", e);
      }
    }
  }

  /** Creates the home servlet if the application-wise landing url is defined.
   *
   * @param landingUrl the application-wise landing url. Null if not defined.
   * See BantenApplication.setLandingUrl() for more information.
   *
   * @return the home servlet (an instance of HomeServlet) mapped to the root
   * path of the web application context if the landingUrl is not null. If
   * landingUrl is null, this operation returns null and the servlet container
   * will handle all requests to the root path.
   */
  @Bean(name = "banten.homeServlet")
  @ConditionalOnBean(name = "banten.landingUrl")
  public ServletRegistrationBean homeServlet(
      @Qualifier("banten.landingUrl") final String landingUrl) {
  ServletRegistrationBean servletBean = null;
  if (landingUrl != null) {
    HomeServlet servlet = new HomeServlet(landingUrl);
    servletBean = new ServletRegistrationBean(servlet, false, "");
      servletBean.setOrder(Integer.MAX_VALUE);
    }
    return servletBean;
  }

}
