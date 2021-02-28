package model;

import java.awt.Point;

import modelView.EntityType;

/**
 * Represents an exit within a Level
 * The ASCII representation of an Exit is "@"
 */
public class Exit implements Entity {
	
	public Point location;
	
	public Exit(Point location) {
		this.location = location;
	}

  @Override
  public EntityType getEntityType() {
    return EntityType.EXIT;
  }
}
