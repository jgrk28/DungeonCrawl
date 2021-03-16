package State;

import static Utils.ParseUtils.parsePoint;

import Level.TestLevel;
import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Game.model.Adversary;
import Game.model.Exit;
import Game.model.Ghost;
import Game.model.InteractionResult;
import Game.model.Key;
import Game.model.Level;
import Game.model.LevelComponent;
import Game.model.LevelImpl;
import Game.model.Player;
import Game.model.Zombie;
import Game.modelView.EntityType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class TestState {
  private Point point;
  private String name;
  private Level level;
  private JSONObject JSONState;

  /**
   * Parses the input to create a GameState.
   * Identifies if the given player can be moved to the given position.
   * @param args - command line arguments
   */
  public static void main(String[] args) {
    TestState stateParser = new TestState();
    stateParser.parseInput();
    try {
      InteractionResult moveResult = stateParser.attemptMove();
      stateParser.outputMoveResult(moveResult);
    } catch (IllegalArgumentException e) {
      stateParser.outputMoveError(e.getMessage());
    }
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
    if (!(value instanceof JSONArray)) {
      throw new IllegalArgumentException("Not in valid JSON format");
    }

    JSONArray JSONInput = (JSONArray) value;
    this.JSONState = JSONInput.getJSONObject(0);
    this.name = JSONInput.getString(1);
    JSONArray JSONPoint = JSONInput.getJSONArray(2);

    //The point that the player wants to move to
    this.point = parsePoint(JSONPoint);
    //The initial state of the GameState which currently is only one Level
    this.level = parseState(this.JSONState);
  }

  /**
   * Parses a JSON object. Creates the GameState based on the JSON. Currently the state is
   * only one level so it simply returns a level.
   */
  private Level parseState(JSONObject JSONState) {
    JSONObject JSONLevel = JSONState.getJSONObject("level");
    List<LevelComponent> levelMap = TestLevel.parseLevelMap(JSONLevel);
    Key key = TestLevel.parseKey(JSONLevel);
    Exit exit = TestLevel.parseExit(JSONLevel);

    JSONArray JSONPlayers = JSONState.getJSONArray("players");
    Map<Player, Point> players = parsePlayers(JSONPlayers);
    JSONArray JSONAdversaries = JSONState.getJSONArray("adversaries");
    Map<Adversary, Point> adversaries = parseAdversaries(JSONAdversaries);

    Boolean exitLocked = JSONState.getBoolean("exit-locked");

    return new LevelImpl(players, adversaries, levelMap, !exitLocked, false, key, exit);
  }

  /**
   * Parses a JSON array that contains information about players. Creates a map of players to
   * their respective positions.
   */
  private Map<Player, Point> parsePlayers(JSONArray JSONPlayers) {
    Map<Player, Point> players = new HashMap<>();
    for (int i = 0; i < JSONPlayers.length(); i++) {
      JSONObject JSONActorPosition = JSONPlayers.getJSONObject(i);
      String ActorType = JSONActorPosition.getString("type");
      if (!ActorType.equals("player")) {
        throw new IllegalArgumentException("Non-player in list of players");
      }
      String name = JSONActorPosition.getString("name");
      Player player = new Player(name);

      JSONArray JSONPoint = JSONActorPosition.getJSONArray("position");
      Point position = parsePoint(JSONPoint);

      players.put(player, position);
    }
    return players;
  }

  /**
   * Parses a JSON array that contains information about adversaries. Creates a map of adversaries
   * to their respective positions.
   */
  private Map<Adversary, Point> parseAdversaries(JSONArray JSONAdversaries) {
    Map<Adversary, Point> adversaries = new HashMap<>();
    for (int i = 0; i < JSONAdversaries.length(); i++) {
      JSONObject JSONActorPosition = JSONAdversaries.getJSONObject(i);
      String name = JSONActorPosition.getString("name");

      String ActorType = JSONActorPosition.getString("type");
      Adversary adversary;
      if (ActorType.equals("ghost")) {
        adversary = new Ghost(name);
      } else if (ActorType.equals("zombie")) {
        adversary = new Zombie(name);
      } else {
        throw new IllegalArgumentException("Non-adversary in list of adversaries");
      }

      JSONArray JSONPoint = JSONActorPosition.getJSONArray("position");
      Point position = parsePoint(JSONPoint);

      adversaries.put(adversary, position);
    }
    return adversaries;
  }

  /**
   * Try to move this states player in its level to its point.
   * @return The interaction result of the move
   * @throws IllegalArgumentException No Player if the player is not in the level and Bad
   * Destination if the point is not a traversable tile.
   */
  private InteractionResult attemptMove() {
    Player player;
    try {
      player = this.level.getPlayer(this.name);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("No Player");
    }
    LevelComponent destComponent = this.level.findComponent(this.point);
    Entity destEntity = destComponent.getDestinationEntity(this.point);
    EntityType destEntType = destComponent.getEntityType(destEntity);
    if (!player.isTraversable(destEntType)) {
      throw new IllegalArgumentException("Bad Destination");
    }

    return this.level.playerAction(player, this.point);
  }

  /**
   * Outputs the relevant error if the move could not be completed
   * @param error The error that was returned when trying to move
   */
  private void outputMoveError(String error) {
    JSONArray outputArray = new JSONArray();
    outputArray.put("Failure");

    if (error.equals("No Player")) {
      outputArray.put("Player ");
      outputArray.put(this.name);
      outputArray.put(" is not a part of the game.");
    } else if (error.equals("Bad Destination")) {
      JSONArray JSONPoint = new JSONArray();
      JSONPoint.put(this.point.y);
      JSONPoint.put(this.point.x);

      outputArray.put("The destination position ");
      outputArray.put(JSONPoint);
      outputArray.put(" is invalid.");
    } else {
      outputArray.put("Unknown error: ");
      outputArray.put(error);
    }

    System.out.print(outputArray.toString());
  }

  /**
   * Outputs the relevant information about the move that was performed
   * @param moveResult The interaction result from the move that was performed
   *                   {
   *   "type": "state",
   *   "level": (level),
   *   "players": (actor-position-list),
   *   "adversaries": (actor-position-list),
   *   "exit-locked": (boolean)
   * }
   */
  private void outputMoveResult(InteractionResult moveResult) {
    JSONArray outputArray = new JSONArray();
    outputArray.put("Success");

    switch (moveResult) {
      case NONE:
        updateStatePlayerMove();
        break;
      case FOUND_KEY:
        updateStateKeyFound();
        updateStatePlayerMove();
        break;
      case EXIT:
        updateStatePlayerRemove();
        outputArray.put("Player ");
        outputArray.put(this.name);
        outputArray.put(" exited.");
        break;
      case REMOVE_PLAYER:
        updateStatePlayerRemove();
        outputArray.put("Player ");
        outputArray.put(this.name);
        outputArray.put(" was ejected.");
        break;
      default:
        outputArray.put("Unknown interaction result: ");
        outputArray.put(moveResult);
    }
    outputArray.put(this.JSONState);

    System.out.print(outputArray.toString());
  }

  private void updateStateKeyFound() {
    this.JSONState.put("exit-locked", false);
    JSONObject JSONLevel = this.JSONState.getJSONObject("level");
    JSONArray JSONObjects = JSONLevel.getJSONArray("objects");
    for (int i = 0; i < JSONObjects.length(); i++) {
      JSONObject JSONObj = JSONObjects.getJSONObject(i);
      if (JSONObj.getString("type").equals("key")) {
        Point keyLocation = parsePoint(JSONObj.getJSONArray("position"));
        if (keyLocation.equals(this.point)) {
          JSONObjects.remove(i);
          break;
        }
      }
    }
    JSONLevel.put("objects", JSONObjects);
    this.JSONState.put("level", JSONLevel);
  }

  private void updateStatePlayerMove() {
    JSONArray JSONPlayers = this.JSONState.getJSONArray("players");
    for (int i = 0; i < JSONPlayers.length(); i++) {
      JSONObject JSONPlayer = JSONPlayers.getJSONObject(i);
      if (JSONPlayer.getString("name").equals(this.name)) {
        JSONArray JSONPoint = new JSONArray();
        JSONPoint.put(this.point.y);
        JSONPoint.put(this.point.x);
        JSONPlayer.put("position", JSONPoint);
        JSONPlayers.put(i, JSONPlayer);
        break;
      }
    }
  }

  private void updateStatePlayerRemove() {
    JSONArray JSONPlayers = this.JSONState.getJSONArray("players");
    for (int i = 0; i < JSONPlayers.length(); i++) {
      JSONObject JSONPlayer = JSONPlayers.getJSONObject(i);
      if (JSONPlayer.getString("name").equals(this.name)) {
        JSONPlayers.remove(i);
        break;
      }
    }
  }
}
