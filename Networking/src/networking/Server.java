package networking;

import gameModel.HuntTheWumpus;
import gameModel.Player;
import gameModel.WumpusMap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class is used to represent a Server in a network. Upon creation, the
 * server attempts to initialize a new serverSocket at the passed port. After
 * initializing the server socket, a thread is started to continually listen for
 * new connections until at most 3 clients are connected. For each connected
 * client, a thread is started to receive objects from that client.
 * 
 * @author Salika Dunatunga, Sarah Lutjens, Jane Wang, Denise Werchan
 * 
 */
public class Server extends Observable implements Runnable {

	private ServerSocket serverSocket;
	private LinkedBlockingQueue<ClientManager> removal;
	private LinkedList<ClientManager> clientManagers;
	private int clientCount;
	private WumpusMap map;
	private ArrayList<Player> playerList;
	private HashMap<Object, Player> players;

	/**
	 * The constructor for the Server attempts to initialize a new server socket
	 * at the passed port. After the server socket is initialized, a thread is
	 * started to continually listen for connections from clients. Each time a
	 * client is connected, they are added to a list of clients and sent the
	 * current list of players and the map for the wumpus game. Then, a thread
	 * is started to listen for messages from that client.
	 * 
	 * @param htw
	 *            the hunt the wumpus game associated with the server
	 * @param port
	 *            the port to connect on
	 */
	public Server(int port, HuntTheWumpus htw) {
		new LinkedBlockingDeque<String>();
		clientManagers = new LinkedList<ClientManager>();
		removal = new LinkedBlockingQueue<ClientManager>();
		playerList = new ArrayList<Player>();
		map = htw.getMap();
		playerList.add(htw.getPlayer());
		players = new HashMap<Object, Player>();
		players.put(this, htw.getPlayer());

		try {
			// start listening on given port
			serverSocket = new ServerSocket(port);
			map = htw.getMap();
			clientCount = 0;
		} catch (BindException e) {
			String message = "Port already in use.";
			htw.setBadPort(true);
		} catch (IOException e) {
			String message = "Disconnected from network. Exiting.";
			htw.serverUnavailableProcessing(message + "NTWRK");
		} catch (Exception e) {
			String message = "Unexpected error occurred.";
			htw.serverUnavailableProcessing(message + "NTWRK");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// always wait for more connections. When a new client new connects,
		// add its thread to the list called clients, and start a new thread
		// for it.
		while (clientCount < 3) {
			Socket socket;
			try {
				socket = serverSocket.accept();
				clientCount++;
				ClientManager newManager = new ClientManager(socket);

				// send the other players the new client

				ArrayList<Player> temp = new ArrayList<Player>(players.values());
				newManager.send(map);
				newManager.send(temp);

				clientManagers.add(newManager);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * This method sends an object passed in the parameter to all of the
	 * clients.
	 * 
	 * @param obj
	 *            the object to send
	 */
	public void sendObject(Object obj) {

		for (ClientManager manager : clientManagers) {
			manager.send(obj);
		}
		setChanged();
		notifyObservers(obj);
	}

	/**
	 * Remove the desired player form the player list and client list.
	 * 
	 * @param p
	 *            player to be removed
	 */
	public void removePlayer(Player p) {
		Set<Object> temp = players.keySet();
		Iterator<Object> itr = temp.iterator();
		for (int i = 0; i < temp.size(); i++) {
			Object obj = itr.next();
			if (players.get(obj).equals(p)) {
				players.remove(obj);
				clientCount--;
				return;
			}
		}
		playerList.remove(p);

	}

	// every client gets its own object to be maintained in a list. Within this
	// object is a thread that handles all of the information that is received
	// on the input stream - which corresponds to an EpicClient's output stream.

	private class ClientManager {

		private Socket socket;
		private ObjectInputStream inStream;
		private ObjectOutputStream outStream;
		Thread getMessagesThread;
		private int count = 0;

		private ClientManager(Socket sock) {
			socket = sock;

			try {
				outStream = new ObjectOutputStream(socket.getOutputStream());
				inStream = new ObjectInputStream(socket.getInputStream());
				getMessagesThread = new Thread(new getThread());
				getMessagesThread.start();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		// this thread constantly waits for a new message from the client. When
		// such a message is received, it then has every other ClientManager to
		// send that message out to their clients.
		private class getThread implements Runnable {

			@Override
			public void run() {
				while (true) {
					try {
						Object obj = inStream.readObject();

						if (obj instanceof Player && count == 0) {
							Player p = (Player) obj;
							if (!playerList.contains((Player) obj)) {
								players.put(ClientManager.this, (Player) obj);
								playerList.add((Player) obj);
								count++;
								String s = p.getName() + " has connected";
								for (ClientManager manager : clientManagers) {
									if (!manager.equals(ClientManager.this))
										manager.send(s);
								}
								setChanged();
								notifyObservers(s);

							}
						}

						// sends the object to hunt the wumpus, so that hunt the
						// wumpus can do stuff with it
						setChanged();
						notifyObservers(obj);

						// send object to other clients
						for (ClientManager manager : clientManagers) {
							manager.send(obj);
						}

					} catch (IOException e) {
						try {
							ClientManager.this.socket.close();
							Player temp = players.remove(ClientManager.this);
							ArrayList<Object> list = new ArrayList<Object>();
							list.add(temp);
							list.add("remove");
							for (ClientManager manager : clientManagers) {
								manager.send(list);
								manager.send(temp.getName()
										+ " has disconnected");
							}
							setChanged();
							notifyObservers(list);
							setChanged();
							notifyObservers(temp.getName()
									+ " has disconnected");
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						break;
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}

					while (!removal.isEmpty()) {
						clientManagers.remove(removal.poll());
					}
				}
			}
		}

		public void send(Object obj) {

			try {
				outStream.writeObject(obj);
				outStream.reset();
			} catch (IOException e) {
				removal.add(ClientManager.this);
			}
		}
	}

}
