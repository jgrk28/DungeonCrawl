package Game.model;

import java.awt.Point;

import Game.modelView.EntityType;

/**
 * TODO Add comment here
 *
 */
public abstract class Item {
	
	private Point location;
	
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
	 * TODO add comment here
	 * @return
	 */
	public Point getLocation() {
		return this.location;
	}

}
