package batch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Stream;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;

import util.BkFileUtils;
import util.LogbackUtils;
import bean.BbOldFilesArrangeProps;
import bean.PropsDto;
import ch.qos.logback.classic.Level;

import com.google.common.base.Strings;

import exception.BkFileException;
import factory.BbOldFilesArrangePropsFactory;

/**
 *
 * @author Y.Kyan
 *
 * 2016/02/11
 */
@Slf4j
public class BbOldFilesArrangeBatch {
  private static final String BATCH_NAME = BbOldFilesArrangeBatch.class.getSimpleName();
  private static final String DATETIME_PATTERN = "yyyyMMdd_HHmmss";
  private static final int MAX_PALLARELL_THREADS =50;

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) {
    if (args.length != 1) {
      throw new IllegalArgumentException("invalid arguments. required argument:propertyFilePath. args:" + args);
    }
    final BbOldFilesArrangePropsFactory factory = new BbOldFilesArrangePropsFactory();
    final PropsDto<BbOldFilesArrangeProps> propsDto = factory.create(args[0]);
    final BbOldFilesArrangeProps props = propsDto.getProps();

    final DateTime startTime = DateTime.now();
    final Path logFilePath = createLogFilePath(props.getLogDirPath(), startTime);
    LogbackUtils.addRootLoggerToFileAppender(logFilePath, Level.INFO, true);
    log.info("start! propety file info:{}", propsDto.toString());

    final AddAllArrangeTaskDto addAllArrangeTaskDto = new AddAllArrangeTaskDto(props);

    if (!addArrangeTaskList(props,addAllArrangeTaskDto)) {
      log.info("allDeleteTask failed! execTime: {} sec", getExecSeconds(startTime));
      return;
    }
    log.info("allDeleteTask complete! execTime: {} sec", getExecSeconds(startTime));


    log.info("complete! execTime: {} sec", getExecSeconds(startTime));


//
//    // 最新バックアップディレクトリのファイルの一覧を返すStreamを取得する
//
//    try (Stream<Path> stream = Files.list(props.getLatestBackupDirPath())) {
//      stream
//          .filter(Files::isDirectory)
//          .forEach(path -> {
//            final StringBuilder sb = new StringBuilder(path.toString());
//            sb.append(", fileName: " + path.getFileName().toString());
//            if (Files.isDirectory(path)) {
//              try (Stream<Path> childStream = Files.list(path)) {
//                if (childStream.count() == 0) {
//                  sb.append(", isEmptyDirectory");
//                }
//              } catch (IOException e) {
//                throw new BkFileException(path.toString(), e);
//              }
//            }
//            log.info("{}", sb.toString());
//          });
//    } catch (IOException e) {
//      throw new BkFileException(props.getLatestBackupDirPath().toString(), e);
//    }
    //  try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(props.getLatestBackupDirPath())) {
    //  directoryStream
    //  .forEach(path -> {
    //    log.info("{}",path.getFileName().toString());
    //  });
    //}
    //    final FileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {
    //      @Override
    //      public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
    //        final StringBuilder sb = new StringBuilder(path.toString());
    //        if(path.getParent().equals(props.getOldBackupDirPath())
    //            &&  ExtensionType.LOG.equalsIgnoreCase(getExtensions(path))) {
    //          sb.append(",root log file.");
    //          return FileVisitResult.CONTINUE;
    //        }
    //        if (Files.isDirectory(path)) {
    //          sb.append(",isDirectory");
    //          if (path.getNameCount() == 0) {
    //            sb.append(", isEmptyDirectory");
    //            //          Files.delete(path);
    //          }
    //          log.info(sb.toString());
    //          return FileVisitResult.CONTINUE;
    //        }
    //        sb.append(",isFile");
    //        if (props.getDelFileNames().contains(path.getFileName().toString())) {
    //          sb.append(",contains delFileNames:" + props.getDelFileNames());
    //          //        Files.delete(path);
    //        }
    //        final String extension = getExtensions(path);
    //        if (!Strings.isNullOrEmpty(extension) && props.getDeleteExtensions().contains(extension)) {
    //          sb.append(",contains deleteExtensionss:" + props.getDeleteExtensions());
    //          //        Files.delete(path);
    //        }
    //        if(path.getParent().getNameCount() == 0) {
    //          sb.append(", parentDirectory is Empty: " + path.getParent() );
    ////          Files.delete(path.getParent());
    //        }
    //        log.info(sb.toString());
    //        return FileVisitResult.CONTINUE;
    //      }
    //
    //      private String getExtensions(final Path path) {
    //        return BkFileUtils.getExtension(path);
    //      }
    //    };
    //    Files.walkFileTree(props.getOldBackupDirPath(), visitor);


    //    try (final BufferedWriter bw = Files.newBufferedWriter(Files.createFile(logFilePath), StandardCharsets.UTF_8)) {
    //      bw.write(BATCH_NAME +" start! propety file info: "+ propsDto.toString());
    //      Thread.sleep(1000);
    //
    //      bw.write(BATCH_NAME + "complete! batchExecTime: " + batchExecTime.getStandardSeconds());
    //    }

    //    // 最新バックアップディレクトリのファイルの一覧を返すStreamを取得する
    //    FileSystem fs = FileSystems.getDefault();
    //    try(Stream<Path> stream = Files.list(props.getLatestBackupDirPath())){
    //      stream
    //      .filter(Files::isDirectory)
    //      .forEach(path -> {
    ////        log.info("{}",path.toString());
    //      });
    //    }
    // TODO 1個の集約用バックアップディレクトリと重複ファイル格納用のディレクトリを作成

    // TODO バックアップ
    // TODO 最新ディレクトリと世代別バックアップディレクトリの重複ファイル削除
    // TODO 名前が異なる中身が同じファイルも削除
    // TODO 空になったディレクトリは削除

    // TODO 世代別バックアップ内で重複している場合は世代の新しいもの以外は削除
    // TODO 名前が異なる中身が同じファイルも削除
    // TODO 空になったディレクトリは削除

    // TODO 集約バックアップディレクトリのファイル一覧を出力
    // TODO 重複ファイル格納用のディレクトリを圧縮、もしくは削除
    // TODO 削除ファイル一覧を出力

  }

  protected static long getExecSeconds(final DateTime startTime) {
    final Duration execDuration = new Duration(startTime.getMillis(), DateTime.now().getMillis());
    return execDuration.getStandardSeconds();
  }
  protected static Path createLogFilePath(final Path logDirPath, final DateTime now) {
    return Paths.get(logDirPath.toString(), BATCH_NAME + "_" + now.toString(DATETIME_PATTERN) + ".log");
  }

  protected static boolean addArrangeTaskList(
      final BbOldFilesArrangeProps props,
      final AddAllArrangeTaskDto addAllArrangeTaskDto) {
    try (Stream<Path> oldBackupDirStream = Files.list(addAllArrangeTaskDto.getOldBackupDirPath())) {
      oldBackupDirStream
          .parallel()
          .filter(BbOldFilesArrangeBatch::isGenerationBackupDirectory)
          .forEach(path -> {
            addArrangeTaskList(props.getLatestBackupDirPath(),new GenerationDirectory(path),addAllArrangeTaskDto);
          });
    } catch (Exception e) {
      throw new BkFileException(props.getOldBackupDirPath().toString(), e);
    }
    addAllArrangeTaskDto.getExecutor().shutdown();

    if(addAllArrangeTaskDto.getFutures().size() == 0) {
      return true;
    }
     return addAllArrangeTaskDto.getFutures().stream()
          .allMatch(future -> {
            try {
              return future.get();
            } catch (Exception e) {
              return false;
            }
          });
  }
  protected static void addArrangeTaskList(
      final Path latestBackupPath,
      final GenerationDirectory generationDirectory,
      final AddAllArrangeTaskDto addAllArrangeTaskDto) {

    if(Files.isDirectory(latestBackupPath)
        && pathStreamPredicate(latestBackupPath, (pathStream) -> pathStream.count() == 0)) {
      log.info("{}, is empty dirctory.",latestBackupPath);
      return;
    }
    if(Files.isDirectory(generationDirectory.getPath())
        && pathStreamPredicate(generationDirectory.getPath(), (pathStream) -> pathStream.count() == 0)) {
      log.info("{}, is empty dirctory.",latestBackupPath);
      return;
    }

    if (Files.isDirectory(latestBackupPath)) {
      try (Stream<Path> latestBackupDirStream = Files.list(latestBackupPath)) {
        latestBackupDirStream
            .parallel()
            .forEach(childLatestBackupPath ->
                addArrangeTaskList(childLatestBackupPath,  generationDirectory,addAllArrangeTaskDto)
            );
      } catch (Exception e) {
        throw new BkFileException(latestBackupPath.toString(), e);
      }
    }

    final ArrangeOldBackupDirectoryStatus status = addAllArrangeTaskDto.getStatusMap()
    .map.computeIfAbsent(generationDirectory, k -> new ArrangeOldBackupDirectoryStatus());

    final OldBackupDirectoryArrangeTask task = new OldBackupDirectoryArrangeTask(latestBackupPath, generationDirectory.getPath(), addAllArrangeTaskDto, status);
    addAllArrangeTaskDto.getFutures().add(addAllArrangeTaskDto.getExecutor().submit(task));

  }

  protected static boolean pathStreamPredicate(final Path path, final Predicate<Stream<Path>> predicate) {
    try (Stream<Path> pathStream = Files.list(path)) {
      return predicate.test(pathStream);
    }catch (Exception e) {
      throw new BkFileException(path.toString(), e);
    }
  }

  protected static class OldBackupDirectoryArrangeTask implements Callable<Boolean> {
    private Path latestBackupFilePath;
    private Path oldBackupDirPath;
    private List<String> delFileNames;
    private List<String> deleteExtensions;
    private ArrangeOldBackupDirectoryStatus status;

    public OldBackupDirectoryArrangeTask(
        final Path latestBackupFilePath,
        final Path oldBackupDirPath,
        final AddAllArrangeTaskDto addAllArrangeTaskDto,
        final ArrangeOldBackupDirectoryStatus status) {
      this.latestBackupFilePath = latestBackupFilePath;
      this.oldBackupDirPath = oldBackupDirPath;
      this.delFileNames = addAllArrangeTaskDto.getDelFileNames();
      this.deleteExtensions = addAllArrangeTaskDto.getDeleteExtensions();
      this.status = status;
    }

    @Override
    public Boolean call() throws Exception {
      try (Stream<Path> oldBackupPathDirStream
          = Files.find(oldBackupDirPath,Integer.MAX_VALUE,
              (path,BasicFileAttributes) -> !Files.isDirectory(path))) {

        oldBackupPathDirStream
            .parallel()
            .forEach(path -> oldFileDelete(path));
      } catch (IOException e) {
        throw new BkFileException(oldBackupDirPath.toString(), e);
      }
      return true;
    }

    protected void oldFileDelete(final Path path) {
      final Map<Path,AtomicLong> delFileNamesMap = new HashMap<>();
      final StringBuilder sb = new StringBuilder(path.toString());
      try {
        if (path.getFileName().equals(latestBackupFilePath.getFileName())) {
          sb.append(", match for latest backup fileName." + latestBackupFilePath);
          // Files.delete(path);
          status.deletedFileCount.incrementAndGet();
          log.info("{}", sb.toString());
          return;
        }
        if (delFileNames.contains(path.getFileName().toString())) {
          sb.append(", match for delFileNames.");
          // Files.delete(path);
          status.deletedFileCount.incrementAndGet();
          delFileNamesMap.computeIfAbsent(path.getFileName(), k -> new AtomicLong(0L)).incrementAndGet();
//          log.info("{}", sb.toString());
          return;
        }
        final String extension = BkFileUtils.getExtension(path.getFileName());
        if (!Strings.isNullOrEmpty(extension) && deleteExtensions.contains(extension)) {
          sb.append(", match for deleteExtensions.");
          // Files.delete(path);
          status.deletedFileCount.incrementAndGet();
          log.info("{}", sb.toString());
          return;
        }
        if (!path.getFileName().equals(latestBackupFilePath.getFileName())
            && Files.size(path) == Files.size(latestBackupFilePath)
            && Files.getLastModifiedTime(path).equals(Files.getLastModifiedTime(latestBackupFilePath))) {
          sb.append(",match for latest backup file contents. latest: " + latestBackupFilePath.toString());
          // Files.delete(path);
          status.deletedFileCount.incrementAndGet();
          status.nameChangedFileCount.incrementAndGet();
        }
      } catch (Exception e) {
        status.deleteFailedFileCount.incrementAndGet();
        status.deleteFailedFilePathList.add(path);
        log.info("{}", sb.toString());
      }

    }

  }

  protected static boolean isGenerationBackupDirectory(final Path path) {
    if(!Files.isDirectory(path)){
      return false;
    }
    try {
      DateTimeFormat.forPattern(DATETIME_PATTERN).parseDateTime(path.getFileName().toString());
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  @Value
  protected static class AddAllArrangeTaskDto {
    private ExecutorService executor;
    private List<Future<Boolean>> futures;
    private Path oldBackupDirPath;
    private List<String> delFileNames;
    private List<String> deleteExtensions;
    private GenerationDirectoryStatusMap statusMap;

    public AddAllArrangeTaskDto(final BbOldFilesArrangeProps props) {
      this.executor = Executors.newFixedThreadPool(MAX_PALLARELL_THREADS);
      this.futures = new ArrayList<>();
      this.oldBackupDirPath = props.getOldBackupDirPath();
      this.delFileNames = props.getDelFileNames();
      this.deleteExtensions = props.getDeleteExtensions();
      this.statusMap = new GenerationDirectoryStatusMap();
    }
  }

  @Value
  protected static class BbOldFileArrangeResult {
    private PropsDto<BbOldFilesArrangeProps> propsDto;
    private String backUpDirectoryPath;
    private int generatsionDirectoryCount;
    private GenerationDirectory OldestGenerationDirectory;
    private GenerationDirectory LatestGenerationDirectory;
    private long directoryCount;
    private long fileCount;
    private long backUpFileyCount;
    private long deletedDirectoryCount;
    private long deletedFileCount;
    private long duplicateFileCount;
    private long nameChangedFileCount;
    private GenerationDirectoryStatusMap generationDirectorStatusyMap;

    protected BbOldFileArrangeResult(
        final PropsDto<BbOldFilesArrangeProps> propsDto,
        final Path backUpDirectoryPath,
        final GenerationDirectoryStatusMap statusMap) {
      this.propsDto = propsDto;
      this.backUpDirectoryPath = backUpDirectoryPath.toString();
      this.generatsionDirectoryCount = statusMap.map.keySet().size();
      this.OldestGenerationDirectory = statusMap.map.firstKey();
      this.LatestGenerationDirectory = statusMap.map.lastKey();
      this.directoryCount = statusMap.map.values().stream()
          .map(status -> status.directoryCount)
          .count();
      this.fileCount = statusMap.map.values().stream()
          .map(status -> status.fileCount)
          .count();
      this.backUpFileyCount = statusMap.map.values().stream()
          .map(status -> status.backupFileyCount)
          .count();
      this.deletedDirectoryCount = statusMap.map.values().stream()
          .map(status -> status.oldDeletedDirectoryCount)
          .count();
      this.deletedFileCount = statusMap.map.values().stream()
          .map(status -> status.deletedFileCount)
          .count();
      this.duplicateFileCount = statusMap.map.values().stream()
          .map(status -> status.duplicateFileCount)
          .count();
      this.nameChangedFileCount = statusMap.map.values().stream()
          .map(status -> status.nameChangedFileCount)
          .count();
      this.generationDirectorStatusyMap = statusMap;
    }
  }

  protected static class GenerationDirectoryStatusMap {
    protected ConcurrentSkipListMap<GenerationDirectory, ArrangeOldBackupDirectoryStatus> map = new ConcurrentSkipListMap<>();
  }

  @Value
  protected static class GenerationDirectory implements Comparable<GenerationDirectory> {
    private Path path;
    private DateTime createDateTime;

    public GenerationDirectory(final Path path) {
      this.path = path;
      this.createDateTime = DateTimeFormat.forPattern(DATETIME_PATTERN).parseDateTime(path.getFileName().toString());
    }

    @Override
    public int compareTo(GenerationDirectory generationDirectory) {
      return this.createDateTime.isAfter(generationDirectory.getCreateDateTime()) ? 1 : -1;
    }

    @Override
    public String toString() {
      return this.path.toString();
    }
  }

  protected static class ArrangeOldBackupDirectoryStatus {
    protected AtomicLong directoryCount = new AtomicLong(0L);
    protected AtomicLong fileCount = new AtomicLong(0L);
    protected AtomicLong backupFileyCount = new AtomicLong(0L);
    protected AtomicLong backupFailedFileCount = new AtomicLong(0L);
    protected AtomicLong latestDeletedDirectoryCount = new AtomicLong(0L);
    protected AtomicLong oldDeletedDirectoryCount = new AtomicLong(0L);
    protected AtomicLong deleteFailedDirectoryCount = new AtomicLong(0L);
    protected AtomicLong deletedFileCount = new AtomicLong(0L);
    protected AtomicLong duplicateFileCount = new AtomicLong(0L);
    protected AtomicLong nameChangedFileCount = new AtomicLong(0L);
    protected AtomicLong deleteFailedFileCount = new AtomicLong(0L);
    protected List<Path> backupFailedFileList = new ArrayList<>();
    protected List<Path> deleteFailedDirPathList = new ArrayList<>();
    protected List<Path> deleteFailedFilePathList = new ArrayList<>();
  }
}
