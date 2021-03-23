package Game.model;

import java.awt.Point;
import Game.modelView.EntityType;
import java.util.List;

/**
 * Represents a LevelComponent within a game of Snarl
 * A LevelComponent can be a:
 * - Room
 * - Hall
 * A set of connected LevelComponents make up a Level
 */
public interface LevelComponent {

  /**
   * Determines the left-most Cartesian coordinate for this LevelComponent
   * @return the Point (x and y values) of the top left bound
   */
  Point getTopLeftBound();
  
  /**
   * Determines the right-most Cartesian coordinate for this LevelComponent
   * @return the Point (x and y values) of the bottom right bound
   */
  Point getBottomRightBound();
  
  /**
   * Identifies the EntityType for a given Entity within the LevelComponent
   * @param tile - the Tile in the componentMap 
   * @return the corresponding EntityType
   */
  EntityType getEntityType(Tile tile);

  /**
   * Checks if a given coordinate is located within the Room
   * @param point - the coordinate to check
   * @return true if the point is located within the LevelComponent, false otherwise
   */
  boolean inComponent(Point point);

  /**
   * Finds the Tile at a given point within the componentMap 
   * @param point - the coordinates of the requested entity
   * @return the Tile located at the given coordinates
   * @throws IllegalArgumentException if the coordinates do not exist within
   * the LevelComponent
   */
  Tile getDestinationTile(Point point);
  
  /**
   * Removes the actor from the map of the LevelComponent
   * @param actor - the actor being removed from the LevelComponent
   */
  void removeActor(Actor actor);
  
  /**
   * Places the actor at the specified destination within the LevelComponent
   * @param actor - the actor being placed
   * @param destination - the destination within the LevelComponent
   */
  void placeActor(Actor actor, Point destination);

  /**
   * Places the given item for this level in its position as determined by its field.  
   * This method will be called when the level is created.
   * @param item - the item to be placed
   */
  void placeItem(Item item);
  
  /**
   * Finds the absolute location of the actor within the level
   * @param actor - the actor to be found
   * @return the point that the actor is located at 
   * @throws IllegalArgumentException if the actor is not in the LevelComponent
   */
  Point findActorLocation(Actor actor);

  /**
   * TODO add comment
   */
  List<Point> getDoors();
}
