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
	 * Gets all valid moves for the provided player based on their 
	 * position in the current level of the dungeon
	 * @param player - the player to gather valid moves for
	 * @return a list of all valid moves
	 */
	List<Point> getValidMoves(Player player);

	/**
	 * Checks if the level is over. If so, if it was won or lost. In 
	 * order for a level to be over, all players must be removed from the 
	 * current level. If  any player exited the level, the level was won. 
	 * Otherwise, the level was lost. GameState can be Active, Won, or Lost
	 * @return the GameState
	 */
	GameState isLevelOver();

	/**
	 * Checks if the game is over. If so, if it was won or lost. Determines 
	 * if the level is over, and checks if this level is the last level in 
	 * the dungeon. At least one player must exit the last level in order to 
	 * win the game. GameState can be Active, Won, or Lost
	 * @return the GameState
	 */
	GameState isGameOver();

	/**
	 * Gets the LevelModelView for the current level
	 * @return the LevelModelView
	 */
	LevelModelView getCurrentLevelModelView();

	/**
	 * Gets the actor's position in the dungeon
	 * @param actor - the actor to find
	 * @return the point that the actor is located at
	 */
	Point getPosition(Actor actor);

	/**
	 * Gets all doors that are visible for the player based on their position 
	 * in the dungeon
	 * @param player - the player whose location will be used to determine 
	 * visible doors
	 * @return a list of locations for the visible doors
	 */
	List<Point> getVisibleDoors(Player player);

	/**
	 * Gets all items that are visible for the player based on their position 
	 * in the dungeon
	 * @param player - the player whose location will be used to determine 
	 * visible items
	 * @return a list of Item for the visible items
	 */
	List<Item> getVisibleItems(Player player);

	/**
	 * Gets all actors that are visible for the player based on their position 
	 * in the dungeon
	 * @param player - the player whose location will be used to determine 
	 * visible actors
	 * @return a map of all visible actors and their locations 
	 */
	Map<Actor, Point> getVisibleActors(Player player);
}
