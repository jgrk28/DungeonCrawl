package model;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import view.TextualLevelViewTest;

/**
 * Tests that the methods and constructors for LevelImpl work as expected
 */
public class LevelTest {
	
  private List<LevelComponent> levelMap;
  private Entity space = new Space();
  private Entity wall = new Wall();
  private Key key;
  private Exit exit;
  private Player player1 = new Player();
  private Player player2 = new Player();
  private Player player3 = new Player();
  private Adversary ghost1 = new Ghost();
  private Adversary ghost2 = new Ghost();
  private Adversary zombie = new Zombie();

  private Room room1;
  private Room room2;
  private Room room3;
  private Room room4;

  private Hall hall1;
  private Hall hall2;
  private Hall hall3;

  //Simple 4x4 room with one space for a possible door
  private void initializeRoom1() {
    List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
    componentMap.add(Arrays.asList(wall, wall, wall, wall));
    componentMap.add(Arrays.asList(wall, space, space, wall));
    componentMap.add(Arrays.asList(wall, space, space, space));
    componentMap.add(Arrays.asList(wall, wall, wall, wall));

    room1 = new Room(new Point(0,0), componentMap);
  }

  //4x6 room with three spaces for possible doors
  private void initializeRoom2() {
    List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
    componentMap.add(Arrays.asList(wall, space, wall, wall));
    componentMap.add(Arrays.asList(wall, space, space, wall));
    componentMap.add(Arrays.asList(wall, space, space, wall));
    componentMap.add(Arrays.asList(wall, space, space, wall));
    componentMap.add(Arrays.asList(space, space, space, space));
    componentMap.add(Arrays.asList(wall, wall, wall, wall));

    room2 = new Room(new Point(5,7), componentMap);
  }

  //6x5 room with one spaces for a possible door
  private void initializeRoom3() {
    List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
    componentMap.add(Arrays.asList(wall, wall, space, wall, wall, wall));
    componentMap.add(Arrays.asList(wall, space, space, space, space, wall));
    componentMap.add(Arrays.asList(wall, space, space, space, space, wall));
    componentMap.add(Arrays.asList(wall, space, space, space, space, wall));
    componentMap.add(Arrays.asList(wall, wall, wall, wall, wall, wall));

    room3 = new Room(new Point(0,14), componentMap);
  }

  //5x6 room with one space for a possible door
  private void initializeRoom4() {
    List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
    componentMap.add(Arrays.asList(wall, wall, wall, wall, wall));
    componentMap.add(Arrays.asList(space, space, space, space, wall));
    componentMap.add(Arrays.asList(wall, space, space, space, wall));
    componentMap.add(Arrays.asList(wall, space, space, space, wall));
    componentMap.add(Arrays.asList(wall, space, space, space, wall));
    componentMap.add(Arrays.asList(wall, wall, wall, wall, wall));

    room4 = new Room(new Point(13,10), componentMap);
  }

  //Hall that can connect room1 to room2
  private void initializeHall1() {
    List<Entity> componentMap = Arrays.asList(space, space, space, space, space, space, space);
    List<Point> waypoints = new ArrayList<Point>();
    waypoints.add(new Point(6,2));

    hall1 = new Hall(componentMap, waypoints);
  }
  
  //Hall that can connect room2 to room3
  private void initializeHall2() {
    List<Entity> componentMap = Arrays.asList(space, space, space, space, space);
    List<Point> waypoints = new ArrayList<Point>();
    waypoints.add(new Point(2,11));

    hall2 = new Hall(componentMap, waypoints);
  }

  //Hall that can connect room2 to room4
  private void initializeHall3() {
    List<Entity> componentMap = Arrays.asList(space, space, space, space);
    List<Point> waypoints = new ArrayList<Point>();

    hall3 = new Hall(componentMap, waypoints);
  }

  //Doors for hall1
  private void initializeDoorsHall1() {
    room1.connectHall(new Point(3,2), hall1);
    room2.connectHall(new Point(6,7), hall1);
    hall1.connectRooms(new Point(3,2), room1, new Point(6,7), room2);
  }
  
  //Doors for hall2
  private void initializeDoorsHall2() {
    room2.connectHall(new Point(5,11), hall2);
    room3.connectHall(new Point(2,14), hall2);
    hall2.connectRooms(new Point(2,14), room3, new Point(5,11), room2);
  }

