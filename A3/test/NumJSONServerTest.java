import a3.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumJSONServerTest {
  //Class to act as a server and parse NumJSONs being sent to it
  public class MockNumJSONServer extends NumJSONServer {
    public MockNumJSONServer(int port) {
      super(port);
    }

    @Override
    public Socket createSocket() throws IOException {
      return new MockSocket();
    }
  }

  //Mock Socket class that gets input from STDIN and prints output to STDOUT
  public class MockSocket extends Socket {

    @Override
    public InputStream getInputStream() throws IOException {
      return System.in;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
      return System.out;
    }
  }

  // Test the NumJSONServerConstructor constructor with different ports
  @Test(expected = IllegalArgumentException.class)
  public void testNumJSONServerBigPort() {
    NumJSONServer server = new NumJSONServer(65536);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNumJSONServerSmallPort() {
    NumJSONServer server = new NumJSONServer(-1);
  }

  @Test
  public void testComputeClientOutput() {
    String input = "12        [2, \"foo\", \n4]  { \"name\" : \"SwDev\", \"payload\" : \n  [12, 33]     , \n        \"other\" : { \"payload\" : [ 4, 7 ] } }";
    String expectedOutput = "[{\"total\":12,\"object\":12},{\"total\":6,\"object\":[2,\"foo\",4]},{\"total\":45,\"object\":{\"other\":{\"payload\":[4,7]},\"payload\":[12,33],\"name\":\"SwDev\"}}]\n";

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream print = new PrintStream(output);
    System.setOut(print);

    try {
      NumJSONServer server = new MockNumJSONServer(8000);
      Socket socket = server.createSocket();
      server.linkSocketIO(socket);
      server.computeClientOutput(input);
      server.close(socket);
    } catch (IOException e) {
      e.printStackTrace();
    }

    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testParseClientInput() {
    Map<String, String> mapIO = new HashMap<>();

    String input1 = "12 \"foo\" [ 4, 7 ] END";
    String expectedOutput1 = "[{\"total\":12,\"object\":12},{\"total\":0,\"object\":\"foo\"},{\"total\":11,\"object\":[4,7]}]\n";
    String input2 = "5 END 3";
    String expectedOutput2 = "[{\"total\":5,\"object\":5}]\n";
    String input3 = "5 \"foo\" \nEND";
    String expectedOutput3 = "[{\"total\":5,\"object\":5},{\"total\":0,\"object\":\"foo\"}]\n";
    String input4 = "432 \"foo\"";
    String expectedOutput4 = "Socket stream closed before END sent\n[{\"total\":432,\"object\":432},{\"total\":0,\"object\":\"foo\"}]\n";

    mapIO.put(input1, expectedOutput1);
    mapIO.put(input2, expectedOutput2);
    mapIO.put(input3, expectedOutput3);
    mapIO.put(input4, expectedOutput4);

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream print = new PrintStream(output);
    System.setOut(print);

    for (Map.Entry<String, String> entryIO : mapIO.entrySet()) {
      System.setIn(new ByteArrayInputStream(entryIO.getKey().getBytes()));
      try {
        NumJSONServer server = new MockNumJSONServer(8000);
        Socket socket = server.createSocket();
        server.linkSocketIO(socket);
        server.parseClientInput();
        server.close(socket);
      } catch (IOException e) {
        e.printStackTrace();
      }
      assertEquals(entryIO.getValue(), output.toString());
      output.reset();
    }
  }
}
