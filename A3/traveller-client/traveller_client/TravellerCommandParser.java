package traveller_client;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * This class parses the commands for creating a town network, placing a character
 * in a town, and verifying if a character can move to another town.
 */
public class TravellerCommandParser {
	
	// A set of all towns in the network
	private Set<Town> townNetwork;
	
	// A set of all characters in the town network
	private Set<Character> networkCharacters;
	
	// Instantiate new HashSets for townNetwork and networkCharacters
	public TravellerCommandParser() {
		townNetwork = new HashSet<Town>();
		networkCharacters = new HashSet<Character>();
	}
	
	/**
	 * Create a new TravellerCommandParser and parse the commands
	 * @param args - arguments from the command line (not applicable for this program)
	 */
	public static void main(String[] args) {
		TravellerCommandParser parser = new TravellerCommandParser();
		parser.parseCommands();
	}

	/**
	 * Parse the commands from STDIN and call the corresponding method for the command
	 * @throws an IllegalArgumentException if the command is not in JSON format, if
	 * the first command does not create the town network, or if the command is invalid
	 */
	private void parseCommands() {		
		
		JSONTokener inputTokens = new JSONTokener(System.in);
		
		Object value;
		value = inputTokens.nextValue();
		
		// If the input is not a JSONObject, end the program
		if (!(value instanceof JSONObject)) {
			throw new IllegalArgumentException("Not in JSON format");
		}
		
		JSONObject JSONCommand = (JSONObject) value;
		String currentCommand = JSONCommand.getString("command");
		
		// If the command is "roads", create the network
		if (currentCommand.equals("roads")) {
			exeuteRoadsCommand(JSONCommand);
		} else {
			throw new IllegalArgumentException("The first command must create the town network");
		}

		// Iterate through all remaining commands
		while (!inputTokens.end()) {
			value = inputTokens.nextValue();
			
			// If the input is not a JSONObject, end the program
			if (!(value instanceof JSONObject)) {
				throw new IllegalArgumentException("Not in JSON format");
			}
		
			JSONCommand = (JSONObject) value;
			currentCommand = JSONCommand.getString("command");
			
			// If the command is "place", execute the command to place the character
			if (currentCommand.equals("place")) {
				executePlaceCommand(JSONCommand);	
			}
			// If the command is "passage-safe?", execute the command to check the path
			else if (currentCommand.equals("passage-safe?")) {
				executePassageCommand(JSONCommand);
			} 
			// Otherwise, the entered command is not valid
			else {
				throw new IllegalArgumentException("Invalid command");
			}
		}
	}
	
	/**
	 * Execute the command to connect the towns specified in the params. If the command 
	 * contains invalid JSON values, those are discarded. 
	 * @param JSONCommand - the JSONCommand that contains the params for connecting towns
	 * @throws IllegalArgumentException if the parameters are not a JSON array
	 */
	private void exeuteRoadsCommand(JSONObject JSONCommand) {
		
		JSONArray params;
		
		try {
			params = JSONCommand.getJSONArray("params");
			
			// Iterate through all parameters to connect each set of towns
			for (int i = 0; i < params.length(); i++) {
				JSONObject townConnection = params.getJSONObject(i);
				
				// Get the town names for the to and from parameters
				String from = townConnection.getString("from");
				String to = townConnection.getString("to");
				
				Town fromTown;
				Town toTown;

				// Get the town from the network if it already exists, otherwise,
				// create a new town and add it to the townNetwork
				try {
					fromTown = getTown(from);
				} catch (IllegalArgumentException e) {
					fromTown = new Town();
					fromTown.createTown(from);
					townNetwork.add(fromTown);
				}
				
				try {
					toTown = getTown(to);
				} catch (IllegalArgumentException e) {
					toTown = new Town();
					toTown.createTown(to);
					townNetwork.add(toTown);
				}
				
				// Connect the towns. If the towns cannot be connected based on the
				// rules of a simple graph, do not connect them
				try {
					fromTown.connectTowns(toTown);
					toTown.connectTowns(fromTown);	
				} catch (IllegalArgumentException e) {
					System.out.println("Connection skipped, not a simple graph");
				}
			}
			System.out.println("The network was successfully created");
		}
		// If the parameters are not in the form of a JSON array, end the program
		catch (ClassCastException e) {
			throw new IllegalArgumentException("Road parameters are not a JSON array");
		}					
	}
	
