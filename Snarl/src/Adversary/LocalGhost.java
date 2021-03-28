package Adversary;

import java.awt.Point;
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
			return moveTowardsBoundary();
		} else {
			//If there are players in the LevelComponent, take 1 step towards the closest player
			return stepTowardsPlayer(closestPlayer);
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
	private Point moveTowardsBoundary() {
		//TODO implement this
		//Need to determine if Ghost's can only move into walls in Rooms
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
