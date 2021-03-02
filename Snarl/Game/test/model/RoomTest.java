package model;

import static org.junit.Assert.assertEquals;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

/**
 * Tests that the methods and constructors for Room work as expected
 */
public class RoomTest {
  Room room1;
  Room room2;
  Room room3;
  Wall wall = new Wall();
  Space space = new Space();
  Key key = new Key(new Point(-1, 1));
  Exit exit = new Exit(new Point(17, 9));
  Player player = new Player();
  Ghost ghost = new Ghost();
  Zombie zombie = new Zombie();

  //Once we make the real constructors we will need to change how the rooms are
  //initialized here
  private void initRoom1() {
    //Simple 3x3 room
    List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
    componentMap.add(Arrays.asList(wall, wall, wall));
    componentMap.add(Arrays.asList(wall, player, space));
    componentMap.add(Arrays.asList(wall, wall, wall));

    this.room1 = new Room(new Point(0,0), componentMap);
  }
  
  //Odd 4x5 room with discontinuous space
  private void initRoom2() {
    List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
    componentMap.add(Arrays.asList(wall, space, wall, wall, wall));
    componentMap.add(Arrays.asList(wall, ghost, zombie, wall, wall));
    componentMap.add(Arrays.asList(wall, space, exit, space, space));
    componentMap.add(Arrays.asList(space, wall, wall, wall, wall));

    this.room2 = new Room(new Point(15,7), componentMap);
  }

  //4x4 room with starting at a negative location
  private void initRoom3() {
    List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
    componentMap.add(Arrays.asList(wall, wall, wall, wall));
    componentMap.add(Arrays.asList(wall, space, space, wall));
    componentMap.add(Arrays.asList(wall, key, space, wall));
    componentMap.add(Arrays.asList(wall, wall, wall, wall));

    this.room3 = new Room(new Point(-2,-1), componentMap);
  }

  //Test that Room1 is constructed correctly
  @Test
  public void testRoom1Constructor() {
    initRoom1();

    String expectedOut = ""
        + "XXX\n"
        + "XP.\n"
        + "XXX\n";

    LevelComponentTest.checkLevelComponentLooksLike(this.room1, expectedOut);
  }

  //Test that Room2 is constructed correctly
  @Test
  public void testRoom2Constructor() {
    initRoom2();

    String expectedOut = ""
        + "X.XXX\n"
        + "XGZXX\n"
        + "X.@..\n"
        + ".XXXX\n";

    LevelComponentTest.checkLevelComponentLooksLike(this.room2, expectedOut);
  }

  //Test that Room3 is constructed correctly
  //This room has negative starting position
  @Test
  public void testRoom3Constructor() {
    initRoom3();

    String expectedOut = ""
        + "XXXX\n"
        + "X..X\n"
        + "X!.X\n"
        + "XXXX\n";

    LevelComponentTest.checkLevelComponentLooksLike(this.room3, expectedOut);
  }

  @Test
  public void testGetOrigin() {
    initRoom1();
    initRoom2();
    initRoom3();
    assertEquals(new Point(0, 0), this.room1.getOrigin());
    assertEquals(new Point(15, 7), this.room2.getOrigin());
    assertEquals(new Point(-2, 1), this.room3.getOrigin());
  }

  //Also tests connectHall
  @Test
  public void testGetDoors() {
    initRoom2();

    Hall fakeHall1 = new Hall(new ArrayList<>(), new ArrayList<>()); //Dummy variables to test doors
    Hall fakeHall2 = new Hall(new ArrayList<>(), new ArrayList<>());
    Point door1 = new  Point(16, 6);
    Point door2 = new  Point(20, 10);

    //Map<Point, Hall> doors = new HashMap<>();
    //doors.put(door1, fakeHall1);
    //doors.put(door2, fakeHall2);

    this.room2.connectHall(door1, fakeHall1);
    this.room2.connectHall(door2, fakeHall2);

    //assertEquals(doors, this.roo2.getDoors());

    Map<Point, Hall> actualDoors = this.room2.getDoors();
    Hall actualHall1 = actualDoors.get(door1);
    Hall actualHall2 = actualDoors.get(door2);

    assertEquals(fakeHall1, actualHall1);
    assertEquals(fakeHall2, actualHall2);
  }
}