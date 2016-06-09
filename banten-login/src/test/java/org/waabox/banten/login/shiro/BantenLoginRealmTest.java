package org.waabox.banten.login.shiro;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.junit.Before;

import static org.easymock.EasyMock.*;

import org.junit.Test;
import org.waabox.banten.hibernate.Transaction;
import org.waabox.banten.login.domain.Role;
import org.waabox.banten.login.domain.User;
import org.waabox.banten.login.domain.UserRepository;
import org.waabox.banten.login.shiro.BantenLoginRealm;

public class BantenLoginRealmTest {

  private Transaction transaction = createMock(Transaction.class);
  private UserRepository userRepository = createMock(UserRepository.class);

  @Before public void before() {
    transaction.start();
    transaction.cleanup();
    replay(transaction);
  }

  @Test public void doGetAuthorizationInfo() {
    Set<Role> permissions = new HashSet<>();
    permissions.add(new Role("execute", "execute a command bleh"));
    User user = new User("me@waabox.org", "apassword", permissions);

    PrincipalCollection pc = createMock(PrincipalCollection.class);
    expect(pc.getPrimaryPrincipal()).andReturn(1l);
    expect(userRepository.getById(1l)).andReturn(user);

    replay(userRepository, pc);

    BantenLoginRealm realm = new BantenLoginRealm(userRepository, transaction);
    AuthorizationInfo info = realm.doGetAuthorizationInfo(pc);

    assertThat(info.getRoles().iterator().next(), is("execute"));
    assertThat(info.getRoles().size(), is(1));

    verify(userRepository, pc, transaction);
  }

  @Test public void doGetAuthorizationInfo_userNotFound() {

    PrincipalCollection pc = createMock(PrincipalCollection.class);
    expect(pc.getPrimaryPrincipal()).andReturn(1l);
    expect(userRepository.getById(1l)).andReturn(null);

    replay(userRepository, pc);

    BantenLoginRealm realm = new BantenLoginRealm(userRepository, transaction);
    assertThat(realm.doGetAuthorizationInfo(pc), nullValue());


    verify(userRepository, pc, transaction);
  }

  @Test public void doGetAuthenticationInfo() {
    Set<Role> permissions = new HashSet<>();
    permissions.add(new Role("execute", "execute a command bleh"));
    User user = new User("me@waabox.org", "apassword", permissions);

    UsernamePasswordToken at = createMock(UsernamePasswordToken.class);
    expect(at.getUsername()).andReturn("me@waabox.org");
    expect(userRepository.getByEmail("me@waabox.org")).andReturn(user);

    replay(userRepository, at);

    BantenLoginRealm realm = new BantenLoginRealm(userRepository, transaction);
    SimpleAuthenticationInfo info;
    info = (SimpleAuthenticationInfo) realm.doGetAuthenticationInfo(at);
    assertThat((String) info.getCredentials(), is("apassword"));

    verify(userRepository, at, transaction);
  }

  @Test public void doGetAuthenticationInfo_userNotFound() {

    UsernamePasswordToken at = createMock(UsernamePasswordToken.class);
    expect(at.getUsername()).andReturn("me@waabox.org").times(2);
    expect(userRepository.getByEmail("me@waabox.org")).andReturn(null);

    replay(userRepository, at);

    BantenLoginRealm realm = new BantenLoginRealm(userRepository, transaction);
    try {
      realm.doGetAuthenticationInfo(at);
      fail("Should fail because the account does not exist");
    } catch (UnknownAccountException uae) {
    }

    verify(userRepository, at, transaction);
  }

}
