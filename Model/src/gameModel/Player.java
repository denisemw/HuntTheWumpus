package gameModel;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Random;

/**
 * This class simulates a player object. Each player has a location and a
 * leeway, represented by Points. Each player also has an energy level
 * represented by an integer from 0 to 100. Each player has a number of shots, a
 * name, and an inventory of items they have collected.
 * 
 * @author Salika Dunatunga, Sarah Lutjens, Jane Wang, Denise Werchan
 * 
 */
public class Player implements Serializable {

	private static final long serialVersionUID = 5852038047106417677L;
	private boolean isDead, deathByWumpus, deathByPit, deathByPlayer, killedWumpus;
	private Player playerKilledBy;
	private Point location;
	private Point prevLocation;
	private Point leeway;
	private int numShots;
	private String name;
	private Integer energy, currLevel;
	private boolean teleportBlock;
	private LinkedList<Item> inventory;
	private boolean isVisible, hasWon;
	private Direction lastMove;
	private RoomNavigator roomNav;

	private long id;

	/**
	 * Creates a new player object. Initializes the name and shots to the passed
	 * parameters. Initializes the location and prevLocation to null, the leeway
	 * to a new Point at (2,2), the energy to 100, and the inventory to a new
	 * linked list. The isDead, teleportBlock, and isVisible variables are
	 * initialized to false.
	 * 
	 * @param name
	 *            the name of the player
	 * @param shots
	 *            the number of shots for the player's arrow
	 */
	public Player(String name, int shots) {
		isDead = false;
		deathByWumpus = false;
		deathByPit = false;
		deathByPlayer = false;
		playerKilledBy = null;
		location = null;
		prevLocation = null;
		leeway = new Point(2, 2);
		this.name = name;
		numShots = shots;
		energy = 100;
		teleportBlock = false;
		inventory = new LinkedList<Item>();
		isVisible = false;
		hasWon = false;
		Random rand = new Random();
		id = rand.nextLong();
		currLevel = 0;
		killedWumpus = false;
		lastMove = Direction.UP;
		roomNav = new RoomNavigator(this);
	
	}
	
	/**
	 * Changes the killedWumpus variable to true
	 */
	public void setKilledWumpus() {
		killedWumpus = true;
	}
	
	/**
	 * @return
	 * 		true if the player killed the wumpus, else false
	 */
	public boolean getKilledWumpus() {
		return killedWumpus;
	}
	
	/**
	 * Sets the current level of the player
	 * 
	 * @param level
	 * 			the level to set the player at
	 */
	public void setCurrLevel(int level) {
		currLevel = level;
	}
	
	/**
	 * Returns the players current level
	 * 
	 * @return
	 * 		an int representing the player's level
	 */
	public int getCurrLevel() {
		return currLevel;
	}

	/**
	 * Sets the player's killer to the specified player object.
	 * 
	 * @param p
	 *            The player which killed this player object.
	 */
	public void setDeathByWhichPlayer(Player p) {
		playerKilledBy = p;
	}

	/**
	 * Return the player object who has killed this player.
	 * 
	 * @return The player who has killed this player object.
	 */
	public Player killedByWhichPlayer() {
		return playerKilledBy;
	}

	/**
	 * Set that this player has been killed by a player.
	 */
	public void setDeathByPlayer() {
		deathByPlayer = true;
		isDead = true;
	}

	/**
	 * Return if this player has been killed by another player (return true) or
	 * if not (return false).
	 * 
	 * @return A boolean identifying if the player has been killed by another
	 *         player.
	 */
	public boolean deathByPlayer() {
		return deathByPlayer;
	}

	/**
	 * Set that this player has been killed by a pit.
	 */
	public void setDeathByPit() {
		deathByPit = true;
		isDead = true;
	}

	/**
	 * Return if this player has been killed by a pit (true) or not (false).
	 * 
	 * @return A boolean identifying if the player has been killed by a pit.
	 */
	public boolean deathByPit() {
		return deathByPit;
	}

	/**
	 * Set that this player has been killed by the wumpus
	 */
	public void setDeathByWumpus() {
		deathByWumpus = true;
		isDead = true;
	}

	/**
	 * Return if the player was killed by the wumpus (true) or not (false).
	 * 
	 * @return A boolean identifying if the player has been killed by the
	 *         wumpus.
	 */
	public boolean deathByWumpus() {
		return deathByWumpus;
	}

