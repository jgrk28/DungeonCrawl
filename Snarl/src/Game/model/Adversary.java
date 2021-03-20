package Game.model;

import java.awt.Point;
import java.util.List;

import Game.modelView.EntityType;

/**
 * Represents an adversary for the players in the level.
 */
public abstract class Adversary extends AbstractActor {
	
	private static final int maxMoveDistance = 1;

	public Adversary(String name) {
		super(name);
	}

	public Adversary() {
		super();
	}

	@Override
	public InteractionResult getInteractionResult(EntityType entityType) {
		//An adversary cannot move to a space that has a key, exit, wall, or another adversary
		switch (entityType) {
			case HALL_SPACE:
				//This will return the same result as space
			case SPACE:
				return InteractionResult.NONE;
			case PLAYER:
				return InteractionResult.REMOVE_PLAYER;
			default:
				throw new IllegalArgumentException("Illegal interaction entity for adversary");
		}
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
		return this.maxMoveDistance;
	}
}
