package gameModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import networking.Client;
import networking.Server;

/**
 * This class is the HuntTheWumpus game. It has three constructors, so it can
 * function as a single player game, a multiplayer server game, and a
 * multiplayer client game. If it is a multiplayer server game, then it observes
 * the server. This class extends Observable so that the view for the game can
 * observe it and receive updates.
 * 
 * @author Salika Dunatunga, Sarah Lutjens, Jane Wang, Denise Werchan
 * 
 */
public class HuntTheWumpus extends Observable implements Observer {

	private Player player;
	private WumpusMap map;
	private boolean gameOver, isClient, isServer, isPvP, wumpusDead,
			isConnected, badPort;
	private int itemCount, port, numPlayers;
	private Room[][][] rooms;
	private LinkedList<Player> players;
	private Server server;
	private Client client;
	private String IP;
	private int playerIsDead = 0;

	/**
	 * Creates a new HuntTheWumpus single player game which takes the difficulty
	 * level and the player name the user desires as arguments by creating a new
	 * WumpusMap, initializing the player list, and adding a single new player
	 * with the player name to the player list.
	 * 
	 * @param level
	 *            The difficulty of the Wumpus game as dictated by the user,
	 *            passed to the constructor as an int.
	 * @param shots
	 *            The number of shots the player receives
	 * @param name
	 *            The name the user wants to have identify his/her player,
	 *            passed as a string.
	 */
	public HuntTheWumpus(String name, int shots, int level) {
		Random rand = new Random();
		player = new Player(name, shots);
		players = new LinkedList<Player>();
		players.add(player);
		numPlayers = 1;
		map = new WumpusMap(level, false, false);
		int x = rand.nextInt(6);
		int y = rand.nextInt(7);
		itemCount = 0;
		isPvP = false;
		
		// generate x,y coords that are not occupied by any hazards to place the
		// player at
		while (map.getRoom(x, y, 1).hasWumpus()
				|| map.getRoom(x, y, 1).hasBat()
				|| map.getRoom(x, y, 1).isTunnel()
				|| map.getRoom(x, y, 1).isReverseTunnel()
				|| map.getRoom(x, y, 1).isPit()
				|| map.getRoom(x, y, 1).getHasBlood()
				|| map.getRoom(x, y, 1).hasSlime()) {
			x = rand.nextInt(6);
			y = rand.nextInt(7);
		}
		player.setLocation(new Point(x, y));
		player.setIsVisible(true);
		player.setCurrLevel(1);
		rooms = map.getMap();

		map.setIsVisited(x, y, true, 1);
		map.getRoom(x, y, 1).setIsVisited(true);
		gameOver = false;
		isServer = false;
		isClient = false;
	}

	/**
	 * Creates a new HuntTheWumpus multiplayer client-side game which takes the
	 * difficulty level, a player name, number of shots, and a WumpusMap as
	 * arguments. This constructor initializes the player list, and adds a
	 * single new player with the player name to the player list.
	 * 
	 * @param port
	 *            The port to connect on
	 * @param IP
	 *            The IP address to connect to
	 * @param shots
	 *            The number of shots the player receives
	 * @param name
	 *            The name the user wants to have identify his/her player,
	 *            passed as a string.
	 */
	public HuntTheWumpus(String name, int shots, int port, String IP) {
		player = new Player(name, 1);
		for (int i = 0; i < shots - 1; i++) {
			Silencer s = new Silencer();
			player.addItem(s);
		}
		player.setIsVisible(true);
		players = new LinkedList<Player>();
		players.add(player);
		numPlayers = 1;
		this.port = port;
		this.IP = IP;
	}