  //Doors for hall3
  private void initializeDoorsHall3() {
    room2.connectHall(new Point(8,11), hall3);
    room4.connectHall(new Point(13,11), hall3);
    hall3.connectRooms(new Point(8,11), room2, new Point(13,11), room4);
  }
  
  //Connects hall1, hall2, and hall3 to the normal 4 rooms
  private void initializeLevelMap() {
    initializeDoorsHall1();
    initializeDoorsHall2();
    initializeDoorsHall3();

    levelMap = new ArrayList<LevelComponent>();

    levelMap.add(room1);
    levelMap.add(room2);
    levelMap.add(room3);
    levelMap.add(room4);

    levelMap.add(hall1);
    levelMap.add(hall2);
    levelMap.add(hall3);
  }

  /**
   * Creates a test level by initializing the levelMap, creating
   * a map of players and their positions, creating a map of 
   * adversaries and their positions, and initializing exitUnlocked
   * and levelExited
   * @return a new Level 
   */
  private Level makeTestLevel() {
    initializeLevelMap();
    Map<Player, Point> playersPos = new HashMap<>();
    playersPos.put(this.player1, new Point(4, 2));
    playersPos.put(this.player2, new Point(7, 10));
    playersPos.put(this.player3, new Point(3, 17));
    Map<Adversary, Point> adversariesPos = new HashMap<>();
    adversariesPos.put(this.ghost1, new Point(7, 8));
    adversariesPos.put(this.zombie, new Point(2, 17));
    adversariesPos.put(this.ghost2, new Point(2, 14));
    boolean exitUnlocked = false;
    boolean levelExited = false;
    return new LevelImpl(
        playersPos,
        adversariesPos,
        this.levelMap,
        exitUnlocked,
        levelExited,
        this.key,
        this.exit
    );
  }

  //Initialize all components for use
  //They have not been added to a level but they are available for use
  @Before
  public void initLevelComponents() {
    this.key = new Key(new Point(4, 17));
    this.exit = new Exit(new Point(7, 11));
    initializeRoom1();
    initializeRoom1();
    initializeRoom2();
    initializeRoom3();
    initializeRoom4();

    initializeHall1();
    initializeHall2();
    initializeHall3();
  }

  /**
   * Creates an initial game state with a given level, players, and adversaries.
   * Players are placed in the top left-most room of the level, and adversaries 
   * are placed in the bottom right-most room of the level
   */
  @Test
  public void testLevelStartConstructor() {
    initializeLevelMap();
    List<Player> players = new ArrayList<>(Arrays.asList(this.player1, this.player2, this.player3));
    List<Adversary> adversaries = new ArrayList<>(Arrays.asList(this.ghost1, this.zombie, this.ghost2));
    Level level = new LevelImpl(players, adversaries, this.levelMap, this.key, this.exit);

    String expectedOut = ""
        + "XXXX              \n"
        + "XPPX              \n"
        + "XP..***           \n"
        + "XXXX  *           \n"
        + "      *           \n"
        + "      *           \n"
        + "      *           \n"
        + "     X.XX         \n"
        + "     X..X         \n"
        + "     X..X         \n"
        + "     X..X    XXXXX\n"
        + "  ***..@.****GZG.X\n"
        + "  *  XXXX    X...X\n"
        + "  *          X...X\n"
        + "XX.XXX       X...X\n"
        + "X....X       XXXXX\n"
        + "X....X            \n"
        + "X...!X            \n"
        + "XXXXXX            \n";

    TextualLevelViewTest.testDrawLevel(level, expectedOut);
  }

  //Creates an intermediate game state with given player and adversary locations 
  @Test
  public void testLevelMidConstructor() {
    Level level = makeTestLevel();

    String expectedOut = ""
        + "XXXX              \n"
        + "X..X              \n"
        + "X...P**           \n"
        + "XXXX  *           \n"
        + "      *           \n"
        + "      *           \n"
        + "      *           \n"
        + "     X.XX         \n"
        + "     X.GX         \n"
        + "     X..X         \n"
        + "     X.PX    XXXXX\n"
        + "  ***..@.****....X\n"
        + "  *  XXXX    X...X\n"
        + "  *          X...X\n"
        + "XXGXXX       X...X\n"
        + "X....X       XXXXX\n"
        + "X....X            \n"
        + "X.ZP!X            \n"
        + "XXXXXX            \n";

    TextualLevelViewTest.testDrawLevel(level, expectedOut);
  }
  
