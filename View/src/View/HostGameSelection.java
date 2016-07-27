package View;

import gameModel.HuntTheWumpus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * This is the HostGameSelection class. It gives the interface for a user
 * who chooses to host a game. It allows for theme, level and play type
 * selection. It also asks for the port in which to use for the connection.
 * 
 * @author Salika Dunatunga, Sarah Lutjens, Jang Wang, Denise Werchan
 * 
 */
public class HostGameSelection extends MasterViewPanel {
	private JTextField nameField, portField;
	private JRadioButton beginner, intermediate, advanced;
	private JRadioButton classic, western, space;
	private JRadioButton coop, pvp;
	private JLabel nameLabel, themeLabel, levelLabel, portLabel, multGameType;
	private JPanel eastPanel, westPanel, namePanel, portPanel;
	private JButton startGame;
	private int levelSelected, numShots;
	private GameType selectedGameType;
	private boolean multGameTypeChoice;

	/**
	 * The constructor for HostGameSelection. This class sets up the layout for
	 * the user interface in which the user can select game parameters.
	 * 
	 * @param m
	 * 			Instance of the MasterView JFrame this class appears
	 */
	public HostGameSelection(MasterView m) {
		super(m);
		setUpLayout();
		setUpTextField();
		addLabels();
		setUpPortField();
		addStartButton();
		addReturnButton();
		this.add(eastPanel, BorderLayout.EAST);
		this.add(westPanel, BorderLayout.CENTER);
		this.setVisible(true);
	}

	private void setUpLayout() {
		this.setLayout(new BorderLayout());
		eastPanel = new JPanel();
		eastPanel.setPreferredSize(new Dimension(150, 300));
		// take out the background setting after we have the pictures
		eastPanel.setBackground(Color.lightGray);
		eastPanel.setLayout(new GridLayout(15, 1));
		westPanel = new WestPanel();
		westPanel.setPreferredSize(new Dimension(200, 300));
		selectedGameType = GameType.CLASSIC;
		levelSelected = 0;
	}

	private void setUpTextField() {
		nameLabel = new JLabel("Enter Your Name:");
		nameField = new JTextField("Player");
		namePanel = new JPanel();
		namePanel.setLayout(new GridLayout(2, 1));
		namePanel.add(nameLabel);
		namePanel.add(nameField);
		eastPanel.add(namePanel);
	}

	private void addLabels() {
		themeLabel = new JLabel("Theme");
		eastPanel.add(themeLabel);
		addThemeButtons();

		levelLabel = new JLabel("Level");
		eastPanel.add(levelLabel);
		addLevelButtons();

		multGameType = new JLabel("Multiplayer Game Type");
		eastPanel.add(multGameType);
		addMultGameTypeButtons();
	}

	private void addThemeButtons() {
		classic = new JRadioButton("Classic");
		western = new JRadioButton("Western");
		space = new JRadioButton("Space");

		// use ButtonGroup to make them into a group so that only one button can
		// be selected
		ButtonGroup group = new ButtonGroup();
		group.add(classic);
		group.add(western);
		group.add(space);
		classic.setSelected(true);

		GameTypeListener listener = new GameTypeListener();
		classic.addActionListener(listener);
		western.addActionListener(listener);
		space.addActionListener(listener);

		eastPanel.add(classic);
		eastPanel.add(western);
		eastPanel.add(space);

	}

