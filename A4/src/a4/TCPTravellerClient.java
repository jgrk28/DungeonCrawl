package a4;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import org.json.*;

/**
 * This class operates as a client program for the TCP traveller server
 * It does the following:
 * - Takes in commands from the user
 * - Initiates a connection with the server
 * - Processes commands from the user
 * - Sends these commands to the server
 * - Outputs the corresponding response from the server to the user
 */
public class TCPTravellerClient {
	
	// Stores the connection information for the server
	private String ipAddress;
	private int port;
	private String username;
	
	// Input and output for the socket
	private Socket socket;
	private PrintStream outputToServer;
	private JSONTokener inputFromServer;
	
	// Keeps track of all place character commands for a given batch request
	private Set<JSONObject> placeCharacterCommands;

	// Input from the user, read from STDIN
	JSONTokener inputFromUser;
	
	/**
	 * Creates a TCPTravellerClient with an empty set of place character commands
	 */
	public TCPTravellerClient() {
		placeCharacterCommands = new HashSet<JSONObject>();
	}

	/**
	 * Creates a new client and calls the corresponding methods for reading
	 * and processing commands
	 * @param args - arguments from the command line (IP address, port, username)
	 */
	public static void main(String[] args) {
		TCPTravellerClient client = new TCPTravellerClient();
		
		// Parse the command line arguments and initialize the connection with 
		// the server
		client.parseCommandLine(args);
		try {
			client.initializeConnection();
		} catch (IOException e) {
			System.out.println("Unable to initialize connection");
			e.printStackTrace();
			return;
		}
		
		// Process the user input and end the connection once the session is over
		client.handleUserInput();
		client.endConnection();
	}
	
	/**
	 * Parses command line arguments and saves them to the corresponding variables
	 * If a particular argument is not included, the default is used
	 * @param args - arguments from the command line (IP address, port, user's name)
	 * @throws NumberFormatException if the destination port is not a number
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
	 * Initializes the connection with the server 
	 * @throws IOException if the session ID from the server is not a String
	 */
	private void initializeConnection() throws IOException {
		// Open a connection to the server
		socket = new Socket(ipAddress, port);
		outputToServer = new PrintStream(socket.getOutputStream());
		inputFromServer = new JSONTokener(socket.getInputStream());
		
		// Send sign-up name to the server
		outputToServer.print(username);
		
		//Receive session ID from the server and print the user's name
		Object inputObject = inputFromServer.nextValue();
		if (inputObject instanceof String) {
			String sessionID = (String) inputObject;
			printUserName(username);
		} else {
			throw new IOException("Invalid session ID from the server");
		}
	}
	
	/**
	 * Prints the corresponding response once the client has successfully
	 * received a session ID from the server
	 * @param username - the current user's name
	 */
	private void printUserName(String username) {
		JSONArray output = new JSONArray();
		output.put("the server will call me");
		output.put(username);
		System.out.println(output.toString());
	}

	/**
	 * If the user has entered input, attempt to create the network.
	 * Once the network has been successfully created, check for more
	 * commands from the user and process if applicable
	 */
	private void handleUserInput() {
		// Read input from the user from STDIN
		inputFromUser = new JSONTokener(System.in);
		
		if (inputFromUser.more()) {
			createNetwork();
		}
		
		// If the server has not shut down the connection and the user has 
		// not ended the session, process the input from the user
		while (isServerAlive() && inputFromUser.more()) {
			processUserCommands();
		}
	}
	