	/**
	 * Sets up the client for the multiplayer game.
	 */
	public void setUpClient() {
		client = new Client(IP, port, this);
		client.addObserver(this);
		isClient = true;
		isServer = false;
		itemCount = 0;

		if (client.isConnected()) {
			// initially upon connecting to a server, the server will send the
			// map, then the number of players, then the players themselves
			map = (WumpusMap) client.receiveObject();
			rooms = map.getMap();

			for (int k = 0; k < 3; k++) {
				for (int i = 0; i < rooms[k].length; i++) {
					for (int j = 0; j < rooms[k][0].length; j++) {
						rooms[k][i][j].setIsVisited(false);
						rooms[k][i][j].setIsVisitedL(false);
						rooms[k][i][j].setIsVisitedR(false);
					}
				}
			}
			Random rand = new Random();
			int x = rand.nextInt(6);
			int y = rand.nextInt(7);

			// generate x,y coords that are not occupied by any hazards to place
			// the player at
			while (map.getRoom(x, y, 1).hasWumpus()
					|| map.getRoom(x, y, 1).hasBat()
					|| map.getRoom(x, y, 1).isTunnel()
					|| map.getRoom(x, y, 1).isReverseTunnel()
					|| map.getRoom(x, y, 1).isPit()
					|| map.getRoom(x, y, 1).getHasBlood()
					|| map.getRoom(x, y, 1).hasSlime()) {
				x = rand.nextInt(6);
				y = rand.nextInt(7);
			}

			player.setCurrLevel(1);
			
			map.setIsVisited(x, y, true, 1);
			map.getRoom(x, y, 1).setIsVisited(true);
			this.isPvP = map.getPvP();
			player.setLocation(new Point(x, y));
			ArrayList<Player> newPlayers = (ArrayList<Player>) client
					.receiveObject();
			
			for (int i = 0; i < newPlayers.size(); i++) {
				if (!newPlayers.get(i).isDead()
						&& !players.contains(newPlayers.get(i))) {
					players.add(newPlayers.get(i));
					numPlayers++;
					// check if the player added should be visible or not
					if (newPlayers.get(i).getLocation()
							.compareTo(player.getLocation())
							&& newPlayers.get(i).getCurrLevel() == player
									.getCurrLevel())
						newPlayers.get(i).setIsVisible(true);
					else
						newPlayers.get(i).setIsVisible(false);
				}
			}
			client.writeObject(player);

			Thread clientReceivingThread = new Thread(new ClientReceiveThread());
			clientReceivingThread.start();

			gameOver = false;
			isConnected = true;
		} else {
			isConnected = false;
		}
	}

	/**
	 * Returns if the game client is connected to a multiplayer game.
	 * 
	 * @return a boolean identifying the connection state of the client.
	 */
	public boolean getIsConnected() {
		return isConnected;
	}

	/**
	 * Creates a new HuntTheWumpus multiplayer server-side game which takes the
	 * difficulty level, a player name, number of shots, and a WumpusMap as
	 * arguments. This constructor initializes the player list, and adds a
	 * single new player with the player name to the player list.
	 * 
	 * @param port
	 *            The port to listen on
	 * @param level
	 *            The difficulty of the Wumpus game as dictated by the user,
	 *            passed to the constructor as an int.
	 * @param shots
	 *            The number of shots the player receives
	 * @param name
	 *            The name the user wants to have identify his/her player,
	 *            passed as a string.
	 * @param PvP
	 * 			true if it is a player-vs-player game, else false 
	 */
	public HuntTheWumpus(String name, int shots, int level, int port,
			boolean PvP) {
		this.isPvP = PvP;
		// create new player
		player = new Player(name, 1);
		player = new Player(name, 1);
		for (int i = 0; i < shots - 1; i++) {
			Silencer s = new Silencer();
			player.addItem(s);
		}
		// set player to visible
		player.setIsVisible(true);

		players = new LinkedList<Player>();

		// add player to player list
		players.add(player);
		numPlayers = 1;

		map = new WumpusMap(level, true, PvP);
		rooms = map.getMap();
		Random rand = new Random();
		int x = rand.nextInt(6);
		int y = rand.nextInt(7);
		itemCount = 0;

		// generate x,y coords that are not occupied by any hazards to place the
		// player at
		while (map.getRoom(x, y, 1).hasWumpus()
				|| map.getRoom(x, y, 1).hasBat()
				|| map.getRoom(x, y, 1).isTunnel()
				|| map.getRoom(x, y, 1).isReverseTunnel()
				|| map.getRoom(x, y, 1).isPit()
				|| map.getRoom(x, y, 1).getHasBlood()
				|| map.getRoom(x, y, 1).hasSlime()) {
			x = rand.nextInt(6);
			y = rand.nextInt(7);
		}

		player.setCurrLevel(1);
		// map.setCurrLevel(currLevelNum);
		player.setLocation(new Point(x, y));
		// currLevel = rooms[currLevelNum];

		map.setIsVisited(x, y, true, 1);
		map.getRoom(x, y, 1).setIsVisited(true);

		// create the server, since this is a multiplayer server constructor
		server = new Server(port, this);
		isServer = true;
		isClient = false;
		this.port = port;

		// add this HTW to the server's list of observers
		server.addObserver(this);

		// create new thread to receive new connections
		Thread serverThread = new Thread(server);

		// start the thread
		serverThread.start();
	}

