package Room;

import static Utils.ParseUtils.parsePoint;

import Game.model.Tile;
import Game.modelView.EntityType;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import Game.model.Room;
import Game.model.Space;
import Game.model.Wall;

/**
 * Tests that rooms can be created based on the 
 * corresponding JSON input, and returns the 
 * traversable tiles adjacent to a given point
 */
public class TestRoom {
	
	private Room room;
	private JSONArray JSONPoint;
	private Point point;
	private JSONArray JSONOriginPoint;

	/**
	 * Parses the input to create a corresponding testRoom.
	 * Identifies valid moves from a given point in this room,
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
	 * Parses the JSON input from STDIN. Creates
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
		
		//Parse the JSON for the room
		this.room = parseRoom(JSONRoom);		
	}
	
	/**
	 * Parses the given JSON input for a Room. Identifies the
	 * room layout, origin, bounds, and point. Generates and
	 * returns the created room
	 * @param JSONRoom - the JSON object that defines a room
	 * @return the room created based on the provided specifications
	 */
	public Room parseRoom(JSONObject JSONRoom) {
		//Top-left corner of the room
		this.JSONOriginPoint = JSONRoom.getJSONArray("origin");
		Point origin = parsePoint(this.JSONOriginPoint);
		
		//Bounds of the room
		JSONObject JSONBounds = JSONRoom.getJSONObject("bounds");
		int rows = JSONBounds.getInt("rows");
		int columns = JSONBounds.getInt("columns");
		
		//Layout of the room
		JSONArray layout = JSONRoom.getJSONArray("layout");	
		
		return generateRoom(origin, rows, columns, layout);
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
		List<List<Tile>> componentMap = new ArrayList<>();
		Tile wall = new Wall();
		
		//Create a componentMap based on the layout of the room
		for (int i = 0; i < rows; i++) {
			JSONArray JSONRow = layout.getJSONArray(i);
			List<Tile> componentRow = new ArrayList<>();
			for (int j = 0; j < columns; j++) {
				int cellValue = JSONRow.getInt(j);
				switch (cellValue) {
				//Wall
				case 0:
					componentRow.add(wall);
					break;
				//Traversable Space
				case 1:
					componentRow.add(new Space());
					break;
				//Traversable Door tile
				case 2:
					componentRow.add(new Space());
					break;
				default:
					throw new IllegalArgumentException("Invalid entity type in JSON");
				}
				
			}
			componentMap.add(componentRow);
		}
		//Return a new room with the origin and componentMap
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
			if (!this.room.inComponent(destination)) {
				invalidMoves.add(destination);
			} else {
				Tile destTile = this.room.getDestinationTile(destination);
				EntityType destEntityType = this.room.getEntityType(destTile);
				if (!(destEntityType.equals(EntityType.SPACE)
						|| destEntityType.equals(EntityType.KEY)
						|| destEntityType.equals(EntityType.EXIT))) {
					invalidMoves.add(destination);
				}
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
