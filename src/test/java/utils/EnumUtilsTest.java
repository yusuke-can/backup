package utils;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

//import static org.hamcrest.MatcherAssert.*;
//import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.Test;

import util.EnumUtils;

/**
 * @author Y.Kyan
 *
 * 2016/02/11
 */
public class EnumUtilsTest {

  private enum TestEnumType {
    UPPER_UNDERSCORE,
    lower_underscore,
    UppeerCamel,
    lowerCamel, ;
  }

  private interface CodeTypeIf<T> {
    T getCode();
  }

  @Getter
  @AllArgsConstructor
  private enum GenderCodeType implements CodeTypeIf<Integer> {
    UNKNOWN(0),
    MALE(1),
    FEMALE(2), ;
    private Integer code;
  }

  @Getter
  @AllArgsConstructor
  private enum AuthorityCodeType implements CodeTypeIf<String> {
    ADMIN("11111"),
    CEO("01111"),
    DIRECTOR("00111"),
    OFFICER("00011"),
    STAFF("00001"),
    NONE("00000"), ;
    private String code;
  }

  @Before
  public void before() {
  }

  @Test
  public void test_getType() {
    assertThat(EnumUtils.getType(null, TestEnumType.class), nullValue());
    assertThat(EnumUtils.getType("", TestEnumType.class), nullValue());
    assertThat(EnumUtils.getType("UPPER_UNDERSCORE", TestEnumType.class), is(TestEnumType.UPPER_UNDERSCORE));
    assertThat(EnumUtils.getType("upper_underscore", TestEnumType.class), nullValue());
    assertThat(EnumUtils.getType("NONE_TYPE", TestEnumType.class), nullValue());
  }

  @Test
  public void test_getTypeIgnoreCase() {
    assertThat(EnumUtils.getTypeIgnoreCase(null, TestEnumType.class), nullValue());
    assertThat(EnumUtils.getTypeIgnoreCase("", TestEnumType.class), nullValue());
    assertThat(EnumUtils.getTypeIgnoreCase("UPPER_UNDERSCORE", TestEnumType.class), is(TestEnumType.UPPER_UNDERSCORE));
    assertThat(EnumUtils.getTypeIgnoreCase("upper_underscore", TestEnumType.class), is(TestEnumType.UPPER_UNDERSCORE));
    assertThat(EnumUtils.getTypeIgnoreCase("NONE_TYPE", TestEnumType.class), nullValue());
  }

  @Test
  public void test_getEnumType() {
    assertThat(EnumUtils.getEnumType(null, GenderCodeType.class, (code, en) -> en.getCode().equals(code)), nullValue());
    assertThat(EnumUtils.getEnumType(1, GenderCodeType.class, (code, en) -> en.getCode().equals(code)), is(GenderCodeType.MALE));
    assertThat(EnumUtils.getEnumType(-1, GenderCodeType.class, (code, en) -> en.getCode().equals(code)), nullValue());
  }

  @Test
  public void getEnumTypeOrDefault() {
    assertThat(EnumUtils.getEnumTypeOrDefault(null, GenderCodeType.class, (code, en) -> en.getCode().equals(code), GenderCodeType.UNKNOWN), is(GenderCodeType.UNKNOWN));
    assertThat(EnumUtils.getEnumTypeOrDefault(1, GenderCodeType.class, (code, en) -> en.getCode().equals(code), GenderCodeType.UNKNOWN), is(GenderCodeType.MALE));
    assertThat(EnumUtils.getEnumTypeOrDefault(-1, GenderCodeType.class, (code, en) -> en.getCode().equals(code), GenderCodeType.UNKNOWN), is(GenderCodeType.UNKNOWN));
  }

}
