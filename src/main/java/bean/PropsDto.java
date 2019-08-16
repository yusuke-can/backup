package bean;

import java.nio.file.Path;

import lombok.Value;

/**
 * @author Y.Kyan
 *
 * 2016/02/11
 */

@Value
public class PropsDto<P extends PropsIf> {
  private Path propertyFilePath;
  private P props;
}
