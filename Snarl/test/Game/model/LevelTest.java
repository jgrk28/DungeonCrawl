package Game.model;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import Game.modelView.EntityType;
import Game.view.TextualLevelViewTest;

/**
 * Tests that the methods and constructors for LevelImpl work as expected
 */
public class LevelTest {

  private ModelCreator creator;
  private Level level;
  private List<LevelComponent> levelMap;
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
  
  //Entity types
  private EntityType w = EntityType.WALL;
  private EntityType s = EntityType.SPACE;
  private EntityType h = EntityType.HALL_SPACE;
  private EntityType e = EntityType.EMPTY;
  private EntityType k = EntityType.KEY;
  private EntityType x = EntityType.EXIT;
  private EntityType g = EntityType.GHOST;
  private EntityType z = EntityType.ZOMBIE;
  private EntityType p = EntityType.PLAYER;

  //Initialize all model components for use
  @Before
  public void initLevelComponents() {
    this.creator = new ModelCreator();
    this.level = creator.initializeLevel1();
    this.levelMap = creator.initializeLevel1Map();
    this.player1 = creator.getPlayer1();
    this.player2 = creator.getPlayer2();
    this.player3 = creator.getPlayer3();
    this.ghost1 = creator.getGhost1();
    this.ghost2 = creator.getGhost2();
    this.zombie = creator.getZombie1();
    this.key = creator.getLevel1Key();
    this.exit = creator.getLevel1Exit();
    this.room1 = creator.initializeRoom1();
    this.room2 = creator.initializeRoom2WithEntities();
    this.room3 = creator.initializeRoom3WithEntities();
    this.room4 = creator.initializeRoom4();
    this.hall1 = creator.initializeHall1WithEntities(room1, room2);
    this.hall2 = creator.initializeHall2(room3, room2);
    this.hall3 = creator.initializeHall3(room2, room4);
  }

  /**
   * Creates an initial game state with a given level, players, and adversaries.
   * Players are placed in the top left-most room of the level, and adversaries 
   * are placed in the bottom right-most room of the level
   */
  @Test
  public void testLevelStartConstructor() {
    List<Player> players = new ArrayList<>(Arrays.asList(this.player1, this.player2, this.player3));
    List<Adversary> adversaries = new ArrayList<>(Arrays.asList(this.ghost1, this.zombie, this.ghost2));
    Level level = new LevelImpl(
        players,
        adversaries,
        this.levelMap,
        new ArrayList<>(Arrays.asList(this.key, this.exit))
    );

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

    TextualLevelViewTest.testDrawLevel(this.level, expectedOut);
  }
  
  //Test for placeActors
  @Test
  public void testPlaceActors() {
	  List<Player> players = new ArrayList<>(Arrays.asList(this.player1, this.player2, this.player3));
	  List<Adversary> adversaries = new ArrayList<>(Arrays.asList(this.ghost1, this.zombie, this.ghost2));
	  Level level = new LevelImpl(this.levelMap, new ArrayList<>(Arrays.asList(this.key, this.exit)));
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
    Assert.assertEquals(GameState.LOST, level.isLevelOver());
  }

  /**
   * Tests that players can find the key, exit the level,
   * and that the GameState is WON once all players have
   * been removed from the level
   */
  @Test
  public void testPlayerActionGetKeyAndExit() {
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
    //Need to call a Tile remove player instead of just making it a space
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
    level.playerAction(this.player3, new Point(4, 16));
    assertFalse(level.checkValidMove(this.player3, new Point(1, 16)));
  }

  @Test
  public void testPlayerActionBadTooLongDiag() {
    assertFalse(level.checkValidMove(this.player1, new Point(6, 3)));
  }

  @Test
  public void testPlayerActionBadOutOfBounds() {
    assertFalse(level.checkValidMove(this.player2, new Point(9, 10)));
  }

  @Test
  public void testPlayerActionBadOutOfBoundsHall() {
    level.playerAction(this.player1, new Point(6, 2));
    assertFalse(level.checkValidMove(this.player1, new Point(7, 2)));
  }

