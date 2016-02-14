package factory;

import static factory.BbOldFilesArrangePropsFactory.PropertyType.*;
import static factory.BbOldFilesArrangePropsFactoryTest.PropsFile.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;

import org.junit.Assert;
//import static org.hamcrest.MatcherAssert.*;
//import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.Test;

import bean.BbOldFilesArrangeProps;
import enums.CompressExtentsionType;
import exception.BkFileException;
import exception.BkPropertyException;

/**
 * @author Y.Kyan
 *
 * 2016/02/11
 */
public class BbOldFilesArrangePropsFactoryTest {
  private static final String TEST_DIR = System.getProperty("user.dir").replace("\\", "/") + "/src/test/";
  private static final String TEST_RESOURCE_DIR = System.getProperty("user.dir").replace("\\", "/") + "/src/test/resources/";
  @Getter
  protected enum PropsFile {
    NNDD_DEFAULT("bb_old_files_arrange/nndd_default.properties"),
    EMPTY("bb_old_files_arrange/empty.properties"), ;
    private String path;

    private PropsFile(String relativePath) {
      this.path = System.getProperty("user.dir").replace("\\", "/") + "/src/test/resources/" + relativePath;
    }
  }

  @Before
  public void before() {
  }

  @Test
  public void test_getPropertyMap_success() throws Exception {

    final Map<String, String> propertyMap =
        BbOldFilesArrangePropsFactory.getPropertyMap(NNDD_DEFAULT.getPath(), StandardCharsets.UTF_8);
    assertThat(propertyMap.size(), is(9));
    assertThat(propertyMap.get("latest.backup.dir.path"), is("G:/NNDD/010_latest"));
    assertThat(propertyMap.get("old.backup.dir.path"), is("G:/NNDD/000_世代管理"));
    assertThat(propertyMap.get("aggregate.dir.path"), is("G:/NNDD/000_世代管理"));
    assertThat(propertyMap.get("log.dir.path"), is("G:/NNDD/000_世代管理"));
    assertThat(propertyMap.get("del.duplicate.file"), is("true"));
    assertThat(propertyMap.get("del.compress.source.dir"), is("true"));
    assertThat(propertyMap.get("del.file.names"), is("BunBackup世代管理フォルダ"));
    assertThat(propertyMap.get("compress.file.extension"), is("zip"));
    assertThat(propertyMap.get("exclude.compress.extensions"), is("mp4,flv,rar,part"));
  }

  @Test
  public void test_mapToBbOldFilesArrangeProps_not_found_file() throws Exception {
    final String notExistFilePath = "C:/aaa/bbb/ccc.properties";
    try {
      BbOldFilesArrangePropsFactory.create(notExistFilePath);
      Assert.fail("success!");
    } catch (BkFileException e) {
      assertThat(e.getMessage().toString(), is("filePath: " + notExistFilePath));
    }
  }

  @Test
  public void test_mapToBbOldFilesArrangeProps_empty_file() throws Exception {
    try {
      BbOldFilesArrangePropsFactory.create(EMPTY.getPath());
      Assert.fail("success!");
    } catch (BkFileException e) {
      assertThat(e.getMessage().toString(), is("empty file. filePath: " + EMPTY.getPath()));
    }
  }

  @Test
  public void test_mapToBbOldFilesArrangeProps_success() throws Exception {

    final Map<String, String> propertyMap = createBasePropertyMap();
    final BbOldFilesArrangeProps props = BbOldFilesArrangePropsFactory.mapToProps(NNDD_DEFAULT.getPath(), propertyMap);
    assertThat(props, notNullValue());
    assertThat(props.isEmpty(), is(false));
    assertThat(props.getLatestBackupDirPath(), is(TEST_DIR));
    assertThat(props.getOldBackupDirPath(), is(TEST_RESOURCE_DIR));
    assertThat(props.getAggregateDirPath(), is(TEST_RESOURCE_DIR));
    assertThat(props.getLogDirPath(), is(TEST_RESOURCE_DIR));
    assertThat(props.getDelDuplicateFile(), is(true));
    assertThat(props.getDelCompressSourceDir(), is(true));
    assertThat(props.getDelFileNames(), is(Arrays.asList("BunBackup世代管理フォルダ")));
    assertThat(props.getCompressExtensionType(), is(CompressExtentsionType.ZIP));
    assertThat(props.getExcludeCompressExtensions(), is(Arrays.asList("mp4", "flv", "rar", "part")));
  }

