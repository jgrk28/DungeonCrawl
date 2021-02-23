package modelView;

/**
 * Represents the type of an Entity
 * This includes:
 * - Wall (X)
 * - Space (.)
 * - Hall Space (*)
 * - Key (!)
 * - Exit (@)
 * - Player (P)
 * - Adversary - Ghost (G)
 * - Adversary - Zombie (Z)
 * - Empty - where no entities have been placed  (" ")
 */
public enum EntityType {
	WALL, SPACE, HALL_SPACE, KEY, EXIT, PLAYER, GHOST, ZOMBIE, EMPTY
}
