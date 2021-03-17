package Game.model;

import Game.modelView.EntityType;

/**
 * Represents a space within a LevelComponent
 * The ASCII representation of a Space is "."
 * 
 * An instance of a Space is considered to be the
 * same as any other Space
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

  @Override
  public int hashCode() {
    return this.item.hashCode() * this.actor.hashCode();
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
  
  /** 
   * Add comment here
   * @param items
   * @param actor
   * @return
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
  
}
