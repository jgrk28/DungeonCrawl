package Game.model;

import Game.modelView.EntityType;

/**
 * A Ghost is a type of adversary that currently behaves just like any type of adversary
 * The ASCII representation of a Ghost is "G"
 */
public class Ghost extends Adversary {

	/**
	 * Initialize a Ghost with a unique name
	 * @param name - the name of the Ghost
	 */
	public Ghost(String name) {
		super(name);
	}

	/**
	 * Initialize a Ghost with a unique ID
	 */
	public Ghost() {
		super();
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.GHOST;
	}

}
