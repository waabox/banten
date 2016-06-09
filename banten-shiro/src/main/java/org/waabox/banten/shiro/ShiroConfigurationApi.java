package org.waabox.banten.shiro;

import java.util.*;

import org.apache.commons.lang3.Validate;

import org.apache.shiro.realm.Realm;
import org.waabox.banten.core.BeanBuilder;
import org.waabox.banten.core.ConfigurationApi;

/** Configuration API for ShiroConfiguration.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class ShiroConfigurationApi extends ConfigurationApi {

  /** The realm bean. */
  private boolean realmDefined = false;

  /** The Shiro views bean. */
  private ShiroViews shiroViews;

  /** The list of URL to Role Mappings. */
  private List<UrlToRoleMapping> mappings = new LinkedList<>();

  /** The key is the URL and the value the roles. */
  private Map<String, List<String>> urlRoles = new HashMap<>();

  /** Registers the given Realm into the Spring context.
   * @param realm the realm to register, cannot be null.
   */
  public ShiroConfigurationApi registerRealm(
      final Class<? extends Realm> realm) {
    Validate.notNull(realm, "The realm cannot be null");

    registerBean(new BeanBuilder(realm, false).markAsLazy());

    realmDefined = true;
    return this;
  }

  /** Register the {@link UrlToRoleMapping} belongs to a module.
   * @param mapping the list of {@link UrlToRoleMapping}, cannot be null.
   */
  public ShiroConfigurationApi register(final UrlToRoleMapping...mapping) {
    Validate.notNull(mapping, "The mappings cannot be null");
    mappings.addAll(Arrays.asList(mapping));
    for (UrlToRoleMapping urlToRoleMapping : mapping) {
      urlRoles.put(urlToRoleMapping.getUrl(), urlToRoleMapping.getRoles());
    }
    return this;
  }

  /** Configures the views for each authentication & authorization.
   * @param loginUrl the login URL, cannot be null.
   * @param successUrl the success URL once the login process has succeed,
   *  cannot be null.
   * @param unauthorizedUrl the unauthorized URL, cannot be null.
   */
  public ShiroConfigurationApi configureViews(
      final String loginUrl,
      final String successUrl,
      final String unauthorizedUrl) {
    shiroViews = new ShiroViews(loginUrl, successUrl, unauthorizedUrl);
    return this;
  }


  /** {@inheritDoc}.*/
  @Override
  protected void init() {
    Validate.isTrue(realmDefined,
        "ShiroConfiguration Realm _Must_ be defined");
    Validate.notNull(shiroViews,
        "Shiro views not defined, see ShiroConfigurationApi#configureViews");
  }

  /** Retrieves the mappings.
   * @return the mappings
   */
  public List<UrlToRoleMapping> getMappings() {
    return mappings;
  }

  /** Retrieves the roles for the requested URL.
   * @param url the URL, cannot be null.
   * @return the list of roles, or an empty list if does not have a definition
   * for the given URL.
   */
  public List<String> rolesFor(final String url) {
    Validate.notNull(url, "The url cannot be null");
    if (urlRoles.containsKey(url)) {
      return urlRoles.get(url);
    }
    return new LinkedList<>();
  }

  /** Retrieves a set with the defined roles within the application.
   * @return the list of roles defined for each endpoint in the application.
   */
  public Set<String> getDefinedRoles() {
    Set<String> defined = new HashSet<>();
    for (UrlToRoleMapping mapping : mappings) {
      defined.addAll(mapping.getRoles());
    }
    return defined;
  }

  /** Retrieves the shiroViews.
   * @return the shiroViews
   */
  public ShiroViews getShiroViews() {
    return shiroViews;
  }

}
