package factory;

import static factory.BbOldFilesArrangePropsFactory.PropertyType.*;
import static factory.BbOldFilesArrangePropsFactoryTest.PropsFile.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;

import lombok.Getter;

import org.junit.Assert;
//import static org.hamcrest.MatcherAssert.*;
//import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.Test;

import bean.BbOldFilesArrangeProps;

import com.google.common.collect.ImmutableMap;

import enums.CompressExtensionType;
import exception.BkFileException;
import exception.BkPropertyException;
import factory.BasePropsFactory.PropertiesDto;

/**
 * @author Y.Kyan
 *
 * 2016/02/11
 */
public class BbOldFilesArrangePropsFactoryTest {
  private static final Path TEST_DIR = Paths.get(System.getProperty("user.dir"), "/src/test/");
  private static final Path TEST_RESOURCE_DIR = Paths.get(System.getProperty("user.dir"), "/src/test/resources/");
  private static final Path NOT_EXIST_DIRECTORY = Paths.get("/not_exist/");

  @Getter
  protected enum PropsFile {
    NNDD_DEFAULT("bb_old_files_arrange/nndd_default.properties"),
    EMPTY("bb_old_files_arrange/empty.properties"), ;
    private Path path;

    private PropsFile(String relativePath) {
      this.path = Paths.get(System.getProperty("user.dir"), "/src/test/resources/", relativePath);
    }

    @Override
    public String toString() {
      return this.path.toString();
    }
  }

  @Before
  public void before() {
  }

  @Test
  public void test_getPropertyMap_success() throws Exception {

    final BbOldFilesArrangePropsFactory factory = new BbOldFilesArrangePropsFactory();

    final PropertiesDto propertiesDto = factory.getPropertyDto(NNDD_DEFAULT.toString(), StandardCharsets.UTF_8);
    assertThat(propertiesDto.getMap().size(), is(9));
    assertThat(propertiesDto.getMap().get("latest.backup.dir.path"), is("G:/NNDD/010_latest"));
    assertThat(propertiesDto.getMap().get("old.backup.dir.path"), is("G:/NNDD/000_世代管理"));
    assertThat(propertiesDto.getMap().get("aggregate.dir.path"), is("G:/NNDD/000_世代管理"));
    assertThat(propertiesDto.getMap().get("log.dir.path"), is("G:/NNDD/000_世代管理"));
    assertThat(propertiesDto.getMap().get("del.duplicate.file"), is("true"));
    assertThat(propertiesDto.getMap().get("del.compress.source.dir"), is("true"));
    assertThat(propertiesDto.getMap().get("del.file.names"), is("BunBackup世代管理フォルダ"));
    assertThat(propertiesDto.getMap().get("compress.file.extension"), is("zip"));
    assertThat(propertiesDto.getMap().get("delete.extensions"), is("rar,part"));
  }

