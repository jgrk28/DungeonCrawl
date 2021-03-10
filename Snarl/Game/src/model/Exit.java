package model;

import java.awt.Point;

import modelView.EntityType;

/**
 * Represents an exit within a Level
 * The ASCII representation of an Exit is "@"
 */
public class Exit implements Entity {
  //TODO make this extend an abstract object class
  public Point location;
	
  /**
    * Initialize the exit with a location
    * @param location - the location in the level
    */
  public Exit(Point location) {
    this.location = location;
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.EXIT;
  }
}
