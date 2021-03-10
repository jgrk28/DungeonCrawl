package model;

import static org.junit.Assert.*;

import java.util.List;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;


public class DungeonTest {
	
	//Fields for the Dungeon
	private List<Player> players;
	private List<Adversary> adversaries;
	private List<Level> levels;
	
	//Fields for a level
	private List<LevelComponent> levelMap;
	private Entity space = new Space();
	private Entity wall = new Wall();
	private Key key1;
	private Exit exit1;
	private Key key2;
	private Exit exit2;
	private Key key3;
	private Exit exit3;

	private Room room1;
	private Room room2;
	private Room room3;
	private Room room4;

	private Hall hall1;
	private Hall hall2;
	private Hall hall3;
	
	//Initialize the level maps for each level
	private List<LevelComponent> initializeLevelMapOne() {
		levelMap = new ArrayList<LevelComponent>();
		
		levelMap.add(room1);
		levelMap.add(room2);
		
		levelMap.add(hall1);
		
		return levelMap;
	}
	
	private List<LevelComponent> initializeLevelMapTwo() {
		levelMap = new ArrayList<LevelComponent>();
		
		levelMap.add(room1);
		levelMap.add(room2);
		levelMap.add(room3);
		
		levelMap.add(hall1);
		levelMap.add(hall2);
		
		return levelMap;
	}
	
	private List<LevelComponent> initializeLevelMapThree() {
		levelMap = new ArrayList<LevelComponent>();
		
		levelMap.add(room1);
		levelMap.add(room2);
		levelMap.add(room3);
		levelMap.add(room4);
		
		levelMap.add(hall1);
		levelMap.add(hall2);
		levelMap.add(hall3);
		
		return levelMap;
	}
	
	@Before
	public void initializeLevelComponents() {
		//Initialize all components for use
		//They have not been added to a level but they are available for use
		this.key1 = new Key(new Point(1, 1));
		this.exit1 = new Exit(new Point(6, 8));
		this.key2 = new Key(new Point(2, 2));
		this.exit2 = new Exit(new Point(7, 8));
		this.key3 = new Key(new Point(4, 16));
		this.exit3 = new Exit(new Point(7, 11));
		initializeRoomOne();
		initializeRoomTwo();
		initializeRoomThree();
		initializeRoomFour();
		
		initializeHallOne();
		initializeHallTwo();
		initializeHallThree();
		
		initializeDoorsHallOne();
		initializeDoorsHallTwo();
		initializeDoorsHallThree();
	}

	
	private void initializeRoomOne() {
		//Simple 4x4 room with one space for a possible door
		List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
		componentMap.add(Arrays.asList(wall, wall, wall, wall));
		componentMap.add(Arrays.asList(wall, space, space, wall));
		componentMap.add(Arrays.asList(wall, space, space, space));
		componentMap.add(Arrays.asList(wall, wall, wall, wall));

		room1 = new Room(new Point(0,0), componentMap);
	}
	
	private void initializeRoomTwo() {
		//4x6 room with three spaces for possible doors
		List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
		componentMap.add(Arrays.asList(wall, space, wall, wall));
		componentMap.add(Arrays.asList(wall, space, space, wall));
		componentMap.add(Arrays.asList(wall, space, space, wall));
		componentMap.add(Arrays.asList(wall, space, space, wall));
		componentMap.add(Arrays.asList(space, space, space, space));
		componentMap.add(Arrays.asList(wall, wall, wall, wall));
		
		room2 = new Room(new Point(5,7), componentMap);
	}
	
	private void initializeRoomThree() {
		//6x5 room with one spaces for a possible door
		List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
		componentMap.add(Arrays.asList(wall, wall, space, wall, wall, wall));
		componentMap.add(Arrays.asList(wall, space, space, space, space, wall));
		componentMap.add(Arrays.asList(wall, space, space, space, space, wall));
		componentMap.add(Arrays.asList(wall, space, space, space, space, wall));
		componentMap.add(Arrays.asList(wall, wall, wall, wall, wall, wall));
		
		room3 = new Room(new Point(0,14), componentMap);
	}
	
