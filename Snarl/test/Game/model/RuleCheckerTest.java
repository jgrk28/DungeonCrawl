package Game.model;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

//Tests for the RuleChecker interface
public class RuleCheckerTest {

	private ModelCreator creator;
	private Dungeon dungeon;
	private Dungeon simpleDungeon;
	private RuleChecker ruleChecker;
	private RuleChecker simpleRuleChecker;
	private List<Level> levels;

	private Key key;
	private Exit exit;
	private Player player1;
	private Player player2;
	private Player player3;
	private Adversary ghost1;
	private Adversary ghost2;
	private Adversary zombie;
	private List<Player> players;
	private List<Adversary> adversaries;
	private List<LevelComponent> levelMap;

	//Initialize all model components for use
	@Before
	public void initLevelComponents() {
		this.creator = new ModelCreator();
		this.dungeon = this.creator.initializeDungeonStarted();
		this.ruleChecker = this.dungeon;
		this.adversaries = this.creator.initializeDungeonAdversaries();
		this.simpleDungeon = this.creator.initializeSimpleDungeon();
		this.simpleDungeon.startCurrentLevel(this.adversaries);
		this.simpleRuleChecker = this.simpleDungeon;
		this.levels = this.creator.initializeDungeonLevelsStarted();
		this.players = this.creator.initializeDungeonPlayers();
		this.levelMap = this.creator.initializeLevel1Map();
		this.player1 = creator.getPlayer1();
		this.player2 = creator.getPlayer2();
		this.player3 = creator.getPlayer3();
		this.ghost1 = creator.getGhost1();
		this.ghost2 = creator.getGhost2();
		this.zombie = creator.getZombie1();
		this.key = creator.getLevel1Key();
		this.exit = creator.getLevel1Exit();
	}
	  
	  //Tests for invalid player movements
	  @Test
	  public void testPlayerActionBadTooLongDiag() {
		  assertFalse(this.ruleChecker.checkValidMove(this.player1, new Point(6, 3)));
	  }

	  @Test
	  public void testPlayerActionBadOutOfBounds() {
		  assertFalse(this.ruleChecker.checkValidMove(this.player2, new Point(9, 10)));
	  }

	  @Test
	  public void testPlayerActionBadWall() {
		  assertFalse(this.ruleChecker.checkValidMove(this.player2, new Point(8, 10)));
	  }

	  @Test
	  public void testPlayerActionBadWallDiag() {
		  assertFalse(this.ruleChecker.checkValidMove(this.player2, new Point(8, 9)));
	  }

	  @Test
	  public void testPlayerActionBadIntoRoomWall() {
		  assertFalse(this.ruleChecker.checkValidMove(this.player1, new Point(3, 1)));
	  }
	  
	  //Tests for valid player movements	  
	  @Test
	  public void testPlayerActionTwoSpacesUp() {
		  assertTrue(this.ruleChecker.checkValidMove(this.player3, new Point(3, 15)));
	  }
	  
	  @Test
	  public void testPlayerActionJumpOverAdversary() {
		  assertTrue(this.ruleChecker.checkValidMove(this.player3, new Point(1, 17)));
	  }
	  
	  @Test
	  public void testPlayerActionDiagonal() {
		  assertTrue(this.ruleChecker.checkValidMove(this.player3, new Point(2, 16)));
	  }
	  
	  @Test
	  public void testPlayerMoveThroughDoor() {
		  assertTrue(this.ruleChecker.checkValidMove(this.player1, new Point(2, 2)));
	  }
	  
	  @Test
	  public void testPlayerMoveToAdversary() {
		  assertTrue(this.ruleChecker.checkValidMove(this.player2, new Point(7, 8)));
	  }
	  
	  @Test
	  public void testPlayerMoveToExit() {
		  assertTrue(this.ruleChecker.checkValidMove(this.player2, new Point(7, 11)));
	  }
	  
	  @Test
	  public void testPlayerMoveToKey() {
		  assertTrue(this.ruleChecker.checkValidMove(this.player3, new Point(4, 17)));
	  }

	  //Tests for invalid adversary movements
	  @Test
	  public void testAdversaryActionBadTooLong() {
		  assertFalse(this.ruleChecker.checkValidMove(this.ghost1, new Point(7, 10)));
	  }

