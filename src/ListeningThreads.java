import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
/**
 * This class create a server socket for listening the message sent from the other peers.
 * It extends thread and implements runnable.
 * @author bojunjin
 *
 */
public class ListeningThreads extends Thread implements Runnable {
	private Peer peer;

	public ListeningThreads(Peer p) {
		peer = p;
        //When shut down, the server socket will be closed.
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					peer.serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				;
			}
		});

	}

	public void run() {
		try {
			while (!isInterrupted()) {
				Socket socket = peer.serverSocket.accept();
				ObjectInputStream ois = new ObjectInputStream(
						socket.getInputStream());
				// convert ObjectInputStream object to String
				String message;
				try {
					message = (String) ois.readObject();
					System.out.println(message);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			System.err.println("Can not start listening");
			e.printStackTrace();
			System.exit(1);
		}

	}
}
