package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import modelView.DungeonModelView;
import modelView.EntityType;

public class Dungeon implements RuleChecker, DungeonModelView {
	
	//All players in the game regardless of status in current level
	private List<Player> players;
	
	//All adversaries in the game regardless of status in current level
	private List<Adversary> adversaries;
	
	//Current number of the level that the players are on. The first level
	//is represented as 1
	private int currLevel;
	
	//All levels in the game
	private List<Level> levels;
	
	//Initializes all the levels, sets the players and adversaries for the dungeon.
	//Players are not placed in any level yet
	public Dungeon(List<Player> players, List<Adversary> adversaries, int currLevel, List<Level> levels) {
		if (players.size() < 1 || players.size() > 4) {
			throw new IllegalArgumentException("Invalid number of players");
		}
		this.players = players;
		this.adversaries = adversaries;
		this.currLevel = currLevel;	
		this.levels = levels;
	}

	public Level startCurrentLevel() {
		Level currLevel = getCurrentLevel();
		currLevel.placeActors(this.players, this.adversaries);
		return currLevel;
	}

	/**
	 * Returns the current level
	 * @return The current level in the dungeon
	 */
	public Level getCurrentLevel() {
		Level currLevel = this.levels.get(this.currLevel - 1);
		return currLevel;
	}

	/**
	 * Returns the next level and increments the level counter
	 * @return The next level in the dungeon
	 */
	public Level getNextLevel() {
		currLevel++;
		return getCurrentLevel();
	}

	/**
	 * Checks if the current level is the last level in the dungeon
	 * @return True if the current level is the last level
	 */
	public Boolean isLastLevel() {
		return currLevel == levels.size();		
	}

	@Override
	public GameState isGameOver() {
		//Status of the current level
		GameState levelState = isLevelOver();
		
		if (levelState.equals(GameState.LOST)) {
			return GameState.LOST;
		}
		else if (levelState.equals(GameState.WON) && isLastLevel()) {
			return GameState.WON;
		}
		else {
			return GameState.ACTIVE;
		}
		
	}

	@Override
	public GameState isLevelOver() {
		Level level = getCurrentLevel();
		return level.isLevelOver();
	}

	@Override
	public Boolean checkValidMove(Actor actor, Point destination) {
		Level level = getCurrentLevel();
		return level.checkValidMove(actor, destination);
	}

	@Override
	public Boolean checkValidGameState() {
		//A level is invalid if the level has been exited while the exit is 
		//locked, if there is not exactly one key and exit, or if unknown players 
		//or adversaries are in the level
		for (Level level : this.levels) {
			if (!level.checkValidLevelState(this.players, this.adversaries)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int getCurrentLevelIndex() {
		return this.currLevel;
	}

	@Override
	public Boolean isPlayerAlive(Player player) {
		if (!players.contains(player)) {
			throw new IllegalArgumentException("The player is not in the dungeon");
		}
		Level currLevel = getCurrentLevel();
		return currLevel.isPlayerAlive(player);
	
	}

	@Override
	public List<List<EntityType>> getPlayerMap(Player player) {
		Level currLevel = getCurrentLevel();
		return currLevel.getPlayerMap(player);
	}
 
}
