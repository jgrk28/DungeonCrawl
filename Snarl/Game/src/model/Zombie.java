package model;

import modelView.EntityType;

/**
 * Represents a Zombie within a game of Snarl
 */
public class Zombie extends Adversary {

	public Zombie(String name) {
		super(name);
	}

	public Zombie() {
		super();
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.ZOMBIE;
	}

}
