package Game.model;

/**
 * Represents all possible interactions between two entities when they are put on the same
 * tile.
 */
public enum InteractionResult {
	EXIT, FOUND_KEY, DAMAGE_PLAYER, REMOVE_PLAYER, NONE, TELEPORT
}
