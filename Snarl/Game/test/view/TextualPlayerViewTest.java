package view;

import static org.junit.Assert.assertEquals;

import java.awt.Point;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.Adversary;
import model.Dungeon;
import model.Exit;
import model.Ghost;
import model.Key;
import model.Level;
import model.LevelImpl;
import model.LevelMap;
import model.Player;
import model.Zombie;
import modelView.DungeonModelView;
import modelView.PlayerModelView;

public class TextualPlayerViewTest {
	//Fields for the PlayerModelView
	private PlayerModelView playerModelView0;
	private PlayerModelView playerModelView1;
	
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
	}
	
	public static void testDrawPlayerView(PlayerModelView playerModelView, String expectedOut) {
		//Assign output of STDOUT to new Stream
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PrintStream print = new PrintStream(output);
		
		//Initialize view
		View view = new TextualPlayerView(playerModelView, print);

		//Draw level to STDOUT
		view.draw();

		//Check that level was drawn as expected
		assertEquals(expectedOut, output.toString());
	}
	
	@Test
	public void testDrawPlayer0View() {
		initializePlayerModelView();

		String expectedOut = "You are currently on level: 1\n"
				+ "You are active in the level\n"
				+ "XXXX\n"
				+ "XPPX\n"
				+ "XP..\n"
				+ "XXXX\n";

		testDrawPlayerView(playerModelView0, expectedOut);
	}
	
	@Test
	public void testDrawPlayer1ViewNextLevel() {
		initializePlayerModelView();

		String expectedOut = "You are currently on level: 1\n"
				+ "You are active in the level\n"
				+ "XXXX\n"
				+ "XPPX\n"
				+ "XP..*\n"
				+ "XXXX\n";

		testDrawPlayerView(playerModelView1, expectedOut);
	}

}