  //Test for placeActors
  @Test
  public void testPlaceActors() {
	  initializeLevelMap();
	  List<Player> players = new ArrayList<>(Arrays.asList(this.player1, this.player2, this.player3));
	  List<Adversary> adversaries = new ArrayList<>(Arrays.asList(this.ghost1, this.zombie, this.ghost2));
	  Level level = new LevelImpl(this.levelMap, this.key, this.exit);
	  level.placeActors(players, adversaries);

	    String expectedOut = ""
	        + "XXXX              \n"
	        + "XPPX              \n"
	        + "XP..***           \n"
	        + "XXXX  *           \n"
	        + "      *           \n"
	        + "      *           \n"
	        + "      *           \n"
	        + "     X.XX         \n"
	        + "     X..X         \n"
	        + "     X..X         \n"
	        + "     X..X    XXXXX\n"
	        + "  ***..@.****GZG.X\n"
	        + "  *  XXXX    X...X\n"
	        + "  *          X...X\n"
	        + "XX.XXX       X...X\n"
	        + "X....X       XXXXX\n"
	        + "X....X            \n"
	        + "X...!X            \n"
	        + "XXXXXX            \n";

	    TextualLevelViewTest.testDrawLevel(level, expectedOut);	  
  }
  
  //Tests for playerAction

  //Tests that moving a player is updated in the level
  @Test
  public void testPlayerActionNormalMove() {
    Level level = makeTestLevel();
    level.playerAction(this.player1, new Point(5, 2));
    level.playerAction(this.player2, new Point(6, 9));
    level.playerAction(this.player3, new Point(3, 15));

    String expectedOut = ""
        + "XXXX              \n"
        + "X..X              \n"
        + "X...*P*           \n"
        + "XXXX  *           \n"
        + "      *           \n"
        + "      *           \n"
        + "      *           \n"
        + "     X.XX         \n"
        + "     X.GX         \n"
        + "     XP.X         \n"
        + "     X..X    XXXXX\n"
        + "  ***..@.****....X\n"
        + "  *  XXXX    X...X\n"
        + "  *          X...X\n"
        + "XXGXXX       X...X\n"
        + "X..P.X       XXXXX\n"
        + "X....X            \n"
        + "X.Z.!X            \n"
        + "XXXXXX            \n";

    TextualLevelViewTest.testDrawLevel(level, expectedOut);
  }

  //Tests that a player can correctly move between LevelComponents 
  //during an action
  @Test
  public void testPlayerActionChangeComponent() {
    Level level = makeTestLevel();
    level.playerAction(this.player1, new Point(2, 2));
    level.playerAction(this.player2, new Point(8, 11));
    level.playerAction(this.player2, new Point(9, 11));
    level.playerAction(this.player3, new Point(3, 15));
    level.playerAction(this.player3, new Point(2, 15));
    level.playerAction(this.player3, new Point(2, 13));

    String expectedOut = ""
        + "XXXX              \n"
        + "X..X              \n"
        + "X.P.***           \n"
        + "XXXX  *           \n"
        + "      *           \n"
        + "      *           \n"
        + "      *           \n"
        + "     X.XX         \n"
        + "     X.GX         \n"
        + "     X..X         \n"
        + "     X..X    XXXXX\n"
        + "  ***..@.P***....X\n"
        + "  *  XXXX    X...X\n"
        + "  P          X...X\n"
        + "XXGXXX       X...X\n"
        + "X....X       XXXXX\n"
        + "X....X            \n"
        + "X.Z.!X            \n"
        + "XXXXXX            \n";

    TextualLevelViewTest.testDrawLevel(level, expectedOut);
  }

