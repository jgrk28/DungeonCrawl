package Game.testHarness;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
public class TestStateTest {

  private void testJSONFile(String inFile, String outFile) throws IOException {
    System.setIn(new FileInputStream(inFile));
    String expectedOut = new String(Files.readAllBytes(Paths.get(outFile)));

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream print = new PrintStream(output);
    System.setOut(print);

    TestState.main(new String[]{});

    JSONTokener expectedTokens = new JSONTokener(expectedOut);
    Object expectedValue = expectedTokens.nextValue();
    JSONArray expectedArray = (JSONArray) expectedValue;
    JSONTokener actualTokens = new JSONTokener(output.toString());
    Object actualValue = actualTokens.nextValue();

    assertTrue(expectedArray.similar(actualValue));
  }

  //Check that an exception is thrown when invalid input is provided
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidJSONFormat() {
    String input = "12";
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    TestState.main(new String[]{});
  }

  //Tests a normal move
  @Test
  public void testNormalMove() throws IOException {
    testJSONFile("tests/State/1-in.json", "tests/State/1-out.json");
  }

  //Tests finding a key
  @Test
  public void testFindingKey() throws IOException {
    testJSONFile("tests/State/2-in.json", "tests/State/2-out.json");
  }

  //Tests exiting the level
  @Test
  public void testExitLevel() throws IOException {
    testJSONFile("tests/State/3-in.json", "tests/State/3-out.json");
  }

  //Tests self-elimination from the level
  @Test
  public void testRemovedPlayer() throws IOException {
    testJSONFile("tests/State/4-in.json", "tests/State/4-out.json");
  }

  //Tests bad player
  @Test
  public void testBadPlayer() throws IOException {
    testJSONFile("tests/State/5-in.json", "tests/State/5-out.json");
  }

  //Tests bad destination
  @Test
  public void testBadDestination() throws IOException {
    testJSONFile("tests/State/6-in.json", "tests/State/6-out.json");
  }
}
