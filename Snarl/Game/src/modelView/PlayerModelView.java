package modelView;

import java.util.ArrayList;

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
  ArrayList<ArrayList<EntityType>> getMap() {
	return dungeonModelView.getPlayerMap(this.player); 	  
  }
  
  int getCurrentLevel() {
	return dungeonModelView.getCurrentLevelIndex();
	  
  }
  
  Boolean isPlayerAlive() {  
	return dungeonModelView.isPlayerAlive(this.player);	  
  }
}