	  @Test
	  public void testAdversaryActionBadTooLongDiag() {
		  assertFalse(this.ruleChecker.checkValidMove(this.ghost1, new Point(6, 9)));
	  }

	  @Test
	  public void testAdversaryActionBadWall() {
		  assertFalse(this.ruleChecker.checkValidMove(this.zombie, new Point(2, 18)));
	  }
	  
	  //Tests for valid adversary movements	  
	  @Test
	  public void testAdversaryActionMoveUp() {
		  assertTrue(this.ruleChecker.checkValidMove(this.ghost2, new Point(2, 13)));
	  }

	  @Test
	  public void testAdversaryActionMoveLeft() {
		  assertTrue(this.ruleChecker.checkValidMove(this.ghost1, new Point(6, 8)));
	  }

	  @Test
	  public void testAdversaryActionMoveRight() {
		  assertTrue(this.ruleChecker.checkValidMove(this.zombie, new Point(3, 17)));
	  }
	  
	  @Test
	  public void testAdversaryActionMoveDown() {
		  assertTrue(this.ruleChecker.checkValidMove(this.ghost1, new Point(7, 9)));
	  }

		@Test
		public void testIsLevelOverActive() {
			Assert.assertEquals(GameState.ACTIVE, simpleRuleChecker.isLevelOver());

			Level firstLevel = simpleDungeon.getCurrentLevel();
			beatLevel(firstLevel);

			Level secondLevel = simpleDungeon.getNextLevel();
			simpleDungeon.startCurrentLevel(this.adversaries);

			assertEquals(GameState.ACTIVE, simpleRuleChecker.isLevelOver());
		}

	@Test
	public void testIsLevelOverWon() {
		Level firstLevel = simpleDungeon.getCurrentLevel();
		beatLevel(firstLevel);

		assertEquals(GameState.WON, simpleRuleChecker.isLevelOver());
	}

	@Test
	public void testIsLevelOverLost() {
		Level firstLevel = simpleDungeon.getCurrentLevel();
		loseLevel(firstLevel);

		assertEquals(GameState.LOST, simpleRuleChecker.isLevelOver());
	}

	  @Test
		public void testIsGameOverActive() {
			assertEquals(GameState.ACTIVE, simpleRuleChecker.isGameOver());

			Level firstLevel = simpleDungeon.getCurrentLevel();
			beatLevel(firstLevel);

			assertEquals(GameState.ACTIVE, simpleRuleChecker.isGameOver());

			Level secondLevel = simpleDungeon.getNextLevel();
			simpleDungeon.startCurrentLevel(this.adversaries);

			assertEquals(GameState.ACTIVE, simpleRuleChecker.isGameOver());
		}

	@Test
	public void testIsGameOverWon() {
		Level secondLevel = simpleDungeon.getNextLevel();
		simpleDungeon.startCurrentLevel(this.adversaries);
		beatLevel(secondLevel);

		assertEquals(GameState.WON, simpleRuleChecker.isGameOver());
	}

	@Test
	public void testIsGameOverLost() {
		Level firstLevel = simpleDungeon.getCurrentLevel();
		loseLevel(firstLevel);

		assertEquals(GameState.LOST, simpleRuleChecker.isGameOver());
	}

	@Test
	public void testIsGameOverLostSecondLevel() {
		Level firstLevel = simpleDungeon.getCurrentLevel();
		beatLevel(firstLevel);

		Level secondLevel = simpleDungeon.getNextLevel();
		simpleDungeon.startCurrentLevel(this.adversaries);
		loseLevel(secondLevel);

		assertEquals(GameState.LOST, simpleRuleChecker.isGameOver());
	}

