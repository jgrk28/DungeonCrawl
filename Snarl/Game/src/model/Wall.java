package model;

import modelView.EntityType;

/**
 * Represents a wall within a LevelComponent
 * The ASCII representation of a Space is "X"
 */
public class Wall implements Entity {

  @Override
  public EntityType getEntityType() {
    return EntityType.WALL;
  }
}