  @Test
  public void testPlayerActionBadWall() {
    assertFalse(level.checkValidMove(this.player2, new Point(8, 10)));
  }
  
  @Test
  public void testPlayerActionBadWallDiag() {
    assertFalse(level.checkValidMove(this.player2, new Point(8, 9)));
  }

  @Test
  public void testPlayerActionBadCollision() {
    level.playerAction(this.player2, new Point(6, 9));
    level.playerAction(this.player2, new Point(6, 7));
    level.playerAction(this.player2, new Point(6, 5));
    level.playerAction(this.player2, new Point(6, 3));
    level.playerAction(this.player2, new Point(5, 2));
    assertFalse(level.checkValidMove(this.player2, new Point(4, 2)));
  }

  @Test
  public void testPlayerActionBadIntoHall() {
    level.playerAction(this.player2, new Point(8, 11));
    assertFalse(level.checkValidMove(this.player2, new Point(9, 12)));
  }

  @Test
  public void testPlayerActionBadIntoRoomWall() {
    assertFalse(level.checkValidMove(this.player1, new Point(3, 1)));
  }

  @Test
  public void testPlayerActionBadIntoRoomExit() {
    level.checkValidMove(this.player2, new Point(8, 11));
    level.checkValidMove(this.player2, new Point(9, 11));
    level.checkValidMove(this.player2, new Point(7, 11));
  }
  
  //Tests for valid player movements	  
  @Test
  public void testPlayerActionTwoSpacesUp() {
	  assertTrue(level.checkValidMove(this.player3, new Point(3, 15)));
  }
  
  @Test
  public void testPlayerActionJumpOverAdversary() {
	  assertTrue(level.checkValidMove(this.player3, new Point(1, 17)));
  }
  
  @Test
  public void testPlayerActionDiagonal() {
	  assertTrue(level.checkValidMove(this.player3, new Point(2, 16)));
  }
    
  @Test
  public void testPlayerMoveThroughDoor() {
	  assertTrue(level.checkValidMove(this.player1, new Point(2, 2)));
  }
  
  @Test
  public void testPlayerMoveToAdversary() {
	  assertTrue(level.checkValidMove(this.player2, new Point(7, 8)));
  }
  
  @Test
  public void testPlayerMoveToExit() {
	  assertTrue(level.checkValidMove(this.player2, new Point(7, 11)));
  }
  
  @Test
  public void testPlayerMoveToKey() {
	  assertTrue(level.checkValidMove(this.player3, new Point(4, 17)));
  }
  
  //Tests for adversaryAction

  //Tests that an adversary can move and travel between LevelComponents
  @Test
  public void testAdversaryActionNormalAndChangeComponent() {
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
    assertFalse(level.checkValidMove(this.ghost1, new Point(7, 10)));
  }

  @Test
  public void testAdversaryActionBadTooLongDiag() {
    assertFalse(level.checkValidMove(this.ghost1, new Point(6, 9)));
  }

  @Test
  public void testAdversaryActionBadOutOfBounds() {
    level.adversaryAction(this.ghost2, new Point(2, 13));
    assertFalse(level.checkValidMove(this.ghost2, new Point(3, 13)));
  }

  @Test
  public void testAdversaryActionBadWall() {
    assertFalse(level.checkValidMove(this.zombie, new Point(2, 18)));
  }

  @Test
  public void testAdversaryActionBadExit() {
    level.adversaryAction(this.ghost1, new Point(7, 9));
    level.adversaryAction(this.ghost1, new Point(7, 10));
    assertFalse(level.checkValidMove(this.ghost1, new Point(7, 11)));
  }

  @Test
  public void testAdversaryActionBadCollision() {
    level.adversaryAction(this.ghost2, new Point(2, 15));
    level.adversaryAction(this.ghost2, new Point(2, 16));
    assertFalse(level.checkValidMove(this.ghost2, new Point(2, 17)));
  }

