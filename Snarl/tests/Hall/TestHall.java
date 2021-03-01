package Hall;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import model.Hall;
import model.LevelComponent;
import model.Room;

public class TestHall {
	
	public Hall parseHall(JSONObject JSONHall, List<LevelComponent> currLevelMap) {
		
		//From point
		Point fromPoint = parsePoint(JSONHall.getJSONArray("from"));
		
		//To point
		Point toPoint = parsePoint(JSONHall.getJSONArray("to"));
		
		//Waypoints
		JSONArray JSONWaypoints = JSONHall.getJSONArray("waypoints");
		
		List<Point> waypoints = new ArrayList<>();
		
		for (int i = 0; i < JSONWaypoints.length(); i++) {
			JSONArray JSONPoint = JSONWaypoints.getJSONArray(i);
			waypoints.add(parsePoint(JSONPoint));
		}
		
		return generateHall(fromPoint, toPoint, waypoints, currLevelMap);
		
	}
	
	private Hall generateHall(Point fromPoint, Point toPoint, List<Point> waypoints, List<LevelComponent> currLevelMap) {
		Room fromRoom = findRoom(fromPoint, currLevelMap);
		Room toRoom = findRoom(toPoint, currLevelMap);
		return new Hall(fromPoint, fromRoom, toPoint, toRoom, waypoints);
		
	}
	
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

	/**
	 * Converts the JSONArray values for the location of a cell to a Point
	 * @param JSONPoint - the JSONArray of [row, column] values
	 * @return the Point representation of the JSONPoint, (column, row)
	 */
	private Point parsePoint(JSONArray JSONPoint) {
		int x = JSONPoint.getInt(1);
		int y = JSONPoint.getInt(0);
		return new Point(x,y);
	}

}
