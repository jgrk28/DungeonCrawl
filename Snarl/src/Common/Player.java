package Common;

import java.awt.Point;
import java.util.List;
import Game.modelView.PlayerModelView;
import java.util.Map;
import java.util.Set;

/**
 * Represents the interests of the human behind the keyboard
 * in a game. Players receive updates from the game manager,
 * and send turns to the game manager when appropriate 
 */
public interface Player {
	
	/**
	 * Prompts the player to take a turn and send a move
	 * to the game manager
	 * @param validMove - a list of all valid moves the player can make
	 * @return the point that the player would like to move to
	 */
	Point takeTurn(List<Point> validMove);
	
	/**
	 * Updates the player on the state of the game, based on their view
	 * @param gameState - all information relevant to the state of the
	 * @param message - optional message for important information to convey
	 * to the player
	 * game that the user can see
	 */
	void update(PlayerModelView gameState, String message);
	
	/**
	 * Displays a message to the user
	 * @param message - the message to be displayed
	 */
	void displayMessage(String message);

	/**
	 * Sends the start level information to the player
	 * @param levelIndex - the current level index
	 * @param levelPlayers - the names of all players in the level
	 */
	void sendLevelStart(int levelIndex, Set<String> levelPlayers);


	/**
	 * Sends the end level information to the user
	 * @param keyFinder - the name of the player that found the key
	 * @param exitedPlayers - the names of the players that exited the level
	 * @param ejectedPlayers - the names of the players that were ejected from the level
	 */
	void sendLevelEnd(String keyFinder, List<String> exitedPlayers, List<String> ejectedPlayers);


	/**
	 * Sends the end game information to the user
	 * @param keysFound - a map of player names to the number of keys found during the game
	 * @param numEjects - a map of player names to the number of times they were ejected from the game
	 * @param numExits  - a map of player names to the number of times they exited a level
	 */
	void sendEndGame(Map<String, Integer> keysFound, Map<String, Integer> numEjects, Map<String, Integer> numExits);
}
