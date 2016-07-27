package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;


import gameModel.HuntTheWumpus;
import gameModel.Player;
import gameModel.Room;
import gameModel.WumpusMap;

/**
 * This is the MiniMap class. It sets the cells for each image on the map.
 * It also adds buttons to allow users to select which level of the map to
 * view.
 * 
 * @author Salika Dunatunga, Sarah Lutjens, Jang Wang, Denise Werchan
 * 
 */

public class MiniMap extends MasterViewPanel {
	private WumpusMap wumpusMap;
	private Room roomType;
	private Player player;
	private HuntTheWumpus game;
	private boolean isLarge;
	private GameType gameType;
	private ThemeGenerator themeGen;
	private JLabel mapLevel;
	private Room[][] rooms;
	private int currLevel; //0-2
	

	/**
	 * This is the constructor for MiniMap. It sets the layout for MiniMap
	 * 
	 * @param m
	 * 			The MasterView that shows this JPanel in GameView or as a card
	 * @param htw
	 * 			The HuntTheWumpus instance for this instance of the game
	 * @param g
	 * 			The theme selected for this instance of the game
	 * @param b
	 * 			A boolean flag that indicates whether to show the enlarged view of
	 * 			the map or the small JPanel inside GameView
	 */
	public MiniMap(MasterView m, HuntTheWumpus htw, GameType g, boolean b) {
		super(m);
		game = htw;
		wumpusMap = htw.getMap();
		player = htw.getPlayer();
		gameType = g;
		this.setLayout(null);
		this.setVisible(true);
		isLarge = b;
		addMouseListener();
		themeGen = new ThemeGenerator(g);
		mapLevel = new JLabel("Level: " + (player.getCurrLevel() + 1));
		currLevel = player.getCurrLevel();
		mapLevel.setOpaque(false);
		mapLevel.setForeground(Color.RED);
		mapLevel.setLocation(5, 5);
		mapLevel.setSize(100, 10);
		mapLevel.setVisible(true);
		this.add(mapLevel);
		rooms = game.getCurrLevel();
		
		if(game.isOver()){
			JOptionPane optionPane = new JOptionPane("Click 'File' and 'Return to Menu' to end the game.");
			JDialog dialog = optionPane.createDialog(m, "View the mini-map");
			dialog.setVisible(true);
		}
	}

	private void addMouseListener() {
		this.addMouseListener(new MouseAdapter(){
			
			public void mouseClicked(MouseEvent me) {
				if(isLarge)
					m.changeView(Views.GAME, gameType, game);
				else
					m.changeView(Views.MINIMAP, gameType, game);
		      }
			
		});
	}
		

