package modelView;

import java.util.ArrayList;

/**
 * Represents the model view of a Level
 */
public interface LevelModelView {
	
	/**
	 * Gets the map for the level and returns a read-only version to the view
	 * @return a map of all EntityTypes within the Level
	 */
	ArrayList<ArrayList<EntityType>> getMap();
	
	

}
