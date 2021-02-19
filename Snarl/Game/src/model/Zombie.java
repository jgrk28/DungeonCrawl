package model;

import modelView.EntityType;

public class Zombie extends Adversary {

	@Override
	public EntityType getEntityType() {
		return EntityType.ZOMBIE;
	}

}
