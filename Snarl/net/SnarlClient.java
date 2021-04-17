import Remote.Client;

/**
 * Starts the player client with the provided command line arguments
 */
public class SnarlClient {

  /**
   * Parses the provided command line arguments, creates a player client,
   * and starts the player client 
   * @param args - command line arguments 
   */
  public static void main(String[] args){
    //Set the defaults for the game
    String ipAddress = "127.0.0.1";
    int port = 45678;

    for (int i = 0; i < args.length; i++) {
      switch (args[i]) {
        case "--address":
          ipAddress = args[i + 1];
          i++;
          break;
        case "--port":
          port = Integer.parseInt(args[i + 1]);
          i++;
          break;
        default:
          throw new IllegalArgumentException("Invalid input: " + args[i]);
      }
    }

    //Create and run the client
    Client client = new Client(ipAddress, port);
    client.run();
  }
}