  /**
   * Tests that a player can eliminate themselves by moving into an adversary
   * and that eliminating all players ends the level with the LOST GameState
   */
  @Test
  public void testPlayerActionSelfElim() {
    Level level = makeTestLevel();
    level.playerAction(this.player2, new Point(7, 8));
    level.playerAction(this.player3, new Point(2, 17));

    String expectedOut = ""
        + "XXXX              \n"
        + "X..X              \n"
        + "X...P**           \n"
        + "XXXX  *           \n"
        + "      *           \n"
        + "      *           \n"
        + "      *           \n"
        + "     X.XX         \n"
        + "     X.GX         \n"
        + "     X..X         \n"
        + "     X..X    XXXXX\n"
        + "  ***..@.****....X\n"
        + "  *  XXXX    X...X\n"
        + "  *          X...X\n"
        + "XXGXXX       X...X\n"
        + "X....X       XXXXX\n"
        + "X....X            \n"
        + "X.Z.!X            \n"
        + "XXXXXX            \n";

    TextualLevelViewTest.testDrawLevel(level, expectedOut);

    //Eliminate last player so the game ends
    level.playerAction(this.player1, new Point(6, 2));
    level.playerAction(this.player1, new Point(6, 4));
    level.playerAction(this.player1, new Point(6, 6));
    level.playerAction(this.player1, new Point(6, 8));
    level.playerAction(this.player1, new Point(7, 8));
    assertEquals(GameState.LOST, level.isLevelOver());
  }

  /**
   * Tests that players can find the key, exit the level,
   * and that the GameState is WON once all players have
   * been removed from the level
   */
  @Test
  public void testPlayerActionGetKeyAndExit() {
    Level level = makeTestLevel();
    level.playerAction(this.player3, new Point(4, 17));
    level.playerAction(this.player2, new Point(7, 11));

    String expectedOut = ""
        + "XXXX              \n"
        + "X..X              \n"
        + "X...P**           \n"
        + "XXXX  *           \n"
        + "      *           \n"
        + "      *           \n"
        + "      *           \n"
        + "     X.XX         \n"
        + "     X.GX         \n"
        + "     X..X         \n"
        + "     X..X    XXXXX\n"
        + "  ***..@.****....X\n"
        + "  *  XXXX    X...X\n"
        + "  *          X...X\n"
        + "XXGXXX       X...X\n"
        + "X....X       XXXXX\n"
        + "X....X            \n"
        + "X.Z.PX            \n"
        + "XXXXXX            \n";

    TextualLevelViewTest.testDrawLevel(level, expectedOut);

    //Eliminate both the players so the game ends
    level.playerAction(this.player3, new Point(2, 17));
    level.playerAction(this.player1, new Point(6, 2));
    level.playerAction(this.player1, new Point(6, 4));
    level.playerAction(this.player1, new Point(6, 6));
    level.playerAction(this.player1, new Point(6, 8));
    level.playerAction(this.player1, new Point(7, 8));
    assertEquals(GameState.WON, level.isLevelOver());
  }

  @Test
  public void testPlayerActionOntoLockedExit() {
    Level level = makeTestLevel();
    level.playerAction(this.player2, new Point(7, 11));
    level.playerAction(this.player2, new Point(8, 11));

    String expectedOut = ""
        + "XXXX              \n"
        + "X..X              \n"
        + "X...P**           \n"
        + "XXXX  *           \n"
        + "      *           \n"
        + "      *           \n"
        + "      *           \n"
        + "     X.XX         \n"
        + "     X.GX         \n"
        + "     X..X         \n"
        + "     X..X    XXXXX\n"
        + "  ***..@P****....X\n"
        + "  *  XXXX    X...X\n"
        + "  *          X...X\n"
        + "XXGXXX       X...X\n"
        + "X....X       XXXXX\n"
        + "X....X            \n"
        + "X.ZP!X            \n"
        + "XXXXXX            \n";

    TextualLevelViewTest.testDrawLevel(level, expectedOut);
  }

  //Tests for invalid player movements
  @Test
  public void testPlayerActionBadTooLong() {
    Level level = makeTestLevel();
    level.playerAction(this.player3, new Point(4, 16));
    assertFalse(level.checkValidMove(this.player3, new Point(1, 16)));
  }

  @Test
  public void testPlayerActionBadTooLongDiag() {
    Level level = makeTestLevel();
    assertFalse(level.checkValidMove(this.player1, new Point(6, 3)));
  }

  @Test
  public void testPlayerActionBadOutOfBounds() {
    Level level = makeTestLevel();
    assertFalse(level.checkValidMove(this.player2, new Point(9, 10)));
  }

  @Test
  public void testPlayerActionBadOutOfBoundsHall() {
    Level level = makeTestLevel();
    level.playerAction(this.player1, new Point(6, 2));
    assertFalse(level.checkValidMove(this.player1, new Point(7, 2)));
  }

