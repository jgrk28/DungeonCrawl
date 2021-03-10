package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import modelView.EntityType;

/**
 * Represents a player within a game of Snarl
 */
public class Player implements Actor {
	private static final int maxMoveDistance = 2;
	private static final int sightBoxWidth = 2;

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
		int distance = Math.abs(source.x - destination.x) + Math.abs(source.y - destination.y);
		return distance <= maxMoveDistance;
	}
	
	@Override
	public Boolean checkValidMovePath(Point source, Point destination, 
			List<List<EntityType>> intermediateTypes) {
		int minX = Math.min(source.x, destination.x);
		int minY = Math.min(source.y, destination.y);
		Point relativeSourcePoint = new Point(source.x - minX, source.y - minY);
		Point relativeDestPoint = new Point(destination.x - minX, destination.y - minY);
		
		List<Point> visited = new ArrayList<>();
		
		if (relativeSourcePoint.equals(relativeDestPoint)) {
			return true;
		} else {
			visited.add(relativeSourcePoint);
			return depthFirstSearchRecursive(visited, relativeSourcePoint, relativeDestPoint, intermediateTypes);
		}
		
	}
	
	private Boolean depthFirstSearchRecursive(List<Point> visited, Point currPoint, 
			Point destPoint, List<List<EntityType>> intermediateTypes) {
		List<Point> neighbors = getNeighbors(currPoint, destPoint);
		
		for (Point point : neighbors) {
			
			if (visited.contains(point)) {
				continue;
			} else {
				visited.add(point);
			}
			EntityType entityType = intermediateTypes.get(point.y).get(point.x);
			
			if (!isTraversable(entityType)) {
				continue;
			} else if (point.equals(destPoint)) {
				return true;
			} else {
				if (depthFirstSearchRecursive(visited, point, destPoint, intermediateTypes)) {
					return true;
				}
			}
		}	
		return false;
	}
	
	private List<Point> getNeighbors(Point currPoint, Point bottomRightBound) {
		List<Point> neighbors = new ArrayList<>();
		
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
	
	private Boolean isTraversable(EntityType entityType) {
		try {
			getInteractionResult(entityType);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public List<List<EntityType>> cropViewableMap(
			List<List<EntityType>> fullLevel,
			Point playerLocation) {
		List<List<EntityType>> croppedMap = new ArrayList<>();
		if (fullLevel.size() == 0) {
			throw new IllegalArgumentException("Full Level map contains no components");
		}

		int fullLevelYMin = playerLocation.y - this.sightBoxWidth;
		int fullLevelYMax = playerLocation.y + this.sightBoxWidth;
		int fullLevelXMin = playerLocation.x - this.sightBoxWidth;
		int fullLevelXMax = playerLocation.x + this.sightBoxWidth;
		for (int fullLevelY = fullLevelYMin; fullLevelY <= fullLevelYMax; fullLevelY++) {
			List<EntityType> croppedRow = new ArrayList<>();
			for (int fullLevelX = fullLevelXMin; fullLevelX <= fullLevelXMax; fullLevelX++) {
				if (fullLevelY >= 0
						&& fullLevelX >= 0
						&& fullLevelY < fullLevel.size()
						&& fullLevelY < fullLevel.get(0).size()) {
					EntityType entityType = fullLevel.get(fullLevelY).get(fullLevelX);
					croppedRow.add(entityType);
				}
			}
			croppedMap.add(croppedRow);
		}
		return croppedMap;

	}

}
