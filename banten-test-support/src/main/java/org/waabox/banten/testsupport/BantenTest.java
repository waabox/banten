package org.waabox.banten.testsupport;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

import org.springframework.test.context.ContextConfiguration;
import org.waabox.banten.core.BantenApplication;

/** Annotation that initializes a banten application in a test case.
 *
 * You usually add :
 *
 * @RunWith(SpringJUnit4ClassRunner.class)
 *
 * @BantenTest(applicationClass = Application.class)
 *
 * to your junit 4 test class.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
@ContextConfiguration(loader = BantenTestContextLoader.class)
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BantenTest {

  /** The Factory class that bootstrap the application, cannot be null.
   * @return the Application Factory, never null.
   */
  Class<? extends BantenApplication> applicationClass();
}

