package org.waabox.banten.shiro;

import org.apache.commons.lang3.Validate;

/** Configures the Shiro's views (Login, Success, unauthorized).
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class ShiroViews {

  /** The login URL, it's never null. */
  private final String loginUrl;

  /** The login URL, it's never null. */
  private final String successUrl;

  /** The unauthorized URL, it's never null. */
  private final String unauthorizedUrl;

  /** Configures the views for each authentication & authorization.
   * @param loginUrl the login URL, cannot be null.
   * @param successUrl the success URL once the login process has succeed,
   *  cannot be null.
   * @param unauthorizedUrl the unauthorized URL, cannot be null.
   */
  public ShiroViews(final String aLoginUrl, final String aSuccessUrl,
      final String aUnauthorizedUrl) {
    Validate.notNull(aLoginUrl, "login  URL _Must_ be defined");
    Validate.notNull(aSuccessUrl, "successUrl URL _Must_ be defined");
    Validate.notNull(aUnauthorizedUrl, "unauthorized URL _Must_ be defined");
    loginUrl = aLoginUrl;
    successUrl = aSuccessUrl;
    unauthorizedUrl = aUnauthorizedUrl;
  }

  /** Retrieves the loginUrl.
   * @return the loginUrl
   */
  public String getLoginUrl() {
    return loginUrl;
  }

  /** Retrieves the successUrl.
   * @return the successUrl
   */
  public String getSuccessUrl() {
    return successUrl;
  }

  /** Retrieves the unauthorizedUrl.
   * @return the unauthorizedUrl
   */
  public String getUnauthorizedUrl() {
    return unauthorizedUrl;
  }
}

