package Adversary;

import Game.model.Tile;
import Game.model.Wall;
import java.awt.Point;
import java.util.List;
import java.util.Set;

import Game.model.LevelComponent;

/**
 * The LocalGhost represents an automated adversary that communicates locally
 * during a game of Snarl. Ghosts implement the relevant strategies for an 
 * automated adversary, and send turns to the GameManager based on their 
 * location and the state of the game. 
 */
public class LocalGhost extends AbstractLocalAdversary {

	private static final int chaseRadius = 6;

	/**
	 * Checks if there is a player within a 6 step radius of the Ghost
	 * If players exist within that radius, take 1 step towards the closet player
	 * If there are no players in that radius, move towards the closest door and
	 * teleport to another room by moving into the adjacent wall
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
	 * Finds the closest player to the LocalGhost's current location
	 * @return the location of the closest player, if there is one. 
	 * Returns null if no player is within the chase radius
	 */
	private Point findClosestPlayerLocation() {
		Point closestPlayer = null;
		Integer closestPlayerDist = null;
		
		//Iterate through all player locations
		for (Point playerLocation : playerLocations.values()) {
			int playerDist = Math.abs(playerLocation.x - this.currentLocation.x) 
					+ Math.abs(playerLocation.y - this.currentLocation.y); 
			//If the player is within the chase radius, check if they are
			//the closest player
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
	 * Finds the closest door to the LocalGhost's current location
	 * @return the location of the closest door 
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
	 * Steps towards the boundary of the room based on the location
	 * of the closest door. If the LocalGhost is on the door, moves 
	 * into an adjacent wall to teleport to another room
	 * @return the location that is one step towards the closest door,
	 * or location of the wall adjacent to the door
	 */
	private Point stepTowardsBoundary(Point closestDoor) {
		if (this.currentLocation.equals(closestDoor)) {
			List<Point> possibleMoves = generatePotentialMoves();
			for (Point move : possibleMoves) {
				LevelComponent component = this.level.findComponent(move);
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
	 * Checks that the move is valid for a Ghost, and that the
	 * move is not moving into the position of another adversary
	 * @return true if the move is valid, false otherwise
	 */
	protected Boolean checkValidMove(Point move) {
		return this.level.checkValidMove(adversaryAvatar, move);
	}

}
