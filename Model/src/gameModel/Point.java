package gameModel;

import java.io.Serializable;

/**
 * Point is a single unit on the coordinate grid of the HuntTheWumpus game
 * containing an x and y value. Uses the standard Cartesian coordinate system.
 * 
 * @author Salika Dunatunga, Sarah Lutjens, Jane Wang, Denise Werchan
 */
public class Point implements Serializable {
	private final int xCoor;
	private final int yCoor;

	/**
	 * Creates a point at the coordinates (0,0).
	 */
	public Point() {
		xCoor = 0;
		yCoor = 0;
	}

	/**
	 * Creates a point at the coordinates (x,y).
	 * 
	 * @param x
	 *            The x coordinate of the point to be created.
	 * @param y
	 *            The y coordinate of the point to be created.
	 */
	public Point(int x, int y) {
		xCoor = x;
		yCoor = y;
	}

	/**
	 * Get the x value of the point.
	 * 
	 * @return The x coordinate of the point as an int.
	 */
	public int getXCoor() {
		return xCoor;
	}

	/**
	 * Get the y value of the point.
	 * 
	 * @return The y coordinate of the point as an int.
	 */
	public int getYCoor() {
		return yCoor;
	}

	/**
	 * Compares the x and y coordinates of two points to determine if the points
	 * are the same.
	 * 
	 * @param otherPoint
	 *            The point to which this point should compare.
	 * @return True if both points contain the same x and y coordinate; false if
	 *         either the x or y coordinates are different.
	 */
	public boolean compareTo(Point otherPoint) {
		if (this.xCoor == otherPoint.xCoor && this.yCoor == otherPoint.yCoor)
			return true;
		else
			return false;
	}

	public String toString() {
		return ("(" + xCoor + ", " + yCoor + ")");
	}
}
