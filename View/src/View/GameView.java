package View;

import gameModel.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


/**
 * This class is the GameView class. This class observes the HuntTheWumpus
 * class, which is the model for this game.
 * 
 * @author Salika Dunatunga, Sarah Lutjens, Jane Wang, Denise Werchan
 * 
 */
public class GameView extends MasterViewPanel implements Observer {

	private HuntTheWumpus game;
	private MasterView m;
	private List<Player> playerList;
	private JPanel topPanel, bottomPanel, chatPanel;
	private RoomPanel roomPanel;
	private JPanel multiplayerPanel, inventoryPanel, miniMapPanel;
	private GameType gt;
	private KeyListener movementListener;
	private KeyListener shootListener;
	private ThemeGenerator themeGen;
	private JLabel[] player;
	private JLabel[] playerEnergy;
	private int index = 0, wonIndex = 0; //this is here because update is called twice on death -- prevents dialogs from getting called twice
	private boolean isConnected;
	
	/**
	 * Creates a new MasterViewPanel and sets up the Panel for the main portion of the game.
	 * 
	 * @param MasterView
	 *            An instance of the MasterView that called this MasterViewPanel
	 * @param GameType
	 *            The theme of the game selected by the user
	 * @param HuntTheWumpus
	 *            The game model that the GameView should be observing.
	 */
	public GameView(MasterView m, GameType g, HuntTheWumpus htw) {
		super(m);
		this.playerList = htw.getPlayerList();
		this.m = m;
		isConnected = true;
		game = htw;
		game.addObserver(this);
		gt = g;
		themeGen = new ThemeGenerator(gt);
		this.setLayout(new GridLayout(2, 1));
		if (isConnected) {
			this.setUpTopPanel();
			this.setFocusable(true);
			this.setVisible(true);

			if (m.multiplayer == true) {
				this.setUpBottomPanelMultiPlayer();
			} else {
				this.setUpBottomPanel();
			}
			
			movementListener = new MovementListener();
			shootListener = new ShootListener();
			this.addKeyListener(movementListener);
		}
	}

	/**
	 * This method returns an instance of the game being used by the class
	 * 
	 * @return an instance of HuntTheWumpus being used by this class
	 */
	public HuntTheWumpus getGame() {
		return game;
	}
	
	/**
	 * This sets the isConnected flag to false
	 */
	public void setNotConnected() {
		isConnected = false;
	}

	private void setUpTopPanel() {
		topPanel = new JPanel();
		topPanel.setOpaque(false);
		topPanel.setLayout(new GridLayout(1,2));

		inventoryPanel = new InventoryPanel(game, this, gt);
		inventoryPanel.setOpaque(false);
		
		miniMapPanel = new MiniMap(m, game, gt, false);
		miniMapPanel.setLayout(null);
		miniMapPanel.setOpaque(false);
		miniMapPanel.repaint();
		miniMapPanel.setBackground(Color.black);
		
		topPanel.add(inventoryPanel);
		topPanel.add(miniMapPanel);
		
		this.add(topPanel);
	}
		
	private void setUpBottomPanel() {
		bottomPanel = new JPanel();
		bottomPanel.setOpaque(false);
		bottomPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(1,1,1,1);
		gbc.fill = GridBagConstraints.BOTH;
		
		chatPanel = new JPanel();
		chatPanel.setOpaque(false);
		
		roomPanel = new RoomPanel(game, gt, false, m);
		roomPanel.setSize(m.getWidth()/2, m.getHeight()/2);
		roomPanel.setOpaque(false);
		
		multiplayerPanel = new JPanel();
		multiplayerPanel.setOpaque(false);
		multiplayerPanel.setLayout(null);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = .25;
		gbc.weighty = 1;
		bottomPanel.add(chatPanel, gbc);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = .5;
		gbc.weighty = 1;
		bottomPanel.add(roomPanel, gbc);
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = .25;
		gbc.weighty = 1;
		bottomPanel.add(multiplayerPanel, gbc);
		
		this.add(bottomPanel);
	}
	
	

	private void setUpBottomPanelMultiPlayer(){
		bottomPanel = new JPanel();
		bottomPanel.setOpaque(false);
		bottomPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(1,1,1,1);
		gbc.fill = GridBagConstraints.BOTH;
		
		chatPanel = new ChatPanel(game, this);
		chatPanel.setOpaque(false);
		
		roomPanel = new RoomPanel(game, gt, false, m);
		roomPanel.setOpaque(false);
		
		multiplayerPanel = new JPanel();
		multiplayerPanel.setOpaque(true);
		multiplayerPanel.setBackground(themeGen.multiplayerPanelColor);
		setupMultiplayerPanel();
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = .25;
		gbc.weighty = 1;
		bottomPanel.add(chatPanel, gbc);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = .5;
		gbc.weighty = 1;
		bottomPanel.add(roomPanel, gbc);
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = .25;
		gbc.weighty = 1;
		bottomPanel.add(multiplayerPanel, gbc);
		
		this.add(bottomPanel);
	}
	
