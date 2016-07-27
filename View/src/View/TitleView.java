package View;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;

/**
 * This is the first view seen by the user. Sets up selections for the rest of the game
 * 
 * @author Salika Dunatunga, Sarah Lutjens, Jang Wang, Denise Werchan
 * 
 */
public class TitleView extends MasterViewPanel {
	private JButton newGame, hostGame, joinGame, continueGame, exit;
	private JPanel buttonPanel;
	private JLabel imageLabel;

	/**
	 * Sets up the TitleView layout for this game
	 * 
	 * @param m
	 * 			Instance of Masterview that contains this class
	 */
	public TitleView(MasterView m) {
		super(m);
		setLayout();
		this.setBackground(Color.BLACK);
	}
	
	/**
	 * Overrides the JPanel paintComponetn()
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
	}
	
	private void setLayout() {
		this.setLayout(null);
		
		buttonPanel = new JPanel();
		buttonPanel.setLocation(m.getWidth()-235, m.getHeight()-410);
		buttonPanel.setSize(220, 350);
		buttonPanel.setVisible(true);
		buttonPanel.setOpaque(false);
		this.add(buttonPanel);
		this.setComponentZOrder(buttonPanel, 0);
		
		ImageIcon backgroundImage = new ImageIcon("Images/titleBackground.gif");
		imageLabel = new JLabel(backgroundImage);
		imageLabel.setLocation(0, 0);
		imageLabel.setSize(m.getWidth(), m.getHeight());
		imageLabel.setVisible(true);
		this.add(imageLabel);
		this.setComponentZOrder(imageLabel, 1);
		
		buttonPanel.setLayout(new GridLayout(5, 1));
		newGame = new JButton("New Game");
		hostGame = new JButton("Host Game");
		joinGame = new JButton("Join Game");
		continueGame = new JButton("Continue Game");
		exit = new JButton("Exit");
		
		newGame.setFont(new Font("Verdana", Font.BOLD, 20));
		hostGame.setFont(new Font("Verdana", Font.BOLD, 20));
		joinGame.setFont(new Font("Verdana", Font.BOLD, 20));
		continueGame.setFont(new Font("Verdana", Font.BOLD, 20));
		exit.setFont(new Font("Verdana", Font.BOLD, 20));
		
		ImageIcon scrollImage = new ImageIcon("Images/Classic/ScrollButtonMedium.png");
		
		newGame.setHorizontalTextPosition(SwingConstants.CENTER);
		hostGame.setHorizontalTextPosition(SwingConstants.CENTER);
		joinGame.setHorizontalTextPosition(SwingConstants.CENTER);
		continueGame.setHorizontalTextPosition(SwingConstants.CENTER);
		exit.setHorizontalTextPosition(SwingConstants.CENTER);
		
		newGame.setOpaque(false);
		hostGame.setOpaque(false);
		joinGame.setOpaque(false);
		continueGame.setOpaque(false);
		exit.setOpaque(false);
		
		newGame.setIcon(scrollImage);
		hostGame.setIcon(scrollImage);
		joinGame.setIcon(scrollImage);
		continueGame.setIcon(scrollImage);
		exit.setIcon(scrollImage);
		
		newGame.setBorder(BorderFactory.createEmptyBorder());
		hostGame.setBorder(BorderFactory.createEmptyBorder());
		joinGame.setBorder(BorderFactory.createEmptyBorder());
		continueGame.setBorder(BorderFactory.createEmptyBorder());
		exit.setBorder(BorderFactory.createEmptyBorder());
		
		newGame.setContentAreaFilled(false);
		hostGame.setContentAreaFilled(false);
		joinGame.setContentAreaFilled(false);
		continueGame.setContentAreaFilled(false);
		exit.setContentAreaFilled(false);
	
		ButtonSelector listener = new ButtonSelector();
		newGame.addActionListener(listener);
		hostGame.addActionListener(listener);
		joinGame.addActionListener(listener);
		continueGame.addActionListener(listener);
		exit.addActionListener(listener);

		continueGame.setEnabled(false);

		buttonPanel.add(newGame);
		buttonPanel.add(hostGame);
		buttonPanel.add(joinGame);
		buttonPanel.add(continueGame);
		buttonPanel.add(exit);
		
		File savedGames = new File("SavedGames/");
		File[] listOfGames = savedGames.listFiles();

		if (listOfGames != null && listOfGames.length > 0) {
			for (int i = 0; i < listOfGames.length; i++) {
				String name = listOfGames[i].getName();
				String dat = ".htw";

				if (name.length() > 7) {
					name = name.substring(name.length() - 7, name.length() - 3);
					if (name.contentEquals(dat))
						continueGame.setEnabled(true);
				}
			}
		}
	}

	private class ButtonSelector implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getSource() == newGame) {
				m.multiplayer = false;
				m.changeSelection(Views.NEWGAME, new NewGameSelection(m));
			} else if (arg0.getSource() == hostGame) {
				m.multiplayer = true;
				m.changeSelection(Views.HOSTGAME, new HostGameSelection(m));
			} else if (arg0.getSource() == joinGame) {
				m.multiplayer = true;
				m.changeSelection(Views.JOINGAME, new JoinGameSelection(m));
			} else if (arg0.getSource() == continueGame) {
				m.changeSelection(Views.CONTINUEGAME,
						new ContinueGameSelection(m));
			}else if (arg0.getSource() == exit) {
				System.exit(0);
			}
		}
	}
}
