package org.waabox.banten.web.menu;

import java.util.List;

import org.apache.commons.lang3.Validate;
import org.waabox.banten.shiro.ShiroConfigurationApi;

/** Service that provides {@link Role}s information based on URLs.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class SecuredUrlService {

  /** The {@link ShiroConfigurationApi}, it's never null. */
  private final ShiroConfigurationApi shiroConfigurationApi;

  /** Creates a new instance of the Service.
   * @param api the {@link ShiroConfigurationApi}, cannot be null.
   */
  public SecuredUrlService(final ShiroConfigurationApi api) {
    Validate.notNull(api, "The configuration api cannot be null");
    shiroConfigurationApi = api;
  }

  /** Retrieves the roles for the requested URL.
   * @param url the URL, cannot be null.
   * @return the list of roles, or an empty list if does not have a definition
   * for the given URL.
   */
  public List<String> rolesFor(final String url) {
    return shiroConfigurationApi.rolesFor(url);
  }

}