	/**
	 * Process the roads command from the user. If the command is not "roads",
	 * or if the command is malformed, we output the corresponding error and
	 * end the program.
	 * @throws IllegalArgumentException if:
	 * - The input is not a JSON object
	 * - The first command is not "roads"
	 * - The first command is malformed
	 */
	private void createNetwork() {
		Object inputObject = inputFromUser.nextValue();
		
		if (!(inputObject instanceof JSONObject)) {
			throw new IllegalArgumentException("Input is not a JSONObject");
		}
		
		JSONObject createNetworkJSON = (JSONObject) inputObject;
		
		// If the input is a JSONObject, get the command 
		try {
			String command = createNetworkJSON.getString("command");
			
			// If the first command is not "roads", output the corresponding error 
			// and end the program
			if (!(command.equals("roads"))) {
				outputError(createNetworkJSON);
				endConnection();
				throw new IllegalArgumentException("First command must be roads. Ending program");
			}
			
		// If the input does not contain a command, output the corresponding error
		// and end the program
		} catch (JSONException e) {
			outputError(createNetworkJSON);
			endConnection();
			throw new IllegalArgumentException("First command malformed. Ending program");
		}
		
		// If the input is valid, send the command to the server
		sendServerCreateNetwork(createNetworkJSON);
		
	}
	
	/**
	 * Generates a command in the specified format for the server to create the network
	 * and sends the command to the server. Outputs the corresponding error and ends
	 * the program if the command is malformed.
	 * @param userCommand - the complete "roads" command from the user
	 * @throws IllegalArgumentException if the command is malformed
	 */
	private void sendServerCreateNetwork(JSONObject userCommand) {
		JSONObject serverCommand = new JSONObject();
		Set<String> towns = new HashSet<String>();
		JSONArray roads = new JSONArray();
		
		// Get the array "from" and "to" towns from the command
		JSONArray params = userCommand.getJSONArray("params");
		
		// Iterate through all pairs of "from" and "to" towns
		// If the "from" and "to" keys do not exist, the command is malformed
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
			
			// If we have not seen this town in the network before, add it to the set
			if (!towns.contains(fromTown)) {
				towns.add(fromTown);
			}
			if (!towns.contains(toTown)) {
				towns.add(toTown);
			}
		}
		
