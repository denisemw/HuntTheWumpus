package gameModel;

/**
 * A computer controllable object is an abstract class which identifies some
 * stuff. I really don't know what this is doing anoymore. DENISE!! D:
 * 
 * @author Cody Mingus -- modified by Denise Werchan
 */
public abstract class ComputerControllable {

	protected int delay;

	/**
	 * @return The number of ticks this ComputerControllable object waits before
	 *         acting again.
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * This method returns the player object.
	 * 
	 * @return the Player object associated with the computer controllable
	 */
	public abstract Player getPlayer();

	/**
	 * This method returns the location of the object
	 * 
	 * @return a Point representing the location of the object
	 */
	public abstract Point getLocation();

	/**
	 * This method returns the leeway of the object
	 * 
	 * @return a Point representing the leeway of the object
	 */
	public abstract Point getLeeway();
}