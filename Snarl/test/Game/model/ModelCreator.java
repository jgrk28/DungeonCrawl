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
  private Tile space = new Space();

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
  
  private Room dummyRoom1 = new Room(new Point(0, 0), new ArrayList<>());
  private Room dummyRoom2 = new Room(new Point(10, 5), new ArrayList<>());

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
  public Room initializeRoom1() {
    List<List<Tile>> componentMap = new ArrayList<>();
    componentMap.add(Arrays.asList(wall, wall, wall, wall));
    componentMap.add(Arrays.asList(wall, space, space, wall));
    componentMap.add(Arrays.asList(wall, space, space, space));
    componentMap.add(Arrays.asList(wall, wall, wall, wall));

    return new Room(new Point(0,0), componentMap);
  }

  //4x6 room with three spaces for possible doors
  public Room initializeRoom2() {
    List<List<Tile>> componentMap = new ArrayList<>();
    componentMap.add(Arrays.asList(wall, space, wall, wall));
    componentMap.add(Arrays.asList(wall, space, space, wall));
    componentMap.add(Arrays.asList(wall, space, space, wall));
    componentMap.add(Arrays.asList(wall, space, space, wall));
    componentMap.add(Arrays.asList(space, space, space, space));
    componentMap.add(Arrays.asList(wall, wall, wall, wall));

    return new Room(new Point(5,7), componentMap);
  }

  //6x5 room with one spaces for a possible door
  public Room initializeRoom3() {
    List<List<Tile>> componentMap = new ArrayList<>();
    componentMap.add(Arrays.asList(wall, wall, space, wall, wall, wall));
    componentMap.add(Arrays.asList(wall, space, space, space, space, wall));
    componentMap.add(Arrays.asList(wall, space, space, space, space, wall));
    componentMap.add(Arrays.asList(wall, space, space, space, space, wall));
    componentMap.add(Arrays.asList(wall, wall, wall, wall, wall, wall));

    return new Room(new Point(0,14), componentMap);
  }

  //5x6 room with one space for a possible door
  public Room initializeRoom4() {
    List<List<Tile>> componentMap = new ArrayList<>();
    componentMap.add(Arrays.asList(wall, wall, wall, wall, wall));
    componentMap.add(Arrays.asList(space, space, space, space, wall));
    componentMap.add(Arrays.asList(wall, space, space, space, wall));
    componentMap.add(Arrays.asList(wall, space, space, space, wall));
    componentMap.add(Arrays.asList(wall, space, space, space, wall));
    componentMap.add(Arrays.asList(wall, wall, wall, wall, wall));

    return new Room(new Point(13,10), componentMap);
  }

  public Room initializeRoom5() {
    //Simple 4x4 room
    List<List<Tile>> componentMap = new ArrayList<>();
    componentMap.add(Arrays.asList(wall, wall, wall, wall));
    componentMap.add(Arrays.asList(wall, space, space, wall));
    componentMap.add(Arrays.asList(wall, space, space, wall));
    componentMap.add(Arrays.asList(wall, space, wall, wall));

    Room room5 = new Room(new Point(0,0), componentMap);
    room5.placeItem(room5Key);
    room5.placeActor(jacob, new Point(2, 1));
    room5.placeActor(boo, new Point(1, 3));
    return room5;
  }

  public Room initializeRoom6() {
    //Weird 2x2 room
    List<List<Tile>> componentMap = new ArrayList<>();
    componentMap.add(Arrays.asList(space, wall));
    componentMap.add(Arrays.asList(wall, space));

    Room room6 = new Room(new Point(15,7), componentMap);
    room6.placeItem(room6Exit);
    return room6;
  }

  public Room initializeRoom7() {
    //3x3 room that start at a negative position
    List<List<Tile>> componentMap = new ArrayList<>();
    componentMap.add(Arrays.asList(wall, wall, wall));
    componentMap.add(Arrays.asList(wall, space, wall));
    componentMap.add(Arrays.asList(wall, space, wall));

    Room room7 = new Room(new Point(-5,-1), componentMap);
    room7.placeActor(brainy, new Point(-4, 1));
    return room7;
  }

  public Room getDummyRoom1() {
    return dummyRoom1;
  }

  public Room getDummyRoom2() {
    return dummyRoom2;
  }

  //Hall that can connect room1 to room2
  public Hall initializeHall1() {
    Room startRoom = initializeRoom1();
    Room endRoom = initializeRoom2();

    List<Point> waypoints = new ArrayList<Point>();
    waypoints.add(new Point(6,2));

    Hall hall1 = new Hall(new Point(3,2), startRoom, new Point(6,7), endRoom, waypoints);
    startRoom.connectHall(new Point(3,2), hall1); //room1
    endRoom.connectHall(new Point(6,7), hall1); //room2

    return hall1;
  }

  //Hall that can connect room2 to room3
  public Hall initializeHall2() {
    Room startRoom = initializeRoom3();
    Room endRoom = initializeRoom2();

    List<Point> waypoints = new ArrayList<Point>();
    waypoints.add(new Point(2,11));

    Hall hall2 = new Hall(new Point(2,14), startRoom, new Point(5,11), endRoom, waypoints);
    startRoom.connectHall(new Point(2,14), hall2); //room3
    endRoom.connectHall(new Point(5,11), hall2); //room2

    return hall2;
  }

  //Hall that can connect room2 to room4
  public Hall initializeHall3() {
    Room startRoom = initializeRoom2();
    Room endRoom = initializeRoom4();

    List<Point> waypoints = new ArrayList<Point>();

    Hall hall3 = new Hall(new Point(8,11), startRoom, new Point(13,11), endRoom, waypoints);
    startRoom.connectHall(new Point(8,11), hall3); //room2
    endRoom.connectHall(new Point(13,11), hall3); //room4

    return hall3;
  }

  public Hall initializeHall4() {
    //Hall goes (2,11) -> (5,11) -> (5,8)
    List<Point> waypoints = new ArrayList<Point>();
    waypoints.add(new Point(5,11));

    Hall hall4 = new Hall(new Point(1,11), dummyRoom1, new Point(5,7), dummyRoom2, waypoints);

    hall4.placeActor(juliette, new Point(4, 11));
    return hall4;
  }

  public Hall initializeHall5() {
    //Hall goes (4,3) -> (5,3) -> (5,6) -> (2,6) -> (2,10)
    List<Point> waypoints = new ArrayList<Point>();
    waypoints.add(new Point(5,3));
    waypoints.add(new Point(5,6));
    waypoints.add(new Point(2,6));

    Hall hall5 = new Hall(new Point(3,3), dummyRoom2, new Point(2,11), dummyRoom1, waypoints);

    hall5.placeActor(dracula, new Point(4, 3));
    hall5.placeActor(buster, new Point(5, 3));
    return hall5;
  }

  //Connects hall1, hall2, and hall3 to the normal 4 rooms
  public List<LevelComponent> initializeLevel1Map() {
    Room room1 = initializeRoom1();
    Room room2 = initializeRoom2();
    Room room3 = initializeRoom3();
    Room room4 = initializeRoom4();

    Hall hall1 = initializeHall1();
    Hall hall2 = initializeHall2();
    Hall hall3 = initializeHall3();

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


  //Generates the full level map
  public List<List<EntityType>> initializeLevel1ViewableMap() {
    Level level1 = initializeLevel1();
    return level1.getMap();
  }

  //Places Actors and Items in the Level
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

  //Places Items in the Level2
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


  //Places Items in the Level3
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

  public List<Player> initializeDungeonPlayers() {
    return new ArrayList<>(Arrays.asList(jacob, juliette, spiderMan));
  }

  public List<Adversary> initializeDungeonAdversaries() {
    return new ArrayList<>(Arrays.asList(boo, buster, brainy, dracula));
  }

  public List<Level> initializeDungeonLevels() {
    //List of levels in the Dungeon
    Level level1 = initializeLevel1();
    Level level2 = initializeLevel2();
    Level level3 = initializeLevel3();
    return new ArrayList<>(Arrays.asList(level1, level2, level3));
  }

  public List<Level> initializeSimpleDungeonLevels() {
    //List of levels in the Dungeon
    Level level1 = initializeLevel1();
    Level level2 = initializeLevel1();
    return new ArrayList<>(Arrays.asList(level1, level2));
  }

  //Initialize Dungeon
  public Dungeon initializeDungeon() {
    List<Player> players = initializeDungeonPlayers();
    List<Adversary> adversaries = initializeDungeonAdversaries();
    List<Level> levels = initializeDungeonLevels();
    int startingLevel = 1;

    return new Dungeon(players, adversaries, startingLevel, levels);
  }

  //Initialize Dungeon
  public Dungeon initializeSimpleDungeon() {
    List<Player> players = initializeDungeonPlayers();
    List<Adversary> adversaries = initializeDungeonAdversaries();
    List<Level> levels = initializeSimpleDungeonLevels();
    int startingLevel = 1;

    return new Dungeon(players, adversaries, startingLevel, levels);
  }
}