	private class GameTypeListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getSource() == classic) {
				selectedGameType = GameType.CLASSIC;
				westPanel.repaint();
			} else if (arg0.getSource() == western) {
				selectedGameType = GameType.WESTERN;
				westPanel.repaint();
			} else if (arg0.getSource() == space) {
				selectedGameType = GameType.SPACE;
				westPanel.repaint();
			}

			// set a default if player didn't select anything specifically
			else {
				selectedGameType = GameType.CLASSIC;
			}
		}

	}

	private void addLevelButtons() {
		beginner = new JRadioButton("Beginner");
		intermediate = new JRadioButton("Intermediate");
		advanced = new JRadioButton("Advanced");

		// use ButtonGroup to make them into a group so that only one button can
		// be selected
		ButtonGroup group = new ButtonGroup();
		group.add(beginner);
		group.add(intermediate);
		group.add(advanced);

		LevelListener listener = new LevelListener();
		beginner.addActionListener(listener);
		intermediate.addActionListener(listener);
		advanced.addActionListener(listener);
		beginner.setSelected(true);

		eastPanel.add(beginner);
		eastPanel.add(intermediate);
		eastPanel.add(advanced);
	}

	
	private class LevelListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getSource() == beginner) {
				levelSelected = 0;
			} else if (arg0.getSource() == intermediate) {
				levelSelected = 1;
			} else if (arg0.getSource() == advanced) {
				levelSelected = 2;
			} else {
				levelSelected = 0;
			}
		}
	}

	private void addMultGameTypeButtons() {
		coop = new JRadioButton("Co-op Play");
		pvp = new JRadioButton("Player vs. Player");
		multGameTypeChoice = true;
		// use ButtonGroup to make them into a group so that only one button can
		// be selected
		ButtonGroup group = new ButtonGroup();
		group.add(coop);
		group.add(pvp);

		MultGameTypeListener listener = new MultGameTypeListener();
		pvp.addActionListener(listener);
		coop.addActionListener(listener);
		pvp.setSelected(true);

		eastPanel.add(coop);
		eastPanel.add(pvp);

	}

	private class MultGameTypeListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getSource() == coop)
				multGameTypeChoice = false;
			else
				multGameTypeChoice = true;
		}
	}
	
	private void setUpPortField() {
		portLabel = new JLabel("Enter Port:");
		portField = new JTextField("4000");
		portPanel = new JPanel(new GridLayout(2, 1));
		portPanel.add(portLabel);
		portPanel.add(portField);
		eastPanel.add(portPanel);
	}

	private void addStartButton() {
		startGame = new JButton("Start");
		StartListener listener = new StartListener();
		startGame.addActionListener(listener);
		eastPanel.add(startGame);
	}
	
	private void addReturnButton(){
		JButton returnToMenu = new JButton("Return to Menu");
		returnToMenu.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				m.changeView(Views.TITLE, null, null);
			}
		});
		eastPanel.add(returnToMenu);
	}

	private int parsePort(String str) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			String newStr = JOptionPane.showInputDialog(this,
					"Please input a valid port number.");
			return parsePort(newStr);
		}
	}
	
	private HuntTheWumpus hostHTW(String playerName, int i, int level, int port, boolean mp) {
		return new HuntTheWumpus(
				playerName, 4, levelSelected, parsePort(""), multGameTypeChoice);
	}

	private class StartListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// call the 3rd HTW constructor --> takes (String name, int shots,
			// double diff, int port)
			String playerName = nameField.getText();
			if (playerName.equals(""))
				playerName = "Player";

			String portStr = portField.getText();
			int port = 4000;
			if (!portStr.equals(null))
				port = parsePort(portStr);

			HuntTheWumpus htw = new HuntTheWumpus(
					playerName, 4, levelSelected, port, multGameTypeChoice);
			if (!htw.isBadPort())
				m.changeView(Views.GAME, selectedGameType, htw);
			else {
				htw = hostHTW(playerName, 4, levelSelected, port, multGameTypeChoice);
				m.changeView(Views.GAME, selectedGameType, htw);
			}
		}
	}

private class WestPanel extends JPanel{
		
		private String story = new String();
		private Image backgroundImage, storyImage;
		private JPanel storyPanel, instructionPanel;
		private JLabel storyHeader, instructionTitle, movementLabel, shootModeLabel;
		private JLabel toShootLabel, climbLadderLabel, goDownLevel, warning;
		
