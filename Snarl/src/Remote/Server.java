package Remote;

import Game.model.Actor;
import Game.model.Item;
import Game.model.Player;
import Game.modelView.EntityType;
import Game.modelView.PlayerModelView;
import JSONUtils.Generator;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import Game.controller.GameManager;
import Game.model.Level;
import Observer.LocalObserver;
import Player.RemotePlayer;

/**
 * TODO Add comments
 */
public class Server {
	
	private JSONObject welcomeMessage;
	private GameManager gameManager;
	private ServerSocket socket;
	private Map<String, Socket> playerSockets;
	private Map<String, Integer> playerKeys;
	private Map<String, Integer> playerExits;
	private Map<String, Integer> playerEjects;
	
	/**
	 * TODO
	 * @param ipAddress
	 * @param port
	 * @throws IOException
	 */
	public Server(String ipAddress, int port) throws IOException {
		try {
			InetAddress inetAddress = InetAddress.getByName(ipAddress);
			this.socket = new ServerSocket(port, 50, inetAddress);
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException("Unknown IP address");
		}	
		this.gameManager = new GameManager();
		this.welcomeMessage = generateWelcomeMessage(ipAddress, port);
		this.playerSockets = new HashMap<>();
		this.playerKeys = new HashMap<>();
		this.playerExits = new HashMap<>();
		this.playerEjects = new HashMap<>();
	}
	
	/**
	 * TODO
	 * @param ipAddress
	 * @param port
	 * @return
	 */
	private JSONObject generateWelcomeMessage(String ipAddress, int port) {
		JSONObject message = new JSONObject();
		message.put("type", "welcome");
		
		JSONObject serverInfo = new JSONObject();
		serverInfo.put("ip-address", ipAddress);
		serverInfo.put("port", port);
		
		message.put("info", serverInfo);
		return message;		
	}
	
	/**
	 * TODO
	 * @param levels
	 * @param maxPlayers
	 * @param waitTime
	 * @param observe
	 * @throws IOException
	 */
	public void run(List<Level> levels, int maxPlayers, int waitTime, boolean observe) throws IOException {
		registerPlayers(maxPlayers, waitTime);
		
		if (observe) {
			this.gameManager.attachObserver(new LocalObserver());
		}

		this.gameManager.startGame(levels, 1);	
		this.gameManager.playGame();
		this.gameManager.endGame();
		sendEndGame();
		disconnectClients();
	}
	

	/**
	 * TODO
	 * @param maxPlayers
	 * @param waitTime
	 * @throws IOException
	 */
	public void registerPlayers(int maxPlayers, int waitTime) throws IOException {
		for (int i = 0; i < maxPlayers; i++) {
			Socket playerSocket;
			String name;
			
			this.socket.setSoTimeout(waitTime * 1000);
			
			try {
				playerSocket = this.socket.accept();
			} catch (java.net.SocketTimeoutException e) {
				//Once we timeout, no more players are accepted
				break;
			}
			
			DataOutputStream outputToClient = new DataOutputStream(playerSocket.getOutputStream());
			BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
			
			outputToClient.writeBytes(this.welcomeMessage.toString());
			while (true) {
				outputToClient.writeBytes("name");
				name = inputFromClient.readLine();
				try {
					RemotePlayer remotePlayer = new RemotePlayer(this, name);
					this.gameManager.registerPlayer(name, remotePlayer);
					break;
				} catch (IllegalArgumentException e) {
					//keep going
				}
			}
			
			this.playerSockets.put(name, playerSocket);
			this.playerKeys.put(name, 0);
			this.playerExits.put(name, 0);
			this.playerEjects.put(name, 0);
			
		}
		
	}

	/**
	 * TODO
	 * @param name
	 * @param levelIndex
	 * @param levelPlayers
	 * @return
	 */
	public void sendLevelStart(String name, int levelIndex, Set<Player> levelPlayers) {
		JSONObject startLevel = new JSONObject();
		JSONArray nameList = new JSONArray();
		for (Player player : levelPlayers) {
			nameList.put(player.getName());
		}
		startLevel.put("type", "start-level");
		startLevel.put("level", levelIndex);
		startLevel.put("players", nameList);
		
		displayMessage(name, startLevel.toString());
	}

	//TODO make sure player client can handle no movement
	/**
	 * TODO
	 * @param name
	 * @param validMoves
	 * @return
	 */
	public Point takeTurn(String name, List<Point> validMoves) {
		Socket playerSocket = this.playerSockets.get(name);
		BufferedReader inputFromClient;
		try {
			DataOutputStream outputToClient = new DataOutputStream(playerSocket.getOutputStream());
			inputFromClient = new BufferedReader(
					new InputStreamReader(playerSocket.getInputStream()));

			outputToClient.writeBytes("move");
		} catch (IOException e) {
			throw new IllegalStateException("Unable to communicate with player socket");
		}
		JSONTokener tokener = new JSONTokener(inputFromClient);
		Object value = tokener.nextValue();
		
		if (!(value instanceof JSONObject)) {
			throw new IllegalArgumentException("Invalid move JSON");
		}
		
		JSONObject JSONMove = (JSONObject)value;
		Object move = JSONMove.get("to");
		
		if (move == null) {
			return null;
		} else {
			return Utils.ParseUtils.parsePoint((JSONArray)move);
		}
		
	}
	
