package org.waabox.banten.core;

/** Base factory interface.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public interface Factory<T> {

  /** Creates an object.
   * @return the new object.
   */
  T create();

}