	/**
	 * Creates a new HuntTheWumpus single player game and takes the information
	 * from a saved game, the player and the map, as its arguments.
	 * 
	 * @param savedPlayer
	 *            The Player that is loaded from the saved game file.
	 * @param savedMap
	 *            The saved WumpusMap that is loaded from the saved game file.
	 * @param isClient
	 * 			  true if the user is a client, else false 
	 * @param port 
	 * 			  an int representing the port
	 * @param IP 
	 * 			  a string representing the IP address
	 * @param PvP 
	 * 			  true if it is a player-vs-player game, else false
	 */
	public HuntTheWumpus(Player savedPlayer, WumpusMap savedMap,
			boolean isClient, int port, String IP, boolean PvP) {
		this.isPvP = PvP;
		player = savedPlayer;
		players = new LinkedList<Player>();
		numPlayers = players.size();
		players.add(player);
		map = savedMap;
		rooms = map.getMap();
		itemCount = 0;
		gameOver = false;
		isServer = false;
		this.isClient = isClient;
		if (isClient) {
			// initially upon connecting to a server, the server will send the
			// map, then the number of players, then the players themselves;
			// these must be tossed since the info is from the saved game

			WumpusMap toss = (WumpusMap) client.receiveObject();
			LinkedList<Player> toss2 = (LinkedList<Player>) client
					.receiveObject();

			Thread clientReceivingThread = new Thread(new ClientReceiveThread());
			clientReceivingThread.start();
			gameOver = false;
		}
	}

	/**
	 * Creates a new HuntTheWumpus multiplayer player game and takes the
	 * information from a saved game, the player list and the map, as its
	 * arguments.
	 * 
	 * @param savedPlayers
	 *            The linked list of Player objects that is loaded from the
	 *            saved game file.
	 * @param savedMap
	 *            The saved WumpusMap that is loaded from the saved game file.
	 * @param port 
	 * 			  an int representing the port
	 * @param PvP 
	 * 			  true if it is a player-vs-player game, else false
	 */
	public HuntTheWumpus(LinkedList<Player> savedPlayers, WumpusMap savedMap,
			int port, boolean PvP) {
		player = savedPlayers.get(0);
		players = savedPlayers;
		numPlayers = players.size();
		map = savedMap;
		rooms = map.getMap();
		gameOver = false;
		itemCount = 0;
		this.isPvP = PvP;

		// create the server, since this is a multiplayer server loader
		server = new Server(port, this);

		isServer = true;
		isClient = false;

		// add this HTW to the server's list of observers
		server.addObserver(this);

		// create new thread to receive new connections
		Thread serverThread = new Thread(server);

		// start the thread
		serverThread.start();

		// check the players that are trying to connect to see if they were in
		// the saved game

	}



	/**
	 * Get the list of players present in the game.
	 * 
	 * @return The player list as a List of Player objects.
	 */
	public List<Player> getPlayerList() {
		return players;
	}

	private void regenerateItem() {
		Item i;
		double randNum = Math.random();
		if (randNum > .3) {
			i = new Food();
		} else if (randNum > .1) {
			i = new TeleportBlocker();
		} else {
			i = new Silencer();
		}
		Random rand = new Random();
		int x = rand.nextInt(6);
		int y = rand.nextInt(7);
		while (rooms[player.getCurrLevel()][x][y].isPit()
				|| rooms[player.getCurrLevel()][x][y].hasWumpus()
				|| rooms[player.getCurrLevel()][x][y].hasItem()) {
			x = rand.nextInt(6);
			y = rand.nextInt(7);
		}

		i.setLocation(new Point(x, y));
		i.setLevel(player.getCurrLevel());
		Point p;

		if (rooms[player.getCurrLevel()][x][y].isTunnel()) {
			if (Math.random() > .5)
				p = new Point(0, 2);
			else
				p = new Point(2, 0);
		}

		else if (rooms[player.getCurrLevel()][x][y].isReverseTunnel()) {
			if (Math.random() > .5)
				p = new Point(0, 1);
			else
				p = new Point(3, 2);
		} else {
			if (Math.random() > .5)
				p = new Point(0, 2);
			else
				p = new Point(2, 0);
		}

		i.setLeeway(p);
		rooms[player.getCurrLevel()][x][y].setItem(true);
		rooms[player.getCurrLevel()][x][y].addItem(i);
	}

	/**
	 * Identifies the observers of this game that a client was unable to reach
	 * the server by sending an identifying string.
	 * 
	 * @param s
	 *            the String sent from the client class that identifies the
	 *            server was not reached for whatever the reason.
	 */
	public void serverUnavailableProcessing(String s) {
		setChanged();
		notifyObservers(s);
	}

