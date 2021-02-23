package model;

import modelView.EntityType;

/**
 * Represents a space within a LevelComponent
 * The ASCII representation of a Space is "."
 * 
 * An instance of a Space is considered to be the
 * same as any other Space
 */
public class Space implements Entity {

  @Override
  public EntityType getEntityType() {
    return EntityType.SPACE;
  }

  @Override
  public int hashCode() {
    return 1;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Space;
  }
}
