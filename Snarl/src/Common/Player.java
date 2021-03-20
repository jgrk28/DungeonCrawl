package Common;

import java.awt.Point;
import java.util.List;

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
	 * @param message
	 */
	void displayMessage(String message);
	
}
