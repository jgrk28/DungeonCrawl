package Utils;

import java.awt.Point;
import org.json.JSONArray;

/**
 * Utility methods for the TestHarness
 */
public class ParseUtils {
  /**
   * Converts the JSONArray values for the location of a cell to a Point
   * @param JSONPoint - the JSONArray of [row, column] values
   * @return the Point representation of the JSONPoint, (column, row)
   */
  static public Point parsePoint(JSONArray JSONPoint) {
    int x = JSONPoint.getInt(1);
    int y = JSONPoint.getInt(0);
    return new Point(x,y);
  }
}
