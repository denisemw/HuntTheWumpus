package View;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;

/**
 * The ThemeGenerator sets up images for the game map and background according
 * to the desired theme identified by the user.
 * 
 * @author Salika Dunatunga, Sarah Lutjens, Jane Wang, Denise Werchan
 */
public class ThemeGenerator {

	// Images

	public Image background;
	public Image notVisited;
	public Image hasBlood;
	public Image hasSlime;
	public Image normalTunnel;
	public Image rightTunnel;
	public Image leftTunnel;
	public Image reverseRightTunnel;
	public Image hasReverseTunnel;
	public Image reverseLeftTunnel;
	public Image pit;
	public Image slimeAndBlood;
	public Image wumpus;
	public Image bat;
	public Image regularRoom;
	public Image mapItem;
	public Image playerImage[];
	public Image inventoryPicture;
	public Image foodImage;
	public Image silencerImage;
	public Image blockerImage;
	public Image roomTopLeftTunnel;
	public Image roomBottomRightTunnel;
	public Image roomBothReverseTunnels;
	public Image roomBottomLeftTunnel;
	public Image roomTopRightTunnel;
	public Image roomBothTunnels;
	public Image roomRegular;
	public Image ladder;
	public Image bloodRoom;
	public Image slimeRoom;
	public Image slimeAndBloodRoom;
	public Image batRoom;
	public Image ladderRoom;
	public Font inventoryFont;
	public Color inventoryFontColor;
	public Color multiplayerPanelColor;

