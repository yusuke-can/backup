package util;

import java.io.File;

import com.google.common.base.Strings;

/**
 * @author Y.Kyan
 *
 * 2016/02/13
 */
public class FileUtils {

  public static boolean exists(final String path) {
    if(Strings.isNullOrEmpty(path)) {
      return false;
    }
    final File file = new File(path);
    return file.exists();
  }
}