  @Test
  public void testPlayerActionBadWall() {
    Level level = makeTestLevel();
    assertFalse(level.checkValidMove(this.player2, new Point(8, 10)));
  }
  
  @Test
  public void testPlayerActionBadWallDiag() {
    Level level = makeTestLevel();
    assertFalse(level.checkValidMove(this.player2, new Point(8, 9)));
  }

  @Test
  public void testPlayerActionBadCollision() {
    Level level = makeTestLevel();
    level.playerAction(this.player2, new Point(6, 9));
    level.playerAction(this.player2, new Point(6, 7));
    level.playerAction(this.player2, new Point(6, 5));
    level.playerAction(this.player2, new Point(6, 3));
    level.playerAction(this.player2, new Point(5, 2));
    assertFalse(level.checkValidMove(this.player2, new Point(4, 2)));
  }

  @Test
  public void testPlayerActionBadIntoHall() {
    Level level = makeTestLevel();
    level.playerAction(this.player2, new Point(8, 11));
    assertFalse(level.checkValidMove(this.player2, new Point(9, 12)));
  }

  @Test
  public void testPlayerActionBadIntoRoomWall() {
    Level level = makeTestLevel();
    assertFalse(level.checkValidMove(this.player1, new Point(3, 1)));
  }

  @Test
  public void testPlayerActionBadIntoRoomExit() {
    Level level = makeTestLevel();
    level.checkValidMove(this.player2, new Point(8, 11));
    level.checkValidMove(this.player2, new Point(9, 11));
    level.checkValidMove(this.player2, new Point(7, 11));
  }
  
  //Tests for valid player movements	  
  @Test
  public void testPlayerActionTwoSpacesUp() {
	  Level level = makeTestLevel();
	  assertTrue(level.checkValidMove(this.player3, new Point(3, 15)));
  }
  
  @Test
  public void testPlayerActionJumpOverAdversary() {
	  Level level = makeTestLevel();
	  assertTrue(level.checkValidMove(this.player3, new Point(1, 17)));
  }
  
  @Test
  public void testPlayerActionDiagonal() {
	  Level level = makeTestLevel();
	  assertTrue(level.checkValidMove(this.player3, new Point(2, 16)));
  }
    
  @Test
  public void testPlayerMoveThroughDoor() {
	  Level level = makeTestLevel();
	  assertTrue(level.checkValidMove(this.player1, new Point(2, 2)));
  }
  
  @Test
  public void testPlayerMoveToAdversary() {
	  Level level = makeTestLevel();
	  assertTrue(level.checkValidMove(this.player2, new Point(7, 8)));
  }
  
  @Test
  public void testPlayerMoveToExit() {
	  Level level = makeTestLevel();
	  assertTrue(level.checkValidMove(this.player2, new Point(7, 11)));
  }
  
  @Test
  public void testPlayerMoveToKey() {
	  Level level = makeTestLevel();
	  assertTrue(level.checkValidMove(this.player3, new Point(4, 17)));
  }
  
  //Tests for adversaryAction

  //Tests that an adversary can move and travel between LevelComponents
  @Test
  public void testAdversaryActionNormalAndChangeComponent() {
    Level level = makeTestLevel();
    level.adversaryAction(this.ghost1, new Point(6, 8));
    level.adversaryAction(this.zombie, new Point(2, 16));
    //Move ghost through entire hallway
    level.adversaryAction(this.ghost2, new Point(2, 13));
    level.adversaryAction(this.ghost2, new Point(2, 12));
    level.adversaryAction(this.ghost2, new Point(2, 11));
    level.adversaryAction(this.ghost2, new Point(3, 11));
    level.adversaryAction(this.ghost2, new Point(4, 11));
    level.adversaryAction(this.ghost2, new Point(5, 11));

    String expectedOut = ""
        + "XXXX              \n"
        + "X..X              \n"
        + "X...P**           \n"
        + "XXXX  *           \n"
        + "      *           \n"
        + "      *           \n"
        + "      *           \n"
        + "     X.XX         \n"
        + "     XG.X         \n"
        + "     X..X         \n"
        + "     X.PX    XXXXX\n"
        + "  ***G.@.****....X\n"
        + "  *  XXXX    X...X\n"
        + "  *          X...X\n"
        + "XX.XXX       X...X\n"
        + "X....X       XXXXX\n"
        + "X.Z..X            \n"
        + "X..P!X            \n"
        + "XXXXXX            \n";

    TextualLevelViewTest.testDrawLevel(level, expectedOut);
  }

