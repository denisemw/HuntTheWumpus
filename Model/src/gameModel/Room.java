package gameModel;

import java.io.Serializable;

/**
 * This class simulates a room in a WumpusMap. A room can be either a room or a
 * tunnel. Each room can have a wumpus, a pit, or a bat. The room can also have
 * slime or blood. Each room has a unique location represented by a point, and a
 * boolean representing whether it has been visited or not.
 * 
 * @author Salika Dunatunga, Sarah Lutjens, Jane Wang, Denise Werchan
 */
public class Room implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isVisited;
	private boolean hasLadder;
	private boolean hasBat;
	private boolean isPit;
	private boolean hasWumpus;
	private boolean hasBlood;
	private boolean isTunnel;
	private boolean isReverseTunnel;
	private boolean isTunnelVisitedR;
	private boolean isTunnelVisitedL;
	private boolean isOneAway;
	private boolean hasSlime;
	private boolean hasItem;
	private Point location;
	private Item item;
	
	/**
	 * The Room constructor initializes the location to the passed parameter, and the 
	 * isTunnel, isVisited, hasBlood, hasWumpus, isPit, and hasBat variables to false
	 * 
	 * @param p
	 * 			the location of the room
	 */
	public Room(Point p) {
		this.isVisited = false;
		this.hasBlood = false;
		this.hasWumpus=false;
		this.isPit=false;
		this.hasBat=false;
		this.isTunnel = false;
		this.isReverseTunnel = false;
		this.location = p;
		this.isTunnelVisitedL = false;
		this.isTunnelVisitedR = false;
		this.hasSlime = false;
		this.hasItem = false;
		this.hasLadder = false;
		this.item = null;
		this.isOneAway=false;
		
	}
	
	/**
	 * Sets isOneAway to true if the room is one room away from the wumpus
	 */
	public void setIsOneAway() {
		isOneAway=true;
	}
	
	/**
	 * Returns true if the room is one room away from the wumpus
	 * 
	 * @return
	 * 		true if the room is one room away, else false
	 */
	public boolean isOneAway() {
		return isOneAway;
	}
	
	/**
	 * Sets the hasLadder variable to true
	 */
	public void setLadder() {
		this.hasLadder = true;
	}
	
	/**
	 * Adds an item to the room
	 * 
	 * @param i
	 * 		the item to add to the room
	 */
	public void addItem(Item i) {
		item = i;
	}
	
	/**
	 * Returns the item in this room
	 * 
	 * @return
	 * 		the item in the room
	 */
	public Item getItem() {
		return item;
	}
	
	/**
	 * Returns the leeway of the item in the room
	 * 
	 * @return
	 * 		a Point representing the items leeway
	 */
	public Point getItemLeeway() {
		return item.getLeeway();
	}
	
	/**
	 * Removes and returns the item in this room
	 * 
	 * @return
	 * 		the item in this room
	 */
	public Item removeItem() {
		Item i = item;
		item = null;
		hasItem=false;
		return i;
	}
	
	/**
	 * Returns true if this room has a ladder
	 * 
	 * @return
	 * 		true, if this room has a ladder, else false
	 */
	public boolean hasLadder() {
		return hasLadder;
	}
	
	/**
	 * This method add's or removes an item from this room, based
	 * on whether the passed parameter is true or false.
	 * 
	 * @param addOrRemove
	 * 				true if the item should be added, false if it should be removed
	 */
	public void setItem(boolean addOrRemove) {
		hasItem = addOrRemove;
	}
	
	
	/**
	 * This method returns true if this room contains an item.  Else it returns false;
	 * @return
	 * 		true if this room contains an item, else return false
	 */
	public boolean hasItem() {
		return hasItem;
	}

	/**
	 * This method sets the isTunnel variable to true, indicating that this room is a tunnel
	 */
	public void createTunnel() {
		isTunnel = true;
	}
	
	/**
	 * This method sets the isReverseTunnel variable to true, indicating that this room is a reversed tunnel
	 */
	public void createReverseTunnel() {
		isReverseTunnel = true;
	}
	
	/**
	 * This method returns the isReverseTunnelVariable
	 * 
	 * @return
	 * 		true if this room is a reverse tunnel, else false
	 */
	public boolean isReverseTunnel() {
		return isReverseTunnel;
	}
	
	
	/**
	 * sets the isVisited variable to true, indicating that this room has been visited by the player
	 * 
	 * @param visit 
	 * 			true if the room should be visible, else false
	 */
	public void setIsVisited(boolean visit) {
		isVisited = visit;
	}
	
	/**
	 * sets the isTunnelVisitedR variable to true, indicating that this tunnel has been visited by the player
	 * 
	 * @param visit 
	 * 			true if the tunnel should be visible, else false
	 */
	public void setIsVisitedR(boolean visit) {
		isTunnelVisitedR = visit;
		if (isTunnelVisitedR && isTunnelVisitedL)
			isVisited = true;
	}
	
	/**
	 * sets the isTunnelVisitedL variable to true, indicating that this tunnel has been visited by the player
	 * 
	 * @param visit 
	 * 			true if the tunnel should be visible, else false
	 */
	public void setIsVisitedL(boolean visit) {
		isTunnelVisitedL = visit;
		if (isTunnelVisitedR && isTunnelVisitedL)
			isVisited = true;
	}
	
	/**
	 * @return isVisited
	 * 				returns the isVisted instance variable
	 */
	public boolean isVisited() {
		return isVisited;
	}
	
	/**
	 * @return isTunnelVisitedR
	 * 				returns the isTunnelVisitedR instance variable
	 */
	public boolean isVisitedR() {
		return isTunnelVisitedR;
	}
	
	/**
	 * @return isTunnelVisitedL
	 * 				returns the isTunnelVisitedL instance variable
	 */
	public boolean isVisitedL() {
		return isTunnelVisitedL;
	}
	
	/**
	 * Adds a pit to this room, as long as the room is not a tunnel.
	 * 
	 * @return
	 * 			true is returned if the pit was added, and false if it was not added to the room
	 */
	public boolean setIsPit() {
		if (this.isTunnel || this.hasWumpus || this.isReverseTunnel)
			return false;
		isPit = true;
		return true;
	}

	/**
	 * @return isPit
	 * 				returns the isPit instance variable
	 */
	public boolean isPit() {
		return isPit;
	}
	
	
	/**
	 * Adds or removes blood from this room, as long as the room is not a tunnel.
	 * 
	 * @param blood
	 * 				false if blood is to be removed from the room, and true if it is to be added to the room
	 * @return
	 * 			true is returned if blood was added, and false if it was not added to the room
	 */
	public boolean setHasBlood(boolean blood) {
		if (this.isTunnel || this.hasWumpus || this.isReverseTunnel || this.isPit())
			return false;
		hasBlood = blood;
		return true;
	}
	
	/**
	 * @return
	 * 			true, if this room contains slime, else false is returned
	 */
	public boolean hasSlime() {
		return hasSlime;
	}
	
	/**
	 * Adds slime to this room, as long as the room is not a tunnel.
	 * 
	 * @return
	 * 			true is returned if the slime was added, and false if it was not added to the room
	 */
	public boolean setSlime() {
		if (this.isTunnel || this.isReverseTunnel || this.hasWumpus || this.isPit)
			return false;
		hasSlime = true;
		return true;
	}
	
	/**
	 * @return hasBlood
	 * 				returns the hasBlood instance variable
	 */
	public boolean getHasBlood() {
		return hasBlood;
	}
	
	/**
	 * Adds or removes a wumpus from this room, as long as the room is not a tunnel.
	 * 
	 * @param wumpus
	 * 				false if the wumpus is to be removed from the room, and true if it is to be added to the room
	 * @return
	 * 			true is returned if the wumpus was added, and false if it was not added to the room
	 */
	public boolean setWumpus(boolean wumpus) {
		if (this.isTunnel || this.isReverseTunnel || this.isPit || this.isReverseTunnel)
			return false;
		hasWumpus = wumpus;
		return true;
	}
	
	/**
	 * @return hasWumpus
	 * 				returns the hasWumpus instance variable
	 */
	public boolean hasWumpus() {
		return hasWumpus;
	}
	
	/**
	 * Adds or removes a bat from this room, as long as the room is not a tunnel.
	 * 
	 * @param hasBat
	 * 				false if the bat is to be removed from the room, and true if it is to be added to the room
	 * @return
	 * 			true is returned if the bat was added, and false if it was not added to the room
	 */
	public boolean setBat(boolean hasBat) {
		if (this.isTunnel || this.isReverseTunnel)
			return false;
		this.hasBat = hasBat;
		return true;
	}
	
	/**
	 * @return hasBat
	 * 				returns the hasBat instance variable
	 */
	public boolean hasBat() {
		return hasBat;
	}
	
	/**
	 * @return isTunnel
	 * 				returns the isTunnel instance variable
	 */
	public boolean isTunnel() {
		return isTunnel;
	}
	
	/**
	 * @return location
	 * 				returns the location instance variable
	 */
	public Point getLocation() {
		return location;
	}

}

