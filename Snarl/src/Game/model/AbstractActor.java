package Game.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import Game.modelView.EntityType;

/**
 * Represents an Actor within a level. An Actor can be
 * a Player or Adversary. AbstractActor contains the 
 * common fields and methods for an Actor. 
 */
abstract public class AbstractActor implements Actor {
	
  //The Actor's name
  protected String name;

  //Initialize an Actor with a name
  public AbstractActor(String name) {
    this.name = name;
  }

  //If the Actor is not provided a name during construction,
  //use a random ID as a unique identifier 
  public AbstractActor() {
    this.name = UUID.randomUUID().toString();
  }

  /**
   * Checks if the provided name is the same as the name
   * of this Actor
   * @param name - the provided name
   * @return true if the provided name is the same as this
   * name
   */
  public Boolean hasName(String name) {
    return this.name.equals(name);
  }

  @Override
  public String getName() {
    return this.name;
  }

  /**
   * Determines if the given EntityType is traversable for a player
   * @param entityType - the EntityType
   * @return true if the EntityType is traversable
   */
  public Boolean isTraversable(EntityType entityType) {
	//If the interaction with the EntityType is possible, return true
	//Otherwise, catch the exception and return false
    try {
      getInteractionResult(entityType);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
  
   @Override
   public List<Point> getPotentialMoves(Point actorLocation) {
	   int maxMoveDistance = this.getMaxMoveDistance();
	   Set<Point> potentialMoves = new HashSet<>();
	   potentialMoves.add(actorLocation);
	   for (int i = 0; i < maxMoveDistance; i++) {
		   Set<Point> adjacentMoves = new HashSet<>();
		   for (Point move : potentialMoves) {
			   adjacentMoves.addAll(getAdjacentMoves(move));
		   }
       potentialMoves.addAll(adjacentMoves);
	   }
	   return new ArrayList<>(potentialMoves);
   }
   
   /**
    * Gets all moves adjacent to the given point
    * @param point - the central point
    * @return a set of points adjacent to the provided point
    */
   private Set<Point> getAdjacentMoves(Point point) {
	 Set<Point> moves = new HashSet<>();
	 moves.add(new Point(point.x + 1, point.y));
     moves.add(new Point(point.x - 1, point.y));
     moves.add(new Point(point.x, point.y + 1));
     moves.add(new Point(point.x, point.y - 1));
     return moves;
   }
}









