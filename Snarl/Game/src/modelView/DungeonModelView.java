package modelView;

import java.util.ArrayList;

import model.Player;

public interface DungeonModelView {
	
	int getCurrentLevelIndex();
	
	Boolean isPlayerAlive(Player player);
	
	ArrayList<ArrayList<EntityType>> getPlayerMap(Player player); 
}