	private void beatLevel(Level level) {
		//Player3 get key then exit
		level.playerAction(player3, new Point(2, 2));
		level.playerAction(player3, new Point(4, 2));
		level.playerAction(player3, new Point(6, 2));
		level.playerAction(player3, new Point(6, 4));
		level.playerAction(player3, new Point(6, 6));
		level.playerAction(player3, new Point(6, 8));
		level.playerAction(player3, new Point(6, 10));
		level.playerAction(player3, new Point(5, 11));
		level.playerAction(player3, new Point(3, 11));
		level.playerAction(player3, new Point(2, 12));
		level.playerAction(player3, new Point(2, 14));
		level.playerAction(player3, new Point(3, 15));
		level.playerAction(player3, new Point(4, 16));
		level.playerAction(player3, new Point(4, 17));
		level.playerAction(player3, new Point(4, 16));
		level.playerAction(player3, new Point(3, 15));
		level.playerAction(player3, new Point(2, 14));
		level.playerAction(player3, new Point(2, 12));
		level.playerAction(player3, new Point(3, 11));
		level.playerAction(player3, new Point(5, 11));
		level.playerAction(player3, new Point(7, 11));

		//Player 2 win
		level.playerAction(player2, new Point(2, 2));
		level.playerAction(player2, new Point(4, 2));
		level.playerAction(player2, new Point(6, 2));
		level.playerAction(player2, new Point(6, 4));
		level.playerAction(player2, new Point(6, 6));
		level.playerAction(player2, new Point(6, 8));
		level.playerAction(player2, new Point(6, 10));
		level.playerAction(player2, new Point(7, 11));

		//Player 1 win
		level.playerAction(player1, new Point(2, 2));
		level.playerAction(player1, new Point(4, 2));
		level.playerAction(player1, new Point(6, 2));
		level.playerAction(player1, new Point(6, 4));
		level.playerAction(player1, new Point(6, 6));
		level.playerAction(player1, new Point(6, 8));
		level.playerAction(player1, new Point(6, 10));
		level.playerAction(player1, new Point(7, 11));
	}

	private void loseLevel(Level level) {
		//Player3 die
		level.playerAction(player3, new Point(2, 2));
		level.playerAction(player3, new Point(4, 2));
		level.playerAction(player3, new Point(6, 2));
		level.playerAction(player3, new Point(6, 4));
		level.playerAction(player3, new Point(6, 6));
		level.playerAction(player3, new Point(6, 8));
		level.playerAction(player3, new Point(6, 10));
		level.playerAction(player3, new Point(7, 11));
		level.playerAction(player3, new Point(9, 11));
		level.playerAction(player3, new Point(13, 11));

		//Player 2 die
		level.playerAction(player2, new Point(2, 2));
		level.playerAction(player2, new Point(4, 2));
		level.playerAction(player2, new Point(6, 2));
		level.playerAction(player2, new Point(6, 4));
		level.playerAction(player2, new Point(6, 6));
		level.playerAction(player2, new Point(6, 8));
		level.playerAction(player2, new Point(6, 10));
		level.playerAction(player2, new Point(7, 11));
		level.playerAction(player2, new Point(9, 11));
		level.playerAction(player2, new Point(13, 11));

		//Player 1 die
		level.playerAction(player1, new Point(2, 2));
		level.playerAction(player1, new Point(4, 2));
		level.playerAction(player1, new Point(6, 2));
		level.playerAction(player1, new Point(6, 4));
		level.playerAction(player1, new Point(6, 6));
		level.playerAction(player1, new Point(6, 8));
		level.playerAction(player1, new Point(6, 10));
		level.playerAction(player1, new Point(7, 11));
		level.playerAction(player1, new Point(9, 11));
		level.playerAction(player1, new Point(13, 11));
	}

	@Test
	public void testCheckValidGameStateNormal() {
		RuleChecker ruleChecker = dungeon;
		assertTrue(ruleChecker.checkValidGameState());
	}

	@Test
	public void testCheckValidGameStateNoKey() {
		//Add no key
		List<Item> items = new ArrayList<>(Arrays.asList(this.exit));
		this.levels.add(new LevelImpl(levelMap, items));
		RuleChecker ruleChecker = new Dungeon(players, 1, levels);
		assertFalse(ruleChecker.checkValidGameState());
	}

	@Test
	public void testCheckValidGameStateNoExit() {
		//Add no exit
		List<Item> items = new ArrayList<>(Arrays.asList(this.key));
		this.levels.add(new LevelImpl(levelMap, items));
		RuleChecker ruleChecker = new Dungeon(players, 1, levels);
		assertFalse(ruleChecker.checkValidGameState());
	}

	@Test
	public void testCheckValidGameStateBadPlayer() {
		List<Player> players = new ArrayList<Player>(Arrays.asList(new Player(), this.player2, this.player3));
		RuleChecker ruleChecker = new Dungeon(players, 1, levels);
		assertFalse(ruleChecker.checkValidGameState());
	}
}
