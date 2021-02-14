import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Before;
import org.junit.Test;

import model.*;
import modelView.*;
import view.*;

import static org.junit.Assert.assertEquals;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Create level with random things, level components, connect them together
//Create LevelView and pass in Model as modelView
//Call drawLevel and check if the output reflects the input
//X for wall, . for space, | for door

public class TextualLevelViewTest {
	
	private List<LevelComponent> levelMap;
	private LevelModelView modelView;
	private LevelView view;
	private Entity space = new Space();
	private Entity wall = new Wall();

	LevelComponent room1;
	LevelComponent room2;
	LevelComponent room3;
	LevelComponent room4;

	LevelComponent hall1;
	LevelComponent hall1Snake;
	LevelComponent hall1RoomBrush;
	LevelComponent hall1HallBrush;
	LevelComponent hall2;
	LevelComponent hall3;
	
	private void initializeRoom1() {
		List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
		componentMap.add(Arrays.asList(wall, wall, wall, wall));
		componentMap.add(Arrays.asList(wall, space, space, wall));
		componentMap.add(Arrays.asList(wall, space, space, wall));
		componentMap.add(Arrays.asList(wall, wall, wall, wall));

		room1 = new Room(new Point(0,0), componentMap);
	}
	
	private void initializeRoom2() {
		List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
		componentMap.add(Arrays.asList(wall, wall, wall, wall));
		componentMap.add(Arrays.asList(wall, space, space, wall));
		componentMap.add(Arrays.asList(wall, space, space, wall));
		componentMap.add(Arrays.asList(wall, space, space, wall));
		componentMap.add(Arrays.asList(wall, space, space, wall));
		componentMap.add(Arrays.asList(wall, wall, wall, wall));
		
		room2 = new Room(new Point(5,7), componentMap);
	}
	
	private void initializeRoom3() {
		List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
		componentMap.add(Arrays.asList(wall, wall, wall, wall, wall, wall));
		componentMap.add(Arrays.asList(wall, space, space, space, space, wall));
		componentMap.add(Arrays.asList(wall, space, space, space, space, wall));
		componentMap.add(Arrays.asList(wall, space, space, space, space, wall));
		componentMap.add(Arrays.asList(wall, wall, wall, wall, wall, wall));
		
		room3 = new Room(new Point(0,14), componentMap);
	}
	
	private void initializeRoom4() {
		List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
		componentMap.add(Arrays.asList(wall, wall, wall, wall, wall));
		componentMap.add(Arrays.asList(wall, space, space, space, wall));
		componentMap.add(Arrays.asList(wall, space, space, space, wall));
		componentMap.add(Arrays.asList(wall, space, space, space, wall));
		componentMap.add(Arrays.asList(wall, space, space, space, wall));
		componentMap.add(Arrays.asList(wall, wall, wall, wall, wall));
		
		room4 = new Room(new Point(13,10), componentMap);
	}
	
	private void initializeHall1() {
		// https://piazza.com/class/kjj18tz29a76p2?cid=331
		List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
		componentMap.add(Arrays.asList(wall, wall, wall, wall));
		componentMap.add(Arrays.asList(space, space, space, wall));
		componentMap.add(Arrays.asList(wall, wall, space, wall));
		componentMap.add(Arrays.asList(wall, wall, space, wall));
		componentMap.add(Arrays.asList(wall, wall, space, wall));
		
		// Not entirely sure how we want to define waypoints here
		//After reviewing the examples in Piazza, I think we need walls for hallways
		//Which mean that they must be at least 3 entities wide
		//Using the space where the direction changes here
		List<Point> waypoints = new ArrayList<Point>();
		waypoints.add(new Point(6,3));
		
		hall1 = new Hall(componentMap, waypoints);
	}

	private void initializeHall1Snake() {
		//Not implementing yet in case refactor but basically has waypoints
		//(9,2), (9,4), (5,4).
		List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
		componentMap.add(Arrays.asList(wall, wall, wall, wall));
		componentMap.add(Arrays.asList(space, space, space, wall));
		componentMap.add(Arrays.asList(wall, wall, space, wall));
		componentMap.add(Arrays.asList(wall, wall, space, wall));
		componentMap.add(Arrays.asList(wall, wall, space, wall));

		// Not entirely sure how we want to define waypoints here
		//After reviewing the examples in Piazza, I think we need walls for hallways
		//Which mean that they must be at least 3 entities wide
		//Using the space where the direction changes here
		List<Point> waypoints = new ArrayList<Point>();
		waypoints.add(new Point(6,3));

		hall1Snake = new Hall(componentMap, waypoints);
	}

