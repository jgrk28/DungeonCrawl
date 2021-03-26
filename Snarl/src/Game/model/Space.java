package Game.model;

import Game.modelView.EntityType;

/**
 * Represents a space within a LevelComponent
 * The ASCII representation of a Space is "."
 * 
 * An instance of a Space is considered to be the
 * same as any other Space if it does not contain
 * an item or an actor
 */
public class Space implements Tile {
	
  private Item item;
  private Actor actor;
  
  public Space() {
	  this.item = null;
	  this.actor = null;
  }
  
  public Space(Item item, Actor actor) {
	  this.item = item;
	  this.actor = actor;
  }

  @Override
  public EntityType getEntityType() {
	if (actor != null) {
		return actor.getEntityType();
	} 
	if (item != null) {
		return item.getEntityType();
	}
    return EntityType.SPACE;
  }
  
  /** 
   * Helper method for equals that checks that the fields for the two
   * spaces are the same
   * @param item - the item on the tile
   * @param actor - the actor on the tile
   * @return true if the fields are the same
   */
  private Boolean checkSameFields(Item item, Actor actor) {
	  Boolean sameItem;
	  Boolean sameActor;
	  
	  if (item != null) {
		  sameItem = item.equals(this.item);
	  } else {
		  sameItem = (this.item == null);
	  }
	  if (actor != null) {
		  sameActor = actor.equals(this.actor);
	  } else {
		  sameActor = (this.actor == null);
	  }
	  
	  return sameItem && sameActor;   
  }

  @Override
  public Actor getActor() {
	  return this.actor;
  }
  
  @Override
  public Item getItem() {
	return this.item;
  }

  @Override
  public void placeActor(Actor actor) {
	this.actor = actor;	
  }
  
  @Override
  public void placeItem(Item item) {
	this.item = item;	
  }

  @Override
  public void removeActor() { this.actor = null; }

  @Override
  public void removeItem() { this.item = null; }

  @Override
  public int hashCode() {
    int itemHash = 1;
    int actorHash = 1;
    if (item != null) {
      itemHash = this.item.hashCode();
    }
    if (actor != null) {
      actorHash = this.actor.hashCode();
    }
    return itemHash * actorHash;
  }

  @Override
  public boolean equals(Object obj) {
      if (obj == this) { 
          return true; 
      } 
      if (!(obj instanceof Space)) { 
          return false; 
      } 
      
      Space space = (Space) obj;
      return space.checkSameFields(this.item, this.actor);
  }
  
}
