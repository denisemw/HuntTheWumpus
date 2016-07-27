package View;

/**
 * An enum which contains the beginning "cutscenes" from the HuntTheWumpus game.
 * Separate from PlayAnimation since they are not *.gif files. Contains one
 * method to determine which image to display and which story to print out to
 * the user.
 * 
 * @author Salika Dunatunga, Sarah Lutjens, Jane Wang, Denise Werchan
 */
public enum Cutscene {
	/**
	 * Identifies the beginning cutscene as a classic theme
	 */
	CLASSIC_BEG, 
	/**
	 * Identifies the beginning cutscene as a western theme
	 */
	WESTERN_BEG, 
	/**
	 * Identifies the beginning cutscene as a space theme
	 */
	SPACE_BEG;

	/**
	 * Plays the desired beginning cutscene for the player.
	 * 
	 * @param cs
	 *            the cutscene type that should be played
	 * @param jp
	 *            the JPanel CutsceneView in which this cutscene will be
	 *            displayed
	 */
	public void playCutscene(Cutscene cs, CutsceneView jp) {
		String filePath = "Images/";
		String story = "";
		switch (cs) {
		case CLASSIC_BEG:
			filePath = filePath + "Classic/classicBeg.png";
			story = "One day while tending to the flock, you notice that some of your sheep are missing. You decide to stay the night to make sure that a thief is not stealing your sheep. That night, you see that a wumpus is eating your sheep and escaping back to its cave. You follow the wumpus to its cave and decide to slay the creature before all of the sheep are eaten.";
			break;
		case WESTERN_BEG:
			filePath = filePath + "Western/westernBeg.png";
			story = "Recently a large bounty has been called in on the one-of-a-kind, ever gluttonous wumpus. A new vein of gold has been found in the old mines where the wumpus lives, so the bank is hiring bounty hunters to permanently remove the beast. In desperate need of money, the $1,000,000 bounty is too tempting to pass up, so you head to the mines to claim your prize. ";
			break;
		case SPACE_BEG:
			filePath = filePath + "Space/spaceBeg.png";
			story = "While exploring the one of Jupiter’s moons, you realize that your base camp has been raided by indigenous life. Following the tracks of the great creature, littered with all of your important and essential equipment, you enter a treacherous part of the terrain where the much of the landscape drops into sheer cliffs. Without your equipment recovered, you will die at the base camp anyway, so you decide to eliminate the creature and reclaim your instruments.";
			break;
		default:
			break;

		}
		jp.draw(story, filePath);
	}
}