	/**
	 * This is the method called during repaint for this class
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(themeGen.background, 0, 0, this.getWidth(), this.getHeight(), null);
	}
	
	private void setupMultiplayerPanel(){
		multiplayerPanel.setLayout(new GridBagLayout());
		multiplayerPanel.setPreferredSize(new Dimension(25, m.getHeight()/2 - 60));
		
		JPanel namePanel = new JPanel();
		namePanel.setOpaque(false);
		namePanel.setLayout(new GridLayout(5, 1));
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = .9;
		c.weighty = 1;
		multiplayerPanel.add(namePanel,c);
		
		JPanel energyPanel = new JPanel();
		energyPanel.setLayout(new GridLayout(5,1));
		energyPanel.setOpaque(false);
		c.gridx = 1;
		c.weightx = .1;
		multiplayerPanel.add(energyPanel, c);
		
		JLabel playersNames = new JLabel("Players:");
		playersNames.setOpaque(false);
		playersNames.setForeground(Color.BLACK);
		playersNames.setFont(new Font("Arial", Font.BOLD, 20));
		namePanel.add(playersNames);
		
		JLabel playerEnergyTitle = new JLabel("Energy:");
		playerEnergyTitle.setOpaque(false);
		playerEnergyTitle.setForeground(Color.BLACK);
		playerEnergyTitle.setFont(new Font("Arial", Font.BOLD, 20));
		energyPanel.add(playerEnergyTitle);
		
		player = new JLabel[4];
		playerEnergy = new JLabel[4];
		
		//player 1
		player[0] = new JLabel(game.getPlayer().getName());
		player[0].setOpaque(false);
		player[0].setForeground(Color.MAGENTA);
		player[0].setFont(new Font("Arial", Font.PLAIN, 30));
		ImageIcon playerImage = new ImageIcon(themeGen.playerImage[0].getScaledInstance(40, 40, Image.SCALE_DEFAULT));
		player[0].setIcon(playerImage);
		player[0].setIconTextGap(0);
		namePanel.add(player[0]);
		playerEnergy[0] = new JLabel("" + game.getPlayer().getEnergy());
		playerEnergy[0].setOpaque(false);
		playerEnergy[0].setForeground(Color.MAGENTA);
		playerEnergy[0].setFont(new Font("Arial", Font.PLAIN, 30));
		playerEnergy[0].setHorizontalTextPosition(SwingConstants.CENTER);
		energyPanel.add(playerEnergy[0]);
		
		//player 2
		player[1] = new JLabel();
		player[1].setOpaque(false);
		player[1].setForeground(Color.GREEN);
		player[1].setFont(new Font("Arial", Font.PLAIN, 30));
		namePanel.add(player[1]);
		playerEnergy[1] = new JLabel();
		playerEnergy[1].setOpaque(false);
		playerEnergy[1].setForeground(Color.GREEN);
		playerEnergy[1].setFont(new Font("Arial", Font.PLAIN, 30));
		playerEnergy[1].setHorizontalTextPosition(JLabel.CENTER);
		energyPanel.add(playerEnergy[1]);
		
		//player 3
		player[2] = new JLabel();
		player[2].setOpaque(false);
		player[2].setForeground(Color.BLUE);
		player[2].setFont(new Font("Arial", Font.PLAIN, 30));
		namePanel.add(player[2]);
		playerEnergy[2] = new JLabel();
		playerEnergy[2].setOpaque(false);
		playerEnergy[2].setForeground(Color.BLUE);
		playerEnergy[2].setFont(new Font("Arial", Font.PLAIN, 30));
		playerEnergy[2].setHorizontalTextPosition(JLabel.CENTER);
		energyPanel.add(playerEnergy[2]);
		
		//player 4
		player[3] = new JLabel();
		player[3].setOpaque(false);
		player[3].setForeground(Color.RED);
		player[3].setFont(new Font("Arial", Font.PLAIN, 30));
		namePanel.add(player[3]);
		playerEnergy[3] = new JLabel();
		playerEnergy[3].setOpaque(false);
		playerEnergy[3].setForeground(Color.RED);
		playerEnergy[3].setFont(new Font("Arial", Font.PLAIN, 30));
		playerEnergy[3].setHorizontalTextPosition(JLabel.CENTER);
		energyPanel.add(playerEnergy[3]);
	}
	
	private void updatePlayerInfo() {
		for(int i = 0; i < game.getPlayerList().size(); i++){
			player[i].setVisible(false);
			playerEnergy[i].setVisible(false);
		}
		for(int i = 0; i < game.getPlayerList().size(); i++){
			player[i].setVisible(true);
			playerEnergy[i].setVisible(true);
			ImageIcon playerImage = new ImageIcon(themeGen.playerImage[i].getScaledInstance(40, 40, Image.SCALE_DEFAULT));
			player[i].setText(game.getPlayerList().get(i).getName());
			player[i].setIcon(playerImage);
			player[i].setIconTextGap(0);
			playerEnergy[i].setText("" + game.getPlayerList().get(i).getEnergy());
		}
	}
	
	/**
	 * This method calls repaint on RoomPanel and MiniMap
	 */
	public void paintRoom(){
		roomPanel.repaint();
		miniMapPanel.repaint();
	}
	
