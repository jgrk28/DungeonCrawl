package Adversary;

import java.awt.Point;
import java.util.List;
import java.util.Set;

import Game.model.LevelComponent;

/**
 * The client for adversaries in the game 
 * TODO: This class will be implemented in future milestones
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
			return stepTowardsPlayer(closestPlayer);
		}
	}
	
	/**
	 * TODO Add comments
	 * @return
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
	 * TODO Add comments
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
	 * TODO Add comments
	 * @return
	 */
	protected Boolean checkValidMove(Point move) {
		LevelComponent zombieComponent = this.level.findComponent(this.currentLocation);
		Set<Point> doors = zombieComponent.getDoors().keySet();

		return this.level.checkValidMove(adversaryAvatar, move) 
		&& !adversaryLocations.values().contains(move)
		&& !doors.contains(move);
	}

}
