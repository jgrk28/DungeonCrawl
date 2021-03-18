package Game.model;

import Game.modelView.EntityType;

/**
 * Represents a Zombie within a game of Snarl
 * The ASCII representation of a Ghost is "Z"
 */
public class Zombie extends Adversary {

	/**
	 * Initialize a Zombie with a unique name
	 * @param name - the name of the Zombie
	 */
	public Zombie(String name) {
		super(name);
	}

	/**
	 * Initialize a Zombie with a unique ID
	 */
	public Zombie() {
		super();
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.ZOMBIE;
	}

}
