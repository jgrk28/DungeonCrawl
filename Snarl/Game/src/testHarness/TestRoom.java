package testHarness;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import model.Room;
import model.Space;
import model.Wall;
import model.Entity;

/**
 * Tests that rooms can be created based on the 
 * corresponding JSON input, and returns the 
 * traversable tiles adjacent to a given point in
 * the room
 */
public class TestRoom {
	
	private Room room;
	private JSONArray JSONPoint;
	private Point point;
	private JSONArray JSONOriginPoint;

	/**
	 * Parse the input to create a corresponding testRoom.
	 * Identify valid moves from a given point in this room,
	 * and output in JSON format
	 * @param args - command line arguments
	 */
	public static void main(String[] args) {
		TestRoom roomParser = new TestRoom();
		roomParser.parseInput();
		List<Point> validMoves = roomParser.getValidMoves();
		roomParser.outputMoves(validMoves);
	}
	
	/**
	 * Parses the JSON input from STDIN. Identifies the
	 * room layout, origin, bounds, and point. Creates
	 * the room based on these specifications
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
		JSONObject JSONRoom = JSONInput.getJSONObject(0);
		
		//The point used to identify valid moves
		this.JSONPoint = JSONInput.getJSONArray(1);
		this.point = parsePoint(this.JSONPoint);
		
		//Top-left corner of the room
		this.JSONOriginPoint = JSONRoom.getJSONArray("origin");
		Point origin = parsePoint(this.JSONOriginPoint);
		
		//Bounds of the room
		JSONObject JSONBounds = JSONRoom.getJSONObject("bounds");
		int rows = JSONBounds.getInt("rows");
		int columns = JSONBounds.getInt("columns");
		
		//Layout of the room
		JSONArray layout = JSONRoom.getJSONArray("layout");	
		
		this.room = generateRoom(origin, rows, columns, layout);
		
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
	
	/**
	 * Creates a new room with the corresponding number of rows and columns,
	 * with the top-left corner at the origin
	 * @param origin - point of the top left corner of the room in a 2D grid representation of the level
	 * @param rows - number of rows in the layout
	 * @param columns - number of columns in the layout
	 * @param layout - an array of arrays of Integers that represents the layout of the room
	 * @return a Room that meets these specifications
	 */
	private Room generateRoom(Point origin, int rows, int columns, JSONArray layout) {
		List<List<Entity>> componentMap = new ArrayList<>();
		Wall wall = new Wall();
		Space space = new Space();
		
		//Create a componentMap based on the layout of the room
		for (int i = 0; i < rows; i++) {
			JSONArray JSONRow = layout.getJSONArray(i);
			List<Entity> componentRow = new ArrayList<>();
			for (int j = 0; j < columns; j++) {
				int cellValue = JSONRow.getInt(j);
				switch (cellValue) {
				//Wall
				case 0:
					componentRow.add(wall);
					break;
				//Traversable Space
				case 1:
					componentRow.add(space);
					break;
				//Traversable Door tile
				case 2:
					componentRow.add(space);
					break;
				default:
					throw new IllegalArgumentException("Invalid entity type in JSON");
				}
				
			}
			componentMap.add(componentRow);
		}
		//Create a new room with the origin and componentMap
		return new Room(origin, componentMap);
	}
	
	/**
	 * Gets a list of valid moves for the point
	 * @return a list of points that are valid moves within the room
	 */
	private List<Point> getValidMoves() {
		//Use the point to get all points adjacent to it
		Point upMove = new Point(this.point.x, this.point.y - 1);
		Point leftMove = new Point(this.point.x - 1, this.point.y);
		Point rightMove = new Point(this.point.x + 1, this.point.y);
		Point downMove = new Point(this.point.x, this.point.y + 1);
		
		List<Point> moves = new ArrayList<>(Arrays.asList(upMove, leftMove, rightMove, downMove));
		List<Point> invalidMoves = new ArrayList<>();
		
		for (Point destination : moves) {
			//If the destination is not in the component, or is not a space, the move is invalid
			if (!this.room.inComponent(destination)
				|| !(this.room.getDestinationEntity(destination) instanceof Space)) {
				invalidMoves.add(destination);				
			}
		}
		
		//Remove all invalid moves from the list
		moves.removeAll(invalidMoves);
		
		return moves;
	}
	
	/**
	 * Outputs all valid moves in JSON format
	 * @param validMoves - the list of all valid moves for the given room and point
	 */
	private void outputMoves(List<Point> validMoves) {
		JSONArray outputArray = new JSONArray();
		
		//If the point exists in the Room, output all traversable points
		//Otherwise, output the corresponding failure message
		if (this.room.inComponent(this.point)) {
			outputArray.put("Success: Traversable points from ");
			outputArray.put(JSONPoint);
			outputArray.put(" in room at ");
			outputArray.put(JSONOriginPoint);
			outputArray.put(" are ");
			
			JSONArray JSONValidMoves = new JSONArray();
			for (Point move : validMoves) {
				JSONArray JSONMove = new JSONArray();
				JSONMove.put(move.y);
				JSONMove.put(move.x);
				JSONValidMoves.put(JSONMove);
			}
			
			outputArray.put(JSONValidMoves);
			
		} else {
			outputArray.put("Failure: Point ");
			outputArray.put(JSONPoint);
			outputArray.put(" is not in room at ");
			outputArray.put(JSONOriginPoint);			
		}	
		
		System.out.print(outputArray.toString());
	}
	
}
