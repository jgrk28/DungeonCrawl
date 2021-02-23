package model;

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
}