	private void initializeHall1RoomBrush() {
		//Not implementing yet in case refactor but basically has waypoints
		//(9,2), (9,6), (6,6).
		List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
		componentMap.add(Arrays.asList(wall, wall, wall, wall));
		componentMap.add(Arrays.asList(space, space, space, wall));
		componentMap.add(Arrays.asList(wall, wall, space, wall));
		componentMap.add(Arrays.asList(wall, wall, space, wall));
		componentMap.add(Arrays.asList(wall, wall, space, wall));

		// Not entirely sure how we want to define waypoints here
		//After reviewing the examples in Piazza, I think we need walls for hallways
		//Which mean that they must be at least 3 entities wide
		//Using the space where the direction changes here
		List<Point> waypoints = new ArrayList<Point>();
		waypoints.add(new Point(6,3));

		hall1RoomBrush = new Hall(componentMap, waypoints);
	}

	private void initializeHall1HallBrush() {
		//Not implementing yet in case refactor but basically has waypoints
		//(12,2), (12,10), (9,10), (9,3), (6,3).
		List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
		componentMap.add(Arrays.asList(wall, wall, wall, wall));
		componentMap.add(Arrays.asList(space, space, space, wall));
		componentMap.add(Arrays.asList(wall, wall, space, wall));
		componentMap.add(Arrays.asList(wall, wall, space, wall));
		componentMap.add(Arrays.asList(wall, wall, space, wall));

		// Not entirely sure how we want to define waypoints here
		//After reviewing the examples in Piazza, I think we need walls for hallways
		//Which mean that they must be at least 3 entities wide
		//Using the space where the direction changes here
		List<Point> waypoints = new ArrayList<Point>();
		waypoints.add(new Point(6,3));

		hall1HallBrush = new Hall(componentMap, waypoints);
	}
	
	private void initializeHall2() {
		List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
		componentMap.add(Arrays.asList(wall, wall, wall, wall));
		componentMap.add(Arrays.asList(wall, space, space, space));
		componentMap.add(Arrays.asList(wall, space, wall, wall));
		componentMap.add(Arrays.asList(wall, space, wall));

		List<Point> waypoints = new ArrayList<Point>();
		waypoints.add(new Point(2,11));
		
		hall2 = new Hall(componentMap, waypoints);
	}
	
	private void initializeHall3() {
		List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
		componentMap.add(Arrays.asList(wall, wall, wall, wall));
		componentMap.add(Arrays.asList(space, space, space, space));
		componentMap.add(Arrays.asList(wall, wall, wall, wall));

		List<Point> waypoints = new ArrayList<Point>();
		
		hall3 = new Hall(componentMap, waypoints);
	}

	private void initializeDoorsHall1() {
		//I tried reworking the door class to keep track of the room and hall the door connects
		//It was really confusing creating doors multiple times, especially when a room has
		//more than 1 door

		//Maybe we should think about taking out the door entity. After all you cant hold a door and
		//an actor in the same space right now. Points->Hall map?
		//https://piazza.com/class/kjj18tz29a76p2?cid=370

		//Doors for hall1
		Door room1Door = new Door(room1, hall1);
		//We need to decide if we want to put this in the interface for LevelComponent
		room1.placeDoor(room1Door);
		Door room2Door1 = new Door(room2, hall1);
		room2.placeDoor(room2Door1);
		hall1.placeDoors(new Point(3,3), room1Door, new Point(6,7), room2Door1);
	}

	private void initializeDoorsHall2() {
		//Doors for hall2
		Door room3Door = new Door(room3, hall2);
		room3.placeDoor(room3Door);
		Door room2Door2 = new Door(room2, hall2);
		room2.placeDoor(room2Door2);
		hall2.placeDoors(new Point(2, 14), room3Door, new Point(5, 11), room2Door2);
	}

	private void initializeDoorsHall3() {
		//Doors for hall3
		Door room4Door = new Door(room4, hall3);
		room4.placeDoor(room4Door);
		Door room2Door3 = new Door(room2, hall3);
		room2.placeDoor(room2Door3);
		hall3.placeDoors(new Point(13,11), room4Door, new Point(8,11), room2Door3);
	}

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

	private void initializeLevelMapSmall() {
		initializeDoorsHall3();

		levelMap = new ArrayList<LevelComponent>();

		levelMap.add(room2);
		levelMap.add(room4);
		levelMap.add(hall3);
	}

