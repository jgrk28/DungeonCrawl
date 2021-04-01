
import Observer.LocalObserver;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import Game.controller.GameManager;
import Game.model.Item;
import Game.model.Level;
import Game.model.LevelComponent;
import Game.model.LevelImpl;
import Level.TestLevel;
import Manager.TestManager;
import Player.LocalPlayer;

/**
 * Integrates the GameManager, Player, and Adversary components to allow local 
 * game play for one player
 */
public class LocalSnarl {
	
	/**
	 * Parses the command line arguments to create a game of Snarl. Registers 
	 * players and adversaries, creates the corresponding levels, and allows
	 * one LocalPlayer to play the game
	 * @param args - the command line arguments
	 * @throws FileNotFoundException if the file containing JSON level specifications
	 * cannot be found or opened 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		//Set the defaults for the game
		String fileName = "snarl.levels";
		int numPlayers = 1;
		int startLevel = 1;
		Boolean observer = false;
		
		for (int i = 0; i < args.length; i++) {
			switch (args[i]) {
				case "--levels":
					fileName = args[i + 1];
					i++;
					break;
				case "--players":
					numPlayers = Integer.parseInt(args[i + 1]);
					i++;
					if (numPlayers != 1) {
						throw new IllegalArgumentException("Only one player is supported");
					}
					break;
				case "--start":
					startLevel = Integer.parseInt(args[i + 1]);
					i++;
					break;
				case "--observe":
					observer = true;
					break;
				default:
					throw new IllegalArgumentException("Invalid input: " + args[i]);
			}
		}
		
		LocalSnarl snarl = new LocalSnarl();
		GameManager manager = new GameManager();
		snarl.registerPlayers(manager, numPlayers);
		List<Level> levels = snarl.generateLevels(fileName);
		int numLevels = levels.size();
		snarl.registerAdversaries(manager, numLevels);

		if (observer) {
			manager.attachObserver(new LocalObserver());
		}

		manager.startGame(levels, startLevel);
		manager.playGame();
		manager.endGame();
	}
	
	/**
	 * Registers players with the game manager and creates the corresponding
	 * LocalPlayer
	 * @param manager - the game manager
	 * @param numPlayers - the number of players in the game
	 */
	private void registerPlayers(GameManager manager, int numPlayers) {
		for (int i = 0; i < numPlayers; i++) {
			System.out.println("Enter name for player " + i);
			Scanner in = new Scanner(System.in);
			String name = in.next();
			in.close();
			manager.registerPlayer(name, new LocalPlayer());
		}		
	}
	
	/**
	 * Registers adversries with the game manager. Creates the maximum number
	 * of Ghosts and Zombies for the game. A subsection of these adversaries
	 * may be used based on the current level number
	 * @param numLevels - the total number of levels in the game (the max)
	 * @param manager - the game manager
	 */
	private void registerAdversaries(GameManager manager, int numLevels) {
		//Max number of ghosts and zombies needed for the game
		int numZombies = Math.floorDiv(numLevels, 2) + 1;
		int numGhosts = Math.floorDiv((numLevels - 1), 2);
		manager.registerAdversaries(numZombies, numGhosts);
	}
	
	/**
	 * Generates all levels contained within the level specifications
	 * @param fileName - the name of the file containing the level specifications
	 * @return the list of levels generated based on this specification
	 * @throws FileNotFoundException if the file containing JSON level specifications
	 * cannot be found or opened 
	 */
	private List<Level> generateLevels(String fileName) throws FileNotFoundException {
		List<Level> levels = new ArrayList<>();
		JSONTokener inputTokens = new JSONTokener(new FileInputStream(fileName));
		Object value = inputTokens.nextValue();
		int numLevels = (int)value;
		
		//Get the JSON Object for each level in the file. Parse the levelMap and items.
		//Create the corresponding level and add it to the list
		for (int i = 0; i < numLevels; i++) {
			JSONObject JSONLevel = (JSONObject)inputTokens.nextValue();
			List<LevelComponent> levelMap = TestLevel.parseLevelMap(JSONLevel);
			List<Item> items = TestLevel.parseObjects(JSONLevel);
			
			Level level = new LevelImpl(levelMap, items);
			levels.add(level);
		}
		return levels;
	}
	
}







