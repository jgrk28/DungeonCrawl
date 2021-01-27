package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONObject;
import org.json.JSONTokener;

public class NumJSONParser {

	public static void main(String[] args) {
		
		BufferedReader inputStream = new BufferedReader(new InputStreamReader(System.in));
		String inputLine;
		
		try {
			while ((inputLine = inputStream.readLine()) != null) {
				
				System.out.println(inputLine);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//if a curly brace, mark position and go down the line until we find the next curly brace. Process with JSONTokener
		//if a bracket or singular value, send values to corresponding operation
		
		/*JSONTokener inputTokener = new JSONTokener(System.in);
		JSONObject inputJSON = new JSONObject(inputTokener);
		
		for (String key : inputJSON.keySet()) {
			System.out.println(key);
		}*/
		
	}
	

}
