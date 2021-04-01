package Game.view;

import static org.junit.Assert.assertEquals;

import Game.model.Adversary;
import Game.model.ModelCreator;

import java.awt.Point;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Game.model.Dungeon;
import Game.model.Level;
import Game.model.Player;
import Game.modelView.DungeonModelView;
import Game.modelView.PlayerModelView;

public class TextualPlayerViewTest {
	
	private ModelCreator creator;
	
	//Fields for the PlayerModelView
	private PlayerModelView playerModelView0;
	private PlayerModelView playerModelView1;
	private PlayerModelView playerModelView2;
	
	private DungeonModelView dungeonView;
	
	//Fields for the Dungeon
	private Dungeon dungeon;
	private List<Player> players;
	private List<Adversary> adversaries;
	
	@Before
	public void initializePlayerModelView() {
		this.creator = new ModelCreator();
		this.players = creator.initializeDungeonPlayers();
		this.adversaries = this.creator.initializeDungeonAdversaries();
		this.dungeon = creator.initializeDungeonStarted();
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
				+ "You are active in the level\n"
				+ "XX   \n"
				+ ".X   \n"
				+ "..P**\n"
				+ "XX  *\n"
				+ "    *\n";
		
		testDrawPlayerView(playerModelView0, expectedOut);
	}
	
	@Test
	public void testDrawPlayer1View() {
		initializePlayerModelView();

		String expectedOut = "You are currently on level: 1\n"
				+ "You are active in the level\n"
				+ "X.GX \n"
				+ "X..X \n"
				+ "X.PX \n"
				+ "..@.*\n"
				+ "XXXX \n";

		testDrawPlayerView(playerModelView1, expectedOut);
	}
	
	@Test
	public void testDrawPlayer2View() {
		initializePlayerModelView();
		
		String expectedOut = "You are currently on level: 1\n"
				+ "You are active in the level\n"
				+ "....X\n"
				+ "....X\n"
				+ ".ZP!X\n"
				+ "XXXXX\n"
		    + "     \n";

		testDrawPlayerView(playerModelView2, expectedOut);
	}
	
	//Tests for when the player moves to the next level
	@Test
	public void testDrawLevel2() {
		initializePlayerModelView();
		Dungeon simpleDungeon = this.creator.initializeSimpleDungeon();
		simpleDungeon.getNextLevel();
		simpleDungeon.startCurrentLevel(new ArrayList<>());
		DungeonModelView view = simpleDungeon;
		PlayerModelView playerModelView = new PlayerModelView(this.players.get(1), view);	
		
		String expectedOut = "You are currently on level: 2\n"
				+ "You are active in the level\n"
				+ "     \n"
				+ "XXXX \n"
				+ "XPPX \n"
				+ "XP..*\n"
				+ "XXXX \n";

		testDrawPlayerView(playerModelView, expectedOut);
	}
	
	
	//Test for when a player is no longer in the level
	@Test
	public void testDrawNoLongerInLevel() {
		initializePlayerModelView();
		Level level = this.dungeon.getNextLevel();
		this.dungeon.startCurrentLevel(this.adversaries);
		Player player2 = this.players.get(2);
		
		//Kill player 2
		level.playerAction(player2, new Point(2, 2));
		level.playerAction(player2, new Point(4, 2));
		level.playerAction(player2, new Point(6, 2));
		level.playerAction(player2, new Point(6, 4));
		level.playerAction(player2, new Point(6, 6));
		level.playerAction(player2, new Point(6, 8));
		level.playerAction(player2, new Point(6, 10));
		level.playerAction(player2, new Point(7, 11));
		level.playerAction(player2, new Point(9, 11));
		level.playerAction(player2, new Point(13, 11));

		DungeonModelView view = this.dungeon;
		PlayerModelView playerModelView = new PlayerModelView(player2, view);	
		
		String expectedOut = "You are currently on level: 2\n"
				+ "You are no longer active in the level\n";

		testDrawPlayerView(playerModelView, expectedOut);
	}

}
