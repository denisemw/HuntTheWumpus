package View;

import gameModel.Direction;
import gameModel.Food;
import gameModel.HuntTheWumpus;
import gameModel.Item;
import gameModel.Player;
import gameModel.Point;
import gameModel.Room;
import gameModel.Silencer;
import gameModel.TeleportBlocker;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * This is the RoomPanel class for JPanel. It also contains a private inner
 * class that animates the player.
 * 
 * @author Salika Dunatunga, Sarah Lutjens, Jang Wang, Denise Werchan
 * 
 */

public class RoomPanel extends JPanel {

	private ThemeGenerator themeGen;
	private HuntTheWumpus game;
	private boolean playerMoved;
	private Direction moveDirection;
	private PlayerPanel playerPanel;
	private Player player;
	private Room[][][] rooms;

	/**
	 * This is the constructor for RoomPanel. It sets up the panel that shows
	 * the inside of a room
	 * 
	 * @param g
	 *            instance of HuntTheWumpus for this game
	 * @param gt
	 *            instance of GameType -- the selected theme
	 * @param moved
	 *            boolean that indicates if the player has moved or not
	 * @param m
	 *            instance of the MasterView that contains this JPanel
	 */
	public RoomPanel(HuntTheWumpus g, GameType gt, boolean moved, MasterView m) {
		game = g;
		themeGen = new ThemeGenerator(gt);
		playerMoved = moved;
		rooms = g.getRooms();
		this.setLayout(null);
		this.setSize(m.getWidth() / 2, m.getHeight() / 2);
		playerPanel = new PlayerPanel();
		playerPanel.setSize(this.getWidth(), this.getHeight());
		playerPanel.setLocation(0, 0);
		playerPanel.setVisible(true);
		playerPanel.setOpaque(false);
		this.add(playerPanel);
	}