	/**
	 * Execute the command to place a character in a given town.  If the town does not
	 * exist, the command is invalid and discarded. If the command does not contain the 
	 * town or character params, the command is discarded.
	 * @param JSONCommand - the JSONCommand that contains the params for connecting towns 
	 */
	private void executePlaceCommand(JSONObject JSONCommand) {
		
		try {
			// Get the character name and town name from the command
			String characterName = JSONCommand.getString("character");
			String townName = JSONCommand.getString("town");
			
			Town town;
			Character character;
			
			// Get the town if it exists, otherwise discard the command
			try {
				 town = getTown(townName);
			} catch (IllegalArgumentException e) {
				System.out.println("The town does not exist, command discarded");
				return;
			}
			
			// Get the character if it exists, otherwise create a new character and add it to
			// networkCharacters
			try {
				character = getCharacter(characterName);
			} catch (IllegalArgumentException e) {
				character = new Character();
				character.createCharacter(characterName);
				networkCharacters.add(character);
			}
			
			character.placeCharacter(town);
			
			System.out.println("The character was successfully placed");
			
		} catch (JSONException e) {
			//If the command is invalid, skip it
			System.out.println("The place command was invalid and discarded");
			return;
		}	
	} 
	
	/**
	 * Execute the command to check that a given character has safe passage to the given
	 * town. If the town or character does not exist, the command is discarded. If the 
	 * command does not contain the town or character params, the command is discarded.
	 * @param JSONCommand - the JSONCommand that contains the params for connecting towns
	 */
	private void executePassageCommand(JSONObject JSONCommand) {
		
		try {
			// Get the character name and town name from the command
			String characterName = JSONCommand.getString("character");
			String townName = JSONCommand.getString("town");
			
			Town town;
			Character character;
			
			// Get the town if it exists, otherwise discard the command
			try {
				 town = getTown(townName);
			} catch (IllegalArgumentException e) {
				System.out.println("The town does not exist, command discarded");
				return;
			}
			
			// Get the character if it exists, otherwise discard the command
			try {
				character = getCharacter(characterName);
			} catch (IllegalArgumentException e) {
				System.out.println("The character does not exist, command discarded");
				return;
			}
			
			// Check if the move is valid
			if (character.validateMove(town)) {
				System.out.println("The passage is safe");
			} else {
				System.out.println("The character cannot move to this destination");
			}
			
		} catch (JSONException e) {
			//If the command is invalid, skip it
			System.out.println("The passage-safe? command was invalid and discarded");
			return;
		}		
	}
	
	/**
	 * Get the town associated with the given town name
	 * @param townName - the name of the town
	 * @return the town that matches the given town name
	 * @throws IllegalArgumentException if the town does not exist
	 */
	private Town getTown(String townName) {
		for (Town town : townNetwork) {
			//We will need to add a new method to check that the names of the 
			//towns are the same
			if (town.checkTownName(townName)) {
				return town;
			}
		}
		throw new IllegalArgumentException("The town does not exist");
	}
	
	/**
	 * Get the character associated with the given character name
	 * @param characterName - the name of the character
	 * @return the character that matches the given character name
	 * @throws IllegalArgumentException if the character does not exist
	 */
	private Character getCharacter(String characterName) {
		for (Character c : networkCharacters) {
			//We will need to add a new method to check that the names of the 
			//characters are the same
			if (c.checkCharacterName(characterName)) {
				return c;
			}
		}
		throw new IllegalArgumentException("The character does not exist");
	}

}
