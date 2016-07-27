package View;

import gameModel.HuntTheWumpus;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import newAnimation.PlayAnimation;

/**
 * This the MasterView class that extends JFrame. It is the main JFrame for the
 * GUI. It uses BorderLayout with one main JPanel and the menu bar for the game.
 * The JPanel that takes up the majority of the JFrame uses CardLayout to switch
 * between various JPanels that make up the bulk of the game. It also implements 
 * a changeView method to allow for switching between JPanel "cards" in the layout.
 * 
 * @author Salika Dunatunga, Sarah Lutjens, Jang Wang, Denise Werchan
 * 
 */

public class MasterView extends JFrame {

	protected static JTextArea log;
	private MasterViewPanel currentPanel;
	private Stack<MasterViewPanel> panels;
	private JPanel body;
	protected Boolean multiplayer;
	protected Boolean pvp;

	/**
	 * Main method to allow GUI to run
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new MasterView();
	}

	/**
	 * Constructor for MasterView. Sets up JFrame layout including window size.
	 */
	public MasterView() {
		this.setLayout(new BorderLayout());
		
		log = new JTextArea();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		int w = 1000;
		int h = 860;

		if (screenSize.width < 1000) {
			int minus = screenSize.width - w;
			w = w + minus;
			h = h + minus;
		} else if (screenSize.height < 860) {
			int minus = screenSize.height - h;
			w = w + minus;
			h = h + minus;
		}
		this.setLocation(screenSize.width / 2 - w / 2, screenSize.height / 2 - h / 2);
		this.setSize(w, h);
		this.setUpMenuBar();
		this.setDefaultPanel();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.requestFocus();
		multiplayer = false;
		pvp = false;
	}


	private void setUpMenuBar() {
		JMenuBar jmb = new JMenuBar();
		
		JMenu file = new JMenu("File");
		JMenuItem returnToMenu = new JMenuItem("Return to Menu");
		file.add(returnToMenu);
		returnToMenu.addActionListener(new ReturnListener());
		JMenuItem exit = new JMenuItem("Exit");
		file.add(exit);
		exit.addActionListener(new ExitListener());
		
		JMenu help = new JMenu("Help");
		JMenuItem objective = new JMenuItem("Objective");
		JMenuItem instructions = new JMenuItem("Instructions");
		objective.addActionListener(new ObjectiveListener());
		instructions.addActionListener(new InstructionListener());
		help.add(objective);
		help.add(instructions);
		
		JMenuItem about = new JMenuItem("About");
		about.addActionListener(new AboutListener());
		jmb.add(file);
		jmb.add(help);
		jmb.add(about);
		this.add(jmb, BorderLayout.NORTH);
	}

	private void setDefaultPanel() {
		panels = new Stack<MasterViewPanel>();
		currentPanel = new TitleView(this);

		body = new JPanel();
		body.setLayout(new CardLayout());
		body.add(currentPanel, "TITLE");

		panels.push(currentPanel);
		this.add(body, BorderLayout.CENTER);
	}

