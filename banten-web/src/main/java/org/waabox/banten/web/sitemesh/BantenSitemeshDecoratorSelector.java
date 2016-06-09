/* vim: set et ts=2 sw=2 cindent fo=qroca: */

package org.waabox.banten.web.sitemesh;

import java.io.IOException;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;

import org.springframework.http.MediaType;

import org.sitemesh.DecoratorSelector;
import org.sitemesh.content.Content;
import org.sitemesh.webapp.WebAppContext;

/** Sitemesh decorator selector that chooses a decorator based on four
 * conditions:
 *
 * 1- the user is anonymous.
 * 2- the user is identified.
 * 3- the application is rendering a modal page/dialog.
 * 4- the client is hitting an api.
 * 5- the controller that
 *
 * The intention is that anonymous and identified users see different
 * decorators, and modals and api dont get decorated at all.
 *
 * To decide if a user is anonymous or identified, this module depends on
 * apache shiro. If shiro is not configured, this selector will use
 * decorator.ftl as the only decorator.
 *
 * This selector considers that the client is requesting a modal if the url
 * contains the string '/modal/'.
 *
 * This selector considers that the client is hitting an api if:
 *
 * 1- the content type is json.
 * 2- the client sends the header 'X-Requested-With' with the value
 * 'XMLHttpRequest'.
 * 3- the requested url contains the string '/api/'
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class BantenSitemeshDecoratorSelector
  implements DecoratorSelector<WebAppContext> {

  /** {@inheritDoc}.*/
  @Override
  public String[] selectDecoratorPaths(
      final Content content, final WebAppContext context) throws IOException {

    if (isAjax(context) || isModal(context) || isApiCall(context)
        || explicitNoDecorate(context)) {
      return new String[0];
    }

    boolean isSecurityConfigured = false;

    try {
      SecurityUtils.getSecurityManager();
      isSecurityConfigured = true;
    } catch (UnavailableSecurityManagerException e) {
      // The UnavailableSecurityManagerException when shiro is not
      // configured.
      isSecurityConfigured = false;
    }

    if (!isSecurityConfigured) {
      // No security configured, use the default decorator always.
      return new String[] {"decorator.ftl"};
    }

    if (SecurityUtils.getSubject().isAuthenticated()) {
      return new String[] {"decorator.ftl"};
    } else {
      return new String[] {"decoratorAnonymous.ftl"};
    }
  }

  /** Use a request attribute called: "::decoratePage" and checks that its
   * value is false in the scenario that the requested page does not need
   * decoration. If not present, will return false.
   *
   * @param context the Sitemesh's context.
   * @return true if should not decorate the page.
   */
  private boolean explicitNoDecorate(final WebAppContext context) {
    Object value = context.getRequest().getAttribute("::decoratePage");
    if (value != null) {
      return ((Boolean) value == false);
    }
    return false;
  }

  /** Checks whether or not the given context's path matches with "api".
   * @param context the Sitemesh's context.
   * @return true if matches.
   */
  private boolean isApiCall(final WebAppContext context) {
    return context.getPath().contains("/api/");
  }

  /** Checks whether or not the given context's path matches with "modal".
   * @param context the Sitemesh's context.
   * @return true if matches.
   */
  private boolean isModal(final WebAppContext context) {
    return context.getPath().contains("/modal/");
  }

  /** Checks whether or not the request came from an ajax call.
  *
  * We consider an ajax call when the response is of type json, or the request
  * contains the header "X-Requested-With: XMLHttpRequest".
  *
  * @param context the Sitemesh's context.
  *
  * @return true if matches.
  */
  private boolean isAjax(final WebAppContext context) {
    String requestedWith = context.getRequest().getHeader("X-Requested-With");
    if ("XMLHttpRequest".equals(requestedWith)) {
      return true;
    } else {
      return context.getContentType().equals(MediaType.APPLICATION_JSON_VALUE);
    }
  }
}

