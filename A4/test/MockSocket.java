import java.io.IOException;
import java.net.Socket;

public class MockSocket extends Socket {
  private boolean connected;

  MockSocket() {
    connected = true;
  }

  @Override
  public synchronized void close() throws IOException {
    connected = false;
  }

  @Override
  public boolean isConnected() {
    return connected;
  }
}
