package enums;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

//import static org.hamcrest.MatcherAssert.*;
//import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Y.Kyan
 *
 * 2016/02/11
 */
public class CompressExtensionTypeTest {

  @Before
  public void before() {
  }

  @Test
  public void test_equals() {
    assertThat(CompressExtensionType.ZIP.equals(null), is(false));
    assertThat(CompressExtensionType.ZIP.equals(""), is(false));
    assertThat(CompressExtensionType.ZIP.equals("ZIP"), is(true));
    assertThat(CompressExtensionType.ZIP.equals(".ZIP"), is(true));
    assertThat(CompressExtensionType.ZIP.equals("ZIP."), is(false));
    assertThat(CompressExtensionType.ZIP.equals("zip"), is(false));
    assertThat(CompressExtensionType.ZIP.equals(".zip"), is(false));
  }

  @Test
  public void test_equalsIgnoreCase() {
    assertThat(CompressExtensionType.ZIP.equalsIgnoreCase(null), is(false));
    assertThat(CompressExtensionType.ZIP.equalsIgnoreCase(""), is(false));
    assertThat(CompressExtensionType.ZIP.equalsIgnoreCase("ZIP"), is(true));
    assertThat(CompressExtensionType.ZIP.equalsIgnoreCase(".ZIP"), is(true));
    assertThat(CompressExtensionType.ZIP.equalsIgnoreCase("ZIP."), is(false));
    assertThat(CompressExtensionType.ZIP.equalsIgnoreCase("zip"), is(true));
    assertThat(CompressExtensionType.ZIP.equalsIgnoreCase(".zip"), is(true));
  }

  @Test
  public void test_get() {
    assertThat(CompressExtensionType.ZIP.toLower(), is("zip"));
  }

  @Test
  public void test_equalsFromFile() {
    assertThat(CompressExtensionType.ZIP.equalsFromFile(null), is(false));
    assertThat(CompressExtensionType.ZIP.equalsFromFile(""), is(false));
    assertThat(CompressExtensionType.ZIP.equalsFromFile("test.ZIP"), is(true));
    assertThat(CompressExtensionType.ZIP.equalsFromFile("test.GZIP"), is(false));
    assertThat(CompressExtensionType.ZIP.equalsFromFile("ZIP"), is(false));
    assertThat(CompressExtensionType.ZIP.equalsFromFile(".ZIP"), is(false));
    assertThat(CompressExtensionType.ZIP.equalsFromFile("test.zip"), is(false));
    assertThat(CompressExtensionType.ZIP.equalsFromFile("test.gzip"), is(false));
    assertThat(CompressExtensionType.ZIP.equalsFromFile("zip"), is(false));
    assertThat(CompressExtensionType.ZIP.equalsFromFile(".zip"), is(false));
    assertThat(CompressExtensionType.ZIP.equalsFromFile("test."), is(false));
  }

  @Test
  public void test_equalsIgnoreCaseFromFile() {
    assertThat(CompressExtensionType.ZIP.equalsIgnoreCaseFromFile(null), is(false));
    assertThat(CompressExtensionType.ZIP.equalsIgnoreCaseFromFile(""), is(false));
    assertThat(CompressExtensionType.ZIP.equalsIgnoreCaseFromFile("test.ZIP"), is(true));
    assertThat(CompressExtensionType.ZIP.equalsIgnoreCaseFromFile("test.GZIP"), is(false));
    assertThat(CompressExtensionType.ZIP.equalsIgnoreCaseFromFile("ZIP"), is(false));
    assertThat(CompressExtensionType.ZIP.equalsIgnoreCaseFromFile(".ZIP"), is(false));
    assertThat(CompressExtensionType.ZIP.equalsIgnoreCaseFromFile("test.zip"), is(true));
    assertThat(CompressExtensionType.ZIP.equalsIgnoreCaseFromFile("test.gzip"), is(false));
    assertThat(CompressExtensionType.ZIP.equalsIgnoreCaseFromFile("zip"), is(false));
    assertThat(CompressExtensionType.ZIP.equalsIgnoreCaseFromFile(".zip"), is(false));
    assertThat(CompressExtensionType.ZIP.equalsIgnoreCaseFromFile("test."), is(false));
  }
}
