package Game.model;

import Game.modelView.EntityType;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to generate any sort of Model component for use in testing
 * across different unit tests.
 */
public class ModelCreator {
  private Tile wall = new Wall();

  private Key room5Key = new Key(new Point(1, 1));
  private Exit room6Exit = new Exit(new Point(16, 8));
  private Key level1Key =  new Key(new Point(4, 17));
  private Exit level1Exit = new Exit(new Point(7, 11));
  private Key level2Key =  new Key(new Point(6,8));
  private Exit level2Exit = new Exit(new Point(1,15));
  private Key level3Key =  new Key(new Point(15,12));
  private Exit level3Exit = new Exit(new Point(3,17));

  private Player jacob = new Player("Jacob");
  private Player juliette = new Player("Juliette");
  private Player spiderMan = new Player("SpiderMan");
  private Ghost boo = new Ghost("Boo");
  private Ghost buster = new Ghost("Buster");
  private Zombie brainy = new Zombie("Brainy");
  private Zombie dracula = new Zombie("Dracula");

  public Player getPlayer1() {
    return jacob;
  }

  public Player getPlayer2() {
    return juliette;
  }

  public Player getPlayer3() {
    return spiderMan;
  }

  public Ghost getGhost1() {
    return boo;
  }

  public Ghost getGhost2() {
    return buster;
  }

  public Zombie getZombie1() {
    return brainy;
  }

  public Zombie getZombie2() {
    return dracula;
  }

  public Exit getLevel1Exit() {
    return level1Exit;
  }

  public Key getLevel1Key() {
    return level1Key;
  }

  //Simple 4x4 room with one space for a possible door
  //XXXX
  //X..X
  //X...
  //XXXX
  public Room initializeRoom1() {
    List<List<Tile>> componentMap = new ArrayList<>();
    componentMap.add(Arrays.asList(wall, wall, wall, wall));
    componentMap.add(Arrays.asList(wall, new Space(), new Space(), wall));
    componentMap.add(Arrays.asList(wall, new Space(), new Space(), new Space()));
    componentMap.add(Arrays.asList(wall, wall, wall, wall));

    return new Room(new Point(0,0), componentMap);
  }

  //4x6 room with three spaces for possible doors
  //X.XX
  //X..X
  //X..X
  //X..X
  //....
  //XXXX
  public Room initializeRoom2() {
    List<List<Tile>> componentMap = new ArrayList<>();
    componentMap.add(Arrays.asList(wall, new Space(), wall, wall));
    componentMap.add(Arrays.asList(wall, new Space(), new Space(), wall));
    componentMap.add(Arrays.asList(wall, new Space(), new Space(), wall));
    componentMap.add(Arrays.asList(wall, new Space(), new Space(), wall));
    componentMap.add(Arrays.asList(new Space(), new Space(), new Space(), new Space()));
    componentMap.add(Arrays.asList(wall, wall, wall, wall));

    return new Room(new Point(5,7), componentMap);
  }

  //X.XX
  //X.GX
  //X..X
  //X.PX
  //..@.
  //XXXX
  public Room initializeRoom2WithEntities() {
    Room room2 = initializeRoom2();
    room2.placeActor(this.juliette, new Point(7, 10));
    room2.placeActor(this.boo, new Point(7, 8));
    room2.placeItem(this.level1Exit);

    return room2;
  }

  //6x5 room with one spaces for a possible door
  //XX.XXX
  //X....X
  //X....X
  //X....X
  //X....X
  //XXXXXX
  public Room initializeRoom3() {
    List<List<Tile>> componentMap = new ArrayList<>();
    componentMap.add(Arrays.asList(wall, wall, new Space(), wall, wall, wall));
    componentMap.add(Arrays.asList(wall, new Space(), new Space(), new Space(), new Space(), wall));
    componentMap.add(Arrays.asList(wall, new Space(), new Space(), new Space(), new Space(), wall));
    componentMap.add(Arrays.asList(wall, new Space(), new Space(), new Space(), new Space(), wall));
    componentMap.add(Arrays.asList(wall, wall, wall, wall, wall, wall));

    return new Room(new Point(0,14), componentMap);
  }

  //XXGXXX
  //X....X
  //X....X
  //X....X
  //X.ZP!X
  //XXXXXX
  public Room initializeRoom3WithEntities() {
    Room room3 = initializeRoom3();
    room3.placeActor(this.spiderMan, new Point(3, 17));
    room3.placeActor(this.brainy, new Point(2, 17));
    room3.placeActor(this.buster, new Point(2, 14));
    room3.placeItem(this.level1Key);

    return room3;
  }

