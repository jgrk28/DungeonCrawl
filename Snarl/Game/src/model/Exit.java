package model;

import modelView.EntityType;

/**
 * Represents an exit within a Level
 * The ASCII representation of an Exit is "@"
 */
public class Exit implements Entity {

  @Override
  public EntityType getEntityType() {
    return EntityType.EXIT;
  }
}