  @Test
  public void test_mapToBbOldFilesArrangeProps_latest_backup_dir_not_found() throws Exception {

    final Map<String, String> propertyMap = createPropertyMap(
        LATEST_BACKUP_DIR_PATH.getKey(), "C:/aaa/bbb/"
        , OLD_BACKUP_DIR_PATH.getKey(), TEST_RESOURCE_DIR
        , AGGREGATE_DIR_PATH.getKey(), TEST_RESOURCE_DIR
        , LOG_DIR_PATH.getKey(), TEST_RESOURCE_DIR
        , COMPRESS_FILE_EXTENSION.getKey(), "zip"
        );
    try {
      BbOldFilesArrangePropsFactory.mapToProps(NNDD_DEFAULT.getPath(), propertyMap);
      Assert.fail("success!");
    } catch (BkPropertyException e) {
      assertThat(e.getMessage().toString(),
          is("not found. propertyKey:" + LATEST_BACKUP_DIR_PATH.getKey() + ", prpertyValue:C:/aaa/bbb/, filePath:" + NNDD_DEFAULT.getPath()));
    }
  }


  @Test
  public void test_mapToBbOldFilesArrangeProps_latest_backup_dir_not_directory() throws Exception {

    final Map<String, String> propertyMap = createPropertyMap(
        LATEST_BACKUP_DIR_PATH.getKey(), NNDD_DEFAULT.getPath()
        , OLD_BACKUP_DIR_PATH.getKey(), TEST_RESOURCE_DIR
        , AGGREGATE_DIR_PATH.getKey(), TEST_RESOURCE_DIR
        , LOG_DIR_PATH.getKey(), TEST_RESOURCE_DIR
        , COMPRESS_FILE_EXTENSION.getKey(), "zip"
        );
    try {
      BbOldFilesArrangePropsFactory.mapToProps(NNDD_DEFAULT.getPath(), propertyMap);
      Assert.fail("success!");
    } catch (BkPropertyException e) {
      assertThat(e.getMessage().toString(),
          is("not directory. propertyKey:" + LATEST_BACKUP_DIR_PATH.getKey() + ", prpertyValue:"+ NNDD_DEFAULT.getPath() + ", filePath:" + NNDD_DEFAULT.getPath()));
    }
  }

  @Test
  public void test_mapToBbOldFilesArrangeProps_old_backup_dir_not_found() throws Exception {

    final Map<String, String> propertyMap = createPropertyMap(
        LATEST_BACKUP_DIR_PATH.getKey(), TEST_DIR
        , OLD_BACKUP_DIR_PATH.getKey(), "C:/aaa/bbb/"
        , AGGREGATE_DIR_PATH.getKey(), TEST_RESOURCE_DIR
        , LOG_DIR_PATH.getKey(), TEST_RESOURCE_DIR
        , COMPRESS_FILE_EXTENSION.getKey(), "zip"
        );
    try {
      BbOldFilesArrangePropsFactory.mapToProps(NNDD_DEFAULT.getPath(), propertyMap);
      Assert.fail("success!");
    } catch (BkPropertyException e) {
      assertThat(e.getMessage().toString(),
          is("not found. propertyKey:" + OLD_BACKUP_DIR_PATH.getKey() + ", prpertyValue:C:/aaa/bbb/, filePath:" + NNDD_DEFAULT.getPath()));
    }
  }

  @Test
  public void test_mapToBbOldFilesArrangeProps_aggregate_dir_not_found() throws Exception {

    final Map<String, String> propertyMap = createPropertyMap(
        LATEST_BACKUP_DIR_PATH.getKey(), TEST_DIR
        , OLD_BACKUP_DIR_PATH.getKey(), TEST_RESOURCE_DIR
        , AGGREGATE_DIR_PATH.getKey(), "C:/aaa/bbb/"
        , LOG_DIR_PATH.getKey(), TEST_RESOURCE_DIR
        , COMPRESS_FILE_EXTENSION.getKey(), "zip"
        );
    try {
      BbOldFilesArrangePropsFactory.mapToProps(NNDD_DEFAULT.getPath(), propertyMap);
      Assert.fail("success!");
    } catch (BkPropertyException e) {
      assertThat(e.getMessage().toString(),
          is("not found. propertyKey:" + AGGREGATE_DIR_PATH.getKey() + ", prpertyValue:C:/aaa/bbb/, filePath:" + NNDD_DEFAULT.getPath()));
    }
  }

