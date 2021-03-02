package model;

import static org.junit.Assert.*;

import java.awt.Point;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import modelView.EntityType;
import modelView.LevelModelView;
import org.junit.Before;
import org.junit.Test;
import view.LevelView;
import view.TextualLevelView;

public class LevelComponentTest {
  //Helper function to create a level add one component to it and then test
  //whether the level looks as expected.
  //This is currently used in both the HallTest and RoomTest classes.
  public static void checkLevelComponentLooksLike(LevelComponent component, String expectedOut) {
    //Create a new ModelView containing just the room that was passed in
    ArrayList<LevelComponent> levelMap = new ArrayList<LevelComponent>();
    levelMap.add(component);
    Key key = null;
    Exit exit = null;
    LevelModelView modelView = new LevelImpl(levelMap, key, exit);
    LevelView view = new TextualLevelView(modelView);

    //Get output string from STDOUT
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream print = new PrintStream(output);
    System.setOut(print);

    //Display level to STDOUT
    view.drawLevel();

    //Check if display output is as expected
    assertEquals(expectedOut, output.toString());
  }

  //Objects for LevelComponent tests
  private Room room1;
  private Room room2;
  private Room room3;
  private Hall hall1;
  private Hall hall2;
  private Wall wall = new Wall();
  private Space space = new Space();
  private Key key = new Key(new Point(1, 1));
  private Exit exit = new Exit(new Point(16, 8));
  private Player player = new Player();
  private Zombie zombie = new Zombie();
  private Ghost ghost = new Ghost();

  private void initRoom1() {
    //Simple 4x4 room
    List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
    componentMap.add(Arrays.asList(wall, wall, wall, wall));
    componentMap.add(Arrays.asList(wall, key, player, wall));
    componentMap.add(Arrays.asList(wall, space, space, wall));
    componentMap.add(Arrays.asList(wall, ghost, wall, wall));

    this.room1 = new Room(new Point(0,0), componentMap);
  }

  private void initRoom2() {
    //Weird 2x2 room
    List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
    componentMap.add(Arrays.asList(space, wall));
    componentMap.add(Arrays.asList(wall, exit));

    this.room2 = new Room(new Point(15,7), componentMap);
  }

  private void initRoom3() {
    //3x3 room that start at a negative position
    List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
    componentMap.add(Arrays.asList(wall, wall, wall));
    componentMap.add(Arrays.asList(wall, space, wall));
    componentMap.add(Arrays.asList(wall, zombie, wall));

    this.room3 = new Room(new Point(-5,-1), componentMap);
  }

  private void initHall1() {
    //Hall goes (2,11) -> (5,11) -> (5,8)
    List<Entity> componentMap = Arrays.asList(space, space, player, space, space, space, space);
    List<Point> waypoints = new ArrayList<Point>();
    waypoints.add(new Point(5,11));

    this.hall1 = new Hall(componentMap, waypoints);

    //Doors for hall1
    hall1.connectRooms(new Point(1,11), room1, new Point(5,7), room2);
  }

  private void initHall2() {
    //Hall goes (4,3) -> (5,3) -> (5,6) -> (2,6) -> (2,10)
    List<Entity> componentMap = Arrays.asList(zombie, ghost, space, space, space, space, space,
        space, space, space, space, space);
    List<Point> waypoints = new ArrayList<Point>();
    waypoints.add(new Point(5,3));
    waypoints.add(new Point(5,6));
    waypoints.add(new Point(2,6));

    this.hall2 = new Hall(componentMap, waypoints);

    //Doors for hall2
    hall2.connectRooms(new Point(3,3), room1, new Point(2,11), room2);
  }

  @Before
  public void initEntities() {
    initRoom1();
    initRoom2();
    initRoom3();
    initHall1();
    initHall2();
  }

  @Test
  public void testGetTopLeftBound() {
    //Room at 0,0
    assertEquals(new Point(0,0), this.room1.getTopLeftBound());
    //Room at random position
    assertEquals(new Point(15,7), this.room2.getTopLeftBound());
    //Room at negative position
    assertEquals(new Point(-5,-1), this.room3.getTopLeftBound());
    //Hall where top left is not in the hall
    assertEquals(new Point(1,7), this.hall1.getTopLeftBound());
    //Hall where left side is set by a waypoint instead of the end points
    assertEquals(new Point(2,3), this.hall2.getTopLeftBound());
  }

