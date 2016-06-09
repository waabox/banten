package org.waabox.banten.shiro;

import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.*;

import java.nio.charset.StandardCharsets;

import java.util.*;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import javax.servlet.http.*;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.Validate;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.web.servlet.SimpleCookie;

/** A Shiro's session that keeps its information in a cookie.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class BantenSession implements ValidatingSession {

  /** The class logger. */
  private final Logger log = getLogger(BantenSession.class);

  /** The min key size for the encryption key, in bytes.*/
  private static final int MIN_KEY_SIZE = 40;

  /** The max key size for the encryption key, in bytes.*/
  private static final int MAX_KEY_SIZE = 1024;

  /** The encryption algorithm.*/
  private static final String ENCRYPTION_ALGORITHM = "RC4";

  /** The Cookie name, it's never null.*/
  private static final String COOKIE_NAME = "bantenApplicationSession";

  /** The Servlet's request, never null.*/
  private final HttpServletRequest request;

  /** The Servlet's response, never null. */
  private final HttpServletResponse response;

  /**The host that originated the request, never null.*/
  private final String host;

  /** The session attributes, never null.*/
  private Map<Object, Object> attributes = new HashMap<>();

  /** Indicates that the session should be stopped.
   *
   * This is used in the save operation, to clean up the session cookie.
   */
  private boolean stopping = false;

  /** The client secret, it's  never null.*/
  private final byte[] secretKey;

  /** The {@link SecretKeySpec}, it's never null. */
  private final SecretKeySpec keySpec;

  /** Creates a new instance of the {@link BantenSession}.
   *
   * @param theClientSecret the client secret, cannot be null.
   * @param theHost the host that originated the request.
   * @param theRequest the Servlet's request, cannot be null.
   * @param theResponse the Servlet's request, cannot be null.
   */
  public BantenSession(
      final String theClientSecret,
      final String theHost,
      final HttpServletRequest theRequest,
      final HttpServletResponse theResponse) {

    validateKey(theClientSecret);

    Validate.notNull(theRequest, "The request cannot be null");
    Validate.notNull(theResponse, "The response cannot be null");

    request = theRequest;
    response = theResponse;
    host = theHost;
    secretKey = theClientSecret.getBytes(StandardCharsets.UTF_8);

    keySpec = new SecretKeySpec(secretKey, ENCRYPTION_ALGORITHM);

    if (request.getCookies() != null) {
      for (Cookie cookie : this.request.getCookies()) {
        if (cookie.getName().equals(COOKIE_NAME)) {
          deserialize(cookie.getValue());
        }
      }
    }
  }

  /** Validates the given key for the RC4 algorithm.
   * @param key the key to validate, cannot be null.
   */
  public static void validateKey(final String key) {
    Validate.notNull(key, "The key cannot be null");
    int keySize = key.getBytes().length;
    Validate.isTrue(keySize >= MIN_KEY_SIZE && keySize <= MAX_KEY_SIZE);
  }

  /** Encrypts the provided plain text and generates an encrypted string.
  *
  * @param plainText the data to encrypt. It cannot be null.
  *
  * @return a string with the encrypted data, represented in base64. Never
  * returns null.
  */
  public String encrypt(final byte[] plainText) {
    try {
      Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, keySpec);
      return new Base64(true).encodeToString(cipher.doFinal(plainText));
    } catch (Exception e) {
      throw new RuntimeException("Error encrypting message.", e);
    }
  }

  /** Decrypts the given string and generates a plain array of bytes.
  *
  * @param cipherText the string to decrypt, as returned by encrypt. It cannot
  * be null.
  *
  * @return the decrypted plain array of bytes. Never returns null.
  */
  public byte[] decrypt(final String cipherText) {
    try {
      Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, keySpec);
      return cipher.doFinal(new Base64(true).decode(cipherText));
    } catch (Exception e) {
      throw new RuntimeException("Error decrypting message.", e);
    }
  }

  /** Deserializes the session from a string representation that is recovered
   * fron a browser cookie.
   *
   * @param value the serialized representation of the session. It cannot be
   * null.
   */
  @SuppressWarnings("unchecked")
  private void deserialize(final String value) {
    log.trace("Entering toMap()");
    try (ByteArrayInputStream bis = new ByteArrayInputStream(decrypt(value))) {
      try (ObjectInputStream ois = new ObjectInputStream(bis)) {
        attributes = (Map<Object, Object>) ois.readObject();
      } catch (Exception e) {
        log.debug("Could not deserialize session, ignored");
      }
    } catch (IOException e1) {
      throw new RuntimeException(e1);
    }
    log.trace("Leaving toMap()");
  }

  /** Sends the string representation of this session to the client as a
   * browser cookie.
   */
  void save() {
    String sessionValue = "";
    SimpleCookie cookie = new SimpleCookie(COOKIE_NAME);
    if (stopping) {
      cookie.setMaxAge(0);
    } else {
      try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
          oos.writeObject(attributes);
          oos.close();
          sessionValue = encrypt(baos.toByteArray());
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      } catch (IOException e1) {
        throw new RuntimeException(e1);
      }
    }
    cookie.setValue(sessionValue);
    cookie.saveTo(request, response);
  }

  /** {@inheritDoc}.*/
  @Override
  public void setAttribute(final Object key, final Object value) {
    attributes.put((String) key, value);
  }

  /** {@inheritDoc}.*/
  @Override
  public Object removeAttribute(final Object key) {
    Object value = attributes.remove(key);
    return value;
  }

  /** This is not relevant, this implementation simply returns the java object
   * identifier.
   */
  @Override
  public Serializable getId() {
    return System.identityHashCode(this);
  }

  /** Not implemented yet. */
  @Override
  public Date getStartTimestamp() {
    return null;
  }

  /** Not implemented yet. */
  @Override
  public Date getLastAccessTime() {
    return null;
  }

  /** Not implemented yet. */
  @Override
  public long getTimeout() {
    return 0;
  }

  /** Not implemented yet. */
  @Override
  public void setTimeout(final long maxIdleTimeInMillis) {
  }

  /** {@inheritDoc}.*/
  @Override
  public String getHost() {
    return host;
  }

  /** Not implemented yet. */
  @Override
  public void touch() {
  }

  /** {@inheritDoc}.*/
  @Override
  public void stop() {
    attributes.clear();
    stopping = true;
  }

  /** {@inheritDoc}.*/
  @Override
  public Collection<Object> getAttributeKeys() {
    return attributes.keySet();
  }

  /** {@inheritDoc}.*/
  @Override
  public Object getAttribute(final Object key) {
    return attributes.get(key);
  }

  /** {@inheritDoc}.*/
  @Override
  public boolean isValid() {
    return !stopping;
  }

  /** {@inheritDoc}.*/
  @Override
  public void validate() {
    if (stopping) {
      throw new InvalidSessionException("Session was stopped");
    }
  }

}
