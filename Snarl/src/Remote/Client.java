package Remote;

import static Utils.ParseUtils.parsePoint;

import Game.model.Exit;
import Game.model.Item;
import Game.model.Key;
import Level.TestLevel;
import Player.LocalPlayer;
import java.awt.Point;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Represents a remote player client that connects the player 
 * front-end to the server via TCP. Allows a game of Snarl to
 * be played.
 */
public class Client {
	//The instance of the LocalPlayer used for displaying messages
	//and taking turns
	private LocalPlayer player;
	private Socket socket;

	private JSONTokener inputFromServer;
	private PrintStream outputToServer;

	/**
	 * Creates a Client that connects to the server with a given IP address and 
	 * port number
	 * @param ipAddress - the IP address to connect to
	 * @param port - the port to connect to
	 * @throws IllegalArgumentException if the socket cannot be created, or if 
	 * the socket is not connected when attempting to get the input or output stream 
	 */
	public Client(String ipAddress, int port) {
		this.player = new LocalPlayer();
		try {
			this.socket = new Socket(ipAddress, port);
			this.inputFromServer = new JSONTokener(this.socket.getInputStream());
			this.outputToServer = new PrintStream(this.socket.getOutputStream());
		} catch (IOException e) {
			throw new IllegalArgumentException("Unable to create client");
		}
	}

	/**
	 * Creates a Client for testing purposes
	 * @param player - the player to control
	 * @param socket - the socket used for communication with the server
	 * @throws IllegalArgumentException if the socket is not connected when attempting to
	 * get the input or output stream
	 */
	public Client(LocalPlayer player, Socket socket) {
		this.player = player;
		try {
			this.socket = socket;
			this.inputFromServer = new JSONTokener(this.socket.getInputStream());
			this.outputToServer = new PrintStream(this.socket.getOutputStream());
		} catch (IOException e) {
			throw new IllegalArgumentException("Unable to create client");
		}
	}

	/**
	 * Handles messages to and from the server while the socket is connected.
	 * Parses the corresponding JSON commands for:
	 * - welcome
	 * - name
	 * - start-level
	 * - player-update
	 * - move
	 * - end-level
	 * - end-game
	 */
	public void run() {
		while (!socket.isClosed()) {
			Object value = inputFromServer.nextValue();
			if (isJSONCommand(value, "welcome")) {
				welcomePlayer((JSONObject) value);
			} else if (isJSONString(value, "name")) {
				getPlayerName();
			} else if (isJSONCommand(value, "start-level")) {
				startLevel((JSONObject) value);
			} else if (isJSONCommand(value, "player-update")) {
				updatePlayer((JSONObject) value);
			} else if (isJSONString(value, "move")) {
				movePlayer();
			} else if (isResult(value)) {
				processResult((String) value);
			} else if (isJSONCommand(value, "end-level")) {
				endLevel((JSONObject) value);
			} else if (isJSONCommand(value, "end-game")) {
				endGame((JSONObject) value);
			}
		}
	}

	/**
	 * Determines if the provided object contains the "type" that matches 
	 * the provided command
	 * @param value - the Object to compare
	 * @param command - the String command to compare to
	 * @return false if the command is not a JSONObject or does not match
	 * the provided command. Return true if the command type for the JSONObject
	 * matches the provided command
	 */
	private static boolean isJSONCommand(Object value, String command) {
		if (!(value instanceof JSONObject)) {
			return false;
		}
		return getCommandType((JSONObject) value).equals(command);
	}
	
	/**
	 * Gets the command type from the provided JSONObject
	 * @param json - the JSONObject to get the type from
	 * @return the String that corresponds to the "type" key
	 */
	private static String getCommandType(JSONObject json) {
		return json.getString("type");
	}

	/**
	 * Determines if the provided object matches the provided string
	 * @param value - the Object to compare
	 * @param string - the string to compare to
	 * @return true if the value is a String and matches the provided
	 * string, false otherwise 
	 */
	private static boolean isJSONString(Object value, String string) {
		if (!(value instanceof String)) {
			return false;
		}
		return ((String) value).equals(string);
	}

	/**
	 * Determines if the provided object is a valid result value from the
	 * Server. A valid result includes:
	 * - OK
	 * - Key
	 * - Exit
	 * - Eject
	 * - Invalid
	 * @param value - the value to compare
	 * @return true if the value is a valid result, false otherwise 
	 */
	private static boolean isResult(Object value) {
		if (!(value instanceof String)) {
			return false;
		}
		Set<String> validResults = new HashSet<>();
		validResults.addAll(Arrays.asList("OK", "Key", "Exit", "Eject", "Invalid"));
		return validResults.contains((String) value);
	}

	/**
	 * Displays the server welcome message to the user. This includes the IP address
	 * and port number that the client is connected to
	 * @param json - the JSON command containing the IP address and port number
	 */
	protected void welcomePlayer(JSONObject json) {
		JSONObject serverInfo = json.getJSONObject("info");
		String ipAddress = serverInfo.getString("ip-address");
		int port = serverInfo.getInt("port");
		this.player.displayMessage("Connected to server at ip: " + ipAddress + " port: " + port);
	}

