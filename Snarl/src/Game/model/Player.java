package Game.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import Game.modelView.EntityType;

/**
 * Represents a player within a game of Snarl
 */
public class Player extends AbstractActor {
	private static final int maxMoveDistance = 2;
	private static final int sightBoxWidth = 2;

	/**
	 * Initialize a Player with a unique name
	 * @param name - the name of the Player
	 */
	public Player(String name) {
		super(name);
	}

	/**
	 * Initialize a Player with a unique ID
	 */
	public Player() {
		super();
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.PLAYER;
	}

	@Override
	public InteractionResult getInteractionResult(EntityType entityType) {
		switch (entityType) {
			case HALL_SPACE:
				//Same as SPACE
			case SPACE:
				return InteractionResult.NONE;
			case KEY:
				return InteractionResult.FOUND_KEY;
			case EXIT:
				return InteractionResult.EXIT;		
			case GHOST:
				//Same as ZOMBIE
			case ZOMBIE:
				return InteractionResult.REMOVE_PLAYER;
			default:
				throw new IllegalArgumentException("Illegal interaction entity for player");
		}
	}

	@Override
	public Boolean checkValidMoveDistance(Point source, Point destination) {
		int distance = Math.abs(source.x - destination.x) 
				+ Math.abs(source.y - destination.y);
		return distance <= maxMoveDistance;
	}
	
	@Override
	public Boolean checkValidMovePath(Point source, Point destination, 
			List<List<EntityType>> intermediateTypes) {
		
		//Determine the relative source and destination 
		int minX = Math.min(source.x, destination.x);
		int minY = Math.min(source.y, destination.y);
		Point relativeSourcePoint = new Point(source.x - minX, source.y - minY);
		Point relativeDestPoint = new Point(destination.x - minX, destination.y - minY);
		
		List<Point> visited = new ArrayList<>();
		
		//If the player is located at the destination, the move path is valid
		//Otherwise, conduct depth first search to locate a valid move path
		if (relativeSourcePoint.equals(relativeDestPoint)) {
			return true;
		} else {
			visited.add(relativeSourcePoint);
			return depthFirstSearchRecursive(visited, relativeSourcePoint, 
					relativeDestPoint, intermediateTypes);
		}
		
	}
	
	/**
	 * Conduct depth first search on potential paths between the source
	 * and destination
	 * @param visited - visited neighbors 
	 * @param currPoint - the current point being searched
	 * @param destPoint - the destination that the player is trying to move to
	 * @param intermediateTypes - a map of all intermediate entities between the 
	 * source and destination
	 * @return true if there is a valid path to the destination
	 */
	private Boolean depthFirstSearchRecursive(List<Point> visited, Point currPoint, 
			Point destPoint, List<List<EntityType>> intermediateTypes) {
		List<Point> neighbors = getNeighbors(currPoint, destPoint);
		
		for (Point point : neighbors) {
			
			if (visited.contains(point)) {
				continue;
			} else {
				visited.add(point);
			}
			//Get the entity at this location
			EntityType entityType = intermediateTypes.get(point.y).get(point.x);
			
			//If the entity at this location is not traversable, keep looking
			if (!isTraversable(entityType)) {
				continue;
			} 
			else if (point.equals(destPoint)) {
				return true;
			} else {
				if (depthFirstSearchRecursive(visited, point, destPoint, intermediateTypes)) {
					return true;
				}
			}
		}	
		//if no valid move paths are found
		return false;
	}
	
	/**
	 * Gets all the neighbors around the given point
	 * @param currPoint - the current point
	 * @param bottomRightBound - the bound that the point is contained within
	 * @return the list of points directly surrounding the given point
	 */
	private List<Point> getNeighbors(Point currPoint, Point bottomRightBound) {
		List<Point> neighbors = new ArrayList<>();
		
		//If the neighbor is within the bounds, add it to the list
		if ((currPoint.x + 1) <= bottomRightBound.x) {
			neighbors.add(new Point(currPoint.x + 1, currPoint.y));		
		}
		if ((currPoint.x - 1) >= 0) {
			neighbors.add(new Point(currPoint.x - 1, currPoint.y));		
		}
		if ((currPoint.y + 1) <= bottomRightBound.y) {
			neighbors.add(new Point(currPoint.x, currPoint.y + 1));	
		}
		if ((currPoint.y - 1) >= 0) {
			neighbors.add(new Point(currPoint.x, currPoint.y - 1));	
		}
		return neighbors;		
	}

	/**
	 * Returns the subsection of the level map that the player can see
	 * based on their position and range of sight
	 * @param fullLevel - the full map of EntityType for the level
	 * @param playerLocation - the player's location in the level
	 * @return the cropped map that the player can see
	 * @throws IllegalArgumentException if the fullLevel contains no components
	 */
	public List<List<EntityType>> cropViewableMap(
			List<List<EntityType>> fullLevel,
			Point playerLocation) {
		List<List<EntityType>> croppedMap = new ArrayList<>();
		if (fullLevel.size() == 0) {
			throw new IllegalArgumentException("Full Level map contains no components");
		}

		//Determine the visible range for the player
		int fullLevelYMin = playerLocation.y - sightBoxWidth;
		int fullLevelYMax = playerLocation.y + sightBoxWidth;
		int fullLevelXMin = playerLocation.x - sightBoxWidth;
		int fullLevelXMax = playerLocation.x + sightBoxWidth;
		
		//Iterate through the visible range and get the EntityType at the corresponding
		//location in the full map
		for (int fullLevelY = fullLevelYMin; fullLevelY <= fullLevelYMax; fullLevelY++) {
			List<EntityType> croppedRow = new ArrayList<>();
			for (int fullLevelX = fullLevelXMin; fullLevelX <= fullLevelXMax; fullLevelX++) {
				if (fullLevelY >= 0
						&& fullLevelX >= 0
						&& fullLevelY <= fullLevel.size()
						&& fullLevelY <= fullLevel.get(0).size()) {
					EntityType entityType = fullLevel.get(fullLevelY).get(fullLevelX);
					croppedRow.add(entityType);
				}
			}
			croppedMap.add(croppedRow);
		}
		return croppedMap;
	}

}
