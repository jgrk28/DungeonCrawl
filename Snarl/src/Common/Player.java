package Common;

import java.awt.Point;
import java.util.List;

import Game.model.InteractionResult;
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
	 * TODO
	 */
	void sendLevelStart(int levelIndex, Set<String> levelPlayers);

	/**
	 * TODO
	 */
	void sendLevelEnd(String keyFinder, List<String> exitedPlayers, List<String> ejectedPlayers);

	/**
	 * TODO
	 */
	void sendEndGame(Map<String, Integer> keysFound, Map<String, Integer> numEjects, Map<String, Integer> numExits);
}