	/**
	 * Sets up all of the images depending on the theme chosen by the player.
	 * 
	 * @param g
	 *            the GameType chosen by the player
	 */
	public ThemeGenerator(GameType g) {
		// setup classic images and fonts
		if (g == GameType.SPACE) {
			background = new ImageIcon("Images/Space/spaceBackground.png")
					.getImage();
			inventoryPicture = new ImageIcon("Images/Space/monitor.png")
					.getImage();
			foodImage = new ImageIcon("Images/Classic/food.png").getImage();
			silencerImage = new ImageIcon("Images/Classic/Silencer.png")
					.getImage();
			blockerImage = new ImageIcon("Images/Classic/blocker.png")
					.getImage();

			roomTopLeftTunnel = new ImageIcon("Images/Space/topLeftTunnel.png")
					.getImage();
			roomBottomRightTunnel = new ImageIcon(
					"Images/Space/bottomRightTunnel.png").getImage();
			roomBothReverseTunnels = new ImageIcon(
					"Images/Space/bothReverseTunnels.png").getImage();
			roomBottomLeftTunnel = new ImageIcon(
					"Images/Space/bottomLeftTunnel.png").getImage();
			roomTopRightTunnel = new ImageIcon(
					"Images/Space/topRightTunnel.png").getImage();
			roomBothTunnels = new ImageIcon("Images/Space/bothTunnels.png")
					.getImage();
			roomRegular = new ImageIcon("Images/Space/room.png").getImage();
			bloodRoom = new ImageIcon("Images/Space/bloodRoom.png").getImage();
			slimeRoom = new ImageIcon("Images/Space/slimeRoom.png").getImage();
			slimeAndBloodRoom = new ImageIcon(
					"Images/Space/slimeAndBloodRoom.png").getImage();
			batRoom = new ImageIcon("Images/Space/batRoom.png").getImage();
			ladderRoom = new ImageIcon("Images/Space/ladderRoom.png")
					.getImage();

			inventoryFont = new Font("Miriam Fixed", Font.BOLD, 30);
			inventoryFontColor = Color.GREEN;

			multiplayerPanelColor = new Color(159, 159, 255, 200);

			setPlayerImages();

			notVisited = new ImageIcon("Images/Classic/notVisited.png")
					.getImage();
			hasBlood = new ImageIcon("Images/Space/hasBlood.png").getImage();
			hasSlime = new ImageIcon("Images/Space/hasSlime.png").getImage();
			normalTunnel = new ImageIcon("Images/Space/hasTunnel.png")
					.getImage();
			rightTunnel = new ImageIcon("Images/Space/rightTunnel.png")
					.getImage();
			leftTunnel = new ImageIcon("Images/Space/leftTunnel.png")
					.getImage();
			reverseRightTunnel = new ImageIcon("Images/Space/lowerTunnel.png")
					.getImage();
			hasReverseTunnel = new ImageIcon(
					"Images/Space/hasReverseTunnel.png").getImage();
			reverseLeftTunnel = new ImageIcon("Images/Space/topTunnel.png")
					.getImage();
			pit = new ImageIcon("Images/Space/pit.png").getImage();
			slimeAndBlood = new ImageIcon("Images/Space/slimeAndBlood.png")
					.getImage();
			wumpus = new ImageIcon("Images/Space/wumpus.png").getImage();
			bat = new ImageIcon("Images/Space/bat.png").getImage();
			regularRoom = new ImageIcon("Images/Space/regularRoom.png")
					.getImage();
			mapItem = new ImageIcon("Images/Space/mapItem.png").getImage();
			ladder = new ImageIcon("Images/Space/ladder.png").getImage();

		} else if (g == GameType.WESTERN) {
			background = new ImageIcon("Images/Western/westernBackground.png")
					.getImage();
			inventoryPicture = new ImageIcon("Images/Western/wantedPoster.png")
					.getImage();
			foodImage = new ImageIcon("Images/Classic/food.png").getImage();
			silencerImage = new ImageIcon("Images/Classic/Silencer.png")
					.getImage();
			blockerImage = new ImageIcon("Images/Classic/blocker.png")
					.getImage();

			roomTopLeftTunnel = new ImageIcon(
					"Images/Western/topLeftTunnel.png").getImage();
			roomBottomRightTunnel = new ImageIcon(
					"Images/Western/bottomRightTunnel.png").getImage();
			roomBothReverseTunnels = new ImageIcon(
					"Images/Western/bothReverseTunnels.png").getImage();
			roomBottomLeftTunnel = new ImageIcon(
					"Images/Western/bottomLeftTunnel.png").getImage();
			roomTopRightTunnel = new ImageIcon(
					"Images/Western/topRightTunnel.png").getImage();
			roomBothTunnels = new ImageIcon("Images/Western/bothTunnels.png")
					.getImage();
			roomRegular = new ImageIcon("Images/Western/room.png").getImage();
			bloodRoom = new ImageIcon("Images/Western/bloodRoom.png")
					.getImage();
			slimeRoom = new ImageIcon("Images/Western/slimeRoom.png")
					.getImage();
			slimeAndBloodRoom = new ImageIcon(
					"Images/Western/slimeAndBloodRoom.png").getImage();
			batRoom = new ImageIcon("Images/Western/batRoom.png").getImage();
			ladderRoom = new ImageIcon("Images/Western/ladderRoom.png")
					.getImage();

			inventoryFont = new Font("Kunstler Script", Font.BOLD, 45);
			inventoryFontColor = new Color(108, 96, 70);

			multiplayerPanelColor = new Color(218, 184, 165, 200);

			setPlayerImages();

			notVisited = new ImageIcon("Images/Classic/notVisited.png")
					.getImage();
			hasBlood = new ImageIcon("Images/Western/hasBlood.png").getImage();
			hasSlime = new ImageIcon("Images/Western/hasSlime.png").getImage();
			normalTunnel = new ImageIcon("Images/Western/hasTunnel.png")
					.getImage();
			rightTunnel = new ImageIcon("Images/Western/rightTunnel.png")
					.getImage();
			leftTunnel = new ImageIcon("Images/Western/leftTunnel.png")
					.getImage();
			reverseRightTunnel = new ImageIcon("Images/Western/lowerTunnel.png")
					.getImage();
			hasReverseTunnel = new ImageIcon(
					"Images/Western/hasReverseTunnel.png").getImage();
			reverseLeftTunnel = new ImageIcon("Images/Western/topTunnel.png")
					.getImage();
			pit = new ImageIcon("Images/Western/pit.png").getImage();
			slimeAndBlood = new ImageIcon("Images/Western/slimeAndBlood.png")
					.getImage();
			wumpus = new ImageIcon("Images/Western/wumpus.png").getImage();
			bat = new ImageIcon("Images/Western/bat.png").getImage();
			regularRoom = new ImageIcon("Images/Western/regularRoom.png")
					.getImage();
			mapItem = new ImageIcon("Images/Western/mapItem.png").getImage();
			ladder = new ImageIcon("Images/Western/ladder.png").getImage();

		} else { // setup classic
			background = new ImageIcon("Images/Classic/classicBackground.png")
					.getImage();
			inventoryPicture = new ImageIcon("Images/Classic/parchment.png")
					.getImage();
			foodImage = new ImageIcon("Images/Classic/food.png").getImage();
			silencerImage = new ImageIcon("Images/Classic/Silencer.png")
					.getImage();
			blockerImage = new ImageIcon("Images/Classic/blocker.png")
					.getImage();

			roomTopLeftTunnel = new ImageIcon(
					"Images/Classic/topLeftTunnel.png").getImage();
			roomBottomRightTunnel = new ImageIcon(
					"Images/Classic/bottomRightTunnel.png").getImage();
			roomBothReverseTunnels = new ImageIcon(
					"Images/Classic/bothReverseTunnels.png").getImage();
			roomBottomLeftTunnel = new ImageIcon(
					"Images/Classic/bottomLeftTunnel.png").getImage();
			roomTopRightTunnel = new ImageIcon(
					"Images/Classic/topRightTunnel.png").getImage();
			roomBothTunnels = new ImageIcon("Images/Classic/bothTunnels.png")
					.getImage();
			roomRegular = new ImageIcon("Images/Classic/room.png").getImage();
			bloodRoom = new ImageIcon("Images/Classic/bloodRoom.png")
					.getImage();
			slimeRoom = new ImageIcon("Images/Classic/slimeRoom.png")
					.getImage();
			slimeAndBloodRoom = new ImageIcon(
					"Images/Classic/slimeAndBloodRoom.png").getImage();
			batRoom = new ImageIcon("Images/Classic/batRoom.png").getImage();
			ladderRoom = new ImageIcon("Images/Classic/ladderRoom.png")
					.getImage();

			inventoryFont = new Font("Kunstler Script", Font.BOLD, 45);
			inventoryFontColor = Color.DARK_GRAY;

			multiplayerPanelColor = new Color(185, 122, 87, 200);

			setPlayerImages();

			notVisited = new ImageIcon("Images/Classic/notVisited.png")
					.getImage();
			hasBlood = new ImageIcon("Images/Classic/hasBlood.png").getImage();
			hasSlime = new ImageIcon("Images/Classic/hasSlime.png").getImage();
			normalTunnel = new ImageIcon("Images/Classic/hasTunnel.png")
					.getImage();
			rightTunnel = new ImageIcon("Images/Classic/rightTunnel.png")
					.getImage();
			leftTunnel = new ImageIcon("Images/Classic/leftTunnel.png")
					.getImage();
			reverseRightTunnel = new ImageIcon("Images/Classic/lowerTunnel.png")
					.getImage();
			hasReverseTunnel = new ImageIcon(
					"Images/Classic/hasReverseTunnel.png").getImage();
			reverseLeftTunnel = new ImageIcon("Images/Classic/topTunnel.png")
					.getImage();
			pit = new ImageIcon("Images/Classic/pit.png").getImage();
			slimeAndBlood = new ImageIcon("Images/Classic/slimeAndBlood.png")
					.getImage();
			wumpus = new ImageIcon("Images/Classic/wumpus.png").getImage();
			bat = new ImageIcon("Images/Classic/bat.png").getImage();
			regularRoom = new ImageIcon("Images/Classic/classicRegularRoom.png")
					.getImage();
			mapItem = new ImageIcon("Images/Classic/mapItem.png").getImage();
			ladder = new ImageIcon("Images/Classic/ladder.png").getImage();
		}
	}

