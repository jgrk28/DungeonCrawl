package State;

import static Utils.ParseUtils.parsePoint;

import Level.TestLevel;
import java.awt.Point;
import java.util.List;
import java.util.Map;
import model.Adversary;
import model.Dungeon;
import model.Exit;
import model.Key;
import model.Level;
import model.LevelComponent;
import model.LevelImpl;
import model.Player;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class TestState {
  private Point point;
  private String name;
  private Level level;

  /**
   * Parses the input to create a GameState.
   * Identifies if the given player can be moved to the given position.
   * @param args - command line arguments
   */
  public static void main(String[] args) {
    TestState stateParser = new TestState();
    stateParser.parseInput();
    List<Point> validMoves = roomParser.getValidMoves();
    roomParser.outputMoves(validMoves);
  }

  /**
   * Parses the JSON input from STDIN. Creates the GameState based on these specifications
   * and gets the player and position needed
   */
  private void parseInput() {
    JSONTokener inputTokens = new JSONTokener(System.in);

    Object value;
    value = inputTokens.nextValue();

    // If the input is not a JSONArray, end the program
    if (!(value instanceof JSONObject)) {
      throw new IllegalArgumentException("Not in valid JSON format");
    }

    JSONArray JSONInput = (JSONArray) value;
    JSONObject JSONState = JSONInput.getJSONObject(0);
    String name = JSONInput.getString(1);
    JSONArray JSONPoint = JSONInput.getJSONArray(2);

    //The point that the player wants to move to
    this.point = parsePoint(JSONPoint);

    //Parse the JSON for the room
    JSONObject JSONLevel = JSONState.getJSONObject("level");
    List<LevelComponent> levelMap = TestLevel.parseLevelMap(JSONLevel);
    Key key = TestLevel.parseKey(JSONLevel);
    Exit exit = TestLevel.parseExit(JSONLevel);

    JSONArray JSONPlayers = JSONState.getJSONArray("players");
    Map<Player, Point> players = parsePlayers(JSONPlayers);
    JSONArray JSONAdversaries = JSONState.getJSONArray("adversaries");
    Map<Adversary, Point> adversaries = parsePlayers(JSONAdversaries);

    Boolean exitLocked = JSONState.getBoolean("exit-locked");

    this.level = new LevelImpl(players, adversaries, levelMap, !exitLocked, false, key, exit);
  }
}