	/**
	 * TODO
	 * @param name
	 * @param message
	 */
	public void displayMessage(String name, String message) {
		Socket playerSocket = this.playerSockets.get(name);
		try {
			DataOutputStream outputToClient = new DataOutputStream(playerSocket.getOutputStream());
			outputToClient.writeBytes(message);
		} catch (IOException e) {
			throw new IllegalStateException("Unable to communicate with player socket");
		}	
	}


	/**
	 * TODO
	 * @param name
	 * @param gameState
	 * @return
	 */
	public void update(String name, PlayerModelView gameState, String message) {
		JSONObject playerUpdate = new JSONObject();

		//TODO handle if player is dead
		if (gameState.isPlayerAlive()) {
			List<List<EntityType>> playerMap = gameState.getMap();
			Point absolutePosition = gameState.getPosition();
			List<Point> visibleDoors = gameState.getVisibleDoors();
			List<Item> visibleItems = gameState.getVisibleItems();
			Map<Actor, Point> visibleActors = gameState.getVisibleActors();

			//Generate the JSON representation of the player's state
			JSONArray layout = generateLayout(playerMap, absolutePosition, visibleDoors);
			JSONArray position = Generator.generateJSONPoint(absolutePosition);
			JSONArray objects = Generator.generateJSONObjects(visibleItems);
			JSONArray actors = Generator.generateJSONActorList(visibleActors);

			//Place in the JSONObject format
			playerUpdate = new JSONObject();
			playerUpdate.put("type", "player-update");
			playerUpdate.put("layout", layout);
			playerUpdate.put("position", position);
			playerUpdate.put("objects", objects);
			playerUpdate.put("actors", actors);
			if (message == null) {
				playerUpdate.put("message", JSONObject.NULL);
			} else {
				playerUpdate.put("message", message);
			}
		}

		displayMessage(name, playerUpdate.toString());
	}

	/**
	 * Generates the layout of the room that the player is currently located
	 * in, based on the range of their view
	 * @param playerMap - a matrix of EntityType that the player can view
	 * @param playerPosition - the player's position
	 * @param doors - the locations of the doors in the room
	 * @return a JSONArray representing the layout of the room
	 */
	private JSONArray generateLayout(List<List<EntityType>> playerMap, Point playerPosition, List<Point> doors) {
		Point topLeftPosition = new Point(playerPosition.x - 2, playerPosition.y - 2);
		JSONArray layoutOutput = new JSONArray();
		Set<Point> relativeDoors = new HashSet<>();

		//Get the relative position of the doors
		for (Point point : doors) {
			Point relPoint = new Point(point.x - topLeftPosition.x, point.y - topLeftPosition.y);
			relativeDoors.add(relPoint);
		}

		for (int i = 0; i < playerMap.size(); i++) {
			List<EntityType> entityRow = playerMap.get(i);
			JSONArray JSONRow = new JSONArray();
			for (int j = 0; j < entityRow.size(); j++) {
				EntityType entity = entityRow.get(j);

				//If the entity is empty or a wall, place a 0
				if (entity.equals(EntityType.EMPTY) || entity.equals(EntityType.WALL)) {
					JSONRow.put(0);
				}
				//If the location is a door, place a 2
				else if (relativeDoors.contains(new Point(j, i))) {
					JSONRow.put(2);
				}
				//Otherwise, place a 1
				else {
					JSONRow.put(1);
				}
			}
			layoutOutput.put(JSONRow);
		}
		return layoutOutput;
	}

	/**
	 * TODO
	 * @param name
	 * @param levelPlayers
	 * @return
	 */
	public void sendLevelEnd(String name, Set<Player> levelPlayers) {
		JSONObject endLevel = new JSONObject();

		String keyName = "";
		JSONArray exitNameList = new JSONArray();
		JSONArray ejectsNameList = new JSONArray();
		for (Player player : levelPlayers) {
			String playerName = player.getName();
			int newKeysFound = player.getKeysFound();
			int newNumExits = player.getNumExits();

			if (newKeysFound > this.playerKeys.get(playerName)) {
				keyName = playerName;
				this.playerKeys.replace(playerName, newKeysFound);
			}
			if (newNumExits > this.playerExits.get(playerName)) {
				exitNameList.put(playerName);
				this.playerExits.replace(playerName, newNumExits);
			} else {
				ejectsNameList.put(playerName);
				int numExits = this.playerEjects.get(playerName);
				this.playerEjects.replace(playerName, numExits + 1);
			}
		}

		endLevel.put("type", "end-level");
		endLevel.put("key", keyName);
		endLevel.put("exits", exitNameList);
		endLevel.put("ejects", ejectsNameList);
		
		displayMessage(name, endLevel.toString());
	}
	
	/**
	 * TODO
	 */
	private void sendEndGame() {
		JSONObject endGame = new JSONObject();
		JSONArray playerScoreList = new JSONArray();		
		endGame.put("type", "end-game");
		
		for (String name : this.playerSockets.keySet()) {
			JSONObject playerScore = new JSONObject();
			playerScore.put("type", "player-score");
			playerScore.put("name", name);
			playerScore.put("exits", this.playerExits.get(name));
			playerScore.put("ejects", this.playerEjects.get(name));
			playerScore.put("keys", this.playerKeys.get(name));		
			playerScoreList.put(playerScore);
		}
		
		endGame.put("scores", playerScoreList);
		
		for (String name : this.playerSockets.keySet()) {
			displayMessage(name, endGame.toString());
		}		
	}
	
	/**
	 * TODO
	 */
	private void disconnectClients() {
		for (Socket socket : this.playerSockets.values()) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