	/**
	 * Sets the a boolean expression to the desired value.
	 * 
	 * @param b
	 *            the boolean to which the private boolean should be set
	 */
	public void setBadPort(boolean b) {
		badPort = b;
	}

	/**
	 * Returns if the port given by the server is bad or not. A bad port is one
	 * that is already in use by another application.
	 * 
	 * @return a boolean identifying the given port as bad or not
	 */
	public boolean isBadPort() {
		return badPort;
	}

	/**
	 * This method returns a clone of the 3D array of Rooms in this map
	 * 
	 * @return a clone of the 3D array of Rooms
	 */
	public Room[][][] getRooms() {
		return rooms.clone();
	}

	/**
	 * Returns the 2D array of Rooms that comprises the current level of the
	 * player.
	 * 
	 * @return the 2D array of Rooms of the current level of the player
	 */
	public Room[][] getCurrLevel() {
		return rooms[player.getCurrLevel()];
	}

	/**
	 * Returns the WumpusMap created with the creation of the game.
	 * 
	 * @return The map created when creating the game object as a WumpusMap.
	 */
	public WumpusMap getMap() {
		return map;
	}

	/**
	 * Return the player that was created with this instance of the game. This
	 * is the player object the user controls.
	 * 
	 * @return The player object that was created upon creation of the game.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * This method returns the current ending boolean state of the game
	 * 
	 * @return true if the game is over, and false if it is not over
	 */
	public boolean isOver() {
		return gameOver;
	}

	/**
	 * Returns the PvP status of the game.
	 * 
	 * @return a boolean identifying the game as PvP or not
	 */
	public boolean isPvP() {
		return isPvP;
	}

	/**
	 * This method shoots an arrow in the direction specified in the method
	 * parameter, by calling WumpusMap's shoot method. If the arrow lands in the
	 * same square as the wumpus, then the player wins the game. If it does not,
	 * and if the player does not have any shots left, then the player is eaten
	 * by the wumpus and the wumpus moves to the players location.
	 * 
	 * @param d
	 *            the direction to shoot the arrow in
	 */
	public void shoot(Direction d) {
		Point p = map.shoot(player.getLocation(), player.getPrevLocation(), d,
				player.getCurrLevel());
		player.useShot();

		if (map.getRoom(p.getXCoor(), p.getYCoor(), player.getCurrLevel())
				.hasWumpus()) {
			if (isPvP) {
				player.setKilledWumpus();
				player.setHasWon();
				gameOver = true;
			} else {
				wumpusDead = true;
				player.setKilledWumpus();
				player.setHasWon();
				gameOver = true;
			}

			if (isServer) {
				server.sendObject(player);
				return;
			}

			else if (isClient) {
				client.writeObject(player);
				return;
			} else {
				setChanged();
				notifyObservers(player);
			}
			return;

		}

		if (isPvP) {
			for (int i = 1; i < players.size(); i++) {
				if (players.get(i).getLocation().compareTo(p)) {
					players.get(i).setDeathByPlayer();
					players.get(i).setDeathByWhichPlayer(player);
					numPlayers--;
					if (isServer) {
						server.sendObject(players.get(i));
					}

					else if (isClient) {
						client.writeObject(players.get(i));
					}
					if (wumpusDead) {
						player.setHasWon();
						gameOver = true;

						if (isServer) {
							server.sendObject(player);
							return;
						}

						else if (isClient) {
							client.writeObject(player);
							return;
						}
					}
				}
			}
		}

		if (player.getNumShots() == 0) {
			player.kill();
			player.setDeathByWumpus();
			gameOver = true;
			
			if (isServer) {
				server.sendObject(player);
				return;
			}

			else if (isClient) {
				client.writeObject(player);
				return;
			} else {
				setChanged();
				notifyObservers(player);
			}
		}
	}

	/*
	 * private helper method to check if user moved into an item. If they did,
	 * it is added to the player's inventory and observers are notified that the
	 * item should be undrawn.
	 */
	private void checkMoveIntoItem() {
		if (rooms[player.getCurrLevel()][player.getLocation().getXCoor()][player
				.getLocation().getYCoor()].hasItem()) {
			if (rooms[player.getCurrLevel()][player.getLocation().getXCoor()][player
					.getLocation().getYCoor()].getItemLeeway().compareTo(
					player.getLeeway())) {
				itemCount++;
				Item item = rooms[player.getCurrLevel()][player.getLocation()
						.getXCoor()][player.getLocation().getYCoor()]
						.removeItem();
				player.addItem(item);
				if (itemCount == 2) {
					itemCount = 0;
					regenerateItem();
				}
				if (isServer) {
					server.sendObject(item);
				} else if (isClient) {
					client.writeObject(item);
				} else {
					setChanged();
					notifyObservers(item);
				}
			}
		}
	}

