package Game.model;

import java.awt.Point;
import java.util.List;

import Game.modelView.EntityType;
import Game.modelView.LevelModelView;
import java.util.Map;

/**
 * Level extends the LevelModelView interface to provide
 * the view with the necessary information to render a
 * Level
 * 
 * The Level also includes the necessary functionality for
 * performing player and adversary actions, as well as 
 * checking that the level is over
 */
public interface Level extends LevelModelView {

	/**
	 * Places the actors in the top left-most room in the level, and 
	 * places the adversaries in the bottom right-most room in the 
	 * level
	 * @param players - all players in the level
	 * @param adversaries - all adversaries in the level
	 */
	void placeActors(List<Player> players, List<Adversary> adversaries);

	/**
	 * Places the actors at their specified locations in the level
	 * @param players - all players and their location in the level
	 * @param adversaries - all adversaries and their location in the level
	 */
	void placeActorsSpecifiedLocation(Map<Player, Point> players, Map<Adversary, Point> adversaries);

	/**
	 * Places the actors randomly in the level
	 * @param players - all players in the level
	 * @param adversaries - all adversaries in the level
	 */
	void placeActorsRandomly(List<Player> players, List<Adversary> adversaries);

	/**
	 * Removes the given actor from the level
	 * @throws IllegalArgumentException if the actor is not in the level
	 */
	void removeActor(Actor actor);

	/**
     * Processes a player's action by moving the player and
     * performing the corresponding interaction
     * @param player - the player performing the action
     * @param destination - the destination for the player's move
     */
	InteractionResult playerAction(Player player, Point destination);
	
	/**
	 * Processes an adversary's action by moving the adversary 
	 * and performing the corresponding interaction
	 * @param adversary - the adversary performing the action
	 * @param destination - the destination for the adversary's move
	 */
	InteractionResult adversaryAction(Adversary adversary, Point destination);
	
	/**
	 * Checks if the level has ended. A level is over once all
	 * players have been removed from the level
	 * @return the GameState for the level. This can be ACTIVE,
	 * WON, or LOST
	 */
	GameState isLevelOver();

	/**
	 * Finds the LevelComponent that contains the given point
	 * @param point - the point used to locate the LevelComponent
	 * @return the LevelComponent that the point is located in
	 */
	LevelComponent findComponent(Point point);
	
	/**
	 * Checks if the move is valid for the given actor and if the interaction at the 
	 * destination is valid. A move is valid if it is within the movement bounds of 
	 * the given actor, and if the actor can interact with the destination entity. An 
	 * actor can be a player or an adversary
	 * @param actor - the actor performing the move
	 * @param destination - the destination of the move
	 * @return true if the move is valid, false otherwise
	 */
	Boolean checkValidMove(Actor actor, Point destination);
	
	/**
	 * Checks if the level is in a valid state. The level is invalid if the level has 
	 * been exited while the exit is locked, if there is not exactly one key and exit, 
	 * or if unknown players are in the level
	 * @param players - all players in the dungeon
	 * @return true if the level state is valid, false otherwise
	 */
	Boolean checkValidLevelState(List<Player> players);
	
	/**
	 * Determines whether the player is currently alive in the level
	 * @param player - the player to locate
	 * @return true if the player is in the level, false otherwise
	 */
	Boolean isPlayerAlive(Player player);
	
	/**
	 * Returns the map of EntityType for the viewable map around the player's location
	 * @param player - the player in the level
	 * @return the viewable map of EntityType for the player
	 */
	List<List<EntityType>> getPlayerMap(Player player);

	/**
	 * Returns the player with the given name. Since names are unique across all actors there
	 * will only ever be one player with each name.
	 * @param name - the name of the player to get
	 * @return the referenced player.
	 * @throws IllegalArgumentException if there is no player with the given name
	 */
	Player getPlayer(String name);

	/**
	 * Gets all valid moves for the provided player based on their position in the level
	 * @param player - the player to gather valid moves for
	 * @return a list of all valid moves
	 */
	List<Point> getValidMoves(Player player);

	/**
	 * Gets the actor's position in the level
	 * @param actor - the actor to find
	 * @return the point that the actor is located at
	 */
	Point getActorPosition(Actor actor);

	/**
	 * Gets all doors that are visible for the player based on their position in the level
	 * @param player - the player whose location will be used to determine visible doors
	 * @return a list of locations for the visible doors
	 */
	List<Point> getVisibleDoors(Player player);

	/**
	 * Gets all items that are visible for the player based on their position in the level
	 * @param player - the player whose location will be used to determine visible items
	 * @return a list of Item for the visible items
	 */
	List<Item> getVisibleItems(Player player);

	/**
	 * Gets all actors that are visible for the player based on their position in the level
	 * not including the player themselves
	 * @param player - the player whose location will be used to determine visible actors
	 * @return a map of all visible actors and their locations 
	 */
	Map<Actor, Point> getVisibleActors(Player player);

	/**
	 * Gets all active players in the level
	 * @return the map of players and their location
	 */
	Map<Player, Point> getActivePlayers();

	/**
	 * Gets all active adversaries in the level
	 * @return the map of adversaries and their locations
	 */
	Map<Adversary, Point> getActiveAdversaries();

	/**
	 * Gets the exitUnlocked boolean for the level
	 * @return true if the exit is unlocked, false otherwise
	 */
	Boolean getExitUnlocked();

	/**
	 * Gets the levelExited boolean for the level
	 * @return true if the level has been exited by at least 1 player, false otherwise
	 */
	Boolean getLevelExited();

	/**
	 * Gets the list of LevelComponents that compose the level map
	 * @return the level map
	 */
	List<LevelComponent> getLevelMap();

	/**
	 * Gets the items in the level. This can include keys and exits
	 * @return the list of all items currently in the level
	 */
	List<Item> getItems();

}
