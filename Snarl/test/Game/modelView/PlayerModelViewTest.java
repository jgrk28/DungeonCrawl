package Game.modelView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Game.model.Adversary;
import Game.model.Dungeon;
import Game.model.Exit;
import Game.model.Ghost;
import Game.model.Key;
import Game.model.Level;
import Game.model.LevelImpl;
import Game.model.LevelMap;
import Game.model.Player;
import Game.model.Zombie;

public class PlayerModelViewTest {
	//Fields for the PlayerModelView
	private PlayerModelView playerModelView0;
	private PlayerModelView playerModelView1;
	private PlayerModelView playerModelView2;
	
	private DungeonModelView dungeonView;
	
	//Fields for the Dungeon
	private Dungeon dungeon;
	private List<Player> players;
	private List<Adversary> adversaries;
	private List<Level> levels;
		
	//Fields for a level
	private Key key1;
	private Exit exit1;
	private Key key2;
	private Exit exit2;
	private Key key3;
	private Exit exit3;
		
	//Entity types
	private EntityType w = EntityType.WALL;
	private EntityType s = EntityType.SPACE;
	private EntityType e = EntityType.EMPTY;
	private EntityType p = EntityType.PLAYER;
	    
	//Initialize input for the Dungeon constructor
	private void initializeDungeonInput() {
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
		this.key1 = new Key(new Point(4,17));
		this.key2 = new Key(new Point(6,8));
		this.key3 = new Key(new Point(15,12));
			
		//Exits
		this.exit1 = new Exit(new Point(7,11));
		this.exit2 = new Exit(new Point(1,15));
		this.exit3 = new Exit(new Point(3,17));
			
		LevelMap map1 = new LevelMap();
		LevelMap map2 = new LevelMap();
		LevelMap map3 = new LevelMap();
			
		//List of levels in the Dungeon
		Level level1 = new LevelImpl(this.players, this.adversaries, map1.initializeLevelMap(), 
				this.key1, this.exit1);
		Level level2 = new LevelImpl(this.players, this.adversaries, map2.initializeLevelMap(), 
				this.key2, this.exit2);
		Level level3 = new LevelImpl(this.players, this.adversaries, map3.initializeLevelMap(), 
				this.key3, this.exit3);
		this.levels = new ArrayList<>(Arrays.asList(level1, level2, level3));	
	}
	
	@Before
	public void initializePlayerModelView() {
		initializeDungeonInput();
		this.dungeon = new Dungeon(players, adversaries, 1, levels);
		this.dungeonView = this.dungeon;
		this.playerModelView0 = new PlayerModelView(this.players.get(0), dungeonView);	
		this.playerModelView1 = new PlayerModelView(this.players.get(1), dungeonView);	
		this.playerModelView2 = new PlayerModelView(this.players.get(2), dungeonView);	
	}
		
	//Test for getCurrentLevelIndex
	@Test 
	public void testGetCurrentLevelIndex() {
		assertEquals(1, playerModelView0.getCurrentLevel());	
		this.dungeon.getNextLevel();
		assertEquals(2, playerModelView0.getCurrentLevel());	
		this.dungeon.getNextLevel();
		assertEquals(3, playerModelView0.getCurrentLevel());	
	}
		
	//Tests for isPlayerAlive
	@Test 
	public void testIsPlayerAlive() {
		assertTrue(playerModelView0.isPlayerAlive());
		assertTrue(playerModelView1.isPlayerAlive());
		assertTrue(playerModelView2.isPlayerAlive());
	}
		
	@Test (expected = IllegalArgumentException.class)
	public void testIsPlayerAliveException() {
		PlayerModelView playerView = new PlayerModelView(new Player(), this.dungeonView);
		playerView.isPlayerAlive();
	}

	//Tests for isPlayerAlive
	@Test
	public void testIsPlayerAliveFalse() {
		Level firstLevel = levels.get(0);
		Player player0 = players.get(0);
		firstLevel.playerAction(player0, new Point(2, 2));
		firstLevel.playerAction(player0, new Point(4, 2));
		firstLevel.playerAction(player0, new Point(6, 2));
		firstLevel.playerAction(player0, new Point(6, 4));
		firstLevel.playerAction(player0, new Point(6, 6));
		firstLevel.playerAction(player0, new Point(6, 8));
		firstLevel.playerAction(player0, new Point(6, 10));
		firstLevel.playerAction(player0, new Point(7, 11));
		firstLevel.playerAction(player0, new Point(9, 11));
		firstLevel.playerAction(player0, new Point(11, 11));
		assertTrue(playerModelView0.isPlayerAlive());
		firstLevel.playerAction(player0, new Point(13, 11));
		assertFalse(playerModelView0.isPlayerAlive());
	}
		
	//Test for getPlayerMap
	
	@Test 
	public void testGetPlayer0Map() {
		List<List<EntityType>> expectedMap = Arrays.asList(
				Arrays.asList(),
				Arrays.asList(w, w, w, w),
		        Arrays.asList(w, p, p, w),
		        Arrays.asList(w, p, s, s),
		        Arrays.asList(w, w, w, w));
			
		assertEquals(expectedMap, playerModelView0.getMap());	
			
	}
	
	@Test 
	public void testGetPlayer2Map() {
	    List<List<EntityType>> expectedMap = Arrays.asList(
	            Arrays.asList(w, w, w, w),
	            Arrays.asList(w, p, p, w),
	            Arrays.asList(w, p, s, s),
	            Arrays.asList(w, w, w, w),
	            Arrays.asList(e, e, e, e));
			
		assertEquals(expectedMap, playerModelView2.getMap());			
	}		

}
