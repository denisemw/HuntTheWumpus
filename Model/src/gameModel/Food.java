package gameModel;

import java.io.Serializable;

/**
 * Food is a type of item which adds 20 energy to the player.
 * 
 * @author Salika Dunatunga, Sarah Lutjens, Jane Wang, Denise Werchan
 */

public class Food extends Item implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see gameModel.Item#activateEffect(gameModel.Player)
	 */
	@Override
	public void activateEffect(Player p) {
		p.changeEnergy(20);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gameModel.Item#toString(gameModel.Player)
	 */
	public String toString() {
		return "Food";
	}

}
