import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;
/**
 * This class stores the information of the peers.
 * @author bojunjin
 *
 */
public class Peer {
	protected int listeningPort;
	protected String name;
	protected int SendingPort;
	protected ServerSocket serverSocket;
    /**
     * 
     * @param listen port for listening
     * @param user username
     * @param send port for sending message
     */
	public Peer(int listen, String user, int send) {
		listeningPort = listen;
		name = user;
		SendingPort = send;
		try {
			serverSocket = new ServerSocket(listeningPort);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    /**
     * This method return a string which contains the information of the peer.
     */
	public String toString() {
		return this.listeningPort + this.name + this.SendingPort;
	}
}