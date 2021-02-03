package a4;

import java.io.IOException;
import java.net.Socket;
import java.util.Set;

import org.json.*;

public class TCPTravellerClient {
	
	private String ipAddress;
	private int port;
	private String username;
	private Socket socket;
	private Set<JSONObject> placeCharacterCommands;
	
	public TCPTravellerClient() {
		
	}

	public static void main(String[] args) {
		TCPTravellerClient client = new TCPTravellerClient();
		client.parseCommandLine(args);
		client.initializeConnection();	
	}
	
	/**
	 * 
	 * @param args
	 * @throws NumberFormatException
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
	 * 
	 * @throws IOException
	 */
	private void initializeConnection() throws IOException {
		socket = new Socket(ipAddress, port);
		
		//TCP connect
		//Send sign-up name
		//Receive session ID
		
	}
	
	private void createNetwork() {
		
	}
	
	private void placeCharacter() {
		//add to placeCharacterCommands
	}
	
	private void passageSafeQuery() {
		//Send batch of commands
	}
	
	private void receiveResponse() {
		
	}
	
	private void endConnection() {
		
	}
	
}
