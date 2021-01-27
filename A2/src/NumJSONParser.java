package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.IllegalArgumentException;

public class NumJSONParser {

	//Returns the number of characters until the starting "open character" is closed by
	//its corresponding "close character"
	public static Integer findLength(BufferedReader input, int openCode, int closeCode) {
		//Always start with one brace
		int count = 1;
		int openBraces = 1;

		try {
			//Skipping the original brace
			input.skip(1);
			//If there are no unclosed braces remaining end loop
			while (openBraces > 0) {
				int currChar = input.read();
				count++;
				if (currChar == openCode) {
					//open another brace
					openBraces++;
				} else if (currChar == closeCode) {
					//close latest brace
					openBraces--;
				}
			}
			input.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}

	//Breaks series of NumJSONs (as strings) into a list to iterate over
	public static ArrayList<String> parseInput(BufferedReader input) {
		ArrayList<String> output = new ArrayList<>();
		try {
			input.mark(1024);
			int currChar = 0;
			//Read first char and reset cursor to marker
			//Once char is -1 or EOF end loop
			while((currChar = input.read()) != -1) {
				input.reset();
				if (currChar == 32) {
					//If space skip
					input.skip(1);
					input.mark(1024);
				} else if (currChar == 123) {
					//If curly bracket read to end of curly bracket
					int length = findLength(input, 123, 125);
					char[] buffer = new char[length];
					input.read(buffer, 0, length);
					input.mark(1024);
					output.add(new String(buffer));
				} else if (currChar == 91) {
					//If square bracket read to end of square bracket
					int length = findLength(input, 91, 93);
					char[] buffer = new char[length];
					input.read(buffer, 0, length);
					input.mark(1024);
					output.add(new String(buffer));
				} else {
					//Else read until next space or EOF
					int length = 0;
					while ((currChar = input.read()) != -1 && currChar != 32) {
						length++;
					}
					input.reset();

					char[] buffer = new char[length];
					input.read(buffer, 0, length);
					input.mark(1024);
					output.add(new String(buffer));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return output;
	}
	
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
	
	//Creates and returns a JSON object for a key-value pair
	public static JSONObject appendToJSON(String object, Integer total) {
		JSONObject outputJSON = new JSONObject();
		outputJSON.put("total", total);
		outputJSON.put("object", object);
		return outputJSON;
	}
	
	//Iterates over a given array and performs the corresponding operation
	//Returns the total result for that given operation 
	public static Integer processArray(String s, String operation) {
		Integer total = 0;
		
		//Splits the string on spaces, commas, and square brackets
		String[] values = s.split("\\s*(\\s|,|\\]|\\[)\\s*");
		if (operation.equals("sum")) {
			for (String v: values) {
				if (isNum(v)) {
					total = sum(total, Integer.parseInt(v));
				}
			}
		}
		if (operation.equals("product")) {
			total = 1;
			for (String v: values) {
				if (isNum(v)) {
					total = sum(total, Integer.parseInt(v));
				}
			}
		}
		return total;
	}
	
	//Returns the sum of a NumJSON
	public static Integer sum(Integer total, Integer num) {
		return total + num;
	}
	
	//Returns the product of a NumJSON
	public static Integer product(Integer total, Integer num) {
		return total * num;
	}

	public static void main(String[] args) {
		String operation;
		
		//Determine the operation specified from the command line
		//Throw the corresponding exception if a operation is not provided
		//Or if the input does not match a specified operation
		try {
			operation = args[0];
			
			if (args[0].equals("--sum")) {
				operation = "sum";
				System.out.println("The operation is sum");
			}
			else if (args[0].equals("--product")) {
				operation = "product";
				System.out.println("The operation is product");
			}
			else {
				throw new IllegalArgumentException("Please enter a valid operation: --sum or --product");
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ArrayIndexOutOfBoundsException("Please enter a valid operation: --sum or --product");
		}
		
		
		BufferedReader inputStream = new BufferedReader(new InputStreamReader(System.in));
		ArrayList<String> chunks = parseInput(inputStream);
		
		JSONArray output = new JSONArray();

		for (String s : chunks) {
			//If the value in the array list is an array of NumJSON, process the array and add it to the output
			if (s.contains("[") || s.contains("]")) {
				output.put(appendToJSON(s, processArray(s, operation)));
			}
			//If the value in the array list is an Object, process the object and add it to the output
			else if (s.contains("{") || s.contains("}")) {
				//output.put(appendToJSON(s, processObject(s, operation)));
			}
			//If the value is a singular number or a string, add it to the output with the corresponding total
			else {
				if (isNum(s)) {
					output.put(appendToJSON(s, Integer.parseInt(s)));
					
				} else {
					output.put(appendToJSON(s, 0));
				}
			}
		}
		
		System.out.println(output);

		//System.out.println(chunks);

		/*JSONTokener inputTokener = new JSONTokener(System.in);
		JSONObject inputJSON = new JSONObject(inputTokener);
		
		for (String key : inputJSON.keySet()) {
			System.out.println(key);
		}*/
		
	}
	

}