	/*
	 * private helper method to determine if the player moved into a wumpus
	 */
	private void checkMoveIntoWumpus() {
		// check if they moved into a wumpus
		if (player.getLocation().compareTo(map.getWumpus()) == true
				&& player.getCurrLevel() == map.getWumpusLevel()) {
			player.kill();
			player.setDeathByWumpus();
			gameOver = true;

			if (isServer && playerIsDead==0) {
				server.sendObject(player);
				playerIsDead++;
				processChat(player.getName() + " was killed by the wumpus. " + player.getName() + " is dead.");
				return;
			}

			else if (isClient && playerIsDead==0) {
				client.writeObject(player);
				playerIsDead++;
				processChat(player.getName() + " was killed by the wumpus. " + player.getName() + " is dead.");
				return;
			} else {
				if (playerIsDead==0) {
					playerIsDead++;
					setChanged();
					notifyObservers(player);
				}
				
			}

		}
	}

	/*
	 * private helper method to determine if the player moved into a pit
	 */
	private void checkMoveIntoPit() {
		// check if they moved into a pit
		if (map.getRoom(player.getLocation().getXCoor(),
				player.getLocation().getYCoor(), player.getCurrLevel()).isPit()) {
			if (player.getCurrLevel() == 0) {
				player.kill();
				player.setDeathByPit();
				gameOver = true;
			} else {
				player.setCurrLevel(player.getCurrLevel() - 1);
				if (rooms[player.getCurrLevel()][player.getLocation()
						.getXCoor()][player.getLocation().getYCoor()]
						.isTunnel()) {
					player.setLeeway(new Point(1, 2));
					rooms[player.getCurrLevel()][player.getLocation()
							.getXCoor()][player.getLocation().getYCoor()]
							.setIsVisitedR(true);
				} else if (rooms[player.getCurrLevel()][player.getLocation()
						.getXCoor()][player.getLocation().getYCoor()]
						.isReverseTunnel()) {
					player.setLeeway(new Point(1, 1));
					rooms[player.getCurrLevel()][player.getLocation()
							.getXCoor()][player.getLocation().getYCoor()]
							.setIsVisitedL(true);
				} else
					rooms[player.getCurrLevel()][player.getLocation()
							.getXCoor()][player.getLocation().getYCoor()]
							.setIsVisited(true);
				move(player, player.getLocation(), player.getLeeway(),
						player.getCurrLevel());
			}
			
			if (player.isDead()) {
				
				if (isServer && playerIsDead==0) {
					server.sendObject(player);
					processChat(player.getName() + " fell into a pit. " + player.getName() + " is dead.");
					playerIsDead++;
					return;
				}

				else if (isClient && playerIsDead==0) {
					client.writeObject(player);
					processChat(player.getName() + " fell into a pit. " + player.getName() + " is dead.");
					playerIsDead++;
					return;
				} else {
					if (playerIsDead==0) {
						playerIsDead++;
						setChanged();
						notifyObservers(player);
					}
					
				}
			}
			else {
				
				if (isServer) {
					server.sendObject(player);
					return;
				}

				else if (isClient) {
					client.writeObject(player);
					return;
				} else {
					setChanged();
					notifyObservers(player);					
				}
				
			}
			

		}
	}

	/*
	 * private helper method to determine if they player moved into a bat. If
	 * the player did move into a bat, and they are not using a teleporter
	 * blocker, then there is a 50% chance they will be carried into a random
	 * room. After the player uses a teleporter blocker, it is removed from
	 * their inventory.
	 */
	private void checkMoveIntoBat() {
		// check if they moved into a bat
		if (rooms[player.getCurrLevel()][player.getLocation().getXCoor()][player
				.getLocation().getYCoor()].hasBat()) {
			// move the player/bat
			if (Math.random() > .5 && player.getTeleportBlock() == false) {
				setChanged();
				notifyObservers("MovedByBat");
				rooms[player.getCurrLevel()][player.getLocation().getXCoor()][player
						.getLocation().getYCoor()].setBat(false);
				Random rand = new Random();
				int x = rand.nextInt(6), y = rand.nextInt(7);
				while (rooms[player.getCurrLevel()][x][y].isTunnel()
						|| rooms[player.getCurrLevel()][x][y].isReverseTunnel()) {
					x = rand.nextInt(6);
					y = rand.nextInt(7);
				}
				player.setLocation(new Point(x, y));
				player.setPrevLocation(new Point(x, y));
				rooms[player.getCurrLevel()][x][y].setBat(true);
				rooms[player.getCurrLevel()][x][y].setIsVisited(true);
			}
			player.setTeleportBlock(false);

			if (isServer) {
				server.sendObject(player);
				return;
			}

			else if (isClient) {
				client.writeObject(player);
				return;
			} else {
				setChanged();
				notifyObservers(player);
			}
		}
	}

