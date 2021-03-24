package Manager;

import Game.model.Actor;
import Game.model.Exit;
import Game.model.Ghost;
import Game.model.Item;
import Game.model.Key;
import Game.model.Zombie;
import Game.modelView.EntityType;
import java.awt.Point;
import java.util.HashSet;
import java.util.List;

import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import Utils.ParseUtils;

import Common.Player;
import Game.modelView.PlayerModelView;

/**
 * TODO Add comments
 */
public class TestPlayer implements Player {
	
	private List<Point> moves;
	private JSONArray output;
	
	public TestPlayer(List<Point> moves, JSONArray output) {
		this.moves = moves;
		this.output = output;
	}

	@Override
	public Point takeTurn(List<Point> validMove) {
		if (this.moves.isEmpty()) {
			throw new IllegalStateException("No more moves");
		}
		Point newMove = this.moves.get(0);
		this.moves.remove(0);
		return newMove;
	}

	/**
	 * {
  		"type": "player-update",
  		"layout": (tile-layout),
  		"position": (point),
  		"objects": (object-list),
  		"actors": (actor-position-list)
	 * }
	 */
	@Override
	public void update(PlayerModelView gameState) {
		List<List<EntityType>> playerMap = gameState.getMap();
		Point absolutePosition = gameState.getPosition();
		List<Point> visibleDoors = gameState.getVisibleDoors();
		List<Item> visibleItems = gameState.getVisibleItems();
		Map<Actor, Point> visibleActors = gameState.getVisibleActors();
		
		JSONArray layout = generateLayout(playerMap, absolutePosition, visibleDoors);
		JSONArray position = ParseUtils.generateJSONPoint(absolutePosition);
		JSONArray objects = generateObjectList(visibleItems);
		JSONArray actors = generateActorPositionList(visibleActors);
		
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

	/**
	 * TODO add comment
	 * @param playerMap
	 * @param playerPosition
	 * @param doors
	 * @return
	 */
	private JSONArray generateLayout(List<List<EntityType>> playerMap, Point playerPosition, List<Point> doors) {
		Point topLeftPosition = new Point(playerPosition.x - 2, playerPosition.y - 2);
		JSONArray layoutOutput = new JSONArray();
		Set<Point> relativeDoors = new HashSet<>();
		
		for (Point point : doors) {
			Point relPoint = new Point(point.x - topLeftPosition.x, point.y - topLeftPosition.y);
			relativeDoors.add(relPoint);
		}
 		
		for (int i = 0; i < playerMap.size(); i++) {
			List<EntityType> entityRow = playerMap.get(i);
			JSONArray JSONRow = new JSONArray();
			for (int j = 0; j < entityRow.size(); j++) {
				EntityType entity = entityRow.get(j);
				if (entity.equals(EntityType.EMPTY) || entity.equals(EntityType.WALL)) {
					JSONRow.put(0);
				} else if (relativeDoors.contains(new Point(j, i))) {
					JSONRow.put(2);
				} else {
					JSONRow.put(1);
				}
			}
			layoutOutput.put(JSONRow);
		}
		return layoutOutput;
	}
	
	/**
	 * TODO add comment
	 * @param items
	 * @return
	 */
	private JSONArray generateObjectList(List<Item> items) {
		JSONArray objectList = new JSONArray();
		for (Item item : items) {
			JSONObject JSONItem = new JSONObject();
			if (item instanceof Key) {
				JSONItem.put("type", "key");
			}
			else if (item instanceof Exit) {
				JSONItem.put("type", "exit");
			}
			JSONArray position = ParseUtils.generateJSONPoint(item.getLocation());
			JSONItem.put("position", position);
			objectList.put(JSONItem);
		}
		return objectList;
	}
	

	
	/**
	 * TODO Add comment
	 * @param actors
	 * @return
	 */
	private JSONArray generateActorPositionList(Map<Actor, Point> actors) {
		JSONArray actorPositionList = new JSONArray();
		for (Map.Entry<Actor, Point> actorEntry : actors.entrySet()) {
			JSONObject actorJSON = new JSONObject();
			Actor actor = actorEntry.getKey();
			Point position = actorEntry.getValue();
			
			if (actor instanceof Player) {
				actorJSON.put("type", "player");
			} else if (actor instanceof Zombie) {
				actorJSON.put("type", "zombie");
			} else if (actor instanceof Ghost) {
				actorJSON.put("type", "ghost");
			}
			
			actorJSON.put("name", actor.getName());
			actorJSON.put("position", ParseUtils.generateJSONPoint(position));
			actorPositionList.put(actorJSON);
		}
		return actorPositionList;
	}

	@Override
	public void displayMessage(String message) {
		// Do Nothing
	}

}
