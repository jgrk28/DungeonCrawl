package Game.controller;

import static org.junit.Assert.assertEquals;

import Game.model.ModelCreator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import Game.model.Adversary;
import Game.model.GameState;
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
		this.gameManager.registerActors(1, 6);

		this.gameManager.startGame(this.levels);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void registerActorsTooManyPlayers() {
		GameManager manager = new GameManager();
		manager.registerActors(6, 8);		
	}
	
	@Test
	public void testStartGame() {
		initializeGameManager();
		assertEquals(GameState.ACTIVE, this.levels.get(0).isLevelOver());
	}

}
