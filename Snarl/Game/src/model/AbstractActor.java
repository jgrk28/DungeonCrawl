package model;

import java.util.UUID;
import modelView.EntityType;

abstract public class AbstractActor implements Actor {
  protected String name;

  public AbstractActor(String name) {
    this.name = name;
  }

  public AbstractActor() {
    this.name = UUID.randomUUID().toString();
  }

  public Boolean hasName(String name) {
    return this.name.equals(name);
  }

  /**
   * Determines if the given EntityType is traversable for a player
   * @param entityType - the EntityType
   * @return true if the EntityType is traversable
   */
  public Boolean isTraversable(EntityType entityType) {
    try {
      getInteractionResult(entityType);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}
