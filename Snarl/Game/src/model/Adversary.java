package model;

import modelView.EntityType;

public abstract class Adversary implements Actor {
  
	//An adversary cannot move to a space that has a key, exit, wall, or another adversary
	public InteractionResult getInteractionResult(EntityType entityType) {
		switch (entityType) {
			case SPACE: 
				return InteractionResult.NONE;
			case HALL_SPACE: 
				return InteractionResult.NONE;
			case PLAYER:
				return InteractionResult.REMOVE_PLAYER;
			default:
				throw new IllegalArgumentException("Illegal interaction entity for adversary");
		}
	}
}
