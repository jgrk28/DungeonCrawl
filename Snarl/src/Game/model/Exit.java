package Game.model;

import java.awt.Point;

import Game.modelView.EntityType;

/**
 * Represents an exit within a Level
 * The ASCII representation of an Exit is "@"
 */
public class Exit extends Item {
	
  /**
    * Initialize the exit with a location
    * @param location - the location in the level
    */
  public Exit(Point location) {
	super(location);
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.EXIT;
  }
}
