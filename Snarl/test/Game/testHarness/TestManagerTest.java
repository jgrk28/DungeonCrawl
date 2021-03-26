package Game.testHarness;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import Manager.TestManager;
import State.TestState;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
import org.json.JSONTokener;
import org.junit.Test;

//Tests that the methods for TestState work as expected
public class TestManagerTest {

  private void testJSONFile(String inFile, String outFile) throws IOException {
    System.setIn(new FileInputStream(inFile));
    String expectedOut = new String(Files.readAllBytes(Paths.get(outFile)));

    PrintStream console = System.out;

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream print = new PrintStream(output);
    System.setOut(print);

    TestManager.main(new String[]{});

    JSONTokener expectedTokens = new JSONTokener(expectedOut);
    Object expectedValue = expectedTokens.nextValue();
    JSONArray expectedArray = (JSONArray) expectedValue;
    JSONTokener actualTokens = new JSONTokener(output.toString());
    Object actualValue = actualTokens.nextValue();

    console.println(expectedArray.toString());
    console.println(actualValue.toString());

    assertEquals(expectedArray.toString(), actualValue.toString());
  }

  //Check that an exception is thrown when invalid input is provided
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidJSONFormat() {
    String input = "12";
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    TestManager.main(new String[]{});
  }

  //Tests TODO
  @Test
  public void testNormalMove() throws IOException {
    testJSONFile("tests/Manager/1-in.json", "tests/Manager/1-out.json");
  }

  //Tests TODO
  @Test
  public void testFindingKey() throws IOException {
    testJSONFile("tests/Manager/2-in.json", "tests/Manager/2-out.json");
  }

  //Tests TODO
  @Test
  public void testExitLevel() throws IOException {
    testJSONFile("tests/Manager/3-in.json", "tests/Manager/3-out.json");
  }

}