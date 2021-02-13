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
	
	private void initializeDoors() {
		//I tried reworking the door class to keep track of the room and hall the door connects
		//It was really confusing creating doors multiple times, especially when a room has 
		//more than 1 door
		
		//Doors for hall1
		Door room1Door = new Door(room1, hall1);
		//We need to decide if we want to put this in the interface for LevelComponent
		room1.placeDoor(room1Door);		
		Door room2Door1 = new Door(room2, hall1);
		room2.placeDoor(room2Door1);
		hall1.placeDoors(new Point(3,3), room1Door, new Point(6,7), room2Door1);
		
		//Doors for hall2
		Door room3Door = new Door(room3, hall2);
		room3.placeDoor(room3Door);
		Door room2Door2 = new Door(room2, hall2);
		room2.placeDoor(room2Door2);
		hall1.placeDoors(new Point(2,14), room3Door, new Point(5,11), room2Door2);
		
		//Doors for hall3
		Door room4Door = new Door(room4, hall3);
		room4.placeDoor(room4Door);
		Door room2Door3 = new Door(room2, hall3);
		room2.placeDoor(room2Door3);
		hall1.placeDoors(new Point(13,11), room4Door, new Point(8,11), room2Door3);
		
	}

	private void initializeLevelMap() {
		initializeRoom1();
		initializeRoom2();
		initializeRoom3();
		initializeRoom4();
		
		initializeHall1();
		initializeHall2();
		initializeHall3();
		
		initializeDoors();
		
		levelMap = new ArrayList<LevelComponent>();
		
		levelMap.add(room1);
		levelMap.add(room2);
		levelMap.add(room3);
		levelMap.add(room4);
		
		levelMap.add(hall1);
		levelMap.add(hall2);
		levelMap.add(hall3);
	}
	
	  @Test
	  public void testDrawLevel() {
	    initializeLevelMap();
	    
		modelView = new LevelImpl(levelMap);
		view = new TextualLevelView(modelView);
	    
	    //Changed this to return a string since it will probably be easier to test that way
		//Still need to add correctly drawn level for the expected value
	    String level = view.drawLevel();
	    assertEquals(level, "");

	  }

}
