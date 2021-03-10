package clients;

import java.awt.Point;

/**
 * The PlayerClient runs on the user's side (locally or through TCP connection)
 * This displays relevant information to the user, and sends moves and other 
 * commands that the user would like to perform in the game to the GameManager 
 */
public class PlayerClient {

  /**
   * Prompts the player for a move and sends it to the GameManager
   * @return the point that the player would like to move to
   */
  public Point takeTurn() {
	//TODO This will be implemented in future milestones
    return null;
  }
 
  /**
   * Displays the game state relevant to the user. This includes what the 
   * player can see in the level, the current level, and whether or not
   * they are still active in the level
   * @param gameState - the string containing the relevant game state
   */
  public void displayGameState(String gameState) {
	//TODO This will be implemented in future milestones
  }
}
