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
	private RuleChecker ruleChecker;
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
		this.dungeon = this.creator.initializeSimpleDungeon();
		this.ruleChecker = this.dungeon;
		this.levels = this.creator.initializeSimpleDungeonLevels();
		this.players = this.creator.initializeDungeonPlayers();
		this.adversaries = this.creator.initializeDungeonAdversaries();
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
			Assert.assertEquals(GameState.ACTIVE, ruleChecker.isLevelOver());

			Level firstLevel = dungeon.getCurrentLevel();
			beatLevel(firstLevel);
			dungeon.getNextLevel();

			assertEquals(GameState.ACTIVE, ruleChecker.isLevelOver());
		}

	@Test
	public void testIsLevelOverWon() {
		Level firstLevel = dungeon.getCurrentLevel();
		beatLevel(firstLevel);

		assertEquals(GameState.WON, ruleChecker.isLevelOver());
	}

	@Test
	public void testIsLevelOverLost() {
		Level firstLevel = dungeon.getCurrentLevel();
		loseLevel(firstLevel);

		assertEquals(GameState.LOST, ruleChecker.isLevelOver());
	}

	  @Test
		public void testIsGameOverActive() {
			assertEquals(GameState.ACTIVE, ruleChecker.isGameOver());

			Level firstLevel = dungeon.getCurrentLevel();
			beatLevel(firstLevel);

			assertEquals(GameState.ACTIVE, ruleChecker.isGameOver());

			dungeon.getNextLevel();

			assertEquals(GameState.ACTIVE, ruleChecker.isGameOver());
		}

	@Test
	public void testIsGameOverWon() {
		Level secondLevel = dungeon.getNextLevel();
		beatLevel(secondLevel);

		assertEquals(GameState.WON, ruleChecker.isGameOver());
	}

	@Test
	public void testIsGameOverLost() {
		Level firstLevel = dungeon.getCurrentLevel();
		loseLevel(firstLevel);

		assertEquals(GameState.LOST, ruleChecker.isGameOver());
	}

	@Test
	public void testIsGameOverLostSecondLevel() {
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
		RuleChecker ruleChecker = dungeon;
		assertTrue(ruleChecker.checkValidGameState());
	}

	@Test
	public void testCheckValidGameStateNoKey() {
		//Add no key
		List<Item> items = new ArrayList<>(Arrays.asList(this.exit));
		this.levels.add(new LevelImpl(levelMap, items));
		RuleChecker ruleChecker = new Dungeon(players, adversaries, 1, levels);
		assertFalse(ruleChecker.checkValidGameState());
	}

	@Test
	public void testCheckValidGameStateNoExit() {
		//Add no exit
		List<Item> items = new ArrayList<>(Arrays.asList(this.key));
		this.levels.add(new LevelImpl(levelMap, items));
		RuleChecker ruleChecker = new Dungeon(players, adversaries, 1, levels);
		assertFalse(ruleChecker.checkValidGameState());
	}

	@Test
	public void testCheckValidGameStateBadPlayer() {
		List<Player> players = new ArrayList<Player>(Arrays.asList(new Player(), this.player2, this.player3));
		RuleChecker ruleChecker = new Dungeon(players, adversaries, 1, levels);
		assertFalse(ruleChecker.checkValidGameState());
	}

	@Test
	public void testCheckValidGameStateBadAdversary() {
		List<Adversary> adversaries = new ArrayList<Adversary>(Arrays.asList(new Ghost(), this.zombie, this.ghost2));
		RuleChecker ruleChecker = new Dungeon(players, adversaries, 1, levels);
		assertFalse(ruleChecker.checkValidGameState());
	}
}