package Game.modelView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import Game.model.ModelCreator;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Game.model.Adversary;
import Game.model.Dungeon;
import Game.model.Level;
import Game.model.Player;

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
		
	//Entity types
	private EntityType w = EntityType.WALL;
	private EntityType s = EntityType.SPACE;
	private EntityType e = EntityType.EMPTY;
	private EntityType p = EntityType.PLAYER;
	
	@Before
	public void initializePlayerModelView() {
		ModelCreator creator = new ModelCreator();

		this.players = creator.initializeDungeonPlayers();
		this.adversaries = creator.initializeDungeonAdversaries();
		this.levels = creator.initializeDungeonLevels();
		this.dungeon = creator.initializeDungeon();
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
