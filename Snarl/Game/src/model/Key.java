package model;

import modelView.EntityType;

/**
 * Represents a key within a Level
 * The ASCII representation of a Key is "!"
 */
public class Key implements Entity {

  @Override
  public EntityType getEntityType() {
    return EntityType.KEY;
  }
}
