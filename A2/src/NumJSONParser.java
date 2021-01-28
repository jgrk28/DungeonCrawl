package src;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.IllegalArgumentException;
import org.json.JSONTokener;

public class NumJSONParser {

	public static int recursiveOp(Object value, String operation) {
		int ident;
		if (operation.equals("sum")) {
			ident = 0;
		} else {
			ident = 1;
		}
		if (value instanceof Integer) {
			return (Integer)value;
		} else if (value instanceof String) {
			return ident;
		} else if (value instanceof JSONArray) {
			JSONArray jarr = (JSONArray)value;
			int total = ident;
			for (int ii = 0; ii < jarr.length(); ii++) {
				if (operation.equals("sum")) {
					total = total + recursiveOp(jarr.get(ii), operation);
				} else {
					total = total * recursiveOp(jarr.get(ii), operation);
				}
			}
			return total;
		} else if (value instanceof JSONObject) {
			JSONObject jobj = (JSONObject)value;
			return recursiveOp(jobj.get("payload"), operation);
		} else {
			throw new IllegalArgumentException("Input must be only integers, strings, arrays, and json objects");
		}
	}

	public static void main(String[] args) {
		String operation;
		
		//Determine the operation specified from the command line
		//Throw the corresponding exception if a operation is not provided or 
		// if the input does not match a specified operation
		try {
			if (args[0].equals("--sum")) {
				operation = "sum";
				//System.out.println("The operation is sum");
			}
			else if (args[0].equals("--product")) {
				operation = "product";
				//System.out.println("The operation is product");
			}
			else {
				throw new IllegalArgumentException("Please enter a valid operation: --sum or --product");
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ArrayIndexOutOfBoundsException("Please enter a valid operation: --sum or --product");
		}

		JSONArray outputArray = new JSONArray();
		JSONTokener inputTokens = new JSONTokener(System.in);
		try {
			while (true) {
				Object value = inputTokens.nextValue();
				JSONObject outputJSON = new JSONObject();
				outputJSON.put("total", recursiveOp(value, operation));
				outputJSON.put("object", value);
				outputArray.put(outputJSON);
			}
		} catch (org.json.JSONException e) {

		}

		System.out.println(outputArray);
	}
	

}
