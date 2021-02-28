package model;

import java.awt.Point;

import modelView.EntityType;

/**
 * Represents a key within a Level
 * The ASCII representation of a Key is "!"
 */
public class Key implements Entity {
	
	public Point location;
	
	public Key(Point location) {
		this.location = location;
	}

  @Override
  public EntityType getEntityType() {
    return EntityType.KEY;
  }
}
