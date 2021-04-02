package Adversary;

import Game.model.LevelImpl;
import Game.model.Player;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Common.AdversaryClient;
import Game.model.Adversary;
import Game.model.Level;

/**
 * Represents a local adversary client. AbstractLocalAdversary contains 
 * the common fields and methods for LocalGhost and LocalZombie. 
 */
public abstract class AbstractLocalAdversary implements AdversaryClient {
	
	//The full level information for the current level
	protected Level level;

	//The level with the current actors placed
	protected Level occupiedLevel;
	
	//All player and adversary locations in the level
	protected Map<Player, Point> playerLocations;
	protected Map<Adversary, Point> adversaryLocations;
	
	//The current location of this AdversaryClient in the current level
	protected Point currentLocation;
	
	//The avatar that represents the AdversaryClient within the game
	protected Adversary adversaryAvatar;
	
	@Override
	public void getLevelStart(Level startLevel) {
		this.level = startLevel;	
	}

	@Override
	public void updateActorLocations(Map<Player, Point> playerLocations,
			Map<Adversary, Point> adversaryLocations, Adversary adversaryAvatar) {
		//if they exist, remove old adversaries and players from our level

		this.playerLocations = playerLocations;
		this.adversaryLocations = adversaryLocations;
		this.adversaryAvatar = adversaryAvatar;
		this.currentLocation = adversaryLocations.get(adversaryAvatar);

		//add new adversaries and players from our level

		//This is not actually copying the level because the level map is not copied
		this.occupiedLevel = new LevelImpl(
				this.playerLocations,
				this.adversaryLocations,
				this.level.getLevelMap(),
				this.level.getExitUnlocked(),
				this.level.getLevelExited(),
				this.level.getItems());
	}

	/**
	 * Generates all potential moves for this AdversaryClient based on their
	 * current location. A potential move is at most one tile in a cardinal 
	 * direction
	 * @return a list of all potential moves
	 */
	protected List<Point> generatePotentialMoves() {
		List<Point> potentialMoves = new ArrayList<>();
		
		potentialMoves.add(new Point(this.currentLocation.x + 1, this.currentLocation.y));
		potentialMoves.add(new Point(this.currentLocation.x - 1, this.currentLocation.y));
		potentialMoves.add(new Point(this.currentLocation.x, this.currentLocation.y + 1));
		potentialMoves.add(new Point(this.currentLocation.x, this.currentLocation.y - 1));
		
		return potentialMoves;
		
	}
	
	/**
	 * Finds the best move towards the destination point
	 * @param destPoint - the destination to move towards
	 * @return the closest valid move to the destination
	 */
	protected Point stepTowardsPoint(Point destPoint) {
		List<Point> potentialMoves = generatePotentialMoves();
		List<Point> validMoves = new ArrayList<>();
		
		//Find all valid moves
		for (Point move : potentialMoves) {
			if (checkValidMove(move)) {
				validMoves.add(move);
			}
		}
		
		if (validMoves.isEmpty()) {
			return this.currentLocation;
		}
		
		//Find the best move
		Point closestMove = validMoves.get(0);
		int closestMoveDist = Math.abs(closestMove.x - destPoint.x)
				+ Math.abs(closestMove.y - destPoint.y);
		for (Point move : validMoves) {
			int nextMoveDist = Math.abs(move.x - destPoint.x)
					+ Math.abs(move.y - destPoint.y);
			if (nextMoveDist < closestMoveDist) {
				closestMove = move;
				closestMoveDist = nextMoveDist;
			}
		}		
		return closestMove;		
	}
	
	/**
	 * Checks that the move is valid for this AdversaryClient
	 * @param move - the move to check
	 * @return true if the move is valid, false otherwise
	 */
	abstract protected Boolean checkValidMove(Point move);

}
