package org.waabox.banten.web;

import org.apache.commons.lang3.Validate;

/** A Weblet is a specification of an endpoint that expose pages that will
 * be attached into another one.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class Weblet {

  /** The Weblet's name, this value should be unique within the module,
   * it's never null.
   */
  private final String name;

  /** The endpoint URL, it's never null.*/
  private final String endpoint;

  /** Creates a new {@link Weblet} instance.
   *
   * @param theName the {@link Weblet#name}, cannot be null.
   * @param theEndpoint the {@link Weblet#endpoint}, cannot be null.
   */
  public Weblet(final String theName, final String theEndpoint) {
    Validate.notNull(theName, "the name cannot be null");
    Validate.notNull(theEndpoint, "the endpoint cannot be null");
    name = theName;
    endpoint = theEndpoint;
  }

  /** Retrieves the Weblet's name.
   *
   * @return the weblet name, never null.
   */
  public String name() {
    return name;
  }

  /** Retrieves the Weblet's endpoint.
   *
   * @return the endpoint, never null.
   */
  public String endpoint() {
    return endpoint;
  }
}
