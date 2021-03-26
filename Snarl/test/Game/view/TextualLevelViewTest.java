package Game.view;

import Game.model.Adversary;
import Game.model.Exit;
import Game.model.Hall;
import Game.model.Key;
import Game.model.Level;
import Game.model.LevelImpl;
import Game.model.ModelCreator;
import Game.model.Player;
import Game.model.Room;
import Game.modelView.LevelModelView;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

//Tests for the TextualLevelView class. This class displays a level in ASCII art with
//X for wall, . for space, * for hallways, P for player, G for ghost, and Z for zombie
public class TextualLevelViewTest {
	public static void testDrawLevel(LevelModelView modelView, String expectedOut) {
		
		//Assign output of STDOUT to new Stream
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PrintStream print = new PrintStream(output);
		
		//Initialize view
		View view = new TextualLevelView(modelView, print);

		//Draw level to STDOUT
		view.draw();

		//Check that level was drawn as expected
		assertEquals(expectedOut, output.toString());
	}

	private Key key;
	private Exit exit;

	private List<Player> players;
	private List<Adversary> adversaries;

	private Room room1;
	private Room room2;
	private Room room3;
	private Room room4;

	private Hall hall1;
	private Hall hall1Snake;
	private Hall hall1RoomBrush;
	private Hall hall1HallBrush;
	private Hall hall2;
	private Hall hall3;

	//Initialize all components for use
	//They have not been added to a level but they are available for use
	@Before
	public void initLevelComponents() {
		ModelCreator creator = new ModelCreator();
		this.key = creator.getLevel1Key();
		this.exit = creator.getLevel1Exit();
		this.room1 = creator.initializeRoom1();
		this.room2 = creator.initializeRoom2();
		this.room3 = creator.initializeRoom3();
		this.room4 = creator.initializeRoom4();

		this.hall1 = creator.initializeHall1(room1, room2);
		this.hall1Snake = creator.initializeHall1Snake(room1, room2);
		this.hall1RoomBrush = creator.initializeHall1RoomBrush(room1, room2);
		this.hall1HallBrush = creator.initializeHall1HallBrush(room1, room2);
		this.hall2 = creator.initializeHall2(room3, room2);
		this.hall3 = creator.initializeHall3(room2, room4);
		this.players = new ArrayList<>(Arrays.asList(creator.getPlayer1()));
		this.adversaries = new ArrayList<>(Arrays.asList(creator.getGhost1(), creator.getZombie1()));
	}

	//Tests drawing a normal room
	@Test
	public void testDrawLevelNormal() {
		Level normalLevel = new LevelImpl(
				new ArrayList<>(Arrays.asList(room1, room2, room3, room4, hall1, hall2, hall3)),
				new ArrayList<>(Arrays.asList(key, exit))
		);

		normalLevel.placeActors(players, adversaries);

		String expectedOut = ""
				+ "XXXX              \n"
				+ "XP.X              \n"
				+ "X...***           \n"
				+ "XXXX  *           \n"
				+ "      *           \n"
				+ "      *           \n"
				+ "      *           \n"
				+ "     X.XX         \n"
				+ "     X..X         \n"
				+ "     X..X         \n"
				+ "     X..X    XXXXX\n"
				+ "  ***..@.****GZ..X\n"
				+ "  *  XXXX    X...X\n"
				+ "  *          X...X\n"
				+ "XX.XXX       X...X\n"
				+ "X....X       XXXXX\n"
				+ "X....X            \n"
				+ "X...!X            \n"
				+ "XXXXXX            \n";

		testDrawLevel(normalLevel, expectedOut);
	}

	//Check drawing when right and top do not line up with 0
	@Test
	public void testDrawLevelSmall() {
		Level smallLevel = new LevelImpl(
				new ArrayList<>(Arrays.asList(room2, room4, hall3)),
				new ArrayList<>(Arrays.asList(exit))
		);
		smallLevel.placeActors(new ArrayList<>(), adversaries);

		String expectedOut = ""
				+ "X.XX         \n"
				+ "X..X         \n"
				+ "X..X         \n"
				+ "X..X    XXXXX\n"
				+ "..@.****GZ..X\n"
				+ "XXXX    X...X\n"
				+ "        X...X\n"
				+ "        X...X\n"
				+ "        XXXXX\n";

		testDrawLevel(smallLevel, expectedOut);

	}

