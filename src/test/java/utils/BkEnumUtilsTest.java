package utils;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.EnumSet;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;

//import static org.hamcrest.MatcherAssert.*;
//import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.Test;

import util.BkEnumUtils;

/**
 * @author Y.Kyan
 *
 * 2016/02/11
 */
public class BkEnumUtilsTest {

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

    public static AuthorityCodeType getType(final String code) {
      return Optional.ofNullable(code).flatMap(
          cd -> EnumSet.allOf(AuthorityCodeType.class).stream().filter(en -> en.getCode().equals(cd)).findFirst())
          .orElse(NONE);
    }
  }

  @Before
  public void before() {
  }

  @Test
  public void test_getType() {
    assertThat(BkEnumUtils.getType(null, TestEnumType.class), nullValue());
    assertThat(BkEnumUtils.getType("", TestEnumType.class), nullValue());
    assertThat(BkEnumUtils.getType("UPPER_UNDERSCORE", TestEnumType.class), is(TestEnumType.UPPER_UNDERSCORE));
    assertThat(BkEnumUtils.getType("upper_underscore", TestEnumType.class), nullValue());
    assertThat(BkEnumUtils.getType("NONE_TYPE", TestEnumType.class), nullValue());
  }

  @Test
  public void test_getTypeIgnoreCase() {
    assertThat(BkEnumUtils.getTypeIgnoreCase(null, TestEnumType.class), nullValue());
    assertThat(BkEnumUtils.getTypeIgnoreCase("", TestEnumType.class), nullValue());
    assertThat(BkEnumUtils.getTypeIgnoreCase("UPPER_UNDERSCORE", TestEnumType.class), is(TestEnumType.UPPER_UNDERSCORE));
    assertThat(BkEnumUtils.getTypeIgnoreCase("upper_underscore", TestEnumType.class), is(TestEnumType.UPPER_UNDERSCORE));
    assertThat(BkEnumUtils.getTypeIgnoreCase("NONE_TYPE", TestEnumType.class), nullValue());
  }

  @Test
  public void test_getEnumType() {
    assertThat(BkEnumUtils.getEnumType(null, GenderCodeType.class, (cd, en) -> en.getCode().equals(cd)), nullValue());
    assertThat(BkEnumUtils.getEnumType(1, GenderCodeType.class, (cd, en) -> en.getCode().equals(cd)), is(GenderCodeType.MALE));
    assertThat(BkEnumUtils.getEnumType(-1, GenderCodeType.class, (cd, en) -> en.getCode().equals(cd)), nullValue());
  }

  @Test
  public void getEnumTypeOrDefault() {
    assertThat(BkEnumUtils.getEnumTypeOrDefault(null, GenderCodeType.class, (cd, en) -> en.getCode().equals(cd), GenderCodeType.UNKNOWN), is(GenderCodeType.UNKNOWN));
    assertThat(BkEnumUtils.getEnumTypeOrDefault(1, GenderCodeType.class, (cd, en) -> en.getCode().equals(cd), GenderCodeType.UNKNOWN), is(GenderCodeType.MALE));
    assertThat(BkEnumUtils.getEnumTypeOrDefault(-1, GenderCodeType.class, (cd, en) -> en.getCode().equals(cd), GenderCodeType.UNKNOWN), is(GenderCodeType.UNKNOWN));
  }

}
