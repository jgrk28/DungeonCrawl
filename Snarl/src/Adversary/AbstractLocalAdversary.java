package Adversary;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Common.AdversaryClient;
import Game.model.Adversary;
import Game.model.Level;
import Game.model.Player;

public abstract class AbstractLocalAdversary implements AdversaryClient {
	
	protected Level level;
	protected Map<Player, Point> playerLocations;
	protected Map<Adversary, Point> adversaryLocations;
	protected Point currentLocation;
	protected Adversary adversaryAvatar;
	
	@Override
	public void getLevelStart(Level startLevel) {
		this.level = startLevel;	
	}

	@Override
	public void updateActorLocations(Map<Player, Point> playerLocations, 
			Map<Adversary, Point> adversaryLocations,
			Adversary adversaryAvatar) {
		this.playerLocations = playerLocations;
		this.adversaryLocations = adversaryLocations;
		this.adversaryAvatar = adversaryAvatar;
		this.currentLocation = adversaryLocations.get(adversaryAvatar);				
	}
	
	/**
	 * TODO Add comments
	 * @return
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
	 * TODO Add comments
	 * @param closestPlayer
	 * @return
	 */
	protected Point stepTowardsPlayer(Point closestPlayer) {
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
		int closestMoveDist = Math.abs(closestMove.x - closestPlayer.x) 
				+ Math.abs(closestMove.y - closestPlayer.y);
		for (Point move : validMoves) {
			int nextMoveDist = Math.abs(move.x - closestPlayer.x) 
					+ Math.abs(move.y - closestPlayer.y);
			if (nextMoveDist < closestMoveDist) {
				closestMove = move;
				closestMoveDist = nextMoveDist;
			}
		}
		
		return closestMove;
		
	}
	
	/**
	 * TODO Add comments
	 * @param move
	 * @return
	 */
	abstract protected Boolean checkValidMove(Point move);

}
