package org.waabox.banten.shiro;

import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import static org.apache.shiro.subject.support.DefaultSubjectContext.*;

import org.apache.shiro.authc.AuthenticationToken;

import org.apache.shiro.mgt.SecurityManager;

import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;

/** A Shiro's subject that obtains it login information from a browser cookie
 * generated session.
 *
 * This subject lets you support 'stateless' web applications with very little
 * state in its 'session'. It knows about http requests and responses, and can
 * read and write state to a cookie.
 *
 * See {@link BantenSession} and {@link BantenWebSessionManager}
 * for more information.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class BantenSubject extends WebDelegatingSubject {

  /** The class logger. */
  private final Logger log = getLogger(BantenSubject.class);

  /** The current session, initialized from a request cookie. It is never null.
   */
  private final BantenSession currentSession;

  /** Constructor, creates a new BantenSubject.
   *
   * @param principals the principals that identifies this subject. May be null
   *  if the subject is not authenticated.
   * @param authenticated indicates if the subject is authenticated.
   * @param host the host that originated the request.
   * @param session a session that should be bound to this subject. If null,
   *  this operation creates a new session from the request cookie.
   * @param sessionEnabled indicates if sessions are enabled. This
   *   implementation needs this to be true.
   * @param theRequest the servlet request, It cannot be null.
   * @param theResponse the servlet request, It cannot be null.
   * @param securityManager the configured shiro security manager, It cannot
   *   be null.
   */
  public BantenSubject(final PrincipalCollection principals,
      final boolean authenticated, final String host, final Session session,
      final boolean sessionEnabled, final ServletRequest theRequest,
      final ServletResponse theResponse,
      final SecurityManager securityManager) {

    super(principals, authenticated, host, session, sessionEnabled, theRequest,
        theResponse, securityManager);

    currentSession = (BantenSession) session;

    PrincipalCollection sessionPrincipals = getPrincipals(currentSession);

    if (sessionPrincipals != null && super.principals == null) {
      // Copy principals from session.
      super.principals = sessionPrincipals;
    }

    if (authenticated) {
      session.setAttribute(AUTHENTICATED_SESSION_KEY, Boolean.TRUE);
      super.authenticated = true;
    } else {
      Object auth = session.getAttribute(AUTHENTICATED_SESSION_KEY);
      auth = session.getAttribute(AUTHENTICATED_SESSION_KEY);
      if (auth != null && ((boolean) auth)) {
        super.authenticated = true;
      } else {
        super.authenticated = false;
      }
    }
  }

  /** We override this because the SaveSessionFilter needs a K2Session to
   * store it in a cookie.
   */
  @Override
  public BantenSession getSession(final boolean create) {
    return currentSession;
  }

  /** Attempts login (as implemented in the superclass), and stores a the
   * principal in the session.
   */
  @Override
  public void login(final AuthenticationToken token) {
    log.trace("Entering login");
    super.login(token);
    session.setAttribute(AUTHENTICATED_SESSION_KEY, Boolean.TRUE);
    log.trace("Leaving login");
  }

  /** Retrieves the {@link PrincipalCollection} from the {@link BantenSession}.
   * @param session the {@link BantenSession}, cannot be null.
   * @return the {@link PrincipalCollection} or null.
   */
  private PrincipalCollection getPrincipals(final BantenSession session) {
    return (PrincipalCollection) session.getAttribute(PRINCIPALS_SESSION_KEY);
  }

}
