package org.waabox.banten.login;

import org.apache.commons.lang3.Validate;
import org.waabox.banten.core.ConfigurationApi;

/** The login configuration API.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class LoginConfigurationApi extends ConfigurationApi {

  /** The success URL. */
  private String successUrl = "/";

  /** Sets the success URL.
   * @param theSuccessUrl the success URL, cannot be null.
   * @return this.
   */
  public LoginConfigurationApi successUrl(final String theSuccessUrl) {
    Validate.notNull(theSuccessUrl, "Success url cannot be null");
    successUrl = theSuccessUrl;
    return this;
  }

  /** Retrieves the success URL.
   * @return the URl never null.
   */
  public String getSuccessUrl() {
    return successUrl;
  }

}
