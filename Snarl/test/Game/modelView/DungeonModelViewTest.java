package Game.modelView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import Game.model.ModelCreator;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Game.model.Adversary;
import Game.model.Dungeon;
import Game.model.Level;
import Game.model.Player;

//Tests for the DungeonModelView interface which gives the view everything it needs to draw
//without allowing the view to edit the model at all.
public class DungeonModelViewTest {
	//Fields for the Dungeon
	private List<Player> players;
	private List<Adversary> adversaries;
	private List<Level> levels;
	private Dungeon dungeon;
	private DungeonModelView dungeonView;

	//Entity types
	private EntityType w = EntityType.WALL;
	private EntityType s = EntityType.SPACE;
	private EntityType e = EntityType.EMPTY;
	private EntityType p = EntityType.PLAYER;
    
	@Before
	public void initializeDungeonModelView() {
		ModelCreator creator = new ModelCreator();

		this.players = creator.initializeDungeonPlayers();
		this.adversaries = creator.initializeDungeonAdversaries();
		this.levels = creator.initializeDungeonLevels();
		this.dungeon = creator.initializeDungeon();
		this.dungeonView = dungeon;
	}
	
	//Test for getCurrentLevelIndex
	@Test 
	public void testGetCurrentLevelIndex() {
		assertEquals(1, dungeonView.getCurrentLevelIndex());	
	}
	
	//Tests for isPlayerAlive
	@Test 
	public void testIsPlayerAlive() {
		dungeon.startCurrentLevel();
		assertTrue(dungeonView.isPlayerAlive(this.players.get(0)));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testIsPlayerAliveException() {
		dungeon.startCurrentLevel();
		dungeonView.isPlayerAlive(new Player());
	}
	
	//TODO add test for isPlayerAlive => false
	
	//Test for getPlayerMap
	@Test 
	public void testGetPlayer0Map() {
		dungeon.startCurrentLevel();
		
	    List<List<EntityType>> expectedMap = Arrays.asList(
	    		Arrays.asList(e, e, e, e, e),
	            Arrays.asList(e, w, w, w, w),
	            Arrays.asList(e, w, p, p, w),
	            Arrays.asList(e, w, p, s, s),
	            Arrays.asList(e, w, w, w, w));
		
	    assertEquals(expectedMap,dungeonView.getPlayerMap(this.players.get(0)));
		
	}	
	
	@Test 
	public void testGetPlayer2Map() {
		dungeon.startCurrentLevel();
		
	    List<List<EntityType>> expectedMap = Arrays.asList(
	            Arrays.asList(e, w, w, w, w),
	            Arrays.asList(e, w, p, p, w),
	            Arrays.asList(e, w, p, s, s),
	            Arrays.asList(e, w, w, w, w),
	            Arrays.asList(e, e, e, e, e));
		
	    assertEquals(expectedMap,dungeonView.getPlayerMap(this.players.get(2)));
		
	}

}
