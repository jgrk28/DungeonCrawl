package model;

import java.awt.Point;

/**
 * Checks the rules for the game state. The GameManager leverages 
 * these methods to validates moves for the actors and to check 
 * when the level or game is over
 */
public interface RuleChecker {
	
	/**
	 * Checks if the game is over. If so, if it was won or lost. Determines 
	 * if the level is over, and checks if this level is the last level in 
	 * the dungeon. At least one player must exit the last level in order to 
	 * win the game. GameState can be Active, Won, or Lost
	 * @return the GameState
	 */
	GameState isGameOver();
	
	/**
	 * Checks if the level is over. If so, if it was won or lost. In order for a 
	 * level to be over, all players must be removed from the current level. If 
	 * any player exited the level, the level was won. Otherwise, the level was lost.
	 * GameState can be Active, Won, or Lost
	 * @return the GameState
	 */
	GameState isLevelOver();
	
	/**
	 * Checks if the move is valid for the given actor and if the interaction at the 
	 * destination is valid. A move is valid if it is within the movement bounds of 
	 * the given actor, and if the actor can interact with the destination entity. An
	 * actor can be a player or an adversary
	 * @param actor - the actor attempting to move
	 * @param destination - the intended destination for the actor
	 * @return true if the move is valid
	 */
	Boolean checkValidMove(Actor actor, Point destination);
	
	/**
	 * Checks if the current state of the Dungeon is valid by checking if each level is 
	 * valid. A level is invalid if the level has been exited while the exit is locked, 
	 * if there is not exactly one key and exit, or if unknown players or adversaries are 
	 * in the level
	 * @return true if the game state is valid
	 */
	Boolean checkValidGameState();

}
