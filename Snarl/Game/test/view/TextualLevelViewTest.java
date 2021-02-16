package view;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Before;
import org.junit.Test;

import model.*;
import modelView.*;

import static org.junit.Assert.assertEquals;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//X for wall, . for space, * for hallways
public class TextualLevelViewTest {
	
	private List<LevelComponent> levelMap;
	private LevelModelView modelView;
	private LevelView view;
	private Entity space = new Space();
	private Entity wall = new Wall();

	Room room1;
	Room room2;
	Room room3;
	Room room4;

	Hall hall1;
	Hall hall1Snake;
	Hall hall1RoomBrush;
	Hall hall1HallBrush;
	Hall hall2;
	Hall hall3;
	
	private void initializeRoom1() {
		List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
		componentMap.add(Arrays.asList(wall, wall, wall, wall));
		componentMap.add(Arrays.asList(wall, space, space, wall));
		componentMap.add(Arrays.asList(wall, space, space, space));
		componentMap.add(Arrays.asList(wall, wall, wall, wall));

		room1 = new Room(new Point(0,0), componentMap);
	}
	
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
	
	private void initializeRoom3() {
		List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
		componentMap.add(Arrays.asList(wall, wall, space, wall, wall, wall));
		componentMap.add(Arrays.asList(wall, space, space, space, space, wall));
		componentMap.add(Arrays.asList(wall, space, space, space, space, wall));
		componentMap.add(Arrays.asList(wall, space, space, space, space, wall));
		componentMap.add(Arrays.asList(wall, wall, wall, wall, wall, wall));
		
		room3 = new Room(new Point(0,14), componentMap);
	}
	
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
	
	private void initializeHall1() {
		List<Entity> componentMap = Arrays.asList(space, space, space, space, space, space, space);
		List<Point> waypoints = new ArrayList<Point>();
		waypoints.add(new Point(6,2));
		
		hall1 = new Hall(componentMap, waypoints);
	}

	private void initializeHall1Snake() {
		List<Entity> componentMap = Arrays.asList(space, space, space, space, space, space, space,
				space, space, space, space, space, space);
		List<Point> waypoints = new ArrayList<Point>();
		waypoints.add(new Point(9,2));
		waypoints.add(new Point(9,4));
		waypoints.add(new Point(6,4));

		hall1Snake = new Hall(componentMap, waypoints);
	}

	private void initializeHall1RoomBrush() {
		List<Entity> componentMap = Arrays.asList(space, space, space, space, space, space, space,
				space, space, space, space, space, space);
		List<Point> waypoints = new ArrayList<Point>();
		waypoints.add(new Point(9,2));
		waypoints.add(new Point(9,6));
		waypoints.add(new Point(6,6));

		hall1RoomBrush = new Hall(componentMap, waypoints);
	}

	private void initializeHall1HallBrush() {
		//(12,2), (12,10), (9,10), (9,3), (6,3).
		List<Entity> componentMap = Arrays.asList(space, space, space, space, space, space, space,
				space, space, space, space, space, space, space, space, space, space, space, space, space,
				space, space, space, space, space, space, space, space, space, space, space, space, space);
		List<Point> waypoints = new ArrayList<Point>();
		waypoints.add(new Point(12,2));
		waypoints.add(new Point(12,10));
		waypoints.add(new Point(9,10));
		waypoints.add(new Point(9,3));
		waypoints.add(new Point(6,3));

		hall1HallBrush = new Hall(componentMap, waypoints);
	}
	
	private void initializeHall2() {
		List<Entity> componentMap = Arrays.asList(space, space, space, space, space);
		List<Point> waypoints = new ArrayList<Point>();
		waypoints.add(new Point(2,11));
		
		hall2 = new Hall(componentMap, waypoints);
	}
	
	private void initializeHall3() {
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
		room1.connectHall(new Point(4,2), hall1Snake);
		room2.connectHall(new Point(6,6), hall1Snake);
		hall1Snake.connectRooms(new Point(3,2), room1, new Point(6,7), room2);
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
		room1.connectHall(new Point(4,2), hall1RoomBrush);
		room2.connectHall(new Point(6,6), hall1RoomBrush);
		hall1RoomBrush.connectRooms(new Point(3,2), room1, new Point(6,7), room2);
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
		room1.connectHall(new Point(4,2), hall1HallBrush);
		room2.connectHall(new Point(6,6), hall1HallBrush);
		hall1HallBrush.connectRooms(new Point(3,2), room1, new Point(6,7), room2);
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
				+ "X..X              \n"
				+ "X...***           \n"
				+ "XXXX  *           \n"
				+ "      *           \n"
				+ "      *           \n"
				+ "      *           \n"
				+ "     X.XX         \n"
				+ "     X..X         \n"
				+ "     X..X         \n"
				+ "     X..X    XXXXX\n"
				+ "  ***....****....X\n"
				+ "  *  XXXX    X...X\n"
				+ "  *          X...X\n"
				+ "XX.XXX       X...X\n"
				+ "X....X       XXXXX\n"
				+ "X....X            \n"
				+ "X....X            \n"
				+ "XXXXXX            \n";
	    
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
				+ "X.XX         \n"
				+ "X..X         \n"
				+ "X..X         \n"
				+ "X..X    XXXXX\n"
				+ "....****....X\n"
				+ "XXXX    X...X\n"
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
				+ "X..X              \n"
				+ "X...******        \n"
				+ "XXXX     *        \n"
				+ "      ****        \n"
				+ "      *           \n"
				+ "      *           \n"
				+ "     X.XX         \n"
				+ "     X..X         \n"
				+ "     X..X         \n"
				+ "     X..X    XXXXX\n"
				+ "  ***....****....X\n"
				+ "  *  XXXX    X...X\n"
				+ "  *          X...X\n"
				+ "XX.XXX       X...X\n"
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
				+ "X..X              \n"
				+ "X...******        \n"
				+ "XXXX     *        \n"
				+ "         *        \n"
				+ "         *        \n"
				+ "      ****        \n"
				+ "     X.XX         \n"
				+ "     X..X         \n"
				+ "     X..X         \n"
				+ "     X..X    XXXXX\n"
				+ "  ***....****....X\n"
				+ "  *  XXXX    X...X\n"
				+ "  *          X...X\n"
				+ "XX.XXX       X...X\n"
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
				+ "X..X              \n"
				+ "X...*********     \n"
				+ "XXXX  ****  *     \n"
				+ "      *  *  *     \n"
				+ "      *  *  *     \n"
				+ "      *  *  *     \n"
				+ "     X.XX*  *     \n"
				+ "     X..X*  *     \n"
				+ "     X..X*  *     \n"
				+ "     X..X****XXXXX\n"
				+ "  ***....****....X\n"
				+ "  *  XXXX    X...X\n"
				+ "  *          X...X\n"
				+ "XX.XXX       X...X\n"
				+ "X....X       XXXXX\n"
				+ "X....X            \n"
				+ "X....X            \n"
				+ "XXXXXX            \n";

		view.drawLevel();

		assertEquals(expectedOut, output.toString());

	}

}
