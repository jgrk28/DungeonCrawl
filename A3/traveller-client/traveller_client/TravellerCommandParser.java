package traveller_client;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class TravellerCommandParser {
	
	private Set<Town> townNetwork;
	private Set<Character> networkCharacters;
	
	public TravellerCommandParser() {
		townNetwork = new HashSet<Town>();
		networkCharacters = new HashSet<Character>();
	}
	
	public static void main(String[] args) {
		TravellerCommandParser parser = new TravellerCommandParser();
		parser.parseCommands();
	}

	private void parseCommands() {		
		
		JSONTokener inputTokens = new JSONTokener(System.in);
		
		Object value;
		value = inputTokens.nextValue();
		
		if (!(value instanceof JSONObject)) {
			throw new IllegalArgumentException("Not in JSON format");
		}
		
		JSONObject JSONCommand = (JSONObject) value;
		String currentCommand = JSONCommand.getString("command");
		
		if (currentCommand.equals("roads")) {
			exeuteRoadsCommand(JSONCommand);
		} else {
			throw new IllegalArgumentException("The first command must create the town network");
		}

		while (!inputTokens.end()) {
			value = inputTokens.nextValue();
			
			if (!(value instanceof JSONObject)) {
				throw new IllegalArgumentException("Not in JSON format");
			}
		
			JSONCommand = (JSONObject) value;
			currentCommand = JSONCommand.getString("command");
			
			if (currentCommand.equals("place")) {
				executePlaceCommand(JSONCommand);	
			} else if (currentCommand.equals("passage-safe?")) {
				executePassageCommand(JSONCommand);
			} else {
				throw new IllegalArgumentException("Invalid command");
			}
		}
	}
	
	private void exeuteRoadsCommand(JSONObject JSONCommand) {
		
		JSONArray params;
		
		try {
			params = JSONCommand.getJSONArray("params");
			for (int i = 0; i < params.length(); i++) {
				JSONObject townConnection = params.getJSONObject(i);
				
				String from = townConnection.getString("from");
				String to = townConnection.getString("to");
				
				Town fromTown;
				Town toTown;

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
				
				try {
					fromTown.connectTowns(toTown);
					toTown.connectTowns(fromTown);	
				} catch (IllegalArgumentException e) {
					System.out.println("Connection skipped, not a simple graph");
				}
			}
			System.out.println("The network was successfully created");
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("Road paramters are not a JSON array");
		}					
	}
	
	private void executePlaceCommand(JSONObject JSONCommand) {
		
		try {
			String characterName = JSONCommand.getString("character");
			String townName = JSONCommand.getString("town");
			Town town;
			Character character;
			
			try {
				 town = getTown(townName);
			} catch (IllegalArgumentException e) {
				System.out.println("The town does not exist, command discarded");
				return;
			}
			
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
			System.out.println("The passage-safe? command was invalid and discarded");
			return;
		}	
	} 
	
	private void executePassageCommand(JSONObject JSONCommand) {
		
		try {
			String characterName = JSONCommand.getString("character");
			String townName = JSONCommand.getString("town");
			Town town;
			Character character;
			
			try {
				 town = getTown(townName);
			} catch (IllegalArgumentException e) {
				System.out.println("The town does not exist, command discarded");
				return;
			}
			
			try {
				character = getCharacter(characterName);
			} catch (IllegalArgumentException e) {
				System.out.println("The character does not exist, command discarded");
				return;
			}
			
			if (character.validateMove(town)) {
				System.out.println("The passage is safe");
			} else {
				System.out.println("The character cannot move to this destination");
			}
			
		} catch (JSONException e) {
			//If the command is invalid, skip it
			System.out.println("The place command was invalid and discarded");
			return;
		}		
	}
	
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
