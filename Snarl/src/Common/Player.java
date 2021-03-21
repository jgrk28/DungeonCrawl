package Common;

import java.awt.Point;
import java.util.List;

/**
 * TODO Add comments here
 */
public interface Player extends Observer {
	
	/**
	 * 
	 * @return
	 */
	Point takeTurn(List<Point> validMove);
}
