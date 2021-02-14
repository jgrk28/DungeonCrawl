package model;

import modelView.EntityType;

public class Wall implements Entity {

  @Override
  public EntityType getEntityType() {
    return EntityType.WALL;
  }
}