	/**
	 * Method called in the event that the game cannot reconnect to the server
	 */
	public void unableToConnectServer() {
		String[] options = new String[3];
		options[0] = "Single-player game";
		options[1] = "Host a game";
		options[2] = "Return to main menu";
		int choice = JOptionPane.showOptionDialog(m,
				"What would you like to do?", "Unable to connect to server",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options,
				options[0]);
		switch (choice) {
		case 0:
			m.changeView(Views.NEWGAME, gt, null);
			break;
		case 1:
			m.changeView(Views.HOSTGAME, gt, null);
			break;
		case 2:
			m.changeView(Views.TITLE, gt, null);
			break;
		case 3:
			break;
		default:
			m.changeView(Views.TITLE, gt, null);
			break;
		}
	}
	
	private int countDisconnect = 0;
	
	/**
	 * This is the method that overrides the update method in the Observer class
	 */
	@Override
	public void update(Observable arg0, Object obj) {

		if(m.multiplayer)
			updatePlayerInfo();
		
		// chat message for multiplayer game
		if (obj instanceof String) {
			String str = (String) obj;
			if (str.equals("Connection to server lost") && countDisconnect==0) {
				int n = JOptionPane.showConfirmDialog(null, "The server has disconnected" + " Would you like to save your game?", "Please pick one", JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION){
					game.saveGame();
					JOptionPane optionPane = new JOptionPane("Click OK to return to the main menu.");
					JDialog dialog = optionPane.createDialog(m, "Return to main menu");
					Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
					dialog.setLocation(screenSize.width/2 - 100, 50);
					dialog.setVisible(true);
					countDisconnect++;

				}
				m.changeView(Views.TITLE, gt, game);
			}
			else if (str.contains(" has disconnected")) {
				if (str.substring(0, 4).equals(" has")) {
					str = "A player" + str;
				}
				JOptionPane optionPane = new JOptionPane(str);
				JDialog j = optionPane.createDialog(m, "Player disconnected");
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				j.setLocation(screenSize.width/2 - 100, 50);
				j.setVisible(true);
			}
			else if (str.contains(" has connected")) {
				if (str.substring(0, 4).equals(" has")) {
					str = "A new player" + str;
				}
				JOptionPane optionPane = new JOptionPane(str);
				JDialog j = optionPane.createDialog(m, "New player connected");
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				j.setLocation(screenSize.width/2 - 100, 50);
				j.setVisible(true);
			}
			else if (str.contentEquals("MovedByBat")){
				m.changeView(Views.BATTAKINGPLAYER, gt, game);
			}
			else if (m.multiplayer) {
				((ChatPanel) chatPanel).addText((String) obj);
			}
		}
		
		else if (obj instanceof Player) {
			
			Player p = (Player) obj;
			
			// player is the user
			if (p.isDead() && game.isOver() && index == 0) {
				String deathBy;
				if(p.deathByPit()){
					deathBy = "You fell into a pit and died.";
					m.changeView(Views.FALLINGPLAYER, gt, game);
				}
				else if(p.deathByPlayer()){
					deathBy = "You were killed by another player.";
					m.changeView(Views.PLAYERGOTKILLED, gt, game);
				}
				else if (p.deathByWumpus()){
					deathBy = "You were killed by the Wumpus.";
					m.changeView(Views.HUNGRYWUMPUS, gt, game);
				}
				else{
					deathBy = "Your energy ran out and you died.";
					m.changeView(Views.STARVEDPLAYER, gt, game);
				}
				if(index == 0){
					int n = JOptionPane.showConfirmDialog(null, deathBy + " Would you like to see the map?", "Please pick one", JOptionPane.YES_NO_OPTION);
					if (n == JOptionPane.YES_OPTION){
						m.changeView(Views.MINIMAP, gt, game);
					}
					if(n == JOptionPane.NO_OPTION)
						m.changeView(Views.TITLE, gt, game);
				}
				index++;
			}

			else if (p.hasWon()) {
				if (gt.equals(GameType.CLASSIC))
					m.changeView(Views.DEADWUMPUS_CLASSICAL, gt, game);
				else if (gt.equals(GameType.SPACE))
					m.changeView(Views.DEADWUMPUS_SPACE, gt, game);
				else if (gt.equals(GameType.WESTERN))
					m.changeView(Views.DEADWUMPUS_WESTERN, gt, game);
								
				if(wonIndex == 0){
					int n = JOptionPane.showConfirmDialog(null, p.getName() + " has won. Would you like to see the map?", "Please pick one", JOptionPane.YES_NO_OPTION);
					if (n == JOptionPane.YES_OPTION){
						m.changeView(Views.MINIMAP, gt, game);
					}
					if(n == JOptionPane.NO_OPTION)
						m.changeView(Views.TITLE, gt, game);
				}
				wonIndex++;
			}
			else {
				roomPanel.setMoved(true, p.getLastMove(), p);
				roomPanel.repaint();
				inventoryPanel.repaint();
				miniMapPanel.repaint();
				
			}
		}
	
		else if (obj instanceof Item) {
			// unpaint item
		}
		repaint();
		
	}
	
