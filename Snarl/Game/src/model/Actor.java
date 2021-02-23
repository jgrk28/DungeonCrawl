package model;

import modelView.EntityType;

/**
 * Represents an entity that has the ability to move and interact.
 */
public interface Actor extends Entity {

	/**
	 * Returns the InteractionResult that corresponds to this actor moving into
	 * the given EntityType
	 * @return The type of InteractionResult generated
	 */
	InteractionResult getInteractionResult(EntityType entityType);

}
