package Game.model;

import java.awt.Point;

import Game.modelView.EntityType;

/**
 * Represents an item within a game of Snarl.
 * Contains all common fields and methods for 
 * Items. An item can be a Key or an Exit
 */
public abstract class Item {
	
	protected Point location;
	
	/**    
	 * Initialize the item with a location
	 * @param location - the location in the level
	 */
	public Item(Point location) {
	  this.location = location;
	}
	
	/** 
	 * Returns the EntityType that corresponds to this entity
	 * @return The type of entity 
	 */
	public abstract EntityType getEntityType();
	
	/**
	 * Getter for the location of the Item
	 * @return returns the location field of the item
	 */
	public Point getLocation() {
		return this.location;
	}

	@Override
	public int hashCode() {
		return this.location.hashCode();
	}
}
