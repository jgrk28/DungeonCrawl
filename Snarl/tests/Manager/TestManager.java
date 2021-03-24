package Manager;

import static Utils.ParseUtils.parsePoint;

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
 * TODO Add comments
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
	
	public static void main(String[] args) {
		TestManager managerParser = new TestManager();
		managerParser.parseInput();
		managerParser.playGame();
	}
	
	private void parseInput() {
	  JSONTokener inputTokens = new JSONTokener(System.in);

	  Object value;
	  value = inputTokens.nextValue();

	  // If the input is not a JSONArray, end the program
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
	  registerPlayers(nameList, testPlayers);
	  generateActorMaps(nameList, initialPositions);
		registerAdversaries();
		this.level = new LevelImpl(this.players, this.adversaries, levelMap, false, false, items);
	}
	
	private void registerPlayers(JSONArray nameList, List<TestPlayer> testPlayers) {
		for (int i = 0; i < nameList.length(); i++) {
			String name = nameList.getString(i);
			TestPlayer testPlayer = testPlayers.get(i);
			gameManager.registerPlayer(name, testPlayer);			
		}		
	}
	
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
	
	private void generateActorMaps(JSONArray nameList, JSONArray initialPositions) {
		this.players = new HashMap<>();
		this.adversaries = new HashMap<>();
		
		for (int i = 0; i < initialPositions.length(); i++) {
			//Players
			if (i < nameList.length()) {
				Player player = new Player(nameList.getString(i));
				Point location = parsePoint(initialPositions.getJSONArray(i));
				this.players.put(player, location);
			}
			//Adversaries
			else {
				Adversary zombie = new Zombie();
				Point location = parsePoint(initialPositions.getJSONArray(i));
				this.adversaries.put(zombie, location);
			}
		}
	}

	private void registerAdversaries() {
		for (Adversary adversary : this.adversaries.keySet()) {
			String name = adversary.getName();
			this.gameManager.registerAdversary(name);
		}
	}
	
	private void playGame() {
		this.gameManager.notifyAllObservers();
		try {
			this.gameManager.playLevelTrace(this.level, this.maxNumTurns);		
		} catch (IllegalStateException e) {
			//Do nothing
		}
		
	}

}




