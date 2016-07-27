package gameModel;

import java.io.Serializable;

/**
 * An item has a location, visibility, and effect in the HuntTheWumpus game.
 * This is an abstract class.
 * 
 * @author Salika Dunatunga, Sarah Lutjens, Jane Wang, Denise Werchan
 * 
 */
@SuppressWarnings("serial")
public abstract class Item implements Serializable {

	private Point location, leeway;
	private boolean isVisible = false;
	private int level;

	/**
	 * Using the command pattern, an Item will improve the state of the Player
	 * when used.
	 * 
	 * @param p
	 *            the Player receiving the benefit.
	 */
	public abstract void activateEffect(Player p);

	/**
	 * Sets the location of this item.
	 * 
	 * @param p
	 *            the Point to set the location to
	 */
	public void setLocation(Point p) {
		location = p;
	}
	
	/**
	 * Sets the level that the item is on
	 * 
	 * @param level
	 * 			an int representing the level
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	
	/**
	 * Returns the level that the item is on
	 * 
	 * @return
	 * 		an int representing the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Sets the item to visible.
	 */
	public void setIsVisible() {
		isVisible = true;
	}

	/**
	 * Returns the visibility of the item; true if the item is visible, false if
	 * not.
	 * 
	 * @return item visibility as a boolean
	 */
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * Sets the leeway of this item.
	 * 
	 * @param p
	 *            the Point to set the leeway to
	 */
	public void setLeeway(Point p) {
		leeway = p;
	}

	/**
	 * Returns the leeway of this item
	 * 
	 * @return a Point representing the leeway
	 */
	public Point getLeeway() {
		return leeway;
	}

	/**
	 * Returns the location of this item
	 * 
	 * @return a Point representing the location
	 */
	public Point getLocation() {
		return location;
	}

	/**
	 * Return a string name of the item. @ return String name of the item.
	 */
	public abstract String toString();

}