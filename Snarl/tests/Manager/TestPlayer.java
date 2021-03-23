package Manager;

import Game.model.Actor;
import Game.model.Item;
import Game.modelView.EntityType;
import java.awt.Point;
import java.util.List;

import java.util.Map;
import org.json.JSONArray;

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
			throw new IllegalArgumentException("No more moves");
		}
		Point newMove = this.moves.get(0);
		this.moves.remove(0);
		return newMove;
	}

	@Override
	public void update(PlayerModelView gameState) {
		List<List<EntityType>> playerMap = gameState.getMap();
		Point absolutePosition = gameState.getPosition();
		List<Point> doors = gameState.getVisibleDoors();
		List<Item> items = gameState.getVisibleItems();
		Map<Actor, Point> actors = gameState.getVisibleActors();
		JSONArray layout = generateLayout(playerMap, absolutePosition, doors);
	}

	private JSONArray generateLayout(List<List<EntityType>> playerMap, Point position, List<Point> doors) {
    return new JSONArray();
	}

	@Override
	public void displayMessage(String message) {
		// Do Nothing
	}

}
