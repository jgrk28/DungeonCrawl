package Manager;

import static Utils.ParseUtils.parsePoint;

import JSONUtils.Generator;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import Game.model.Adversary;
import Game.model.Item;
import Game.model.Level;
import Game.model.LevelComponent;
import Game.model.LevelImpl;
import Game.model.Player;
import Game.model.Zombie;
import Level.TestLevel;

/**
 * Exercises the GameManager and RuleChecker over several
 * turns. Collects a trace of interactions based on the moves
 * of the players in the game. 
 */
public class TestManager {
	
	private TraceManager gameManager;
	private Map<Player, Point> players;
	private Map<Adversary, Point> adversaries;
	private Level level;
	private JSONArray trace;
	private int maxNumTurns;
	
	public TestManager() {
		this.trace = new JSONArray();
	}
	
	/**
	 * Parses the input to create and start a game. Gets 
	 * the relevant information for the game, plays the game,
	 * and outputs the interactions that occur over the course
	 * of the game.
	 * @param args - command line arguments
	 */
	public static void main(String[] args) {
		TestManager managerParser = new TestManager();
		managerParser.parseInput();
		managerParser.playGame();
		managerParser.outputTrace();
	}
	
	/**
	 * Parses the JSON input from STDIN. Gets the name list, level 
	 * map, items, number of turns, initial positions, and moves. 
	 * Creates the players and adversaries, registers them with the
	 * GameManager, and creates the level. 
	 */
	private void parseInput() {
	  JSONTokener inputTokens = new JSONTokener(System.in);

	  Object value;
	  value = inputTokens.nextValue();

	  //If the input is not a JSONArray, end the program
	  if (!(value instanceof JSONArray)) {
		  throw new IllegalArgumentException("Not in valid JSON format");
	  }

	  JSONArray JSONInput = (JSONArray) value;
	  JSONArray nameList = JSONInput.getJSONArray(0);
	  
	  JSONObject JSONLevel = JSONInput.getJSONObject(1);
	  List<LevelComponent> levelMap = TestLevel.parseLevelMap(JSONLevel);
	  List<Item> items = TestLevel.parseObjects(JSONLevel);
	  
	  this.maxNumTurns = JSONInput.getInt(2);  
	  JSONArray initialPositions = JSONInput.getJSONArray(3);
	  JSONArray allMoveLists = JSONInput.getJSONArray(4);	 
	  
	  this.gameManager = new TraceManager(this.trace);
	  List<TestPlayer> testPlayers = createTestPlayers(allMoveLists);
	  
	  //Register the players and adversaries
	  registerPlayers(nameList, testPlayers);
	  generateActorMaps(nameList, initialPositions);
	  registerAdversaries();
	  
	  //Create the level
	  this.level = new LevelImpl(this.players, this.adversaries, levelMap, false, false, items);
	}
	
	
	/**
	 * Creates the players that will be used to test the GameManager and RuleChecker,
	 * and stores the list of moves within the TestPlayer object
	 * @param allMoveLists - a list of moves for each registered player
	 * @return a list of TestPlayers created with their corresponding moves
	 */
	private List<TestPlayer> createTestPlayers(JSONArray allMoveLists) {
		List<TestPlayer> testPlayers = new ArrayList<>();
		
		for (int i = 0; i < allMoveLists.length(); i++) {
			JSONArray JSONPlayerMoveList = allMoveLists.getJSONArray(i);
			List<Point> playerMoveList = new ArrayList<>();
			
			for (int j = 0; j < JSONPlayerMoveList.length(); j++) {
				JSONObject playerMove = JSONPlayerMoveList.getJSONObject(j);
				if (playerMove.isNull("to")) {
					playerMoveList.add(null);
				} else {
					JSONArray point = playerMove.getJSONArray("to");
					playerMoveList.add(parsePoint(point));
				}
			}
			TestPlayer player = new TestPlayer(playerMoveList, this.trace);
			testPlayers.add(player);
		}
		
		return testPlayers;
	}
	
	/**
	 * Registers the players with the GameManager
	 * @param nameList - the list of names for all players in the game
	 * @param testPlayers - the TestPlayers that will take turns and receive 
	 * updates from the GameManager
	 */
	private void registerPlayers(JSONArray nameList, List<TestPlayer> testPlayers) {
		for (int i = 0; i < nameList.length(); i++) {
			String name = nameList.getString(i);
			TestPlayer testPlayer = testPlayers.get(i);
			gameManager.registerPlayer(name, testPlayer);			
		}		
	}
	
	/**
	 * Creates the actor maps with the corresponding Player or Adversary and the
	 * actor's initial location
	 * @param nameList - the list of names for all players in the game
	 * @param initialPositions - the list of initial player and adversary positions
	 */
	private void generateActorMaps(JSONArray nameList, JSONArray initialPositions) {
		this.players = new HashMap<>();
		this.adversaries = new HashMap<>();
		
		for (int i = 0; i < initialPositions.length(); i++) {
			//Add players until we run out of names
			if (i < nameList.length()) {
				Player player = new Player(nameList.getString(i));
				Point location = parsePoint(initialPositions.getJSONArray(i));
				this.players.put(player, location);
			}
			//Add adversaries for the remaining initial positions
			else {
				Adversary zombie = new Zombie();
				Point location = parsePoint(initialPositions.getJSONArray(i));
				this.adversaries.put(zombie, location);
			}
		}
	}

	/**
	 * Registers all adversaries in the game with the GameManager
	 */
	private void registerAdversaries() {
		for (Adversary adversary : this.adversaries.keySet()) {
			String name = adversary.getName();
			this.gameManager.registerAdversary(name);
		}
	}
	
	/**
	 * Starts the game by notifying all players of their initial state.
	 * Plays the game until:
	 * - The given number of turns are performed and an IllegalStateException 
	 *   is thrown
	 * - One of the move input streams is exhausted and an IllegalStateException
	 *   is thrown
	 * - The level is over
	 */
	private void playGame() {
		this.gameManager.notifyAllObservers();
		try {
			this.gameManager.playLevelTrace(this.level, this.maxNumTurns);		
		} catch (IllegalStateException e) {
			//Do nothing
		}		
	}

	/**
	 * Outputs once game play has stopped. This includes the state that 
	 * existed at the end of the sequence, as well as the full manager
	 * trace of interactions and updates over the course of the game
	 */
	private void outputTrace() {
		//Get the state of the level
		JSONObject JSONState = Generator.generateJSONState(this.level);

		JSONArray testOutput = new JSONArray();
		testOutput.put(JSONState);
		testOutput.put(this.trace);

		System.out.print(testOutput.toString());
	}

}




