package Game.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is used to generate a level map
 * for testing
 */
public class LevelMap {
	
	private List<LevelComponent> levelMap;
	private Entity space = new Space();
	private Entity wall = new Wall();

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
	public List<LevelComponent> initializeLevelMap() {
	  initializeRoom1();
	  initializeRoom1();
	  initializeRoom2();
  	  initializeRoom3();
	  initializeRoom4();

	  initializeHall1();
	  initializeHall2();
	  initializeHall3();
		    
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
	    
	  return levelMap;
	}
}
