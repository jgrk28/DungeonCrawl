package Game.model;

import java.awt.Point;

import Game.modelView.EntityType;

/**
 * Represents a key within a Level
 * The ASCII representation of a Key is "!"
 */
public class Key extends Item {
	
  /**
	* Initialize the key with a location
	* @param location - the location in the level
	*/
  public Key(Point location) {
    super(location);
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.KEY;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Key)) {
      return false;
    }

    Key otherKey = (Key) obj;
    return this.location.equals(otherKey.location);
  }
}
