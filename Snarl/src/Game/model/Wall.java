package Game.model;

import Game.modelView.EntityType;

/**
 * Represents a wall within a LevelComponent
 * The ASCII representation of a Space is "X"
 * 
 * An instance of a Wall is considered to be the
 * same as any other Wall
 */
public class Wall implements Tile {

  @Override
  public EntityType getEntityType() {
    return EntityType.WALL;
  }

  @Override
  public Actor getActor() {
	  throw new IllegalArgumentException("Actor does not exist on a wall");
  }
  
  @Override
  public Item getItem() {
	  throw new IllegalArgumentException("Item does not exist on a wall");
  }
  
  @Override
  public void placeActor(Actor actor) {
	throw new IllegalArgumentException("Actor cannot be placed on a wall");
  }
  
  @Override
  public void placeItem(Item item) {
	throw new IllegalArgumentException("Item cannot be placed on a wall");
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