	/**
	 * Sets the player movement
	 * 
	 * @param moved
	 *            a boolean that indicates if the player moved
	 * @param dir
	 *            an instance of Direction that indicates the last direction
	 *            moved by the player
	 * @param p
	 *            an instance of Player to indicate which player moved
	 */
	public void setMoved(boolean moved, Direction dir, Player p) {
		int totalSteps = 8;
		player = p;
		playerMoved = moved;
		
		if (dir == Direction.STILL)
			moved = false;

		if (moved) {
			for (int i = totalSteps; i > 0; i--) {
				playerPanel.playerMoved(dir, i, totalSteps);
				this.update(this.getGraphics());
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		playerMoved = false;
		p.setLastMove(Direction.STILL);
	}

	/**
	 * This is the paintComponent() method that overrides JPanel's
	 * paintComponent
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int h = this.getHeight();
		int w = this.getWidth();
		Point playerLocation = game.getPlayer().getLocation();

		Room rooms[][] = game.getCurrLevel();
		Room currentRoom = rooms[playerLocation.getXCoor()][playerLocation
				.getYCoor()];

		if (currentRoom.isReverseTunnel() && currentRoom.isVisitedL()
				&& !currentRoom.isVisitedR()) {
			// this is a reverse tunnel that has been visited on left, but not
			// right
			g.drawImage(themeGen.roomTopLeftTunnel, 0, 0, w, h, null);
		} else if (currentRoom.isReverseTunnel() && !currentRoom.isVisitedL()
				&& currentRoom.isVisitedR()) {
			// this is a reverse tunnel that has been visited on right, but not
			// left
			g.drawImage(themeGen.roomBottomRightTunnel, 0, 0, w, h, null);
		} else if (currentRoom.isReverseTunnel() && currentRoom.isVisitedL()
				&& currentRoom.isVisitedR()) {
			// this is a reverse tunnel that has been visited on both sides
			g.drawImage(themeGen.roomBothReverseTunnels, 0, 0, w, h, null);
		} else if (currentRoom.isTunnel() && currentRoom.isVisitedL()
				&& !currentRoom.isVisitedR()) {
			// this is a regular tunnel that has been visited on left, but not
			// right
			g.drawImage(themeGen.roomBottomLeftTunnel, 0, 0, w, h, null);
		} else if (currentRoom.isTunnel() && !currentRoom.isVisitedL()
				&& currentRoom.isVisitedR()) {
			// this is a regular tunnel that has been visited on right, but not
			// left
			g.drawImage(themeGen.roomTopRightTunnel, 0, 0, w, h, null);
		} else if (currentRoom.isTunnel() && currentRoom.isVisitedL()
				&& currentRoom.isVisitedR()) {
			// this is a regular tunnel that has been visited on both sides
			g.drawImage(themeGen.roomBothTunnels, 0, 0, w, h, null);
		} else { // it's a regular room
			if (currentRoom.getHasBlood() && currentRoom.hasSlime())
				g.drawImage(themeGen.slimeAndBloodRoom, 0, 0, w, h, null);
			else if (currentRoom.getHasBlood())
				g.drawImage(themeGen.bloodRoom, 0, 0, w, h, null);
			else if (currentRoom.hasSlime())
				g.drawImage(themeGen.slimeRoom, 0, 0, w, h, null);
			else
				g.drawImage(themeGen.roomRegular, 0, 0, w, h, null);
			if (currentRoom.hasBat())
				g.drawImage(themeGen.batRoom, 0, 0, w, h, null);
			if (currentRoom.hasLadder())
				g.drawImage(themeGen.ladderRoom, 0, 0, w, h, null);

		}

		if (currentRoom.hasItem()) {
			Item item = currentRoom.getItem();
			if (((item.getLeeway().compareTo(new Point(0, 1)) || item
					.getLeeway().compareTo(new Point(2, 0))) && currentRoom
					.isVisitedL())
					|| ((item.getLeeway().compareTo(new Point(3, 2)) || item
							.getLeeway().compareTo(new Point(0, 2))) && currentRoom
							.isVisitedR()) || currentRoom.isVisited()) {

				if (item instanceof Food
						&& rooms[item.getLocation().getXCoor()][item
								.getLocation().getYCoor()].hasItem()) {
					g.drawImage(themeGen.foodImage,
							item.getLeeway().getYCoor() * 120 + 20, item
									.getLeeway().getXCoor() * 97, 80, 80, null);
				} else if (item instanceof Silencer
						&& rooms[item.getLocation().getXCoor()][item
								.getLocation().getYCoor()].hasItem()) {
					g.drawImage(themeGen.silencerImage, item.getLeeway()
							.getYCoor() * 120 + 20,
							item.getLeeway().getXCoor() * 97, 80, 80, null);
				} else if (item instanceof TeleportBlocker
						&& rooms[item.getLocation().getXCoor()][item
								.getLocation().getYCoor()].hasItem()) {
					g.drawImage(themeGen.blockerImage, item.getLeeway()
							.getYCoor() * 120 + 20,
							item.getLeeway().getXCoor() * 97, 80, 80, null);
				}
			}

		}
	}

	private class PlayerPanel extends JPanel {
		private Direction direction;
		private int currentStep = 0, totalSteps = 1, offset;

		public PlayerPanel() {
			direction = Direction.UP;
		}

		private void playerMoved(Direction dir, int i, int j) {
			direction = dir;
			currentStep = i;
			totalSteps = j;
			if (!(currentStep == 1))
				if (currentStep % 2 == 0)
					offset = 40;
				else
					offset = 20;
			else
				offset = 0;
		}

		public void paintComponent(Graphics g) {
			int h = RoomPanel.this.getHeight();
			int w = RoomPanel.this.getWidth();
			this.setSize(w, h);

			Point playerLocation = game.getPlayer().getLocation();
			Image playerImage[] = themeGen.playerImage;

			for (int i = 0; i < game.getPlayerList().size(); i++) {
				Player currentPlayer = game.getPlayerList().get(i);
				if (player == null || currentPlayer.equals(player)) {
					if (currentPlayer.isVisible()
							&& currentPlayer.getLocation().compareTo(
									playerLocation) && !currentPlayer.isDead()) {
						switch (direction) {
						default:
							if (currentPlayer.getTeleportBlock()) {
								Image blockerImage = new ImageIcon(
										"Images/blockerGlow.png").getImage();
								g.drawImage(blockerImage, currentPlayer
										.getLeeway().getYCoor() * (w / 4),
										currentPlayer.getLeeway().getXCoor()
												* (h / 4), w / 4, h / 4, null);
							}
							g.drawImage(playerImage[i % 5 + offset],
									currentPlayer.getLeeway().getYCoor()
											* (w / 4), currentPlayer
											.getLeeway().getXCoor() * (h / 4),
									w / 4, h / 4, null);
							break;
						case DOWN:
							if (currentPlayer.getTeleportBlock()) {
								Image blockerImage = new ImageIcon(
										"Images/blockerGlow.png").getImage();
								g.drawImage(blockerImage, currentPlayer
										.getLeeway().getYCoor() * (w / 4),
										currentPlayer.getLeeway().getXCoor()
												* (h / 4) - h * currentStep
												/ (4 * totalSteps), w / 4,
										h / 4, null);
							}
							g.drawImage(playerImage[i % 5 + 5 + offset],
									currentPlayer.getLeeway().getYCoor()
											* (w / 4), currentPlayer
											.getLeeway().getXCoor()
											* (h / 4)
											- h
											* currentStep
											/ (4 * totalSteps), w / 4, h / 4,
									null);
							break;
						case UP:
							if (currentPlayer.getTeleportBlock()) {
								Image blockerImage = new ImageIcon(
										"Images/blockerGlow.png").getImage();
								g.drawImage(blockerImage, currentPlayer
										.getLeeway().getYCoor() * (w / 4),
										currentPlayer.getLeeway().getXCoor()
												* (h / 4) + h * currentStep
												/ (4 * totalSteps), w / 4,
										h / 4, null);
							}
							g.drawImage(playerImage[i % 5 + 10 + offset],
									currentPlayer.getLeeway().getYCoor()
											* (w / 4), currentPlayer
											.getLeeway().getXCoor()
											* (h / 4)
											+ h
											* currentStep
											/ (4 * totalSteps), w / 4, h / 4,
									null);
							break;
						case LEFT:
							if (currentPlayer.getTeleportBlock()) {
								Image blockerImage = new ImageIcon(
										"Images/blockerGlow.png").getImage();
								g.drawImage(blockerImage, currentPlayer
										.getLeeway().getYCoor()
										* (w / 4)
										+ w
										* currentStep / (4 * totalSteps),
										currentPlayer.getLeeway().getXCoor()
												* (h / 4), w / 4, h / 4, null);
							}
							g.drawImage(playerImage[i % 5 + 15 + offset],
									currentPlayer.getLeeway().getYCoor()
											* (w / 4) + w * currentStep
											/ (4 * totalSteps), currentPlayer
											.getLeeway().getXCoor() * (h / 4),
									w / 4, h / 4, null);
							break;
						case RIGHT:
							if (currentPlayer.getTeleportBlock()) {
								Image blockerImage = new ImageIcon(
										"Images/blockerGlow.png").getImage();
								g.drawImage(blockerImage, currentPlayer
										.getLeeway().getYCoor()
										* (w / 4)
										- w
										* currentStep / (4 * totalSteps),
										currentPlayer.getLeeway().getXCoor()
												* (h / 4), w / 4, h / 4, null);
							}
							g.drawImage(playerImage[i % 5 + 20 + offset],
									currentPlayer.getLeeway().getYCoor()
											* (w / 4) - w * currentStep
											/ (4 * totalSteps), currentPlayer
											.getLeeway().getXCoor() * (h / 4),
									w / 4, h / 4, null);
							break;
						}
					}
				} else {
					if (currentPlayer.isVisible()
							&& currentPlayer.getLocation().compareTo(
									playerLocation) && !currentPlayer.isDead()) {
						if (currentPlayer.getTeleportBlock()) {
							Image blockerImage = new ImageIcon(
									"Images/blockerGlow.png").getImage();
							g.drawImage(blockerImage, currentPlayer.getLeeway()
									.getYCoor() * (w / 4), currentPlayer
									.getLeeway().getXCoor() * (h / 4), w / 4,
									h / 4, null);
						}

						switch (currentPlayer.getLastMove()) {
						default:
							g.drawImage(playerImage[i % 5], currentPlayer
									.getLeeway().getYCoor() * (w / 4),
									currentPlayer.getLeeway().getXCoor()
											* (h / 4), w / 4, h / 4, null);
							break;
						case DOWN:
							g.drawImage(playerImage[i % 5 + 5], currentPlayer
									.getLeeway().getYCoor() * (w / 4),
									currentPlayer.getLeeway().getXCoor()
											* (h / 4), w / 4, h / 4, null);
							break;
						case UP:
							g.drawImage(playerImage[i % 5 + 10], currentPlayer
									.getLeeway().getYCoor() * (w / 4),
									currentPlayer.getLeeway().getXCoor()
											* (h / 4), w / 4, h / 4, null);
							break;
						case LEFT:
							g.drawImage(playerImage[i % 5 + 15], currentPlayer
									.getLeeway().getYCoor() * (w / 4),
									currentPlayer.getLeeway().getXCoor()
											* (h / 4), w / 4, h / 4, null);
							break;
						case RIGHT:
							g.drawImage(playerImage[i % 5 + 20], currentPlayer
									.getLeeway().getYCoor() * (w / 4),
									currentPlayer.getLeeway().getXCoor()
											* (h / 4), w / 4, h / 4, null);
							break;
						}
					}
				}
			}
		}
	}
}