		// Create the server command and send to the server
		serverCommand.put("towns", new JSONArray(towns));
		serverCommand.put("roads", roads);
		outputToServer.print(serverCommand);
	}
	
	/**
	 * Processes all other user commands once the network is successfully created. 
	 * If the command is not a well-formed request, the corresponding error is 
	 * sent to the user.
	 * @throws IllegalArgumentException if the input from the user is not a JSONObject
	 */
	private void processUserCommands() {
		Object inputObject = inputFromUser.nextValue();
		
		if (!(inputObject instanceof JSONObject)) {
			throw new IllegalArgumentException("Input is not a JSONObject");
		}
		
		// Cast the current command as a JSONObject
		JSONObject commandJSON = (JSONObject) inputObject;
		
		// Identify the command and perform the corresponding operation 
		try {
			String command = commandJSON.getString("command");
			
			if (command.equals("place")) {
				placeCharacter(commandJSON);
			} 
			else if (command.equals("passage-safe?")) {
				passageSafeQuery(commandJSON);
			} 
			// If the command is not "place" or "passage-safe?", output the corresponding
			// error to the user
			else {
				outputError(commandJSON);
			}
		// If the JSONObject does not contain a command, output the corresponding error to
		// the user
		} catch (JSONException e) {
			outputError(commandJSON);
		}		
	}

	/**
	 * Adds all "place" commands for a given batch to the placeCharacterCommands set
	 * @param commandJSON - the JSONObject for the "place" command
	 */
	private void placeCharacter(JSONObject commandJSON) {
		JSONObject newCharacterJSON = new JSONObject();
		JSONObject params = commandJSON.getJSONObject("params");

		// Check that the "character" and "town" keys exist. If so, add the place command 
		// to the set. Otherwise, output an error since the command is malformed
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

	/**
	 * Generates a command in the specified format for the server to check if a 
	 * character can move to a given town. Outputs the corresponding error 
	 * if the command is malformed.
	 * @param commandJSON - the JSONObject for the "passage-safe?" command
	 */
	private void passageSafeQuery(JSONObject commandJSON) {
		JSONObject queryJSON = new JSONObject();
		JSONObject params = commandJSON.getJSONObject("params");

		// Check that "character" and "town" keys exist. If so, create the
		// query JSON and send the server the batch request. Send the server
		// response to the user
		try {
			String character = params.getString("character");
			String destination = params.getString("town");

			queryJSON.put("character", character);
			queryJSON.put("destination", destination);

			sendServerBatchRequest(queryJSON);
			receiveResponse(character, destination);
			
		// Output an error since the command is malformed
		} catch (JSONException e) {
			outputError(commandJSON);
		}
	}

	/**
	 * Sends the current batch of "place" commands and the subsequent "passage-safe?"
	 * query to the server.
	 * @param queryJSON - the "passage-safe?" query
	 */
	private void sendServerBatchRequest(JSONObject queryJSON) {
		JSONObject serverCommand = new JSONObject();

		// Put the command in specified format and send to the server
		serverCommand.put("characters", new JSONArray(placeCharacterCommands));
		serverCommand.put("query", queryJSON);
		outputToServer.print(serverCommand);
	}

	/**
	 * Receives the response from the server, and outputs this response to the user
	 * in the specified format
	 * @param character - the character from the "passage-safe?" query
	 * @param destination - the destination from the "passage-safe?" query
	 */
	private void receiveResponse(String character, String destination) {
		// Get the input from the server
		Object inputObject = inputFromServer.nextValue();
		JSONObject responseJSON = (JSONObject) inputObject;

		// Add all invalid placements to the output for the user
		JSONArray badPlacementJSON = responseJSON.getJSONArray("invalid");
		outputInvalidPlacements(badPlacementJSON);

		// Add the query response to the output for the user
		Boolean responseBool = responseJSON.getBoolean("response");
		outputQueryResponse(character, destination, responseBool);
	}

	/** 
	 * Iterate through all invalid placements sent by the server, and output to
	 * the user in the specified format
	 * @param placements - the JSONArray of invalid character placements
	 */
	private void outputInvalidPlacements(JSONArray placements) {
		for (int i = 0; i < placements.length(); i++) {
			JSONObject placement = placements.getJSONObject(i);
			
			// Create the output message and send to the user
			JSONArray outputMessage = new JSONArray();
			outputMessage.put("invalid placement");
			outputMessage.put(placement);
			System.out.println(outputMessage.toString());
		}
	}

	/**
	 * Output response of the "passage-safe?" query to the user
	 * @param character - the character from the "passage-safe?" query
	 * @param destination - the destination from the "passage-safe?" query
	 * @param response - the response from the server (true/false)
	 */
	private void outputQueryResponse(String character, String destination, Boolean response) {
		JSONObject query = new JSONObject();
		query.put("character", character);
		query.put("destination", destination);

		// // Create the output message and send to the user
		JSONArray outputMessage = new JSONArray();
		outputMessage.put("the response for");
		outputMessage.put(query);
		outputMessage.put("is");
		outputMessage.put(response);

		System.out.println(outputMessage.toString());
	}
	
	/**
	 * Check that the server is alive. The server will shut down the connection if:
	 * - A batch request is ill-formed
	 * - The create request is invalid
	 * - The query in a batch is invalid.
	 * @return true if the server is alive, false if the connection as closed
	 */
	private boolean isServerAlive() {
		return socket.isConnected();		
	}

	/**
	 * Ends the connection with the socket
	 */
	private void endConnection() {
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("Unable to close connection");
			e.printStackTrace();
		}
	}

	/**
	 * If the user enters JSON that does not represent a well-formed request, this method
	 * will output the corresponding error and include the JSON object sent by the user
	 * @param invalidJSON
	 */
	private void outputError(JSONObject invalidJSON) {
		JSONObject outputJSON = new JSONObject();
		outputJSON.put("error", "not a request");
		outputJSON.put("object", invalidJSON);
		System.out.println(outputJSON.toString());
	}	
	
}
