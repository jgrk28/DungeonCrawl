package model;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class RuleCheckerTest {
	
	  private Key key;
	  private Exit exit;
	  private Player player1 = new Player();
	  private Player player2 = new Player();
	  private Player player3 = new Player();
	  private Adversary ghost1 = new Ghost();
	  private Adversary ghost2 = new Ghost();
	  private Adversary zombie = new Zombie();
	  private Map<Player, Point> playersPos;
	  private Map<Adversary, Point> adversariesPos;
	
	/**
	   * Creates a test level by initializing the levelMap, creating
	   * a map of players and their positions, creating a map of 
	   * adversaries and their positions, and initializing exitUnlocked
	   * and levelExited
	   * @return a new Level 
	   */
	  private Level makeTestLevel() {
		this.key = new Key(new Point(4, 17));
		this.exit = new Exit(new Point(7, 11));
		
		LevelMap map = new LevelMap();
	    List<LevelComponent> levelMap = map.initializeLevelMap();
	    
	    this.playersPos = new HashMap<>();
	    playersPos.put(this.player1, new Point(4, 2));
	    playersPos.put(this.player2, new Point(7, 10));
	    playersPos.put(this.player3, new Point(3, 17));
	    
	    this.adversariesPos = new HashMap<>();
	    adversariesPos.put(this.ghost1, new Point(7, 8));
	    adversariesPos.put(this.zombie, new Point(2, 17));
	    adversariesPos.put(this.ghost2, new Point(2, 14));
	    
	    boolean exitUnlocked = false;
	    boolean levelExited = false;
	    
	    return new LevelImpl(
	        playersPos,
	        adversariesPos,
	        levelMap,
	        exitUnlocked,
	        levelExited,
	        this.key,
	        this.exit
	    );
	  }
	  
	  public Dungeon makeTestDungeon() {
		  Level level = makeTestLevel();
		  List<Level> levels = new ArrayList<>(Arrays.asList(level));
		  List<Player> players = new ArrayList<>(Arrays.asList(this.player1, this.player2, this.player3));
		  List<Adversary> adversaries = new ArrayList<>(Arrays.asList(this.ghost1, this.zombie, this.ghost2));
		  return new Dungeon(players, adversaries, 1, levels);	 
	  }

	  public Dungeon makeTwoLevelDungeon() {
			Level level1 = makeTestLevel();
			Level level2 = makeTestLevel();
			List<Level> levels = new ArrayList<>(Arrays.asList(level1, level2));
			List<Player> players = new ArrayList<>(Arrays.asList(this.player1, this.player2, this.player3));
			List<Adversary> adversaries = new ArrayList<>(Arrays.asList(this.ghost1, this.zombie, this.ghost2));
			return new Dungeon(players, adversaries, 1, levels);
		}
	  
	  //Tests for invalid player movements
	  @Test
	  public void testPlayerActionBadTooLongDiag() {
		  RuleChecker dungeon = makeTestDungeon();
		  assertFalse(dungeon.checkValidMove(this.player1, new Point(6, 3)));
	  }

	  @Test
	  public void testPlayerActionBadOutOfBounds() {
		  RuleChecker dungeon = makeTestDungeon();
		  assertFalse(dungeon.checkValidMove(this.player2, new Point(9, 10)));
	  }

	  @Test
	  public void testPlayerActionBadWall() {
		  RuleChecker dungeon = makeTestDungeon();
		  assertFalse(dungeon.checkValidMove(this.player2, new Point(8, 10)));
	  }

	  @Test
	  public void testPlayerActionBadWallDiag() {
		  RuleChecker dungeon = makeTestDungeon();
		  assertFalse(dungeon.checkValidMove(this.player2, new Point(8, 9)));
	  }

	  @Test
	  public void testPlayerActionBadExit() {
		  RuleChecker dungeon = makeTestDungeon();
		  assertFalse(dungeon.checkValidMove(this.player2, new Point(7, 11)));
	  }

	  @Test
	  public void testPlayerActionBadIntoRoomWall() {
		  RuleChecker dungeon = makeTestDungeon();
		  assertFalse(dungeon.checkValidMove(this.player1, new Point(3, 1)));
	  }
	  
	  //Tests for valid player movements	  
	  @Test
	  public void testPlayerActionTwoSpacesUp() {
		  RuleChecker dungeon = makeTestDungeon();
		  assertTrue(dungeon.checkValidMove(this.player3, new Point(3, 15)));
	  }
	  
	  @Test
	  public void testPlayerActionJumpOverAdversary() {
		  RuleChecker dungeon = makeTestDungeon();
		  assertTrue(dungeon.checkValidMove(this.player3, new Point(1, 17)));
	  }
	  
	  @Test
	  public void testPlayerActionDiagonal() {
		  RuleChecker dungeon = makeTestDungeon();
		  assertTrue(dungeon.checkValidMove(this.player3, new Point(2, 16)));
	  }
	  
	  @Test
	  public void testPlayerMoveThroughDoor() {
		  RuleChecker dungeon = makeTestDungeon();
		  assertTrue(dungeon.checkValidMove(this.player1, new Point(2, 2)));
	  }
	  
	  @Test
	  public void testPlayerMoveToAdversary() {
		  RuleChecker dungeon = makeTestDungeon();
		  assertTrue(dungeon.checkValidMove(this.player2, new Point(7, 8)));
	  }
	  
	  @Test
	  public void testPlayerMoveToExit() {
		  RuleChecker dungeon = makeTestDungeon();
		  assertTrue(dungeon.checkValidMove(this.player2, new Point(7, 11)));
	  }
	  
	  @Test
	  public void testPlayerMoveToKey() {
		  RuleChecker dungeon = makeTestDungeon();
		  assertTrue(dungeon.checkValidMove(this.player3, new Point(4, 17)));
	  }

	  //Tests for invalid adversary movements
	  @Test
	  public void testAdversaryActionBadTooLong() {
		  RuleChecker dungeon = makeTestDungeon();
		  assertFalse(dungeon.checkValidMove(this.ghost1, new Point(7, 10)));
	  }

	  @Test
	  public void testAdversaryActionBadTooLongDiag() {
		  RuleChecker dungeon = makeTestDungeon();
		  assertFalse(dungeon.checkValidMove(this.ghost1, new Point(6, 9)));
	  }

	  @Test
	  public void testAdversaryActionBadWall() {
		  RuleChecker dungeon = makeTestDungeon();
		  assertFalse(dungeon.checkValidMove(this.zombie, new Point(2, 18)));
	  }
	  
	  //Tests for valid adversary movements	  
	  @Test
	  public void testAdversaryActionMoveUp() {
		  RuleChecker dungeon = makeTestDungeon();
		  assertTrue(dungeon.checkValidMove(this.ghost2, new Point(2, 13)));
	  }

	  @Test
	  public void testAdversaryActionMoveLeft() {
		  RuleChecker dungeon = makeTestDungeon();
		  assertTrue(dungeon.checkValidMove(this.ghost1, new Point(6, 8)));
	  }

	  @Test
	  public void testAdversaryActionMoveRight() {
		  RuleChecker dungeon = makeTestDungeon();
		  assertTrue(dungeon.checkValidMove(this.zombie, new Point(3, 17)));
	  }
	  
	  @Test
	  public void testAdversaryActionMoveDown() {
		  RuleChecker dungeon = makeTestDungeon();
		  assertTrue(dungeon.checkValidMove(this.ghost1, new Point(7, 9)));
	  }

		@Test
		public void testIsLevelOverActive() {
			Dungeon dungeon = makeTwoLevelDungeon();
			RuleChecker ruleChecker = dungeon;

			assertEquals(GameState.ACTIVE, ruleChecker.isLevelOver());

			Level firstLevel = dungeon.getCurrentLevel();
			beatLevel(firstLevel);
			Level secondLevel = dungeon.getCurrentLevel();

			assertEquals(GameState.ACTIVE, ruleChecker.isLevelOver());
		}

	@Test
	public void testIsLevelOverWon() {
		Dungeon dungeon = makeTwoLevelDungeon();
		RuleChecker ruleChecker = dungeon;

		Level firstLevel = dungeon.getCurrentLevel();
		beatLevel(firstLevel);

		assertEquals(GameState.WON, ruleChecker.isLevelOver());
	}

	@Test
	public void testIsLevelOverLost() {
		Dungeon dungeon = makeTwoLevelDungeon();
		RuleChecker ruleChecker = dungeon;

		Level firstLevel = dungeon.getCurrentLevel();
		loseLevel(firstLevel);

		assertEquals(GameState.LOST, ruleChecker.isLevelOver());
	}

	  @Test
		public void testIsGameOverActive() {
			Dungeon dungeon = makeTwoLevelDungeon();
			RuleChecker ruleChecker = dungeon;

			assertEquals(GameState.ACTIVE, ruleChecker.isGameOver());

			Level firstLevel = dungeon.getCurrentLevel();
			beatLevel(firstLevel);

			assertEquals(GameState.ACTIVE, ruleChecker.isGameOver());

			Level secondLevel = dungeon.getCurrentLevel();

			assertEquals(GameState.ACTIVE, ruleChecker.isGameOver());
		}

	@Test
	public void testIsGameOverWon() {
		Dungeon dungeon = makeTwoLevelDungeon();
		RuleChecker ruleChecker = dungeon;

		Level secondLevel = dungeon.getNextLevel();
		beatLevel(secondLevel);

		assertEquals(GameState.WON, ruleChecker.isGameOver());
	}

	@Test
	public void testIsGameOverLost() {
		Dungeon dungeon = makeTwoLevelDungeon();
		RuleChecker ruleChecker = dungeon;

		Level firstLevel = dungeon.getCurrentLevel();
		loseLevel(firstLevel);

		assertEquals(GameState.LOST, ruleChecker.isGameOver());
	}

	@Test
	public void testIsGameOverLostSecondLevel() {
		Dungeon dungeon = makeTwoLevelDungeon();
		RuleChecker ruleChecker = dungeon;

		Level firstLevel = dungeon.getCurrentLevel();
		beatLevel(firstLevel);

		Level secondLevel = dungeon.getNextLevel();
		loseLevel(secondLevel);

		assertEquals(GameState.LOST, ruleChecker.isGameOver());
	}

	private void beatLevel(Level level) {
		//Player3 get Key and die
		level.playerAction(player3, new Point(4, 17));
		level.playerAction(player3, new Point(2, 17));

		//Player 2 exit
		level.playerAction(player2, new Point(7, 11));

		//Player 1 die
		level.playerAction(player1, new Point(6, 2));
		level.playerAction(player1, new Point(6, 4));
		level.playerAction(player1, new Point(6, 6));
		level.playerAction(player1, new Point(6, 8));
		level.playerAction(player1, new Point(7, 8));
	}

	private void loseLevel(Level level) {
		//Player3 get Key and die
		level.playerAction(player3, new Point(4, 17));
		level.playerAction(player3, new Point(2, 17));

		//Player 2 die
		level.playerAction(player2, new Point(7, 8));

		//Player 1 die
		level.playerAction(player1, new Point(6, 2));
		level.playerAction(player1, new Point(6, 4));
		level.playerAction(player1, new Point(6, 6));
		level.playerAction(player1, new Point(6, 8));
		level.playerAction(player1, new Point(7, 8));
	}

	@Test
	public void testCheckValidGameStateNormal() {
		Dungeon dungeon = makeTwoLevelDungeon();
		RuleChecker ruleChecker = dungeon;
		assertTrue(ruleChecker.checkValidGameState());
	}

	@Test
	public void testCheckValidGameStateNoKey() {
		Level level1 = makeTestLevel();
		//Add null key
		LevelMap map = new LevelMap();
		Level level2 = new LevelImpl(map.initializeLevelMap(), null, this.exit);
		List<Level> levels = new ArrayList<>(Arrays.asList(level1, level2));
		List<Player> players = new ArrayList<>(Arrays.asList(this.player1, this.player2, this.player3));
		List<Adversary> adversaries = new ArrayList<>(Arrays.asList(this.ghost1, this.zombie, this.ghost2));
		RuleChecker ruleChecker = new Dungeon(players, adversaries, 1, levels);
		assertFalse(ruleChecker.checkValidGameState());
	}

	@Test
	public void testCheckValidGameStateNoExit() {
		Level level1 = makeTestLevel();
		//Add no exit
		LevelMap map = new LevelMap();
		Level level2 = new LevelImpl(map.initializeLevelMap(), this.key, null);
		List<Level> levels = new ArrayList<>(Arrays.asList(level1, level2));
		List<Player> players = new ArrayList<>(Arrays.asList(this.player1, this.player2, this.player3));
		List<Adversary> adversaries = new ArrayList<>(Arrays.asList(this.ghost1, this.zombie, this.ghost2));
		RuleChecker ruleChecker = new Dungeon(players, adversaries, 1, levels);
		assertFalse(ruleChecker.checkValidGameState());
	}

	@Test
	public void testCheckValidGameStateBadExit() {
		Level level1 = makeTestLevel();
		//Add no exit
		LevelMap map = new LevelMap();
		Level level2 = new LevelImpl(
				new HashMap<Player, Point>(),
				new HashMap<Adversary, Point>(),
				map.initializeLevelMap(),
				false,
				true,
				this.key,
				this.exit
		);
		List<Level> levels = new ArrayList<>(Arrays.asList(level1, level2));
		List<Player> players = new ArrayList<>(Arrays.asList(this.player1, this.player2, this.player3));
		List<Adversary> adversaries = new ArrayList<>(Arrays.asList(this.ghost1, this.zombie, this.ghost2));
		RuleChecker ruleChecker = new Dungeon(players, adversaries, 1, levels);
		assertFalse(ruleChecker.checkValidGameState());
	}

	@Test
	public void testCheckValidGameStateBadPlayer() {
		Level level1 = makeTestLevel();
		Level level2 = makeTestLevel();
		List<Level> levels = new ArrayList<>(Arrays.asList(level1, level2));
		List<Player> players = new ArrayList<Player>(Arrays.asList(new Player(), this.player2, this.player3));
		List<Adversary> adversaries = new ArrayList<>(Arrays.asList(this.ghost1, this.zombie, this.ghost2));
		RuleChecker ruleChecker = new Dungeon(players, adversaries, 1, levels);
		assertFalse(ruleChecker.checkValidGameState());
	}

	@Test
	public void testCheckValidGameStateBadAdversary() {
		Level level1 = makeTestLevel();
		Level level2 = makeTestLevel();
		List<Level> levels = new ArrayList<>(Arrays.asList(level1, level2));
		List<Player> players = new ArrayList<>(Arrays.asList(this.player1, this.player2, this.player3));
		List<Adversary> adversaries = new ArrayList<Adversary>(Arrays.asList(new Ghost(), this.zombie, this.ghost2));
		RuleChecker ruleChecker = new Dungeon(players, adversaries, 1, levels);
		assertFalse(ruleChecker.checkValidGameState());
	}
}