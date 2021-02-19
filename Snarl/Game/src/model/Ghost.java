package model;

import modelView.EntityType;

public class Ghost extends Adversary {

	@Override
	public EntityType getEntityType() {
		return EntityType.GHOST;
	}

}
