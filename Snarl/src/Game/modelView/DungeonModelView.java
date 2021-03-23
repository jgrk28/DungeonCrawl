package Game.modelView;

import Game.model.Actor;
import Game.model.GameState;
import Game.model.Item;
import java.awt.Point;
import java.util.List;
import Game.model.Player;
import java.util.Map;

/**
 * Represents the model view of a Dungeon
 */
public interface DungeonModelView {
	
	/**
	 * Gets the index of the current level
	 * @return the integer corresponding to the current level
	 */
	int getCurrentLevelIndex();
	
	/**
	 * Determines if the player active in the current level of
	 * the dungeon
	 * @param player - the player to find
	 * @return true if the player is still active in the level
	 */
	Boolean isPlayerAlive(Player player);
	
	/**
	 * Gets the map for the current level that can be viewed by 
	 * the given player and returns a read-only version to the view
	 * @param player - the player used to determine the viewable map
	 * @return a map of all EntityTypes that the given player can see
	 */
	List<List<EntityType>> getPlayerMap(Player player);
	
	/**
	 * TODO Add comment here
	 * @param player
	 * @return
	 */
	List<Point> getValidMoves(Player player);

	/**
	 * TODO Add comment here
	 * @return
	 */
	GameState isLevelOver();

	/**
	 * TODO Add comment here
	 * @return
	 */
	GameState isGameOver();

	/**
	 * TODO Add comment here
	 * @return
	 */
	LevelModelView getCurrentLevelModelView();

	/**
	 * TODO Add comment here
	 * @return
	 */
	Point getPosition(Actor actor);

	/**
	 * TODO Add comment here
	 * @return
	 */
	List<Point> getVisibleDoors(Player player);

	/**
	 * TODO Add comment here
	 * @return
	 */
	List<Item> getVisibleItems(Player player);

	/**
	 * TODO Add comment here
	 * @return
	 */
	Map<Actor, Point> getVisibleActors(Player player);
}
