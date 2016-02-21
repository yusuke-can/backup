package util;

import java.io.File;
import java.nio.file.Path;

import com.google.common.base.Strings;

/**
 * @author Y.Kyan
 *
 * 2016/02/13
 */
public class BkFileUtils {

  public static boolean exists(final String path) {
    if(Strings.isNullOrEmpty(path)) {
      return false;
    }
    final File file = new File(path);
    return file.exists();
  }

  public static String getExtension(final Path path) {
    final String fileName = path.getFileName().toString();
    final int dotPos = fileName.lastIndexOf(".");
    if (dotPos == -1) {
      return null;
    }
    return fileName.substring(dotPos + 1);
  }
}