	/**
	 * Sets the visibility of the player
	 * 
	 * @param isVisible
	 *            true if the player should be visible, else false if the player
	 *            should not be
	 */
	public void setIsVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	/**
	 * Return the ID of this player.
	 * 
	 * @return ID of the player as a long.
	 */
	public long getID() {
		return id;
	}

	/**
	 * This method is called if this player wins the game by killing the wumpus.
	 * This method will change the player's hasWon variable to true.
	 */
	public void setHasWon() {
		hasWon = true;
	}

	/**
	 * This method returns true if this player has won the game by killing the
	 * wumpus. Else, this method will return false.
	 * 
	 * @return true if the player has won, else false
	 */
	public boolean hasWon() {
		return hasWon;
	}

	/**
	 * This method returns the players energy level
	 * 
	 * @return an integer representing the player's energy
	 */
	public int getEnergy() {
		return energy;
	}
	
	/**
	 * Returns the visibility of the player
	 * 
	 * @return returns the isVisible variable
	 */
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * Adds the passed item to the player's inventory
	 * 
	 * @param i
	 *            the item to be added to the inventory
	 */
	public void addItem(Item i) {
		inventory.add(i);
	}

	/**
	 * Returns the player's inventory
	 * 
	 * @return the player's inventory
	 */
	public LinkedList<Item> getInventory() {
		return inventory;
	}

	/**
	 * Changes the player's teleportBlock variable to the passed boolean.
	 * 
	 * @param isSet
	 *            the boolean to set teleportBlock to
	 */
	public void setTeleportBlock(boolean isSet) {
		teleportBlock = isSet;
	}

	/**
	 * Returns the status of the player's teleportBlock
	 * 
	 * @return the teleportBlock variable
	 */
	public boolean getTeleportBlock() {
		return teleportBlock;
	}

	/**
	 * Adjusts the player's energy by the given amount. If it dips below 0, it
	 * is set to 0, and if it is higher than 100, it is set to 100.
	 * 
	 * @param amount
	 *            the amount to adjust the energy by
	 */
	public void changeEnergy(int amount) {
		energy += amount;
		if (energy < 0) {
			energy = 0;
		}
		if (energy > 100)
			energy = 100;
	}

	/**
	 * Returns the player's name
	 * 
	 * @return the player's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the number of shots the player has left
	 * 
	 * @return numShots
	 */
	public int getNumShots() {
		return numShots;
	}

	/**
	 * Subtracts one from the player's number of shots
	 */
	public void useShot() {
		numShots--;
	}

	/**
	 * Returns true if the player is dead, else false is returned.
	 * 
	 * @return isDead variable
	 */
	public boolean isDead() {
		return isDead;
	}

	/**
	 * Sets the player's prevLocation to the location, and the location to the
	 * passed Point
	 * 
	 * @param p
	 *            the Point to set the location to
	 */
	public void setLocation(Point p) {
		if (location == null)
			prevLocation = p;
		location = p;
	}

	/**
	 * Returns the player's location
	 * 
	 * @return the player's location variable
	 */
	public Point getLocation() {
		return location;
	}

	/**
	 * Returns the player's previous location
	 * 
	 * @return the prevLocation
	 */
	public Point getPrevLocation() {
		return prevLocation;
	}

	/**
	 * This method set's the player's previous location to the passed Point.
	 * 
	 * @param p
	 *            the Point to set the previous location to
	 */
	public void setPrevLocation(Point p) {
		prevLocation = p;
	}

	/**
	 * Kill's the player by changing the isDead variable to true
	 */
	public void kill() {
		isDead = true;
		energy = 0;
	}

	/**
	 * Returns the player's leeway
	 * 
	 * @return the leeway variable
	 */
	public Point getLeeway() {
		return leeway;
	}

	/**
	 * Sets the player's leeway to the passed Point
	 * 
	 * @param p
	 *            the Point to set the leeway to
	 */
	public void setLeeway(Point p) {
		leeway = p;
	}

	/**
	 * Add's a shot to the player's number of shots
	 */
	public void addShot() {
		numShots++;
	}

