package org.waabox.banten.login.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.*;

import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.waabox.banten.hibernate.Transaction;
import org.waabox.banten.login.domain.Role;
import org.waabox.banten.login.domain.User;
import org.waabox.banten.login.domain.UserRepository;

/** Hibernate's Real that use the {@link User} as principal.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class BantenLoginRealm extends AuthorizingRealm {

  /** The user repository, it's never null. */
  @Autowired private UserRepository repository;

  /** The platform transaction. */
  @Autowired private Transaction transaction;

  /** Creates a new instance of the Realm.*/
  public BantenLoginRealm() {
    setName("banten.realm");
  }

  /** Creates a new instance of the Realm.
   * @param theUserRepository the {@link UserRepository}, cannot be null.
   * @param theTransaction the {@link Transaction}, cannot be null.
   */
  public BantenLoginRealm(final UserRepository theUserRepository,
      final Transaction theTransaction) {
    this();
    repository = theUserRepository;
    transaction = theTransaction;
  }

  /** {@inheritDoc}.*/
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(
      final PrincipalCollection pc) {
    try {
      transaction.start();

      Long id = (Long) pc.getPrimaryPrincipal();

      User user = repository.getById(id);

      if (user != null) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        for (Role permission : user.getRoles()) {
          info.addRole(permission.getName());
        }
        return info;
      }

      return null;
    } finally {
      transaction.cleanup();
    }
  }

  /** {@inheritDoc}.*/
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(
      final AuthenticationToken authcToken) {
    try {
      transaction.start();
      UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
      User user = repository.getByEmail(token.getUsername());
      if (user != null) {
        return new SimpleAuthenticationInfo(user.getId(), user.getPassword(),
            getName());
      } else {
        throw new UnknownAccountException(
            "No account found for user [" + token.getUsername() + "]");
      }
    } finally {
      transaction.cleanup();
    }
  }

}