  @Test
  public void testAdversaryActionBadIntoHall() {
    level.adversaryAction(this.ghost2, new Point(2, 13));
    level.adversaryAction(this.zombie, new Point(2, 16));
    level.adversaryAction(this.zombie, new Point(2, 15));
    level.adversaryAction(this.zombie, new Point(2, 14));
    assertFalse(level.checkValidMove(this.zombie, new Point(2, 13)));
  }

  @Test
  public void testPlayerActionBadIntoRoom() {
    level.adversaryAction(this.ghost2, new Point(2, 13));
    level.adversaryAction(this.zombie, new Point(2, 16));
    level.adversaryAction(this.zombie, new Point(2, 15));
    level.adversaryAction(this.zombie, new Point(2, 14));
    assertFalse(level.checkValidMove(this.ghost2, new Point(2, 14)));
  }
  
  //Tests for valid adversary movements	 
  @Test
  public void testAdversaryActionMoveUp() {
	  assertTrue(level.checkValidMove(this.ghost2, new Point(2, 13)));
  }

  @Test
  public void testAdversaryActionMoveLeft() {
	  assertTrue(level.checkValidMove(this.ghost1, new Point(6, 8)));
  }

  @Test
  public void testAdversaryActionMoveRight() {
	  assertTrue(level.checkValidMove(this.zombie, new Point(3, 17)));
  }
  
  @Test
  public void testAdversaryActionMoveDown() {
	  assertTrue(level.checkValidMove(this.ghost1, new Point(7, 9)));
  }
  
  //Test for isLevelOver

  /**
   * Tests that the level has been won when there are no players in the level,
   * the exit is unlocked, and the level has been exited
   */
  @Test
  public void testIsLevelOverWon() {
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
        new ArrayList<>(Arrays.asList(this.key, this.exit))
    );

