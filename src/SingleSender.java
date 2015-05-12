import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
/**
 * This class create a new socket which is connected to the specified destination host 
 * and send the message to the host.
 * @author bojunjin
 *
 */
public class SingleSender extends Thread implements Runnable {
	protected Socket socket;
	protected String message;
	public SingleSender(int dstPort, String m) {
		try {
			socket = new Socket("localhost", dstPort);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		message = m;
	}

	public void run() {
		while (!isInterrupted()) {
			try {
				ObjectOutputStream oos = new ObjectOutputStream(
						socket.getOutputStream());
				oos.writeObject(message);
				this.interrupt();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
