package Game.testHarness;

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
  
  //Tests when a player moves to a space where another player is located
  @Test
  public void testBadDestinationPlayerExists() throws IOException {
    testJSONFile("tests/State/7-in.json", "tests/State/7-out.json");
  }

  //Supplied by instructors finds key
  @Test
  public void testSupplied1() throws IOException {
    testJSONFile("tests/State/8-in.json", "tests/State/8-out.json");
  }

  //Supplied by instructors player exits
  @Test
  public void testSupplied2() throws IOException {
    testJSONFile("tests/State/9-in.json", "tests/State/9-out.json");
  }

  //Supplied by instructors player ejected
  @Test
  public void testSupplied3() throws IOException {
    testJSONFile("tests/State/10-in.json", "tests/State/10-out.json");
  }

  //New level move into hallway
  @Test
  public void testMoveIntoHall() throws IOException {
    testJSONFile("tests/State/11-in.json", "tests/State/11-out.json");
  }

  //Bad player and bad tile
  @Test
  public void testDoubleInvalid() throws IOException {
    testJSONFile("tests/State/12-in.json", "tests/State/12-out.json");
  }

  //Move player onto exit but the exit is locked
  @Test
  public void testExitLocked() throws IOException {
    testJSONFile("tests/State/13-in.json", "tests/State/13-out.json");
  }

}
