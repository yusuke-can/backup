package bean;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import enums.CompressExtensionType;

/**
 * @author Y.Kyan
 *
 * 2016/02/11
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BbOldFilesArrangeProps implements PropsIf {
  public static final BbOldFilesArrangeProps EMPTY = new BbOldFilesArrangeProps(
      null, null, null, null, false, Collections.emptyList(), false, null, Collections.emptyList());

  private Path latestBackupDirPath;
  private Path oldBackupDirPath;
  private Path logDirPath;
  private Path aggregateDirPath;
  private boolean delDuplicateFile;
  private List<String> delFileNames;
  private boolean delCompressSourceDir;
  private CompressExtensionType compressExtensionType;
  private List<String> deleteExtensions;

  @Override
  public boolean isEmpty() {
    return this.equals(EMPTY);
  }
}
