package Game.model;

import Game.modelView.EntityType;

/**
 * Add comment here
 * 
 * A Tile can be:
 * - Empty
 * - Space
 * - Wall
 */
public interface Tile {
	
	/** 
	  * Returns the EntityType that corresponds to this entity
	  * @return The type of entity 
	  */
	EntityType getEntityType();
	
	/**
	 * TODO Add comment here
	 * @param actor
	 * @return
	 */
	Actor getActor();
	
	/**
	 * TODO Add comment here
	 */
	Item getItem();
	
	/**
	 * TODO Add comment here
	 */
	void placeActor(Actor actor);
	
	/**
	 * TODO Add comment here
	 */
	void placeItem(Item item);

}
