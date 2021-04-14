package Remote;

import Game.model.Player;
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
	 * @param message
	 */
	public void sendMessage(String name, String message) {
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
	 * @return
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
		
		sendMessage(name, endLevel.toString());
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
			sendMessage(name, endGame.toString());
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
