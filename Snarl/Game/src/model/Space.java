package model;

import modelView.EntityType;

/**
 * Represents a space within a LevelComponent
 */
public class Space implements Entity {

  @Override
  public EntityType getEntityType() {
    return EntityType.SPACE;
  }
}