	/**
	 * A method which sends the player to the next level up if there is a ladder
	 * in the room.
	 */
	public void climbLadder() {
		if (rooms[player.getCurrLevel()][player.getLocation().getXCoor()][player
				.getLocation().getYCoor()].hasLadder()) {
			player.setCurrLevel(player.getCurrLevel() + 1);
			if (rooms[player.getCurrLevel()][player.getLocation().getXCoor()][player
					.getLocation().getYCoor()].isTunnel()) {
				player.setLeeway(new Point(1, 2));
				rooms[player.getCurrLevel()][player.getLocation().getXCoor()][player
						.getLocation().getYCoor()].setIsVisitedR(true);
			} else if (rooms[player.getCurrLevel()][player.getLocation()
					.getXCoor()][player.getLocation().getYCoor()]
					.isReverseTunnel()) {
				player.setLeeway(new Point(1, 1));
				rooms[player.getCurrLevel()][player.getLocation().getXCoor()][player
						.getLocation().getYCoor()].setIsVisitedL(true);
			} else
				rooms[player.getCurrLevel()][player.getLocation().getXCoor()][player
						.getLocation().getYCoor()].setIsVisited(true);
			checkMoveIntoBat();
			if (isServer) {
				server.sendObject(player);
				return;
			}

			else if (isClient) {
				client.writeObject(player);
				return;
			} else {
				setChanged();
				notifyObservers(player);
			}
		}
	}


	private class ClientReceiveThread implements Runnable {

		@Override
		public void run() {
			while (true) {
				Object obj = client.receiveObject();

				// if it's a string then it's a chat so send it to observers
				if (obj instanceof String) {
					setChanged();
					notifyObservers((String) obj);
				}

				else if (obj instanceof ArrayList) {
					ArrayList<Object> temp = (ArrayList) obj;
					Player p = (Player) temp.get(0);
					for (int i = 0; i < players.size(); i++) {
						if (players.get(i).equals(p)) {
							players.remove(i);
						}
					}
				}

				else if (obj instanceof Item) {
					Item item = (Item) obj;
					Point temp = item.getLocation();
					rooms[item.getLevel()][temp.getXCoor()][temp.getYCoor()]
							.removeItem();
					setChanged();
					notifyObservers(obj);
				}

				else if (obj instanceof Player) {
					Player p = (Player) obj;

					// new player joined the game, add them to list of players
					boolean contains = false;
					for (int i = 0; i < players.size(); i++) {
						if (players.get(i).equals(p))
							contains = true;
					}

					if (!contains) {
						if (p.getLocation().compareTo(player.getLocation())
								&& p.getCurrLevel() == player.getCurrLevel())
							p.setIsVisible(true);
						else
							p.setIsVisible(false);
						players.add(p);
						numPlayers++;
					}

					else if (p.isDead()) {
						if (p.equals(player) && p.deathByPlayer()) {
							player.setDeathByPlayer();
							player.setDeathByWhichPlayer(p
									.killedByWhichPlayer());
							gameOver = true;
							setChanged();
							notifyObservers(player);
							return;
						}
						for (int i = 0; i < players.size(); i++) {
							if (players.get(i).equals(p)) {
								players.get(i).kill();
								numPlayers--;
								Player temp = players.get(i);
								setChanged();
								notifyObservers(temp);
								return;
							}
						}

					} else if (p.hasWon()) {
						for (int i = 0; i < players.size(); i++) {
							if (players.get(i).equals(p)) {
								players.get(i).setHasWon();
								gameOver = true;
								setChanged();
								notifyObservers(players.get(i));
							}
						}
					} else if (!p.equals(player)) {
						move(p, p.getLocation(), p.getLeeway(),
								p.getCurrLevel());
					}
				}

			}
		}

	}

