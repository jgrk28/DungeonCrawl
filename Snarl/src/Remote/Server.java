package Remote;

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
 * TODO Add comments
 */
public class Server {
	
	private JSONObject welcomeMessage;
	private GameManager gameManager;
	private ServerSocket socket;
	private Map<String, Socket> playerSockets;
	
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
	 * 
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
	}
	

	/**
	 * 
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
			
		}
		
	}
	
	/**
	 * TODO
	 * @param name
	 * @param validMoves
	 * @return
	 * @throws IOException
	 */
	public Point takeTurn(String name, List<Point> validMoves) throws IOException {
		Socket playerSocket = this.playerSockets.get(name);
		
		DataOutputStream outputToClient = new DataOutputStream(playerSocket.getOutputStream());
		BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
		
		outputToClient.writeBytes("move");
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

}
