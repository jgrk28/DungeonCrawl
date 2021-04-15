import Game.controller.GameManager;
import Game.model.Level;
import Observer.LocalObserver;
import Remote.Client;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class SnarlClient {
  /**
   * TODO fix this
   * Parses the command line arguments to create a game of Snarl. Registers
   * players and adversaries, creates the corresponding levels, and allows
   * one LocalPlayer to play the game
   * @param args - the command line arguments
   * @throws FileNotFoundException if the file containing JSON level specifications
   * cannot be found or opened
   */
  public static void main(String[] args) throws FileNotFoundException {
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

    Client client = new Client(ipAddress, port);
    client.run();
  }
}