  /**
   * Tests that adversaries can remove players from the level
   * during an interaction, and that the GameState is LOST 
   * once all players have been removed
   */
  @Test
  public void testAdversaryActionRemovePlayer() {
    Level level = makeTestLevel();
    level.adversaryAction(this.ghost1, new Point(7, 9));
    level.adversaryAction(this.ghost1, new Point(7, 10));
    level.adversaryAction(this.zombie, new Point(3, 17));

    String expectedOut = ""
        + "XXXX              \n"
        + "X..X              \n"
        + "X...P**           \n"
        + "XXXX  *           \n"
        + "      *           \n"
        + "      *           \n"
        + "      *           \n"
        + "     X.XX         \n"
        + "     X..X         \n"
        + "     X..X         \n"
        + "     X.GX    XXXXX\n"
        + "  ***..@.****....X\n"
        + "  *  XXXX    X...X\n"
        + "  *          X...X\n"
        + "XXGXXX       X...X\n"
        + "X....X       XXXXX\n"
        + "X....X            \n"
        + "X..Z!X            \n"
        + "XXXXXX            \n";

    TextualLevelViewTest.testDrawLevel(level, expectedOut);

    //Remove last player
    level.adversaryAction(this.ghost1, new Point(6, 10));
    level.adversaryAction(this.ghost1, new Point(6, 9));
    level.adversaryAction(this.ghost1, new Point(6, 8));
    level.adversaryAction(this.ghost1, new Point(6, 7));
    level.adversaryAction(this.ghost1, new Point(6, 6));
    level.adversaryAction(this.ghost1, new Point(6, 5));
    level.adversaryAction(this.ghost1, new Point(6, 4));
    level.adversaryAction(this.ghost1, new Point(6, 3));
    level.adversaryAction(this.ghost1, new Point(6, 2));
    level.adversaryAction(this.ghost1, new Point(5, 2));
    level.adversaryAction(this.ghost1, new Point(4, 2));
    assertEquals(GameState.LOST, level.isLevelOver());
  }

  //Tests for invalid adversary movements
  @Test
  public void testAdversaryActionBadTooLong() {
    Level level = makeTestLevel();
    assertFalse(level.checkValidMove(this.ghost1, new Point(7, 10)));
  }

  @Test
  public void testAdversaryActionBadTooLongDiag() {
    Level level = makeTestLevel();
    assertFalse(level.checkValidMove(this.ghost1, new Point(6, 9)));
  }

  @Test
  public void testAdversaryActionBadOutOfBounds() {
    Level level = makeTestLevel();
    level.adversaryAction(this.ghost2, new Point(2, 13));
    assertFalse(level.checkValidMove(this.ghost2, new Point(3, 13)));
  }

  @Test
  public void testAdversaryActionBadWall() {
    Level level = makeTestLevel();
    assertFalse(level.checkValidMove(this.zombie, new Point(2, 18)));
  }

  @Test
  public void testAdversaryActionBadExit() {
    Level level = makeTestLevel();
    level.adversaryAction(this.ghost1, new Point(7, 9));
    level.adversaryAction(this.ghost1, new Point(7, 10));
    assertFalse(level.checkValidMove(this.ghost1, new Point(7, 11)));
  }

  @Test
  public void testAdversaryActionBadCollision() {
    Level level = makeTestLevel();
    level.adversaryAction(this.ghost2, new Point(2, 15));
    level.adversaryAction(this.ghost2, new Point(2, 16));
    assertFalse(level.checkValidMove(this.ghost2, new Point(2, 17)));
  }

  @Test
  public void testAdversaryActionBadIntoHall() {
    Level level = makeTestLevel();
    level.adversaryAction(this.ghost2, new Point(2, 13));
    level.adversaryAction(this.zombie, new Point(2, 16));
    level.adversaryAction(this.zombie, new Point(2, 15));
    level.adversaryAction(this.zombie, new Point(2, 14));
    assertFalse(level.checkValidMove(this.zombie, new Point(2, 13)));
  }

  @Test
  public void testPlayerActionBadIntoRoom() {
    Level level = makeTestLevel();
    level.adversaryAction(this.ghost2, new Point(2, 13));
    level.adversaryAction(this.zombie, new Point(2, 16));
    level.adversaryAction(this.zombie, new Point(2, 15));
    level.adversaryAction(this.zombie, new Point(2, 14));
    assertFalse(level.checkValidMove(this.ghost2, new Point(2, 14)));
  }
  
