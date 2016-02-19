package factory;

import static enums.CompressExtentsionType.*;
import static factory.BbOldFilesArrangePropsFactory.PropertyType.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import bean.BbOldFilesArrangeProps;
import enums.CompressExtentsionType;
import exception.BkFileException;

/**
 * @author Y.Kyan
 *
 * 2016/02/13
 */
public class BbOldFilesArrangePropsFactory extends BasePropsFactory {
  private static final EnumSet<CompressExtentsionType> ENABLE_COMPRESS_EXTENSION_TYPS = EnumSet.of(ZIP);

  @Getter
  @AllArgsConstructor
  protected enum PropertyType implements PropertyTypeIf {
    LATEST_BACKUP_DIR_PATH("latest.backup.dir.path"),
    OLD_BACKUP_DIR_PATH("old.backup.dir.path"),
    AGGREGATE_DIR_PATH("aggregate.dir.path"),
    LOG_DIR_PATH("log.dir.path"),
    DEL_DUPLICATE_FILE("del.duplicate.file"),
    DEL_COMPRESS_SOURCE_DIR("del.compress.source.dir"),
    DEL_FILE_NAMES("del.file.names"),
    COMPRESS_FILE_EXTENSION("compress.file.extension"),
    EXCLUDE_COMPRESS_EXTENSIONS("exclude.compress.extensions"), ;

    private String key;
  }

  public static BbOldFilesArrangeProps create(final String propertyFilePath) {
    try {
      final Map<String, String> propertyMap = getPropertyMap(propertyFilePath, StandardCharsets.UTF_8);
      if (propertyMap.isEmpty()) {
        throw new BkFileException("empty file.", propertyFilePath);
      }
      return mapToProps(propertyFilePath, propertyMap);
    } catch (IOException e) {
      throw new BkFileException(propertyFilePath, e);
    }
  }

  protected static BbOldFilesArrangeProps mapToProps(
      final String propertyFilePath,
      final Map<String, String> propertyMap) {
    final BbOldFilesArrangeProps props = new BbOldFilesArrangeProps();
    props.setLatestBackupDirPath(validateAndGetDirPath(LATEST_BACKUP_DIR_PATH, propertyMap, propertyFilePath));
    props.setOldBackupDirPath(validateAndGetDirPath(OLD_BACKUP_DIR_PATH, propertyMap, propertyFilePath));
    props.setLogDirPath(validateAndGetDirPath(LOG_DIR_PATH, propertyMap, propertyFilePath));
    props.setAggregateDirPath(validateAndGetDirPath(AGGREGATE_DIR_PATH, propertyMap, propertyFilePath));
    props.setDelDuplicateFile(Boolean.valueOf(DEL_DUPLICATE_FILE.get(propertyMap)));
    props.setDelCompressSourceDir(Boolean.valueOf(DEL_COMPRESS_SOURCE_DIR.get(propertyMap)));
    props.setDelFileNames(toStringList(DEL_FILE_NAMES.get(propertyMap)));
    final CompressExtentsionType compressExtensionType = validateAndGetExtensionType(
        COMPRESS_FILE_EXTENSION, propertyMap, ENABLE_COMPRESS_EXTENSION_TYPS, propertyFilePath);
    props.setCompressExtensionType(compressExtensionType);
    props.setExcludeCompressExtensions(toStringList(EXCLUDE_COMPRESS_EXTENSIONS.get(propertyMap)));
    return props;
  }

}