    assertEquals(GameState.WON, level.isLevelOver());
  }

  /**
   * Tests that the level has been lost when there are no players in the level,
   * the exit is unlocked, and the level has not been exited
   */
  @Test
  public void testIsLevelOverLost() {
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
        new ArrayList<>(Arrays.asList(this.key, this.exit))
    );
    assertEquals(GameState.LOST, level.isLevelOver());
  }

  /**
   * Tests that the level is active when there is at least one player in the level,
   * the exit is unlocked, and the level has been exited
   */
  @Test
  public void testIsLevelOverActive() {
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
        new ArrayList<>(Arrays.asList(this.key, this.exit))
    );

    assertEquals(GameState.ACTIVE, level.isLevelOver());
  }
  
  //Tests for findComponent
  @Test (expected = IllegalArgumentException.class)
  public void testFindComponentNoComponent() {
	  level.findComponent(new Point(11, 0));  
  }
  
  @Test
  public void testFindComponentKey() {
	  assertEquals(this.room3, level.findComponent(new Point(4, 17)));
  }
  
  @Test
  public void testFindComponentExit() {
	  assertEquals(this.room2,level.findComponent(new Point(7, 11)));
  }
  
  @Test
  public void testFindComponentPlayer() {
	  assertEquals(this.hall1,level.findComponent(new Point(4, 2)));
  }
  
  @Test
  public void testFindComponentRoom() {
	  assertEquals(this.room4,level.findComponent(new Point(15, 15)));
  }
  
  @Test
  public void testFindComponentAdversary() {
	  assertEquals(this.room2,level.findComponent(new Point(7, 8)));
  }
  
  @Test
  public void testFindComponentHall() {
	  assertEquals(this.hall2,level.findComponent(new Point(4, 11)));
  }
  
  //Tests for checkValidLevelState
  @Test
	public void testCheckValidLevelStateNormal() {
    List<Player> players = new ArrayList<>(Arrays.asList(this.player1, this.player2, this.player3));
		assertTrue(level.checkValidLevelState(players));
	}

	@Test
	public void testCheckValidLevelStateNoKey() {
		//Add null key
		Level level = new LevelImpl(this.levelMap, new ArrayList<>(Arrays.asList(this.exit)));
		List<Player> players = new ArrayList<>(Arrays.asList(this.player1, this.player2, this.player3));
		assertFalse(level.checkValidLevelState(players));
	}

	@Test
	public void testCheckValidLevelStateNoExit() {
		//Add no exit
		Level level = new LevelImpl(this.levelMap, new ArrayList<>(Arrays.asList(this.key)));
		List<Player> players = new ArrayList<>(Arrays.asList(this.player1, this.player2, this.player3));
		assertFalse(level.checkValidLevelState(players));
	}

	@Test
	public void testCheckValidLevelStateBadExit() {
		//Add no exit
		Level level = new LevelImpl(
				new HashMap<Player, Point>(),
				new HashMap<Adversary, Point>(),
        this.levelMap,
				false,
				true,
        new ArrayList<>(Arrays.asList(this.key, this.exit))
		);
		List<Player> players = new ArrayList<>(Arrays.asList(this.player1, this.player2, this.player3));
		assertFalse(level.checkValidLevelState(players));
	}

	@Test
	public void testCheckValidLevelStateBadPlayer() {
		List<Player> players = new ArrayList<Player>(Arrays.asList(new Player(), this.player2, this.player3));
		assertFalse(level.checkValidLevelState(players));
	}
 
  //Tests for isPlayerAlive
	
	@Test
	public void testIsPlayerAliveTrue() {
		List<Player> players = new ArrayList<>(Arrays.asList(this.player1, this.player2, this.player3));
		List<Adversary> adversaries = new ArrayList<>(Arrays.asList(this.ghost1, this.zombie, this.ghost2));
		Level level = new LevelImpl(players, adversaries, this.levelMap, new ArrayList<>(Arrays.asList(this.key, this.exit)));
		assertTrue(level.isPlayerAlive(this.player1));
	}
	
	@Test
	public void testIsPlayerAliveFalsePlayer() {
		List<Player> players = new ArrayList<>(Arrays.asList(this.player1, this.player2, this.player3));
		List<Adversary> adversaries = new ArrayList<>(Arrays.asList(this.ghost1, this.zombie, this.ghost2));
		Level level = new LevelImpl(players, adversaries, this.levelMap, new ArrayList<>(Arrays.asList(this.key, this.exit)));
		assertFalse(level.isPlayerAlive(new Player()));
	}  
  
  //Tests for getPlayerMap
	@Test
	public void testGetPlayer1Map() {
	    List<List<EntityType>> expectedMap = Arrays.asList(
	            Arrays.asList(w, w, e, e, e),
	            Arrays.asList(s, w, e, e, e),
	            Arrays.asList(s, s, p, h, h),
	            Arrays.asList(w, w, e, e, h),
	            Arrays.asList(e, e, e, e, h));

	    assertEquals(expectedMap,level.getPlayerMap(this.player1));
	}
	
	@Test
	public void testGetPlayer2Map() {
	    List<List<EntityType>> expectedMap = Arrays.asList(
	            Arrays.asList(w, s, g, w, e),
	            Arrays.asList(w, s, s, w, e),
	            Arrays.asList(w, s, p, w, e),
	            Arrays.asList(s, s, x, s, h),
	            Arrays.asList(w, w, w, w, e));

	    assertEquals(expectedMap,level.getPlayerMap(this.player2));
	}
	
	@Test
	public void testGetPlayer3Map() {
	    List<List<EntityType>> expectedMap = Arrays.asList(
	            Arrays.asList(s, s, s, s, w),
	            Arrays.asList(s, s, s, s, w),
	            Arrays.asList(s, z, p, k, w),
	            Arrays.asList(w, w, w, w, w),
	            Arrays.asList(e, e, e, e, e));

	    assertEquals(expectedMap,level.getPlayerMap(this.player3));
	}
	
	//Tests for getPlayer
	@Test
	public void testGetPlayer() {
	    assertEquals(this.player1, level.getPlayer("Jacob"));
	    assertEquals(this.player2, level.getPlayer("Juliette"));
	    assertEquals(this.player3, level.getPlayer("SpiderMan"));
	}
   
	@Test (expected = IllegalArgumentException.class)
	public void testGetPlayerException() {
	    level.getPlayer("AJ");
	}
}
