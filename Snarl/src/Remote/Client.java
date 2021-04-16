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

public class Client {
	private LocalPlayer player;
	private Socket socket;

	private JSONTokener inputFromServer;
	private PrintStream outputToServer;

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

	private static boolean isJSONCommand(Object value, String command) {
		if (!(value instanceof JSONObject)) {
			return false;
		}
		return getCommandType((JSONObject) value).equals(command);
	}

	private static boolean isJSONString(Object value, String string) {
		if (!(value instanceof String)) {
			return false;
		}
		return ((String) value).equals(string);
	}

	private static boolean isResult(Object value) {
		if (!(value instanceof String)) {
			return false;
		}
		Set<String> validResults = new HashSet<>();
		validResults.addAll(Arrays.asList("OK", "Key", "Exit", "Eject", "Invalid"));
		return validResults.contains((String) value);
	}

	private static String getCommandType(JSONObject json) {
		return json.getString("type");
	}

	private void welcomePlayer(JSONObject json) {
		JSONObject serverInfo = json.getJSONObject("info");
		String ipAddress = serverInfo.getString("ip-address");
		int port = serverInfo.getInt("port");
		this.player.displayMessage("Connected to server at ip: " + ipAddress + " port: " + port);
	}

	private void getPlayerName() {
		Scanner in = new Scanner(System.in);
		System.out.println("Enter player name:");
		String name = in.next();
		this.outputToServer.println(name);
	}

	private void startLevel(JSONObject json) {
		int levelIndex = json.getInt("level");
		JSONArray nameList = json.getJSONArray("players");
		Set<String> names = new HashSet<String>();
		for (int i = 0; i < nameList.length(); i++) {
			names.add(nameList.getString(i));
		}
		this.player.sendLevelStart(levelIndex, names);
	}

	private void updatePlayer(JSONObject json) {
		JSONArray layout = json.getJSONArray("layout");
		JSONArray JSONPosition = json.getJSONArray("position");
		Point position = Utils.ParseUtils.parsePoint(JSONPosition);
		List<Item> items = TestLevel.parseObjects(json);
		JSONArray JSONActors = json.getJSONArray("actors");
		String renderedMap = renderMap(layout, position, items, JSONActors);

		this.player.updatePosition(position);
		this.player.displayMessage("Current position: [" + position.x + "," + position.y + "]");
		this.player.displayMessage(renderedMap);
		if (!json.isNull("message")) {
			String message = json.getString("message");
			this.player.displayMessage(message);
		}
	}

	private String renderMap(JSONArray JSONLayout, Point position, List<Item> items, JSONArray JSONActors) {
		Map<Point, Character> actorPositionMap = parseActorArray(JSONActors);
		Map<Point, Character> itemPositionMap = parseItems(items);
		List<List<Integer>> layout = generateLayout(JSONLayout);
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
	
	private void movePlayer() {
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
	
	private void processResult(String result) {
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
	
	private void endLevel(JSONObject json) {
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
	
	private void endGame(JSONObject json) {
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
