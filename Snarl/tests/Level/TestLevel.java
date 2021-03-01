package Level;

import com.sun.codemodel.internal.JCase;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import java.util.ListResourceBundle;
import java.util.Map;
import model.Entity;
import model.Space;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import Hall.TestHall;
import Room.TestRoom;
import model.Exit;
import model.Hall;
import model.Key;
import model.Level;
import model.LevelComponent;
import model.LevelImpl;
import model.Room;

public class TestLevel {

	private Point point;
	private List<LevelComponent> levelMap;
	private Level level;
	private boolean traversable;
	private String objectType;
	private String componentType;
	private List<Point> reachable;
	private Key key;
	private Exit exit;

	public static void main(String[] args) {
		TestLevel levelParser = new TestLevel();
		levelParser.parseInput();
		levelParser.getPointInfo();
		levelParser.outputPointInfo();
	}

	/**
	 * Parses the JSON input from STDIN. Creates the
	 * level and the point.
	 */
	private void parseInput() {
		JSONTokener inputTokens = new JSONTokener(System.in);

		Object value;
		value = inputTokens.nextValue();

		// If the input is not a JSONArray, end the program
		if (!(value instanceof JSONArray)) {
			throw new IllegalArgumentException("Not in valid JSON format");
		}

		JSONArray JSONInput = (JSONArray) value;
		JSONObject JSONLevel = JSONInput.getJSONObject(0);

		//The point used to identify valid moves
		JSONArray JSONPoint = JSONInput.getJSONArray(1);
		this.point = parsePoint(JSONPoint);

		this.level = parseLevel(JSONLevel);

	}
	
	private Level parseLevel(JSONObject JSONLevel) {
		//Array of Rooms
		JSONArray JSONRooms = JSONLevel.getJSONArray("rooms");
		//Array of Halls
		JSONArray JSONHalls = JSONLevel.getJSONArray("hallways");
		
		//Objects
		JSONArray JSONObjects = JSONLevel.getJSONArray("objects");
		//Key
		JSONObject JSONKey = JSONObjects.getJSONObject(0);
		
		//Key location
		//Might need to worry about the type if the order changes
		Point keyLocation = parsePoint(JSONKey.getJSONArray("position"));
		this.key = new Key(keyLocation);
		
		//Exit
		JSONObject JSONExit = JSONObjects.getJSONObject(1);
		Point exitLocation = parsePoint(JSONExit.getJSONArray("position"));
		this.exit = new Exit(exitLocation);
		
		for (int i = 0; i < JSONRooms.length(); i++) {
			JSONObject JSONRoom = JSONRooms.getJSONObject(i);
			TestRoom roomParser = new TestRoom();
			Room newRoom = roomParser.parseRoom(JSONRoom);
			this.levelMap.add(newRoom);
		}
		
		for (int j = 0; j < JSONHalls.length(); j++) {
			JSONObject JSONHall = JSONHalls.getJSONObject(j);
			TestHall hallParser = new TestHall();
			Hall newHall = hallParser.parseHall(JSONHall, this.levelMap);
			this.levelMap.add(newHall);
		}
		
		return new LevelImpl(this.levelMap, key, exit);
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

	private void getPointInfo() {
		try {
			LevelComponent currComponent = this.level.findComponent(this.point);
			this.traversable = isTravesable(currComponent);
			this.objectType = getObjectType();
			this.componentType = getComponentType(currComponent);
			this.reachable = getReachableRooms(currComponent);
		} catch (IllegalArgumentException e) {
			this.traversable = false;
			this.objectType = null;
			this.componentType = "void";
			this.reachable = new ArrayList<>();
		}

	}

	private boolean isTravesable(LevelComponent currComponent) {
		Entity destEntity = currComponent.getDestinationEntity(this.point);
		return destEntity.equals(new Space());
	}

	private String getObjectType() {
		if (this.point.equals(key.location)) {
			return "key";
		} else if (this.point.equals(exit.location)) {
			return "exit";
		} else {
			return null;
		}
	}

	private String getComponentType(LevelComponent currComponent) {
		if (currComponent instanceof Room) {
			return "room";
		} else if (currComponent instanceof Hall) {
			return "hallway";
		} else {
			throw new IllegalArgumentException("Invalid LevelComponent type");
		}
	}


	private List<Point> getReachableRooms(LevelComponent currComponent) {
		if (currComponent instanceof Room) {
			return getRoomReachableRooms((Room)currComponent);
		} else if (currComponent instanceof Hall) {
			return getHallReachableRooms((Hall)currComponent);
		} else {
			throw new IllegalArgumentException("Invalid LevelComponent type");
		}
	}

	private List<Point> getHallReachableRooms(Hall hall) {
		List<Point> reachableRooms = new ArrayList<>();
		Room startRoom = hall.getStartRoom();
		Room endRoom = hall.getEndRoom();
		Point startOrigin = startRoom.getOrigin();
		Point endOrigin = endRoom.getOrigin();
		reachableRooms.add(startOrigin);
		reachableRooms.add(endOrigin);
		return reachableRooms;
	}

	private List<Point> getRoomReachableRooms(Room room) {
		List<Point> reachableRooms = new ArrayList<>();

		Map<Point, Hall> doors = room.getDoors();
		for (Hall hall : doors.values()) {
			Room startRoom = hall.getStartRoom();
			Room endRoom = hall.getStartRoom();
			if (!startRoom.equals(room)) {
				Point startOrigin = startRoom.getOrigin();
				reachableRooms.add(startOrigin);
			}
			if (!endRoom.equals(room)) {
				Point endOrigin = endRoom.getOrigin();
				reachableRooms.add(endOrigin);
			}
		}
		return reachableRooms;
	}

	/**
	 * TODO add comment
	 */
	private void outputPointInfo() {
		JSONObject outputObject = new JSONObject();

		JSONArray JSONReachable = new JSONArray();
		for (Point reachableRoom : this.reachable) {
			JSONArray JSONPoint = new JSONArray();
			JSONPoint.put(reachableRoom.y);
			JSONPoint.put(reachableRoom.x);
			JSONReachable.put(JSONPoint);
		}

		outputObject.put("traversable", this.traversable);
		outputObject.put("object", this.objectType);
		outputObject.put("type", this.componentType);
		outputObject.put("reachable", JSONReachable);

		System.out.print(outputObject.toString());
	}

}