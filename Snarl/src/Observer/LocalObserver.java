package Observer;

import Common.Observer;

/**
 * Represents a local observer within a game of Snarl
 * The observer receives updates from the game manager 
 * on every state change
 */
public class LocalObserver implements Observer {

	@Override
	public void update(String gameState) { 
		System.out.print("OBSERVER VIEW\n");
		System.out.print(gameState); }

}