	private class ExitListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			if (currentPanel instanceof GameView)
				((GameView) currentPanel).getGame().saveGame();
			System.exit(0);
		}
	}
	
	private class ReturnListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			if (currentPanel instanceof GameView)
				((GameView) currentPanel).getGame().saveGame();
			MasterView.this.changeView(Views.TITLE, null, null);
		}
	}

	private class AboutListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			JOptionPane.showMessageDialog(null, "Hunt the Wumpus is a classic game first released in 1972 by Gregory Yob." + 
												"\nWumpus is based on a hide-and-seek format wherein the player discovers" +
												"\nthe location of a monster, the Wumpus. This redesigned version of Hunt" +
												"\nthe Wumpus contains some new game-play features in addition to the old." +
												"\n\n Brought to you by Team Implosion, 2011:" +
												"\nSalika Dunatunga, Sarah Lutjens, Jane Wang, and Denise Werchan");
		}
	}
	
	private class InstructionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			String instructionText = new String();
			instructionText += "-- Movement: Use the arrow Keys" +
					"\n-- Shooting: Press 'S' to change to shoot mode and then use arrow\n" +
					"keys to pick a direction to shoot" +
					"\n-- Using ladders: If you enter a room with a ladder and want to use\n" +
					"\nit, press the 'L' key."+
					"\n-- Click Minimap to zoom in. Click enlarged map to return to game." +
					"\n\n                                        Good Luck!!!";
			
			JOptionPane.showMessageDialog(null, instructionText);
		}
	}
	
	private class ObjectiveListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			String objectiveText = new String();
			objectiveText += 
					"The Object of this game is to find and shoot the Wumpus. If you are\n" +
					"playing in multi-player you must do this before the other players\n" +
					"to win. In PvP mode, you and other players can shoot each other to\n" +
					"reduce the victim's energy. To increase your energy, pick up and\n" +
					"eat food before your energy bar reaches zero. Food items as well as\n" +
					"silencers and teleport blockers can be found throughout the game.";
			
			JOptionPane.showMessageDialog(null, objectiveText);
		}

	}

	/**
	 * changeView method to allow for switching to different JPanels
	 *  
	 * @param v
	 * 			instance of the enum Views. This is the JPanel to switch to 
	 * @param g
	 * 			instance of GameType or the theme selected by the user
	 * @param o
	 * 			This is either null if not needed or an instance of HuntTheWumpus
	 */
	public void changeView(Views v, GameType g, Object o) {
		switch (v) {
		case PREVIOUS:
			CardLayout cl = (CardLayout) body.getLayout();
			JPanel temp = currentPanel;
			panels.pop();
			currentPanel = panels.peek();
			cl.show(body, currentPanel.toString());
			cl.removeLayoutComponent(temp);
			for (Component c : body.getComponents()) {
				if (c == currentPanel) {
					c.requestFocusInWindow();
				}
			}
			break;
		case GAME:
			currentPanel = new GameView(this, g, (HuntTheWumpus) o);
			panels.push(currentPanel);
			body.add(currentPanel, v.name());
			CardLayout cl1 = (CardLayout) body.getLayout();
			cl1.show(body, v.name());
			for (Component c : body.getComponents()) {
				if (c == currentPanel) {
					c.requestFocusInWindow();
				}
			}
			break;
		case MINIMAP:
			currentPanel = new MiniMap(this, (HuntTheWumpus) o, g, true);
			panels.push(currentPanel);
			body.add(currentPanel, v.name());
			CardLayout cl2 = (CardLayout) body.getLayout();
			cl2.show(body, v.name());
			for (Component c : body.getComponents()) {
				if (c == currentPanel) {
					c.requestFocusInWindow();
				}
			}
			break;
		case TITLE:
			currentPanel = new TitleView(this);
			panels.push(currentPanel);
			body.add(currentPanel, v.name());
			CardLayout cl3 = (CardLayout) body.getLayout();
			cl3.show(body, v.name());
			for (Component c : body.getComponents()) {
				if (c == currentPanel) {
					c.requestFocusInWindow();
				}
			}
			break;
		case FALLINGPLAYER:
			currentPanel = new PlayAnimation(this, "Falling Player", g, (HuntTheWumpus)o);
			panels.push(currentPanel);
			body.add(currentPanel, v.name());
			CardLayout cl8 = (CardLayout) body.getLayout();
			cl8.show(body, v.name());
			for (Component c : body.getComponents()) {
				if (c == currentPanel) {
					c.requestFocusInWindow();
				}
			}
			break;
		case HUNGRYWUMPUS:
			currentPanel = new PlayAnimation(this, "Hungry Wumpus", g, (HuntTheWumpus)o );
			panels.push(currentPanel);
			body.add(currentPanel, v.name());
			CardLayout cl9 = (CardLayout) body.getLayout();
			cl9.show(body, v.name());
			for (Component c : body.getComponents()) {
				if (c == currentPanel) {
					c.requestFocusInWindow();
				}
			}
			break;
		case DEADWUMPUS_CLASSICAL:
			currentPanel = new PlayAnimation(this, "Dead Wumpus Classical", g, (HuntTheWumpus)o);
			panels.push(currentPanel);
			body.add(currentPanel, v.name());
			CardLayout cl10 = (CardLayout) body.getLayout();
			cl10.show(body, v.name());
			for (Component c : body.getComponents()) {
				if (c == currentPanel) {
					c.requestFocusInWindow();
				}
			}
			break;
		
		case DEADWUMPUS_SPACE:
			currentPanel = new PlayAnimation(this, "Dead Wumpus Space", g, (HuntTheWumpus)o);
			panels.push(currentPanel);
			body.add(currentPanel, v.name());
			CardLayout cl11 = (CardLayout) body.getLayout();
			cl11.show(body, v.name());
			for (Component c : body.getComponents()) {
				if (c == currentPanel) {
					c.requestFocusInWindow();
				}
			}
			break;
			
		case DEADWUMPUS_WESTERN:
			currentPanel = new PlayAnimation(this, "Dead Wumpus Western", g, (HuntTheWumpus)o);
			panels.push(currentPanel);
			body.add(currentPanel, v.name());
			CardLayout cl12 = (CardLayout) body.getLayout();
			cl12.show(body, v.name());
			for (Component c : body.getComponents()) {
				if (c == currentPanel) {
					c.requestFocusInWindow();
				}
			}
			break;
			
		case ARROWSHOOTING:
			currentPanel = new PlayAnimation(this, "Arrow Shooting", g, (HuntTheWumpus)o);
			panels.push(currentPanel);
			body.add(currentPanel, v.name());
			CardLayout cl13 = (CardLayout) body.getLayout();
			cl13.show(body, v.name());
			for (Component c : body.getComponents()) {
				if (c == currentPanel) {
					c.requestFocusInWindow();
				}
			}
			break;
			
		case BATTAKINGPLAYER:
			currentPanel = new PlayAnimation(this, "Bat Taking Player", g, (HuntTheWumpus)o);
			panels.push(currentPanel);
			body.add(currentPanel, v.name());
			CardLayout cl14 = (CardLayout) body.getLayout();
			cl14.show(body, v.name());
			for (Component c : body.getComponents()) {
				if (c == currentPanel) {
					c.requestFocusInWindow();
				}
			}
			break;
			
		case BULLETSHOOTING:
			currentPanel = new PlayAnimation(this, "Bullet Shooting", g, (HuntTheWumpus)o);
			panels.push(currentPanel);
			body.add(currentPanel, v.name());
			CardLayout cl15 = (CardLayout) body.getLayout();
			cl15.show(body, v.name());
			for (Component c : body.getComponents()) {
				if (c == currentPanel) {
					c.requestFocusInWindow();
				}
			}
			break;
			
		case PLAYERGOTKILLED:
			currentPanel = new PlayAnimation(this, "Player Got Killed", g, (HuntTheWumpus)o);
			panels.push(currentPanel);
			body.add(currentPanel, v.name());
			CardLayout cl16 = (CardLayout) body.getLayout();
			cl16.show(body, v.name());
			for (Component c : body.getComponents()) {
				if (c == currentPanel) {
					c.requestFocusInWindow();
				}
			}
			break;
		
		case STARVEDPLAYER:
			currentPanel = new PlayAnimation(this, "Starved Player", g, (HuntTheWumpus)o);
			panels.push(currentPanel);
			body.add(currentPanel, v.name());
			CardLayout cl17 = (CardLayout) body.getLayout();
			cl17.show(body, v.name());
			for (Component c : body.getComponents()) {
				if (c == currentPanel) {
					c.requestFocusInWindow();
				}
			}
			break;
			
		case LASERBEAM:
			currentPanel = new PlayAnimation(this, "Laser Beam", g, (HuntTheWumpus)o);
			panels.push(currentPanel);
			body.add(currentPanel, v.name());
			CardLayout cl18 = (CardLayout) body.getLayout();
			cl18.show(body, v.name());
			for (Component c : body.getComponents()) {
				if (c == currentPanel) {
					c.requestFocusInWindow();
				}
			}
			break;
		case BEGINNING_CUTSCENE:
			currentPanel = new CutsceneView(this, g, (HuntTheWumpus) o);
			panels.push(currentPanel);
			body.add(currentPanel, v.name());
			CardLayout cl19 = (CardLayout) body.getLayout();
			cl19.show(body, v.name());
			for (Component c : body.getComponents()) {
				if (c == currentPanel) {
					c.requestFocusInWindow();
				}
			}
			break;
		default:
		}

	}

	/**
	 * This is the changeSelection method to switch between the four GameSelection classes
	 * @param v
	 * 			The View in which the JPanel should switch to in the CardLayout
	 * @param o
	 * 			null - this is reserved in case it is needed for future use.
	 * 		
	 */
	public void changeSelection(Views v, Object o) {
		switch (v) {
		case NEWGAME:
			currentPanel = new NewGameSelection(this);
			panels.push(currentPanel);
			body.add(currentPanel, v.name());
			CardLayout cl4 = (CardLayout) body.getLayout();
			cl4.show(body, v.name());
			for (Component c : body.getComponents()) {
				if (c == currentPanel) {
					c.requestFocusInWindow();
				}
			}
			break;
		case HOSTGAME:
			currentPanel = new HostGameSelection(this);
			panels.push(currentPanel);
			body.add(currentPanel, v.name());
			CardLayout cl5 = (CardLayout) body.getLayout();
			cl5.show(body, v.name());
			for (Component c : body.getComponents()) {
				if (c == currentPanel) {
					c.requestFocusInWindow();
				}
			}
			break;
		case JOINGAME:
			currentPanel = new JoinGameSelection(this);
			panels.push(currentPanel);
			body.add(currentPanel, v.name());
			CardLayout cl6 = (CardLayout) body.getLayout();
			cl6.show(body, v.name());
			for (Component c : body.getComponents()) {
				if (c == currentPanel) {
					c.requestFocusInWindow();
				}
			}
			break;
		case CONTINUEGAME:
			currentPanel = new ContinueGameSelection(this);
			panels.push(currentPanel);
			body.add(currentPanel, v.name());
			CardLayout cl7 = (CardLayout) body.getLayout();
			cl7.show(body, v.name());
			for (Component c : body.getComponents()) {
				if (c == currentPanel) {
					c.requestFocusInWindow();
				}
			}
			break;
		default:
		}	
	}
}