  @Test
  public void test_mapToBbOldFilesArrangeProps_propertyPath_null() throws Exception {
    try {
      final BbOldFilesArrangePropsFactory factory = new BbOldFilesArrangePropsFactory();
      factory.create(null);
      Assert.fail("success!");
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage().toString(), is("propertyFilePath: " + null));
    }
  }

  @Test
  public void test_mapToBbOldFilesArrangeProps_not_found_file() throws Exception {
    final String notExistFilePath = "C:/aaa/bbb/ccc.properties";
    try {
      final BbOldFilesArrangePropsFactory factory = new BbOldFilesArrangePropsFactory();
      factory.create(notExistFilePath);
      Assert.fail("success!");
    } catch (BkFileException e) {
      assertThat(e.getMessage().toString(), is("filePath: " + notExistFilePath));
    }
  }

  @Test
  public void test_mapToBbOldFilesArrangeProps_empty_file() throws Exception {
    try {
      final BbOldFilesArrangePropsFactory factory = new BbOldFilesArrangePropsFactory();
      factory.create(EMPTY.toString());
      Assert.fail("success!");
    } catch (BkFileException e) {
      assertThat(e.getMessage().toString(), is("empty file. filePath: " + EMPTY.toString()));
    }
  }

  @Test
  public void test_mapToBbOldFilesArrangeProps_success() throws Exception {

    final ImmutableMap<String, String> propertyMap = createBasePropertyMap();
    final BbOldFilesArrangePropsFactory factory = new BbOldFilesArrangePropsFactory();
    final PropertiesDto propertyDto = new PropertiesDto(NNDD_DEFAULT.toString(), propertyMap);
    final BbOldFilesArrangeProps props = factory.mapToProps(propertyDto);
    assertThat(props, notNullValue());
    assertThat(props.isEmpty(), is(false));
    assertThat(props.getLatestBackupDirPath(), is(TEST_DIR));
    assertThat(props.getOldBackupDirPath(), is(TEST_RESOURCE_DIR));
    assertThat(props.getAggregateDirPath(), is(TEST_RESOURCE_DIR));
    assertThat(props.getLogDirPath(), is(TEST_RESOURCE_DIR));
    assertThat(props.isDelDuplicateFile(), is(true));
    assertThat(props.isDelCompressSourceDir(), is(true));
    assertThat(props.getDelFileNames(), is(Arrays.asList("BunBackup世代管理フォルダ")));
    assertThat(props.getCompressExtensionType(), is(CompressExtensionType.ZIP));
    assertThat(props.getDeleteExtensions(), is(Arrays.asList("rar", "part")));
  }

  @Test
  public void test_mapToBbOldFilesArrangeProps_latest_backup_dir_not_found() throws Exception {

    final ImmutableMap<String, String> propertyMap = createPropertyMap(
        LATEST_BACKUP_DIR_PATH.getKey(), NOT_EXIST_DIRECTORY.toString()
        , OLD_BACKUP_DIR_PATH.getKey(), TEST_RESOURCE_DIR.toString()
        , AGGREGATE_DIR_PATH.getKey(), TEST_RESOURCE_DIR.toString()
        , LOG_DIR_PATH.getKey(), TEST_RESOURCE_DIR.toString()
        , COMPRESS_FILE_EXTENSION.getKey(), "zip"
        );
    try {
      final BbOldFilesArrangePropsFactory factory = new BbOldFilesArrangePropsFactory();
      final PropertiesDto propertyDto = new PropertiesDto(NNDD_DEFAULT.toString(), propertyMap);
      factory.mapToProps(propertyDto);
      Assert.fail("success!");
    } catch (BkPropertyException e) {
      assertThat(e.getMessage().toString(),
          is("not found. key:" + LATEST_BACKUP_DIR_PATH.getKey() + ", value:" + NOT_EXIST_DIRECTORY.toString() + ", filePath:" + NNDD_DEFAULT.toString()));
    }
  }

  @Test
  public void test_mapToBbOldFilesArrangeProps_latest_backup_dir_not_directory() throws Exception {

    final ImmutableMap<String, String> propertyMap = createPropertyMap(
        LATEST_BACKUP_DIR_PATH.getKey(), NNDD_DEFAULT.toString()
        , OLD_BACKUP_DIR_PATH.getKey(), TEST_RESOURCE_DIR.toString()
        , AGGREGATE_DIR_PATH.getKey(), TEST_RESOURCE_DIR.toString()
        , LOG_DIR_PATH.getKey(), TEST_RESOURCE_DIR.toString()
        , COMPRESS_FILE_EXTENSION.getKey(), "zip"
        );
    try {
      final BbOldFilesArrangePropsFactory factory = new BbOldFilesArrangePropsFactory();
      final PropertiesDto propertyDto = new PropertiesDto(NNDD_DEFAULT.toString(), propertyMap);
      factory.mapToProps(propertyDto);
      Assert.fail("success!");
    } catch (BkPropertyException e) {
      assertThat(e.getMessage().toString(),
          is("not directory. key:" + LATEST_BACKUP_DIR_PATH.getKey() + ", value:" + NNDD_DEFAULT.toString() + ", filePath:" + NNDD_DEFAULT.getPath()));
    }
  }

  @Test
  public void test_mapToBbOldFilesArrangeProps_old_backup_dir_not_found() throws Exception {

    final ImmutableMap<String, String> propertyMap = createPropertyMap(
        LATEST_BACKUP_DIR_PATH.getKey(), TEST_DIR.toString()
        , OLD_BACKUP_DIR_PATH.getKey(), NOT_EXIST_DIRECTORY.toString()
        , AGGREGATE_DIR_PATH.getKey(), TEST_RESOURCE_DIR.toString()
        , LOG_DIR_PATH.getKey(), TEST_RESOURCE_DIR.toString()
        , COMPRESS_FILE_EXTENSION.getKey(), "zip"
        );
    try {
      final BbOldFilesArrangePropsFactory factory = new BbOldFilesArrangePropsFactory();
      final PropertiesDto propertyDto = new PropertiesDto(NNDD_DEFAULT.toString(), propertyMap);
      factory.mapToProps(propertyDto);
      Assert.fail("success!");
    } catch (BkPropertyException e) {
      assertThat(e.getMessage().toString(),
          is("not found. key:" + OLD_BACKUP_DIR_PATH.getKey() + ", value:" + NOT_EXIST_DIRECTORY.toString() + ", filePath:" + NNDD_DEFAULT.toString()));
    }
  }

  @Test
  public void test_mapToBbOldFilesArrangeProps_aggregate_dir_not_found() throws Exception {

    final ImmutableMap<String, String> propertyMap = createPropertyMap(
        LATEST_BACKUP_DIR_PATH.getKey(), TEST_DIR.toString()
        , OLD_BACKUP_DIR_PATH.getKey(), TEST_RESOURCE_DIR.toString()
        , AGGREGATE_DIR_PATH.getKey(), NOT_EXIST_DIRECTORY.toString()
        , LOG_DIR_PATH.getKey(), TEST_RESOURCE_DIR.toString()
        , COMPRESS_FILE_EXTENSION.getKey(), "zip"
        );
    try {
      final BbOldFilesArrangePropsFactory factory = new BbOldFilesArrangePropsFactory();
      final PropertiesDto propertyDto = new PropertiesDto(NNDD_DEFAULT.toString(), propertyMap);
      factory.mapToProps(propertyDto);
      Assert.fail("success!");
    } catch (BkPropertyException e) {
      assertThat(e.getMessage().toString(),
          is("not found. key:" + AGGREGATE_DIR_PATH.getKey() + ", value:" + NOT_EXIST_DIRECTORY.toString() + ", filePath:" + NNDD_DEFAULT.toString()));
    }
  }

  @Test
  public void test_mapToBbOldFilesArrangeProps_log_dir_not_found() throws Exception {

    final ImmutableMap<String, String> propertyMap = createPropertyMap(
        LATEST_BACKUP_DIR_PATH.getKey(), TEST_DIR.toString()
        , OLD_BACKUP_DIR_PATH.getKey(), TEST_RESOURCE_DIR.toString()
        , AGGREGATE_DIR_PATH.getKey(), TEST_RESOURCE_DIR.toString()
        , LOG_DIR_PATH.getKey(), NOT_EXIST_DIRECTORY.toString()
        , COMPRESS_FILE_EXTENSION.getKey(), "zip"
        );
    try {
      final BbOldFilesArrangePropsFactory factory = new BbOldFilesArrangePropsFactory();
      final PropertiesDto propertyDto = new PropertiesDto(NNDD_DEFAULT.toString(), propertyMap);
      factory.mapToProps(propertyDto);
      Assert.fail("success!");
    } catch (BkPropertyException e) {
      assertThat(e.getMessage().toString(),
          is("not found. key:" + LOG_DIR_PATH.getKey() + ", value:" + NOT_EXIST_DIRECTORY.toString() + ", filePath:" + NNDD_DEFAULT.toString()));
    }
  }

  @Test
  public void test_mapToBbOldFilesArrangeProps_compress_file_extension_invalid() throws Exception {

    final ImmutableMap<String, String> propertyMap = createPropertyMap(
        LATEST_BACKUP_DIR_PATH.getKey(), TEST_DIR.toString()
        , OLD_BACKUP_DIR_PATH.getKey(), TEST_RESOURCE_DIR.toString()
        , AGGREGATE_DIR_PATH.getKey(), TEST_RESOURCE_DIR.toString()
        , LOG_DIR_PATH.getKey(), TEST_RESOURCE_DIR.toString()
        , COMPRESS_FILE_EXTENSION.getKey(), "invalid_extension"
        );
    try {
      final BbOldFilesArrangePropsFactory factory = new BbOldFilesArrangePropsFactory();
      final PropertiesDto propertyDto = new PropertiesDto(NNDD_DEFAULT.toString(), propertyMap);
      factory.mapToProps(propertyDto);
      Assert.fail("success!");
    } catch (BkPropertyException e) {
      assertThat(e.getMessage().toString(),
          is("invalid extension. key:" + COMPRESS_FILE_EXTENSION.getKey() + ", value:invalid_extension, filePath:" + NNDD_DEFAULT.getPath()));
    }
  }

  @Test
  public void test_mapToBbOldFilesArrangeProps_delete_extensions_invalid() throws Exception {

    final ImmutableMap<String, String> propertyMap = createPropertyMap(
        LATEST_BACKUP_DIR_PATH.getKey(), TEST_DIR.toString()
        , OLD_BACKUP_DIR_PATH.getKey(), TEST_RESOURCE_DIR.toString()
        , AGGREGATE_DIR_PATH.getKey(), TEST_RESOURCE_DIR.toString()
        , LOG_DIR_PATH.getKey(), TEST_RESOURCE_DIR.toString()
        , COMPRESS_FILE_EXTENSION.getKey(), "zip"
        , DELETE_EXTENSIONS.getKey(), "a.part"
        );
    try {
      final BbOldFilesArrangePropsFactory factory = new BbOldFilesArrangePropsFactory();
      final PropertiesDto propertyDto = new PropertiesDto(NNDD_DEFAULT.toString(), propertyMap);
      factory.mapToProps(propertyDto);
      Assert.fail("success!");
    } catch (BkPropertyException e) {
      assertThat(e.getMessage().toString(),
          is("not extension. key:" + DELETE_EXTENSIONS.getKey() + ", value:a.part, filePath:" + NNDD_DEFAULT.getPath()));
    }
  }

  @Test
  public void test_mapToBbOldFilesArrangeProps_delete_extensions_contains_start_with_dot() throws Exception {

    final ImmutableMap<String, String> propertyMap = createPropertyMap(
        LATEST_BACKUP_DIR_PATH.getKey(), TEST_DIR.toString()
        , OLD_BACKUP_DIR_PATH.getKey(), TEST_RESOURCE_DIR.toString()
        , AGGREGATE_DIR_PATH.getKey(), TEST_RESOURCE_DIR.toString()
        , LOG_DIR_PATH.getKey(), TEST_RESOURCE_DIR.toString()
        , COMPRESS_FILE_EXTENSION.getKey(), "zip"
        , DELETE_EXTENSIONS.getKey(), ".part"
        );

    final BbOldFilesArrangePropsFactory factory = new BbOldFilesArrangePropsFactory();
    final PropertiesDto propertyDto = new PropertiesDto(NNDD_DEFAULT.toString(), propertyMap);
    final BbOldFilesArrangeProps props = factory.mapToProps(propertyDto);
    assertThat(props, notNullValue());
    assertThat(props.isEmpty(), is(false));
    assertThat(props.getLatestBackupDirPath(), is(TEST_DIR));
    assertThat(props.getOldBackupDirPath(), is(TEST_RESOURCE_DIR));
    assertThat(props.getAggregateDirPath(), is(TEST_RESOURCE_DIR));
    assertThat(props.getLogDirPath(), is(TEST_RESOURCE_DIR));
    assertThat(props.isDelDuplicateFile(), is(false));
    assertThat(props.isDelCompressSourceDir(), is(false));
    assertThat(props.getDelFileNames(), empty());
    assertThat(props.getCompressExtensionType(), is(CompressExtensionType.ZIP));
    assertThat(props.getDeleteExtensions(), is(Arrays.asList("part")));
  }

  @Test
  public void test_mapToBbOldFilesArrangeProps_contains_not_input_property() throws Exception {

    final ImmutableMap<String, String> propertyMap = createPropertyMap(
        LATEST_BACKUP_DIR_PATH.getKey(), TEST_DIR.toString()
        , OLD_BACKUP_DIR_PATH.getKey(), TEST_RESOURCE_DIR.toString()
        , AGGREGATE_DIR_PATH.getKey(), TEST_RESOURCE_DIR.toString()
        , LOG_DIR_PATH.getKey(), TEST_RESOURCE_DIR.toString()
        , COMPRESS_FILE_EXTENSION.getKey(), "zip"
        );

    final BbOldFilesArrangePropsFactory factory = new BbOldFilesArrangePropsFactory();
    final PropertiesDto propertyDto = new PropertiesDto(NNDD_DEFAULT.toString(), propertyMap);
    final BbOldFilesArrangeProps props = factory.mapToProps(propertyDto);
    assertThat(props, notNullValue());
    assertThat(props.isEmpty(), is(false));
    assertThat(props.getLatestBackupDirPath(), is(TEST_DIR));
    assertThat(props.getOldBackupDirPath(), is(TEST_RESOURCE_DIR));
    assertThat(props.getAggregateDirPath(), is(TEST_RESOURCE_DIR));
    assertThat(props.getLogDirPath(), is(TEST_RESOURCE_DIR));
    assertThat(props.isDelDuplicateFile(), is(false));
    assertThat(props.isDelCompressSourceDir(), is(false));
    assertThat(props.getDelFileNames(), empty());
    assertThat(props.getCompressExtensionType(), is(CompressExtensionType.ZIP));
    assertThat(props.getDeleteExtensions(), empty());
  }

  private ImmutableMap<String, String> createBasePropertyMap() {
    return createPropertyMap(
        LATEST_BACKUP_DIR_PATH.getKey(), TEST_DIR.toString()
        , OLD_BACKUP_DIR_PATH.getKey(), TEST_RESOURCE_DIR.toString()
        , AGGREGATE_DIR_PATH.getKey(), TEST_RESOURCE_DIR.toString()
        , LOG_DIR_PATH.getKey(), TEST_RESOURCE_DIR.toString()
        , DEL_DUPLICATE_FILE.getKey(), "true"
        , DEL_COMPRESS_SOURCE_DIR.getKey(), "true"
        , DEL_FILE_NAMES.getKey(), "BunBackup世代管理フォルダ"
        , COMPRESS_FILE_EXTENSION.getKey(), "zip"
        , DELETE_EXTENSIONS.getKey(), "rar,part");
  }

  private ImmutableMap<String, String> createPropertyMap(final String... KeyValues) {
    if (KeyValues.length % 2 == 1) {
      throw new IllegalArgumentException("not exist all key value pair");
    }
    final Map<String, String> propertyMap = new LinkedHashMap<>();
    IntStream.range(0, KeyValues.length / 2)
        .forEach(i -> propertyMap.put(KeyValues[i * 2], KeyValues[i * 2 + 1]));
    return ImmutableMap.copyOf(propertyMap);
  }
}
