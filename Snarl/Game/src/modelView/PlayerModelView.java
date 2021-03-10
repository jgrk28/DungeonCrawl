package modelView;

import java.util.ArrayList;

import java.util.List;
import model.Player;

/**
 * Represents the model view from a players perspective
 */
public class PlayerModelView {
	
	Player player;
	DungeonModelView dungeonModelView;
	
	public PlayerModelView(Player player, DungeonModelView dungeonModelView) {
		this.player = player;
		this.dungeonModelView = dungeonModelView;
	}
	
  /**
   * Gets the map for the level and returns a read-only version of the part that this player can
   * see.
   * @return a map of all EntityTypes within the Level
   */
  public List<List<EntityType>> getMap() {
	return dungeonModelView.getPlayerMap(this.player); 	  
  }

	public int getCurrentLevel() { return dungeonModelView.getCurrentLevelIndex(); }

	public Boolean isPlayerAlive() {
	return dungeonModelView.isPlayerAlive(this.player);	  
  }
}
