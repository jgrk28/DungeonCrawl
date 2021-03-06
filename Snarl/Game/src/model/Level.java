package model;

import java.awt.Point;
import java.util.List;

import modelView.LevelModelView;

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
	 * Processes a player's action by moving the player and
	 * performing the corresponding interaction
	 * @param player - the player performing the action
	 * @param destination - the destination for the player's move
	 */
	void playerAction(Player player, Point destination);
	
	/**
	 * Processes an adversary's action by moving the adversary 
	 * and performing the corresponding interaction
	 * @param adversary - the adversary performing the action
	 * @param destination - the destination for the adversary's move
	 */
	void adversaryAction(Adversary adversary, Point destination);
	
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
	
	Boolean checkValidMove(Actor actor, Point destination);
	
	Boolean checkValidLevelState(List<Player> players, List<Adversary> adversaries);

}
