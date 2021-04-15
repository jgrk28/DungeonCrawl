import Game.controller.GameManager;
import Game.model.Item;
import Game.model.Level;
import Game.model.LevelComponent;
import Game.model.LevelImpl;
import Level.TestLevel;
import Observer.LocalObserver;
import Remote.Server;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONTokener;

public class SnarlServer {
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
    String fileName = "snarl.levels";
    int numClients = 4;
    int wait = 60;
    Boolean observer = false;
    String ipAddress = "127.0.0.1";
    int port = 45678;

    for (int i = 0; i < args.length; i++) {
      switch (args[i]) {
        case "--levels":
          fileName = args[i + 1];
          i++;
          break;
        case "--clients":
          numClients = Integer.parseInt(args[i + 1]);
          i++;

          if (numClients < 1 || numClients > 4) {
            System.out.println("Invalid number of players. Ending game.");
            return;
          }

          break;
        case "--wait":
          wait = Integer.parseInt(args[i + 1]);
          i++;
          break;
        case "--observe":
          observer = true;
          break;
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

    Server server = new Server(ipAddress, port);

    SnarlServer snarlServer = new SnarlServer();
    List<Level> levels = snarlServer.generateLevels(fileName);

    server.run(levels, numClients, wait, observer);
  }

  /**
   * Generates all levels contained within the level specifications
   * @param fileName - the name of the file containing the level specifications
   * @return the list of levels generated based on this specification
   * @throws FileNotFoundException if the file containing JSON level specifications
   * cannot be found or opened
   */
  private List<Level> generateLevels(String fileName) throws FileNotFoundException {
    List<Level> levels = new ArrayList<>();
    JSONTokener inputTokens = new JSONTokener(new FileInputStream(fileName));
    Object value = inputTokens.nextValue();
    int numLevels = (int)value;

    //Get the JSON Object for each level in the file. Parse the levelMap and items.
    //Create the corresponding level and add it to the list
    for (int i = 0; i < numLevels; i++) {
      JSONObject JSONLevel = (JSONObject)inputTokens.nextValue();
      List<LevelComponent> levelMap = TestLevel.parseLevelMap(JSONLevel);
      List<Item> items = TestLevel.parseObjects(JSONLevel);

      Level level = new LevelImpl(levelMap, items);
      levels.add(level);
    }
    return levels;
  }
}