  //5x6 room with one space for a possible door
  //XXXXX
  //....X
  //X...X
  //X...X
  //X...X
  //XXXXX
  public Room initializeRoom4() {
    List<List<Tile>> componentMap = new ArrayList<>();
    componentMap.add(Arrays.asList(wall, wall, wall, wall, wall));
    componentMap.add(Arrays.asList(new Space(), new Space(), new Space(), new Space(), wall));
    componentMap.add(Arrays.asList(wall, new Space(), new Space(), new Space(), wall));
    componentMap.add(Arrays.asList(wall, new Space(), new Space(), new Space(), wall));
    componentMap.add(Arrays.asList(wall, new Space(), new Space(), new Space(), wall));
    componentMap.add(Arrays.asList(wall, wall, wall, wall, wall));

    return new Room(new Point(13,10), componentMap);
  }

  //Simple 4x4 room
  //XXXX
  //X!PX
  //X..X
  //XGXX
  public Room initializeRoom5() {
    List<List<Tile>> componentMap = new ArrayList<>();
    componentMap.add(Arrays.asList(wall, wall, wall, wall));
    componentMap.add(Arrays.asList(wall, new Space(), new Space(), wall));
    componentMap.add(Arrays.asList(wall, new Space(), new Space(), wall));
    componentMap.add(Arrays.asList(wall, new Space(), wall, wall));

    Room room5 = new Room(new Point(0,0), componentMap);
    room5.placeItem(room5Key);
    room5.placeActor(jacob, new Point(2, 1));
    room5.placeActor(boo, new Point(1, 3));
    return room5;
  }

  //Weird 2x2 room
  //.X
  //X@
  public Room initializeRoom6() {
    List<List<Tile>> componentMap = new ArrayList<>();
    componentMap.add(Arrays.asList(new Space(), wall));
    componentMap.add(Arrays.asList(wall, new Space()));

    Room room6 = new Room(new Point(15,7), componentMap);
    room6.placeItem(room6Exit);
    return room6;
  }

  //3x3 room that start at a negative position
  //XXX
  //X.X
  //XZX
  public Room initializeRoom7() {
    List<List<Tile>> componentMap = new ArrayList<>();
    componentMap.add(Arrays.asList(wall, wall, wall));
    componentMap.add(Arrays.asList(wall, new Space(), wall));
    componentMap.add(Arrays.asList(wall, new Space(), wall));

    Room room7 = new Room(new Point(-5,-1), componentMap);
    room7.placeActor(brainy, new Point(-4, 1));
    return room7;
  }

  //2x3 room
  //X.
  //X.
  //XX
  public Room initializeRoom8() {
    List<List<Tile>> componentMap = new ArrayList<>();

    componentMap.add(Arrays.asList(wall, new Space()));
    componentMap.add(Arrays.asList(wall, new Space()));
    componentMap.add(Arrays.asList(wall, wall));

    Room room8 = new Room(new Point(0,11), componentMap);
    return room8;
  }

  //3x2 room
  //X.X
  //X.X
  public Room initializeRoom9() {
    List<List<Tile>> componentMap = new ArrayList<>();

    componentMap.add(Arrays.asList(wall, new Space(), wall));
    componentMap.add(Arrays.asList(wall, new Space(), wall));

    Room room9 = new Room(new Point(4,6), componentMap);
    return room9;
  }

  //4x4 room
  //X.XX
  //X..X
  //X..X
  //XX..
  public Room initializeRoom10() {
    List<List<Tile>> componentMap = new ArrayList<>();

    componentMap.add(Arrays.asList(wall, new Space(), wall, wall));
    componentMap.add(Arrays.asList(wall, new Space(), new Space(), wall));
    componentMap.add(Arrays.asList(wall, new Space(), new Space(), wall));
    componentMap.add(Arrays.asList(wall, wall, new Space(), new Space()));

    Room room10 = new Room(new Point(0,0), componentMap);
    return room10;
  }

  //4x4 room
  //X.XX
  //X..X
  //X..X
  //XX..
  public Room initializeRoom11() {
    List<List<Tile>> componentMap = new ArrayList<>();

    componentMap.add(Arrays.asList(wall, new Space(), wall, wall));
    componentMap.add(Arrays.asList(wall, new Space(), new Space(), wall));
    componentMap.add(Arrays.asList(wall, new Space(), new Space(), wall));
    componentMap.add(Arrays.asList(wall, wall, new Space(), new Space()));

    Room room11 = new Room(new Point(1,11), componentMap);
    return room11;
  }

