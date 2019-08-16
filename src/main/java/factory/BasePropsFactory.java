package factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Value;
import bean.PropsDto;
import bean.PropsIf;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;

import enums.ExtensionTypeIf;
import exception.BkPropertyException;

/**
 * @author Y.Kyan
 *
 * 2016/02/13
 */
public abstract class BasePropsFactory<P extends PropsIf> {

  protected interface PropertyTypeIf {
    String getKey();
  }

  protected abstract PropsDto<P> create(final String propertyFilePath);

  protected abstract P mapToProps(final PropertiesDto propertiesDto);

  /**
   * プロパティファイルの読み込み
   * @param filePath
   * @param charset
   * @return
   * @throws IOException
   */
  protected PropertiesDto getPropertyDto(final String propertyFilePath, final Charset charset)
      throws IOException {
    try (final BufferedReader reader = Files.newBufferedReader(Paths.get(propertyFilePath), charset)) {
      final Properties properties = new Properties();
      properties.load(reader);
      final Map<String, String> propertyMap = properties.stringPropertyNames().stream()
          .collect(Collectors.toMap(String::toString, properties::getProperty));
      return new PropertiesDto(propertyFilePath, ImmutableMap.copyOf(propertyMap));
    }
  }

  protected Path validateAndGetDirPath(final PropertyTypeIf propertyType, final PropertiesDto propertiesDto) {
    final Path filePath = validateAndGetFilePath(propertyType, propertiesDto);
    if (!Files.isDirectory(filePath)) {
      throw new BkPropertyException("not directory.", propertyType.getKey(), propertiesDto.get(propertyType), propertiesDto.getPath());
    }
    return filePath;
  }

  protected Path validateAndGetFilePath(final PropertyTypeIf propertyType, final PropertiesDto propertiesDto) {

    final String filePathStr = propertiesDto.get(propertyType);
    if (Strings.isNullOrEmpty(filePathStr)) {
      throw new BkPropertyException(propertyType.getKey(), filePathStr, propertiesDto.getPath());
    }
    final Path filePath = Paths.get(filePathStr);
    if (!Files.exists(filePath)) {
      throw new BkPropertyException("not found.", propertyType.getKey(), filePath, propertiesDto.getPath());
    }
    return filePath;
  }

  protected <E extends ExtensionTypeIf> E validateAndGetExtensionType(
      final PropertyTypeIf propertyType,
      final PropertiesDto propertiesDto,
      final Set<E> validExtensionTyps) {
    final String extension = propertiesDto.get(propertyType);
    if (Strings.isNullOrEmpty(extension)) {
      throw new BkPropertyException(propertyType.getKey(), extension, propertiesDto.getPath());
    }
    for (E extentionType : validExtensionTyps) {
      if (extentionType.equalsIgnoreCase(extension)) {
        return extentionType;
      }
    }
    throw new BkPropertyException("invalid extension.", propertyType.getKey(), extension, propertiesDto.getPath());
  }

  @Value
  protected static class PropertiesDto {

    private String path;
    private ImmutableMap<String, String> map;

    public List<String> getStringList(final PropertyTypeIf propertyType) {
      return getStringList(propertyType, ",");
    }

    public List<String> getStringList(final PropertyTypeIf propertyType, final String delimitor) {
      if (propertyType == null || Strings.isNullOrEmpty(map.get(propertyType.getKey()))) {
        return Collections.emptyList();
      }
      return Arrays.asList(map.get(propertyType.getKey()).split(delimitor));
    }

    public boolean getBoolean(final PropertyTypeIf propertyType) {
      return Boolean.valueOf(map.get(propertyType.getKey()));
    }

    public double getDoubleOrZero(final PropertyTypeIf propertyType) {
      return getDoubleOrDefault(propertyType, 0d);
    }

    public long getLongOrZero(final PropertyTypeIf propertyType) {
      return getLongOrDefault(propertyType, 0L);
    }

    public int getIntOrZero(final PropertyTypeIf propertyType) {
      return getIntOrDefault(propertyType, 0);
    }

    public double getDoubleOrDefault(final PropertyTypeIf propertyType, final double defaultValue) {
      if (propertyType == null || map.get(propertyType.getKey()) == null) {
        return defaultValue;
      }
      try {
        return Double.valueOf(map.get(propertyType.getKey()));
      } catch (NumberFormatException e) {
        return defaultValue;
      }
    }

    public long getLongOrDefault(final PropertyTypeIf propertyType, final long defaultValue) {
      if (propertyType == null || map.get(propertyType.getKey()) == null) {
        return defaultValue;
      }
      try {
        return Long.valueOf(map.get(propertyType.getKey()));
      } catch (NumberFormatException e) {
        return defaultValue;
      }
    }

    public int getIntOrDefault(final PropertyTypeIf propertyType, final int defaultValue) {
      if (propertyType == null || map.get(propertyType.getKey()) == null) {
        return defaultValue;
      }
      try {
        return Integer.valueOf(map.get(propertyType.getKey()));
      } catch (NumberFormatException e) {
        return defaultValue;
      }
    }

    public String get(final PropertyTypeIf propertyType) {
      return (propertyType == null) ? null : map.get(propertyType.getKey());
    }

    public String getOrDefault(final PropertyTypeIf propertyType, final String defaultValue) {
      return (propertyType == null) ? null : map.getOrDefault(propertyType.getKey(), defaultValue);
    }
  }
}
