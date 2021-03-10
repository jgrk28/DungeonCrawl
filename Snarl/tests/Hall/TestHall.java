package Hall;

import static Utils.ParseUtils.parsePoint;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import model.Hall;
import model.LevelComponent;
import model.Room;

/**
 * Tests that halls can be created based on the 
 * corresponding JSON input, and returns a Hall
 * that meets these specifications
 */
public class TestHall {
	
	/**
	 * Parses the given JSON input for a hall. Identifies the 
	 * fromPoint, toPoint, and waypoints. Generates and returns 
	 * the created hall.
	 * @param JSONHall - the JSONObject that defines a hall
	 * @param currLevelMap - the list of LevelComponents currently in the levelMap
	 * @return the hall created based on the provided specifications 
	 */
	public Hall parseHall(JSONObject JSONHall, List<LevelComponent> currLevelMap) {
		
		//From point
		Point fromPoint = parsePoint(JSONHall.getJSONArray("from"));
		
		//To point
		Point toPoint = parsePoint(JSONHall.getJSONArray("to"));
		
		//Waypoints
		JSONArray JSONWaypoints = JSONHall.getJSONArray("waypoints");
		
		//Convert the JSONArray of waypoints to Points
		List<Point> waypoints = new ArrayList<>();
		for (int i = 0; i < JSONWaypoints.length(); i++) {
			JSONArray JSONPoint = JSONWaypoints.getJSONArray(i);
			waypoints.add(parsePoint(JSONPoint));
		}
		
		return generateHall(fromPoint, toPoint, waypoints, currLevelMap);
		
	}
	
	/**
	 * Creates a new hall with the corresponding fromPoint, toPoint, and waypoints
	 * @param fromPoint - the door tile on the boundary of the starting room
	 * @param toPoint - the door tile on the boundary of the ending room
	 * @param waypoints - the list of points that define direction changes between
	 * the to and from points
	 * @param currLevelMap - the list of LevelComponents currently in the levelMap
	 * @return a Hall that meets these specifications
	 */
	private Hall generateHall(Point fromPoint, Point toPoint, List<Point> waypoints, List<LevelComponent> currLevelMap) {
		Room fromRoom = findRoom(fromPoint, currLevelMap);
		Room toRoom = findRoom(toPoint, currLevelMap);
		return new Hall(fromPoint, fromRoom, toPoint, toRoom, waypoints);
		
	}
	
	/**
	 * Finds the room that the from or to point is located in
	 * @param point - the from or to point
	 * @param currLevelMap - the list of LevelComponents currently in the levelMap
	 * @return the room that the point is located in
	 * @throws IllegalArgumentException if the point is not located in a room
	 */
	private Room findRoom(Point point, List<LevelComponent> currLevelMap) {
		for (LevelComponent component : currLevelMap) {
			if (!(component instanceof Room)) {
				continue;
			}
			else if (component.inComponent(point)) {
				return (Room) component;
			}
		}
		throw new IllegalArgumentException("Point is not anywhere within the level");
	}
}
