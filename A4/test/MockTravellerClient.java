import a4.TCPTravellerClient;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import org.json.JSONTokener;

public class MockTravellerClient extends TCPTravellerClient{
  private InputStream inputFromTest;
  private OutputStream outputToTest;
  private MockSocket mockSocket;

  MockTravellerClient(InputStream inputFromTest, OutputStream outputToTest, MockSocket mockSocket) {
    super();
    this.outputToTest = outputToTest;
    this.inputFromTest = inputFromTest;
    this.mockSocket = mockSocket;
  }

  @Override
  protected void startSocket() {
    socket = mockSocket;
    outputToServer = new PrintStream(outputToTest);
    inputFromServer = new JSONTokener(inputFromTest);
  }
}
