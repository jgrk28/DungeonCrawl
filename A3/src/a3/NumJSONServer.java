package a3;

import java.net.*;
import java.io.*;

import a2.*;

//Class to act as a server and parse NumJSONs being sent to it
public class NumJSONServer {

	protected int port;
	private BufferedReader input;
	private PrintStream output;

	//Initialize a server with the given port
	public NumJSONServer(int port) {
		if (port < 0 || port > 65535) {
			throw new IllegalArgumentException("Illegal port number");
		}
		this.port = port;
	}

	//Needed to be able to override for a mock
	public Socket createSocket() throws IOException{
		ServerSocket server = new ServerSocket(this.port);
		return server.accept();
	}

	//Links the input and output of the given socket to class fields
	public void linkSocketIO(Socket socket) {
		try {
			this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.output = new PrintStream(socket.getOutputStream());
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	//Accept NumJSONs from client until "END" is sent and perform calculation
	public void parseClientInput() {
		//String builder to create input string from users input stream
		StringBuilder numJSONParserInput = new StringBuilder();

		//Read each incoming line until "END" is read
		while (true) {
			try {
				String inputLine = this.input.readLine();
				//Unexpected for socket stream ran into an EOF before "END"
				if (inputLine == null) {
					System.out.println("Socket stream closed before END sent");
					break;
				}
				if (inputLine.contains("END")) {
					//If "END" is in this line get the info before it and break the loop
					String[] splitInput = inputLine.split("END");
					try {
						numJSONParserInput.append(splitInput[0]);
					} catch (ArrayIndexOutOfBoundsException e) {
						//skip if the only value is END
					}
					break;
				} else {
					//If "END" is not in this line add to input builder and continue reading
					numJSONParserInput.append(inputLine);	
					numJSONParserInput.append("\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		computeClientOutput(numJSONParserInput.toString());
	}

	//Computes the sum of the input String of NumJSONs using NumJSONParser
	//Need to connect STDIN and STDOUT to work with the Parser API
	public void computeClientOutput(String clientInput) {
		//Parser gets input from STDIN
		System.setIn(new ByteArrayInputStream(clientInput.getBytes()));
		//Pipe return into the socket for the client
		System.setOut(this.output);
		//Always summing for this parser
		NumJSONParser.main(new String[] {"--sum"});
		
	}

	//Closes the socket after use.
	public void close(Socket socket) throws IOException {
		socket.close();
	}

	public static void main(String[] args) {
		//Create a server and then parse the input coming to that server
		NumJSONServer JSONServer = new NumJSONServer(8000);
		try {
			Socket socket = JSONServer.createSocket();
			JSONServer.linkSocketIO(socket);
			JSONServer.parseClientInput();
			JSONServer.close(socket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
