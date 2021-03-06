package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import modelView.EntityType;

/**
 * Represents a player within a game of Snarl
 */
public class Player implements Actor {

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
		return distance <= 2;		
	}
	
	@Override
	public Boolean checkValidMovePath(List<List<EntityType>> intermediateTypes) {
		Point sourcePoint = new Point(0,0);
		int numCols = intermediateTypes.size();
		if (numCols == 0) {
			throw new IllegalArgumentException("Source to destination map must have at least one EntityType");
		}
		int numRows = intermediateTypes.get(0).size();
		Point destPoint = new Point(numRows, numCols);
		
		List<Point> visited = new ArrayList<>();
		
		if (sourcePoint.equals(destPoint)) {
			return true;
		} else {
			visited.add(sourcePoint);
			return depthFirstSearchRecursive(visited, sourcePoint, destPoint, intermediateTypes);
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

}
