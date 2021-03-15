package Game.model;

import java.awt.Point;
import java.util.List;

import Game.modelView.EntityType;

/**
 * Represents an entity that has the ability to move and interact.
 */
public interface Actor {
	
	/** 
	 * Returns the EntityType that corresponds to this entity
	 * @return The type of entity 
	 */
	EntityType getEntityType();

	/**
	 * Returns the InteractionResult that corresponds to this actor moving into
	 * the given EntityType
	 * @return The type of InteractionResult generated
	 */
	InteractionResult getInteractionResult(EntityType entityType);

	/**
	 * Checks if the distance of the given move is valid for this actor
	 * @return true if the move distance is acceptable
	 */
	Boolean checkValidMoveDistance(Point source, Point destination);

	/**
	 * Checks if there is a valid path from the source to the destination given this actors
	 * rules of movement and the intermediate types given
	 * @param source - The absolute starting position of the move with respect to (0,0), the level
	 *  origin
	 * @param destination - The absolute ending position of the move with respect to (0,0), the level
	 * origin
	 * @param intermediateTypes - A matrix of EntityTypes in the smallest rectangle including the
	 * start and end position of the move
	 * @return true if there is a valid path
	 */
	Boolean checkValidMovePath(Point source, Point destination, List<List<EntityType>> intermediateTypes);
}
