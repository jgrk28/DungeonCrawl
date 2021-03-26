package Game.model;

import static org.junit.Assert.*;

import java.awt.Point;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import Game.modelView.EntityType;
import Game.modelView.LevelModelView;
import org.junit.Before;
import org.junit.Test;
import Game.view.TextualLevelView;
import Game.view.View;

//Tests for LevelComponents
public class LevelComponentTest {
	
  //Helper function to create a level add one component to it and then test
  //whether the level looks as expected.
  //This is currently used in both the HallTest and RoomTest classes.
  public static void checkLevelComponentLooksLike(LevelComponent component, String expectedOut) {
    //Create a new ModelView containing just the room that was passed in
    ArrayList<LevelComponent> levelMap = new ArrayList<LevelComponent>();
    levelMap.add(component);
    LevelModelView modelView = new LevelImpl(levelMap, new ArrayList<>());

    //Make print stream to go into view
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream print = new PrintStream(output);

    View view = new TextualLevelView(modelView, print);

    //Display level to STDOUT
    view.draw();

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
  private Player player1;
  private Player player2;
  private Zombie zombie1;
  private Zombie zombie2;
  private Ghost ghost1;
  private Ghost ghost2;

  @Before
  public void initEntities() {
    ModelCreator creator = new ModelCreator();
    this.room1 = creator.initializeRoom5();
    this.room2 = creator.initializeRoom6();
    this.room3 = creator.initializeRoom7();
    this.hall1 = creator.initializeHall4(creator.initializeRoom8(), creator.initializeRoom9());
    this.hall2 = creator.initializeHall5(creator.initializeRoom10(), creator.initializeRoom11());
    this.player1 = creator.getPlayer1();
    this.player2 = creator.getPlayer2();
    this.ghost1 = creator.getGhost1();
    this.ghost2 = creator.getGhost2();
    this.zombie1 = creator.getZombie1();
    this.zombie2 = creator.getZombie2();
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
  public void testGetDestinationTile() {
    assertEquals(wall, this.room1.getDestinationTile(new Point(0,0)));
    assertEquals(space, this.room1.getDestinationTile(new Point(1,2)));
  
    Tile keyTile = this.room1.getDestinationTile(new Point(1,1));
    assertEquals(key, keyTile.getItem());
    
    Tile playerTile = this.room1.getDestinationTile(new Point(2,1));
    assertEquals(player1, playerTile.getActor());
    
    Tile ghostTile = this.room1.getDestinationTile(new Point(1,3));
    assertEquals(ghost1, ghostTile.getActor());
    
    Tile exitTile = this.room2.getDestinationTile(new Point(16,8));
    assertEquals(exit, exitTile.getItem());
    
    Tile zombieTile = this.room3.getDestinationTile(new Point(-4,1));
    assertEquals(zombie1, zombieTile.getActor());

    assertEquals(space, this.hall1.getDestinationTile(new Point(5,11)));
    assertEquals(space, this.hall2.getDestinationTile(new Point(5,5)));
   
    Tile hallPlayerTile = this.hall1.getDestinationTile(new Point(4,11));
    assertEquals(player2, hallPlayerTile.getActor());
    
    Tile hallZombieTile = this.hall2.getDestinationTile(new Point(4,3));
    assertEquals(zombie2, hallZombieTile.getActor());
    
    Tile hallGhostTile = this.hall2.getDestinationTile(new Point(5,3));
    assertEquals(ghost2, hallGhostTile.getActor());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDestinationEntityDoesNotExistRoomLow() {
    this.room1.getDestinationTile(new Point(0,5));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDestinationEntityDoesNotExistRoomHigh() {
    this.room3.getDestinationTile(new Point(-4,-2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDestinationEntityDoesNotExistRoomRight() {
    this.room2.getDestinationTile(new Point(17,7));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDestinationEntityDoesNotExistRoomLeft() {
    this.room1.getDestinationTile(new Point(-100,3));
  }

  //Destination is not within bounding box of hall
  @Test(expected = IllegalArgumentException.class)
  public void testDestinationEntityDoesNotExistHallOutBound() {
    this.hall1.getDestinationTile(new Point(7,3));
  }

  //Destination is not within bounding box of hall but hall does not hit that point
  @Test(expected = IllegalArgumentException.class)
  public void testDestinationEntityDoesNotExistHallInBound() {
    this.hall2.getDestinationTile(new Point(4,4));
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
    
    Tile keyTile = new Space(this.key, null);
    assertEquals(EntityType.KEY, this.room1.getEntityType(keyTile));
    
    Tile exitTile = new Space(this.exit, null);
    assertEquals(EntityType.EXIT, this.room1.getEntityType(exitTile));
    
    Tile playerTile = new Space(null, this.player1);
    assertEquals(EntityType.PLAYER, this.room1.getEntityType(playerTile));
    
    Tile ghostTile = new Space(null, this.ghost1);
    assertEquals(EntityType.GHOST, this.room1.getEntityType(ghostTile));
    
    Tile zombieTile = new Space(null, this.zombie2);
    assertEquals(EntityType.ZOMBIE, this.room1.getEntityType(zombieTile));

    //Hall space is a special type as it will be displayed differently
    assertEquals(EntityType.HALL_SPACE, this.hall1.getEntityType(new Space()));
    assertEquals(EntityType.WALL, this.hall1.getEntityType(new Wall()));
    
    assertEquals(EntityType.PLAYER, this.hall1.getEntityType(playerTile));
    assertEquals(EntityType.GHOST, this.hall1.getEntityType(ghostTile));
    assertEquals(EntityType.ZOMBIE, this.hall1.getEntityType(zombieTile));

  }

  //PlaceActor does not check which entity is being overridden as long as it is in a room
  @Test
  public void testPlaceActor() {
	  
    Player player2 = new Player();
    
    Tile room1SpaceTile = this.room1.getDestinationTile(new Point(1, 2));
    assertEquals(null, room1SpaceTile.getActor()); 
    this.room1.placeActor(player2, new Point(1,2));
    assertEquals(player2, room1SpaceTile.getActor());

    Tile hall1SpaceTile = this.hall1.getDestinationTile(new Point(5,11));
    assertEquals(null, hall1SpaceTile.getActor());   
    this.hall1.placeActor(player2, new Point(5,11));
    assertEquals(player2, hall1SpaceTile.getActor());
  }

  //PlaceActor outside bounds of room
  @Test(expected = IllegalArgumentException.class)
  public void testPlaceActorOutOfRoom() {
    this.room1.placeActor(player2, new Point(4,3));
  }

  //PlaceActor outside bounds of hall
  @Test(expected = IllegalArgumentException.class)
  public void testPlaceActorOutOfHall() {
    this.hall1.placeActor(player1, new Point(4,10));
  }

  //Check that player exists then remove and check that they are replaced with a space
  @Test
  public void testRemoveActor() {
	  Tile room1Tile = this.room1.getDestinationTile(new Point(2, 1));
    assertEquals(player1, room1Tile.getActor());
    this.room1.removeActor(player1);
    room1Tile = this.room1.getDestinationTile(new Point(2, 1));
    assertEquals(null, room1Tile.getActor());

    Tile hall1Tile = this.hall1.getDestinationTile(new Point(4, 11));
    assertEquals(player2, hall1Tile.getActor());
    this.hall1.removeActor(player2);
    hall1Tile = this.hall1.getDestinationTile(new Point(4, 11));
    assertEquals(null, hall1Tile.getActor());
  }

  //Remove a player that is no longer in the room
  @Test(expected = IllegalArgumentException.class)
  public void testRemoveActorNoActorRoom() {
    this.room1.removeActor(player1);
    this.room1.removeActor(player1);
  }

  //Remove a player that is no longer in the hall
  @Test(expected = IllegalArgumentException.class)
  public void testRemoveActorNoActorHall() {
    this.hall1.removeActor(player2);
    this.hall1.removeActor(player2);
  }

  //We can test MoveActor if it is used

  //Tests that a key can be placed. If the keys location is a space the key should be placed.
  @Test
  public void testPlaceKey() {
    Key newKey = new Key(new Point(2, 2));
    Tile room1Tile = this.room1.getDestinationTile(new Point(2, 2));
    assertEquals(null, room1Tile.getItem());
    this.room1.placeItem(newKey);
    assertEquals(newKey, room1Tile.getItem());
  }
  
  //If the location that the key is being placed is a wall, an exception
  //will be thrown
  @Test (expected = IllegalArgumentException.class)
  public void testPlaceKeyException() {
    Key newKey = new Key(new Point(15, 8));
    Tile room2Tile = this.room2.getDestinationTile(new Point(15, 8));
    assertEquals(wall, room2Tile);
    this.room2.placeItem(newKey);
  }

  //Tests that a exit can be placed. If the exits location is a space the exit should be placed.
  //If not this function should have no effect.
  @Test
  public void testPlaceExit() {
    Exit newExit = new Exit(new Point(15, 7));
    Tile room2Tile = this.room2.getDestinationTile(new Point(15, 7));
    assertEquals(null, room2Tile.getItem());
    this.room2.placeItem(newExit);
    assertEquals(newExit, room2Tile.getItem());

  }
  
  //If the location that the exit is being placed is a wall, an exception
  //will be thrown
  @Test (expected = IllegalArgumentException.class)
  public void testPlaceExitException() {
	Exit newExit = new Exit(new Point(0, 0));
	Tile room1Tile = this.room1.getDestinationTile(new Point(0, 0));
	assertEquals(wall, room1Tile);
	this.room1.placeItem(newExit);
  }

  @Test
  public void testFindEntityLocationSimple() {
    assertEquals(new Point(2, 1), this.room1.findActorLocation(this.player1));
    assertEquals(new Point(1, 3), this.room1.findActorLocation(this.ghost1));
    assertEquals(new Point(4, 11), this.hall1.findActorLocation(this.player2));
    assertEquals(new Point(4, 3), this.hall2.findActorLocation(this.zombie2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindEntityLocationNotInComponent1() {
    this.room1.findActorLocation(this.zombie2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindEntityLocationNotInComponent2() {
    this.room2.findActorLocation(this.player1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindEntityLocationNotInComponent3() {
    this.hall1.findActorLocation(this.ghost1);
  }
}