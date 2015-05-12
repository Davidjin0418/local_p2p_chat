/**
 * The main class is the entry of the program, it create a new peer as well as its listening 
 * and sending threads.
 * @author bojunjin
 *
 */
public class Main {

	public static void main(String[] args) {
		Peer peer = new Peer(Integer.parseInt(args[0]), args[1],
				Integer.parseInt(args[2]));
		SendingThread sendingThread = new SendingThread(peer);
		sendingThread.start();

	}

}
