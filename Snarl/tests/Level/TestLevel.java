package Level;

import java.awt.Point;
import java.util.List;

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
	
	List<LevelComponent> levelMap;
	Level level;

	public static void main(String[] args) {
		TestLevel levelParser = new TestLevel();
		levelParser.parseInput();
	}
	
	private void parseInput() {
		JSONTokener inputTokens = new JSONTokener(System.in);
		
		Object value;
		value = inputTokens.nextValue();
		
		// If the input is not a JSONArray, end the program
		if (!(value instanceof JSONObject)) {
			throw new IllegalArgumentException("Not in valid JSON format");
		}
		
		JSONObject JSONInput = (JSONObject) value;
		
		//Array of Rooms
		JSONArray JSONRooms = JSONInput.getJSONArray("rooms");
		//Array of Halls
		JSONArray JSONHalls = JSONInput.getJSONArray("hallways");
		
		//Objects
		JSONArray JSONObjects = JSONInput.getJSONArray("objects");
		//Key
		JSONObject JSONKey = JSONObjects.getJSONObject(0);
		
		//Key location
		//Might need to worry about the type if the order changes
		Point keyLocation = parsePoint(JSONKey.getJSONArray("position"));
		Key key = new Key(keyLocation);
		
		//Exit
		JSONObject JSONExit = JSONObjects.getJSONObject(1);
		Point exitLocation = parsePoint(JSONExit.getJSONArray("position"));
		Exit exit = new Exit(exitLocation);
		
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
		
		this.level = new LevelImpl(this.levelMap, key, exit);		
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


/*
{ 
"rooms": (room-list),
"hallways": (hall-list),
"objects": [ { "type": "key", "position": (point) }, 
             { "type": "exit", "position": (point) } ]
}
*/