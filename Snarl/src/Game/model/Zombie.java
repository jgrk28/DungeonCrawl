package Game.model;

import Game.modelView.EntityType;

/**
 * Represents a Zombie within a game of Snarl
 * The ASCII representation of a Ghost is "Z"
 */
public class Zombie extends Adversary {
	
	private static final int damage = 7;

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
	public InteractionResult getInteractionResult(EntityType entityType) {
		//An adversary cannot move to a space that has a wall, or another adversary
		switch (entityType) {
			case KEY:
				//This will return the same result as space
			case EXIT:
				//This will return the same result as space
			case HALL_SPACE:
				//This will return the same result as space
			case SPACE:
				return InteractionResult.NONE;
			case PLAYER:
				return InteractionResult.REMOVE_PLAYER;
			default:
				throw new IllegalArgumentException("Illegal interaction entity for adversary");
		}
	}

	@Override
	public InteractionResult getTileInteractionResult(Tile destTile) {
		EntityType destType = destTile.getEntityType();
		//An adversary cannot move to a space that has a wall, or another adversary
		switch (destType) {
			case KEY:
				//This will return the same result as space
			case EXIT:
				//This will return the same result as space
			case HALL_SPACE:
				//This will return the same result as space
			case SPACE:
				return InteractionResult.NONE;
			case PLAYER:
				Player player = (Player)destTile.getActor();
				if (player.isFatal(this.getDamage())) {
					return InteractionResult.REMOVE_PLAYER;
				} else {
					return InteractionResult.DAMAGE_PLAYER;
				}
			case ZOMBIE:
				//This will return the same result as ghost
			case GHOST:
				if (this.equals(destTile.getActor())) {
					return InteractionResult.NONE;
				} else {
					throw new IllegalArgumentException("Illegal interaction entity for adversary");
				}
			default:
				throw new IllegalArgumentException("Illegal interaction entity for adversary");
		}
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.ZOMBIE;
	}

	@Override
	public int getDamage() {
		return damage;
	}

}
