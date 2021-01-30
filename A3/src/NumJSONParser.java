
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.IllegalArgumentException;

//A NumJSON is one of:
//Integer
//String
//Array of NumJSONs
//JSON with NumJSON in "payload" key

//Class to parse NumJSONs.
public class NumJSONParser {

	//Determines whether the given string is an integer value
	//Returns the corresponding boolean
	public static Boolean isNum(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	//Depending on the given operation adds or multiplies all the integers
	//in the potentially nested NumJSON value and returns the total
	//Operations must be "sum" and "product"
	//Value must be a NumJSON
	public static int recursiveOp(Object value, String operation) {
		//identity value for given operation
		int ident;
		if (operation.equals("sum")) {
			ident = 0;
		} else if (operation.equals("product")) {
			ident = 1;
		} else {
			throw new IllegalArgumentException("Operation must be sum or product");
		}

		if (value instanceof Integer) {
			return (Integer)value;
		} else if (value instanceof String) {
			//Strings are treated as the identity value so as to not change the total
			return ident;
		} else if (value instanceof JSONArray) {
			JSONArray jarr = (JSONArray)value;
			int total = ident;
			for (int ii = 0; ii < jarr.length(); ii++) {
				if (operation.equals("sum")) {
					total = total + recursiveOp(jarr.get(ii), operation);
				} else if (operation.equals("product")) {
					total = total * recursiveOp(jarr.get(ii), operation);
				} else {
					throw new IllegalArgumentException("Operation must be sum or product");
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

	//Takes command line input to determine mathematical operation and applies
	//it to the NumJSONs in STDIN.
	//First command line argument must be "--sum" or "--product"
	public static void main(String[] args) {
		
		//Determine the operation specified from the command line
		//Throw an exception if a operation is not provided or
		//if the input does not match a specified operation
		String operation;
		try {
			if (args[0].equals("--sum")) {
				operation = "sum";
			}
			else if (args[0].equals("--product")) {
				operation = "product";
			}
			else {
				throw new IllegalArgumentException("Please enter a valid operation: --sum or --product");
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ArrayIndexOutOfBoundsException("Please enter a valid operation: --sum or --product");
		}

		//Read NumJSONs from STDIN and apply operation to each one.
		//Return as an array of JSONs with the NumJSON value and the total after operation.
		JSONArray outputArray = new JSONArray();
		JSONTokener inputTokens = new JSONTokener(System.in);

		while (true) {
			Object value;
			//nextValue() does not separate integers if there are multiple in a row
			//If first character is not a { or [, parse manually
			Character charCheck = inputTokens.nextClean();
			//If EOF is read, end the loop
			if (inputTokens.end()) {
				break;
			}
			inputTokens.back();
			if (charCheck != 123 && charCheck != 91) {
				String input = inputTokens.nextTo(" \t");
				if (isNum(input)) {
					value = Integer.parseInt(input);
				} else {
					//Strips double quotes
					input = input.replaceAll("^\"|\"$", "");
					value = input;
				}
			} else {
				value = inputTokens.nextValue();
			}

			JSONObject outputJSON = new JSONObject();
			outputJSON.put("total", recursiveOp(value, operation));
			outputJSON.put("object", value);
			outputArray.put(outputJSON);
		}

		System.out.println(outputArray);
	}
	

}
