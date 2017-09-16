package utils;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
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

  protected enum FinalResultType {
    STAY,
    EXCLUDE
  }

  protected enum ResultType {
    STAY,
    EXCLUDE,
    UNCONFIRMED
  }

  protected <T,V> Map<FinalResultType,List<V>> createFinalResltMap(
      final List<T> list,
      final Predicate<T> predicate,
      final Function<T,V> valueConverter
      ) {
    final Map<FinalResultType,List<V>> map = new EnumMap<>(FinalResultType.class);
    final Function<T, FinalResultType> keyTypeConverter
       = (t) -> predicate.test(t) ? FinalResultType.STAY : FinalResultType.EXCLUDE;
    list.stream().forEach(t -> map.get(keyTypeConverter.apply(t)).add(valueConverter.apply(t)));
    return map;
  }

  protected interface FinalResultAccumuratorIf<S> {
    List<S> getStayListAndIncrementExCounter();
  }


  public static class CalcResultAcc implements FinalResultAccumuratorIf<Double> {
    private FinalResult<Double, String> result = new FinalResult<Double, String>();
    private Predicate<Integer> predicate;
    private Function<Double, Double> stayConverter;
    private Function<Double, String> excludeConverter;

    public CalcResultAcc() {
      this.result = new FinalResult<Double, String>();
      this.predicate = i -> i % 2 == 0;
      this.stayConverter = d -> d;
      this.excludeConverter = d -> {
        final BigDecimal bd = new BigDecimal(d).setScale(3, RoundingMode.HALF_UP);
        return String.format("%s", bd.doubleValue());
      };
    }

    public boolean accumulate(final Integer t, final Double v) {
      final boolean isStay = predicate.test(t);
      if (isStay) {
        this.result.getStayList().add(stayConverter.apply(v));
      } else {
        this.result.getExcludeList().add(excludeConverter.apply(v));
      }
      return isStay;
    }

    @Override
    public List<Double> getStayListAndIncrementExCounter() {
      return result.getStayList();
    }
  }

  @Value
  protected static class FinalResult<S,E> {
    private List<S> stayList = new ArrayList<>();
    private List<E> excludeList = new ArrayList<>();
  }

//  protected static class DivideFinalResult<> {
//
//  }


  @Test
  public void testStream1() {

    final String results = IntStream.iterate(100, i -> i + 10)
    .limit(100)
    .mapToObj(Double::valueOf)
    .flatMap(d -> Stream.of(d, d/2,d/3))
    .map(d -> new BigDecimal(d).setScale(3, RoundingMode.HALF_UP))
    .sorted(Comparator.reverseOrder())
    .map(bd -> String.format("%s", bd.doubleValue()))
    .collect(Collectors.joining(","));

    log.info("devide and set scale 3 round half up reverse orderd: {}", results);
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