  //Tests for valid adversary movements	 
  @Test
  public void testAdversaryActionMoveUp() {
	  Level level = makeTestLevel();
	  assertTrue(level.checkValidMove(this.ghost2, new Point(2, 13)));
  }

  @Test
  public void testAdversaryActionMoveLeft() {
	  Level level = makeTestLevel();
	  assertTrue(level.checkValidMove(this.ghost1, new Point(6, 8)));
  }

  @Test
  public void testAdversaryActionMoveRight() {
	  Level level = makeTestLevel();
	  assertTrue(level.checkValidMove(this.zombie, new Point(3, 17)));
  }
  
  @Test
  public void testAdversaryActionMoveDown() {
	  Level level = makeTestLevel();
	  assertTrue(level.checkValidMove(this.ghost1, new Point(7, 9)));
  }
  
  //Test for isLevelOver

  /**
   * Tests that the level has been won when there are no players in the level,
   * the exit is unlocked, and the level has been exited
   */
  @Test
  public void testIsLevelOverWon() {
    initializeLevelMap();
    Map<Player, Point> playersPos = new HashMap<>();
    Map<Adversary, Point> adversariesPos = new HashMap<>();
    adversariesPos.put(this.ghost1, new Point(7, 8));
    boolean exitUnlocked = true;
    boolean levelExited = true;
    Level level = new LevelImpl(
        playersPos,
        adversariesPos,
        this.levelMap,
        exitUnlocked,
        levelExited,
        this.key,
        this.exit
    );

    assertEquals(GameState.WON, level.isLevelOver());
  }

  /**
   * Tests that the level has been lost when there are no players in the level,
   * the exit is unlocked, and the level has not been exited
   */
  @Test
  public void testIsLevelOverLost() {
    initializeLevelMap();
    Map<Player, Point> playersPos = new HashMap<>();
    Map<Adversary, Point> adversariesPos = new HashMap<>();
    adversariesPos.put(this.ghost1, new Point(7, 8));
    boolean exitUnlocked = true;
    boolean levelExited = false;
    Level level = new LevelImpl(
        playersPos,
        adversariesPos,
        this.levelMap,
        exitUnlocked,
        levelExited,
        this.key,
        this.exit
    );
    assertEquals(GameState.LOST, level.isLevelOver());
  }

  /**
   * Tests that the level is active when there is at least one player in the level,
   * the exit is unlocked, and the level has been exited
   */
  @Test
  public void testIsLevelOverActive() {
    initializeLevelMap();
    Map<Player, Point> playersPos = new HashMap<>();
    playersPos.put(this.player1, new Point(5, 2));
    Map<Adversary, Point> adversariesPos = new HashMap<>();
    adversariesPos.put(this.ghost1, new Point(7, 8));
    boolean exitUnlocked = true;
    boolean levelExited = true;
    Level level = new LevelImpl(
        playersPos,
        adversariesPos,
        this.levelMap,
        exitUnlocked,
        levelExited,
        this.key,
        this.exit
    );

    assertEquals(GameState.ACTIVE, level.isLevelOver());
  }
  
  //Tests for findComponent
  
  @Test (expected = IllegalArgumentException.class)
  public void testFindComponentNoComponent() {
	  Level level = makeTestLevel();
	  level.findComponent(new Point(11, 0));  
  }
  
  @Test
  public void testFindComponentKey() {
	  Level level = makeTestLevel();
	  assertEquals(this.room3,level.findComponent(new Point(4, 17)));  
  }
  
  @Test
  public void testFindComponentExit() {
	  Level level = makeTestLevel();
	  assertEquals(this.room2,level.findComponent(new Point(7, 11)));  
  }
  
  @Test
  public void testFindComponentPlayer() {
	  Level level = makeTestLevel();
	  assertEquals(this.hall1,level.findComponent(new Point(4, 2)));  
  }
  
  @Test
  public void testFindComponentAdversary() {
	  Level level = makeTestLevel();
	  assertEquals(this.room3,level.findComponent(new Point(7, 8)));  
  }
  
  @Test
  public void testFindComponentRoom() {
	  Level level = makeTestLevel();
	  assertEquals(this.room4,level.findComponent(new Point(15, 15)));  
  }
  
