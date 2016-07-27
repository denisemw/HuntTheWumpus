package gameModel;

import java.io.Serializable;

/**
 * Silencer is a type of item which adds a shot to the player.
 * 
 * @author Salika Dunatunga, Sarah Lutjens, Jane Wang, Denise Werchan
 * 
 */
public class Silencer extends Item implements Serializable {

	/*
	 * (non-Javadoc)
	 * 
	 * @see gameModel.Item#activateEffect(gameModel.Player)
	 */
	public void activateEffect(Player p) {
		p.addShot();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gameModel.Item#toString(gameModel.Player)
	 */
	public String toString() {
		return "Silencer";
	}
}
