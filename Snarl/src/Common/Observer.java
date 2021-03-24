package Common;

/**
 * Represents an observing component for a game of Snarl
 */
public interface Observer {
	
	/**
	 * Updates the observer with the current state of the game
	 * @param gameState - a string representing the state of the
	 * game. This includes:
	 * - The state of the game (active, won, or lost)
	 * - The current level
	 * - A full view of the current level with adversary, player, 
	 *   and item positions 
	 */
	void update(String gameState);

}
