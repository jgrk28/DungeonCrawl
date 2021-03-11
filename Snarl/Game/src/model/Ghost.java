package model;

import modelView.EntityType;

/**
 * A Ghost is a type of adversary that currently behaves just like any type of adversary
 * The ASCII representation of a Ghost is "G"
 */
public class Ghost extends Adversary {

	public Ghost(String name) {
		super(name);
	}

	public Ghost() {
		super();
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.GHOST;
	}

}
