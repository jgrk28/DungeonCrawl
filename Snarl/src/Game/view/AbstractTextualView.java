package Game.view;

import java.io.PrintStream;
import java.util.List;
import Game.modelView.EntityType;

/**
 * Abstract class for common methods of Textual views
 * (ASCII based)
 */
public abstract class AbstractTextualView implements View {
 
  protected PrintStream output;

  public AbstractTextualView(PrintStream output) {
    this.output = output;
  }

  /**
   * Returns the String representation for a given EntityType
   * @param entity - the given EntityType from the Level
   * @return the String corresponding to an EntityType
   */
  protected String drawEntity(EntityType entity) {
    switch (entity) {
      case SPACE: return ".";
      case WALL: return "X";
      case HALL_SPACE: return "*";
      case KEY: return "!";
      case EXIT: return "@";
      case PLAYER: return "P";
      case GHOST: return "G";
      case ZOMBIE: return "Z";
      case EMPTY: return " ";
      default: throw new IllegalArgumentException("Entity type is not valid");
    }
  }

  /**
   * Returns the ASCII representation of the level map based 
   * on the provided EntityTypes
   * @param level - the map of all EntityTypes in the level
   * @return the level map representation as a string
   */
  protected String drawLevelMap(List<List<EntityType>> level) {
    StringBuilder output = new StringBuilder();

    //Iterate through the level, identify the entity, and append to
    //the output
    for (int i = 0; i < level.size(); i++) {
      List<EntityType> row = level.get(i);
      for (int j = 0; j < row.size(); j++) {
        String currEntity = drawEntity(row.get(j));
        output.append(currEntity);
      }
      output.append("\n");
    }
    return output.toString();
  }
}
