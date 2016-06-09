package org.waabox.banten.shiro;

import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

import org.apache.shiro.subject.*;

import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.apache.shiro.web.subject.WebSubjectContext;

/** A shiro subject factory that creates BantenSubject instances.
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class BantenSubjectFactory extends DefaultWebSubjectFactory {

  /** The class logger. */
  private final Logger log = getLogger(BantenSubjectFactory.class);

  /** Creates a new Banten's subject if the caller provided a web context.
   *
   * @param context the subject context used to create the subject.
   * It must be a WebSubjectContext for this factory to create a BantenSubject.
   * Otherwise it creates the default subject as defined in the super class.
   *
   * @return returns a new {@link Subject}, never null.
   */
  @Override
  public Subject createSubject(final SubjectContext context) {
    log.trace("Entering createSubject()");

    if (!(context instanceof WebSubjectContext)) {
      log.debug("Non-web context, returning default subject type,"
          + "leaving createSubject()");

      return super.createSubject(context);

    } else {
      log.debug("Web context, creating a BantenSubject");

      WebSubjectContext subjectContext = (WebSubjectContext) context;

      log.debug("Leaving createSubject()");

      return new BantenSubject(
          subjectContext.resolvePrincipals(),
          subjectContext.resolveAuthenticated(),
          subjectContext.resolveHost(),
          subjectContext.resolveSession(),
          subjectContext.isSessionCreationEnabled(),
          subjectContext.resolveServletRequest(),
          subjectContext.resolveServletResponse(),
          subjectContext.resolveSecurityManager());
    }
  }
}
