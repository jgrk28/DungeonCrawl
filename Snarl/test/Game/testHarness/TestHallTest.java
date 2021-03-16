package Game.testHarness;

import static org.junit.Assert.assertEquals;

import Game.model.Tile;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Before;
import org.junit.Test;

import Hall.TestHall;
import Game.model.Hall;
import Game.model.LevelComponent;
import Game.model.Room;
import Game.model.Space;
import Game.model.Wall;

//Tests that the methods for TestHall work as expected
public class TestHallTest {
	
	Tile space = new Space();
	Tile wall = new Wall();
	List<LevelComponent> levelMap;
	Room room1;
	Room room2;
	Room room3;
	
	//Create the first room
	public Room createRoom1() {
		List<Tile> row1 = new ArrayList<>(Arrays.asList(wall, wall, space, wall));
		List<Tile> row2 = new ArrayList<>(Arrays.asList(wall, space, space, wall));
		List<Tile> row3 = new ArrayList<>(Arrays.asList(wall, space, space, wall));
		List<Tile> row4 = new ArrayList<>(Arrays.asList(wall, space, wall, wall));
		List<List<Tile>> componentMap = new ArrayList<>(Arrays.asList(row1, row2, row3, row4));
		
		return new Room(new Point(1,3), componentMap);
	}
	
	//Create the second room
	public Room createRoom2() {
		List<Tile> row1 = new ArrayList<>(Arrays.asList(wall, wall, wall, wall, wall));
		List<Tile> row2 = new ArrayList<>(Arrays.asList(wall, space, space, space, wall));
		List<Tile> row3 = new ArrayList<>(Arrays.asList(space, space, space, space, wall));
		List<Tile> row4 = new ArrayList<>(Arrays.asList(wall, space, space, space, wall));
		List<Tile> row5 = new ArrayList<>(Arrays.asList(wall, wall, wall, wall, wall));
		List<List<Tile>> componentMap = new ArrayList<>(Arrays.asList(row1, row2, row3, row4, row5));
		
		return new Room(new Point(5,10), componentMap);	
	}
	
	//Create the third room
	public Room createRoom3() {
		List<Tile> row1 = new ArrayList<>(Arrays.asList(wall, wall, space, wall, wall));
		List<Tile> row2 = new ArrayList<>(Arrays.asList(wall, space, space, space, wall));
		List<Tile> row3 = new ArrayList<>(Arrays.asList(wall, space, space, space, wall));
		List<Tile> row4 = new ArrayList<>(Arrays.asList(wall, space, space, space, wall));
		List<Tile> row5 = new ArrayList<>(Arrays.asList(wall, wall, wall, wall, wall));
		List<List<Tile>> componentMap = new ArrayList<>(Arrays.asList(row1, row2, row3, row4, row5));
		
		return new Room(new Point(14,4), componentMap);
	}
	
	//Create the level map
	@Before
	public void createLevelMap() {
		this.room1 = createRoom1();
		this.room2 = createRoom2();
		this.room3 = createRoom3();
		this.levelMap = new ArrayList<>(Arrays.asList(room1, room2, room3));
	}
	
	
	//Tests the generateHall method
	@Test
	public void testGenerateHall1() {
		String roomInput = "{ \"type\": \"hallway\",\n"
				+ "           \"from\": [ 3, 3 ],\n"
				+ "           \"to\": [ 4, 16 ],\n"
				+ "           \"waypoints\": [ [ 1, 3 ], [ 1, 16 ] ] }";
		
		JSONTokener inputTokens = new JSONTokener(roomInput);
		Object value = inputTokens.nextValue();
		JSONObject JSONInput = (JSONObject) value;
		
		TestHall testHall = new TestHall();
		Hall hall = testHall.parseHall(JSONInput, this.levelMap);
		
		List<Point> waypoints = new ArrayList<>(Arrays.asList(new Point(3,1), new Point(16,1)));
		
		Hall expectedHall = new Hall(new Point(3,3), this.room1, new Point(16,4), this.room3, waypoints);
		
		assertEquals(expectedHall, hall);
	}
	
	//Tests the generateHall method
	@Test
	public void testGenerateHall2() {
		String roomInput = "{ \"type\": \"hallway\",\n"
				+ "           \"from\": [ 6, 2 ],\n"
				+ "           \"to\": [ 12, 5 ],\n"
				+ "           \"waypoints\": [ [ 12, 2 ] ] }";
		
		JSONTokener inputTokens = new JSONTokener(roomInput);
		Object value = inputTokens.nextValue();
		JSONObject JSONInput = (JSONObject) value;
		
		TestHall testHall = new TestHall();
		Hall hall = testHall.parseHall(JSONInput, this.levelMap);
		
		List<Point> waypoints = new ArrayList<>(Arrays.asList(new Point(2,12)));
		
		Hall expectedHall = new Hall(new Point(2,6), this.room1, new Point(5,12), this.room2, waypoints);
		
		assertEquals(expectedHall, hall);
	}	

}
