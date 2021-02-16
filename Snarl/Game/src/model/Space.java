package model;

import modelView.EntityType;

/**
 * Represents a space within a LevelComponent
 * The ASCII representation of a Space is "."
 */
public class Space implements Entity {

  @Override
  public EntityType getEntityType() {
    return EntityType.SPACE;
  }
}
