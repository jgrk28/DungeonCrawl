package Manager;

import Game.model.Actor;
import Game.model.Item;
import Game.modelView.EntityType;
import JSONUtils.Generator;
import java.awt.Point;
import java.util.HashSet;
import java.util.List;

import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import Common.Player;
import Game.modelView.PlayerModelView;

/**
 * Represents a TestPlayer within a game of Snarl. Works
 * with the TestManager to provide input in the corresponding
 * format for the test harness. 
 */
public class TestPlayer implements Player {
	
	//All moves for the player
	private List<Point> moves;
	private JSONArray output;
	private Point location;
	
	//Constructs a TestPlayer with a list of moves and an output
	//JSONArray to append updates to
	public TestPlayer(List<Point> moves, JSONArray output) {
		this.moves = moves;
		this.output = output;
	}

	@Override
	public Point takeTurn(List<Point> validMove) {
		//If the player cannot move again, throw the corresponding error
		if (this.moves.isEmpty()) {
			throw new IllegalStateException("No more moves");
		}
		Point newMove = this.moves.get(0);
		this.moves.remove(0);
		if (newMove == null) {
			return this.location;
		} else {
			return newMove;
		}
	}

	/**
	 * Provides the update for the player in the corresponding JSON format:
	 * {
  	 *  "type": "player-update",
  	 *  "layout": (tile-layout),
  	 *  "position": (point),
  	 *  "objects": (object-list),
  	 *  "actors": (actor-position-list)
	 * }
	 */
	@Override
	public void update(PlayerModelView gameState) {
		//Gather all information for the player
		if (gameState.isPlayerAlive()) {
			List<List<EntityType>> playerMap = gameState.getMap();
			Point absolutePosition = gameState.getPosition();
			this.location = absolutePosition;
			List<Point> visibleDoors = gameState.getVisibleDoors();
			List<Item> visibleItems = gameState.getVisibleItems();
			Map<Actor, Point> visibleActors = gameState.getVisibleActors();

			//Generate the JSON representation of the player's state
			JSONArray layout = generateLayout(playerMap, absolutePosition, visibleDoors);
			JSONArray position = Generator.generateJSONPoint(absolutePosition);
			JSONArray objects = Generator.generateJSONObjects(visibleItems);
			JSONArray actors = Generator.generateJSONActorList(visibleActors);

			//Place in the JSONObject format
			JSONObject playerUpdate = new JSONObject();
			playerUpdate.put("type", "player-update");
			playerUpdate.put("layout", layout);
			playerUpdate.put("position", position);
			playerUpdate.put("objects", objects);
			playerUpdate.put("actors", actors);

			JSONArray traceEntry = new JSONArray();
			traceEntry.put(gameState.getName());
			traceEntry.put(playerUpdate);

			this.output.put(traceEntry);
		}
	}

	/**
	 * Generates the layout of the room that the player is currently located
	 * in, based on the range of their view 
	 * @param playerMap - a matrix of EntityType that the player can view 
	 * @param playerPosition - the player's position
	 * @param doors - the locations of the doors in the room
	 * @return a JSONArray representing the layout of the room
	 */
	private JSONArray generateLayout(List<List<EntityType>> playerMap, Point playerPosition, List<Point> doors) {
		Point topLeftPosition = new Point(playerPosition.x - 2, playerPosition.y - 2);
		JSONArray layoutOutput = new JSONArray();
		Set<Point> relativeDoors = new HashSet<>();
		
		//Get the relative position of the doors
		for (Point point : doors) {
			Point relPoint = new Point(point.x - topLeftPosition.x, point.y - topLeftPosition.y);
			relativeDoors.add(relPoint);
		}
 		
		for (int i = 0; i < playerMap.size(); i++) {
			List<EntityType> entityRow = playerMap.get(i);
			JSONArray JSONRow = new JSONArray();
			for (int j = 0; j < entityRow.size(); j++) {
				EntityType entity = entityRow.get(j);
				
				 //If the entity is empty or a wall, place a 0
				if (entity.equals(EntityType.EMPTY) || entity.equals(EntityType.WALL)) {
					JSONRow.put(0);
				} 
				//If the location is a door, place a 2
				else if (relativeDoors.contains(new Point(j, i))) {
					JSONRow.put(2);
				} 
				//Otherwise, place a 1
				else {
					JSONRow.put(1);
				}
			}
			layoutOutput.put(JSONRow);
		}
		return layoutOutput;
	}

	@Override
	public void displayMessage(String message) {
		// Do Nothing
	}

	@Override
	public void sendLevelStart(int levelIndex, Set<Game.model.Player> levelPlayers) {
		// Do Nothing
	}

	@Override
	public void sendLevelEnd(Set<Game.model.Player> levelPlayers) {
		// Do Nothing
	}

}
