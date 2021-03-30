import static org.junit.Assert.assertEquals;

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
 * TODO Add comments
 */
public class LocalSnarl {
	
	/**
	 * TODO Add comment
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
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

		//TODO Figure out how to start the game with the players and adversaries in the level
		//Number of adversaries varies based on the current level
		manager.startGame(levels, startLevel);
	}
	
	/**
	 * TODO Add comment
	 * @param manager
	 * @param numPlayers
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
	 * TODO Add comment
	 * @param numLevels
	 * @param manager
	 */
	private void registerAdversaries(GameManager manager, int numLevels) {
		//Max number of ghosts and zombies needed for the game
		int numZombies = Math.floorDiv(numLevels, 2) + 1;
		int numGhosts = Math.floorDiv((numLevels - 1), 2);
		manager.registerAdversaries(numZombies, numGhosts);
	}
	
	/**
	 * TODO Add comment
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException 
	 */
	private List<Level> generateLevels(String fileName) throws FileNotFoundException {
		List<Level> levels = new ArrayList<>();
		JSONTokener inputTokens = new JSONTokener(new FileInputStream(fileName));
		Object value = inputTokens.nextValue();
		int numLevels = (int)value;
		
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







