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
   * Gets the map for the level and returns a read-only version of the part that this player can
   * see.
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
   * TODO Add comment here
   * @return
   */
  public List<Point> getValidMoves() { return dungeonModelView.getValidMoves(this.player); }

  /**
   * TODO Add comment here
   * @return
   */
  public GameState isLevelOver() { return dungeonModelView.isLevelOver(); }

  /**
   * TODO Add comment here
   * @return
   */
  public GameState isGameOver() { return dungeonModelView.isGameOver(); }

  /**
   * TODO Add comment here
   * @return
   */
  public Point getPosition() { return dungeonModelView.getPosition(this.player); }

  /**
   * TODO Add comment here
   * @return
   */
  public List<Point> getVisibleDoors() { return dungeonModelView.getVisibleDoors(this.player); }


  /**
   * TODO Add comment here
   * @return
   */
  public List<Item> getVisibleItems() { return dungeonModelView.getVisibleItems(this.player); }


  /**
   * TODO Add comment here
   * @return
   */
  public Map<Actor, Point> getVisibleActors() { return dungeonModelView.getVisibleActors(this.player); }
}