	/**
	 * Prompts the player to provide a name and outputs this name to the server
	 */
	protected void getPlayerName() {
		Scanner in = new Scanner(System.in);
		System.out.println("Enter player name:");
		String name = in.next();
		this.outputToServer.println(name);
	}

	/**
	 * Displays the start-level message to the player. This includes the level index and the 
	 * names of all players in the level
	 * @param json - the JSON command containing the level index and list of players
	 */
	protected void startLevel(JSONObject json) {
		int levelIndex = json.getInt("level");
		JSONArray nameList = json.getJSONArray("players");
		Set<String> names = new HashSet<String>();
		for (int i = 0; i < nameList.length(); i++) {
			names.add(nameList.getString(i));
		}
		
		String message = "Starting level " + levelIndex + " with players "  + nameList.toString();
		this.player.displayMessage(message);
	}

	/**
	 * Displays the player-update to the player. This includes the layout of the level within
	 * their view, their position in the level, items in their view, other actors in their view,
	 * and potentially a message regarding their status in the level
	 * @param json - the JSON command containing the layout, position, objects, actors, and message
	 */
	protected void updatePlayer(JSONObject json) {
		JSONArray layout = json.getJSONArray("layout");
		JSONArray JSONPosition = json.getJSONArray("position");
		Point position = Utils.ParseUtils.parsePoint(JSONPosition);
		List<Item> items = TestLevel.parseObjects(json);
		JSONArray JSONActors = json.getJSONArray("actors");
		String renderedMap = renderMap(layout, position, items, JSONActors);
		int currentHealth = json.getInt("current-health");
		int maxHealth = json.getInt("max-health");

		//Update the local player with their current position
		this.player.updatePosition(position);
		
		this.player.displayMessage("Current position: [" + position.x + "," + position.y + "]");
		this.player.displayMessage("You have " + currentHealth + " out of " + maxHealth + " health points\n");
		this.player.displayMessage(renderedMap);
		if (!json.isNull("message")) {
			String message = json.getString("message");
			this.player.displayMessage(message);
		}
	}

	/**
	 * Renders the JSON layout into the corresponding player view. Entities are represented with the 
	 * following characters:
	 * - Space "."
	 * - Wall "X"
	 * - Door "|"
	 * - Player "P"
	 * - Ghost "G"
	 * - Zombie "Z"
	 * - Key "!"
	 * - Exit "@"
	 * @param JSONLayout - the JSON representation of walls, spaces, and doors
	 * @param position - the position of this player
	 * @param items - the items within the player's view
	 * @param JSONActors - the actors within the player's view
	 * @return a String representing the rendered player map view
	 */
	private String renderMap(JSONArray JSONLayout, Point position, List<Item> items, JSONArray JSONActors) {
		Map<Point, Character> actorPositionMap = parseActorArray(JSONActors);
		Map<Point, Character> itemPositionMap = parseItems(items);
		List<List<Integer>> layout = generateLayout(JSONLayout);
		
		//Determine the top left coordinate 
		int layoutHalfLength = layout.size() / 2;
		Point topLeft = new Point(position.x - layoutHalfLength, position.y - layoutHalfLength);

		StringBuilder mapBuilder = new StringBuilder();
		int rowIndex = topLeft.y;
		int colIndex = topLeft.x;
		for (List<Integer> row : layout) {
			colIndex = topLeft.x;
			for (Integer tile : row) {
				Point currPos = new Point(colIndex, rowIndex);
				if (tile == 0) {
					mapBuilder.append('X');
				} else if (position.equals(currPos)) {
					mapBuilder.append('P');
				} else if (actorPositionMap.containsKey(currPos)) {
					mapBuilder.append(actorPositionMap.get(currPos));
				} else if (itemPositionMap.containsKey(currPos)) {
					mapBuilder.append(itemPositionMap.get(currPos));
				} else if (tile == 2) {
					mapBuilder.append('|');
				} else {
					mapBuilder.append('.');
				}
				colIndex++;
			}
			rowIndex++;
			mapBuilder.append("\n");
		}
		return mapBuilder.toString();
	}

	/**
	 * Parses the JSONArray of JSONObjects into a map of Point to Character
	 * that represents the actor within the player map view
	 * @param JSONActors -the JSONActors to parse
	 * @return a Map of Point to Character that represents the location for
	 * a given actor 
	 */
	private Map<Point, Character> parseActorArray(JSONArray JSONActors) {
		Map<Point, Character> actorPositionMap = new HashMap<>();
		for (int i = 0; i < JSONActors.length(); i++) {
			JSONObject JSONActorPosition = JSONActors.getJSONObject(i);
			String actorType = JSONActorPosition.getString("type");
			Character charType = actorTypeToCharacter(actorType);

			JSONArray JSONPoint = JSONActorPosition.getJSONArray("position");
			Point position = parsePoint(JSONPoint);

			actorPositionMap.put(position, charType);
		}
		return actorPositionMap;
	}

