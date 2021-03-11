package Game.modelView;

import java.util.List;
import Game.model.Player;

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
}
