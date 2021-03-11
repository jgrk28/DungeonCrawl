package Game.modelView;

import java.util.List;
import Game.model.Player;

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
}
