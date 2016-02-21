package exception;


/**
 * @author Y.Kyan
 *
 *         2016/02/13
 */
public class BkPropertyException extends RuntimeException {

  /**
   *
   * @param filePath
   */
  public BkPropertyException(final Object key, final Object value, final String filePath) {
    super(createCommonMessage(key, value, filePath));
  }

  /**
   *
   * @param cause
   */
  public BkPropertyException(final Object key, final Object value, final String filePath, final Throwable cause) {
    super(createCommonMessage(key, value, filePath), cause);
  }

  /**
   *
   * @param message
   * @param filePath
   */
  public BkPropertyException(final String message, final Object key, final Object value, final String filePath) {
    super(message + " " + createCommonMessage(key, value, filePath));
  }

  /**
   *
   * @param message
   * @param filePath
   * @param cause
   */
  public BkPropertyException(final String message, final Object key, final Object value, final String filePath,
      Throwable cause) {
    super(message + " " + createCommonMessage(key, value, filePath), cause);
  }

  protected static String createCommonMessage(final Object key, final Object value, final String filePath) {
    return String.format("key:%s, value:%s, filePath:%s", key, value, filePath);
  }
}
