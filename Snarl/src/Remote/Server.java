package Remote;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import Game.controller.GameManager;
import Game.model.Level;
import Observer.LocalObserver;
import Player.RemotePlayer;

/**
 * Represents the server within a distributed game of Snarl. Waits for player clients to connect via a 
 * TCP socket. Once all players have joined, the server communicates with the GameManager to start and 
 * play a game of Snarl
 */
public class Server {
	
	private GameManager gameManager;
	private ServerSocket socket;
	private Map<String, Socket> playerSockets;
	private JSONObject welcomeMessage;

	/**
	 * Creates the server at the specified IP address and port
	 * @param ipAddress - the IP address where the server will listen for connections
	 * @param port - the port to listen on
	 * @throws IOException
	 */
	public Server(String ipAddress, int port) {
		try {
			InetAddress inetAddress = InetAddress.getByName(ipAddress);
			this.socket = new ServerSocket(port, 50, inetAddress);
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException("Unknown IP address");
		}	catch (IOException e) {
			throw new IllegalArgumentException("Unable to create server");
		}
		this.gameManager = new GameManager();
		this.welcomeMessage = generateWelcomeMessage(ipAddress, port);
		this.playerSockets = new HashMap<>();
	}
	
	/**
	 * Generates the welcome message to send to all connected player clients
	 * @param ipAddress - the IP address that the server is listening on
	 * @param port - the port that the server is listening on
	 * @return a JSONObject containing the welcome message
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
	 * Runs the server with the provided levels. Registers up to the max number of players,
	 * with a specified wait time between registrations. Creates an observer for the game if 
	 * specified. Starts and plays the game. Disconnects all clients once the game is over
	 * @param levels - the list of levels for the game
	 * @param maxPlayers - the maximum number of clients the server will wait for before starting the game
	 * @param waitTime - the number of seconds to wait for the next client to connect
	 * @param observe - if true, the server will start a local observer to display the progress of the game
	 * @throws IOException if players cannot be registered 
	 */
	public void run(List<Level> levels, int maxPlayers, int waitTime, boolean observe) {
		try {
			registerPlayers(maxPlayers, waitTime);
		} catch (IOException e) {
			throw new IllegalStateException("Unable to register players so cannot continue with game");
		}
		
		int numLevels = levels.size();
		registerAdversaries(numLevels);
		
		if (observe) {
			this.gameManager.attachObserver(new LocalObserver());
		}

		this.gameManager.startGame(levels, 1);	
		this.gameManager.playGame();
		disconnectClients();
	}
	
	/**
	 * Registers player clients for a game of Snarl and prompts the clients
	 * to provide a name for the player 
	 * @param maxPlayers - the maximum number of clients the server will wait for before starting the game
	 * @param waitTime - the number of seconds to wait for the next client to connect
	 * @throws IOException if the socket is not connected
	 * 
	 */
	public void registerPlayers(int maxPlayers, int waitTime) throws IOException {
		
		//Up to the max number of players to wait for 
		for (int i = 0; i < maxPlayers; i++) {
			Socket playerSocket;
			String name;
			
			//The timeout to wait for before no longer accepting players 
			this.socket.setSoTimeout(waitTime * 1000);
			
			try {
				playerSocket = this.socket.accept();
			} catch (java.net.SocketTimeoutException e) {
				//Once we timeout, no more players are accepted
				break;
			}
			
			DataOutputStream outputToClient = new DataOutputStream(playerSocket.getOutputStream());
			BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
			
			//Send the welcome message to the client
			outputToClient.writeBytes(this.welcomeMessage.toString() + "\n");
			
			//Prompt the client to enter a name. If the name is not valid with the game manager,
			//continue to prompt the client to enter a name
			while (true) {
				outputToClient.writeBytes("name\n");
				name = inputFromClient.readLine();
				try {
					RemotePlayer remotePlayer = new RemotePlayer(this, name);
					this.gameManager.registerPlayer(name, remotePlayer);
					break;
				} catch (IllegalArgumentException e) {
					//keep going
				}
			}
			
			//Add the name and player socket to the map of playerSockets
			this.playerSockets.put(name, playerSocket);	
		}
		
	}

