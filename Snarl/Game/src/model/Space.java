package model;

import modelView.EntityType;

public class Space implements Entity {

  @Override
  public EntityType getEntityType() {
    return EntityType.SPACE;
  }
}
