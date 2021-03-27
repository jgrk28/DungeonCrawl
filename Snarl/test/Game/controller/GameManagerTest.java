package Game.controller;

import static org.junit.Assert.assertEquals;

import Game.model.Actor;
import Game.model.GameState;
import Game.model.ModelCreator;
import Manager.TestPlayer;
import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import org.json.JSONArray;
import org.json.JSONTokener;
import org.junit.Before;
import org.junit.Test;
import Game.model.Adversary;
import Game.model.Level;
import Game.model.Player;

//Tests for the GameManager
public class GameManagerTest {
	
	private GameManager gameManager;
	
	//Fields for the Dungeon
	private List<Player> players;
	private List<Adversary> adversaries;
	private List<Level> levels;
	
	@Before
	public void initializeGameManager() {
		ModelCreator creator = new ModelCreator();
		this.players = creator.initializeDungeonPlayers();
		this.adversaries = creator.initializeDungeonAdversaries();
		this.levels = creator.initializeDungeonLevels();

		this.gameManager = new GameManager();
	}

	@Test (expected = IllegalArgumentException.class)
	public void registerPlayerNotUniquePlayer() {
		Common.Player player1 = new TestPlayer(new ArrayList<>(), new JSONArray());
		Common.Player player2 = new TestPlayer(new ArrayList<>(), new JSONArray());
		this.gameManager.registerPlayer("Juliette", player1);
		this.gameManager.registerPlayer("Juliette", player2);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void registerActorsTooManyPlayers() {
		Common.Player player1 = new TestPlayer(new ArrayList<>(), new JSONArray());
		Common.Player player2 = new TestPlayer(new ArrayList<>(), new JSONArray());
		Common.Player player3 = new TestPlayer(new ArrayList<>(), new JSONArray());
		Common.Player player4 = new TestPlayer(new ArrayList<>(), new JSONArray());
		Common.Player player5 = new TestPlayer(new ArrayList<>(), new JSONArray());
		this.gameManager.registerPlayer("Jacob", player1);
		this.gameManager.registerPlayer("Juliette", player2);
		this.gameManager.registerPlayer("Batman", player3);
		this.gameManager.registerPlayer("Mario", player4);
		this.gameManager.registerPlayer("Luigi", player5);
	}

	@Test (expected = IllegalArgumentException.class)
	public void registerAdversaryNotUnique() {
		this.gameManager.registerAdversary("Boo");
		this.gameManager.registerAdversary("Boo");
	}

	@Test (expected = IllegalArgumentException.class)
	public void registerAdversaryNotUniquePlayer() {
		Common.Player player1 = new TestPlayer(new ArrayList<>(), new JSONArray());
		this.gameManager.registerPlayer("Jacob", player1);
		this.gameManager.registerAdversary("Jacob");
	}

	@Test
	public void testStartGame() {
		Common.Player player1 = new TestPlayer(new ArrayList<>(), new JSONArray());
		this.gameManager.registerPlayer("Jacob", player1);
		this.gameManager.startGame(this.levels);
		Level firstLevel = this.gameManager.dungeon.getCurrentLevel();
		Map<Actor, Point> players = firstLevel.getActivePlayers();
		Map<Actor, Point> expectedPlayers = new HashMap<>();
		expectedPlayers.put(new Player("Jacob"), new Point(1, 1));

		assertEquals(1, this.gameManager.dungeon.getCurrentLevelIndex());
		assertEquals(this.levels.get(0), firstLevel);
		assertEquals(expectedPlayers, players);
	}
	
	@Test
	public void testPlayGame() {
		Common.Player player1 = new TestPlayer(ModelCreator.initGetKeyMoves(), new JSONArray());
		this.gameManager.registerPlayer("Jacob", player1);
		this.gameManager.startGame(this.levels);

		Level firstLevel = this.gameManager.dungeon.getCurrentLevel();
		Map<Actor, Point> expectedPlayers = new HashMap<>();
		expectedPlayers.put(new Player("Jacob"), new Point(4, 17));

		assertEquals(false, firstLevel.getExitUnlocked());
		try {
			this.gameManager.playGame();
		} catch (IllegalStateException e) {
			//Do nothing this will happen when the player runs out of moves
		}
		Map<Actor, Point> players = firstLevel.getActivePlayers();

		assertEquals(1, this.gameManager.dungeon.getCurrentLevelIndex());
		assertEquals(this.levels.get(0), this.gameManager.dungeon.getCurrentLevel());
		assertEquals(expectedPlayers, players);
		assertEquals(true, firstLevel.getExitUnlocked());
	}

	@Test
	public void testPlayGameNewLevel() {
		Common.Player player1 = new TestPlayer(ModelCreator.initWinningMoves(), new JSONArray());
		this.gameManager.registerPlayer("Jacob", player1);
		this.gameManager.startGame(this.levels);

		Map<Actor, Point> expectedPlayers = new HashMap<>();
		expectedPlayers.put(new Player("Jacob"), new Point(1, 1));

		try {
			this.gameManager.playGame();
		} catch (IllegalStateException e) {
			//Do nothing this will happen when the player runs out of moves
		}
		Level secondLevel = this.gameManager.dungeon.getCurrentLevel();
		Map<Actor, Point> players = secondLevel.getActivePlayers();

		assertEquals(2, this.gameManager.dungeon.getCurrentLevelIndex());
		assertEquals(this.levels.get(1), secondLevel);
		assertEquals(expectedPlayers, players);
	}

	@Test
	public void testNotifyAllObservers() throws IOException {
		JSONArray updates = new JSONArray();
		Common.Player player1 = new TestPlayer(new ArrayList<>(), updates);
		Common.Player player2 = new TestPlayer(new ArrayList<>(), updates);
		this.gameManager.registerPlayer("Jacob", player1);
		this.gameManager.registerPlayer("Juliette", player2);
		this.gameManager.initDungeon(this.levels);
		this.gameManager.dungeon.startCurrentLevel();
		this.gameManager.notifyAllObservers();

		String expectedOut = new String(Files.readAllBytes(Paths.get("test/Game/Controller/notify-simple.json")));
		JSONTokener expectedTokens = new JSONTokener(expectedOut);
		Object expectedValue = expectedTokens.nextValue();
		JSONArray expectedArray = (JSONArray) expectedValue;

		assertEquals(true, expectedArray.similar(updates));
	}

	@Test
	public void testPlayLevel() {
		Common.Player player1 = new TestPlayer(ModelCreator.initWinningMoves(), new JSONArray());
		this.gameManager.registerPlayer("Juliette", player1);
		this.gameManager.initDungeon(this.levels);
		Level firstLevel = this.gameManager.dungeon.startCurrentLevel();
		this.gameManager.playLevel(firstLevel);

		Level currLevel = this.gameManager.dungeon.getCurrentLevel();
		Map<Actor, Point> players = currLevel.getActivePlayers();
		Map<Actor, Point> expectedPlayers = new HashMap<>();

		assertEquals(1, this.gameManager.dungeon.getCurrentLevelIndex());
		assertEquals(this.levels.get(0), currLevel);
		assertEquals(expectedPlayers, players);
		assertEquals(GameState.WON, currLevel.isLevelOver());
	}
}