  @Test
  public void testGetBottomRightBound() {
    //Room at 0,0
    assertEquals(new Point(3,3), this.room1.getBottomRightBound());
    //Room at random position
    assertEquals(new Point(16,8), this.room2.getBottomRightBound());
    //Room at negative position
    assertEquals(new Point(-3,1), this.room3.getBottomRightBound());
    //Hall where bottom right is a way point
    assertEquals(new Point(5,11), this.hall1.getBottomRightBound());
    //Hall where bottom right is not in the hall
    assertEquals(new Point(5,11), this.hall2.getBottomRightBound());
  }

  @Test
  public void testGetDestinationEntity() {
    assertEquals(wall, this.room1.getDestinationEntity(new Point(0,0)));
    assertEquals(space, this.room1.getDestinationEntity(new Point(1,2)));
    assertEquals(key, this.room1.getDestinationEntity(new Point(1,1)));
    assertEquals(player, this.room1.getDestinationEntity(new Point(2,1)));
    assertEquals(ghost, this.room1.getDestinationEntity(new Point(1,3)));
    assertEquals(exit, this.room2.getDestinationEntity(new Point(16,8)));
    assertEquals(zombie, this.room3.getDestinationEntity(new Point(-4,1)));

    assertEquals(space, this.hall1.getDestinationEntity(new Point(5,11)));
    assertEquals(player, this.hall1.getDestinationEntity(new Point(4,11)));
    assertEquals(space, this.hall2.getDestinationEntity(new Point(5,5)));
    assertEquals(zombie, this.hall2.getDestinationEntity(new Point(4,3)));
    assertEquals(ghost, this.hall2.getDestinationEntity(new Point(5,3)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDestinationEntityDoesNotExistRoomLow() {
    this.room1.getDestinationEntity(new Point(0,5));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDestinationEntityDoesNotExistRoomHigh() {
    this.room3.getDestinationEntity(new Point(-4,-2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDestinationEntityDoesNotExistRoomRight() {
    this.room2.getDestinationEntity(new Point(17,7));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDestinationEntityDoesNotExistRoomLeft() {
    this.room1.getDestinationEntity(new Point(-100,3));
  }

  //Destination is not within bounding box of hall
  @Test(expected = IllegalArgumentException.class)
  public void testDestinationEntityDoesNotExistHallOutBound() {
    this.hall1.getDestinationEntity(new Point(7,3));
  }

  //Destination is not within bounding box of hall but hall does not hit that point
  @Test(expected = IllegalArgumentException.class)
  public void testDestinationEntityDoesNotExistHallInBound() {
    this.hall2.getDestinationEntity(new Point(4,4));
  }

  @Test
  public void testInComponent() {
    assertEquals(true, this.room1.inComponent(new Point(0,0)));
    assertEquals(true, this.room1.inComponent(new Point(1,2)));
    assertEquals(true, this.room1.inComponent(new Point(3,0)));
    assertEquals(false, this.room1.inComponent(new Point(4,2)));
    assertEquals(false, this.room1.inComponent(new Point(-1,0)));
    assertEquals(true, this.room2.inComponent(new Point(16,7)));
    assertEquals(false, this.room2.inComponent(new Point(15,6)));
    assertEquals(false, this.room2.inComponent(new Point(16,10)));

    assertEquals(true, this.hall1.inComponent(new Point(4,11)));
    assertEquals(true, this.hall1.inComponent(new Point(5,11)));
    assertEquals(false, this.hall1.inComponent(new Point(4,10)));
    assertEquals(false, this.hall1.inComponent(new Point(4,12)));
  }

  @Test
  public void testGetEntityType() {
    assertEquals(EntityType.WALL, this.room1.getEntityType(new Wall()));
    assertEquals(EntityType.SPACE, this.room1.getEntityType(new Space()));
    assertEquals(EntityType.KEY, this.room1.getEntityType(new Key(new Point(1, 1))));
    assertEquals(EntityType.EXIT, this.room1.getEntityType(new Exit(new Point(16, 8))));
    assertEquals(EntityType.PLAYER, this.room1.getEntityType(new Player()));
    assertEquals(EntityType.GHOST, this.room1.getEntityType(new Ghost()));
    assertEquals(EntityType.ZOMBIE, this.room1.getEntityType(new Zombie()));

    assertEquals(EntityType.WALL, this.hall1.getEntityType(new Wall()));
    assertEquals(EntityType.PLAYER, this.hall1.getEntityType(new Player()));
    assertEquals(EntityType.GHOST, this.hall1.getEntityType(new Ghost()));
    assertEquals(EntityType.ZOMBIE, this.hall1.getEntityType(new Zombie()));
    //Hall space is a special type as it will be displayed differently
    assertEquals(EntityType.HALL_SPACE, this.hall1.getEntityType(new Space()));
  }

  //PlaceActor does not check which entity is being overridden as long as it is in a room
  @Test
  public void testPlaceActor() {
    Player player2 = new Player();
    assertEquals(space, this.room1.getDestinationEntity(new Point(1, 2)));
    this.room1.placeActor(player2, new Point(1,2));
    assertEquals(player2, this.room1.getDestinationEntity(new Point(1, 2)));

    assertEquals(wall, this.room1.getDestinationEntity(new Point(0, 0)));
    this.room1.placeActor(player2, new Point(1,2));
    assertEquals(player2, this.room1.getDestinationEntity(new Point(1, 2)));

    assertEquals(space, this.hall1.getDestinationEntity(new Point(5,11)));
    this.hall1.placeActor(player2, new Point(5,11));
    assertEquals(player2, this.hall1.getDestinationEntity(new Point(5,11)));
  }

  //PlaceActor outside bounds of room
  @Test(expected = IllegalArgumentException.class)
  public void testPlaceActorOutOfRoom() {
    this.room1.placeActor(player, new Point(4,3));
  }

  //PlaceActor outside bounds of hall
  @Test(expected = IllegalArgumentException.class)
  public void testPlaceActorOutOfHall() {
    this.hall1.placeActor(player, new Point(4,10));
  }

  //Check that player exists then remove and check that they are replaced with a space
  @Test
  public void testRemoveActor() {
    assertEquals(player, this.room1.getDestinationEntity(new Point(2, 1)));
    this.room1.removeActor(player);
    assertEquals(space, this.room1.getDestinationEntity(new Point(2, 1)));

    assertEquals(player, this.hall1.getDestinationEntity(new Point(4, 11)));
    this.hall1.removeActor(player);
    assertEquals(space, this.hall1.getDestinationEntity(new Point(4, 11)));
  }

  //Remove a player that is no longer in the room
  @Test(expected = IllegalArgumentException.class)
  public void testRemoveActorNoActorRoom() {
    this.room1.removeActor(player);
    this.room1.removeActor(player);
  }

  //Remove a player that is no longer in the hall
  @Test(expected = IllegalArgumentException.class)
  public void testRemoveActorNoActorHall() {
    this.hall1.removeActor(player);
    this.hall1.removeActor(player);
  }

  //We can test MoveActor if it is used

  //Tests that a key can be placed. If the keys location is a space the key should be placed.
  //If not this function should have no effect.
  @Test
  public void testPlaceKey() {
    Key newKey1 = new Key(new Point(15, 8));
    Key newKey2 = new Key(new Point(2, 2));

    assertEquals(wall, this.room2.getDestinationEntity(new Point(15, 8)));
    this.room2.placeKey(newKey1);
    assertEquals(wall, this.room2.getDestinationEntity(new Point(15, 8)));

    assertEquals(space, this.room1.getDestinationEntity(new Point(2, 2)));
    this.room1.placeKey(newKey2);
    assertEquals(newKey2, this.room1.getDestinationEntity(new Point(2, 2)));
  }

  //Tests that a exit can be placed. If the exits location is a space the exit should be placed.
  //If not this function should have no effect.
  @Test
  public void testPlaceExit() {
    Exit newExit1 = new Exit(new Point(0, 0));
    Exit newExit2 = new Exit(new Point(15, 7));



    assertEquals(wall, this.room1.getDestinationEntity(new Point(0, 0)));
    this.room1.placeExit(newExit1);
    assertEquals(wall, this.room1.getDestinationEntity(new Point(0, 0)));

    assertEquals(space, this.room2.getDestinationEntity(new Point(15, 7)));
    this.room2.placeExit(newExit2);
    assertEquals(newExit2, this.room2.getDestinationEntity(new Point(15, 7)));

  }
}