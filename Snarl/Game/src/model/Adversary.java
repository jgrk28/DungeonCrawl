package model;

import java.awt.Point;
import java.util.List;

import modelView.EntityType;

/**
 * Represents an adversary for the players in the level.
 */
public abstract class Adversary implements Actor {
  
	//An adversary cannot move to a space that has a key, exit, wall, or another adversary
	public InteractionResult getInteractionResult(EntityType entityType) {
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
		return distance <= 1;		
	}
	
	@Override
	public Boolean checkValidMovePath(List<List<EntityType>> intermediateTypes) {
		int numCols = intermediateTypes.size();
		if (numCols == 0) {
			throw new IllegalArgumentException("Source to destination map must have at least one EntityType");
		}
		int numRows = intermediateTypes.get(0).size();
		
		return isTraversable(intermediateTypes.get(numCols).get(numRows));		
	}
	
	private Boolean isTraversable(EntityType entityType) {
		try {
			getInteractionResult(entityType);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
}
