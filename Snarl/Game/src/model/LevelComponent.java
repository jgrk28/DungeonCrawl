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
   * Checks if a given coordinate is located within the Room
   * @param point - the coordinate to check
   * @return true if the point is located within the LevelComponent, false otherwise
   */
  boolean inComponent(Point point);

  /**
   * Finds the entity at a given point within the componentMap 
   * @param point - the coordinates of the requested entity
   * @return the entity located at the given coordinates
   * @throws IllegalArgumentException if the coordinates do not exist within
   * the LevelComponent
   */
  Entity getDestinationEntity(Point point);
  
  /**
   * Moves the actor to the specified destination by removing them from
   * their current location and placing them at the destination
   * @param actor - the actor being moved
   * @param destination - the destination point
   */
  //This may not be needed as we are currently just individually calling remove and place
  //when moving an actor.
  void moveActor(Actor actor, Point destination);
  
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

  //TODO combine these to just placeObject
  /**
   * Places the given key for this level in its position as determined by its field. If the
   * position is occupied it will simply not place the key. This function will be called when the
   * level is created.
   */
  void placeKey(Key key);

  /**
   * Places the given exit for this level in its position as determined by its field. If the
   * position is occupied it will simply not place the exit. This function will be called when the
   * level is created and also any time an actor moves off the exit.
   */
  void placeExit(Exit exit);
  
  /**
   * Finds the location of the entity within the componentMap
   * @param entity - the entity to be found
   * @return the point that the entity is located at 
   * @throws IllegalArgumentException if the entity is not in the room
   */
  Point findEntityLocation(Entity entity);

}