	/**
	 * A player can always move left if they are not in a tunnel. If they are in
	 * a tunnel, the player can only move Left if they are in tunnel that goes
	 * from the east to the north. This method returns true if the player can
	 * move to the left.
	 * @param rooms
	 * 			the map of Rooms from the HTW game 
	 * @param curr 
	 * 			the player's current location
	 * @param prev 
	 * 			the player's previous location
	 * @return true, if the player can move left, and false if they can't
	 */
	public boolean canMoveLeft(Room[][][] rooms, Point curr, Point prev) {
		int currX = this.location.getXCoor();
		int currY = this.location.getYCoor();
		if (rooms[currLevel][currX][currY].isTunnel() == false
				&& rooms[currLevel][currX][currY].isReverseTunnel() == false)
			return true;
		if (rooms[currLevel][currX][currY].isReverseTunnel()
				&& (this.getLeeway().compareTo(new Point(1, 0))
						|| this.getLeeway().compareTo(new Point(1, 1)) || this
						.getLeeway().compareTo(new Point(2, 3))))
			return true;
		
		if (rooms[currLevel][currX][currY].isTunnel()
				&& (this.getLeeway().compareTo(new Point(2,1))
						|| this.getLeeway().compareTo(new Point(2, 0)) || this
						.getLeeway().compareTo(new Point(1,3))))
			return true;
		
		return false;
	}

	/**
	 * This sets the direction of the last move made by the player
	 * 
	 * @param dir
	 * 			instance of Direction
	 */
	public void setLastMove(Direction dir){
		lastMove = dir;
	}
	
	/**
	 * Returns the direction of the last move
	 * 
	 * @return
	 * 			Direction of the last move
	 */
	public Direction getLastMove(){
		return lastMove;
	}
	
	/**
	 * A player can always move right if they are not in a tunnel. If they are
	 * in a tunnel, the player can only move right if they are in tunnel that
	 * goes from the west to the south. This method returns true if the player
	 * can move to the right.
	 * 
	 * @param rooms
	 * 			the map of Rooms from the HTW game 
	 * @param curr 
	 * 			the player's current location
	 * @param prev 
	 * 			the player's previous location
	 * 
	 * @return true, if the player can move right, and false if they can't
	 */
	public boolean canMoveRight(Room[][][] rooms, Point curr, Point prev) {
		int currX = this.location.getXCoor();
		int currY = this.location.getYCoor();
		if (rooms[currLevel][currX][currY].isTunnel() == false
				&& rooms[currLevel][currX][currY].isReverseTunnel() == false)
			return true;
		if (rooms[currLevel][currX][currY].isReverseTunnel()
				&& (this.getLeeway().compareTo(new Point(1, 0))
						|| this.getLeeway().compareTo(new Point(2, 2)) || this
						.getLeeway().compareTo(new Point(2, 3))))
			return true;
		
		if (rooms[currLevel][currX][currY].isTunnel()
				&& (this.getLeeway().compareTo(new Point(2,0))
						|| this.getLeeway().compareTo(new Point(1, 2)) || this
						.getLeeway().compareTo(new Point(1,3))))
			return true;

		return false;
	}

	/**
	 * A player can always move up if they are not in a tunnel. If they are in a
	 * tunnel, the player can only move up if they are in tunnel that goes from
	 * the east to the north. This method returns true if the player can move to
	 * the up.
	 * @param rooms
	 * 			the map of Rooms from the HTW game 
	 * @param curr 
	 * 			the player's current location
	 * @param prev 
	 * 			the player's previous location
	 * @return true, if the player can move up, and false if they can't
	 */
	public boolean canMoveUp(Room[][][] rooms, Point curr, Point prev) {
		int currX = this.location.getXCoor();
		int currY = this.location.getYCoor();
		if (rooms[currLevel][currX][currY].isTunnel() == false
				&& rooms[currLevel][currX][currY].isReverseTunnel() == false)
			return true;
		if (rooms[currLevel][currX][currY].isReverseTunnel()
				&& (this.getLeeway().compareTo(new Point(1, 1))
						|| this.getLeeway().compareTo(new Point(0, 1)) || this
						.getLeeway().compareTo(new Point(3, 2))))
			return true;
		
		if (rooms[currLevel][currX][currY].isTunnel()
				&& (this.getLeeway().compareTo(new Point(3,1))
						|| this.getLeeway().compareTo(new Point(1,2)) || this
						.getLeeway().compareTo(new Point(0,2))))
			return true;
		return false;
	}

