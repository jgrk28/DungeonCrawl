package a4;


import java.io.IOException;
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
		placeCharacterCommands = new HashSet<JSONObject>();
	}

	public static void main(String[] args) {
		TCPTravellerClient client = new TCPTravellerClient();
		client.parseCommandLine(args);
		try {
			client.initializeConnection();
		} catch (IOException e) {
			System.out.println("Unable to initialize connection");
			e.printStackTrace();
			return;
		}
		client.handleUserInput();
		client.endConnection();
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
			printUserName(username);
		} else {
			throw new IOException("Invalid session ID from the server");
		}
	}
	
	private void printUserName(String username) {
		JSONArray output = new JSONArray();
		output.put("the server will call me");
		output.put(username);
		System.out.println(output.toString());
	}

	private void handleUserInput() {
		if (inputFromUser.more()) {
			createNetwork();
		}
		while (isServerAlive() && inputFromUser.more()) {
			processUserCommands();
		}
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
				outputError(createNetworkJSON);
				endConnection();
				throw new IllegalArgumentException("First command must be roads. Ending program");
			}
			
		} catch (JSONException e) {
			outputError(createNetworkJSON);
			endConnection();
			throw new IllegalArgumentException("First command malformed. Ending program");
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

			String fromTown;
			String toTown;
			try {
				fromTown = currRoad.getString("from");
				toTown = currRoad.getString("to");
			} catch (JSONException e) {
				outputError(userCommand);
				endConnection();
				throw new IllegalArgumentException("First command malformed. Ending program");
			}
			
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
				outputError(commandJSON);
			}
			
		} catch (JSONException e) {
			outputError(commandJSON);
		}
		
		
	}

	private void placeCharacter(JSONObject commandJSON) {
		JSONObject newCharacterJSON = new JSONObject();
		JSONObject params = commandJSON.getJSONObject("params");

		try {
			String name = params.getString("character");
			String town = params.getString("town");
			newCharacterJSON.put("name", name);
			newCharacterJSON.put("town", town);
			placeCharacterCommands.add(newCharacterJSON);
		} catch (JSONException e) {
			outputError(commandJSON);
		}
	}

	private void passageSafeQuery(JSONObject commandJSON) {
		JSONObject queryJSON = new JSONObject();
		JSONObject params = commandJSON.getJSONObject("params");

		try {
			String character = params.getString("character");
			String destination = params.getString("town");

			queryJSON.put("character", character);
			queryJSON.put("destination", destination);

			sendServerBatchRequest(queryJSON);
			receiveResponse(character, destination);
		} catch (JSONException e) {
			outputError(commandJSON);
		}
	}

	private void sendServerBatchRequest(JSONObject queryJSON) {
		JSONObject serverCommand = new JSONObject();

		serverCommand.put("characters", new JSONArray(placeCharacterCommands));
		serverCommand.put("query", queryJSON);

		outputToServer.print(serverCommand);
	}
	
	private boolean isServerAlive() {
		return socket.isConnected();		
	}

	private void receiveResponse(String character, String destination) {
		//Receive session ID
		Object inputObject = inputFromServer.nextValue();
		JSONObject responseJSON = (JSONObject) inputObject;

		JSONArray badPlacementJSON = responseJSON.getJSONArray("invalid");
		outputInvalidPlacements(badPlacementJSON);

		Boolean responseBool = responseJSON.getBoolean("response");
		outputQueryResponse(character, destination, responseBool);
	}

	private void outputInvalidPlacements(JSONArray placements) {
		for (int i = 0; i < placements.length(); i++) {
			JSONObject placement = placements.getJSONObject(i);
			JSONArray outputMessage = new JSONArray();
			outputMessage.put("invalid placement");
			outputMessage.put(placement);
			System.out.println(outputMessage.toString());
		}
	}

	private void outputQueryResponse(String character, String destination, Boolean response) {
		JSONObject query = new JSONObject();
		query.put("character", character);
		query.put("destination", destination);

		JSONArray outputMessage = new JSONArray();
		outputMessage.put("the response for");
		outputMessage.put(query);
		outputMessage.put("is");
		outputMessage.put(response);

		System.out.println(outputMessage.toString());
	}

	private void endConnection() {
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("Unable to close connection");
			e.printStackTrace();
		}
	}

	private void outputError(JSONObject invalidJSON) {
		JSONObject outputJSON = new JSONObject();
		outputJSON.put("error", "not a request");
		outputJSON.put("object", invalidJSON);
		System.out.println(outputJSON.toString());
	}
	
	
	
}
