package model;

import modelView.EntityType;

public interface Actor extends Entity {
	
	InteractionResult getInteractionResult(EntityType entityType);

}