	private void initializeRoomFour() {
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
	
	private void initializeHallOne() {
		//Hall that can connect room1 to room2
		List<Entity> componentMap = Arrays.asList(space, space, space, space, space, space, space);
		List<Point> waypoints = new ArrayList<Point>();
		waypoints.add(new Point(6,2));
		
		hall1 = new Hall(componentMap, waypoints);
	}
	
	private void initializeHallTwo() {
		//Hall that can connect room2 to room3
		List<Entity> componentMap = Arrays.asList(space, space, space, space, space);
		List<Point> waypoints = new ArrayList<Point>();
		waypoints.add(new Point(2,11));
		
		hall2 = new Hall(componentMap, waypoints);
	}
	
	private void initializeHallThree() {
		//Hall that can connect room2 to room4
		List<Entity> componentMap = Arrays.asList(space, space, space, space);
		List<Point> waypoints = new ArrayList<Point>();
		
		hall3 = new Hall(componentMap, waypoints);
	}
	
	private void initializeDoorsHallOne() {
		//Doors for hall1
		room1.connectHall(new Point(3,2), hall1);
		room2.connectHall(new Point(6,7), hall1);
		hall1.connectRooms(new Point(3,2), room1, new Point(6,7), room2);
	}
	
	private void initializeDoorsHallTwo() {
		//Doors for hall2
		room2.connectHall(new Point(5,11), hall2);
		room3.connectHall(new Point(2,14), hall2);
		hall2.connectRooms(new Point(2,14), room3, new Point(5,11), room2);
	}
	
	
	private void initializeDoorsHallThree() {
		//Doors for hall3
		room2.connectHall(new Point(8,11), hall3);
		room4.connectHall(new Point(13,11), hall3);
		hall3.connectRooms(new Point(8,11), room2, new Point(13,11), room4);
	}
	
	
	//Initialize input for the Dungeon constructor
	private void initializeDungeonInput() {
		//Players 
		Player player1 = new Player();
		Player player2 = new Player();
		Player player3 = new Player();
		this.players = new ArrayList<>(Arrays.asList(player1, player2, player3));
		
		//Adversaries 
		Adversary ghost1 = new Ghost();
		Adversary ghost2 = new Ghost();
		Adversary zombie1 = new Zombie();
		Adversary zombie2 = new Zombie();
		this.adversaries = new ArrayList<>(Arrays.asList(ghost1, ghost2, zombie1, zombie2));
		
		//List of levels in the Dungeon
		Level level1 = new LevelImpl(initializeLevelMapOne(), this.key1, this.exit1);
		Level level2 = new LevelImpl(initializeLevelMapTwo(), this.key2, this.exit2);
		Level level3 = new LevelImpl(initializeLevelMapThree(), this.key3, this.exit3);
		this.levels = new ArrayList<>(Arrays.asList(level1, level2, level3));	
	}
	
	//Test that the constructor throws the corresponding error when too many players are added
	@Test (expected = IllegalArgumentException.class)
	public void testDungeonConstructorExtraPlayers() {
		initializeDungeonInput();
		int currLevel = 1;
		
		//Add new players 
		players.add(new Player());
		players.add(new Player());
		
		new Dungeon(players, adversaries, currLevel, levels);
	}
	
	//Test that the constructor throws the corresponding error when no players are added
	@Test (expected = IllegalArgumentException.class)
	public void testDungeonConstructorNoPlayers() {
		initializeDungeonInput();
		int currLevel = 1;
		
		List<Player> noPlayers = new ArrayList<>();

		new Dungeon(noPlayers, adversaries, currLevel, levels);
	}
	
	//Test that the next level in the Dungeon can be retrieved 
	@Test
	public void getNextLevel() {
		initializeDungeonInput();
		int currLevel = 1;
		
		Dungeon dungeon = new Dungeon(players, adversaries, currLevel, levels);
		
		assertEquals(levels.get(1), dungeon.getNextLevel());
		assertEquals(levels.get(2), dungeon.getNextLevel());
	}

	//Test that this is the last level in the Dungeon
	@Test
	public void isLastLevelTrue() {
		initializeDungeonInput();
		int currLevel = 3;
		
		Dungeon dungeon = new Dungeon(players, adversaries, currLevel, levels);
		
		assertEquals(true, dungeon.isLastLevel());	
		
	}
	
	//Test that this is not the last level in the Dungeon
	@Test
	public void isLastLevelFalse() {
		initializeDungeonInput();
		int currLevel = 1;
		
		Dungeon dungeon = new Dungeon(players, adversaries, currLevel, levels);
		
		assertEquals(false, dungeon.isLastLevel());
		
		dungeon.getNextLevel();
		assertEquals(false, dungeon.isLastLevel());	
	}

}