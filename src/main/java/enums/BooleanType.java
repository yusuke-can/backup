package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Y.Kyan
 *
 * 2016/03/03
 */
@Getter
@AllArgsConstructor
public enum BooleanType {
  TRUE(Boolean.TRUE),
  FALSE(Boolean.FALSE);

  private Boolean boolValue;

  public static BooleanType valueOf(final boolean boolValue) {
    return (boolValue) ? BooleanType.TRUE : BooleanType.FALSE;
  }
}
