package model;

import modelView.EntityType;

/**
 * Represents a wall within a LevelComponent
 */
public class Wall implements Entity {

  @Override
  public EntityType getEntityType() {
    return EntityType.WALL;
  }
}