  //Hall goes
  //room1 -> (4,2) -> (6,2) -> (6,6) -> room2
  public Hall initializeHall1(Room startRoom, Room endRoom) {
    List<Point> waypoints = new ArrayList<Point>();
    waypoints.add(new Point(6,2));

    Hall hall1 = new Hall(new Point(3,2), startRoom, new Point(6,7), endRoom, waypoints);
    startRoom.connectHall(new Point(3,2), hall1); //room1
    endRoom.connectHall(new Point(6,7), hall1); //room2

    return hall1;
  }

  //room1 -> (4,2) -> (6,2) -> (6,6) -> room2
  //Player map [P......]
  public Hall initializeHall1WithEntities(Room startRoom, Room endRoom) {
    Hall hall1 = initializeHall1(startRoom, endRoom);
    hall1.placeActor(this.jacob, new Point(4, 2));

    return hall1;
  }

  //Hall to replace hall1 that makes multiple turns
  //Hall goes
  //room1 -> (4,2) -> (9,2) -> (9,4) -> (6,4) -> (6,6) -> room2
  public Hall initializeHall1Snake(Room startRoom, Room endRoom) {
    List<Point> waypoints = new ArrayList<Point>();
    waypoints.add(new Point(9,2));
    waypoints.add(new Point(9,4));
    waypoints.add(new Point(6,4));

    Hall hall1 = new Hall(new Point(3,2), startRoom, new Point(6,7), endRoom, waypoints);
    startRoom.connectHall(new Point(3,2), hall1); //room1
    endRoom.connectHall(new Point(6,7), hall1); //room2

    return hall1;
  }

  //Hall to replace hall1 that travels directly next to a room
  //Hall goes
  //room1 -> (4,2) -> (9,2) -> (9,6) -> (6,6) -> room2
  public Hall initializeHall1RoomBrush(Room startRoom, Room endRoom) {
    List<Point> waypoints = new ArrayList<Point>();
    waypoints.add(new Point(9,2));
    waypoints.add(new Point(9,6));
    waypoints.add(new Point(6,6));

    Hall hall1 = new Hall(new Point(3,2), startRoom, new Point(6,7), endRoom, waypoints);
    startRoom.connectHall(new Point(3,2), hall1); //room1
    endRoom.connectHall(new Point(6,7), hall1); //room2

    return hall1;
  }

  //Hall to replace hall1 that travels directly next to other halls and itself
  //(12,2), (12,10), (9,10), (9,3), (6,3).
  //Hall goes
  //room1 -> (4,2) -> (12,2) -> (12,10) -> (9,10) -> (9,3) -> (6,3) -> (6,6) -> room2
  public Hall initializeHall1HallBrush(Room startRoom, Room endRoom) {
    List<Point> waypoints = new ArrayList<Point>();
    waypoints.add(new Point(12,2));
    waypoints.add(new Point(12,10));
    waypoints.add(new Point(9,10));
    waypoints.add(new Point(9,3));
    waypoints.add(new Point(6,3));

    Hall hall1 = new Hall(new Point(3,2), startRoom, new Point(6,7), endRoom, waypoints);
    startRoom.connectHall(new Point(3,2), hall1); //room1
    endRoom.connectHall(new Point(6,7), hall1); //room2

    return hall1;
  }

  //Hall goes
  //room3 -> (2,13) -> (2,11) -> (4,11) -> room2
  public Hall initializeHall2(Room startRoom, Room endRoom) {
    List<Point> waypoints = new ArrayList<Point>();
    waypoints.add(new Point(2,11));

    Hall hall2 = new Hall(new Point(2,14), startRoom, new Point(5,11), endRoom, waypoints);
    startRoom.connectHall(new Point(2,14), hall2); //room3
    endRoom.connectHall(new Point(5,11), hall2); //room2

    return hall2;
  }

  //Hall goes
  //room2 -> (9,11) -> (12, 11) -> room4
  public Hall initializeHall3(Room startRoom, Room endRoom) {
    List<Point> waypoints = new ArrayList<Point>();

    Hall hall3 = new Hall(new Point(8,11), startRoom, new Point(13,11), endRoom, waypoints);
    startRoom.connectHall(new Point(8,11), hall3); //room2
    endRoom.connectHall(new Point(13,11), hall3); //room4

    return hall3;
  }