	/**
	 * A player can always move down if they are not in a tunnel. If they are in
	 * a tunnel, the player can only move down if they are in tunnel that goes
	 * from the west to the south. This method returns true if the player can
	 * move to the down.
	 * @param rooms
	 * 			the map of Rooms from the HTW game 
	 * @param curr 
	 * 			the player's current location
	 * @param prev 
	 * 			the player's previous location
	 * @return true, if the player can move down, and false if they can't
	 */
	public boolean canMoveDown(Room[][][] rooms, Point curr, Point prev) {
		int currX = curr.getXCoor();
		int currY = curr.getYCoor();

		if (rooms[currLevel][currX][currY].isTunnel() == false
				&& rooms[currLevel][currX][currY].isReverseTunnel() == false)
			return true;
		if (rooms[currLevel][currX][currY].isReverseTunnel()
				&& (this.getLeeway().compareTo(new Point(0, 1))
						|| this.getLeeway().compareTo(new Point(2, 2)) || this
						.getLeeway().compareTo(new Point(3, 2))))
			return true;
		
		if (rooms[currLevel][currX][currY].isTunnel()
				&& (this.getLeeway().compareTo(new Point(0, 2))
						|| this.getLeeway().compareTo(new Point(2, 1)) || this
						.getLeeway().compareTo(new Point(3, 1))))
			return true;

		return false;
	}

	/**
	 * Moves the player left by adjusting the player's leeway. If the player's
	 * leeway passes an extreme (less than 0 or greater than 4), then the leeway
	 * wraps around and the player's location is adjusted.
	 * 
	 * @param map
	 *            the map that the player is currently on
	 * @param game
	 * 			the HuntTheWumpus game that the player is in
	 * @return 
	 * 			true, if the player moved, else false
	 */
	public boolean moveLeft(WumpusMap map, HuntTheWumpus game) {
		boolean validMove = false;
		Room[][][] rooms = map.getMap();
		Point newLoc = this.getLocation();
		Point newLeeway = this.getLeeway();
		if (canMoveLeft(rooms, this.getLocation(), this.getPrevLocation())) {
			setLastMove(Direction.LEFT);
			validMove = true;
			if (map.getRoom(this.getLocation().getXCoor(),
					this.getLocation().getYCoor(), currLevel).isTunnel()) {
				if (this.getLeeway().getYCoor() < 2) {
					if (this.getLeeway().getYCoor() == 0) {
						newLeeway = (new Point(this.getLeeway().getXCoor(), 3));
						newLoc = (new Point(this.getLocation().getXCoor(),
								(this.getLocation().getYCoor()
										+ rooms[currLevel][0].length - 1)
										% rooms[currLevel][0].length));
						if (rooms[currLevel][newLoc.getXCoor()][newLoc.getYCoor()]
								.isTunnel())
							newLeeway = new Point(1, 3);
						if (rooms[currLevel][newLoc.getXCoor()][newLoc.getYCoor()]
								.isReverseTunnel())
							newLeeway = new Point(2, 3);
						this.setLeeway(newLeeway);
						changeEnergy(-2);

					}

					else {
						newLeeway = (new Point(this.getLeeway().getXCoor(),
								this.getLeeway().getYCoor() - 1));
					}
				} else {
					newLeeway = (new Point(this.getLeeway().getXCoor(), this
							.getLeeway().getYCoor() - 1));
				}

			}
			if (!map.getRoom(this.getLocation().getXCoor(),
					this.getLocation().getYCoor(), currLevel).isTunnel()) {
				if (this.getLeeway().getYCoor() == 0) {
					newLeeway = (new Point(this.getLeeway().getXCoor(), 3));
					newLoc = (new Point(this.getLocation().getXCoor(), (this
							.getLocation().getYCoor() + rooms[currLevel][0].length - 1)
							% rooms[currLevel][0].length));
					if (rooms[currLevel][newLoc.getXCoor()][newLoc.getYCoor()].isTunnel())
						newLeeway = new Point(1, 3);
					if (rooms[currLevel][newLoc.getXCoor()][newLoc.getYCoor()]
							.isReverseTunnel())
						newLeeway = new Point(2, 3);
					this.setLeeway(newLeeway);
					changeEnergy(-2);

				} else {
					newLeeway = (new Point(this.getLeeway().getXCoor(), this
							.getLeeway().getYCoor() - 1));
				}
			}

		}
		game.move(this, newLoc, newLeeway, currLevel);
		return validMove;
	}

