package Game.modelView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import Game.model.Adversary;
import Game.model.Dungeon;
import Game.model.Exit;
import Game.model.Ghost;
import Game.model.Key;
import Game.model.Level;
import Game.model.LevelImpl;
import Game.model.Player;
import Game.model.Zombie;

//Tests for the DungeonModelView interface which gives the view everything it needs to draw
//without allowing the view to edit the model at all.
public class DungeonModelViewTest {
	//Fields for the Dungeon
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
		Level level1 = new LevelImpl(map1.initializeLevelMap(), this.key1, this.exit1);
		Level level2 = new LevelImpl(map2.initializeLevelMap(), this.key2, this.exit2);
		Level level3 = new LevelImpl(map3.initializeLevelMap(), this.key3, this.exit3);
		this.levels = new ArrayList<>(Arrays.asList(level1, level2, level3));	
	}
	
	//Test for getCurrentLevelIndex
	@Test 
	public void testGetCurrentLevelIndex() {
		initializeDungeonInput();
		int currLevel = 1;
		DungeonModelView dungeonView = new Dungeon(players, adversaries, currLevel, levels);
		assertEquals(1, dungeonView.getCurrentLevelIndex());	
	}
	
	//Tests for isPlayerAlive
	@Test 
	public void testIsPlayerAlive() {
		initializeDungeonInput();
		int currLevel = 1;
		Dungeon dungeon = new Dungeon(players, adversaries, currLevel, levels);
		dungeon.startCurrentLevel();
		DungeonModelView dungeonView = dungeon;
		assertTrue(dungeonView.isPlayerAlive(this.players.get(0)));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testIsPlayerAliveException() {
		initializeDungeonInput();
		int currLevel = 1;
		Dungeon dungeon = new Dungeon(players, adversaries, currLevel, levels);
		dungeon.startCurrentLevel();
		DungeonModelView dungeonView = dungeon;
		dungeonView.isPlayerAlive(new Player());
	}
	
	//TODO add test for isPlayerAlive => false
	
	//Test for getPlayerMap
	@Test 
	public void testGetPlayer0Map() {
		initializeDungeonInput();
		int currLevel = 1;
		Dungeon dungeon = new Dungeon(players, adversaries, currLevel, levels);
		dungeon.startCurrentLevel();
		DungeonModelView dungeonView = dungeon;
		
	    List<List<EntityType>> expectedMap = Arrays.asList(
	    		Arrays.asList(),
	            Arrays.asList(w, w, w, w),
	            Arrays.asList(w, p, p, w),
	            Arrays.asList(w, p, s, s),
	            Arrays.asList(w, w, w, w));
		
	    assertEquals(expectedMap,dungeonView.getPlayerMap(this.players.get(0)));
		
	}	
	
	@Test 
	public void testGetPlayer2Map() {
		initializeDungeonInput();
		int currLevel = 1;
		Dungeon dungeon = new Dungeon(players, adversaries, currLevel, levels);
		dungeon.startCurrentLevel();
		DungeonModelView dungeonView = dungeon;
		
	    List<List<EntityType>> expectedMap = Arrays.asList(
	            Arrays.asList(w, w, w, w),
	            Arrays.asList(w, p, p, w),
	            Arrays.asList(w, p, s, s),
	            Arrays.asList(w, w, w, w),
	            Arrays.asList(e, e, e, e));
		
	    assertEquals(expectedMap,dungeonView.getPlayerMap(this.players.get(2)));
		
	}

}
