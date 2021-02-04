package a4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import org.json.*;

public class TCPTravellerClient {
	
	private String ipAddress;
	private int port;
	private String username;
	private Socket socket;
	private PrintStream outputToServer;
	private JSONTokener inputFromServer;
	private Set<JSONObject> placeCharacterCommands;
	JSONTokener inputFromUser;
	
	public TCPTravellerClient() {
		
	}

	public static void main(String[] args) {
		TCPTravellerClient client = new TCPTravellerClient();
		client.parseCommandLine(args);
		client.initializeConnection();	
		client.createNetwork();
		client.processUserCommands();
		
		if (!client.isServerAlive()) {
			client.endConnection();
			return;
		}
		
	}
	
	/**
	 * 
	 * @param args
	 * @throws NumberFormatException
	 */
	private void parseCommandLine(String[] args) throws NumberFormatException {
		if (args.length < 3) {
			username = "Glorifrir Flintshoulder";
		} else {
			username = args[2];
		}
		
		if (args.length < 2) {
			port = 8000;
		} else {
			port = Integer.parseInt(args[1]);
		}
		
		if (args.length < 1) {
			ipAddress = "127.0.0.1";
		} else {
			ipAddress = args[0];
		}
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	private void initializeConnection() throws IOException {
		//Opens a connection to the server
		socket = new Socket(ipAddress, port);
		outputToServer = new PrintStream(socket.getOutputStream());
		inputFromServer = new JSONTokener(socket.getInputStream());
		inputFromUser = new JSONTokener(System.in);
		
		//Send sign-up name
		outputToServer.print(username);
		
		//Receive session ID
		Object inputObject = inputFromServer.nextValue();
		if (inputObject instanceof String) {
			String sessionID = (String) inputObject;
			printSessionID(sessionID);
		} else {
			throw new IOException("Invalid session ID from the server");
		}
	}
	
	private void printSessionID(String sessionID) {
		JSONArray output = new JSONArray();
		output.put("the server will call me");
		output.put(sessionID);
		System.out.println(output.toString());
	}
	
	private void createNetwork() {
		Object inputObject = inputFromUser.nextValue();
		
		if (!(inputObject instanceof JSONObject)) {
			throw new IllegalArgumentException("Input is not a JSONObject");
		}
		
		JSONObject createNetworkJSON = (JSONObject) inputObject;
		
		try {
			String command = createNetworkJSON.getString("command");
			
			if (!(command.equals("roads"))) {
				throw new IllegalArgumentException("Invalid command. Must start with roads command.");
			}
			
		} catch (JSONException e) {
			throw new JSONException("The request does not contain a command");
		}
		
		sendServerCreateNetwork(createNetworkJSON);
		
	}
	
	private void sendServerCreateNetwork(JSONObject userCommand) {
		JSONObject serverCommand = new JSONObject();
		Set<String> towns = new HashSet<String>();
		JSONArray roads = new JSONArray();
		JSONArray params = userCommand.getJSONArray("params");
		
		for (int i = 0; i < params.length(); i++) {
			JSONObject currRoad = params.getJSONObject(i);
			roads.put(currRoad);
			
			String fromTown = currRoad.getString("from");
			String toTown = currRoad.getString("to");
			
			if (!towns.contains(fromTown)) {
				towns.add(fromTown);
			}
			if (!towns.contains(toTown)) {
				towns.add(toTown);
			}
		}
		
		serverCommand.put("towns", new JSONArray(towns));
		serverCommand.put("roads", roads);
		
		outputToServer.print(serverCommand);
	}
	
	private void processUserCommands() {
		Object inputObject = inputFromUser.nextValue();
		
		if (!(inputObject instanceof JSONObject)) {
			throw new IllegalArgumentException("Input is not a JSONObject");
		}
		
		JSONObject commandJSON = (JSONObject) inputObject;
		
		try {
			String command = commandJSON.getString("command");
			
			if (command.equals("place")) {
				placeCharacter(commandJSON);
			} 
			else if (command.equals("passage-safe?")) {
				passageSafeQuery(commandJSON);
			} 
			else {
				throw new IllegalArgumentException("Invalid command. Must start with roads command.");
			}
			
		} catch (JSONException e) {
			throw new JSONException("The request does not contain a command");
		}
		
		
	}
	
	/*
	 * { "command" : "place", 
  	"params" : { "character" : String, "town" : String } }
  	
  	{ "characters" : [ { "name" : String, "town" : String }, ... ],
  	"query" : { "character" : String, "destination" : String } }
  	
	 */
	private void placeCharacter(JSONObject commandJSON) {
		JSONObject newCharacterJSON = new JSONObject();
		JSONObject params = commandJSON.getJSONObject("params");
		
		String name = params.getString("character");
		String town = params.getString("town");
		
		newCharacterJSON.put("name", name);
		newCharacterJSON.put("town", town);
		
		//TO-DO
		//placeCharacterCommands.
		
		
	}
	
	private void passageSafeQuery(JSONObject commandJSON) {
		//Send batch of commands
	}
	
	private boolean isServerAlive() {
		return socket.isConnected();		
	}
	
	private void receiveResponse() {
		
	}
	
	private void endConnection() {
		
	}
	
	
	
}