	private void setPlayerImages() {
		playerImage = new Image[65];
		playerImage[0] = new ImageIcon("Images/Classic/player.png").getImage();
		playerImage[1] = new ImageIcon("Images/Classic/player2.png").getImage();
		playerImage[2] = new ImageIcon("Images/Classic/player3.png").getImage();
		playerImage[3] = new ImageIcon("Images/Classic/player4.png").getImage();
		playerImage[4] = new ImageIcon("Images/Classic/player5.png").getImage();

		playerImage[5] = new ImageIcon("Images/Classic/playerDown1.png")
				.getImage();
		playerImage[6] = new ImageIcon("Images/Classic/player2Down1.png")
				.getImage();
		playerImage[7] = new ImageIcon("Images/Classic/player3Down1.png")
				.getImage();
		playerImage[8] = new ImageIcon("Images/Classic/player4Down1.png")
				.getImage();
		playerImage[9] = new ImageIcon("Images/Classic/player5Down1.png")
				.getImage();

		playerImage[10] = new ImageIcon("Images/Classic/playerUp1.png")
				.getImage();
		playerImage[11] = new ImageIcon("Images/Classic/player2Up1.png")
				.getImage();
		playerImage[12] = new ImageIcon("Images/Classic/player3Up1.png")
				.getImage();
		playerImage[13] = new ImageIcon("Images/Classic/player4Up1.png")
				.getImage();
		playerImage[14] = new ImageIcon("Images/Classic/player5Up1.png")
				.getImage();

		playerImage[15] = new ImageIcon("Images/Classic/playerLeft1.png")
				.getImage();
		playerImage[16] = new ImageIcon("Images/Classic/player2Left1.png")
				.getImage();
		playerImage[17] = new ImageIcon("Images/Classic/player3Left1.png")
				.getImage();
		playerImage[18] = new ImageIcon("Images/Classic/player4Left1.png")
				.getImage();
		playerImage[19] = new ImageIcon("Images/Classic/player5Left1.png")
				.getImage();

		playerImage[20] = new ImageIcon("Images/Classic/playerRight1.png")
				.getImage();
		playerImage[21] = new ImageIcon("Images/Classic/player2Right1.png")
				.getImage();
		playerImage[22] = new ImageIcon("Images/Classic/player3Right1.png")
				.getImage();
		playerImage[23] = new ImageIcon("Images/Classic/player4Right1.png")
				.getImage();
		playerImage[24] = new ImageIcon("Images/Classic/player5Right1.png")
				.getImage();

		playerImage[25] = new ImageIcon("Images/Classic/playerDown2.png")
				.getImage();
		playerImage[26] = new ImageIcon("Images/Classic/player2Down2.png")
				.getImage();
		playerImage[27] = new ImageIcon("Images/Classic/player3Down2.png")
				.getImage();
		playerImage[28] = new ImageIcon("Images/Classic/player4Down2.png")
				.getImage();
		playerImage[29] = new ImageIcon("Images/Classic/player5Down2.png")
				.getImage();

		playerImage[30] = new ImageIcon("Images/Classic/playerUp2.png")
				.getImage();
		playerImage[31] = new ImageIcon("Images/Classic/player2Up2.png")
				.getImage();
		playerImage[32] = new ImageIcon("Images/Classic/player3Up2.png")
				.getImage();
		playerImage[33] = new ImageIcon("Images/Classic/player4Up2.png")
				.getImage();
		playerImage[34] = new ImageIcon("Images/Classic/player5Up2.png")
				.getImage();

		playerImage[35] = new ImageIcon("Images/Classic/playerLeft2.png")
				.getImage();
		playerImage[36] = new ImageIcon("Images/Classic/player2Left2.png")
				.getImage();
		playerImage[37] = new ImageIcon("Images/Classic/player3Left2.png")
				.getImage();
		playerImage[38] = new ImageIcon("Images/Classic/player4Left2.png")
				.getImage();
		playerImage[39] = new ImageIcon("Images/Classic/player5Left2.png")
				.getImage();

		playerImage[40] = new ImageIcon("Images/Classic/playerRight2.png")
				.getImage();
		playerImage[41] = new ImageIcon("Images/Classic/player2Right2.png")
				.getImage();
		playerImage[42] = new ImageIcon("Images/Classic/player3Right2.png")
				.getImage();
		playerImage[43] = new ImageIcon("Images/Classic/player4Right2.png")
				.getImage();
		playerImage[44] = new ImageIcon("Images/Classic/player5Right2.png")
				.getImage();

		playerImage[45] = new ImageIcon("Images/Classic/playerDown3.png")
				.getImage();
		playerImage[46] = new ImageIcon("Images/Classic/player2Down3.png")
				.getImage();
		playerImage[47] = new ImageIcon("Images/Classic/player3Down3.png")
				.getImage();
		playerImage[48] = new ImageIcon("Images/Classic/player4Down3.png")
				.getImage();
		playerImage[49] = new ImageIcon("Images/Classic/player5Down3.png")
				.getImage();

		playerImage[50] = new ImageIcon("Images/Classic/playerUp3.png")
				.getImage();
		playerImage[51] = new ImageIcon("Images/Classic/player2Up3.png")
				.getImage();
		playerImage[52] = new ImageIcon("Images/Classic/player3Up3.png")
				.getImage();
		playerImage[53] = new ImageIcon("Images/Classic/player4Up3.png")
				.getImage();
		playerImage[54] = new ImageIcon("Images/Classic/player5Up3.png")
				.getImage();

		playerImage[55] = new ImageIcon("Images/Classic/playerLeft3.png")
				.getImage();
		playerImage[56] = new ImageIcon("Images/Classic/player2Left3.png")
				.getImage();
		playerImage[57] = new ImageIcon("Images/Classic/player3Left3.png")
				.getImage();
		playerImage[58] = new ImageIcon("Images/Classic/player4Left3.png")
				.getImage();
		playerImage[59] = new ImageIcon("Images/Classic/player5Left3.png")
				.getImage();

		playerImage[60] = new ImageIcon("Images/Classic/playerRight3.png")
				.getImage();
		playerImage[61] = new ImageIcon("Images/Classic/player2Right3.png")
				.getImage();
		playerImage[62] = new ImageIcon("Images/Classic/player3Right3.png")
				.getImage();
		playerImage[63] = new ImageIcon("Images/Classic/player4Right3.png")
				.getImage();
		playerImage[64] = new ImageIcon("Images/Classic/player5Right3.png")
				.getImage();
	}
}
