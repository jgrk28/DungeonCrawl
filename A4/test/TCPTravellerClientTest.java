import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import org.junit.Test;

public class TCPTravellerClientTest {
  private MockTravellerClient client;
  private MockSocket socket;
  private InputStream inputFromTestServer;
  private OutputStream outputToTestServer;
  private ByteArrayOutputStream outputToUser;

  public void setupStreams(String testInput) {
    socket = new MockSocket();

    inputFromTestServer = new ByteArrayInputStream(testInput.getBytes());
    outputToTestServer = new ByteArrayOutputStream();
    client = new MockTravellerClient(inputFromTestServer, outputToTestServer, socket);

    outputToUser = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputToUser));
  }

  @Test
  public void testParseCommandLine() {
    //Just testing that no error is thrown
    setupStreams("");
    String[] args1 = {""};
    String[] args2 = {"1.1.1.1"};
    String[] args3 = {"1.1.1.1", "8001"};
    String[] args4 = {"127.65.0.10", "25", "Jacob Kaplan"};

    client.parseCommandLine(args1);
    client.parseCommandLine(args2);
    client.parseCommandLine(args3);
    client.parseCommandLine(args4);
  }

  @Test (expected = NumberFormatException.class)
  public void testParseCommandLineError1() {
    //Just testing that no error is thrown
    setupStreams("");
    String[] args = {"0.0.0.0", "25f", "1234"};

    client.parseCommandLine(args);
  }

  @Test (expected = NumberFormatException.class)
  public void testParseCommandLineError2() {
    //Just testing that no error is thrown
    setupStreams("");
    String[] args = {"2.2.2.2", "1.1", ""};

    client.parseCommandLine(args);
  }

  @Test
  public void testInitializeConnection() {
    setupStreams("\"1234\"");
    String expectedOutput = "[\"the server will call me\",\"Jacob Kaplan\"]\n";

    String[] args = {"127.65.0.10", "25", "Jacob Kaplan"};
    client.parseCommandLine(args);
    try {
      client.initializeConnection();
    } catch (IOException e) {
      //Will never get here because mock client never throws IOException
    }
    assertEquals(expectedOutput, outputToUser.toString());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInitializeConnectionBadID() {
    setupStreams("{\"total\":12,\"object\":12}");
    try {
      client.initializeConnection();
    } catch (IOException e) {
      //Will never get here because mock client never throws IOException
    }
  }

  @Test
  public void testHandleUserInputNormalOperation() {
    String userInput = "{ \"command\" : \"roads\", "
        + "\"params\" : [ {\"from\" : \"Boston\", \"to\" : \"Cambridge\" }, "
        + "{\"from\" : \"Boston\", \"to\" : \"Allston\" }, "
        + "{\"from\" : \"Cambridge\", \"to\" : \"Somerville\" }] } \n"
        + "{ \"command\" : \"place\", "
        + "\"params\" : { \"character\" : \"Jacob\", \"town\" : \"Boston\" } } \n"
        + "{ \"command\" : \"passage-safe?\", "
        + "\"params\" : { \"character\" : \"Jacob\", \"town\" : \"Somerville\" } }";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    String serverResponses = "\"1234\"{\"invalid\":[],"
        + "\"response\":true}";
    setupStreams(serverResponses);

    String expectedToServer = "Jacob Kaplan"
        + "{\"towns\":[\"Allston\",\"Cambridge\",\"Somerville\",\"Boston\"],"
        + "\"roads\":[{\"from\":\"Boston\",\"to\":\"Cambridge\"},"
        + "{\"from\":\"Boston\",\"to\":\"Allston\"},"
        + "{\"from\":\"Cambridge\",\"to\":\"Somerville\"}]}"
        + "{\"characters\":[{\"town\":\"Boston\",\"name\":\"Jacob\"}],"
        + "\"query\":{\"character\":\"Jacob\",\"destination\":\"Somerville\"}}";
    String expectedToUser = "[\"the server will call me\",\"Jacob Kaplan\"]\n"
        + "[\"the response for\",{\"character\":\"Jacob\",\"destination\":\"Somerville\"},"
        + "\"is\",true]\n";

    String[] args = {"127.65.0.10", "25", "Jacob Kaplan"};
    client.parseCommandLine(args);
    try {
      client.initializeConnection();
    } catch (IOException e) {
      //Will never get here because mock client never throws IOException
    }
    client.handleUserInput();
    assertEquals(expectedToServer, outputToTestServer.toString());
    assertEquals(expectedToUser, outputToUser.toString());
  }

  @Test
  public void testHandleUserInputInvalidPlacement() {
    String userInput = "{ \"command\" : \"roads\", "
        + "\"params\" : [ {\"from\" : \"Boston\", \"to\" : \"Cambridge\" }, "
        + "{\"from\" : \"Boston\", \"to\" : \"Allston\" }, "
        + "{\"from\" : \"Cambridge\", \"to\" : \"Somerville\" }] } \n"
        + "{ \"command\" : \"place\", "
        + "\"params\" : { \"character\" : \"Jacob\", \"town\" : \"Above\" } } \n"
        + "{ \"command\" : \"place\", "
        + "\"params\" : { \"character\" : \"Juliette\", \"town\" : \"Moon\" } } \n"
        + "{ \"command\" : \"place\", "
        + "\"params\" : { \"character\" : \"WALL-E\", \"town\" : \"Boston\" } } \n"
        + "{ \"command\" : \"passage-safe?\", "
        + "\"params\" : { \"character\" : \"WALL-E\", \"town\" : \"Allston\" } }";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    String serverResponses = "\"1234\"{\"invalid\":[{\"name\":\"Jacob\",\"town\":\"Above\"},"
        + "{\"name\":\"Juliette\",\"town\":\"Moon\"}],"
        + "\"response\":true}";
    setupStreams(serverResponses);

    String expectedToServer = "Jacob Kaplan"
        + "{\"towns\":[\"Allston\",\"Cambridge\",\"Somerville\",\"Boston\"],"
        + "\"roads\":[{\"from\":\"Boston\",\"to\":\"Cambridge\"},"
        + "{\"from\":\"Boston\",\"to\":\"Allston\"},"
        + "{\"from\":\"Cambridge\",\"to\":\"Somerville\"}]}"
        + "{\"characters\":[{\"town\":\"Moon\",\"name\":\"Juliette\"},"
        + "{\"town\":\"Boston\",\"name\":\"WALL-E\"},"
        + "{\"town\":\"Above\",\"name\":\"Jacob\"}],"
        + "\"query\":{\"character\":\"WALL-E\",\"destination\":\"Allston\"}}";
    String expectedToUser = "[\"the server will call me\",\"Jacob Kaplan\"]\n"
        + "[\"invalid placement\",{\"town\":\"Above\",\"name\":\"Jacob\"}]\n"
        + "[\"invalid placement\",{\"town\":\"Moon\",\"name\":\"Juliette\"}]\n"
        + "[\"the response for\",{\"character\":\"WALL-E\",\"destination\":\"Allston\"},"
        + "\"is\",true]\n";

    String[] args = {"127.65.0.10", "25", "Jacob Kaplan"};
    client.parseCommandLine(args);
    try {
      client.initializeConnection();
    } catch (IOException e) {
      //Will never get here because mock client never throws IOException
    }
    client.handleUserInput();
    assertEquals(expectedToServer, outputToTestServer.toString());
    assertEquals(expectedToUser, outputToUser.toString());
  }

  @Test
  public void testHandleUserInputMultipleQuery() {
    String userInput = "{ \"command\" : \"roads\", "
        + "\"params\" : [ {\"from\" : \"Boston\", \"to\" : \"Cambridge\" }, "
        + "{\"from\" : \"Boston\", \"to\" : \"Allston\" }, "
        + "{\"from\" : \"Cambridge\", \"to\" : \"Somerville\" }] } \n"
        + "{ \"command\" : \"place\", "
        + "\"params\" : { \"character\" : \"Jacob\", \"town\" : \"Above\" } } \n"
        + "{ \"command\" : \"place\", "
        + "\"params\" : { \"character\" : \"Juliette\", \"town\" : \"Cambridge\" } } \n"
        + "{ \"command\" : \"passage-safe?\", "
        + "\"params\" : { \"character\" : \"Juliette\", \"town\" : \"Somerville\" } } \n"
        + "{ \"command\" : \"place\", "
        + "\"params\" : { \"character\" : \"WALL-E\", \"town\" : \"Boston\" } } \n"
        + "{ \"command\" : \"passage-safe?\", "
        + "\"params\" : { \"character\" : \"WALL-E\", \"town\" : \"Somerville\" } }";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    String serverResponses = "\"1234\""
        + "{\"invalid\":[{\"name\":\"Jacob\",\"town\":\"Above\"}],"
        + "\"response\":true}"
        + "{\"invalid\":[],"
        + "\"response\":false}";
    setupStreams(serverResponses);

    String expectedToServer = "Jacob Kaplan"
        + "{\"towns\":[\"Allston\",\"Cambridge\",\"Somerville\",\"Boston\"],"
        + "\"roads\":[{\"from\":\"Boston\",\"to\":\"Cambridge\"},"
        + "{\"from\":\"Boston\",\"to\":\"Allston\"},"
        + "{\"from\":\"Cambridge\",\"to\":\"Somerville\"}]}"
        + "{\"characters\":[{\"town\":\"Cambridge\",\"name\":\"Juliette\"},"
        + "{\"town\":\"Above\",\"name\":\"Jacob\"}],"
        + "\"query\":{\"character\":\"Juliette\",\"destination\":\"Somerville\"}}"
        + "{\"characters\":[{\"town\":\"Boston\",\"name\":\"WALL-E\"}],"
        + "\"query\":{\"character\":\"WALL-E\",\"destination\":\"Somerville\"}}";
    String expectedToUser = "[\"the server will call me\",\"Jacob Kaplan\"]\n"
        + "[\"invalid placement\",{\"town\":\"Above\",\"name\":\"Jacob\"}]\n"
        + "[\"the response for\",{\"character\":\"Juliette\",\"destination\":\"Somerville\"},"
        + "\"is\",true]\n"
        + "[\"the response for\",{\"character\":\"WALL-E\",\"destination\":\"Somerville\"},"
        + "\"is\",false]\n";

    String[] args = {"127.65.0.10", "25", "Jacob Kaplan"};
    client.parseCommandLine(args);
    try {
      client.initializeConnection();
    } catch (IOException e) {
      //Will never get here because mock client never throws IOException
    }
    client.handleUserInput();
    assertEquals(expectedToServer, outputToTestServer.toString());
    assertEquals(expectedToUser, outputToUser.toString());
  }

  @Test
  public void testHandleUserInputBadUserInput() {
    String userInput = "{ \"command\" : \"roads\", "
        + "\"params\" : [ {\"from\" : \"Boston\", \"to\" : \"Cambridge\" }, "
        + "{\"from\" : \"Boston\", \"to\" : \"Allston\" }, "
        + "{\"from\" : \"Cambridge\", \"to\" : \"Somerville\" }] } \n"
        + "{ \"command\" : \"dont-place\", "
        + "\"params\" : { \"character\" : \"Jacob\", \"town\" : \"Above\" } } \n"
        + "{ \"command\" : \"place\", "
        + "\"params\" : { \"character\" : \"Juliette\"} } \n"
        + "{ \"command\" : \"place\", "
        + "\"params\" : {\"town\" : \"Boston\" } } \n"
        + "{ \"command\" : \"passage-safe?\", "
        + "\"params\" : { \"character\" : \"WALL-E\"} }\n"
        + "{ \"command\" : \"passage-safe?\", "
        + "\"params\" : { \"town\" : \"Somerville\" } }";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    String serverResponses = "\"1234\"";
    setupStreams(serverResponses);

    String expectedToServer = "Jacob Kaplan"
        + "{\"towns\":[\"Allston\",\"Cambridge\",\"Somerville\",\"Boston\"],"
        + "\"roads\":[{\"from\":\"Boston\",\"to\":\"Cambridge\"},"
        + "{\"from\":\"Boston\",\"to\":\"Allston\"},"
        + "{\"from\":\"Cambridge\",\"to\":\"Somerville\"}]}";
    String expectedToUser = "[\"the server will call me\",\"Jacob Kaplan\"]\n"
        + "{\"error\":\"not a request\","
        + "\"object\":{\"params\":{\"character\":\"Jacob\",\"town\":\"Above\"},"
        + "\"command\":\"dont-place\"}}\n"
        + "{\"error\":\"not a request\","
        + "\"object\":{\"params\":{\"character\":\"Juliette\"},"
        + "\"command\":\"place\"}}\n"
        + "{\"error\":\"not a request\","
        + "\"object\":{\"params\":{\"town\":\"Boston\"},"
        + "\"command\":\"place\"}}\n"
        + "{\"error\":\"not a request\","
        + "\"object\":{\"params\":{\"character\":\"WALL-E\"},"
        + "\"command\":\"passage-safe?\"}}\n"
        + "{\"error\":\"not a request\","
        + "\"object\":{\"params\":{\"town\":\"Somerville\"},"
        + "\"command\":\"passage-safe?\"}}\n";

    String[] args = {"127.65.0.10", "25", "Jacob Kaplan"};
    client.parseCommandLine(args);
    try {
      client.initializeConnection();
    } catch (IOException e) {
      //Will never get here because mock client never throws IOException
    }
    client.handleUserInput();
    assertEquals(expectedToServer, outputToTestServer.toString());
    assertEquals(expectedToUser, outputToUser.toString());
  }

  @Test
  public void testHandleUserInputServerShutDown() {
    String userInput = "{ \"command\" : \"roads\", "
        + "\"params\" : [ {\"from\" : \"Boston\", \"to\" : \"Cambridge\" }, "
        + "{\"from\" : \"Cambridge\", \"to\" : \"Boston\" }] } \n"
        + "{ \"command\" : \"place\", "
        + "\"params\" : { \"character\" : \"Jacob\", \"town\" : \"Boston\" } } \n"
        + "{ \"command\" : \"passage-safe?\", "
        + "\"params\" : { \"character\" : \"Jacob\", \"town\" : \"Somerville\" } }";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    String serverResponses = "\"1234\""
        + "{\"name\":\"Jacob\",\"town\":\"Somerville\"}],"
        + "\"response\":true}";
    setupStreams(serverResponses);

    String expectedToServer = "Jacob Kaplan"
        + "{\"towns\":[\"Cambridge\",\"Boston\"],"
        + "\"roads\":[{\"from\":\"Boston\",\"to\":\"Cambridge\"},"
        + "{\"from\":\"Cambridge\",\"to\":\"Boston\"}]}";
    String expectedToUser = "[\"the server will call me\",\"Jacob Kaplan\"]\n";

    String[] args = {"127.65.0.10", "25", "Jacob Kaplan"};
    client.parseCommandLine(args);
    try {
      client.initializeConnection();
      socket.close();
    } catch (IOException e) {
      //Will never get here because mock client and socket never throw IOException
    }

    client.handleUserInput();
    assertEquals(expectedToServer, outputToTestServer.toString());
    assertEquals(expectedToUser, outputToUser.toString());
  }

}
