package exception;

/**
 * @author Y.Kyan
 *
 *         2016/02/13
 */
public class BkFileException extends RuntimeException {

  /**
   *
   * @param filePath
   */
  public BkFileException(final String filePath) {
    super("filePath: " + filePath);
  }

  /**
   *
   * @param cause
   */
  public BkFileException(final Throwable cause) {
    super(cause);
  }

  /**
   *
   * @param filePath
   * @param cause
   */
  public BkFileException(final String filePath, final Throwable cause) {
    super("filePath: " + filePath, cause);
  }

  /**
   *
   * @param message
   * @param filePath
   */
  public BkFileException(final String message, final String filePath) {
    super(message + " filePath: " + filePath);
  }

  /**
   *
   * @param message
   * @param filePath
   * @param cause
   */
  public BkFileException(final String message, final String filePath, Throwable cause) {
    super(message + " filePath: " + filePath, cause);
  }
}
