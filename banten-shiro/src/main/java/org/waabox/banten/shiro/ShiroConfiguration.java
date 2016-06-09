package org.waabox.banten.shiro;

import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;

import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.FilterRegistrationBean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.Ordered;

/** Shiro Spring configuration.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
@Configuration
public class ShiroConfiguration {

  /** The log. */
  private final Logger log = getLogger(ShiroConfiguration.class);

  /** Configures the Shiro's Filter.
   * @param securityManager the {@link DefaultSecurityManager}.
   * @param api the {@link ShiroConfigurationApi}, cannot be null.
   * @return the {@link FilterRegistrationBean}, never null.
   */
  @Bean public FilterRegistrationBean shiroFilter(
      final DefaultWebSecurityManager securityManager,
      final ShiroConfigurationApi api) {

    ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
    bean.setSecurityManager(securityManager);

    bean.getFilters().put("saveSession", new BantenSessionStorerFilter());

    Map<String, String> chainDefinitions = new LinkedHashMap<>();

    chainDefinitions.put("/logout", "saveSession, noSessionCreation, logout");
    chainDefinitions.put("/**/static/**", "anon");

    for (UrlToRoleMapping mapping : api.getMappings()) {
      String authc;
      authc = String.format("authc, roles[%s]", mapping.rolesAsString());

      log.debug("Mapping shiro authc: {}", authc);

      chainDefinitions.put(mapping.getUrl(), authc);
    }

    chainDefinitions.put("/**", "saveSession, noSessionCreation, authc");

    bean.setFilterChainDefinitionMap(chainDefinitions);

    ShiroViews shiroViews = api.getShiroViews();
    bean.setLoginUrl(shiroViews.getLoginUrl());
    bean.setUnauthorizedUrl(shiroViews.getUnauthorizedUrl());
    bean.setSuccessUrl(shiroViews.getSuccessUrl());

    try {
      FilterRegistrationBean registration;
      registration = new FilterRegistrationBean((Filter) bean.getObject());
      registration.setName("shiroFilter");
      registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);

      return registration;

    } catch (Exception e) {
      throw new RuntimeException("Error creating shiro filter.", e);
    }
  }

  /** Configures the {@link DefaultWebSecurityManager}.
   * @param encryptionKey the client secret
   * @param realm the {@link Realm}, cannot be null.
   * @return the {@link DefaultWebSecurityManager}, never null.
   */
  @Bean public DefaultWebSecurityManager securityManager(
      @Value("${shiro.encryption.key}") final String encryptionKey,
      final AuthorizingRealm realm) {

    BantenSession.validateKey(encryptionKey);

    DefaultWebSecurityManager sm = new DefaultWebSecurityManager();
    sm.setRealm(realm);
    sm.setSessionManager(new BantenWebSessionManager(encryptionKey));
    sm.setSubjectFactory(new BantenSubjectFactory());
    return sm;
  }

}
