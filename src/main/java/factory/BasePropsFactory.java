package factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Strings;

import enums.ExtentionTypeIf;
import exception.BkPropertyException;

/**
 * @author Y.Kyan
 *
 * 2016/02/13
 */
public class BasePropsFactory {

  protected interface PropertyTypeIf {
    String getKey();

    default String get(final Map<String, String> propertyMap) {
      return (propertyMap == null) ? null : propertyMap.get(getKey());
    };

    default String getOrDefault(final Map<String, String> propertyMap, final String defaultValue) {
      return (propertyMap == null) ? defaultValue : propertyMap.getOrDefault(getKey(), defaultValue);
    };
  }

  /**
   * プロパティファイルの読み込み
   * @param filePath
   * @param charset
   * @return
   * @throws IOException
   */
  protected static Map<String, String> getPropertyMap(final String propertyFilePath, final Charset charset)
      throws IOException {
    try (final InputStreamReader reader = new InputStreamReader(new FileInputStream(new File(propertyFilePath)), charset)) {
      final Properties properties = new Properties();
      properties.load(reader);
      return properties.stringPropertyNames().stream()
          .collect(Collectors.toMap(key -> key, properties::getProperty));
    }
  }

  /**
   * カンマ区切りのプロパティ文字列をStringリストに変換
   * @param propety
   * @return
   */
  protected static List<String> toStringList(final String propety) {
    return toStringList(propety, ",");
  }

  /**
   * 指定された区切り文字で区切ったStringリストに変換
   * @param propety
   * @param delimitor
   * @return
   */
  protected static List<String> toStringList(final String propety, final String delimitor) {
    if (Strings.isNullOrEmpty(propety)) {
      return Collections.emptyList();
    }
    return Arrays.asList(propety.split(delimitor));
  }

  protected static String validateAndGetDirPath(
      final PropertyTypeIf propertyType,
      final Map<String, String> propertyMap,
      final String propertyFilePath) {
    final File file = validateAndGetFile(propertyType, propertyMap, propertyFilePath);
    if (!file.isDirectory()) {
      throw new BkPropertyException("not directory.", propertyType.getKey(), propertyType.get(propertyMap), propertyFilePath);
    }
    return propertyType.get(propertyMap);
  }

  protected static String validateAndGetFilePath(
      final PropertyTypeIf propertyType,
      final Map<String, String> propertyMap,
      final String propertyFilePath) {
    validateAndGetFile(propertyType, propertyMap, propertyFilePath);
    return propertyType.get(propertyMap);
  }

  protected static File validateAndGetFile(
      final PropertyTypeIf propertyType,
      final Map<String, String> propertyMap,
      final String propertyFilePath) {

    final String filePath = propertyType.get(propertyMap);
    if (Strings.isNullOrEmpty(filePath)) {
      throw new BkPropertyException(propertyType.getKey(), filePath, propertyFilePath);
    }
    final File file = new File(filePath);
    if (!file.exists()) {
      throw new BkPropertyException("not found.", propertyType.getKey(), filePath, propertyFilePath);
    }
    return file;
  }

  protected static <E extends ExtentionTypeIf> E validateAndGetExtensionType(
      final PropertyTypeIf propertyType,
      final Map<String, String> propertyMap,
      final Set<E> validExtensionTyps,
      final String propertyFilePath) {
    final String extension = propertyType.get(propertyMap);
    if (Strings.isNullOrEmpty(extension)) {
      throw new BkPropertyException(propertyType.getKey(), extension, propertyFilePath);
    }
    for (E extentionType : validExtensionTyps) {
      if (extentionType.equalsIgnoreCase(extension)) {
        return extentionType;
      }
    }
    throw new BkPropertyException("invalid extension.", propertyType.getKey(), extension, propertyFilePath);
  }

}