  @Test
  public void testFindComponentHall() {
	  Level level = makeTestLevel();
	  assertEquals(this.hall2,level.findComponent(new Point(11, 11)));  
  }
  
  //Tests for checkValidLevelState
  @Test
	public void testCheckValidLevelStateNormal() {
		Level level = makeTestLevel();
	    List<Player> players = new ArrayList<>(Arrays.asList(this.player1, this.player2, this.player3));
	    List<Adversary> adversaries = new ArrayList<>(Arrays.asList(this.ghost1, this.zombie, this.ghost2));
		assertTrue(level.checkValidLevelState(players, adversaries));
	}

	@Test
	public void testCheckValidLeveltateNoKey() {
		//Add null key
		LevelMap map = new LevelMap();
		Level level = new LevelImpl(map.initializeLevelMap(), null, this.exit);
		List<Player> players = new ArrayList<>(Arrays.asList(this.player1, this.player2, this.player3));
		List<Adversary> adversaries = new ArrayList<>(Arrays.asList(this.ghost1, this.zombie, this.ghost2));
		assertFalse(level.checkValidLevelState(players, adversaries));
	}

	@Test
	public void testCheckValidLevelStateNoExit() {
		//Add no exit
		LevelMap map = new LevelMap();
		Level level = new LevelImpl(map.initializeLevelMap(), this.key, null);
		List<Player> players = new ArrayList<>(Arrays.asList(this.player1, this.player2, this.player3));
		List<Adversary> adversaries = new ArrayList<>(Arrays.asList(this.ghost1, this.zombie, this.ghost2));
		assertFalse(level.checkValidLevelState(players, adversaries));
	}

	@Test
	public void testCheckValidLevelStateBadExit() {
		//Add no exit
		LevelMap map = new LevelMap();
		Level level = new LevelImpl(
				new HashMap<Player, Point>(),
				new HashMap<Adversary, Point>(),
				map.initializeLevelMap(),
				false,
				true,
				this.key,
				this.exit
		);
		List<Player> players = new ArrayList<>(Arrays.asList(this.player1, this.player2, this.player3));
		List<Adversary> adversaries = new ArrayList<>(Arrays.asList(this.ghost1, this.zombie, this.ghost2));
		assertFalse(level.checkValidLevelState(players, adversaries));
	}

	@Test
	public void testCheckValidLevelStateBadPlayer() {
		Level level = makeTestLevel();
		List<Player> players = new ArrayList<Player>(Arrays.asList(new Player(), this.player2, this.player3));
		List<Adversary> adversaries = new ArrayList<>(Arrays.asList(this.ghost1, this.zombie, this.ghost2));
		assertFalse(level.checkValidLevelState(players, adversaries));
	}

	@Test
	public void testCheckValidLevelStateBadAdversary() {
		Level level = makeTestLevel();
		List<Player> players = new ArrayList<>(Arrays.asList(this.player1, this.player2, this.player3));
		List<Adversary> adversaries = new ArrayList<Adversary>(Arrays.asList(new Ghost(), this.zombie, this.ghost2));
		assertFalse(level.checkValidLevelState(players, adversaries));
	}
 
  //Tests for isPlayerAlive
	
	@Test
	public void testIsPlayerAliveTrue() {
		LevelMap map = new LevelMap();
		List<Player> players = new ArrayList<>(Arrays.asList(this.player1, this.player2, this.player3));
		List<Adversary> adversaries = new ArrayList<>(Arrays.asList(this.ghost1, this.zombie, this.ghost2));
		Level level = new LevelImpl(players, adversaries, map.initializeLevelMap(),this.key, this.exit);
		assertTrue(level.isPlayerAlive(this.player1));
	}
	
	@Test
	public void testIsPlayerAliveFalsePlayer() {
		LevelMap map = new LevelMap();
		List<Player> players = new ArrayList<>(Arrays.asList(this.player1, this.player2, this.player3));
		List<Adversary> adversaries = new ArrayList<>(Arrays.asList(this.ghost1, this.zombie, this.ghost2));
		Level level = new LevelImpl(players, adversaries, map.initializeLevelMap(),this.key, this.exit);
		assertFalse(level.isPlayerAlive(new Player()));
	}  
  
  //Tests for getPlayerMap
  //TODO complete implementation for getPlayerMap
   
}
