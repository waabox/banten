package org.waabox.banten.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.support.*;

/** Core bean configuration.
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class CoreBeansConfiguration {

  /** Bean factory post processor to support @Value in spring beans.
   *
   * This lets modules use @Value in their global bean configuration.
   *
   * @return a post processor that can interpret @Value annotations,
   * never null.
   */
  @Bean public PropertySourcesPlaceholderConfigurer
      propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }

  /** Creates the Module Service locator.
   * @return the module service locator.
   */
  @Bean public ModuleServiceLocator createModuleServiceLocator() {
    return new ModuleServiceLocator();
  }

}
