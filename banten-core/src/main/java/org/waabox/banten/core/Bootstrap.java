package org.waabox.banten.core;

import java.util.LinkedList;

/** Collection of {@link Module} that allow bootstraping for
 * any {@link BantenApplication}.
 *
 * In the future, we could have for example "WebbAppBootstrap" that configures
 * modules for you.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class Bootstrap extends LinkedList<Class<? extends Module>> {

  /** The serial version */
  private static final long serialVersionUID = 1L;

  /** Creates a new instance of the {@link Bootstrap}.
   *
   * @param modules the list of modules to bootstrap.
   */
  @SafeVarargs
  public Bootstrap(final Class<? extends Module> ... modules) {
    for (Class<? extends Module> moduleClass : modules) {
      add(moduleClass);
    }
  }

}
