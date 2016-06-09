package org.waabox.banten.web;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import org.slf4j.*;
import org.waabox.banten.core.WebModule;

import java.util.*;

/** Holds Weblets that modules has been declared.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public final class WebletContainer {

  /** The log.*/
  private final Logger log = LoggerFactory.getLogger(WebletContainer.class);

  /** The weblet container instance. */
  private static WebletContainer instance = new WebletContainer();

  /** The Weblets's holder **/
  private Map<Key, ModuleWeblet> weblets = new HashMap<>();

  /** Private constructor. */
  private WebletContainer() {}

  /** Retrieves the WebletContainer instance.
   *
   * @return the WebletContainer, never null.
   */
  public static WebletContainer instance() {
    return instance;
  }

  /** Register the given weblet for a module.
   *
   * @param module the module, cannot be null.
   * @param webletList the list of weblets, cannot be null.
   */
  public void register(final WebModule module, final List<Weblet> webletList) {
    Validate.notNull(module, "The module name cannot be null");
    Validate.notNull(webletList, "The weblet cannot be null");
    for (Weblet weblet : webletList) {
      String webletName = weblet.name();
      log.info("Registering the weblet: {}", webletName);
      weblets.put(new Key(module.getName(), webletName),
          new ModuleWeblet(module, weblet));
    }
  }

  /** Retrieves a Weblet by its name.
   *
   * @param webletName the weblet name, cannot be null.
   * @return the Module-Weblet or null.
   */
  public ModuleWeblet get(final String webletName, final String moduleName) {
    Validate.notNull(webletName, "The weblet name cannot be null");
    Key key = new Key(moduleName, webletName);
    if (weblets.containsKey(key)) {
      return weblets.get(key);
    }
    return null;
  }

  /** Key for the weblets container map.*/
  private static final class Key {

    /** The module name, it's never null.*/
    private final String moduleName;

    /** The weblet name, it's never null.*/
    private final String webletName;

    /** Creates a new instance of the Key.
     *
     * @param aModuleName the module name, cannot be null.
     * @param aWebletName the weblet name, cannot be null.
     */
    private Key(final String aModuleName, final String aWebletName) {
      moduleName = aModuleName;
      webletName = aWebletName;
    }

    /** {@inheritDoc}. */
    @Override
    public boolean equals(final Object obj) {

      if (obj == null || (!(obj instanceof Key))) {
        return false;
      }

      Key key = (Key) obj;
      return moduleName.equals(key.moduleName)
          && webletName.equals(key.webletName);
    }

    /** {@inheritDoc}. */
    @Override
    public int hashCode() {
      HashCodeBuilder builder = new HashCodeBuilder();
      builder.append(moduleName.hashCode());
      builder.append(webletName.hashCode());
      return builder.toHashCode();
    }
  }

  /** Holds a Weblet for a Module.*/
  public static final class ModuleWeblet {

    /** The module, it's never null.*/
    private final WebModule module;

    /** The weblet, it's never null.*/
    private final Weblet weblet;

    /** Creates a new Entry.
     *
     * @param aModule the {@link Module}, cannot be null.
     * @param aWeblet the {@link Weblet}, cannot be null.
     */
    private ModuleWeblet(final WebModule aModule, final Weblet aWeblet) {
      Validate.notNull(aModule, "The module cannot be null");
      Validate.notNull(aWeblet, "The weblet cannot be null");
      module = aModule;
      weblet = aWeblet;
    }

    /** Retrieves the endpoint for this weblet within this module.
     *
     * @return the endpoint.
     */
    public String endpoint() {
      return "/" + module.getNamespace() + "/" + weblet.endpoint();
    }

  }

}