  //Hall goes
  //room8 -> (2,11) -> (5,11) -> (5,8) -> room9
  //Player map [..P....]
  public Hall initializeHall4(Room startRoom, Room endRoom) {
    List<Point> waypoints = new ArrayList<Point>();
    waypoints.add(new Point(5,11));

    Hall hall4 = new Hall(new Point(1,11), startRoom, new Point(5,7), endRoom, waypoints);

    hall4.placeActor(juliette, new Point(4, 11));
    return hall4;
  }

  //Hall goes
  //room10 -> (4,3) -> (5,3) -> (5,6) -> (2,6) -> (2,10) -> room11
  //Player map [ZG.........]
  public Hall initializeHall5(Room startRoom, Room endRoom) {
    List<Point> waypoints = new ArrayList<Point>();
    waypoints.add(new Point(5,3));
    waypoints.add(new Point(5,6));
    waypoints.add(new Point(2,6));

    Hall hall5 = new Hall(new Point(3,3), startRoom, new Point(2,11), endRoom, waypoints);

    hall5.placeActor(dracula, new Point(4, 3));
    hall5.placeActor(buster, new Point(5, 3));
    return hall5;
  }

  //Connects hall1, hall2, and hall3 to the normal 4 rooms
  //All rooms and halls are empty
  public List<LevelComponent> initializeLevel1Map() {
    Room room1 = initializeRoom1();
    Room room2 = initializeRoom2();
    Room room3 = initializeRoom3();
    Room room4 = initializeRoom4();

    Hall hall1 = initializeHall1(room1, room2);
    Hall hall2 = initializeHall2(room3, room2);
    Hall hall3 = initializeHall3(room2, room4);

    List<LevelComponent> level1Map = new ArrayList<LevelComponent>();

    level1Map.add(room1);
    level1Map.add(room2);
    level1Map.add(room3);
    level1Map.add(room4);

    level1Map.add(hall1);
    level1Map.add(hall2);
    level1Map.add(hall3);

    return level1Map;
  }

  //Generates the full level viewable map
  public List<List<EntityType>> initializeLevel1ViewableMap() {
    Level level1 = initializeLevel1();
    return level1.getMap();
  }

  //Places Actors and Items in the first Level in an intermediate state
  public Level initializeLevel1() {
    List<LevelComponent> level1Map = initializeLevel1Map();

    Map<Player, Point> playersPos = new HashMap<>();
    playersPos.put(this.jacob, new Point(4, 2));
    playersPos.put(this.juliette, new Point(7, 10));
    playersPos.put(this.spiderMan, new Point(3, 17));
    Map<Adversary, Point> adversariesPos = new HashMap<>();
    adversariesPos.put(this.boo, new Point(7, 8));
    adversariesPos.put(this.brainy, new Point(2, 17));
    adversariesPos.put(this.buster, new Point(2, 14));
    List<Item> items = new ArrayList<>();
    items.add(level1Exit);
    items.add(level1Key);

    boolean exitUnlocked = false;
    boolean levelExited = false;
    return new LevelImpl(
        playersPos,
        adversariesPos,
        level1Map,
        exitUnlocked,
        levelExited,
        items
    );
  }

  //Initialize the first level without placing any actors
  public Level initializeLevel1NoActor() {
    List<LevelComponent> level1Map = initializeLevel1Map();
    List<Item> items = new ArrayList<>();
    items.add(level1Exit);
    items.add(level1Key);

    return new LevelImpl(
        level1Map,
        items
    );
  }

  //Initialize the second level without placing any actors
  //Same as the first level but the key and exit are in different positions
  public Level initializeLevel2() {
    List<LevelComponent> level2Map = initializeLevel1Map();

    List<Item> items = new ArrayList<>();
    items.add(level2Exit);
    items.add(level2Key);
    return new LevelImpl(
        level2Map,
        items
    );
  }

  //Initialize the third level without placing any actors
  //Same as the first level but the key and exit are in different positions
  public Level initializeLevel3() {
    List<LevelComponent> level3Map = initializeLevel1Map();

    List<Item> items = new ArrayList<>();
    items.add(level3Exit);
    items.add(level3Key);
    return new LevelImpl(
        level3Map,
        items
    );
  }

  //Return the list of players that are in out dungeon
  public List<Player> initializeDungeonPlayers() {
    return new ArrayList<>(Arrays.asList(jacob, juliette, spiderMan));
  }

