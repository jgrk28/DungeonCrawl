package Player;

import static org.junit.Assert.*;

import Game.model.Dungeon;
import Game.model.ModelCreator;
import Game.model.Player;
import Game.modelView.DungeonModelView;
import Game.modelView.PlayerModelView;
import com.sun.org.apache.xpath.internal.operations.Mod;
import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class LocalPlayerTest {

  @Test
  public void testTakeTurn() {
    ByteArrayInputStream in = new ByteArrayInputStream("3 4".getBytes());
    System.setIn(in);

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream print = new PrintStream(output);
    System.setOut(print);

    LocalPlayer player = new LocalPlayer();
    List<Point> validMoves = new ArrayList<>(
        Arrays.asList(new Point(3, 4), new Point(3, 3), new Point(4, 3)));
    Point returnedPoint = player.takeTurn(validMoves);

    String expectedOut = ""
        + "Please enter a valid move from the below list:\n"
        + "[3,4], [3,3], [4,3]\n"
        + "Enter x position\n"
        + "Enter y position\n";

    assertEquals(expectedOut, output.toString());
    assertEquals(new Point(3, 4), returnedPoint);
  }

  @Test
  public void testTakeTurnBadMove() {
    ByteArrayInputStream in = new ByteArrayInputStream("6 12".getBytes());
    System.setIn(in);

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream print = new PrintStream(output);
    System.setOut(print);

    LocalPlayer player = new LocalPlayer();
    List<Point> validMoves = new ArrayList<>(Arrays.asList(
        new Point(1, 1),
        new Point(1, 0),
        new Point(1, 2),
        new Point(0, 0),
        new Point(2, 2),
        new Point(1, 3)));
    Point returnedPoint = player.takeTurn(validMoves);

    String expectedOut = ""
        + "Please enter a valid move from the below list:\n"
        + "[1,1], [1,0], [1,2], [0,0], [2,2], [1,3]\n"
        + "Enter x position\n"
        + "Enter y position\n";

    assertEquals(expectedOut, output.toString());
    assertEquals(new Point(6, 12), returnedPoint);
  }

  @Test(expected = java.util.InputMismatchException.class)
  public void testTakeTurnBadInput() {
    ByteArrayInputStream in = new ByteArrayInputStream("4 abcd".getBytes());
    System.setIn(in);

    LocalPlayer player = new LocalPlayer();
    List<Point> validMoves = new ArrayList<>(
        Arrays.asList(new Point(3, 4), new Point(3, 3), new Point(4, 3)));
    player.takeTurn(validMoves);
  }

  @Test
  public void testUpdate1() {
    ModelCreator creator = new ModelCreator();
    Dungeon dungeon = creator.initializeDungeonStarted();
    DungeonModelView dungeonModelView = dungeon;
    PlayerModelView playerModelView = new PlayerModelView(creator.getPlayer1(), dungeonModelView);

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream print = new PrintStream(output);
    System.setOut(print);

    LocalPlayer player = new LocalPlayer();
    player.update(playerModelView);

    String expectedOut = "You are currently on level: 1\n"
        + "You are active in the level\n"
        + "XX   \n"
        + ".X   \n"
        + "..P**\n"
        + "XX  *\n"
        + "    *\n";

    assertEquals(expectedOut, output.toString());
  }

  @Test
  public void testUpdate2() {
    ModelCreator creator = new ModelCreator();
    Dungeon dungeon = creator.initializeDungeonStarted();
    DungeonModelView dungeonModelView = dungeon;
    PlayerModelView playerModelView = new PlayerModelView(creator.getPlayer3(), dungeonModelView);

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream print = new PrintStream(output);
    System.setOut(print);

    LocalPlayer player = new LocalPlayer();
    player.update(playerModelView);

    String expectedOut = "You are currently on level: 1\n"
        + "You are active in the level\n"
        + "....X\n"
        + "....X\n"
        + ".ZP!X\n"
        + "XXXXX\n"
        + "     \n";

    assertEquals(expectedOut, output.toString());
  }

  @Test
  public void testDisplayMessage() {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream print = new PrintStream(output);
    System.setOut(print);

    LocalPlayer player = new LocalPlayer();
    player.displayMessage("Hello World");

    assertEquals("Hello World\n", output.toString());
  }
}