	/**
	 * Moves the desired player to a new location and leeway from the previous
	 * location and leeway.
	 * 
	 * @param p
	 *            the Player object to be moved
	 * @param location
	 *            a Point that identifies the location of the player
	 * @param leeway
	 *            a Point that identifies the leeway of the player
	 * @param level
	 * 			  the level that the player is on
	 */
	public void move(Player p, Point location, Point leeway, int level) {
		Point prev = player.getLocation();
		int prevLevel = player.getCurrLevel();
		p.setCurrLevel(level);
		boolean checkBats = false;
		// only check for bats if the room has been visited
		if (rooms[player.getCurrLevel()][location.getXCoor()][location
				.getYCoor()].isVisited())
			checkBats = true;
		// set visited for tunnels/rooms for current player
		if (p.equals(player)) {
			if (rooms[player.getCurrLevel()][location.getXCoor()][location
					.getYCoor()].isTunnel()) {
				if (p.getLeeway().compareTo(new Point(2, 0))
						|| p.getLeeway().compareTo(new Point(3, 1))) {
					rooms[player.getCurrLevel()][location.getXCoor()][location
							.getYCoor()].setIsVisitedL(true);
				}
				if (p.getLeeway().compareTo(new Point(1, 3))
						|| p.getLeeway().compareTo(new Point(0, 2))) {
					rooms[player.getCurrLevel()][location.getXCoor()][location
							.getYCoor()].setIsVisitedR(true);
				}

			} else if (rooms[player.getCurrLevel()][location.getXCoor()][location
					.getYCoor()].isReverseTunnel()) {
				if (p.getLeeway().compareTo(new Point(1, 0))
						|| p.getLeeway().compareTo(new Point(0, 1))) {
					rooms[player.getCurrLevel()][location.getXCoor()][location
							.getYCoor()].setIsVisitedL(true);
				}
				if (p.getLeeway().compareTo(new Point(2, 3))
						|| p.getLeeway().compareTo(new Point(3, 2))) {
					rooms[player.getCurrLevel()][location.getXCoor()][location
							.getYCoor()].setIsVisitedR(true);
				}
			} else {
				rooms[player.getCurrLevel()][location.getXCoor()][location
						.getYCoor()].setIsVisited(true);
			}

			for (int i = 1; i < players.size(); i++) {
				Point temp = players.get(i).getLocation();
				// check if the player should be visible
				if (player.getCurrLevel() == players.get(i).getCurrLevel()
						&& (rooms[player.getCurrLevel()][temp.getXCoor()][temp
								.getYCoor()].isVisited()
								|| rooms[player.getCurrLevel()][temp.getXCoor()][temp
										.getYCoor()].isVisitedR() || rooms[player
								.getCurrLevel()][temp.getXCoor()][temp
								.getYCoor()].isVisitedL())) {
					players.get(i).setIsVisible(true);
				} else {
					players.get(i).setIsVisible(false);
				}
			}
			player.setLocation(location);
			player.setLeeway(leeway);
			player.setCurrLevel(level);

			checkMoveIntoItem();
			checkMoveIntoPit();
			checkMoveIntoWumpus();
			// if they moved to a new square, check for bats
			if (!location.compareTo(prev) && checkBats)
				checkMoveIntoBat();

			// check if out of energy
			if (player.getEnergy() < 1) {
				player.kill();
				gameOver = true;
				if (isServer && playerIsDead==0) {
					server.sendObject(player);
					playerIsDead++;
					processChat(player.getName() + " starved to death. " + player.getName() + " is dead.");
					return;
				}

				else if (isClient && playerIsDead==0) {
					client.writeObject(player);
					playerIsDead++;
					processChat(player.getName() + " starved to death. " + player.getName() + " is dead.");
					return;
				} else {
					if (playerIsDead==0) {
						playerIsDead++;
						setChanged();
						notifyObservers(player);
					}
					
				}
			}
			
			else {
				if (isServer) {
					server.sendObject(player);
				}

				else if (isClient) {
					client.writeObject(player);
				}
				setChanged();
				notifyObservers(player);
			}


		}

		else {
			int i;
			for (i = 0; i < players.size(); i++) {
				if (players.get(i).equals(p)) {
					players.set(i, p);
					// check if the player should be visible
					if ((players.get(i).getCurrLevel() == player.getCurrLevel())
							&& (rooms[player.getCurrLevel()][location
									.getXCoor()][location.getYCoor()]
									.isVisited()
									|| rooms[player.getCurrLevel()][location
											.getXCoor()][location.getYCoor()]
											.isVisitedR() || rooms[player
									.getCurrLevel()][location.getXCoor()][location
									.getYCoor()].isVisitedL())) {
						players.get(i).setIsVisible(true);
					} else {
						players.get(i).setIsVisible(false);
					}
					setChanged();
					notifyObservers(players.get(i));
					break;
				}
			}

		}
	}

