package enums;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

//import static org.hamcrest.MatcherAssert.*;
//import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Y.Kyan
 *
 * 2016/02/11
 */
public class CompressExtentsionTypeTest {

  @Before
  public void before() {
  }

  @Test
  public void test_equals() {
    assertThat(CompressExtentsionType.ZIP.equals(null), is(false));
    assertThat(CompressExtentsionType.ZIP.equals(""), is(false));
    assertThat(CompressExtentsionType.ZIP.equals("ZIP"), is(true));
    assertThat(CompressExtentsionType.ZIP.equals(".ZIP"), is(true));
    assertThat(CompressExtentsionType.ZIP.equals("ZIP."), is(false));
    assertThat(CompressExtentsionType.ZIP.equals("zip"), is(false));
    assertThat(CompressExtentsionType.ZIP.equals(".zip"), is(false));
  }

  @Test
  public void test_equalsIgnoreCase() {
    assertThat(CompressExtentsionType.ZIP.equalsIgnoreCase(null), is(false));
    assertThat(CompressExtentsionType.ZIP.equalsIgnoreCase(""), is(false));
    assertThat(CompressExtentsionType.ZIP.equalsIgnoreCase("ZIP"), is(true));
    assertThat(CompressExtentsionType.ZIP.equalsIgnoreCase(".ZIP"), is(true));
    assertThat(CompressExtentsionType.ZIP.equalsIgnoreCase("ZIP."), is(false));
    assertThat(CompressExtentsionType.ZIP.equalsIgnoreCase("zip"), is(true));
    assertThat(CompressExtentsionType.ZIP.equalsIgnoreCase(".zip"), is(true));
  }

  @Test
  public void test_get() {
    assertThat(CompressExtentsionType.ZIP.get(), is("zip"));
  }

  @Test
  public void test_equalsFromFile() {
    assertThat(CompressExtentsionType.ZIP.equalsFromFile(null), is(false));
    assertThat(CompressExtentsionType.ZIP.equalsFromFile(""), is(false));
    assertThat(CompressExtentsionType.ZIP.equalsFromFile("test.ZIP"), is(true));
    assertThat(CompressExtentsionType.ZIP.equalsFromFile("test.GZIP"), is(false));
    assertThat(CompressExtentsionType.ZIP.equalsFromFile("ZIP"), is(false));
    assertThat(CompressExtentsionType.ZIP.equalsFromFile(".ZIP"), is(false));
    assertThat(CompressExtentsionType.ZIP.equalsFromFile("test.zip"), is(false));
    assertThat(CompressExtentsionType.ZIP.equalsFromFile("test.gzip"), is(false));
    assertThat(CompressExtentsionType.ZIP.equalsFromFile("zip"), is(false));
    assertThat(CompressExtentsionType.ZIP.equalsFromFile(".zip"), is(false));
    assertThat(CompressExtentsionType.ZIP.equalsFromFile("test."), is(false));
  }

  @Test
  public void test_equalsIgnoreCaseFromFile() {
    assertThat(CompressExtentsionType.ZIP.equalsIgnoreCaseFromFile(null), is(false));
    assertThat(CompressExtentsionType.ZIP.equalsIgnoreCaseFromFile(""), is(false));
    assertThat(CompressExtentsionType.ZIP.equalsIgnoreCaseFromFile("test.ZIP"), is(true));
    assertThat(CompressExtentsionType.ZIP.equalsIgnoreCaseFromFile("test.GZIP"), is(false));
    assertThat(CompressExtentsionType.ZIP.equalsIgnoreCaseFromFile("ZIP"), is(false));
    assertThat(CompressExtentsionType.ZIP.equalsIgnoreCaseFromFile(".ZIP"), is(false));
    assertThat(CompressExtentsionType.ZIP.equalsIgnoreCaseFromFile("test.zip"), is(true));
    assertThat(CompressExtentsionType.ZIP.equalsIgnoreCaseFromFile("test.gzip"), is(false));
    assertThat(CompressExtentsionType.ZIP.equalsIgnoreCaseFromFile("zip"), is(false));
    assertThat(CompressExtentsionType.ZIP.equalsIgnoreCaseFromFile(".zip"), is(false));
    assertThat(CompressExtentsionType.ZIP.equalsIgnoreCaseFromFile("test."), is(false));
  }
}
