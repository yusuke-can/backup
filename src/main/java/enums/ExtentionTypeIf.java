package enums;

import com.google.common.base.Strings;

/**
 * @author Y.Kyan
 *
 * 2016/02/14
 */
public interface ExtentionTypeIf {

  default boolean equals(final String enumStr) {
    final boolean isStartCharDot = Strings.isNullOrEmpty(enumStr) ? false : enumStr.substring(0, 1).equals(".");
    return (isStartCharDot) ? this.toString().equals(enumStr.substring(1)) : this.toString().equals(enumStr);
  }

  default boolean equalsIgnoreCase(final String enumStr) {
    return Strings.isNullOrEmpty(enumStr) ? false : equals(enumStr.toUpperCase());
  }

  default String get() {
    return this.toString().toLowerCase();
  }

  default boolean equalsFromFile(final String filePath) {
    if (Strings.isNullOrEmpty(filePath)) {
      return false;
    }
    final int lastIndexOfDot = filePath.lastIndexOf(".");
    if (lastIndexOfDot == -1) {
      return false;
    }
    final String extension = filePath.substring(lastIndexOfDot);
    if (filePath.equals(extension)) {
      return false;
    }
    return this.equals(extension);
  }

  default boolean equalsIgnoreCaseFromFile(final String filePath) {
    return (Strings.isNullOrEmpty(filePath)) ? false : equalsFromFile(filePath.toUpperCase());
  }
}
