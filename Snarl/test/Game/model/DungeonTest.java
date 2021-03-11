package Game.model;

import static org.junit.Assert.*;

import java.util.List;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import Game.view.TextualLevelViewTest;

//Tests for the Dungeon class
public class DungeonTest {
	
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
	
	//Test that the constructor throws the corresponding error when too many players are added
	@Test (expected = IllegalArgumentException.class)
	public void testDungeonConstructorExtraPlayers() {
		initializeDungeonInput();
		int currLevel = 1;
		
		//Add new players 
		players.add(new Player());
		players.add(new Player());
		
		new Dungeon(players, adversaries, currLevel, levels);
	}
	
	//Test that the constructor throws the corresponding error when no players are added
	@Test (expected = IllegalArgumentException.class)
	public void testDungeonConstructorNoPlayers() {
		initializeDungeonInput();
		int currLevel = 1;
		
		List<Player> noPlayers = new ArrayList<>();

		new Dungeon(noPlayers, adversaries, currLevel, levels);
	}
	
	//Tests that the level is in the correct state once started
	@Test
	public void testStartCurrentLevel() {
		initializeDungeonInput();
		int currLevel = 1;
		Dungeon dungeon = new Dungeon(players, adversaries, currLevel, levels);
		dungeon.startCurrentLevel();
		
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
		initializeDungeonInput();
		int currLevel = 1;
		Dungeon dungeon = new Dungeon(players, adversaries, currLevel, levels);
		assertEquals(this.levels.get(0), dungeon.getCurrentLevel());
		dungeon.getNextLevel();
		assertEquals(this.levels.get(1), dungeon.getCurrentLevel());
		dungeon.getNextLevel();
		assertEquals(this.levels.get(2), dungeon.getCurrentLevel());
	}
	
	
	//Test that the next level in the Dungeon can be retrieved 
	@Test
	public void getNextLevel() {
		initializeDungeonInput();
		int currLevel = 1;
		
		Dungeon dungeon = new Dungeon(players, adversaries, currLevel, levels);
		
		assertEquals(levels.get(1), dungeon.getNextLevel());
		assertEquals(levels.get(2), dungeon.getNextLevel());
	}

	//Test that this is the last level in the Dungeon
	@Test
	public void isLastLevelTrue() {
		initializeDungeonInput();
		int currLevel = 3;
		
		Dungeon dungeon = new Dungeon(players, adversaries, currLevel, levels);
		
		assertEquals(true, dungeon.isLastLevel());	
		
	}
	
	//Test that this is not the last level in the Dungeon
	@Test
	public void isLastLevelFalse() {
		initializeDungeonInput();
		int currLevel = 1;
		
		Dungeon dungeon = new Dungeon(players, adversaries, currLevel, levels);
		
		assertEquals(false, dungeon.isLastLevel());
		
		dungeon.getNextLevel();
		assertEquals(false, dungeon.isLastLevel());	
	}

}