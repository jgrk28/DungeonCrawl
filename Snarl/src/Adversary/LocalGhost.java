package Adversary;

import Game.model.Room;
import Game.model.Tile;
import Game.model.Wall;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import Game.model.LevelComponent;

/**
 * TODO Add comments
 */
public class LocalGhost extends AbstractLocalAdversary {
	
	private static final int chaseRadius = 6;

	/**
	 * Checks if there is a player within a 6 step radius of the Ghost
	 * If players exist within that radius, take 1 step towards the closet player
	 * If there are no players in that radius, enter a wall tile and move to a new room
	 */
	@Override
	public Point takeTurn() {
		//This will be null if there are no players within the Ghost's chase radius
		Point closestPlayer = findClosestPlayerLocation();
		if (closestPlayer == null) {
			//If there are no players in the chase radius, the ghost should move towards a wall
			Point closestDoor = findClosestDoor();
			return stepTowardsBoundary(closestDoor);
		} else {
			//If there are players in the LevelComponent, take 1 step towards the closest player
			return stepTowardsPoint(closestPlayer);
		}
	}
		
	/**
	 * TODO Add comments
	 * @return
	 */
	private Point findClosestPlayerLocation() {
		Point closestPlayer = null;
		Integer closestPlayerDist = null;
		for (Point playerLocation : playerLocations.values()) {
			int playerDist = Math.abs(playerLocation.x - this.currentLocation.x) 
					+ Math.abs(playerLocation.y - this.currentLocation.y); 
			if (playerDist <= chaseRadius) {
				if (closestPlayerDist == null) {
					closestPlayer = playerLocation;
					closestPlayerDist = playerDist;
				} else {
					if (playerDist < closestPlayerDist) {
						closestPlayer = playerLocation;
						closestPlayerDist = playerDist;
					}					
				}
			}			
		}
		return closestPlayer;
	}

	/**
	 * TODO Add comments
	 * @return
	 */
	private Point findClosestDoor() {
		LevelComponent component = this.level.findComponent(this.currentLocation);

		Set<Point> doors = component.getDoors().keySet();

		Point closestDoor = null;
		Integer closestDoorDist = null;
		for (Point door : doors) {
			int currDoorDist = Math.abs(door.x - this.currentLocation.x)
					+ Math.abs(door.y - this.currentLocation.y);
			if (closestDoor == null) {
				closestDoor = door;
				closestDoorDist = currDoorDist;
			} else if (currDoorDist < closestDoorDist) {
				closestDoor = door;
				closestDoorDist = currDoorDist;
			}
		}
		return closestDoor;
	}
	
	/**
	 * TODO Add comments
	 * @return
	 */
	private Point stepTowardsBoundary(Point closestDoor) {
		LevelComponent component = this.level.findComponent(this.currentLocation);
		if (this.currentLocation.equals(closestDoor)) {
			List<Point> possibleMoves = generatePotentialMoves();
			for (Point move : possibleMoves) {
				Tile tile = component.getDestinationTile(move);
				if (tile instanceof Wall) {
					return move;
				}
			}
			throw new IllegalStateException("No Wall next to door");
		} else {
			return stepTowardsPoint(closestDoor);
		}
	}
	
	/**
	 * TODO Add comments
	 * @return
	 */
	protected Boolean checkValidMove(Point move) {
		return this.level.checkValidMove(adversaryAvatar, move) 
		&& !adversaryLocations.values().contains(move);
	}

}