	private class MovementListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {}
		@Override
		public void keyReleased(KeyEvent e) {
			int keyCode = e.getKeyCode();
			if (keyCode == KeyEvent.VK_UP) {
				game.getPlayer().moveUp(game.getMap(), game);
			} else if (keyCode == KeyEvent.VK_DOWN) {
				game.getPlayer().moveDown(game.getMap(), game);
			} else if (keyCode == KeyEvent.VK_LEFT) {
				game.getPlayer().moveLeft(game.getMap(), game);
			} else if (keyCode == KeyEvent.VK_RIGHT) {
				game.getPlayer().moveRight(game.getMap(), game);
			}
			else if (keyCode == KeyEvent.VK_S) {
				GameView.this.removeKeyListener(movementListener);
				GameView.this.addKeyListener(shootListener);
				JOptionPane.showMessageDialog(m, "You are in shooting mode.");
			}
			else if (keyCode == KeyEvent.VK_L) {
				game.climbLadder();
			}	
		}
		@Override
		public void keyTyped(KeyEvent e) {}
	}
	
	private class ShootListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			if (keyCode == KeyEvent.VK_UP) {
				shootAnime();
				game.shoot(Direction.UP);
				GameView.this.removeKeyListener(shootListener);
				GameView.this.addKeyListener(movementListener);
			} else if (keyCode == KeyEvent.VK_DOWN) {
				shootAnime();
				game.shoot(Direction.DOWN);
				GameView.this.removeKeyListener(shootListener);
				GameView.this.addKeyListener(movementListener);
			} else if (keyCode == KeyEvent.VK_LEFT) {
				shootAnime();
				game.shoot(Direction.LEFT);
				GameView.this.removeKeyListener(shootListener);
				GameView.this.addKeyListener(movementListener);
			} else if (keyCode == KeyEvent.VK_RIGHT) {
				shootAnime();
				game.shoot(Direction.RIGHT);
				GameView.this.removeKeyListener(shootListener);
				GameView.this.addKeyListener(movementListener);
			}
			else if (keyCode == KeyEvent.VK_S) {
				GameView.this.removeKeyListener(shootListener);
				GameView.this.addKeyListener(movementListener);
				JOptionPane.showMessageDialog(m, "You are in move mode.");
			}
		}
		@Override
		public void keyReleased(KeyEvent e) {}
		@Override
		public void keyTyped(KeyEvent e) {}
	}
	
	/*
	 * Determine the shooting animation that should be played.
	 */
	private void shootAnime() {
		if (gt.equals(GameType.CLASSIC))
			m.changeView(Views.ARROWSHOOTING, gt, game);
		else if (gt.equals(GameType.SPACE))
			m.changeView(Views.LASERBEAM, gt, game);
		else if (gt.equals(GameType.WESTERN))
			m.changeView(Views.BULLETSHOOTING, gt, game);
	}
	/**
	 * Returns a string name for this class.
	 * 
	 * @return "Game", a string
	 */
	public String toString(){
		return "Game";
	}

	/**
	 * A listener for adding key's movement listeners so that there isn't a
	 * backlog of movement keys pressed.
	 * 
	 * @param b
	 *            boolean determining whether or not to add the listener
	 */
	public void startKeyListener(boolean b) {
		if (b == false){
			removeKeyListener(movementListener);
		}
		else{
			addKeyListener(movementListener);
		}
	}
}