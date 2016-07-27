package gameModel;

import java.io.Serializable;

/**
 * TeleportBlocker is a type of item which prevents a bat from teleporting the
 * player whom uses it.
 * 
 * @author Salika Dunatunga, Sarah Lutjens, Jane Wang, Denise Werchan
 * 
 */

public class TeleportBlocker extends Item implements Serializable {

	/*
	 * (non-Javadoc)
	 * 
	 * @see gameModel.Item#activateEffect(gameModel.Player)
	 */
	@Override
	public void activateEffect(Player p) {
		p.setTeleportBlock(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gameModel.Item#toString(gameModel.Player)
	 */
	public String toString() {
		return "TeleportBlocker";
	}

}
