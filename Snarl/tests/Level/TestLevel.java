package Level;

import static Utils.ParseUtils.parsePoint;

import Game.model.Item;
import Game.model.Tile;
import Game.modelView.EntityType;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import Hall.TestHall;
import Room.TestRoom;
import Game.model.Exit;
import Game.model.Hall;
import Game.model.Key;
import Game.model.Level;
import Game.model.LevelComponent;
import Game.model.LevelImpl;
import Game.model.Room;

/**
 * Tests that a level can be generated based on the
 * corresponding JSON input. Returns the information
 * for a given point in the level, including whether 
 * it is traversable, if a key or object is at that point,
 * the type of component the point is located in, and
 * any reachable rooms. 
 */
public class TestLevel {

	//Fields for the level
	private Point point;
	private List<LevelComponent> levelMap;
	private Level level;
	private List<Item> items;

	//Fields for the output
	private boolean traversable;
	private String objectType;
	private String componentType;
	private List<Point> reachable;
	
	/**
	 * Parses the input to create a corresponding testLevel.
	 * Gets the relevant information for a point in the level,
	 * and outputs it in JSON format
	 * @param args - command line arguments
	 */
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

		//The point within the level
		JSONArray JSONPoint = JSONInput.getJSONArray(1);
		this.point = parsePoint(JSONPoint);

