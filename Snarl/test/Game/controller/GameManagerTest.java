package Game.controller;

import static org.junit.Assert.assertEquals;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import Game.model.Adversary;
import Game.model.Exit;
import Game.model.GameState;
import Game.model.Ghost;
import Game.model.Key;
import Game.model.Level;
import Game.model.LevelImpl;
import Game.model.LevelMap;
import Game.model.Player;
import Game.model.Zombie;

public class GameManagerTest {
	
	private GameManager gameManager;
	
	//Fields for the Dungeon
	private List<Player> players;
	private List<Adversary> adversaries;
	private List<Level> levels;
			
	//Initialize input for the Dungeon constructor
	private void initializeLevelInput() {
		//Players 
		Player player1 = new Player();
		Player player2 = new Player();
		Player player3 = new Player();
		this.players = new ArrayList<>(Arrays.asList(player1, player2, player3));
				
		//Adversaries 
		Adversary ghost1 = new Ghost();
		Adversary ghost2 = new Ghost();
		Adversary zombie1 = new Zombie();
		Adversary zombie2 = new Zombie();
		this.adversaries = new ArrayList<>(Arrays.asList(ghost1, ghost2, zombie1, zombie2));
				
		//Keys
		Key key1 = new Key(new Point(4,17));
		Key key2 = new Key(new Point(6,8));
		Key key3 = new Key(new Point(15,12));
				
		//Exits
		Exit exit1 = new Exit(new Point(7,11));
		Exit exit2 = new Exit(new Point(1,15));
		Exit exit3 = new Exit(new Point(3,17));
				
		LevelMap map1 = new LevelMap();
		LevelMap map2 = new LevelMap();
		LevelMap map3 = new LevelMap();
				
		//List of levels in the Dungeon
		Level level1 = new LevelImpl(this.players, this.adversaries, map1.initializeLevelMap(), 
				key1, exit1);
		Level level2 = new LevelImpl(this.players, this.adversaries, map2.initializeLevelMap(), 
				key2, exit2);
		Level level3 = new LevelImpl(this.players, this.adversaries, map3.initializeLevelMap(), 
				key3, exit3);
		this.levels = new ArrayList<>(Arrays.asList(level1, level2, level3));	
	}
	
	@Before
	public void initializeGameManager() {
		this.gameManager = new GameManager();
		this.gameManager.registerActors(1, 6);
		
		initializeLevelInput();
		this.gameManager.startGame(levels);
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