	/**
	 * Converts the actorType to the corresponding Character value
	 * @param actorType - the type of the actor (player, zombie, ghost)
	 * @return the Character that represents the actor type
	 */
	private Character actorTypeToCharacter(String actorType) {
		switch (actorType) {
		case "player":
			return 'P';
		case "zombie":
			return 'Z';
		case "ghost":
			return 'G';
		default:
			throw new IllegalArgumentException("Invalid actor type");
		}
	}

	/**
	 * Parses the provided items into a map of Point to Character
	 * that represents the item within the player map view
	 * @param items -the items to parse
	 * @return a Map of Point to Character that represents the location for
	 * a given item 
	 */
	private Map<Point, Character> parseItems(List<Item> items) {
		Map<Point, Character> itemPositionMap = new HashMap<>();
		for (Item item : items) {
			Point position = item.getLocation();
			Character itemType;
			if (item instanceof Key) {
				itemType = '!';
			} else if (item instanceof Exit) {
				itemType = '@';
			} else {
				throw new IllegalArgumentException("Unknown item type");
			}
			itemPositionMap.put(position, itemType);
		}

		return itemPositionMap;
	}

	/**
	 * Converts the provided JSONLayout into a List<List<Integer>>
	 * @param JSONLayout - the JSONLayout to convert
	 * @return the JSONArray in the corresponding List<List<Integer>> form
	 */
	private List<List<Integer>> generateLayout(JSONArray JSONLayout) {
		List<List<Integer>> layout = new ArrayList<>();
		for (int row = 0; row < JSONLayout.length(); row++) {
			List<Integer> layoutRow = new ArrayList<>();
			JSONArray JSONRow = JSONLayout.getJSONArray(row);
			for (int col = 0; col < JSONRow.length(); col++) {
				layoutRow.add(JSONRow.getInt(col));
			}
			layout.add(layoutRow);
		}
		return layout;
	}
	
	/**
	 * Prompts the local player to take a turn, and sends the corresponding
	 * move to the server
	 */
	protected void movePlayer() {
		Point move = this.player.takeTurn();
		JSONObject JSONMove = new JSONObject();
		JSONMove.put("type", "move");
		
		if (move == null) {
			JSONMove.put("to", JSONObject.NULL);
		} else {
			JSONArray JSONPoint = JSONUtils.Generator.generateJSONPoint(move);
			JSONMove.put("to", JSONPoint);
		}
		
		this.outputToServer.println(JSONMove.toString());		
	}
	
	/**
	 * Processes the result from the server and outputs the corresponding message
	 * to the player
	 * @param result - the result to process
	 */
	protected void processResult(String result) {
		if (result.equals("Invalid")) {
			this.player.displayMessage("The move was invalid");
		}
		if (result.equals("Exit")) {
			this.player.displayMessage("You exited the level");
		}
		if (result.equals("Eject")) {
			this.player.displayMessage("You were ejected from the level");
		}
	}
	
	/**
	 * Displays the end-level message to the player with the name of the player that found
	 * the key, the names of the players that exited the level, and the names of the players 
	 * that were ejected from the level 
	 * @param json - the end-level message to parse 
	 */
	protected void endLevel(JSONObject json) {
		JSONArray exitedPlayers = json.getJSONArray("exits");
		JSONArray ejectedPlayers = json.getJSONArray("ejects");
		this.player.displayMessage("The level is over");
		if (json.isNull("key")) {
			this.player.displayMessage("No one found the key");
		} else {
			this.player.displayMessage("The key was found by: " + json.getString("key"));
		}
		
		if (exitedPlayers.length() == 0) {
			this.player.displayMessage("No one exited the level");
		} else {
			this.player.displayMessage("The following players exited the level: " + exitedPlayers);
		}
		
		if (ejectedPlayers.length() == 0) {
			this.player.displayMessage("No one was ejected from the level");
		} else {
			this.player.displayMessage("The following players were ejected from the level: " + ejectedPlayers);
		}	
	}
	
	/**
	 * Displays the end-game message to the user with the resulting player scores. For each player
	 * in the game, it shows the name, number of exits, number of ejects, and number of keys found. 
	 * Once all scores are displayed, the socket is closed
	 * @param json - the end-game message to parse 
	 */
	protected void endGame(JSONObject json) {
		this.player.displayMessage("PLAYER SCORES");
		JSONArray scores = json.getJSONArray("scores");
		for (int i = 0; i < scores.length(); i++) {
			JSONObject playerScore = scores.getJSONObject(i);
			String name = playerScore.getString("name");
			int numExits = playerScore.getInt("exits");
			int numEjects = playerScore.getInt("ejects");
			int numKeys = playerScore.getInt("keys");
			
			this.player.displayMessage(name + ": exited " + numExits + " times and was ejected " + numEjects +
			          " times. Found " + numKeys + " keys");

		}
		try {
			this.socket.close();
		} catch (IOException e) {
			throw new IllegalArgumentException("Unable to close connection");
		}
	}

}
