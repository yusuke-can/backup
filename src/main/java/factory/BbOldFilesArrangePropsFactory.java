package factory;

import static enums.CompressExtensionType.*;
import static factory.BbOldFilesArrangePropsFactory.PropertyType.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import bean.BbOldFilesArrangeProps;
import bean.PropsDto;

import com.google.common.base.Strings;

import enums.CompressExtensionType;
import exception.BkFileException;
import exception.BkPropertyException;

/**
 * @author Y.Kyan
 *
 * 2016/02/13
 */
public class BbOldFilesArrangePropsFactory extends BasePropsFactory<BbOldFilesArrangeProps> {
  private static final EnumSet<CompressExtensionType> ENABLE_COMPRESS_EXTENSION_TYPS = EnumSet.of(ZIP);

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
    DELETE_EXTENSIONS("delete.extensions"), ;

    private String key;
  }

  @Override
  public PropsDto<BbOldFilesArrangeProps> create(final String propertyFilePath) {
    if (Strings.isNullOrEmpty(propertyFilePath)) {
      throw new IllegalArgumentException("propertyFilePath: " + propertyFilePath);
    }
    try {
      final PropertiesDto propertiesDto = getPropertyDto(propertyFilePath, StandardCharsets.UTF_8);
      if (propertiesDto.getMap().isEmpty()) {
        throw new BkFileException("empty file.", propertyFilePath);
      }
      return new PropsDto<BbOldFilesArrangeProps>(Paths.get(propertyFilePath), mapToProps(propertiesDto));
    } catch (IOException e) {
      throw new BkFileException(propertyFilePath, e);
    }
  }

  @Override
  protected BbOldFilesArrangeProps mapToProps(final PropertiesDto propertiesDto) {
    final BbOldFilesArrangeProps props = new BbOldFilesArrangeProps();
    props.setLatestBackupDirPath(validateAndGetDirPath(LATEST_BACKUP_DIR_PATH, propertiesDto));
    props.setOldBackupDirPath(validateAndGetDirPath(OLD_BACKUP_DIR_PATH, propertiesDto));
    props.setLogDirPath(validateAndGetDirPath(LOG_DIR_PATH, propertiesDto));
    props.setAggregateDirPath(validateAndGetDirPath(AGGREGATE_DIR_PATH, propertiesDto));
    props.setDelDuplicateFile(propertiesDto.getBoolean(DEL_DUPLICATE_FILE));
    props.setDelCompressSourceDir(propertiesDto.getBoolean(DEL_COMPRESS_SOURCE_DIR));
    props.setDelFileNames(propertiesDto.getStringList(DEL_FILE_NAMES));
    final CompressExtensionType compressExtensionType = validateAndGetExtensionType(
        COMPRESS_FILE_EXTENSION, propertiesDto, ENABLE_COMPRESS_EXTENSION_TYPS);
    props.setCompressExtensionType(compressExtensionType);
    props.setDeleteExtensions(validateAndGetExtentions(DELETE_EXTENSIONS,propertiesDto));
    return props;
  }

  private List<String> validateAndGetExtentions(final PropertyTypeIf propertyType, final PropertiesDto propertiesDto) {
    final List<String> extentions = propertiesDto.getStringList(propertyType);
    return extentions.stream()
        .map(extention -> {
          final int dotPos = extention.lastIndexOf(".");
          if (dotPos > 0) {
            throw new BkPropertyException("not extension.", propertyType.getKey(), propertiesDto.get(propertyType), propertiesDto.getPath());
          }
          return (dotPos == -1) ? extention : extention.substring(1);
        })
        .collect(Collectors.toList());
  }

}
