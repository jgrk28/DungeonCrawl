package modelView;

import java.util.List;
import model.Player;

public interface DungeonModelView {
	
	int getCurrentLevelIndex();
	
	Boolean isPlayerAlive(Player player);
	
	List<List<EntityType>> getPlayerMap(Player player);
}
