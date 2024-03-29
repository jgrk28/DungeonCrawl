package Player;

import java.awt.Point;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import Common.Player;
import Game.model.Actor;
import Game.model.Item;
import Game.modelView.EntityType;
import Game.modelView.PlayerModelView;
import JSONUtils.Generator;
import Remote.Server;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Represents a remote player on the server side. Generates
 * the corresponding JSON messages for this player that are
 * sent to the client
 */
public class RemotePlayer implements Player {
	
	Server server;
	String name;
	
	/**
	 * Creates a remote player with the server and a name
	 * @param server - the server this player is communicating through
	 * @param name - the name of this player
	 */
	public RemotePlayer(Server server, String name) {
		this.server = server;
		this.name = name;
	}

	@Override
	public Point takeTurn(List<Point> validMove) {
		//Prompts the player to move and receives the 
		//corresponding move from the client
		this.server.sendMessage(this.name, "move");
		Object value = server.receiveMessage(this.name);
		
		if (!(value instanceof JSONObject)) {
			throw new IllegalArgumentException("Invalid move JSON");
		}
		
		JSONObject JSONMove = (JSONObject)value;
		Object move = JSONMove.get("to");
		
		if (move == null) {
			return null;
		} else {
			//Return the Point for the provided move
			return Utils.ParseUtils.parsePoint((JSONArray)move);
		}
	}

	@Override
	public void update(PlayerModelView gameState, String message) {
		//If the player is still active in the level, send the corresponding update
		if (gameState.isPlayerAlive()) {
			JSONObject playerUpdate = new JSONObject();
			List<List<EntityType>> playerMap = gameState.getMap();
			Point absolutePosition = gameState.getPosition();
			List<Point> visibleDoors = gameState.getVisibleDoors();
			List<Item> visibleItems = gameState.getVisibleItems();
			Map<Actor, Point> visibleActors = gameState.getVisibleActors();
			int currentHealth = gameState.getCurrentHealth();
			int maxHealth = gameState.getMaxHealth();

			//Generate the JSON representation of the player's state
			JSONArray layout = generateLayout(playerMap, absolutePosition, visibleDoors);
			JSONArray position = Generator.generateJSONPoint(absolutePosition);
			JSONArray objects = Generator.generateJSONObjects(visibleItems);
			JSONArray actors = Generator.generateJSONActorList(visibleActors);

			//Place in the JSONObject format
			playerUpdate = new JSONObject();
			playerUpdate.put("type", "player-update");
			playerUpdate.put("layout", layout);
			playerUpdate.put("position", position);
			playerUpdate.put("objects", objects);
			playerUpdate.put("actors", actors);
			if (message == null) {
				playerUpdate.put("message", JSONObject.NULL);
			} else {
				playerUpdate.put("message", message);
			}
			playerUpdate.put("current-health", currentHealth);
			playerUpdate.put("max-health", maxHealth);
			this.server.sendMessage(this.name, playerUpdate.toString());
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
	public void displayMessage(String message) { this.server.sendMessage(this.name, message); }

	@Override
	public void sendLevelStart(int levelIndex, Set<String> levelPlayers) {
		JSONObject startLevel = new JSONObject();
		JSONArray nameList = new JSONArray();
		for (String playerName : levelPlayers) {
			nameList.put(playerName);
		}
		startLevel.put("type", "start-level");
		startLevel.put("level", levelIndex);
		startLevel.put("players", nameList);
		
		this.server.sendMessage(this.name, startLevel.toString());
	}

	@Override
	public void sendLevelEnd(String keyFinder, List<String> exitedPlayers, List<String> ejectedPlayers) {
		this.server.sendLevelEnd(this.name, keyFinder, exitedPlayers, ejectedPlayers);
	}

	@Override
	public void sendEndGame(Map<String, Integer> keysFound, Map<String, Integer> numEjects,
			Map<String, Integer> numExits) {
		this.server.sendEndGame(this.name, keysFound, numEjects, numExits);
	}

}
