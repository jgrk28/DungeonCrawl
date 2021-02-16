package modelView;

/**
 * Represents the type of an Entity
 * This includes:
 * - Walls
 * - Spaces
 * - Hall Spaces
 * - Empty - where no entities have been placed 
 * 
 * In an ASCII representation of the level, a Wall 
 * corresponds to "X", a Space corresponds to ".", 
 * a Hall Space corresponds to "*", and  an Empty Space 
 * is simply " "
 */
public enum EntityType {
	WALL, SPACE, HALL_SPACE, EMPTY
}