	/**
	 * Moves the player right by adjusting the player's leeway. If the player's
	 * leeway passes an extreme (less than 0 or greater than 4), then the leeway
	 * wraps around and the player's location is adjusted.
	 * @param map
	 *            the map that the player is currently on
	 * @param game
	 * 			the HuntTheWumpus game that the player is in
	 * @return 
	 * 			true, if the player moved, else false
	 */
	public boolean moveRight(WumpusMap map, HuntTheWumpus game) {
		boolean validMove = false;
		Room[][][] rooms = map.getMap();
		Point newLoc = this.getLocation();
		Point newLeeway = this.getLeeway();
		if (canMoveRight(rooms, this.getLocation(), this.getPrevLocation())) {
			validMove = true;
			setLastMove(Direction.RIGHT);
			if (map.getRoom(this.getLocation().getXCoor(),
					this.getLocation().getYCoor(), currLevel).isTunnel()) {
				if (this.getLeeway().getYCoor() > 1) {
					if (this.getLeeway().getYCoor() == 3) {
						newLeeway = (new Point(this.getLeeway().getXCoor(), 0));
						newLoc = (new Point(this.getLocation().getXCoor(),
								(this.getLocation().getYCoor() + 1)
										% rooms[currLevel][0].length));
						if (rooms[currLevel][newLoc.getXCoor()][newLoc.getYCoor()]
								.isTunnel())
							newLeeway = new Point(2, 0);
						if (rooms[currLevel][newLoc.getXCoor()][newLoc.getYCoor()]
								.isReverseTunnel())
							newLeeway = new Point(1, 0);
						this.setLeeway(newLeeway);
						changeEnergy(-2);

					} else {
						newLeeway = (new Point(this.getLeeway().getXCoor(),
								this.getLeeway().getYCoor() + 1));
					}
				} else {
					newLeeway = (new Point(this.getLeeway().getXCoor(), this
							.getLeeway().getYCoor() + 1));
				}

			} else {
				if (this.getLeeway().getYCoor() == 3) {
					newLeeway = (new Point(this.getLeeway().getXCoor(), 0));
					newLoc = (new Point(this.getLocation().getXCoor(), (this
							.getLocation().getYCoor() + 1) % rooms[currLevel][0].length));
					if (rooms[currLevel][newLoc.getXCoor()][newLoc.getYCoor()].isTunnel())
						newLeeway = new Point(2, 0);
					if (rooms[currLevel][newLoc.getXCoor()][newLoc.getYCoor()]
							.isReverseTunnel())
						newLeeway = new Point(1, 0);
					this.setLeeway(newLeeway);
					changeEnergy(-2);

				} else {
					newLeeway = (new Point(this.getLeeway().getXCoor(), this
							.getLeeway().getYCoor() + 1));
				}
			}

		}
		game.move(this, newLoc, newLeeway, currLevel);
		return validMove;
	}

	/**
	 * Moves the player up by adjusting the player's leeway. If the player's
	 * leeway passes an extreme (less than 0 or greater than 4), then the leeway
	 * wraps around and the player's location is adjusted.
	 * @param map
	 *            the map that the player is currently on
	 * @param game
	 * 			the HuntTheWumpus game that the player is in
	 * @return 
	 * 			true, if the player moved, else false
	 */
	public boolean moveUp(WumpusMap map, HuntTheWumpus game) {
		boolean validMove = false;
		Room[][][] rooms = map.getMap();
		Point newLoc = this.getLocation();
		Point newLeeway = this.getLeeway();
		
		//extra code for RoomNavigator:
		Point miniLeeway = roomNav.getMiniLeeway();
		
		if (canMoveUp(rooms, this.getLocation(), this.getPrevLocation())) {
			setLastMove(Direction.UP);
			validMove = true;
			if (map.getRoom(this.getLocation().getXCoor(),
					this.getLocation().getYCoor(), currLevel).isTunnel()) {
				if (this.getLeeway().getXCoor() < 1) {
					if (this.getLeeway().getXCoor() == 0) {
						newLeeway = (new Point(3, this.getLeeway().getYCoor()));
						if (this.getLocation().getXCoor() == 0) {
							newLoc = new Point(5, this.getLocation().getYCoor());
						} else {
							newLoc = (new Point((this.getLocation().getXCoor()
									+ rooms[0].length - 1)
									% rooms[0].length, this.getLocation()
									.getYCoor()));
						}
						if (rooms[currLevel][newLoc.getXCoor()][newLoc.getYCoor()]
								.isTunnel())
							newLeeway = new Point(3, 1);
						if (rooms[currLevel][newLoc.getXCoor()][newLoc.getYCoor()]
								.isReverseTunnel())
							newLeeway = new Point(3, 2);
						this.setLeeway(newLeeway);
						changeEnergy(-2);

					}

					else {
						newLeeway = (new Point(this.getLeeway().getXCoor() - 1,
								this.getLeeway().getYCoor()));
					}
				} else {
					newLeeway = (new Point(this.getLeeway().getXCoor() - 1,
							this.getLeeway().getYCoor()));
				}

			} else {
				if (this.getLeeway().getXCoor() == 0) {
					newLeeway = (new Point(3, this.getLeeway().getYCoor()));
					if (this.getLocation().getXCoor() == 0) {
						newLoc = new Point(5, this.getLocation().getYCoor());
					} else
						newLoc = (new Point((this.getLocation().getXCoor() - 1)
								% rooms[0].length, this.getLocation().getYCoor()));
					if (rooms[currLevel][newLoc.getXCoor()][newLoc.getYCoor()].isTunnel())
						newLeeway = new Point(3, 1);
					if (rooms[currLevel][newLoc.getXCoor()][newLoc.getYCoor()]
							.isReverseTunnel())
						newLeeway = new Point(3, 2);
					this.setLeeway(newLeeway);
					changeEnergy(-2);

				} else {
					newLeeway = (new Point((this.getLeeway().getXCoor() - 1)
							% rooms.length, this.getLeeway().getYCoor()));
				}
			}

		}
		
		game.move(this, newLoc, newLeeway, currLevel);
		return validMove;
	}

