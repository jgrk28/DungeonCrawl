package Adversary;

import java.awt.Point;
import java.util.List;
import java.util.Set;

import Game.model.LevelComponent;

/**
 * The LocalZombie represents an automated adversary that communicates locally
 * during a game of Snarl. Zombies implement the relevant strategies for an 
 * automated adversary, and send turns to the GameManager based on their 
 * location and the state of the game. 
 */
public class LocalZombie extends AbstractLocalAdversary {
	
	/**
	 * Checks if there is a player in the Zombie's LevelComponent
	 * If there are players in the LevelComponent, take 1 step towards the closest player
	 * If there are no players in the LevelComponent, make some valid arbitrary move
	 */
	@Override
	public Point takeTurn() {
		//This will be null if there are no players in the Zombie's LevelComponent
		Point closestPlayer = findClosestPlayerLocation();
		if (closestPlayer == null) {
			//If there are no players in the LevelComponent, make some valid arbitrary move
			return arbitraryMove();
		} else {
			//If there are players in the LevelComponent, take 1 step towards the closest player
			return stepTowardsPoint(closestPlayer);
		}
	}
	
	/**
	 * Finds the closest player to the LocalZombie's current location
	 * @return the location of the closest player, if there is one. 
	 * Returns null if there are no players in the LevelComponent
	 */
	private Point findClosestPlayerLocation() {
		LevelComponent zombieComponent = this.level.findComponent(this.currentLocation);
		Point closestPlayer = null;
		Integer closestPlayerDist = null;
		for (Point playerLocation : playerLocations.values()) {
			if (zombieComponent.inComponent(playerLocation)) {
				if (closestPlayerDist == null) {
					closestPlayer = playerLocation;
					closestPlayerDist = Math.abs(playerLocation.x - this.currentLocation.x) 
							+ Math.abs(playerLocation.y - this.currentLocation.y);
				} else {
					int playerDist = Math.abs(playerLocation.x - this.currentLocation.x) 
							+ Math.abs(playerLocation.y - this.currentLocation.y);
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
	 * Returns a valid, arbitrary move of one tile in a cardinal direction
	 * @return
	 */
	private Point arbitraryMove() {
		List<Point> potentialMoves = generatePotentialMoves();
		
		for (Point move : potentialMoves) {
			if (checkValidMove(move)) {
				return move;
			}
		}	
		return this.currentLocation;
	}
	
	/**
	 * Checks that the move is valid for a Zombie, is not moving into the position 
	 * of another adversary, and would not place the Zombie on a door tile
	 * @return true if the move is valid, false otherwise
	 */
	protected Boolean checkValidMove(Point move) {
		LevelComponent zombieComponent = this.level.findComponent(this.currentLocation);
		Set<Point> doors = zombieComponent.getDoors().keySet();

		return this.level.checkValidMove(adversaryAvatar, move) 
		&& !adversaryLocations.values().contains(move)
		&& !doors.contains(move);
	}

}
