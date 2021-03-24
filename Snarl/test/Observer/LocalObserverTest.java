package Observer;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Test;

public class LocalObserverTest {

  @Test
  public void testUpdateSimple() {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream print = new PrintStream(output);
    System.setOut(print);

    LocalObserver observer = new LocalObserver();
    observer.update("Hello World");

    assertEquals("Hello World\n", output.toString());
  }

  @Test
  public void testUpdateState() {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream print = new PrintStream(output);
    System.setOut(print);

    String input = ""
        + "X.XX         \n"
        + "X..X         \n"
        + "X..X         \n"
        + "X..X    XXXXX\n"
        + "..@.****GZ..X\n"
        + "XXXX    X...X\n"
        + "        X...X\n"
        + "        X...X\n"
        + "        XXXXX\n";

    String expectedOut = ""
        + "X.XX         \n"
        + "X..X         \n"
        + "X..X         \n"
        + "X..X    XXXXX\n"
        + "..@.****GZ..X\n"
        + "XXXX    X...X\n"
        + "        X...X\n"
        + "        X...X\n"
        + "        XXXXX\n"
        + "\n";

    LocalObserver observer = new LocalObserver();
    observer.update(input);

    assertEquals(expectedOut, output.toString());
  }
}