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
 * This is the NewGameSelection that extends MasterViewPanel. It sets up
 * the interface to allow users to choose various options for a single
 * player game.
 * 
 * @author Salika Dunatunga, Sarah Lutjens, Jang Wang, Denise Werchan
 * 
 */
public class NewGameSelection extends MasterViewPanel {
	private JTextField nameField;
	private JRadioButton beginner, intermediate, advanced;
	private JRadioButton classic, western, space;
	private JLabel nameLabel, themeLabel, levelLabel;
	private JPanel eastPanel, westPanel, namePanel;
	private JButton startGame;
	private int levelSelected, numShots;
	private GameType selectedGameType;
	private String playerName;

	/**
	 * This is the constructor that sets up the layout for the class.
	 * 
	 * @param m
	 * 			Instance of the MasterView where this MasterViewPanel appears
	 */
	public NewGameSelection(MasterView m) {
		super(m);
		setUpLayout();
		setUpTextField();
		addLabels();
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
		eastPanel.setLayout(new GridLayout(12, 1));
		westPanel = new WestPanel();
		selectedGameType = GameType.CLASSIC;
		levelSelected = 0;
	}

	private void setUpTextField() {
		nameLabel = new JLabel("Enter Your Name:");
		nameField = new JTextField("Player");
		nameField.setEditable(true);
		playerName = "";
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
				numShots = 3;
			} else if (arg0.getSource() == intermediate) {
				levelSelected = 1;
				numShots = 2;
			} else if (arg0.getSource() == advanced) {
				levelSelected = 2;
				numShots = 1;
			} else {
				levelSelected = 0;
				numShots = 3;
			}
		}
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

	private class StartListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// call the 1st HTW constructor --> takes (String name, int shots,
			// double chance)
			String name = nameField.getText();
			if (!name.equals(""))
				playerName = playerName + name;
			else
				playerName = "Player";

			m.changeView(Views.BEGINNING_CUTSCENE, selectedGameType, new HuntTheWumpus(
					playerName, 1, levelSelected));

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
					storyPanel.setSize((int)(m.getSize().getWidth()*.8), (int)(m.getSize().getHeight()* .7));
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
			storyHeader.setFont(new Font("Miriam Fixed", Font.BOLD, 25));
			
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
			warning.setLocation(0,465);

			
		}

		private void setWestern() {
			backgroundImage = new ImageIcon("Images/Western/westernBackground.png").getImage();
			story = "Hunt the wumpus and claim the bounty!";
			storyHeader.setText(story);
			storyImage = new ImageIcon("Images/Western/wantedPoster.png").getImage();

			storyHeader.setForeground(new Color(108,96,70));
			storyHeader.setLocation(0, 100);
			storyHeader.setFont(new Font("Arial", Font.BOLD, 30));
			
			instructionTitle.setForeground(new Color(108,96,70));
			instructionTitle.setLocation(0, 100);
			instructionTitle.setFont(new Font("Arial", Font.BOLD, 30));
			
			movementLabel.setForeground(new Color(108,96,70));
			movementLabel.setFont(new Font("Arial", Font.BOLD, 30));
			movementLabel.setLocation(0, 170);
			movementLabel.setIconTextGap(50);
			
			shootModeLabel.setForeground(new Color(108,96,70));
			shootModeLabel.setFont(new Font("Arial", Font.BOLD, 30));
			shootModeLabel.setLocation(0, 240);
			shootModeLabel.setIconTextGap(50);
			
			toShootLabel.setForeground(new Color(108,96,70));
			toShootLabel.setFont(new Font("Arial", Font.BOLD, 30));
			toShootLabel.setLocation(0, 310);
			toShootLabel.setIconTextGap(50);
			
			climbLadderLabel.setForeground(new Color(108,96,70));
			climbLadderLabel.setFont(new Font("Arial", Font.BOLD, 30));
			climbLadderLabel.setLocation(0, 375);
			climbLadderLabel.setIconTextGap(50);
			
			goDownLevel.setForeground(new Color(108,96,70));
			goDownLevel.setFont(new Font("Arial", Font.BOLD, 30));
			goDownLevel.setLocation(0, 440);
			goDownLevel.setIcon(new ImageIcon("Images/Classic/pit.png"));
			goDownLevel.setIconTextGap(50);
			
			warning.setForeground(Color.RED);
			warning.setFont(new Font("Arial", Font.BOLD, 20));
//			warning.setLocation((int)(m.getSize().getWidth()*.005), (int)(m.getSize().getHeight()* .9)-170);
			
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