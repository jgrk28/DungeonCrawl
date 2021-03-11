package Game.model;

import java.awt.Point;

import Game.modelView.EntityType;

/**
 * Represents a key within a Level
 * The ASCII representation of a Key is "!"
 */
public class Key implements Entity {
  //TODO make this extend an abstract object class
  public Point location;
	
  /**
	* Initialize the key with a location
	* @param location - the location in the level
	*/
  public Key(Point location) {
    this.location = location;
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.KEY;
  }
}
