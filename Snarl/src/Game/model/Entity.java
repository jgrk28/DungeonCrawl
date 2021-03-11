package Game.model;

import Game.modelView.EntityType;

/**
 * Represents an entity within a game of Snarl. An entity can be a:
 * - Space
 * - Wall
 * - Key
 * - Exit
 * - Player
 * - Adversary
 */
public interface Entity {

  /** 
   * Returns the EntityType that corresponds to this entity
   * @return The type of entity 
   */
  EntityType getEntityType();
}
