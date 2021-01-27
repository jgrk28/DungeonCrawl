package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONTokener;

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
			//Read first char and reset curser to marker
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

	public static void main(String[] args) {
		
		BufferedReader inputStream = new BufferedReader(new InputStreamReader(System.in));
		ArrayList<String> chunks = parseInput(inputStream);

		System.out.println(chunks);
		
		//if a curly brace, mark position and go down the line until we find the next curly brace. Process with JSONTokener
		//if a bracket or singular value, send values to corresponding operation
		
		/*JSONTokener inputTokener = new JSONTokener(System.in);
		JSONObject inputJSON = new JSONObject(inputTokener);
		
		for (String key : inputJSON.keySet()) {
			System.out.println(key);
		}*/
		
	}
	

}
