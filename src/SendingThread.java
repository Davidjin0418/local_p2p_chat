import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Vector;

/**
 * This class sends the message to all the peers who are in the chat room.
 * It also has the functionality of the groupleader which determine whether a peer can 
 * join the talk.
 * @author bojunjin
 * 
 */
public class SendingThread extends Thread implements Runnable {
	public static Vector<String[]> peers;

	private Peer peer;

	public SendingThread(Peer p) {
		peer = p;
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					sendMessageToAllClients(peer.name
							+ " has left the group chat");
					deacttivate(peer.listeningPort);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void run() {
		peers = readFile("file.txt");
		//check if the peer has joined the talk or not.
		if (isActivate(peer.listeningPort)) {
			System.out.println("The user has alreadt joined the talk");
			System.exit(1);
		} else {
			activate(peer.listeningPort);
			boolean contain = false;
			String color = null;
			for (int i = 0; i < peers.size(); i++) {
				String[] p = peers.get(i);
				String compare = p[0] + p[1] + p[2];
				if (compare.equals(peer.toString())) {
					color = p[3];
					contain = true;
					ListeningThreads listeningThread = new ListeningThreads(
							peer);
					listeningThread.start();
				}
			}
			if (contain) {
				try {
					System.out.println("-------Welcome to chat room-------");
					sendMessageToAllClients((char) 27 + color + peer.name
							+ " has joined the group chat");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			while (!isInterrupted()) {
				if (contain) {
					BufferedReader in = new BufferedReader(
							new InputStreamReader(System.in));
					try {
						String message = in.readLine();
						message = (char) 27 + color + peer.name + ":" + message;
						sendMessageToAllClients(message);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("You can not join the talk");
					System.exit(1);
				}
			}
		}
	}

	/**
	 * This method sends the message to all peers who are in the chat room.
	 * @param message
	 * @throws IOException
	 */
	private synchronized void sendMessageToAllClients(String message)
			throws IOException {
		peers = readFile("file.txt");
		for (int i = 0; i < peers.size(); i++) {
			String[] s = peers.get(i);
			int port = Integer.parseInt(s[0]);
			if (isActivate(port)) {
				SingleSender sender = new SingleSender(port, message);
				sender.start();
			}

		}
	}
    /**
     * 
     * @param port
     * @return the port is activated or not.
     */
	private boolean isActivate(int port) {
		boolean result = false;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("file.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				String[] s = line.split(" ");
				if (port == Integer.parseInt(s[0])) {
					if (s[4].equals("a")) {
						result = true;
					}
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
    /**
     * This method checks activate the specified port by changing the file information
     * the file has a field for the status of the peer. a for activate ,d for deactivate.
     * This method changes the status to a.
     * @param port
     */
	private void activate(int port) {
		try {
			// input the file content to the String "input"
			BufferedReader file = new BufferedReader(new FileReader("file.txt"));
			String line;
			String input = "";

			while ((line = file.readLine()) != null) {
				String[] s = line.split(" ");
				if (port == Integer.parseInt(s[0])) {
					s[4] = "a";
				}
				String result = s[0] + " " + s[1] + " " + s[2] + " " + s[3]
						+ " " + s[4];
				input += result + '\n';
			}

			// write the new String with the replaced line OVER the same file
			@SuppressWarnings("resource")
			FileOutputStream File = new FileOutputStream("file.txt");
			File.write(input.getBytes());

		} catch (Exception e) {
			System.out.println("Problem reading file.");
		}
	}
    /**
     * This method checks activate the specified port by changing the file information
     * the file has a field for the status of the peer. a for activate ,d for deactivate.
     * This method changes the status to d.
     * @param port
     */
	private void deacttivate(int port) {
		try {
			// input the file content to the String "input"
			BufferedReader file = new BufferedReader(new FileReader("file.txt"));
			String line;
			String input = "";

			while ((line = file.readLine()) != null) {
				String[] s = line.split(" ");
				if (port == Integer.parseInt(s[0])) {
					s[4] = "d";
				}
				String result = s[0] + " " + s[1] + " " + s[2] + " " + s[3]
						+ " " + s[4];
				input += result + '\n';
			}

			// write the new String with the replaced line over the same file
			FileOutputStream File = new FileOutputStream("file.txt");
			File.write(input.getBytes());

		} catch (Exception e) {
			System.out.println("Problem reading file.");
		}
	}

	/**
	 * This method scan the file specified, this file should contain the
	 * information of peers who can join the talk.
	 * 
	 * @param filename
	 *            which stores the information of all peers
	 * @return a vector which contains all the information of peers who are able
	 *         to join the talk
	 */
	private Vector<String[]> readFile(String filename) {
		Vector<String[]> peers = new Vector<String[]>();
		try {
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			String peerMessage = br.readLine();
			while (!(peerMessage == null)) {
				String[] result = peerMessage.split(" ");

				peers.add(result);
				peerMessage = br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return peers;

	}

}
