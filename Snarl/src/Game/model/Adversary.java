package Game.model;

import java.awt.Point;
import java.util.List;

import Game.modelView.EntityType;

/**
 * Represents an adversary for the players in the level.
 */
public abstract class Adversary extends AbstractActor {
	
	private static final int maxMoveDistance = 1;

	/**
	 * Initialize an Adversary with a unique name
	 * @param name - the name of the Adversary
	 */
	public Adversary(String name) {
		super(name);
	}

	/**
	 * Initialize an Adversary with a unique ID
	 */
	public Adversary() {
		super();
	}

	@Override
	public Boolean checkValidMoveDistance(Point source, Point destination) {
		int distance = Math.abs(source.x - destination.x) + Math.abs(source.y - destination.y);
		return distance <= maxMoveDistance;
	}
	
	@Override
	public Boolean checkValidMovePath(Point source, Point destination, 
			List<List<EntityType>> intermediateTypes) {
		int minX = Math.min(source.x, destination.x);
		int minY = Math.min(source.y, destination.y);
		Point relativeDestPoint = new Point(destination.x - minX, destination.y - minY);
		
		return isTraversable(intermediateTypes.get(relativeDestPoint.y).get(relativeDestPoint.x));		
	}
	
	@Override
	public int getMaxMoveDistance() {
		return maxMoveDistance;
	}
	
	/**
	 * Returns the damage that this adversary does when interacting with a player
	 * @return the value corresponding to the damage done
	 */
	public abstract int getDamage();
}
