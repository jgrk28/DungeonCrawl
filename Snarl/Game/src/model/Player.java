package model;

import modelView.EntityType;

/**
 * Represents a player within a game of Snarl
 */
public class Player implements Actor {

	@Override
	public EntityType getEntityType() {
		return EntityType.PLAYER;
	}

	@Override
	public InteractionResult getInteractionResult(EntityType entityType) {
		switch (entityType) {
			case SPACE: 
				return InteractionResult.NONE;
			case HALL_SPACE: 
				return InteractionResult.NONE;
			case KEY:
				return InteractionResult.FOUND_KEY;
			case EXIT:
				return InteractionResult.EXIT;		
			case GHOST:
				return InteractionResult.REMOVE_PLAYER;
			case ZOMBIE:
				return InteractionResult.REMOVE_PLAYER;
			default:
				throw new IllegalArgumentException("Illegal interaction entity for player");
		}
	}

}
