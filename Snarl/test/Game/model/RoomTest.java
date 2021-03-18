package Game.model;

import static org.junit.Assert.assertEquals;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests that the methods and constructors for Room work as expected
 */
public class RoomTest {
  Room room1;
  Room room2;
  Room room3;

  @Before
  public void initEntities() {
    ModelCreator creator = new ModelCreator();
    this.room1 = creator.initializeRoom5();
    this.room2 = creator.initializeRoom6();
    this.room3 = creator.initializeRoom7();
  }

  //Test that Room1 is constructed correctly
  @Test
  public void testRoom1Constructor() {
    String expectedOut = ""
        + "XXXX\n"
        + "X!PX\n"
        + "X..X\n"
        + "XGXX\n";

    LevelComponentTest.checkLevelComponentLooksLike(this.room1, expectedOut);
  }

  //Test that Room2 is constructed correctly
  @Test
  public void testRoom2Constructor() {
    String expectedOut = ""
        + ".X\n"
        + "X@\n";

    LevelComponentTest.checkLevelComponentLooksLike(this.room2, expectedOut);
  }

  //Test that Room3 is constructed correctly
  //This room has negative starting position
  @Test
  public void testRoom3Constructor() {
    String expectedOut = ""
        + "XXX\n"
        + "X.X\n"
        + "XZX\n";

    LevelComponentTest.checkLevelComponentLooksLike(this.room3, expectedOut);
  }

  @Test
  public void testGetOrigin() {
    assertEquals(new Point(0, 0), this.room1.getOrigin());
    assertEquals(new Point(15, 7), this.room2.getOrigin());
    assertEquals(new Point(-5, -1), this.room3.getOrigin());
  }

  //Also tests connectHall
  @Test
  public void testGetDoors() {
    Hall fakeHall1 = new Hall(new ArrayList<>(), new ArrayList<>()); //Dummy variables to test doors
    Hall fakeHall2 = new Hall(new ArrayList<>(), new ArrayList<>());
    Point door1 = new  Point(15, 7);
    Point door2 = new  Point(16, 8);

    Map<Point, Hall> doors = new HashMap<>();
    doors.put(door1, fakeHall1);
    doors.put(door2, fakeHall2);

    this.room2.connectHall(door1, fakeHall1);
    this.room2.connectHall(door2, fakeHall2);

    assertEquals(doors, this.room2.getDoors());
  }
}