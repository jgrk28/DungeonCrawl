package Observer;

import Common.Observer;

/**
 * TODO Add comments here
 */
public class LocalObserver implements Observer {

	@Override
	public void update(String gameState) { System.out.println(gameState); }

}