		//Parse the JSONLevel
		this.level = parseLevel(JSONLevel);

	}
	
	/**
	 * Parses the given JSON input for a Level. Identifies the
	 * the rooms and hallways in the level, as well as the key and 
	 * exit. Generates the rooms and halls for the level, and returns
	 * a new level.
	 * @param JSONLevel - the JSON object that defines a level
	 * @return the level created based on the provided specifications
	 */
	private Level parseLevel(JSONObject JSONLevel) {
		this.levelMap = parseLevelMap(JSONLevel);
		this.items = parseObjects(JSONLevel);
		
		return new LevelImpl(this.levelMap, this.items);
	}

	/**
	 * Parses the given JSON input for a Level. Identifies the
	 * the rooms and hallways in the level, and puts them into a level map
	 * @param JSONLevel - the JSON object that defines a level
	 * @return the list of level components created based on the provided specifications
	 */
	public static List<LevelComponent> parseLevelMap(JSONObject JSONLevel) {

		List<LevelComponent> map = new ArrayList<>();

		//Array of Rooms
		JSONArray JSONRooms = JSONLevel.getJSONArray("rooms");
		//Array of Halls
		JSONArray JSONHalls = JSONLevel.getJSONArray("hallways");

		//Create the rooms and add them to the map
		for (int i = 0; i < JSONRooms.length(); i++) {
			JSONObject JSONRoom = JSONRooms.getJSONObject(i);
			TestRoom roomParser = new TestRoom();
			Room newRoom = roomParser.parseRoom(JSONRoom);
			map.add(newRoom);
		}

		//Create the halls and add them to the map
		for (int j = 0; j < JSONHalls.length(); j++) {
			JSONObject JSONHall = JSONHalls.getJSONObject(j);
			TestHall hallParser = new TestHall();
			Hall newHall = hallParser.parseHall(JSONHall, map);
			map.add(newHall);
		}

		return map;
	}

	/**
	 * Parses the given JSON input for a Level. Identifies all the
	 * objects for this level.
	 * @param JSONLevel - the JSON object that defines a level
	 * @return a list of all objects (items) found in the list of objects
	 */
	public static List<Item> parseObjects(JSONObject JSONLevel) {
		List<Item> items = new ArrayList<>();
		JSONArray JSONObjects = JSONLevel.getJSONArray("objects");
		for (int i = 0; i < JSONObjects.length(); i++) {
			JSONObject JSONObj = JSONObjects.getJSONObject(i);
			if (JSONObj.getString("type").equals("key")) {
				Point keyLocation = parsePoint(JSONObj.getJSONArray("position"));
				items.add(new Key(keyLocation));
			} else if (JSONObj.getString("type").equals("exit")) {
				Point exitLocation = parsePoint(JSONObj.getJSONArray("position"));
				items.add(new Exit(exitLocation));
			} else {
				throw new IllegalArgumentException("Object type not supported");
			}
		}
		return items;
	}

	/**
	 * Finds the component that the  point is located in. If the point
	 * is not in a component, we return the corresponding values for that 
	 * point. Otherwise, we determine the attributes for the point within 
	 * the given LevelComponent. 
	 */
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

	/**
	 * Determines whether or not the location of the point is traversable
	 * @param currComponent - the component where the point is located
	 * @return true if the location is traversable, false otherwise
	 */
	private boolean isTravesable(LevelComponent currComponent) {
		Tile destTile = currComponent.getDestinationTile(this.point);
		EntityType destEntityType = currComponent.getEntityType(destTile);
		return destEntityType.equals(EntityType.SPACE)
				|| destEntityType.equals(EntityType.HALL_SPACE)
				|| destEntityType.equals(EntityType.KEY)
				|| destEntityType.equals(EntityType.EXIT);
	}

	/**
	 * Determines if a key or exit is located at the given point
	 * @return the "key" or "exit" string if the corresponding
	 * object is at this location, null otherwise
	 */
	private String getObjectType() {
		for (Item item : this.items) {
			if (item.getLocation().equals(this.point)) {
				if (item instanceof Key) {
					return "key";
				} else if (item instanceof Exit) {
					return "exit";
				}
			}
		}
		return null;
	}

	/**
	 * Determines the LevelComponent type of the component that the point
	 * is located in 
	 * @param currComponent - the component where the point is located
	 * @return the "room" or "hallway" string if the component is a room
	 * or hallway
	 * @throws IllegalArgumentException if the LevelComponent type is invalid
	 */
	private String getComponentType(LevelComponent currComponent) {
		if (currComponent instanceof Room) {
			return "room";
		} else if (currComponent instanceof Hall) {
			return "hallway";
		} else {
			throw new IllegalArgumentException("Invalid LevelComponent type");
		}
	}


	/**
	 * Finds all rooms that are immediately reachable from currComponent
	 * @param currComponent - the component where the point is located
	 * @return a list of the origins for all reachable rooms
	 * @throws IllegalArgumentException if the LevelComponent type is invalid
	 */
	private List<Point> getReachableRooms(LevelComponent currComponent) {
		if (currComponent instanceof Room) {
			return getRoomReachableRooms((Room)currComponent);
		} else if (currComponent instanceof Hall) {
			return getHallReachableRooms((Hall)currComponent);
		} else {
			throw new IllegalArgumentException("Invalid LevelComponent type");
		}
	}

	/**
	 * Finds all rooms that are immediately reachable from the given hall
	 * @param hall - the hall used to determine which rooms are reachable
	 * @return a list of the origins for all reachable rooms
	 */
	private List<Point> getHallReachableRooms(Hall hall) {
		List<Point> reachableRooms = new ArrayList<>();
		Room startRoom = hall.getStartRoom();
		Room endRoom = hall.getEndRoom();
		Point startOrigin = startRoom.getTopLeftBound();
		Point endOrigin = endRoom.getTopLeftBound();
		reachableRooms.add(startOrigin);
		reachableRooms.add(endOrigin);
		return reachableRooms;
	}

	/**
	 * Finds all rooms that are immediately reachable from the given room
	 * @param room - the room used to determine which rooms are reachable
	 * @return a list of the origins for all reachable rooms
	 */
	private List<Point> getRoomReachableRooms(Room room) {
		List<Point> reachableRooms = new ArrayList<>();

		Map<Point, Hall> doors = room.getDoors();
		for (Hall hall : doors.values()) {
			Room startRoom = hall.getStartRoom();
			Room endRoom = hall.getEndRoom();

			//Find current room and add the neighbor
			if (startRoom.equals(room)) {
				Point endOrigin = endRoom.getTopLeftBound();
				reachableRooms.add(endOrigin);
			} else if (endRoom.equals(room)) {
				Point startOrigin = startRoom.getTopLeftBound();
				reachableRooms.add(startOrigin);
			}
		}
		return reachableRooms;
	}


	/**
	 * Outputs all relevant information about the provided point
	 * as a JSONObject
	 */
	private void outputPointInfo() {
		JSONObject outputObject = new JSONObject();

		//Convert all points for reachable rooms to JSONArrays
		JSONArray JSONReachable = new JSONArray();
		for (Point reachableRoom : this.reachable) {
			JSONArray JSONPoint = new JSONArray();
			JSONPoint.put(reachableRoom.y);
			JSONPoint.put(reachableRoom.x);
			JSONReachable.put(JSONPoint);
		}

		outputObject.put("traversable", this.traversable);
		if (this.objectType == null) {
			outputObject.put("object", JSONObject.NULL);
		} else {
			outputObject.put("object", this.objectType);
		}
		outputObject.put("type", this.componentType);
		outputObject.put("reachable", JSONReachable);

		System.out.print(outputObject.toString());
	}

}