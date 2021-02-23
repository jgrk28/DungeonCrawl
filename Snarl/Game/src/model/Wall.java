package model;

import java.util.Objects;
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

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Wall;
  }
}