  //Return the list of adversaries that are in out dungeon
  public List<Adversary> initializeDungeonAdversaries() {
    return new ArrayList<>(Arrays.asList(boo, buster, brainy, dracula));
  }

  //Return the list of levels that are in our dungeon
  //There are no players in any level so the first level would need to be started
  public List<Level> initializeDungeonLevels() {
    Level level1 = initializeLevel1NoActor();
    Level level2 = initializeLevel2();
    Level level3 = initializeLevel3();
    return new ArrayList<>(Arrays.asList(level1, level2, level3));
  }

  //Return the list of levels that are in our dungeon
  //There are players in the first level in an intermediate state
  public List<Level> initializeDungeonLevelsStarted() {
    Level level1 = initializeLevel1();
    Level level2 = initializeLevel2();
    Level level3 = initializeLevel3();
    return new ArrayList<>(Arrays.asList(level1, level2, level3));
  }

  //Return the list of levels that are in our simple dungeon
  //There are no actors in this dungeon and both levels are identical
  public List<Level> initializeSimpleDungeonLevels() {
    Level level1 = initializeLevel1NoActor();
    Level level2 = initializeLevel1NoActor();
    return new ArrayList<>(Arrays.asList(level1, level2));
  }

  //Initialize Dungeon with no level started
  public Dungeon initializeDungeon() {
    List<Player> players = initializeDungeonPlayers();
    List<Adversary> adversaries = initializeDungeonAdversaries();
    List<Level> levels = initializeDungeonLevels();
    int startingLevel = 1;

    return new Dungeon(players, adversaries, startingLevel, levels);
  }

  //Initialize Dungeon with the first level started in an intermediate position
  public Dungeon initializeDungeonStarted() {
    List<Player> players = initializeDungeonPlayers();
    List<Adversary> adversaries = initializeDungeonAdversaries();
    List<Level> levels = initializeDungeonLevelsStarted();
    int startingLevel = 1;

    return new Dungeon(players, adversaries, startingLevel, levels);
  }

  //Initialize simple Dungeon with no level started
  public Dungeon initializeSimpleDungeon() {
    List<Player> players = initializeDungeonPlayers();
    List<Adversary> adversaries = initializeDungeonAdversaries();
    List<Level> levels = initializeSimpleDungeonLevels();
    int startingLevel = 1;

    return new Dungeon(players, adversaries, startingLevel, levels);
  }

  public static List<Point> initWinningMoves() {
    List<Point> winningMoves = new ArrayList<>();

    //Get key then exit
    winningMoves.add(new Point(2, 2));
    winningMoves.add(new Point(4, 2));
    winningMoves.add(new Point(6, 2));
    winningMoves.add(new Point(6, 4));
    winningMoves.add(new Point(6, 6));
    winningMoves.add(new Point(6, 8));
    winningMoves.add(new Point(6, 10));
    winningMoves.add(new Point(5, 11));
    winningMoves.add(new Point(3, 11));
    winningMoves.add(new Point(2, 12));
    winningMoves.add(new Point(2, 14));
    winningMoves.add(new Point(3, 15));
    winningMoves.add(new Point(4, 16));
    winningMoves.add(new Point(4, 17));
    winningMoves.add(new Point(4, 16));
    winningMoves.add(new Point(3, 15));
    winningMoves.add(new Point(2, 14));
    winningMoves.add(new Point(2, 12));
    winningMoves.add(new Point(3, 11));
    winningMoves.add(new Point(5, 11));
    winningMoves.add(new Point(7, 11));

    return winningMoves;
  }

  public static List<Point> initGetKeyMoves() {
    List<Point> getKeyMoves = new ArrayList<>();

    //Get key then exit
    getKeyMoves.add(new Point(2, 2));
    getKeyMoves.add(new Point(4, 2));
    getKeyMoves.add(new Point(6, 2));
    getKeyMoves.add(new Point(6, 4));
    getKeyMoves.add(new Point(6, 6));
    getKeyMoves.add(new Point(6, 8));
    getKeyMoves.add(new Point(6, 10));
    getKeyMoves.add(new Point(5, 11));
    getKeyMoves.add(new Point(3, 11));
    getKeyMoves.add(new Point(2, 12));
    getKeyMoves.add(new Point(2, 14));
    getKeyMoves.add(new Point(3, 15));
    getKeyMoves.add(new Point(4, 16));
    getKeyMoves.add(new Point(4, 17));

    return getKeyMoves;
  }
}
