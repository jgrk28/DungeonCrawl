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


public class TestRoom {
	
	private Room room;
	private JSONArray JSONPoint;
	private Point point;
	private JSONArray JSONOriginPoint;

	public static void main(String[] args) {
		TestRoom roomParser = new TestRoom();
		roomParser.parseInput();
		List<Point> validMoves = roomParser.getValidMoves();
		roomParser.outputMoves(validMoves);
	}
	
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
		
		this.JSONPoint = JSONInput.getJSONArray(1);
		this.point = parsePoint(this.JSONPoint);
		
		this.JSONOriginPoint = JSONRoom.getJSONArray("origin");
		Point origin = parsePoint(this.JSONOriginPoint);
		
		JSONObject JSONBounds = JSONRoom.getJSONObject("bounds");
		int rows = JSONBounds.getInt("rows");
		int columns = JSONBounds.getInt("columns");
		
		JSONArray layout = JSONRoom.getJSONArray("layout");	
		
		this.room = generateRoom(origin, rows, columns, layout);
		
	}
	
	private Point parsePoint(JSONArray JSONPoint) {
		int y = JSONPoint.getInt(0);
		int x = JSONPoint.getInt(1);
		return new Point(x,y);
	}
	
	private Room generateRoom(Point origin, int rows, int columns, JSONArray layout) {
		List<List<Entity>> componentMap = new ArrayList<>();
		Wall wall = new Wall();
		Space space = new Space();
		for (int i = 0; i < rows; i++) {
			JSONArray JSONRow = layout.getJSONArray(i);
			List<Entity> componentRow = new ArrayList<>();
			for (int j = 0; j < columns; j++) {
				int cellValue = JSONRow.getInt(j);
				switch (cellValue) {
				case 0:
					componentRow.add(wall);
				case 1:
					componentRow.add(space);
				case 2:
					componentRow.add(space);
				}
				
			}
			componentMap.add(componentRow);
		}
		return new Room(origin, componentMap);
	}
	
	private List<Point> getValidMoves() {
		//use the point to get all points next to it
		Point upMove = new Point(this.point.x, this.point.y - 1);
		Point downMove = new Point(this.point.x, this.point.y + 1);
		Point rightMove = new Point(this.point.x + 1, this.point.y);
		Point leftMove = new Point(this.point.x - 1, this.point.y);
		
		List<Point> moves = new ArrayList<>(Arrays.asList(upMove, downMove, rightMove, leftMove));
		
		for (Point destination : moves) {
			if (!this.room.inComponent(destination)
				|| !(this.room.getDestinationEntity(destination) instanceof Space)) {
				moves.remove(destination);				
			}
		}
		
		return moves;
	}
	
	private void outputMoves(List<Point> validMoves) {
		JSONArray outputArray = new JSONArray();
		
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