	private void initializeDoorsHall1Snake() {

		//Doors for hall1Snake
		Door room1Door = new Door(room1, hall1Snake);
		//We need to decide if we want to put this in the interface for LevelComponent
		room1.placeDoor(room1Door);
		Door room2Door1 = new Door(room2, hall1Snake);
		room2.placeDoor(room2Door1);
		hall1Snake.placeDoors(new Point(3,3), room1Door, new Point(6,7), room2Door1);
	}

	private void initializeLevelMapSnake() {
		initializeDoorsHall1Snake();
		initializeDoorsHall2();
		initializeDoorsHall3();

		levelMap = new ArrayList<LevelComponent>();

		levelMap.add(room1);
		levelMap.add(room2);
		levelMap.add(room3);
		levelMap.add(room4);

		levelMap.add(hall1Snake);
		levelMap.add(hall2);
		levelMap.add(hall3);
	}

	private void initializeDoorsHall1RoomBrush() {
		//Doors for hall1RoomBrush
		Door room1Door = new Door(room1, hall1RoomBrush);
		//We need to decide if we want to put this in the interface for LevelComponent
		room1.placeDoor(room1Door);
		Door room2Door1 = new Door(room2, hall1RoomBrush);
		room2.placeDoor(room2Door1);
		hall1RoomBrush.placeDoors(new Point(3,3), room1Door, new Point(6,7), room2Door1);
	}

	private void initializeLevelMapRoomBrush() {

		initializeDoorsHall1RoomBrush();
		initializeDoorsHall2();
		initializeDoorsHall3();

		levelMap = new ArrayList<LevelComponent>();

		levelMap.add(room1);
		levelMap.add(room2);
		levelMap.add(room3);
		levelMap.add(room4);

		levelMap.add(hall1RoomBrush);
		levelMap.add(hall2);
		levelMap.add(hall3);
	}

	private void initializeDoorsHall1HallBrush() {
		//Doors for hall1HallBrush
		Door room1Door = new Door(room1, hall1HallBrush);
		//We need to decide if we want to put this in the interface for LevelComponent
		room1.placeDoor(room1Door);
		Door room2Door1 = new Door(room2, hall1HallBrush);
		room2.placeDoor(room2Door1);
		hall1HallBrush.placeDoors(new Point(3,3), room1Door, new Point(6,7), room2Door1);
	}

	private void initializeLevelMapHallBrush() {

		initializeDoorsHall1HallBrush();
		initializeDoorsHall2();
		initializeDoorsHall3();

		levelMap = new ArrayList<LevelComponent>();

		levelMap.add(room1);
		levelMap.add(room2);
		levelMap.add(room3);
		levelMap.add(room4);

		levelMap.add(hall1HallBrush);
		levelMap.add(hall2);
		levelMap.add(hall3);
	}

	@Before
	public void initLevelComponents() {
		initializeRoom1();
		initializeRoom1();
		initializeRoom2();
		initializeRoom3();
		initializeRoom4();

		initializeHall1();
		initializeHall1Snake();
		initializeHall1RoomBrush();
		initializeHall1HallBrush();
		initializeHall2();
		initializeHall3();
	}

	@Test
	public void testDrawLevel() {
		initializeLevelMap();

		modelView = new LevelImpl(levelMap);
		view = new TextualLevelView(modelView);

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PrintStream print = new PrintStream(output);
		System.setOut(print);

		String expectedOut = ""
				+ "XXXX              \n"
				+ "X..XXXXX          \n"
				+ "X..|...X          \n"
				+ "XXXXXX.X          \n"
				+ "     X.X          \n"
				+ "     X.X          \n"
				+ "     X.X          \n"
				+ "     X|XX         \n"
				+ "     X..X         \n"
				+ "     X..X         \n"
				+ " XXXXX..XXXXXXXXXX\n"
				+ " X...|..|....|...X\n"
				+ " X.XXXXXXXXXXX...X\n"
				+ " X.X         X...X\n"
				+ "XX|XXX       X...X\n"
				+ "X....X       XXXXX\n"
				+ "X....X            \n"
				+ "X....X            \n"
				+ "XXXXXX            \n";
	    
		//Changed this to return a string since it will probably be easier to test that way
		//Still need to add correctly drawn level for the expected value
		//Changed back to STDOUT because its easy enough to test and we will have to do it at
		//some point
		view.drawLevel();

		assertEquals(expectedOut, output.toString());

	}

