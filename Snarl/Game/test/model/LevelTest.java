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
import org.junit.rules.ExpectedException;
import view.TextualLevelViewTest;

public class LevelTest {
  private List<LevelComponent> levelMap;
  private Entity space = new Space();
  private Entity wall = new Wall();
  private Entity key = new Key();
  private Entity exit = new Exit();
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

  private void initializeRoom1() {
    //Simple 4x4 room with one space for a possible door
    List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
    componentMap.add(Arrays.asList(wall, wall, wall, wall));
    componentMap.add(Arrays.asList(wall, space, space, wall));
    componentMap.add(Arrays.asList(wall, space, space, space));
    componentMap.add(Arrays.asList(wall, wall, wall, wall));

    room1 = new Room(new Point(0,0), componentMap);
  }

  private void initializeRoom2() {
    //4x6 room with three spaces for possible doors
    List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
    componentMap.add(Arrays.asList(wall, space, wall, wall));
    componentMap.add(Arrays.asList(wall, space, space, wall));
    componentMap.add(Arrays.asList(wall, space, space, wall));
    componentMap.add(Arrays.asList(wall, space, space, wall));
    componentMap.add(Arrays.asList(space, space, exit, space));
    componentMap.add(Arrays.asList(wall, wall, wall, wall));

    room2 = new Room(new Point(5,7), componentMap);
  }

  //6x5 room with one spaces for a possible door
  private void initializeRoom3() {
    List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
    componentMap.add(Arrays.asList(wall, wall, space, wall, wall, wall));
    componentMap.add(Arrays.asList(wall, space, space, space, space, wall));
    componentMap.add(Arrays.asList(wall, space, space, space, space, wall));
    componentMap.add(Arrays.asList(wall, space, space, space, key, wall));
    componentMap.add(Arrays.asList(wall, wall, wall, wall, wall, wall));

    room3 = new Room(new Point(0,14), componentMap);
  }

  private void initializeRoom4() {
    //5x6 room with one space for a possible door
    List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
    componentMap.add(Arrays.asList(wall, wall, wall, wall, wall));
    componentMap.add(Arrays.asList(space, space, space, space, wall));
    componentMap.add(Arrays.asList(wall, space, space, space, wall));
    componentMap.add(Arrays.asList(wall, space, space, space, wall));
    componentMap.add(Arrays.asList(wall, space, space, space, wall));
    componentMap.add(Arrays.asList(wall, wall, wall, wall, wall));

    room4 = new Room(new Point(13,10), componentMap);
  }

  private void initializeHall1() {
    //Hall that can connect room1 to room2
    List<Entity> componentMap = Arrays.asList(space, space, space, space, space, space, space);
    List<Point> waypoints = new ArrayList<Point>();
    waypoints.add(new Point(6,2));

    hall1 = new Hall(componentMap, waypoints);
  }

  private void initializeHall2() {
    //Hall that can connect room2 to room3
    List<Entity> componentMap = Arrays.asList(space, space, space, space, space);
    List<Point> waypoints = new ArrayList<Point>();
    waypoints.add(new Point(2,11));

    hall2 = new Hall(componentMap, waypoints);
  }

  private void initializeHall3() {
    //Hall that can connect room2 to room4
    List<Entity> componentMap = Arrays.asList(space, space, space, space);
    List<Point> waypoints = new ArrayList<Point>();

    hall3 = new Hall(componentMap, waypoints);
  }

  private void initializeDoorsHall1() {
    //Doors for hall1
    room1.connectHall(new Point(4,2), hall1);
    room2.connectHall(new Point(6,6), hall1);
    hall1.connectRooms(new Point(3,2), room1, new Point(6,7), room2);
  }

  private void initializeDoorsHall2() {
    //Doors for hall2
    room2.connectHall(new Point(4,11), hall2);
    room3.connectHall(new Point(2,13), hall2);
    hall2.connectRooms(new Point(2,14), room3, new Point(5,11), room2);
  }

