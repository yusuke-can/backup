package bean;

import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import enums.CompressExtentsionType;

/**
 * @author Y.Kyan
 *
 * 2016/02/11
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BbOldFilesArrangeProps {
  public static final BbOldFilesArrangeProps EMPTY = new BbOldFilesArrangeProps(
      null, null, null, null, false, Collections.emptyList(), false, null, Collections.emptyList());

  private String latestBackupDirPath;
  private String oldBackupDirPath;
  private String logDirPath;
  private String aggregateDirPath;
  private Boolean delDuplicateFile; 
  private List<String> delFileNames;
  private Boolean delCompressSourceDir;
  private CompressExtentsionType compressExtensionType;
  private List<String> excludeCompressExtensions;

  public boolean isEmpty() {
    return this.equals(EMPTY);
  }
}
