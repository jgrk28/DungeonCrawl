package model;

import modelView.EntityType;

/**
 * Represents a Zombie within a game of Snarl
 */
public class Zombie extends Adversary {

	@Override
	public EntityType getEntityType() {
		return EntityType.ZOMBIE;
	}

}
