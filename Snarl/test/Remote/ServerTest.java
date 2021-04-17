package Remote;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class ServerTest {
  private class MockServer extends Server {
    public MockServer(String ipAddress, int port, MockServerSocket socket) {
      super(ipAddress, port);
      this.socket = socket;
    }

    public Set<String> getPlayerNames() {
      return this.playerSockets.keySet();
    }
  }

  private class MockServerSocket extends ServerSocket {
    private StringBuilder log;
    private List<Socket> socketsToAccept;

    public MockServerSocket() throws IOException {
      super();
      this.socketsToAccept = new ArrayList<>();
      this.log = new StringBuilder();
    }

    public void addSocketToAccept(Socket socket) {
      this.socketsToAccept.add(socket);
    }

    @Override
    public Socket accept() {
      log.append("accept\n");
      Socket socket = this.socketsToAccept.get(0);
      this.socketsToAccept.remove(socket);
      return socket;
    }

    @Override
    public synchronized void setSoTimeout(int timeout) throws SocketException {
      log.append("setSoTimeout(" + timeout + ")\n");
    }

    public String getLog() {
      return log.toString();
    }
  }

  public static class MockSocket extends Socket {
    private ArrayList<String> becomesInput;
    private OutputStream streamOut;

    public MockSocket() {
      this.becomesInput = new ArrayList();
      this.streamOut = new ByteArrayOutputStream();
    }

    public void addInput(String string) {
      this.becomesInput.add(string);
    }

    @Override
    public InputStream getInputStream() {
      String toSend = this.becomesInput.get(0);
      this.becomesInput.remove(0);
      return new ByteArrayInputStream(toSend.getBytes());
    }

    @Override
    public OutputStream getOutputStream() {
      return this.streamOut;
    }

    public String[] getLines() {
      return streamOut.toString().split("\n");
    }
  }

  private MockServer server;
  private MockServerSocket serverSocket;
  private MockSocket socket1;
  private MockSocket socket2;

  @Before
  public void setupServerTest() throws IOException {
    this.serverSocket = new MockServerSocket();
    this.socket1 = new MockSocket();
    this.socket1.addInput("Jacob");
    this.socket2 = new MockSocket();
    this.socket2.addInput("Juliette");
    this.serverSocket.addSocketToAccept(this.socket1);
    this.serverSocket.addSocketToAccept(this.socket2);
    this.server = new MockServer("127.0.0.1", 54321, serverSocket);
  }

  @Test
  public void testWelcome() throws IOException {
    this.server.registerPlayers(1, 5);

    String[] outputToSocketLines = socket1.getLines();

    JSONObject expectedWelcome = new JSONObject();
    expectedWelcome.put("type", "welcome");
    JSONObject serverInfo = new JSONObject();
    serverInfo.put("ip-address", "127.0.0.1");
    serverInfo.put("port", 54321);
    expectedWelcome.put("info", serverInfo);

    assertEquals(expectedWelcome.toString(), outputToSocketLines[0]);
  }

  @Test
  public void testRegisterPlayers() throws IOException {
    this.server.registerPlayers(2, 5);

    String expectedLog = "setSoTimeout(5000)\n"
        + "accept\n"
        + "setSoTimeout(5000)\n"
        + "accept\n";
    assertEquals(expectedLog, this.serverSocket.getLog());

    Set<String> expectedNames = new HashSet<>(Arrays.asList("Jacob", "Juliette"));
    assertEquals(expectedNames, this.server.getPlayerNames());
  }

  @Test
  public void testSendMessage() throws IOException {
    this.server.registerPlayers(2, 5);

    String message1 = "Hello there!";
    String message2 = "Oh hi!";
    server.sendMessage("Jacob", message1);
    server.sendMessage("Juliette", message2);
    String[] output1Lines = socket1.getLines();
    String[] output2Lines = socket2.getLines();

    assertEquals(message1, output1Lines[2]);
    assertEquals(message2, output2Lines[2]);
  }

  @Test
  public void testReceiveMessage() throws IOException {
    MockSocket socket3 = new MockSocket();
    socket3.addInput("Spiderman");
    socket3.addInput("Hello");
    socket3.addInput("{\"type\":\"test\"}");
    this.serverSocket.addSocketToAccept(socket3);
    this.server.registerPlayers(3, 5);

    Object received = this.server.receiveMessage("Spiderman");
    assertEquals("Hello", received);

    received = this.server.receiveMessage("Spiderman");
    JSONObject JSONReceived = (JSONObject) received;
    assertEquals("test", JSONReceived.getString("type"));
  }

  @Test
  public void testSendLevelEnd() throws IOException {
    this.server.registerPlayers(2, 5);
    List<String> exitedPlayers = new ArrayList<>(Arrays.asList("Juliette", "Jacob"));
    List<String> ejectedPlayers = new ArrayList<>();

    this.server.sendLevelEnd("Jacob", "Jacob", exitedPlayers, ejectedPlayers);

    String[] output1Lines = socket1.getLines();

    JSONObject expectedLevelEnd = new JSONObject();
    JSONArray exitNameList = new JSONArray(exitedPlayers);
    JSONArray ejectsNameList = new JSONArray(ejectedPlayers);
    expectedLevelEnd.put("type", "end-level");
    expectedLevelEnd.put("key", "Jacob");
    expectedLevelEnd.put("exits", exitNameList);
    expectedLevelEnd.put("ejects", ejectsNameList);

    assertEquals(expectedLevelEnd.toString(), output1Lines[2]);
  }

  @Test
  public void testSendEndGame() throws IOException {
    this.server.registerPlayers(2, 5);
    Map<String, Integer> keysFound = new HashMap<>();
    keysFound.put("Jacob", 1);
    keysFound.put("Juliette", 1);
    Map<String, Integer> numExits = new HashMap<>();
    numExits.put("Jacob", 0);
    numExits.put("Juliette", 1);
    Map<String, Integer> numEjects = new HashMap<>();
    numEjects.put("Jacob", 2);
    numEjects.put("Juliette", 1);

    this.server.sendEndGame("Jacob", keysFound, numEjects, numExits);

    JSONObject expectedEndGame = new JSONObject();
    JSONArray playerScoreList = new JSONArray();
    expectedEndGame.put("type", "end-game");

    JSONObject julietteScore = new JSONObject();
    julietteScore.put("type", "player-score");
    julietteScore.put("name", "Juliette");
    julietteScore.put("exits", 1);
    julietteScore.put("ejects", 1);
    julietteScore.put("keys", 1);
    playerScoreList.put(julietteScore);

    JSONObject jacobScore = new JSONObject();
    jacobScore.put("type", "player-score");
    jacobScore.put("name", "Jacob");
    jacobScore.put("exits", 0);
    jacobScore.put("ejects", 2);
    jacobScore.put("keys", 1);
    playerScoreList.put(jacobScore);

    expectedEndGame.put("scores", playerScoreList);

    String[] output1Lines = socket1.getLines();
    assertEquals(expectedEndGame.toString(), output1Lines[2]);
  }
}