package networking;

import gameModel.HuntTheWumpus;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Observable;

/**
 * This class is used to represent a Client in a network. Upon creation, the
 * client attempts to connect to a server at a given port and IP address. The
 * client can send and receive objects from the server at any point.
 * 
 * @author Salika Dunatunga, Sarah Lutjens, Jane Wang, Denise Werchan
 * 
 */
public class Client extends Observable {

	private Socket socket;

	private ObjectInputStream inStream;
	private ObjectOutputStream outStream;
	private int count;
	private HuntTheWumpus htw;
	private boolean connected;

	/**
	 * The constructor for the Client attempts to initialize a new socket at the
	 * passed ip and port. After the socket is initialized, an inputStream and
	 * an outputStream are created for the socket.
	 * 
	 * @param ip
	 *            the IP address to connect to
	 * @param port
	 *            the port to connect on
	 * @param htw 
	 * 			  the HuntTheWumpus game this client belongs to
	 */
	public Client(String ip, Integer port, HuntTheWumpus htw) {
		// connect to server using information given above
		try {
			count = 0;
			this.htw = htw;

			socket = new Socket(ip, port);

			inStream = new ObjectInputStream(socket.getInputStream());
			outStream = new ObjectOutputStream(socket.getOutputStream());
			connected = true;

		} catch (ConnectException e) {
			String message = "Connection failed.";
			htw.serverUnavailableProcessing(message + "NTWRK");
		} catch (UnknownHostException e) {
			String message = "Invalid I.P. address.";
			htw.serverUnavailableProcessing(message + "NTWRK");
		} catch (IOException e) {
			String message = "Disconnected from network.";
			htw.serverUnavailableProcessing(message + "NTWRK");
		} catch (Exception e) {
			String message = "Unexpected error occurred.";
			htw.serverUnavailableProcessing(message + "NTWRK");
		}
	}

	/**
	 * Returns a boolean identifying if this client is connected to a server.
	 * 
	 * @return boolean identifying the connection state of this client object.
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * This method receives object's from the server through the inputStream
	 * associated with this client.
	 * 
	 * @return the object received from the server
	 */
	public Object receiveObject() {
		Object obj;
		try {
			if (inStream != null) {
				obj = inStream.readObject();
				return obj;
			}
		} catch (SocketException se) {
			try {
				String message = "Socket closed.";
				htw.serverUnavailableProcessing(message + "NTWRK");
			} catch (Exception e) {
				String message = "Unexpected error occurred.";
				htw.serverUnavailableProcessing(message + "NTWRK");
			}
		} catch (IOException ioe) {
			String message = "Disconnected from network.";
			htw.serverUnavailableProcessing(message + "NTWRK");
		} catch (Exception e) {
			String message = "Unexpected error occurred.";
			htw.serverUnavailableProcessing(message + "NTWRK");
		}
		return null;
	}

	/**
	 * This method writes a passed object to the server, so that the server can
	 * process it and send it to the other clients.
	 * 
	 * @param obj
	 *            the object to send.
	 */
	public void writeObject(Object obj) {
		try {
			outStream.writeObject(obj);
			outStream.reset();
		} catch (SocketException se) {
			String message = "Socket closed.";
			htw.serverUnavailableProcessing(message + "NTWRK");
		} catch (IOException e) {
			String message = "Disconnected from network.";
			htw.serverUnavailableProcessing(message + "NTWRK");
		} catch (Exception e) {
			String message = "Unexpected error occured.";
			htw.serverUnavailableProcessing(message + "NTWRK");
		}
	}
}
