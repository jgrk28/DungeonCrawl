package Game.modelView;

import Game.model.Actor;
import Game.model.GameState;
import Game.model.Item;
import java.awt.Point;
import java.util.List;
import Game.model.Player;
import java.util.Map;

/**
 * Represents the model view from a player's perspective
 */
public class PlayerModelView {
	
	Player player;
	DungeonModelView dungeonModelView;
	
	//Create a PlayerModelView with the player and corresponding 
	//DungeonModelView
	public PlayerModelView(Player player, DungeonModelView dungeonModelView) {
		this.player = player;
		this.dungeonModelView = dungeonModelView;
	}
	
  /**
   * Gets the map for the level and returns a read-only version of the part that 
   * this player can see
   * @return a map of all EntityTypes that this player can see
   */
  public List<List<EntityType>> getMap() {
	return dungeonModelView.getPlayerMap(this.player); 	  
  }

  /**
   * Gets the current level of the dungeon
   * @return the integer corresponding to the current level
   */
  public int getCurrentLevel() { return dungeonModelView.getCurrentLevelIndex(); }

  /**
   * Determines if this player active in the current level of
   * the dungeon
   * @return true if the player is still active in the level
   */
  public Boolean isPlayerAlive() { return dungeonModelView.isPlayerAlive(this.player); }
  
  /**
   * Gets all valid moves for this player based on their position in the dungeon
   * @return a list of all valid moves
   * */
  public List<Point> getValidMoves() { return dungeonModelView.getValidMoves(this.player); }

  /**
   * Checks if the current level has ended. A level is over once all players have 
   * been removed from the level
   * @return the GameState for the level. This can be ACTIVE, WON, or LOST
   */
  public GameState isLevelOver() { return dungeonModelView.isLevelOver(); }

  /**
   * Checks if the game is over. If so, if it was won or lost. Determines if the level 
   * is over, and checks if this level is the last level in  the dungeon. At least one 
   * player must exit the last level in order to win the game. GameState can be Active, 
   * Won, or Lost
   * @return the GameState
   */
  public GameState isGameOver() { return dungeonModelView.isGameOver(); }

  /**
   * Gets this player's position in the dungeon
   * @return the point where this player is located 
   */
  public Point getPosition() { return dungeonModelView.getPosition(this.player); }

  /**
   * Gets all doors that are visible for this player based on their position in the
   * dungeon
   * @return a list of locations for the visible doors
   */
  public List<Point> getVisibleDoors() { return dungeonModelView.getVisibleDoors(this.player); }


  /**
   * Gets all items that are visible for this player based on their position in the dungeon
   * @return a list of Item for the visible items
   */
  public List<Item> getVisibleItems() { return dungeonModelView.getVisibleItems(this.player); }


  /**
   * Gets all actors that are visible for the player based on their position 
   * in the dungeon
   * @return a map of all visible actors and their locations 
   */
  public Map<Actor, Point> getVisibleActors() { return dungeonModelView.getVisibleActors(this.player); }
  
}