	//Tests drawing with curvy halls
	@Test
	public void testDrawLevelSnake() {
		Level snakeLevel = new LevelImpl(
				new ArrayList<>(Arrays.asList(room1, room2, room3, room4, hall1Snake, hall2, hall3)),
				new ArrayList<>(Arrays.asList(key, exit))
		);

		snakeLevel.placeActors(players, adversaries);

		String expectedOut = ""
				+ "XXXX              \n"
				+ "XP.X              \n"
				+ "X...******        \n"
				+ "XXXX     *        \n"
				+ "      ****        \n"
				+ "      *           \n"
				+ "      *           \n"
				+ "     X.XX         \n"
				+ "     X..X         \n"
				+ "     X..X         \n"
				+ "     X..X    XXXXX\n"
				+ "  ***..@.****GZ..X\n"
				+ "  *  XXXX    X...X\n"
				+ "  *          X...X\n"
				+ "XX.XXX       X...X\n"
				+ "X....X       XXXXX\n"
				+ "X....X            \n"
				+ "X...!X            \n"
				+ "XXXXXX            \n";

		testDrawLevel(snakeLevel, expectedOut);
	}

	//Tests drawing with hall directly next to a room
	@Test
	public void testDrawLevelRoomBrush() {
		Level roomBrushLevel = new LevelImpl(
				new ArrayList<>(Arrays.asList(room1, room2, room3, room4, hall1RoomBrush, hall2, hall3)),
				new ArrayList<>(Arrays.asList(key, exit))
		);

		roomBrushLevel.placeActors(players, adversaries);

		String expectedOut = ""
				+ "XXXX              \n"
				+ "XP.X              \n"
				+ "X...******        \n"
				+ "XXXX     *        \n"
				+ "         *        \n"
				+ "         *        \n"
				+ "      ****        \n"
				+ "     X.XX         \n"
				+ "     X..X         \n"
				+ "     X..X         \n"
				+ "     X..X    XXXXX\n"
				+ "  ***..@.****GZ..X\n"
				+ "  *  XXXX    X...X\n"
				+ "  *          X...X\n"
				+ "XX.XXX       X...X\n"
				+ "X....X       XXXXX\n"
				+ "X....X            \n"
				+ "X...!X            \n"
				+ "XXXXXX            \n";

		testDrawLevel(roomBrushLevel, expectedOut);
	}

	//Tests drawing with hall directly next to another hall or itself
	@Test
	public void testDrawLevelHallBrush() {
		Level hallBrushLevel = new LevelImpl(
				new ArrayList<>(Arrays.asList(room1, room2, room3, room4, hall1HallBrush, hall2, hall3)),
				new ArrayList<>(Arrays.asList(key, exit))
		);

		hallBrushLevel.placeActors(players, adversaries);
		
		String expectedOut = ""
				+ "XXXX              \n"
				+ "XP.X              \n"
				+ "X...*********     \n"
				+ "XXXX  ****  *     \n"
				+ "      *  *  *     \n"
				+ "      *  *  *     \n"
				+ "      *  *  *     \n"
				+ "     X.XX*  *     \n"
				+ "     X..X*  *     \n"
				+ "     X..X*  *     \n"
				+ "     X..X****XXXXX\n"
				+ "  ***..@.****GZ..X\n"
				+ "  *  XXXX    X...X\n"
				+ "  *          X...X\n"
				+ "XX.XXX       X...X\n"
				+ "X....X       XXXXX\n"
				+ "X....X            \n"
				+ "X...!X            \n"
				+ "XXXXXX            \n";

		testDrawLevel(hallBrushLevel, expectedOut);
	}

}
