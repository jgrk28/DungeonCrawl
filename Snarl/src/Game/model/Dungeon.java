package Game.model;

import Game.modelView.LevelModelView;
import java.awt.Point;
import java.util.List;

import Game.modelView.DungeonModelView;
import Game.modelView.EntityType;
import java.util.Map;

/**
 * Represents a dungeon for a game of Snarl. Manages the levels relevant 
 * to the dungeon, and checks that interactions within the dungeon are 
 * valid based on the rules of the game.
 */
public class Dungeon implements RuleChecker, DungeonModelView {
	
	//All players in the game regardless of status in current level
	private List<Player> players;
	
	//Current number of the level that the players are on. The first level
	//is represented as 1
	private int currLevel;
	
	//All levels in the game
	private List<Level> levels;

	/**
	 * Initializes a dungeon with all of the levels for the game, as well as the index
	 * for the current level
	 * @param players - the players playing this dungeon
	 * @param currLevel - the index of the current level
	 * @param levels - the list of all levels in the game
	 */
	public Dungeon(List<Player> players, int currLevel, List<Level> levels) {
		if (players.size() < 1 || players.size() > 4) {
			throw new IllegalArgumentException("Invalid number of players");
		}
		this.players = players;
		this.currLevel = currLevel;	
		this.levels = levels;
	}

	/**
	 * Starts the current level of the game and places the players and 
	 * adversaries in the level.
	 * @return the level
	 */
	public Level startCurrentLevel(List<Adversary> adversaries) {
		Level currLevel = getCurrentLevel();
		currLevel.placeActors(this.players, adversaries);
		return currLevel;
	}

	/**
	 * Starts the current level by placing all players and adversaries randomly
	 * within the level map
	 * @param adversaries - all adversaries in the level
	 * @return the level with the placed actors
	 */
	public Level startCurrentLevelRandom(List<Adversary> adversaries) {
		Level currLevel = getCurrentLevel();
		currLevel.placeActorsRandomly(this.players, adversaries);
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
	 * @return true if the current level is the last level
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
	public LevelModelView getCurrentLevelModelView() {
		return getCurrentLevel();
	}

	@Override
	public Point getPosition(Actor actor) {
		Level level = getCurrentLevel();
		return level.getActorPosition(actor);
	}

	@Override
	public List<Point> getVisibleDoors(Player player) {
		Level level = getCurrentLevel();
		return level.getVisibleDoors(player);
	}

	@Override
	public List<Item> getVisibleItems(Player player) {
		Level level = getCurrentLevel();
		return level.getVisibleItems(player);
	}

	@Override
	public Map<Actor, Point> getVisibleActors(Player player) {
		Level level = getCurrentLevel();
		return level.getVisibleActors(player);
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

	/**
	 * A level is invalid if the level has been exited while the exit is 
	 * locked, if there is not exactly one key and exit, if there is a key
	 * in the level but the exit is unlocked, or if unknown players are in the level
	 */
	@Override
	public Boolean checkValidGameState() {
		for (Level level : this.levels) {
			if (!level.checkValidLevelState(this.players)) {
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

	@Override
	public List<Point> getValidMoves(Player player) {
		Level currLevel = getCurrentLevel();
		return currLevel.getValidMoves(player);
	}

}