	/**
	 * Registers adversaries with the game manager. Creates the maximum number
	 * of Ghosts and Zombies for the game. A subsection of these adversaries
	 * may be used based on the current level number
	 * @param numLevels - the total number of levels in the game (the max)
	 */
	private void registerAdversaries(int numLevels) {
		//Max number of ghosts and zombies needed for the game
		int numZombies = Math.floorDiv(numLevels, 2) + 1;
		int numGhosts = Math.floorDiv((numLevels - 1), 2);
		this.gameManager.registerAdversaries(numZombies, numGhosts);
	}
	
	/**
	 * Sends a message to the corresponding playerSocket based on the name
	 * of the player
	 * @param name - the name of the player
	 * @param message - the message to send
	 */
	public void sendMessage(String name, String message) {
		Socket playerSocket = this.playerSockets.get(name);
		try {
			DataOutputStream outputToClient = new DataOutputStream(playerSocket.getOutputStream());
			outputToClient.writeBytes(message + "\n");
		} catch (IOException e) {
			throw new IllegalStateException("Unable to communicate with player socket");
		}	
	}
	
	/**
	 * Receives a message from the playerSocket that corresponds to the provided name
	 * @param name - the name of the player
	 * @return the Object that is received from the playerClient
	 */
	public Object receiveMessage(String name) {
		Socket playerSocket = this.playerSockets.get(name);
		BufferedReader inputFromClient;
		try {
			inputFromClient = new BufferedReader(
					new InputStreamReader(playerSocket.getInputStream()));
		} catch (IOException e) {
			throw new IllegalStateException("Unable to communicate with player socket");
		}
		JSONTokener tokener = new JSONTokener(inputFromClient);
		return tokener.nextValue();	
	}

	/**
	 * Sends the end level message to the playerClient that corresponds to the provided name
	 * @param name - the name of the player
	 * @param keyFinder - the name of the player that found the key in the level
	 * @param exitedPlayers - the names of the players that exited the level
	 * @param ejectedPlayers - the names of the players that were ejected from the level
	 */
	public void sendLevelEnd(String name, String keyFinder, List<String> exitedPlayers, List<String> ejectedPlayers) {
		JSONObject endLevel = new JSONObject();

		JSONArray exitNameList = new JSONArray(exitedPlayers);
		JSONArray ejectsNameList = new JSONArray(ejectedPlayers);

		endLevel.put("type", "end-level");
		if (keyFinder == null) {
			endLevel.put("key", JSONObject.NULL);
		} else {
			endLevel.put("key", keyFinder);
		}
		endLevel.put("exits", exitNameList);
		endLevel.put("ejects", ejectsNameList);
		
		sendMessage(name, endLevel.toString());
	}
	
	/**
	 * Sends the end game message to the playerClient that corresponds to the provided name
	 * @param name - the name of the player
	 * @param keysFound - the map of player names to the total keys found during the game
	 * @param numEjects - the map of player names to the total number of ejects during the game
	 * @param numExits - the map of player names to the total number of exits during the game
	 */
	public void sendEndGame(String name, Map<String, Integer> keysFound, Map<String, Integer> numEjects,
			Map<String, Integer> numExits) {
		JSONObject endGame = new JSONObject();
		JSONArray playerScoreList = new JSONArray();		
		endGame.put("type", "end-game");
		
		for (String currName : this.playerSockets.keySet()) {
			JSONObject playerScore = new JSONObject();
			playerScore.put("type", "player-score");
			playerScore.put("name", currName);
			playerScore.put("exits", numExits.get(currName));
			playerScore.put("ejects", numEjects.get(currName));
			playerScore.put("keys", keysFound.get(currName));
			playerScoreList.put(playerScore);
		}
		
		endGame.put("scores", playerScoreList);
		
		sendMessage(name, endGame.toString());
	}
	
	/**
	 * Disconnects all clients from the server at the end of the game
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
