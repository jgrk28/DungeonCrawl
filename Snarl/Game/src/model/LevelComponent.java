package model;

import java.awt.Point;
import modelView.EntityType;

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
   * @param entity - the Entity in the componentMap 
   * @return the corresponding EntityType
   */
  EntityType getEntityType(Entity entity);

  /**
   * Finds the entity at a given point within the componentMap 
   * @param point - the coordinates of the requested entity
   * @return the entity located at the given coordinates
   * @throws IllegalArgumentException if the coordinates do not exist within
   * the LevelComponent
   */
  Entity getDestinationEntity(Point point);
}
