package a3;

import java.net.*;
import java.io.*;

import a2.*;

public class NumJSONServer {
	
	private ServerSocket server;
	private Socket socket;
	private BufferedReader input;
	private PrintStream output;
	
	public NumJSONServer(int port) {
	
		try {
			server = new ServerSocket(port);		
			socket = server.accept();
			
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintStream(socket.getOutputStream());
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void parseClientInput() {
		
		StringBuilder numJSONParserInput = new StringBuilder();
		
		while (true) {
			try {
				String inputLine = input.readLine();
				System.out.println(inputLine);
				if (inputLine.contains("END")) {
					String[] splitInput = inputLine.split("END");
					try {
						numJSONParserInput.append(splitInput[0]);
					} catch (ArrayIndexOutOfBoundsException e) {
						//skip if the only value is END
					}
					break;
				} else {
					numJSONParserInput.append(inputLine);	
					numJSONParserInput.append("\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(numJSONParserInput.toString());
		computeClientOutput(numJSONParserInput.toString());	
	}
	
	public void computeClientOutput(String clientInput) {
		
		System.setIn(new ByteArrayInputStream(clientInput.getBytes()));
		
		System.setOut(output);
		
		NumJSONParser.main(new String[] {"--sum"});
		
	}

	public static void main(String[] args) {
		NumJSONServer JSONServer = new NumJSONServer(8000);
		JSONServer.parseClientInput();
	}

}
