package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class RoomTest {
  Room room1;
  Room room2;
  Room room3;
  Wall wall = new Wall();
  Space space = new Space();

  //Once we make the real constructors we will need to change how the rooms are
  //initialized here
  private void initRoom1() {
    //Simple 3x3 room
    List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
    componentMap.add(Arrays.asList(wall, wall, wall));
    componentMap.add(Arrays.asList(wall, space, space));
    componentMap.add(Arrays.asList(wall, wall, wall));

    this.room1 = new Room(new Point(0,0), componentMap);
  }

  private void initRoom2() {
    //Weird 4x5 room with discontinuous space
    List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
    componentMap.add(Arrays.asList(wall, space, wall, wall, wall));
    componentMap.add(Arrays.asList(wall, space, space, wall, wall));
    componentMap.add(Arrays.asList(wall, space, space, space, space));
    componentMap.add(Arrays.asList(space, wall, wall, wall, wall));

    this.room2 = new Room(new Point(15,7), componentMap);
  }

  private void initRoom3() {
    //4x4 room with starting at a negative location
    List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
    componentMap.add(Arrays.asList(wall, wall, wall, wall));
    componentMap.add(Arrays.asList(wall, space, space, wall));
    componentMap.add(Arrays.asList(wall, space, space, wall));
    componentMap.add(Arrays.asList(wall, wall, wall, wall));

    this.room3 = new Room(new Point(-2,-1), componentMap);
  }

  @Test
  public void testRoom1Constructor() {
    initRoom1();

    String expectedOut = ""
        + "XXX\n"
        + "X..\n"
        + "XXX\n";

    LevelComponentTest.checkLevelComponentLooksLike(this.room1, expectedOut);
  }

  @Test
  public void testRoom2Constructor() {
    initRoom2();

    String expectedOut = ""
        + "X.XXX\n"
        + "X..XX\n"
        + "X....\n"
        + ".XXXX\n";

    LevelComponentTest.checkLevelComponentLooksLike(this.room2, expectedOut);
  }

  @Test
  public void testRoom3Constructor() {
    //This room has negative starting position
    initRoom3();

    String expectedOut = ""
        + "XXXX\n"
        + "X..X\n"
        + "X..X\n"
        + "XXXX\n";

    LevelComponentTest.checkLevelComponentLooksLike(this.room3, expectedOut);
  }

  //Cannot test connectHall yet because there is no displayable effect of
  //connecting a hall and we do not want to add getters for the doors.
}