	/**
	 * Overrides the paintComponent in JPanel. Allows for a repaint() to be called
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		
		Image playerImage[] = themeGen.playerImage;
		
		Image notVisited = themeGen.notVisited;
		Image hasBlood = themeGen.hasBlood;
		Image hasSlime = themeGen.hasSlime;
		Image leftTunnel = themeGen.leftTunnel;
		Image reverseRightTunnel = themeGen.reverseRightTunnel;
		Image hasReverseTunnel = themeGen.hasReverseTunnel;
		Image pit = themeGen.pit;
		Image rightTunnel = themeGen.rightTunnel;
		Image slimeAndBlood = themeGen.slimeAndBlood;
		Image reverseLeftTunnel = themeGen.reverseLeftTunnel;
		Image wumpus = themeGen.wumpus;
		Image bat = themeGen.bat;
		Image regularRoom = themeGen.regularRoom;
		Image mapItem = themeGen.mapItem;
		Image normalTunnel = themeGen.normalTunnel;
		Image ladder = themeGen.ladder;
		
		int h = ((m.getHeight()-40)/12)-1;
		int w = (m.getWidth()-10)/14;

		
		for (int i = 0; i < rooms.length; i++) {
			for (int j = 0; j < rooms[0].length; j++) {
				int y = i * h;
				int x = j * w;

				if (isLarge) {
					h = ((m.getHeight() - 40) / 6)-5;
					w = (m.getWidth()- 10) / 7;
					y = i * h;
					x = j * w;
					setButtons();
				}
				else{
					rooms = game.getCurrLevel();
					mapLevel.setText("Level: " + (player.getCurrLevel() + 1));
					currLevel = player.getCurrLevel();
				}

				// Room is tunnel, but neither side visited -- hide entire thing
				if (rooms[i][j].isTunnel() && !rooms[i][j].isVisitedR()
						&& !rooms[i][j].isVisitedL()&& !game.isOver()) {
					g.drawImage(notVisited, x, y, w, h, null);
				}

				//room is tunnel, and both sides are visited
				else if (rooms[i][j].isTunnel() && rooms[i][j].isVisitedR()
						&& rooms[i][j].isVisitedL()) {
					g.drawImage(normalTunnel, x, y, w, h, null);
				} 
				//room is tunnel, only left side visited - show only left side
				else if (rooms[i][j].isTunnel()
						&& rooms[i][j].isVisitedR() == false && !game.isOver()) {
					g.drawImage(leftTunnel, x, y, w, h, null);
				}
				//room is tunnel, only right side visited -- show only right side
				else if (rooms[i][j].isTunnel()
						&& rooms[i][j].isVisitedL() == false && !game.isOver()) {
					g.drawImage(rightTunnel, x, y, w, h, null);
				}

				else if (rooms[i][j].isReverseTunnel()
						&& !rooms[i][j].isVisitedR()
						&& !rooms[i][j].isVisitedL() && !game.isOver()) {
					g.drawImage(notVisited, x, y, w, h, null);
				}

				else if (rooms[i][j].isReverseTunnel()
						&& rooms[i][j].isVisitedR() && rooms[i][j].isVisitedL()) {
					g.drawImage(hasReverseTunnel, x, y, w, h, null);
				} 
				else if (rooms[i][j].isReverseTunnel()
						&& rooms[i][j].isVisitedR() == false && !game.isOver()) {
					g.drawImage(reverseLeftTunnel, x, y, w, h, null);
				}

				else if (rooms[i][j].isReverseTunnel()
						&& rooms[i][j].isVisitedL() == false && !game.isOver()) {
					g.drawImage(reverseRightTunnel, x, y, w, h, null);
				}

				else if (rooms[i][j].isVisited() == false && !game.isOver()) {
					g.drawImage(notVisited, x, y, w, h, null);
				}

				else if (rooms[i][j].getHasBlood() && rooms[i][j].hasSlime()) {
					g.drawImage(slimeAndBlood, x, y, w, h, null);
				}
				else if (rooms[i][j].isTunnel()){
					g.drawImage(normalTunnel, x, y, w, h, null);
				}
				else if (rooms[i][j].isReverseTunnel()){
					 g.drawImage(hasReverseTunnel, x, y, w, h, null);
				}
				else if (rooms[i][j].isPit()) {
					g.drawImage(pit, x, y, w, h, null);
				} 
				else if (rooms[i][j].hasSlime()) {
					g.drawImage(hasSlime, x, y, w, h, null);
				} 
				else if (rooms[i][j].getHasBlood() == true) {
					g.drawImage(hasBlood, x, y, w, h, null);
				} 
				else {
					g.drawImage(regularRoom, x, y, w, h, null);
				}
				if (rooms[i][j].hasWumpus() && (rooms[i][j].isVisited() || game.isOver())) {
					g.drawImage(wumpus, x, y, w, h, null);
				}
				
				if (rooms[i][j].hasBat() && (rooms[i][j].isVisited() || game.isOver())) {
					g.drawImage(bat, x + (w/4), y + (w/4), w/2, h/2, null);
				} 
				
				if ((rooms[i][j].isVisited() || game.isOver()) && rooms[i][j].hasItem()) {
					g.drawImage(mapItem, x, y, w, h, null);
				}
				
				if ((rooms[i][j].isVisited() || game.isOver()) && rooms[i][j].hasLadder()) {
					g.drawImage(ladder, x + (w/4), y + (w/4), w/2, h/2, null);
				}
			}
		}

		for (int i = 0; i < game.getPlayerList().size(); i++) {
			Player currentPlayer = game.getPlayerList().get(i);
			int playerX = currentPlayer.getLocation().getXCoor() * h;
			int playerY = currentPlayer.getLocation().getYCoor() * w;
			if (currentPlayer.isVisible() && !currentPlayer.isDead() && currentPlayer.getCurrLevel()==currLevel) {
				if(currentPlayer.getTeleportBlock()){
					Image blockerImage = new ImageIcon("Images/blockerGlow.png").getImage();
					g.drawImage(blockerImage, playerY, playerX, w, h, null);
				}
				g.drawImage(playerImage[i%5], playerY, playerX, w, h, null);
			}
		}
	}

	private void setButtons() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,3));
		buttonPanel.setSize(new Dimension(300,25));
		buttonPanel.setLocation((m.getWidth()/2) - 150, 0);
		buttonPanel.setVisible(true);
		this.add(buttonPanel);
		
		JButton level1 = new JButton("Level One");
		level1.setSize(new Dimension(100,25));
		level1.setLocation(0,0);
		level1.setVisible(true);
		level1.setFocusable(false);
		level1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Room[][][] map = game.getRooms();
				rooms = map[0];
				mapLevel.setText("Level: 1");
				currLevel = 0;
				MiniMap.this.repaint();
			}
		});
		buttonPanel.add(level1);
		JButton level2 = new JButton("Level Two");
		level2.setSize(new Dimension(100,25));
		level2.setLocation(100,0);
		level2.setVisible(true);
		level2.setFocusable(false);
		level2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Room[][][] map = game.getRooms();
				rooms = map[1];
				mapLevel.setText("Level: 2");
				currLevel = 1;
				MiniMap.this.repaint();
			}
		});
		buttonPanel.add(level2);
		JButton level3 = new JButton("Level Three");
		level3.setSize(new Dimension(100,25));
		level3.setLocation(200,0);
		level3.setVisible(true);
		level3.setFocusable(false);
		level3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Room[][][] map = game.getRooms();
				rooms = map[2];
				mapLevel.setText("Level: 3");
				currLevel = 2;
				MiniMap.this.repaint();
			}
		});
		buttonPanel.add(level3);
	}

	/**
	 * Returns a string name for this class.
	 * 
	 * @return "MiniMap", a string
	 */
	public String toString() {
		return "MiniMap";
	}
}
