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

//Tests for the TextualLevelView class. This class displays a level in ASCII art with
//X for wall, . for space, * for hallways, P for player, G for ghost, and Z for zombie
public class TextualLevelViewTest {
	public static void testDrawLevel(LevelModelView modelView, String expectedOut) {
		//Initialize view
		LevelView view = new TextualLevelView(modelView);

		//Assign output of STDOUT to new Stream
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PrintStream print = new PrintStream(output);
		System.setOut(print);

		//Draw level to STDOUT
		view.drawLevel();

		//Check that level was drawn as expected
		assertEquals(expectedOut, output.toString());
	}
	
	private List<LevelComponent> levelMap;
	private Entity space = new Space();
	private Entity wall = new Wall();
	private Key key;
	private Exit exit;
	private Player player = new Player();
	private Adversary ghost = new Ghost();
	private Adversary zombie = new Zombie();

	private Room room1;
	private Room room2;
	private Room room3;
	private Room room4;

	private Hall hall1;
	private Hall hall1Snake;
	private Hall hall1RoomBrush;
	private Hall hall1HallBrush;
	private Hall hall2;
	private Hall hall3;
	
	//Simple 4x4 room with one space for a possible door
	private void initializeRoom1() {
		List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
		componentMap.add(Arrays.asList(wall, wall, wall, wall));
		componentMap.add(Arrays.asList(wall, player, space, wall));
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
		componentMap.add(Arrays.asList(ghost, zombie, space, space, wall));
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

	//Hall to replace hall1 that makes multiple turns
	private void initializeHall1Snake() {
		List<Entity> componentMap = Arrays.asList(space, space, space, space, space, space, space,
				space, space, space, space, space, space);
		List<Point> waypoints = new ArrayList<Point>();
		waypoints.add(new Point(9,2));
		waypoints.add(new Point(9,4));
		waypoints.add(new Point(6,4));

		hall1Snake = new Hall(componentMap, waypoints);
	}

	//Hall to replace hall1 that travels directly next to a room
	private void initializeHall1RoomBrush() {
		List<Entity> componentMap = Arrays.asList(space, space, space, space, space, space, space,
				space, space, space, space, space, space);
		List<Point> waypoints = new ArrayList<Point>();
		waypoints.add(new Point(9,2));
		waypoints.add(new Point(9,6));
		waypoints.add(new Point(6,6));

		hall1RoomBrush = new Hall(componentMap, waypoints);
	}

	//Hall to replace hall1 that travels directly next to other halls and itself
	//(12,2), (12,10), (9,10), (9,3), (6,3).
	private void initializeHall1HallBrush() {
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

	//Only connects hall3 between room2 and room4
	//This map does not have have a room at the origin (0,0)
	private void initializeLevelMapSmall() {
		initializeDoorsHall3();

		levelMap = new ArrayList<LevelComponent>();

		levelMap.add(room2);
		levelMap.add(room4);
		levelMap.add(hall3);
	}

	//Doors for hall1Snake
	private void initializeDoorsHall1Snake() {
		room1.connectHall(new Point(3,2), hall1Snake);
		room2.connectHall(new Point(6,7), hall1Snake);
		hall1Snake.connectRooms(new Point(3,2), room1, new Point(6,7), room2);
	}

	//Initializes normal level replacing hall1 for hall1Snake
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

	//Doors for hall1RoomBrush
	private void initializeDoorsHall1RoomBrush() {
		room1.connectHall(new Point(3,2), hall1RoomBrush);
		room2.connectHall(new Point(6,7), hall1RoomBrush);
		hall1RoomBrush.connectRooms(new Point(3,2), room1, new Point(6,7), room2);
	}

	//Initializes normal level replacing hall1 for hall1RoomBrush
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

	//Doors for hall1HallBrush
	private void initializeDoorsHall1HallBrush() {
		room1.connectHall(new Point(3,2), hall1HallBrush);
		room2.connectHall(new Point(6,7), hall1HallBrush);
		hall1HallBrush.connectRooms(new Point(3,2), room1, new Point(6,7), room2);
	}

	//Initializes normal level replacing hall1 for hall1HallBrush
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
		initializeHall1Snake();
		initializeHall1RoomBrush();
		initializeHall1HallBrush();
		initializeHall2();
		initializeHall3();
	}

	//Tests drawing a normal room
	@Test
	public void testDrawLevelNormal() {
		initializeLevelMap();

		String expectedOut = ""
				+ "XXXX              \n"
				+ "XP.X              \n"
				+ "X...***           \n"
				+ "XXXX  *           \n"
				+ "      *           \n"
				+ "      *           \n"
				+ "      *           \n"
				+ "     X.XX         \n"
				+ "     X..X         \n"
				+ "     X..X         \n"
				+ "     X..X    XXXXX\n"
				+ "  ***..@.****GZ..X\n"
				+ "  *  XXXX    X...X\n"
				+ "  *          X...X\n"
				+ "XX.XXX       X...X\n"
				+ "X....X       XXXXX\n"
				+ "X....X            \n"
				+ "X...!X            \n"
				+ "XXXXXX            \n";

		testDrawLevel(new LevelImpl(this.levelMap, this.key, this.exit), expectedOut);
	}

	//Check drawing when right and top do not line up with 0
	@Test
	public void testDrawLevelSmall() {
		initializeLevelMapSmall();

		String expectedOut = ""
				+ "X.XX         \n"
				+ "X..X         \n"
				+ "X..X         \n"
				+ "X..X    XXXXX\n"
				+ "..@.****GZ..X\n"
				+ "XXXX    X...X\n"
				+ "        X...X\n"
				+ "        X...X\n"
				+ "        XXXXX\n";

		//Room with key is not in this level
		//TODO cant put in no key
		testDrawLevel(new LevelImpl(this.levelMap, null, this.exit), expectedOut);

	}

	//Tests drawing with curvy halls
	@Test
	public void testDrawLevelSnake() {
		initializeLevelMapSnake();

		String expectedOut = ""
				+ "XXXX              \n"
				+ "XP.X              \n"
				+ "X...******        \n"
				+ "XXXX     *        \n"
				+ "      ****        \n"
				+ "      *           \n"
				+ "      *           \n"
				+ "     X.XX         \n"
				+ "     X..X         \n"
				+ "     X..X         \n"
				+ "     X..X    XXXXX\n"
				+ "  ***..@.****GZ..X\n"
				+ "  *  XXXX    X...X\n"
				+ "  *          X...X\n"
				+ "XX.XXX       X...X\n"
				+ "X....X       XXXXX\n"
				+ "X....X            \n"
				+ "X...!X            \n"
				+ "XXXXXX            \n";

		testDrawLevel(new LevelImpl(this.levelMap, this.key, this.exit), expectedOut);
	}

	//Tests drawing with hall directly next to a room
	@Test
	public void testDrawLevelRoomBrush() {
		initializeLevelMapRoomBrush();

		String expectedOut = ""
				+ "XXXX              \n"
				+ "XP.X              \n"
				+ "X...******        \n"
				+ "XXXX     *        \n"
				+ "         *        \n"
				+ "         *        \n"
				+ "      ****        \n"
				+ "     X.XX         \n"
				+ "     X..X         \n"
				+ "     X..X         \n"
				+ "     X..X    XXXXX\n"
				+ "  ***..@.****GZ..X\n"
				+ "  *  XXXX    X...X\n"
				+ "  *          X...X\n"
				+ "XX.XXX       X...X\n"
				+ "X....X       XXXXX\n"
				+ "X....X            \n"
				+ "X...!X            \n"
				+ "XXXXXX            \n";

		testDrawLevel(new LevelImpl(this.levelMap, this.key, this.exit), expectedOut);
	}

	//Tests drawing with hall directly next to another hall or itself
	@Test
	public void testDrawLevelHallBrush() {
		initializeLevelMapHallBrush();
		
		String expectedOut = ""
				+ "XXXX              \n"
				+ "XP.X              \n"
				+ "X...*********     \n"
				+ "XXXX  ****  *     \n"
				+ "      *  *  *     \n"
				+ "      *  *  *     \n"
				+ "      *  *  *     \n"
				+ "     X.XX*  *     \n"
				+ "     X..X*  *     \n"
				+ "     X..X*  *     \n"
				+ "     X..X****XXXXX\n"
				+ "  ***..@.****GZ..X\n"
				+ "  *  XXXX    X...X\n"
				+ "  *          X...X\n"
				+ "XX.XXX       X...X\n"
				+ "X....X       XXXXX\n"
				+ "X....X            \n"
				+ "X...!X            \n"
				+ "XXXXXX            \n";

		testDrawLevel(new LevelImpl(this.levelMap, this.key, this.exit), expectedOut);
	}

}