	//Check drawing when right and top do not line up with 0
	@Test
	public void testDrawLevelSmall() {
		initializeLevelMapSmall();

		modelView = new LevelImpl(levelMap);
		view = new TextualLevelView(modelView);

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PrintStream print = new PrintStream(output);
		System.setOut(print);

		String expectedOut = ""
				+ "XXXX         \n"
				+ "X..X         \n"
				+ "X..X         \n"
				+ "X..XXXXXXXXXX\n"
				+ "X..|....|...X\n"
				+ "XXXXXXXXX...X\n"
				+ "        X...X\n"
				+ "        X...X\n"
				+ "        XXXXX\n";

		view.drawLevel();

		assertEquals(expectedOut, output.toString());

	}

	//Tests drawing with curvy halls
	@Test
	public void testDrawLevelSnake() {
		initializeLevelMapSnake();

		modelView = new LevelImpl(levelMap);
		view = new TextualLevelView(modelView);

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PrintStream print = new PrintStream(output);
		System.setOut(print);

		String expectedOut = ""
				+ "XXXX              \n"
				+ "X..XXXXXXXX       \n"
				+ "X..|......X       \n"
				+ "XXXXXXXXX.X       \n"
				+ "     X....X       \n"
				+ "     X.XXXX       \n"
				+ "     X.X          \n"
				+ "     X|XX         \n"
				+ "     X..X         \n"
				+ "     X..X         \n"
				+ " XXXXX..XXXXXXXXXX\n"
				+ " X...|..|....|...X\n"
				+ " X.XXXXXXXXXXX...X\n"
				+ " X.X         X...X\n"
				+ "XX|XXX       X...X\n"
				+ "X....X       XXXXX\n"
				+ "X....X            \n"
				+ "X....X            \n"
				+ "XXXXXX            \n";

		view.drawLevel();

		assertEquals(expectedOut, output.toString());

	}

	//Tests drawing with hall directly next to a room
	@Test
	public void testDrawLevelRoomBrush() {
		initializeLevelMapRoomBrush();

		modelView = new LevelImpl(levelMap);
		view = new TextualLevelView(modelView);

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PrintStream print = new PrintStream(output);
		System.setOut(print);

		String expectedOut = ""
				+ "XXXX              \n"
				+ "X..XXXXXXXX       \n"
				+ "X..|......X       \n"
				+ "XXXXXXXXX.X       \n"
				+ "        X.X       \n"
				+ "     XXXX.X       \n"
				+ "     X....X       \n"
				+ "     X|XXXX       \n"
				+ "     X..X         \n"
				+ "     X..X         \n"
				+ " XXXXX..XXXXXXXXXX\n"
				+ " X...|..|....|...X\n"
				+ " X.XXXXXXXXXXX...X\n"
				+ " X.X         X...X\n"
				+ "XX|XXX       X...X\n"
				+ "X....X       XXXXX\n"
				+ "X....X            \n"
				+ "X....X            \n"
				+ "XXXXXX            \n";

		view.drawLevel();

		assertEquals(expectedOut, output.toString());

	}

	//Tests drawing with hall directly next to another hall or itself
	@Test
	public void testDrawLevelHallBrush() {
		initializeLevelMapHallBrush();

		modelView = new LevelImpl(levelMap);
		view = new TextualLevelView(modelView);

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PrintStream print = new PrintStream(output);
		System.setOut(print);

		String expectedOut = ""
				+ "XXXX              \n"
				+ "X..XXXXXXXXXXX    \n"
				+ "X..|.........X    \n"
				+ "XXXXXX....XX.X    \n"
				+ "     X.XX.XX.X    \n"
				+ "     X.XX.XX.X    \n"
				+ "     X.XX.XX.X    \n"
				+ "     X|XX.XX.X    \n"
				+ "     X..X.XX.X    \n"
				+ "     X..X.XX.X    \n"
				+ " XXXXX..X....XXXXX\n"
				+ " X...|..|....|...X\n"
				+ " X.XXXXXXXXXXX...X\n"
				+ " X.X         X...X\n"
				+ "XX|XXX       X...X\n"
				+ "X....X       XXXXX\n"
				+ "X....X            \n"
				+ "X....X            \n"
				+ "XXXXXX            \n";

		view.drawLevel();

		assertEquals(expectedOut, output.toString());

	}

	// A level is comprised of a series of rooms connected by hallways.
	// A level is valid if no two rooms overlap, no two hallways overlap,
	// and no hallways overlap with any rooms.

	// We are not making levels like we will in the future so it seems kinda silly to test that now


}
