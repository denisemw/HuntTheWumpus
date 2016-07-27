package gameModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/**
 * The WumpusMap is the dungeon (board) to which the player, wumpus, items,
 * bats, and pits are added for the HuntTheWumpus game.
 * 
 * @author Salika Dunatunga, Sarah Lutjens, Jane Wang, Denise Werchan
 */

public class WumpusMap implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Point wumpusLocation;
	private boolean isMultiplayer, pvp;
	private int wumpusLevel;
	private double probability;
	private Room[][][] levels;

	/**
	 * The wumpusMap constructor creates a 6x7 array of Rooms objects to
	 * represent a WumpusMap. Each grid square in this array represents either a
	 * room or a hallway. The map is randomly generated, so that a new map will
	 * be generated with each construction. Additionally, the map uses
	 * wraparound.
	 * 
	 * After the map is constructed, a wumpus is added at a random location, as
	 * long as this location is not a tunnel. Then, two slime pits are added at
	 * random, non-tunnel locations. Last, 2 bats are added at random,
	 * non-tunnel locations as well. Each room within two rooms of the wumpus
	 * has indicators that the wumpus is nearby, and each room within one room
	 * of the slime pits has an indicator that a pit is nearby.
	 * 
	 * @param level
	 *            the level that the user selected
	 * @param multiplayer
	 *            true if this is a multiplayer game, else false
	 * @param pvp 
	 * 			  true if this is a PvP game, else false
	 */
	public WumpusMap(int level, boolean multiplayer, boolean pvp) {
		this.pvp = pvp;
		isMultiplayer = multiplayer;
		if (level == 0)
			probability = .7;
		else if (level == 1)
			probability = .6;
		else
			probability = .4;


			levels = new Room[3][6][7];
			
			do {
			
			Room[][] level1 = generateWumpusFreeLevel();
			Room[][] level2 = generateWumpusFreeLevel();
			Room[][] level3 = generateWumpusFreeLevel();
			
			
			levels[0] = level1;
			levels[1] = level2;
			levels[2] = level3;
			
			Random rand = new Random();
			int x = rand.nextInt(6);
			int y = rand.nextInt(7);
			int z = rand.nextInt(3);


			// add a wumpus/blood
			setWumpusAndBlood(true, x, y, z);


			// add pits to each level
			for (int i = 0; i < 3; i++) {
				x = rand.nextInt(6);
				y = rand.nextInt(7);
				setPits(x, y, i);
			}
			
			// add ladders to the 1st and 2nd level
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 2; j++) {
				x = rand.nextInt(6);
				y = rand.nextInt(7);
				while (levels[i+1][x][y].isPit() || levels[i+1][x][y].hasWumpus() || levels[i+1][x][y].hasSlime() 
						|| levels[i+1][x][y].getHasBlood() || levels[i+1][x][y].hasWumpus()|| levels[i][x][y].isPit() 
						|| levels[i][x][y].isReverseTunnel() || levels[i][x][y].isTunnel() || levels[i][x][y].hasWumpus()) {
					x = rand.nextInt(6);
					y = rand.nextInt(7);
				}
				levels[i][x][y].setLadder();
				if (levels[i+1][x][y].isOneAway()) {
					levels[i][x][y].setHasBlood(true);
				}
				}
			}
			
			// add 2 bats to each level
			for (int i = 0; i < 3; i++) {
				x = rand.nextInt(6);
				y = rand.nextInt(7);
				Point temp = setBats(true, x, y, i);
				levels[i][temp.getXCoor()][temp.getYCoor()].setBat(true);
				x = rand.nextInt(6);
				y = rand.nextInt(7);
				temp = setBats(true, x, y, i);
				levels[i][temp.getXCoor()][temp.getYCoor()].setBat(true);
			}

			// add items to each level			
			for (int i = 0; i < 3; i++) {
				
				int numFood = 1;
				int numSilencers = 1;
				int numBlockers = 1;

				if (level == 0) {
					numFood = 5;
					numSilencers = 2;
					numBlockers = 3;
				}

				else if (level == 1) {
					numFood = 3;
					numSilencers = 1;
					numBlockers = 2;
				}

				else {
					numFood = 2;
					numSilencers = 0;
					numBlockers = 1;
				}

				if (isMultiplayer) {
					numFood *= numFood;
					numSilencers *= numSilencers;
					numBlockers *= numBlockers;
				}

				// add food
				for (int j = 0; j < numFood; j++) {
					Food food = new Food();
					x = rand.nextInt(6);
					y = rand.nextInt(7);
					while (levels[i][x][y].isPit() || levels[i][x][y].hasWumpus()
							|| levels[i][x][y].hasItem()) {
						x = rand.nextInt(6);
						y = rand.nextInt(7);
					}
					food.setLocation(new Point(x, y));
					food.setLevel(i);
					Point p;

					if (levels[i][x][y].isTunnel()) {
						if (Math.random() > .5)
							p = new Point(0, 2);
						else
							p = new Point(2, 0);
					}

					else if (levels[i][x][y].isReverseTunnel()) {
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

					food.setLeeway(p);
					levels[i][x][y].setItem(true);
					levels[i][x][y].addItem(food);

				}

				// add silencers
				for (int k = 0; k < numSilencers; k++) {
					Silencer silencer = new Silencer();
					x = rand.nextInt(6);
					y = rand.nextInt(7);
					while (levels[i][x][y].isPit() || levels[i][x][y].hasWumpus()
							|| levels[i][x][y].hasItem()) {
						x = rand.nextInt(6);
						y = rand.nextInt(7);
					}
					silencer.setLocation(new Point(x, y));
					silencer.setLevel(i);
					levels[i][x][y].setItem(true);
					Point p;

					if (levels[i][x][y].isTunnel()) {
						if (Math.random() > .5)
							p = new Point(0, 2);
						else
							p = new Point(2, 0);
					}

					else if (levels[i][x][y].isReverseTunnel()) {
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

					silencer.setLeeway(p);
					levels[i][x][y].addItem(silencer);


				}

				// add teleport blockers
				for (int h = 0; h < numBlockers; h++) {
					TeleportBlocker blocker = new TeleportBlocker();
					x = rand.nextInt(6);
					y = rand.nextInt(7);
					while (levels[i][x][y].isPit() || levels[i][x][y].hasWumpus()
							|| levels[i][x][y].hasItem()) {
						x = rand.nextInt(6);
						y = rand.nextInt(7);
					}
					blocker.setLocation(new Point(x, y));
					blocker.setLevel(i);
					levels[i][x][y].setItem(true);
					Point p;

					if (levels[i][x][y].isTunnel()) {
						if (Math.random() > .5)
							p = new Point(0, 2);
						else
							p = new Point(2, 0);
					}

					else if (levels[i][x][y].isReverseTunnel()) {
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
					blocker.setLeeway(p);
					levels[i][x][y].addItem(blocker);

				}
			}
			} while (!isSolveableWumpus());

	}
	
	/*
	 * This private helper method generates a single level of the map, without pits, items, or a wumpus
	 */
	private Room[][] generateWumpusFreeLevel() {
		Room[][] newRooms;
		do {
			newRooms = new Room[6][7];
			Random rand = new Random();
			int x = rand.nextInt(6);
			int y = rand.nextInt(7);

			newRooms[x][y] = new Room(new Point(x, y));
			Room start = newRooms[x][y];

			Stack<Room> roomStack = new Stack<Room>();
			roomStack.push(start);

			while (!roomStack.isEmpty()) {
				Room current = roomStack.pop();
				int xCoord = current.getLocation().getXCoor();
				int xWrapUp = (xCoord + newRooms.length - 1) % newRooms.length;
				int xWrapDown = (xCoord + 1) % newRooms.length;
				int yCoord = current.getLocation().getYCoor();
				int yWrapL = (yCoord + newRooms[0].length - 1) % newRooms[0].length;
				int yWrapR = (yCoord + 1) % newRooms[0].length;

				// check room to the left
				if (newRooms[xCoord][yWrapL] == null) {
					newRooms[xCoord][yWrapL] = new Room(new Point(xCoord, yWrapL));
					if (Math.random() > probability) {
						if (Math.random() > .5) {
							newRooms[xCoord][yWrapL].createReverseTunnel();
						} else {
							newRooms[xCoord][yWrapL].createTunnel();
						}
					}
					roomStack.push(newRooms[xCoord][yWrapL]);
				}

				// check room above
				if (newRooms[xWrapUp][yCoord] == null) {
					newRooms[xWrapUp][yCoord] = new Room(
							new Point(xWrapUp, yCoord));
					if (Math.random() > probability) {
						if (Math.random() > .5) {
							newRooms[xWrapUp][yCoord].createReverseTunnel();
						} else {
							newRooms[xWrapUp][yCoord].createTunnel();
						}
					}
					roomStack.push(newRooms[xWrapUp][yCoord]);
				}

				// check room to the right
				if (newRooms[xCoord][yWrapR] == null) {
					newRooms[xCoord][yWrapR] = new Room(new Point(xCoord, yWrapR));
					if (Math.random() > probability) {
						if (Math.random() > .5) {
							newRooms[xCoord][yWrapR].createReverseTunnel();
						} else {
							newRooms[xCoord][yWrapR].createTunnel();
						}
					}
					roomStack.push(newRooms[xCoord][yWrapR]);
				}

				// check room below
				if (newRooms[xWrapDown][yCoord] == null) {
					newRooms[xWrapDown][yCoord] = new Room(new Point(xWrapDown,
							yCoord));
					if (Math.random() > probability) {
						if (Math.random() > .5) {
							newRooms[xWrapDown][yCoord].createReverseTunnel();
						} else {
							newRooms[xWrapDown][yCoord].createTunnel();
						}
					}
					roomStack.push(newRooms[xWrapDown][yCoord]);
				}

			}

			// add 2 pits at random locations
		//	x = rand.nextInt(6);
			//y = rand.nextInt(7);
			//setPits(x, y);


			
		} while (!this.isSolveableTunnels(newRooms));
		return newRooms;

	}
	
	
	/**
	 * This method returns whether the map is a player-vs-player map or not
	 * 
	 * @return
	 * 		true if it is a PvP game else false
	 */
	public boolean getPvP() {
		return pvp;
	}

	// checks to make sure no 4 tunnels create a circle
	private boolean isSolveableTunnels(Room[][] rooms) {
		for (int i = 0; i < rooms.length - 1; i++) {
			for (int j = 0; j < rooms[0].length; j++) {
				int xWrapUp = (i + rooms.length - 1) % rooms.length;
				int yWrapL = (j + rooms[0].length - 1) % rooms[0].length;
				if (rooms[i][yWrapL].isTunnel() && rooms[xWrapUp][j].isTunnel()
						&& rooms[i][j].isReverseTunnel()
						&& rooms[xWrapUp][yWrapL].isReverseTunnel())
					return false;
				if (rooms[i][yWrapL].isReverseTunnel()
						&& rooms[xWrapUp][j].isReverseTunnel()
						&& rooms[i][j].isTunnel()
						&& rooms[xWrapUp][yWrapL].isTunnel())
					return false;
			}
		}
		return true;
	}
	
	
	
	

	/*
	 * private helper method to determine if the game is solveable or not.
	 */
	private boolean isSolveableWumpus() {
		Point p = getWumpus();
		int x = p.getXCoor(), y = p.getYCoor();
		int xWrapUp = (x + levels[wumpusLevel].length - 1) % levels[wumpusLevel].length;
		int xWrapDown = (x + 1) % levels[wumpusLevel].length;
		int yWrapL = (y + levels[wumpusLevel][0].length - 1) % levels[wumpusLevel][0].length;
		int yWrapR = (y + 1) % levels[wumpusLevel][0].length;
		int tunnelCount = 0;
		int pitCount = 0;
		if (levels[wumpusLevel][x][yWrapL].isTunnel() || levels[wumpusLevel][x][yWrapL].isReverseTunnel())
			tunnelCount++;
		if (levels[wumpusLevel][x][yWrapR].isTunnel() || levels[wumpusLevel][x][yWrapR].isReverseTunnel())
			tunnelCount++;
		if (levels[wumpusLevel][xWrapUp][y].isTunnel() || levels[wumpusLevel][xWrapUp][y].isReverseTunnel())
			tunnelCount++;
		if (levels[wumpusLevel][xWrapDown][y].isTunnel()
				|| levels[wumpusLevel][xWrapDown][y].isReverseTunnel())
			tunnelCount++;

		if (levels[wumpusLevel][x][yWrapL].isPit())
			pitCount++;
		if (levels[wumpusLevel][x][yWrapR].isPit())
			pitCount++;
		if (levels[wumpusLevel][xWrapUp][y].isPit())
			pitCount++;
		if (levels[wumpusLevel][xWrapDown][y].isPit())
			pitCount++;

		if (tunnelCount == 4 || (tunnelCount == 2 && pitCount == 2)
				|| (tunnelCount == 3 && pitCount == 1)) {
			return false;
		}

		return true;
	}

	/**
	 * This method either adds or removes a wumpus and it's clues from the map.
	 * If the boolean 'action' is true, an attempt is made to add a wumpus to
	 * the map at the passed x and y coordinates until the wumpus is succesfully
	 * added. New, random, x and y coordinates are continually generated until
	 * the wumpus is added succesfully. Clues to the wumpus's location are also
	 * added at the rooms around the wumpus.
	 * 
	 * @param action
	 *            true if the wumpus should be added, and false if it should be
	 *            removed
	 * @param x
	 *            the x coordinate to place the wumpus
	 * @param y
	 *            the y coordinate to place the wumpus
	 * @param z 
	 * 			  the level to place the wumpus
	 */
	public void setWumpusAndBlood(boolean action, int x, int y, int z) {
		// add or remove a wumpus
		while (true) {
			Random rand = new Random();
			x = rand.nextInt(6);
			y = rand.nextInt(7);
			if (levels[z][x][y].setWumpus(action)) {
				wumpusLevel = z;
				wumpusLocation = new Point(x,y);
				Room current = levels[z][x][y];
				
				int xCoord = current.getLocation().getXCoor();
				int xWrapUp = (xCoord + levels[0].length - 1) % levels[0].length;
				int xWrapDown = (xCoord + 1) % levels[0].length;
				int yCoord = current.getLocation().getYCoor();
				int yWrapL = (yCoord + levels[0][0].length - 1) % levels[0][0].length;
				int yWrapR = (yCoord + 1) % levels[0][0].length;
				
				Point currL, currR, currUp, currD;
				currL = moveArrow(new Point(xCoord, yWrapL), null, Direction.LEFT, z);
				currR = moveArrow(new Point(xCoord, yWrapR), null, Direction.RIGHT, z);
				currUp = moveArrow(new Point(xWrapUp, yCoord), null, Direction.UP, z);
				currD = moveArrow(new Point(xWrapDown, yCoord), null, Direction.DOWN, z);				
				
				levels[z][currL.getXCoor()][currL.getYCoor()].setHasBlood(true);
				levels[z][currR.getXCoor()][currR.getYCoor()].setIsOneAway();

				levels[z][currR.getXCoor()][currR.getYCoor()].setHasBlood(true);
				levels[z][currL.getXCoor()][currL.getYCoor()].setIsOneAway();
				
				levels[z][currD.getXCoor()][currD.getYCoor()].setHasBlood(true);
				levels[z][currD.getXCoor()][currD.getYCoor()].setIsOneAway();

				levels[z][currUp.getXCoor()][currUp.getYCoor()].setHasBlood(true);
				levels[z][currUp.getXCoor()][currUp.getYCoor()].setIsOneAway();
				
				ArrayList<Room> bloodRooms = new ArrayList<Room>();
				bloodRooms.add(levels[z][currL.getXCoor()][currL.getYCoor()]);
				bloodRooms.add(levels[z][currR.getXCoor()][currR.getYCoor()]);
				bloodRooms.add(levels[z][currD.getXCoor()][currD.getYCoor()]);
				bloodRooms.add(levels[z][currUp.getXCoor()][currUp.getYCoor()]);
				
				for (int i = 0; i < bloodRooms.size(); i++) {
					Room room = bloodRooms.get(i);
					xCoord = room.getLocation().getXCoor();
					xWrapUp = (xCoord + levels[0].length - 1) % levels[0].length;
					xWrapDown = (xCoord + 1) % levels[0].length;
					yCoord = room.getLocation().getYCoor();
					yWrapL = (yCoord + levels[0][0].length - 1) % levels[0][0].length;
					yWrapR = (yCoord + 1) % levels[0][0].length;
					
					currL = moveArrow(new Point(xCoord, yWrapL), null, Direction.LEFT, z);
					currR = moveArrow(new Point(xCoord, yWrapR), null, Direction.RIGHT, z);
					currUp = moveArrow(new Point(xWrapUp, yCoord), null, Direction.UP, z);
					currD = moveArrow(new Point(xWrapDown, yCoord), null, Direction.DOWN, z);
					
					levels[z][currL.getXCoor()][currL.getYCoor()].setHasBlood(true);
					levels[z][currR.getXCoor()][currR.getYCoor()].setIsOneAway();

					levels[z][currR.getXCoor()][currR.getYCoor()].setHasBlood(true);
					levels[z][currL.getXCoor()][currL.getYCoor()].setIsOneAway();
					
					levels[z][currD.getXCoor()][currD.getYCoor()].setHasBlood(true);
					levels[z][currD.getXCoor()][currD.getYCoor()].setIsOneAway();

					levels[z][currUp.getXCoor()][currUp.getYCoor()].setHasBlood(true);
					levels[z][currUp.getXCoor()][currUp.getYCoor()].setIsOneAway();
				}
				

				break;
			}

		}
	}

	/**
	 * This method adds two slime pits to the map. An attempt is made to add the
	 * pits to the map at the passed x and y coordinates until the pit is
	 * succesfully added. New, random, x and y coordinates are continually
	 * generated until the pits are added succesfully. Clues to the pit's
	 * locations are also added at the rooms around the pits.
	 * 
	 * @param x
	 *            the x coordinate to place the pit
	 * @param y
	 *            the y coordinate to place the pit           
	 * @param z 
	 * 			  the level to place the wumpus
	 */
	public void setPits(int x, int y, int z) {
		// add 2 pits at random locations
		int count = 0;
		while (count < 2 ) {

			Random rand = new Random();
			x = rand.nextInt(6);
			y = rand.nextInt(7);
			
			int xWrapUp = (x + levels[z].length - 1) % levels[z].length;
			int xWrapDown = (x + 1) % levels[z].length;
			int yWrapL = (y + levels[z][0].length - 1) % levels[z][0].length;
			int yWrapR = (y + 1) % levels[z][0].length;
			int tunnelCount = 0;
			if (y == 0)
				yWrapL = 6;
		
			if (levels[z][x][yWrapL].isTunnel()
				|| levels[z][x][yWrapL].isReverseTunnel())
				tunnelCount++;
			if (levels[z][x][yWrapR].isTunnel()
				|| levels[z][x][yWrapR].isReverseTunnel())
				tunnelCount++;
			if (levels[z][xWrapUp][y].isTunnel()
				|| levels[z][xWrapUp][y].isReverseTunnel())
				tunnelCount++;
			if (levels[z][xWrapDown][y].isTunnel()
				|| levels[z][xWrapDown][y].isReverseTunnel())
				tunnelCount++;
			if (tunnelCount == 4)
				continue;
			if (Math.abs(getWumpus().getXCoor()-x)<2)
				continue;
			if (Math.abs(getWumpus().getYCoor()-y)<2)
				continue;
			
			if (tunnelCount!=4) {
				
			if (levels[z][x][y].setIsPit()) {
			Room current = levels[z][x][y];
			
			int xCoord = current.getLocation().getXCoor();
			xWrapUp = (xCoord + levels[0].length - 1) % levels[0].length;
			xWrapDown = (xCoord + 1) % levels[0].length;
			int yCoord = current.getLocation().getYCoor();
			yWrapL = (yCoord + levels[0][0].length - 1) % levels[0][0].length;
			yWrapR = (yCoord + 1) % levels[0][0].length;
			
			Point currL, currR, currUp, currD;
			currL = moveArrow(new Point(xCoord, yWrapL), null, Direction.LEFT, z);
			currR = moveArrow(new Point(xCoord, yWrapR), null, Direction.RIGHT, z);
			currUp = moveArrow(new Point(xWrapUp, yCoord), null, Direction.UP, z);
			currD = moveArrow(new Point(xWrapDown, yCoord), null, Direction.DOWN, z);
			
			
			levels[z][currL.getXCoor()][currL.getYCoor()].setSlime();
			levels[z][currR.getXCoor()][currR.getYCoor()].setSlime();			
			levels[z][currD.getXCoor()][currD.getYCoor()].setSlime();
			levels[z][currUp.getXCoor()][currUp.getYCoor()].setSlime();
			count++;
		}
	}
	}

	}

	/**
	 * This method adds or removes a bat from the map. An attempt is made to add
	 * the bat to the map at the passed x and y coordinate until the bat is
	 * succesfully added. New, random, x and y coordinates are continually
	 * generated until the bat is added succesfully.
	 * 
	 * @param action
	 *            the boolean, true to add a bat, and false to remove a bat
	 * @param x
	 *            the x coordinate to place the pit
	 * @param y
	 *            the y coordinate to place the pit           
	 * @param z 
	 * 			  the level to place the wumpus
	 * 
	 * @return Point the Point the bat was placed at
	 */
	public Point setBats(boolean action, int x, int y, int z) {
		// add 2 bats at random locations
		while (true) {
			Random rand = new Random();
			x = rand.nextInt(6);
			y = rand.nextInt(7);
			if (!levels[z][x][y].isReverseTunnel() && !levels[z][x][y].isTunnel()
					&& !levels[z][x][y].isPit() && !levels[z][x][y].hasWumpus())
				return new Point(x, y);
		}
	}

	/**
	 * @return String a string representing the board, with the character 'R'
	 *         representing a room, and the character 'T' representing a tunnel
	 */
	public String toString() {
		String s = "";
		for (int k = 0; k < 3; k++) {
		for (int i = 0; i < levels[k].length; i++) {
			for (int j = 0; j < levels[k][0].length; j++) {
				if (levels[k][i][j].hasWumpus()) {
					s += "W ";
				} else if (levels[k][i][j].getHasBlood())
					s += "B ";
				else if (levels[k][i][j].isTunnel() || levels[k][i][j].isReverseTunnel())
					s += "T ";
				else
					s += "R ";
			}
			s += '\n';
		}
		s += '\n';
		}
		return s;
	}

	/**
	 * This method returns the 3D 3x6x7 array of Rooms that the map is composed of
	 * 
	 * @return rooms,the 2D array of rooms that comprises the map
	 */
	public Room[][][] getMap() {
		return levels;
	}

	/**
	 * This method returns the room in this map at the passed Point location
	 * 
	 * @param x 
	 * 			the x-coordinate of the room
	 * @param y 
	 * 			the y-coordinate of the room
	 * @param level 
	 * 			the level of the room in the map
	 * @return Room, the Room object at the specified location
	 */
	public Room getRoom(int x, int y, int level) {
		return levels[level][x][y];
	}

	/*
	 * This private helper method determines where an arrow will end up when it
	 * is shot. An arrow traverses through hallways from where it was shot until
	 * it hits the first adjacent room, which is where the arrow lands. This
	 * method takes three variables: A point of the arrows current location, a
	 * point for the arrow's previous location, and a direction for the
	 * direction that the arrow was traveling in the previous location. This
	 * method returns the point that the arrow lands at.
	 */
	private Point moveArrow(Point curr, Point prev, Direction d, int currLevel) {
		Point newLocation;
		while (getRoom(curr.getXCoor(), curr.getYCoor(), currLevel).isTunnel() || getRoom(curr.getXCoor(), curr.getYCoor(), currLevel).isReverseTunnel()) {
			switch (d) {
			case RIGHT:

				if (levels[currLevel][curr.getXCoor()][curr.getYCoor()].isTunnel()) {
					d = Direction.DOWN;
					newLocation = new Point((curr.getXCoor() + 1) % levels[currLevel].length,
							curr.getYCoor());
				} else {
					d = Direction.UP;
					newLocation = new Point((curr.getXCoor() + levels[currLevel].length - 1)
							% levels[currLevel].length, curr.getYCoor());
					if (curr.getXCoor() == 0) {
						newLocation = new Point(5, curr.getYCoor());
					}
				}
				curr = newLocation;
				break;
			case DOWN:

				if (levels[currLevel][curr.getXCoor()][curr.getYCoor()].isTunnel()) {
					d = Direction.RIGHT;
					newLocation = new Point(curr.getXCoor(), (curr.getYCoor() + 1)
							% levels[currLevel][0].length);
				} else {
					d = Direction.LEFT;
					newLocation = new Point(curr.getXCoor(), (curr.getYCoor()
							+ levels[currLevel][0].length - 1)
							% levels[currLevel][0].length);
					if (curr.getYCoor() == 0) {
						newLocation = new Point(curr.getXCoor(), 6);
					}
				}
				curr = newLocation;
				break;
			case LEFT:

				if (levels[currLevel][curr.getXCoor()][curr.getYCoor()].isTunnel()) {
					d = Direction.UP;
					newLocation = new Point((curr.getXCoor() + levels[currLevel].length - 1)
							% levels[currLevel].length, curr.getYCoor());
					if (curr.getXCoor() == 0) {
						newLocation = new Point(5, curr.getYCoor());
					}
				} else {
					d = Direction.DOWN;
					newLocation = new Point((curr.getXCoor() + 1) % levels[currLevel].length,
							curr.getYCoor());
				}
				curr = newLocation;
				break;
			case UP:

				if (levels[currLevel][curr.getXCoor()][curr.getYCoor()].isTunnel()) {
					d = Direction.LEFT;
					newLocation = new Point(curr.getXCoor(), (curr.getYCoor()
							+ levels[currLevel][0].length - 1)
							% levels[currLevel][0].length);
					if (curr.getYCoor() == 0) {
						newLocation = new Point(curr.getXCoor(), 6);
					}
				} else {
					d = Direction.RIGHT;
					newLocation = new Point(curr.getXCoor(), (curr.getYCoor() + 1)
							% levels[currLevel][0].length);
				}
				curr = newLocation;
				break;
			}
		}
		return curr;
	}

	/**
	 * This method shoots an arrow in the specified direction
	 * 
	 * @param location
	 *            the current location of the arrow
	 * @param prev
	 *            the previous location of the arrow
	 * @param d
	 *            the direction of the arrow
	 * @param currLevel
	 * 			  the level of the map 
	 * @return the Point that the arrow lands at
	 */
	public Point shoot(Point location, Point prev, Direction d, int currLevel) {
		if (d == Direction.LEFT) {
			Point temp = location;
			Point arrowLanded = location;
			if (getRoom(location.getXCoor(), location.getYCoor(), currLevel).isTunnel() == false) {
				location = new Point(temp.getXCoor(), (temp.getYCoor()
						+ levels[currLevel][0].length - 1)
						% levels[currLevel][0].length);
				prev = temp;
				arrowLanded = moveArrow(location, prev, Direction.LEFT, currLevel);
			} else {
				if (location.getXCoor() == prev.getXCoor()
						&& location.getYCoor() > prev.getYCoor()) {
					location = new Point(temp.getXCoor(), (temp.getYCoor()
							+ levels[currLevel][0].length - 1)
							% levels[currLevel][0].length);
					prev = temp;
					arrowLanded = moveArrow(location, prev, Direction.LEFT, currLevel);
				} else if (location.getXCoor() < prev.getXCoor()
						&& location.getYCoor() == prev.getYCoor()) {
					location = new Point(temp.getXCoor(), (temp.getYCoor()
							+ levels[currLevel][0].length - 1)
							% levels[currLevel][0].length);
					prev = temp;
					arrowLanded = moveArrow(location, prev, Direction.LEFT, currLevel);
				}
			}
			return arrowLanded;
		}

		else if (d == Direction.RIGHT) {
			Point temp = location;
			Point arrowLanded = location;
			if (getRoom(location.getXCoor(), location.getYCoor(), currLevel).isTunnel() == false) {
				location = new Point(temp.getXCoor(), (temp.getYCoor() + 1)
						% levels[currLevel][0].length);
				prev = temp;
				arrowLanded = moveArrow(location, prev, Direction.RIGHT, currLevel);
			} else {
				if (prev.getXCoor() < location.getXCoor()
						&& location.getYCoor() == prev.getYCoor()) {
					location = new Point(temp.getXCoor(), (temp.getYCoor() + 1)
							% levels[currLevel][0].length);
					prev = temp;
					arrowLanded = moveArrow(location, prev, Direction.RIGHT, currLevel);
				} else if (location.getXCoor() == prev.getXCoor()
						&& location.getYCoor() < prev.getYCoor()) {
					location = new Point(temp.getXCoor(), (temp.getYCoor() + 1)
							% levels[currLevel][0].length);
					prev = temp;
					arrowLanded = moveArrow(location, prev, Direction.RIGHT, currLevel);
				}
			}
			return arrowLanded;
		}

		else if (d == Direction.UP) {
			Point temp = location;
			Point arrowLanded = location;
			if (getRoom(location.getXCoor(), location.getYCoor(), currLevel).isTunnel() == false) {
				location = new Point((temp.getXCoor() + levels[currLevel].length - 1)
						% levels[currLevel].length, temp.getYCoor());
				prev = temp;
				arrowLanded = moveArrow(location, prev, Direction.UP, currLevel);
			} else {
				if (prev.getXCoor() < location.getXCoor()
						&& location.getYCoor() == prev.getYCoor()) {
					location = new Point((temp.getXCoor() + levels[currLevel].length - 1)
							% levels[currLevel].length, temp.getYCoor());
					prev = temp;
					arrowLanded = moveArrow(location, prev, Direction.UP, currLevel);
				} else if (location.getXCoor() == prev.getXCoor()
						&& location.getYCoor() < prev.getYCoor()) {
					location = new Point((temp.getXCoor() + levels[currLevel].length - 1)
							% levels[currLevel].length, temp.getYCoor());
					prev = temp;
					arrowLanded = moveArrow(location, prev, Direction.UP, currLevel);
				}
			}
			return arrowLanded;
		}

		else {
			Point temp = location;
			Point arrowLanded = location;
			if (getRoom(location.getXCoor(), location.getYCoor(), currLevel).isTunnel() == false) {
				location = new Point((temp.getXCoor() + 1) % levels[currLevel].length,
						temp.getYCoor());
				prev = temp;
				arrowLanded = moveArrow(location, prev, Direction.DOWN, currLevel);
			} else {
				if (location.getXCoor() == prev.getXCoor()
						&& location.getYCoor() > prev.getYCoor()) {
					location = new Point((temp.getXCoor() + 1) % levels[currLevel].length,
							temp.getYCoor());
					prev = temp;
					arrowLanded = moveArrow(location, prev, Direction.DOWN, currLevel);
				} else if (location.getXCoor() < prev.getXCoor()
						&& location.getYCoor() == prev.getYCoor()) {
					location = new Point((temp.getXCoor() + 1) % levels[currLevel].length,
							temp.getYCoor());
					prev = temp;
					arrowLanded = moveArrow(location, prev, Direction.DOWN, currLevel);
				}
			}
			return arrowLanded;
		}
	}

	/**
	 * Sets the room at the give coordinates to the passed boolean by calling
	 * the room's setIsVisited method.
	 * 
	 * @param x
	 *            an int representing the x coordinate of the room.
	 * @param y
	 *            an int representing the y coordinate of the room.
	 * @param bool
	 *            the boolean to which the room's setIsVisited must be set to.
	 * @param level 
	 * 			  the level of the map
	 */
	public void setIsVisited(int x, int y, boolean bool, int level) {
		levels[level][x][y].setIsVisited(bool);
	}


	/**
	 * This method returns the wumpus's location
	 * 
	 * @return a Point representing the location of the wumpus
	 */
	public Point getWumpus() {
		return wumpusLocation;
	}
	
	/**
	 * This method returns the level that the wumpus is located on
	 * 
	 * @return
	 * 		an int representing the level that the wumpus is on
	 */
	public int getWumpusLevel() {
		return wumpusLevel;
	}



}
