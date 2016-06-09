package org.waabox.banten.shiro;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;

public class BantenWebSessionManager implements SessionManager {

  /** The client's secret, it's never null.*/
  private final String clientSecret;

  /** Creates a new instance of the {@link BantenWebSessionManager}.
   * @param theClientSecret the lcinet secret.
   */
  public BantenWebSessionManager(final String theClientSecret) {
    clientSecret = theClientSecret;
  }

  /** {@inheritDoc}.*/
  @Override
  public Session start(final SessionContext context) {
    throw new RuntimeException("Should not create a session here");
  }

  /** {@inheritDoc}.*/
  @Override
  public Session getSession(final SessionKey sessionKey) {
    WebSessionKey key = (WebSessionKey) sessionKey;

    HttpServletRequest request;
    request = (HttpServletRequest) key.getServletRequest();

    HttpServletResponse response;
    response = (HttpServletResponse) key.getServletResponse();

    return new BantenSession(
        clientSecret,
        request.getRemoteHost(),
        request,
        response);
  }

}
