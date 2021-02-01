package traveller_client;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class travellerCommandParser {

	public static void main(String[] args) {		
		
		JSONTokener inputTokens = new JSONTokener(System.in);
		//Set<Town> townNetwork = new HashSet<Town>();
		
		Object value;
		value = inputTokens.nextValue();
		
		if (!(value instanceof JSONObject)) {
			throw new IllegalArgumentException("Not in JSON format");
		}
		
		JSONObject JSONCommand = (JSONObject) value;
		String currentCommand = JSONCommand.getString("command");
		
		if (currentCommand.equals("roads")) {
			JSONArray params;
			try {
				params = JSONCommand.getJSONArray("params");
				for (int i = 0; i < params.length(); i++) {
					JSONObject townConnection = params.getJSONObject(i);
					String from = townConnection.getString("from");
					String to = townConnection.getString("to");
					
				}
			} catch (ClassCastException e) {
				throw new IllegalArgumentException("Road paramters are not a JSON array");
			}
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
				
			} else if (currentCommand.equals("passage.safe?")) {
				
			} else {
				throw new IllegalArgumentException("Invalid command");
			}
		}
	}

}
