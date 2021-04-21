package Remote;

import static org.junit.Assert.*;

import Player.LocalPlayer;
import Remote.ServerTest.MockSocket;
import java.io.ByteArrayInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class ClientTest {

  private class MockPlayer extends LocalPlayer {
    public ArrayList<String> messages;

    public MockPlayer() {
      this.messages = new ArrayList<>();
    }

    @Override
    public void displayMessage(String message) {
      this.messages.add(message);
    }
  }

  private class MockClient extends Client {

    public MockClient(LocalPlayer player, Socket socket) {
      super(player, socket);
    }
  }

  private MockPlayer player;
  private MockClient client;
  private MockSocket socket;

  @Before
  public void initClientTest() {
    this.player = new MockPlayer();
    this.socket = new MockSocket();
    this.socket.addInput("name\n");
    this.client = new MockClient(this.player, socket);
  }

  @Test
  public void testWelcomePlayer() {
    JSONObject welcomeJSON = new JSONObject();
    welcomeJSON.put("type", "welcome");
    JSONObject serverInfo = new JSONObject();
    serverInfo.put("port", 45678);
    serverInfo.put("ip-address", "127.0.0.1");
    welcomeJSON.put("info", serverInfo);
    this.client.welcomePlayer(welcomeJSON);

    String expectedMessage = "Connected to server at ip: 127.0.0.1 port: 45678";
    assertEquals(expectedMessage, this.player.messages.get(0));
  }

  @Test
  public void testGetPlayerName() {
    ByteArrayInputStream in = new ByteArrayInputStream("Jacob".getBytes());
    System.setIn(in);
    this.client.getPlayerName();

    assertEquals("Jacob\n", this.socket.getOutputStream().toString());
  }

  @Test
  public void testStartLevel() {
    JSONObject startLevelJSON = new JSONObject();
    startLevelJSON.put("type", "start-level");
    startLevelJSON.put("level", 1);
    JSONArray playerNames = new JSONArray();
    playerNames.put("Jacob");
    playerNames.put("Juliette");
    startLevelJSON.put("players", playerNames);

    this.client.startLevel(startLevelJSON);

    String expectedMessage = "Starting level 1 with players [\"Jacob\",\"Juliette\"]";
    assertEquals(expectedMessage, this.player.messages.get(0));
  }

  @Test
  public void testUpdatePlayer() {
    JSONArray layout = new JSONArray();
    JSONArray row1 = new JSONArray(Arrays.asList(0,0,0,0,0));
    JSONArray row2 = new JSONArray(Arrays.asList(0,0,0,0,0));
    JSONArray row3 = new JSONArray(Arrays.asList(1,1,1,0,0));
    JSONArray row4 = new JSONArray(Arrays.asList(1,1,1,2,1));
    JSONArray row5 = new JSONArray(Arrays.asList(1,1,1,0,0));
    layout.put(row1);
    layout.put(row2);
    layout.put(row3);
    layout.put(row4);
    layout.put(row5);

    JSONArray position = new JSONArray(Arrays.asList(3,5));

    JSONObject exitJSON = new JSONObject();
    JSONArray exitPosition = new JSONArray(Arrays.asList(4,4));
    exitJSON.put("type", "exit");
    exitJSON.put("position", exitPosition);

    JSONArray objectList = new JSONArray(Arrays.asList(exitJSON));
    JSONArray actorList = new JSONArray();

    JSONObject updateJSON = new JSONObject();
    updateJSON.put("type", "player-update");
    updateJSON.put("current-health", 20);
    updateJSON.put("max-health", 20);
    updateJSON.put("layout", layout);
    updateJSON.put("position", position);
    updateJSON.put("objects", objectList);
    updateJSON.put("actors", actorList);
    updateJSON.put("message", JSONObject.NULL);

    this.client.updatePlayer(updateJSON);

    String expectedMessage1 = "Current position: [5,3]";
    String expectedMessage2 = "You have 20 out of 20 health points\n";
    String expectedMessage3 = "XXXXX\n"
        + "XXXXX\n"
        + "..PXX\n"
        + ".@.|.\n"
        + "...XX\n";
    assertEquals(expectedMessage1, this.player.messages.get(0));
    assertEquals(expectedMessage2, this.player.messages.get(1));
    assertEquals(expectedMessage3, this.player.messages.get(2));
  }

  @Test
  public void testMovePlayer() {
    ByteArrayInputStream in = new ByteArrayInputStream("4 5".getBytes());
    System.setIn(in);
    this.client.movePlayer();

    String expectedOutput = "{\"to\":[5,4],\"type\":\"move\"}\n";
    assertEquals(expectedOutput, this.socket.getOutputStream().toString());
  }

  @Test
  public void testProcessResult() {
    this.client.processResult("Invalid");
    assertEquals("The move was invalid", this.player.messages.get(0));

    this.client.processResult("Exit");
    assertEquals("You exited the level", this.player.messages.get(1));

    this.client.processResult("Eject");
    assertEquals("You were ejected from the level", this.player.messages.get(2));
  }

  @Test
  public void testEndLevel() {
    JSONObject endLevelJSON = new JSONObject();
    endLevelJSON.put("type", "end-level");
    endLevelJSON.put("key", "Jacob");
    JSONArray exitedPlayers = new JSONArray(Arrays.asList("Juliette", "Jacob"));
    endLevelJSON.put("exits", exitedPlayers);
    JSONArray ejectedPlayers = new JSONArray();
    endLevelJSON.put("ejects", ejectedPlayers);

    this.client.endLevel(endLevelJSON);

    String expectedMessage0 = "The level is over";
    String expectedMessage1 = "The key was found by: Jacob";
    String expectedMessage2 = "The following players exited the level: [\"Juliette\",\"Jacob\"]";
    String expectedMessage3 = "No one was ejected from the level";
    assertEquals(expectedMessage0, this.player.messages.get(0));
    assertEquals(expectedMessage1, this.player.messages.get(1));
    assertEquals(expectedMessage2, this.player.messages.get(2));
    assertEquals(expectedMessage3, this.player.messages.get(3));
  }

  @Test
  public void testEndGame() {
    JSONArray scoreList = new JSONArray();
    JSONObject jacobScore = new JSONObject();
    jacobScore.put("type", "player-score");
    jacobScore.put("name", "Jacob");
    jacobScore.put("exits", 5);
    jacobScore.put("ejects", 0);
    jacobScore.put("keys", 0);
    JSONObject julietteScore = new JSONObject();
    julietteScore.put("type", "player-score");
    julietteScore.put("name", "Juliette");
    julietteScore.put("exits", 0);
    julietteScore.put("ejects", 5);
    julietteScore.put("keys", 5);
    scoreList.put(jacobScore);
    scoreList.put(julietteScore);

    JSONObject endGameJSON = new JSONObject();
    endGameJSON.put("type", "end-game");
    endGameJSON.put("scores", scoreList);

    this.client.endGame(endGameJSON);

    String expectedMessage0 = "PLAYER SCORES";
    String expectedMessage1 = "Jacob: exited 5 times and was ejected 0 times. Found 0 keys";
    String expectedMessage2 = "Juliette: exited 0 times and was ejected 5 times. Found 5 keys";
    assertEquals(expectedMessage0, this.player.messages.get(0));
    assertEquals(expectedMessage1, this.player.messages.get(1));
    assertEquals(expectedMessage2, this.player.messages.get(2));
  }
}