	/**
	 * In the multiplayer version of the game, this method is called to process
	 * a chat message. If the player is a client, the string is sent to the
	 * server so that the server can send it to the other clients. If the player
	 * is the server, they send it out to the other clients.
	 * 
	 * @param text
	 *            the string to send to the other players on the network
	 */
	public void processChat(String text) {
		if (isServer) {
			server.sendObject(text);
		}
		if (isClient) {
			client.writeObject(text);
		}
	}

	/**
	 * Save the current game of HuntTheWumpus by saving the player and wumpus
	 * map data into a uniquely named file.
	 */
	public void saveGame() {
		File folder = new File("SavedGames/");

		try {
			if (!folder.exists())
				folder.mkdir();
		} catch (Exception e) {
			e.printStackTrace();
		}

		File playerFile = new File("SavedGames/" + dateTime()
				+ removeNameSpaces() + ".htwdat");

		try {
			if (!playerFile.exists()) // write into file (if file does not
										// exist, create a new one)
				playerFile.createNewFile();

			FileOutputStream fileStream = new FileOutputStream(playerFile);
			ObjectOutputStream objStream = new ObjectOutputStream(fileStream);
			if (isServer)
				objStream.writeObject(players); // write the player list for a
												// network game
			else
				objStream.writeObject(player); // write the player and the map
												// to
												// the file
			objStream.writeObject(map);

			if (isClient || isServer) {
				objStream.writeInt(port);
				objStream.writeBoolean(isPvP);
				if (isClient)
					objStream.writeObject(IP);
			}

			objStream.flush();
			objStream.close();
		} catch (FileNotFoundException f) {
			f.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Private helper method used to remove the spaces from a name while saving
	 * the game.
	 */
	private String removeNameSpaces() {
		return player.getName().replace(" ", "_");
	}

	/*
	 * Private helper method used to format the date-time section for the name
	 * of the saved game.
	 */
	private String dateTime() {
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance();
		if (isClient)
			return "_XT" + df.format(cal.getTime());
		else if (isServer)
			return "_ZT" + df.format(cal.getTime());
		else
			return "_DT" + df.format(cal.getTime());
	}

	/*
	 * This is only for the server!!! HTW observes the server, and when the
	 * server receives stuff from a client it notifies HTW so that HTW can do
	 * the necessary processing for it.
	 */
	/**
	 * Only used by the server to receive items from a client and notify the
	 * game so that the necessary processing can be done.
	 */
	public void update(Observable arg0, Object obj) {

		// if it's a string then it's a chat so send it to observers

		if (obj instanceof String) {
			String s = (String) obj;
			setChanged();
			notifyObservers((String) obj);
		}

		else if (obj instanceof ArrayList) {
			ArrayList<Object> temp = (ArrayList) obj;
			Player p = (Player) temp.get(0);
		}

		else if (obj instanceof Item) {
			Item item = (Item) obj;
			Point temp = item.getLocation();
			rooms[item.getLevel()][temp.getXCoor()][temp.getYCoor()]
					.removeItem();
			setChanged();
			notifyObservers(obj);
		}

		else if (obj instanceof Player) {
			Player p = (Player) obj;
			// new player joined the game, add them to list of players
			boolean contains = false;
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i).equals(p))
					contains = true;
			}

			if (!contains) {
				if (p.getLocation().compareTo(player.getLocation())
						&& p.getCurrLevel() == player.getCurrLevel())
					p.setIsVisible(true);
				else
					p.setIsVisible(false);
				players.add(p);
				numPlayers++;
			} else if (p.isDead()) {
				if (p.equals(player) && p.deathByPlayer()) {
					player.setDeathByPlayer();
					player.setDeathByWhichPlayer(p.killedByWhichPlayer());
					gameOver = true;
					setChanged();
					notifyObservers(player);
					return;
				}
				for (int i = 0; i < players.size(); i++) {
					if (players.get(i).equals(p)) {
						players.get(i).kill();
						numPlayers--;
						Player temp = players.get(i);
						server.removePlayer(temp);
						setChanged();
						notifyObservers(temp);
					}
				}

			} else if (p.hasWon()) {
				for (int i = 0; i < players.size(); i++) {
					if (players.get(i).equals(p)) {
						players.get(i).setHasWon();
						gameOver = true;
						setChanged();
						notifyObservers(players.get(i));
					}
				}
			} else if (!p.equals(player)) {
				move(p, p.getLocation(), p.getLeeway(), p.getCurrLevel());
			}

		}

	}
}