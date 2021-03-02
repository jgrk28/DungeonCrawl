package model;

import static org.junit.Assert.assertEquals;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class HallTest {
  Hall hall1;
  Hall hall2;
  Room fakeRoom1;
  Room fakeRoom2;
  Space space = new Space();
  Player player = new Player();
  Zombie zombie = new Zombie();
  Ghost ghost = new Ghost();

  private void initHall1() {
    //Hall goes (2,11) -> (5,11) -> (5,8)
    List<Entity> componentMap = Arrays.asList(space, space, space, space, space, space, space);
    List<Point> waypoints = new ArrayList<Point>();
    waypoints.add(new Point(5,11));

    this.hall1 = new Hall(componentMap, waypoints);

    //Doors for hall1
    hall1.connectRooms(new Point(1,11), fakeRoom1, new Point(5,7), fakeRoom2);
  }

  private void initHall2() {
    //Hall goes (4,3) -> (5,3) -> (5,6) -> (2,6) -> (2,10)
    List<Entity> componentMap = Arrays.asList(space, space, space, space, player, space, space,
        space, zombie, ghost, space, space);
    List<Point> waypoints = new ArrayList<Point>();
    waypoints.add(new Point(5,3));
    waypoints.add(new Point(5,6));
    waypoints.add(new Point(2,6));

    this.hall2 = new Hall(componentMap, waypoints);

    //Doors for hall1
    hall2.connectRooms(new Point(3,3), fakeRoom2, new Point(2,11), fakeRoom1);
  }

  //Constructor and connectRooms tested by initializing hallways
  //Since a hallway must connect two rooms the connectRooms function must be called
  //before we display it. In the future we may want to put the connectRooms functionality
  //into the constructor.
  @Test
  public void testHall1Constructor() {
    initHall1();

    String expectedOut = ""
        + "     \n"
        + "    *\n"
        + "    *\n"
        + "    *\n"
        + " ****\n";

    LevelComponentTest.checkLevelComponentLooksLike(this.hall1, expectedOut);
  }

  @Test
  public void testHall2Constructor() {
    initHall2();

    String expectedOut = ""
        + "  **\n"
        + "   *\n"
        + "   *\n"
        + "***P\n"
        + "Z   \n"
        + "G   \n"
        + "*   \n"
        + "*   \n"
        + "    \n";

    LevelComponentTest.checkLevelComponentLooksLike(this.hall2, expectedOut);
  }

  //Tests that the starting rooms are initialized properly. This is done in the constructor
  //and the returned room should be pointing to the same thing that we passed into the constructor.
  @Test
  public void testGetStartRoom() {
    initHall1();
    initHall2();
    assertEquals(this.fakeRoom1, this.hall1.getStartRoom());
    assertEquals(this.fakeRoom2, this.hall2.getStartRoom());
  }

  //Tests that the ending rooms are initialized properly. This is done in the constructor
  //and the returned room should be pointing to the same thing that we passed into the constructor.
  @Test
  public void testGetEndRoom() {
    initHall1();
    initHall2();
    assertEquals(this.fakeRoom2, this.hall1.getEndRoom());
    assertEquals(this.fakeRoom1, this.hall2.getEndRoom());
  }

}