package Remote;

import static Utils.ParseUtils.parsePoint;

import Game.model.Item;
import Level.TestLevel;
import Player.LocalPlayer;
import java.awt.Point;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Client {
  private LocalPlayer player;
  private Socket socket;

  private JSONTokener inputFromServer;
  private PrintStream outputToServer;

  public Client(String ipAddress, int port) throws IOException {
    this.player = new LocalPlayer();
    this.socket = new Socket(ipAddress, port);
    this.inputFromServer = new JSONTokener(this.socket.getInputStream());
    this.outputToServer = new PrintStream(this.socket.getOutputStream());
  }

  //TODO implement rest of functions in if elses
  public void run() {
    while (socket.isConnected()) {
      Object value = inputFromServer.nextValue();
      if (isJSONCommand(value, "welcome")) {
        welcomePlayer((JSONObject) value);
      } else if (isJSONString(value, "name")) {
        getPlayerName();
      } else if (isJSONCommand(value, "start-level")) {
        startLevel((JSONObject) value);
      } else if (isJSONCommand(value, "player-update")) {
        updatePlayer((JSONObject) value);
      } else if (isJSONString(value, "move")) {

      } else if (isResult(value)) {

      } else if (isJSONCommand(value, "end-level")) {

      } else if (isJSONCommand(value, "end-game")) {

      }
    }
  }

  private void welcomePlayer(JSONObject json) {
    JSONObject serverInfo = json.getJSONObject("info");
    String ipAddress = serverInfo.getString("ip-address");
    int port = serverInfo.getInt("port");
    this.player.displayMessage("Connected to server at ip: " + ipAddress + " port: " + port);
  }

  private void getPlayerName() {
    Scanner in = new Scanner(System.in);
    System.out.println("Enter player name:");
    String name = in.next();
    this.outputToServer.println(name);
  }

  private void startLevel(JSONObject json) {
    int levelIndex = json.getInt("level");
    JSONArray nameList = json.getJSONArray("nameList");
    Set<String> names = new HashSet<String>();
    for (int i = 0; i < nameList.length(); i++) {
      names.add(nameList.getString(i));
    }
    this.player.sendLevelStart(levelIndex, names);
  }

  private void updatePlayer(JSONObject json) {
    String message = json.getString("message");
    JSONArray layout = json.getJSONArray("layout");
    JSONArray JSONPosition = json.getJSONArray("position");
    Point position = Utils.ParseUtils.parsePoint(JSONPosition);
    List<Item> items = TestLevel.parseObjects(json);
    JSONArray JSONActors = json.getJSONArray("actors");
    String renderedMap = renderMap(layout, position, items, JSONActors);

    this.player.updatePosition(position);
    this.player.displayMessage(renderedMap);
    this.player.displayMessage(message);
  }

  private static boolean isJSONCommand(Object value, String command) {
    if (!(value instanceof JSONObject)) {
      return false;
    }
    return getCommandType((JSONObject) value).equals(command);
  }

  private static boolean isJSONString(Object value, String string) {
    if (!(value instanceof String)) {
      return false;
    }
    return ((String) value).equals(string);
  }

  private static boolean isResult(Object value) {
    if (!(value instanceof String)) {
      return false;
    }
    Set<String> validResults = new HashSet<>();
    validResults.addAll(Arrays.asList("OK", "Key", "Exit", "Eject", "Invalid"));
    return validResults.contains((String) value);
  }

  private static String getCommandType(JSONObject json) {
    return json.getString("type");
  }

  //TODO finish rendering map, make sure to increment rowIndex colIndex
  private String renderMap(JSONArray JSONLayout, Point position, List<Item> items,
      JSONArray JSONActors) {
    Map<Point, Character> actorPositionMap = parseActorArray(JSONActors);
    List<List<Integer>> layout = generateLayout(JSONLayout);
    int layoutHalfLength = layout.size() / 2;
    Point topLeft = new Point(position.x - layoutHalfLength, position.y - layoutHalfLength);

    StringBuilder mapBuilder = new StringBuilder();
    int rowIndex = topLeft.y;
    int colIndex = topLeft.x;
    for (List<Integer> row : layout) {
      for (Integer tile : row) {
        if (tile == 0) {
          mapBuilder.append('X');
        } else {
          Point currPos = new Point(rowIndex, colIndex);
          if (actorPositionMap.containsKey(currPos)) {

          }
        }
      }
    }

  }

  private Map<Point, Character> parseActorArray(JSONArray JSONActors) {
    Map<Point, Character> actorPositionMap = new HashMap<>();
    for (int i = 0; i < JSONActors.length(); i++) {
      JSONObject JSONActorPosition = JSONActors.getJSONObject(i);
      String actorType = JSONActorPosition.getString("type");
      Character charType = actorTypeToCharacter(actorType);

      JSONArray JSONPoint = JSONActorPosition.getJSONArray("position");
      Point position = parsePoint(JSONPoint);

      actorPositionMap.put(position, charType);
    }
    return actorPositionMap;
  }

  private Character actorTypeToCharacter(String actorType) {
    switch (actorType) {
      case "player":
        return 'P';
      case "zombie":
        return 'Z';
      case "ghost":
        return 'G';
      default:
        throw new IllegalArgumentException("Invalid actor type");
    }
  }

  private List<List<Integer>> generateLayout(JSONArray JSONLayout) {
    List<List<Integer>> layout = new ArrayList<>();
    for (int row = 0; row < JSONLayout.length(); row++) {
      List<Integer> layoutRow = new ArrayList<>();
      JSONArray JSONRow = JSONLayout.getJSONArray(row);
      for (int col = 0; col < JSONRow.length(); col++) {
        layoutRow.add(JSONRow.getInt(col));
      }
      layout.add(layoutRow);
    }
    return layout;
  }

}
