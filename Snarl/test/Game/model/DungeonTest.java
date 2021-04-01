package Game.model;

import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import Game.view.TextualLevelViewTest;

//Tests for the Dungeon class
public class DungeonTest {

	private ModelCreator creator;
	private Dungeon dungeon;
	
	//Fields for the Dungeon
	private List<Player> players;
	private List<Adversary> adversaries;
	private List<Level> levels;

	//Initialize all model components for use
	@Before
	public void initLevelComponents() {
		this.creator = new ModelCreator();
		this.dungeon = this.creator.initializeDungeon();
		this.levels = creator.initializeDungeonLevels();
		this.players = creator.initializeDungeonPlayers();
		this.adversaries = creator.initializeDungeonAdversaries();
	}
	
	//Test that the constructor throws the corresponding error when too many players are added
	@Test (expected = IllegalArgumentException.class)
	public void testDungeonConstructorExtraPlayers() {
		int currLevel = 1;
		
		//Add new players 
		players.add(new Player());
		players.add(new Player());
		
		new Dungeon(players, currLevel, levels);
	}
	
	//Test that the constructor throws the corresponding error when no players are added
	@Test (expected = IllegalArgumentException.class)
	public void testDungeonConstructorNoPlayers() {
		int currLevel = 1;
		
		List<Player> noPlayers = new ArrayList<>();

		new Dungeon(noPlayers, currLevel, levels);
	}
	
	//Tests that the level is in the correct state once started
	@Test
	public void testStartCurrentLevel() {
		dungeon.startCurrentLevel(this.adversaries);
		
	    String expectedOut = ""
	            + "XXXX              \n"
	            + "XPPX              \n"
	            + "XP..***           \n"
	            + "XXXX  *           \n"
	            + "      *           \n"
	            + "      *           \n"
	            + "      *           \n"
	            + "     X.XX         \n"
	            + "     X..X         \n"
	            + "     X..X         \n"
	            + "     X..X    XXXXX\n"
	            + "  ***..@.****GGZZX\n"
	            + "  *  XXXX    X...X\n"
	            + "  *          X...X\n"
	            + "XX.XXX       X...X\n"
	            + "X....X       XXXXX\n"
	            + "X....X            \n"
	            + "X...!X            \n"
	            + "XXXXXX            \n";

	     TextualLevelViewTest.testDrawLevel(dungeon.getCurrentLevel(), expectedOut);	
	}
	
	//Tests that the current level of the game is returned
	@Test
	public void testGetCurrentLevel() {
		assertEquals(this.levels.get(0), dungeon.getCurrentLevel());
		dungeon.getNextLevel();
		assertEquals(this.levels.get(1), dungeon.getCurrentLevel());
		dungeon.getNextLevel();
		assertEquals(this.levels.get(2), dungeon.getCurrentLevel());
	}
	
	
	//Test that the next level in the Dungeon can be retrieved 
	@Test
	public void getNextLevel() {
		assertEquals(levels.get(1), dungeon.getNextLevel());
		assertEquals(levels.get(2), dungeon.getNextLevel());
	}

	//Test that this is the last level in the Dungeon
	@Test
	public void isLastLevelTrue() {
		dungeon.getNextLevel();
		dungeon.getNextLevel();
		assertEquals(true, dungeon.isLastLevel());	
		
	}
	
	//Test that this is not the last level in the Dungeon
	@Test
	public void isLastLevelFalse() {
		assertEquals(false, dungeon.isLastLevel());
		dungeon.getNextLevel();
		assertEquals(false, dungeon.isLastLevel());	
	}

}