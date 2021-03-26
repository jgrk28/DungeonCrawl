package Game.model;

import Game.modelView.EntityType;

/**
 * Represents a Tile within a game of Snarl.
 * Halls are composed of spaces, and rooms are 
 * composed of spaces and walls
 * 
 * A Tile can be:
 * - Space
 * - Wall
 */
public interface Tile {
	
	/** 
	  * Returns the EntityType that corresponds to the items
	  * and actors located on the tile, based on what the user
	  * should be able to view 
	  * @return The type of entity 
	  */
	EntityType getEntityType();
	
	/**
	 * Gets the actor located at this tile
	 * @return the actor at this tile, if there is one
	 */
	Actor getActor();
	
	/**
	 * Gets the item located at this tile
	 * @return the item at this tile, if there is one
	 */
	Item getItem();
	
	/**
	 * Places the actor on this tile
	 * @param actor - the actor to be placed
	 */
	void placeActor(Actor actor);
	
	/**
	 * Places the item on this tile
	 * @param item - the item to be placed
	 */
	void placeItem(Item item);

	/**
	 * Removes the actor on this tile
	 */
	void removeActor();

	/**
	 * Removes the item on this tile
	 */
	void removeItem();
}