  @Test
  public void test_mapToBbOldFilesArrangeProps_log_dir_not_found() throws Exception {

    final Map<String, String> propertyMap = createPropertyMap(
        LATEST_BACKUP_DIR_PATH.getKey(), TEST_DIR
        , OLD_BACKUP_DIR_PATH.getKey(), TEST_RESOURCE_DIR
        , AGGREGATE_DIR_PATH.getKey(), TEST_RESOURCE_DIR
        , LOG_DIR_PATH.getKey(), "C:/aaa/bbb/"
        , COMPRESS_FILE_EXTENSION.getKey(), "zip"
        );
    try {
      BbOldFilesArrangePropsFactory.mapToProps(NNDD_DEFAULT.getPath(), propertyMap);
      Assert.fail("success!");
    } catch (BkPropertyException e) {
      assertThat(e.getMessage().toString(),
          is("not found. propertyKey:" + LOG_DIR_PATH.getKey() + ", prpertyValue:C:/aaa/bbb/, filePath:" + NNDD_DEFAULT.getPath()));
    }
  }

  @Test
  public void test_mapToBbOldFilesArrangeProps_compress_file_extension_invalid() throws Exception {

    final Map<String, String> propertyMap = createPropertyMap(
        LATEST_BACKUP_DIR_PATH.getKey(), TEST_DIR
        , OLD_BACKUP_DIR_PATH.getKey(), TEST_RESOURCE_DIR
        , AGGREGATE_DIR_PATH.getKey(), TEST_RESOURCE_DIR
        , LOG_DIR_PATH.getKey(), TEST_RESOURCE_DIR
        , COMPRESS_FILE_EXTENSION.getKey(), "invalid_extension"
        );
    try {
      BbOldFilesArrangePropsFactory.mapToProps(NNDD_DEFAULT.getPath(), propertyMap);
      Assert.fail("success!");
    } catch (BkPropertyException e) {
      assertThat(e.getMessage().toString(),
          is("invalid extension. propertyKey:" + COMPRESS_FILE_EXTENSION.getKey() + ", prpertyValue:invalid_extension, filePath:" + NNDD_DEFAULT.getPath()));
    }
  }

  @Test
  public void test_mapToBbOldFilesArrangeProps_contains_not_input_property() throws Exception {

    final Map<String, String> propertyMap = createPropertyMap(
        LATEST_BACKUP_DIR_PATH.getKey(), TEST_DIR
        , OLD_BACKUP_DIR_PATH.getKey(), TEST_RESOURCE_DIR
        , AGGREGATE_DIR_PATH.getKey(), TEST_RESOURCE_DIR
        , LOG_DIR_PATH.getKey(), TEST_RESOURCE_DIR
        , COMPRESS_FILE_EXTENSION.getKey(), "zip"
        );

    final BbOldFilesArrangeProps props = BbOldFilesArrangePropsFactory.mapToProps(NNDD_DEFAULT.getPath(), propertyMap);
    assertThat(props, notNullValue());
    assertThat(props.isEmpty(), is(false));
    assertThat(props.getLatestBackupDirPath(), is(TEST_DIR));
    assertThat(props.getOldBackupDirPath(), is(TEST_RESOURCE_DIR));
    assertThat(props.getAggregateDirPath(), is(TEST_RESOURCE_DIR));
    assertThat(props.getLogDirPath(), is(TEST_RESOURCE_DIR));
    assertThat(props.getDelDuplicateFile(), is(false));
    assertThat(props.getDelCompressSourceDir(), is(false));
    assertThat(props.getDelFileNames(), empty());
    assertThat(props.getCompressExtensionType(), is(CompressExtentsionType.ZIP));
    assertThat(props.getExcludeCompressExtensions(), empty());
  }

  private Map<String, String> createBasePropertyMap() {
    return createPropertyMap(
        LATEST_BACKUP_DIR_PATH.getKey(), TEST_DIR
        , OLD_BACKUP_DIR_PATH.getKey(), TEST_RESOURCE_DIR
        , AGGREGATE_DIR_PATH.getKey(), TEST_RESOURCE_DIR
        , LOG_DIR_PATH.getKey(), TEST_RESOURCE_DIR
        , DEL_DUPLICATE_FILE.getKey(), "true"
        , DEL_COMPRESS_SOURCE_DIR.getKey(), "true"
        , DEL_FILE_NAMES.getKey(), "BunBackup世代管理フォルダ"
        , COMPRESS_FILE_EXTENSION.getKey(), "zip"
        , EXCLUDE_COMPRESS_EXTENSIONS.getKey(), "mp4,flv,rar,part");
  }

  private Map<String, String> createPropertyMap(final String... KeyValues) {
    if (KeyValues.length % 2 == 1) {
      throw new IllegalArgumentException("not exist all key value pair");
    }
    final Map<String, String> propertyMap = new LinkedHashMap<>();
    for (int i = 0; i < KeyValues.length / 2; i++) {
      propertyMap.put(KeyValues[i * 2], KeyValues[i * 2 + 1]);
    }
    return propertyMap;
  }
}
