package Game.view;

import static org.junit.Assert.assertEquals;

import Game.model.ModelCreator;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Game.model.Adversary;
import Game.model.Dungeon;
import Game.model.Level;
import Game.model.Player;
import Game.modelView.DungeonModelView;
import Game.modelView.PlayerModelView;

public class TextualPlayerViewTest {
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
				+ "You are active in the level\n\n"
				+ "XXXX\n"
				+ "XPPX\n"
				+ "XP..\n"
				+ "XXXX\n";

		testDrawPlayerView(playerModelView0, expectedOut);
	}
	
	@Test
	public void testDrawPlayer1View() {
		initializePlayerModelView();

		String expectedOut = "You are currently on level: 1\n"
				+ "You are active in the level\n\n"
				+ "XXXX \n"
				+ "XPPX \n"
				+ "XP..*\n"
				+ "XXXX \n";

		testDrawPlayerView(playerModelView1, expectedOut);
	}
	
	@Test
	public void testDrawPlayer2View() {
		initializePlayerModelView();

		String expectedOut = "You are currently on level: 1\n"
				+ "You are active in the level\n"
				+ "XXXX\n"
				+ "XPPX\n"
				+ "XP..\n"
				+ "XXXX\n"
				+ "    \n";

		testDrawPlayerView(playerModelView2, expectedOut);
	}
	
	//TODO add tests for when a player is no longer in the level

}