  private void initializeDoorsHall3() {
    //Doors for hall3
    room2.connectHall(new Point(9,11), hall3);
    room4.connectHall(new Point(12,11), hall3);
    hall3.connectRooms(new Point(8,11), room2, new Point(13,11), room4);
  }

  private void initializeLevelMap() {
    //Connects hall1, hall2, and hall3 to the normal 4 rooms
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

  private Level makeTestLevel() {
    initializeLevelMap();
    Map<Player, Point> playersPos = new HashMap<>();
    playersPos.put(this.player1, new Point(4, 2));
    playersPos.put(this.player2, new Point(7, 10));
    playersPos.put(this.player2, new Point(3, 17));
    Map<Adversary, Point> adversariesPos = new HashMap<>();
    adversariesPos.put(this.ghost1, new Point(7, 8));
    adversariesPos.put(this.zombie, new Point(2, 17));
    adversariesPos.put(this.ghost2, new Point(2, 14));
    boolean exitUnlocked = false;
    boolean levelExited = false;
    return new LevelImpl(playersPos, adversariesPos, this.levelMap, exitUnlocked, levelExited);
  }

  @Before
  public void initLevelComponents() {
    //Initialize all components for use
    //They have not been added to a level but they are available for use
    initializeRoom1();
    initializeRoom1();
    initializeRoom2();
    initializeRoom3();
    initializeRoom4();

    initializeHall1();
    initializeHall2();
    initializeHall3();
  }

  @Test
  public void testLevelStartConstructor() {
    initializeLevelMap();
    List<Player> players = new ArrayList<>(Arrays.asList(this.player1, this.player2, this.player3));
    List<Adversary> adversaries = new ArrayList<>(Arrays.asList(this.ghost1, this.zombie, this.ghost2));
    Level level = new LevelImpl(players, adversaries, this.levelMap);

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

  @Test
  public void testIsLevelOverWon() {
    initializeLevelMap();
    Map<Player, Point> playersPos = new HashMap<>();
    Map<Adversary, Point> adversariesPos = new HashMap<>();
    adversariesPos.put(this.ghost1, new Point(7, 8));
    boolean exitUnlocked = true;
    boolean levelExited = true;
    Level level = new LevelImpl(playersPos, adversariesPos, this.levelMap, exitUnlocked, levelExited);

    assertEquals(GameState.WON, level.isLevelOver());
  }

  @Test
  public void testIsLevelOverLost() {
    initializeLevelMap();
    Map<Player, Point> playersPos = new HashMap<>();
    Map<Adversary, Point> adversariesPos = new HashMap<>();
    adversariesPos.put(this.ghost1, new Point(7, 8));
    boolean exitUnlocked = true;
    boolean levelExited = false;
    Level level = new LevelImpl(playersPos, adversariesPos, this.levelMap, exitUnlocked, levelExited);
    assertEquals(GameState.LOST, level.isLevelOver());
  }

  @Test
  public void testIsLevelOverActive() {
    initializeLevelMap();
    Map<Player, Point> playersPos = new HashMap<>();
    playersPos.put(this.player1, new Point(5, 2));
    Map<Adversary, Point> adversariesPos = new HashMap<>();
    adversariesPos.put(this.ghost1, new Point(7, 8));
    boolean exitUnlocked = true;
    boolean levelExited = true;
    Level level = new LevelImpl(playersPos, adversariesPos, this.levelMap, exitUnlocked, levelExited);

    assertEquals(GameState.ACTIVE, level.isLevelOver());
  }

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

  @Test(expected = IllegalArgumentException.class)
  public void testPlayerActionBadTooLong() {
    Level level = makeTestLevel();
    level.playerAction(this.player3, new Point(4, 16));
    level.playerAction(this.player3, new Point(1, 16));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayerActionBadTooLongDiag() {
    Level level = makeTestLevel();
    level.playerAction(this.player1, new Point(6, 3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayerActionBadOutOfBounds() {
    Level level = makeTestLevel();
    level.playerAction(this.player2, new Point(9, 10));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayerActionBadOutOfBoundsHall() {
    Level level = makeTestLevel();
    level.playerAction(this.player1, new Point(6, 2));
    level.playerAction(this.player1, new Point(7, 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayerActionBadWall() {
    Level level = makeTestLevel();
    level.playerAction(this.player2, new Point(8, 10));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayerActionBadWallDiag() {
    Level level = makeTestLevel();
    level.playerAction(this.player2, new Point(8, 9));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayerActionBadExit() {
    Level level = makeTestLevel();
    level.playerAction(this.player2, new Point(7, 11));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayerActionBadCollision() {
    Level level = makeTestLevel();
    level.playerAction(this.player2, new Point(6, 9));
    level.playerAction(this.player2, new Point(6, 7));
    level.playerAction(this.player2, new Point(6, 5));
    level.playerAction(this.player2, new Point(6, 3));
    level.playerAction(this.player2, new Point(5, 2));
    level.playerAction(this.player2, new Point(4, 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayerActionBadIntoHall() {
    Level level = makeTestLevel();
    level.playerAction(this.player2, new Point(8, 11));
    level.playerAction(this.player2, new Point(9, 12));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayerActionBadIntoRoomWall() {
    Level level = makeTestLevel();
    level.playerAction(this.player1, new Point(3, 1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayerActionBadIntoRoomExit() {
    Level level = makeTestLevel();
    level.playerAction(this.player2, new Point(8, 11));
    level.playerAction(this.player2, new Point(9, 11));
    level.playerAction(this.player2, new Point(7, 11));
  }

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

  @Test
  public void testAdversaryActionKill() {
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

    //Go kill last player
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

  @Test(expected = IllegalArgumentException.class)
  public void testAdversaryActionBadTooLong() {
    Level level = makeTestLevel();
    level.adversaryAction(this.ghost1, new Point(7, 10));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAdversaryActionBadTooLongDiag() {
    Level level = makeTestLevel();
    level.adversaryAction(this.ghost1, new Point(6, 9));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAdversaryActionBadOutOfBounds() {
    Level level = makeTestLevel();
    level.adversaryAction(this.ghost2, new Point(2, 13));
    level.adversaryAction(this.ghost2, new Point(2, 12));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAdversaryActionBadWall() {
    Level level = makeTestLevel();
    level.adversaryAction(this.zombie, new Point(2, 18));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAdversaryActionBadExit() {
    Level level = makeTestLevel();
    level.adversaryAction(this.ghost1, new Point(7, 9));
    level.adversaryAction(this.ghost1, new Point(7, 10));
    level.adversaryAction(this.ghost1, new Point(7, 11));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAdversaryActionBadCollision() {
    Level level = makeTestLevel();
    level.adversaryAction(this.ghost2, new Point(2, 15));
    level.adversaryAction(this.ghost2, new Point(2, 16));
    level.adversaryAction(this.ghost2, new Point(2, 17));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAdversaryActionBadIntoHall() {
    Level level = makeTestLevel();
    level.adversaryAction(this.ghost2, new Point(2, 13));
    level.adversaryAction(this.zombie, new Point(2, 16));
    level.adversaryAction(this.zombie, new Point(2, 15));
    level.adversaryAction(this.zombie, new Point(2, 14));
    level.adversaryAction(this.zombie, new Point(2, 13));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayerActionBadIntoRoom() {
    Level level = makeTestLevel();
    level.adversaryAction(this.ghost2, new Point(2, 13));
    level.adversaryAction(this.zombie, new Point(2, 16));
    level.adversaryAction(this.zombie, new Point(2, 15));
    level.adversaryAction(this.zombie, new Point(2, 14));

    level.adversaryAction(this.ghost2, new Point(2, 14));
  }
}