	/**
	 * Moves the player down by adjusting the player's leeway. If the player's
	 * leeway passes an extreme (less than 0 or greater than 4), then the leeway
	 * wraps around and the player's location is adjusted.
	 * @param map
	 *            the map that the player is currently on
	 * @param game
	 * 			the HuntTheWumpus game that the player is in
	 * @return 
	 * 			true, if the player moved, else false
	 */
	public boolean moveDown(WumpusMap map, HuntTheWumpus game) {
		boolean validMove = false;
		Room[][][] rooms = map.getMap();
		Point newLoc = this.getLocation();
		Point newLeeway = this.getLeeway();
		if (canMoveDown(rooms, this.getLocation(), this.getPrevLocation())) {
			validMove = true;
			setLastMove(Direction.DOWN);
			if (map.getRoom(this.getLocation().getXCoor(),
					this.getLocation().getYCoor(), currLevel).isTunnel()) {
				if (this.getLeeway().getXCoor() > 1) {
					if (this.getLeeway().getXCoor() == 3) {
						newLoc = new Point((this.getLocation().getXCoor() + 1)
								% rooms[0].length, this.getLocation().getYCoor());
						newLeeway = new Point(0, this.getLeeway().getYCoor());
						newLoc = (new Point((this.getLocation().getXCoor() + 1)
								% rooms[0].length, this.getLocation().getYCoor()));
						if (rooms[currLevel][newLoc.getXCoor()][newLoc.getYCoor()]
								.isTunnel())
							newLeeway = new Point(0, 2);
						if (rooms[currLevel][newLoc.getXCoor()][newLoc.getYCoor()]
								.isReverseTunnel())
							newLeeway = new Point(0, 1);
						this.setLeeway(newLeeway);
						changeEnergy(-2);

					}

					else {
						newLeeway = (new Point(this.getLeeway().getXCoor() + 1,
								this.getLeeway().getYCoor()));
					}
				} else {
					newLeeway = (new Point(this.getLeeway().getXCoor() + 1,
							this.getLeeway().getYCoor()));
				}
			} else {
				if (this.getLeeway().getXCoor() == 3) {
					newLeeway = (new Point(0, this.getLeeway().getYCoor()));
					newLoc = (new Point((this.getLocation().getXCoor() + 1)
							% rooms[0].length, this.getLocation().getYCoor()));
					if (rooms[currLevel][newLoc.getXCoor()][newLoc.getYCoor()].isTunnel())
						newLeeway = new Point(0, 2);
					if (rooms[currLevel][newLoc.getXCoor()][newLoc.getYCoor()]
							.isReverseTunnel())
						newLeeway = new Point(0, 1);
					this.setLeeway(newLeeway);

					changeEnergy(-2);

				} else {
					newLeeway = (new Point(this.getLeeway().getXCoor() + 1,
							this.getLeeway().getYCoor()));
				}
			}
		}
		game.move(this, newLoc, newLeeway, currLevel);
		return validMove;
	}


	/**
	 * Compares this player object to the passed player object. Returns true if
	 * they are they same and false if not.
	 * 
	 * @param p
	 *            The player object to which this object is compared.
	 * @return A boolean identifying if the two player objects are the same.
	 */
	public boolean equals(Player p) {
		if (this.name.equals(p.name) && this.getID() == p.getID())
			return true;
		return false;
	}

}
