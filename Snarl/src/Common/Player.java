package Common;

import java.awt.Point;
import java.util.List;

import Game.modelView.PlayerModelView;

/**
 * TODO Add comments here
 */
public interface Player {
	
	/**
	 * 
	 * @return
	 */
	Point takeTurn(List<Point> validMove);
	
	/**
	 * 
	 * @param gameState
	 */
	void update(PlayerModelView gameState);
	
	/**
	 * 
	 * @param message
	 */
	void displayMessage(String message);
}
