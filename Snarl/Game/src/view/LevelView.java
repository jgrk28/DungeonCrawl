package view;

/**
 * Represents the view of the Level
 * This enables the user to visualize the Level
 */
public interface LevelView {
	
	/**
	 * Draws the Level, which is composed of connected LevelComponents
	 * Outputs a String to STDOUT that represents the placement of Rooms, 
	 * Halls, and Entities within the Level
	 */
	void drawLevel();

}
