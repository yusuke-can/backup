package batch;

import bean.BbOldFilesArrangeProps;
import factory.BbOldFilesArrangePropsFactory;


/**
 *
 * @author Y.Kyan
 *
 * 2016/02/11
 */
public class BbOldFilesArrangeBatch {

  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO 引数のプロパティファイル読み込み
    if(args.length != 1) {
      throw new IllegalArgumentException("invalid arguments. required argument:propertyFilePath. args:"+ args);
    }
    final BbOldFilesArrangeProps props = BbOldFilesArrangePropsFactory.create(args[0]);

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




}