		public WestPanel(){
			this.setSize(new Dimension((int)(m.getSize().getWidth()*.9), (int)m.getSize().getHeight()));	
			this.setLayout(null);
			setupStoryPanel();
			setupInstructionPanel();
		}
		
		
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			
			if(selectedGameType == GameType.SPACE){
				setSpace();
			}else if (selectedGameType == GameType.WESTERN){
				setWestern();
			}else{
				setClassic();	
			}
			g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
		}

		private void setupStoryPanel() {
			storyImage = new ImageIcon("Images/Classic/parchment.png").getImage();
			storyPanel = new JPanel(){
				@Override
				 public void paintComponent (Graphics g){
					super.paintComponent(g);
					g.drawImage(storyImage, 0, -20, (int)(m.getSize().getWidth()*.8), (int)(m.getSize().getHeight()* .73), null);  
		         }
			};
			storyPanel.setLayout(new BorderLayout());
			storyPanel.setSize((int)(m.getSize().getWidth()*.8), (int)(m.getSize().getHeight()* .7));
			storyPanel.setLocation(0,0);
			storyPanel.setVisible(true);
			storyPanel.setOpaque(false);
			
			storyHeader = new JLabel();
			storyHeader.setHorizontalAlignment(JLabel.CENTER);
			storyPanel.add(storyHeader, BorderLayout.NORTH);
			
			this.add(storyPanel);
			
		}
		
		private void setupInstructionPanel() {
			instructionPanel = new JPanel();
			instructionPanel.setLayout(new GridLayout(7,1));
			instructionPanel.setVisible(true);
			instructionPanel.setOpaque(false);
			
			instructionTitle = new JLabel("Instructions");
			instructionTitle.setHorizontalAlignment(JLabel.CENTER);
			instructionPanel.add(instructionTitle);
			
			movementLabel = new JLabel("Movement");
			movementLabel.setIcon(new ImageIcon("Images/arrowkeys.png"));
			movementLabel.setHorizontalAlignment(JLabel.CENTER);
			instructionPanel.add(movementLabel);
			
			shootModeLabel = new JLabel("Turn Shoot Mode On/Off");
			shootModeLabel.setIcon(new ImageIcon("Images/sKey.png"));
			shootModeLabel.setHorizontalAlignment(JLabel.CENTER);
			instructionPanel.add(shootModeLabel);
			
			toShootLabel = new JLabel("Then, to shoot use arrow keys");
			toShootLabel.setIcon(new ImageIcon("Images/arrowkeys.png"));
			toShootLabel.setHorizontalAlignment(JLabel.CENTER);
			instructionPanel.add(toShootLabel);
			
			climbLadderLabel = new JLabel("Climb a ladder");
			climbLadderLabel.setIcon(new ImageIcon("Images/lKey.png"));
			climbLadderLabel.setHorizontalAlignment(JLabel.CENTER);
			instructionPanel.add(climbLadderLabel);
			
			goDownLevel = new JLabel("Go down a level");
			goDownLevel.setHorizontalAlignment(JLabel.CENTER);
			instructionPanel.add(goDownLevel);
			
			warning = new JLabel("Do not go down a pit from level 1. You will die!");
			warning.setHorizontalAlignment(JLabel.CENTER);
			instructionPanel.add(warning);
			
			storyPanel.add(instructionPanel, BorderLayout.CENTER);
		}

		private void setSpace() {
			backgroundImage = new ImageIcon("Images/Space/spaceBackground.png").getImage();
			story = "Reclaim your equipment fromt the Wumpus!";
			storyHeader.setText(story);
			storyImage = new ImageIcon("Images/Space/monitor.png").getImage();

			storyHeader.setForeground(Color.GREEN);
			storyHeader.setLocation(0, 40);
			storyHeader.setFont(new Font("Miriam Fixed", Font.BOLD, 30));
			
			instructionTitle.setForeground(Color.GREEN);
			instructionTitle.setLocation(0, 30);
			instructionTitle.setFont(new Font("Miriam Fixed", Font.BOLD, 30));
			
			movementLabel.setForeground(Color.GREEN);
			movementLabel.setFont(new Font("Miriam Fixed", Font.BOLD, 30));
			movementLabel.setIconTextGap(50);
			
			shootModeLabel.setForeground(Color.GREEN);
			shootModeLabel.setFont(new Font("Miriam Fixed", Font.BOLD, 30));
			shootModeLabel.setIconTextGap(50);
			
			toShootLabel.setForeground(Color.GREEN);
			toShootLabel.setFont(new Font("Miriam Fixed", Font.BOLD, 30));
			toShootLabel.setIconTextGap(50);
			
			climbLadderLabel.setForeground(Color.GREEN);
			climbLadderLabel.setFont(new Font("Miriam Fixed", Font.BOLD, 30));
			climbLadderLabel.setIconTextGap(50);
			
			goDownLevel.setForeground(Color.GREEN);
			goDownLevel.setFont(new Font("Miriam Fixed", Font.BOLD, 30));
			goDownLevel.setIcon(new ImageIcon("Images/Classic/pit.png"));
			goDownLevel.setIconTextGap(50);
			
			warning.setForeground(Color.RED);
			warning.setFont(new Font("Arial", Font.BOLD, 20));			
		}

		private void setWestern() {
			backgroundImage = new ImageIcon("Images/Western/westernBackground.png").getImage();
			story = "Hunt the wumpus and claim the bounty!";
			storyHeader.setText(story);
			storyImage = new ImageIcon("Images/Western/wantedPoster.png").getImage();

			storyHeader.setForeground(new Color(142, 124, 100));
			storyHeader.setLocation(0, 60);
			storyHeader.setFont(new Font("Arial", Font.BOLD, 20));
			
			instructionTitle.setForeground(new Color(142, 124, 100));
			instructionTitle.setLocation(0, 30);
			instructionTitle.setFont(new Font("Arial", Font.BOLD, 30));
			
			movementLabel.setForeground(new Color(142, 124, 100));
			movementLabel.setFont(new Font("Arial", Font.BOLD, 30));
			movementLabel.setIconTextGap(50);
			
			shootModeLabel.setForeground(new Color(142, 124, 100));
			shootModeLabel.setFont(new Font("Arial", Font.BOLD, 30));
			shootModeLabel.setIconTextGap(50);
			
			toShootLabel.setForeground(new Color(142, 124, 100));
			toShootLabel.setFont(new Font("Arial", Font.BOLD, 30));
			toShootLabel.setIconTextGap(50);
			
			climbLadderLabel.setForeground(new Color(142, 124, 100));
			climbLadderLabel.setFont(new Font("Arial", Font.BOLD, 30));
			climbLadderLabel.setIconTextGap(50);
			
			goDownLevel.setForeground(new Color(142, 124, 100));
			goDownLevel.setFont(new Font("Arial", Font.BOLD, 30));
			goDownLevel.setIcon(new ImageIcon("Images/Classic/pit.png"));
			goDownLevel.setIconTextGap(50);
			
			warning.setForeground(Color.RED);
			warning.setFont(new Font("Arial", Font.BOLD, 20));			
		}

		private void setClassic() {
			backgroundImage = new ImageIcon("Images/Classic/classicBackground.png").getImage();
			story =	"Hunt the Wumpus and save your sheep!";
			storyHeader.setText(story);
			storyImage = new ImageIcon("Images/Classic/parchment.png").getImage();
			
			storyHeader.setForeground(Color.DARK_GRAY);
			storyHeader.setLocation(0, 40);
			storyHeader.setFont(new Font("Arial", Font.BOLD, 20));
			
			instructionTitle.setForeground(Color.DARK_GRAY);
			instructionTitle.setLocation(0, 30);
			instructionTitle.setFont(new Font("Arial", Font.BOLD, 30));
			
			movementLabel.setForeground(Color.DARK_GRAY);
			movementLabel.setFont(new Font("Arial", Font.BOLD, 30));
			movementLabel.setIconTextGap(50);
			
			shootModeLabel.setForeground(Color.DARK_GRAY);
			shootModeLabel.setFont(new Font("Arial", Font.BOLD, 30));
			shootModeLabel.setIconTextGap(50);
			
			toShootLabel.setForeground(Color.DARK_GRAY);
			toShootLabel.setFont(new Font("Arial", Font.BOLD, 30));
			toShootLabel.setIconTextGap(50);
			
			climbLadderLabel.setForeground(Color.DARK_GRAY);
			climbLadderLabel.setFont(new Font("Arial", Font.BOLD, 30));
			climbLadderLabel.setIconTextGap(50);
			
			goDownLevel.setForeground(Color.DARK_GRAY);
			goDownLevel.setFont(new Font("Arial", Font.BOLD, 30));
			goDownLevel.setIcon(new ImageIcon("Images/Classic/pit.png"));
			goDownLevel.setIconTextGap(50);
			
			warning.setForeground(Color.RED);
			warning.setFont(new Font("Arial", Font.BOLD, 20));
			warning.setLocation(0, (int)(m.getSize().getHeight()* .7)-130);		
		}
	}

}
