package newAnimation;

import gameModel.HuntTheWumpus;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import View.GameType;
import View.MasterView;
import View.MasterViewPanel;
import View.Views;

/**
 * PlayAnimation plays the *.gif files that are the animations for various
 * in-game and end-game cutscenes.
 * 
 * @author Salika Dunatunga, Sarah Lutjens, Jane Wang, Denise Werchan
 * 
 */
public class PlayAnimation extends MasterViewPanel{
	
	private JLabel animeLabel;
	private String event;
	private String filePath;
	private ImageIcon image;
	private GameType gameType;
	private HuntTheWumpus game;

	/**
	 * Constructor for PlayAnimation which sets up the new panel and sets the
	 * appropriate cutscene and then plays it
	 * 
	 * @param m
	 *            the parent MasterView for this class
	 * @param eventType
	 *            the determined event-type based on why the cutscene is playing
	 * @param gt
	 *            the user-selected game-type
	 * @param htw
	 *            the HuntTheWumpus game which calls the animation
	 */
	public PlayAnimation(MasterView m, String eventType, GameType gt, HuntTheWumpus htw) {
		super(m);
		this.gameType = gt;
		this.game = htw;
		setupLayout();
		event = eventType;
		
		// if the user clicks the mouse, change the view
		animeLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				changeView();
			}
		});
		
		// if the user releases a key, change the view
		animeLabel.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent arg0) {}

			@Override
			public void keyReleased(KeyEvent arg0) {
				changeView();
			}
			
			public void keyTyped(KeyEvent arg0) {}
			
		});
		
		playAnime();
	}
	
	/*
	 * Set up the layout for this panel
	 */
	private void setupLayout() {
		this.setLayout(new BorderLayout());
		animeLabel = new JLabel();
		animeLabel.setSize(m.getWidth(), m.getHeight());
		animeLabel.setBounds(0, 0, m.getWidth(), m.getHeight());
		this.add(animeLabel, BorderLayout.CENTER);
	}
	
	/*
	 * Determine the animation to play
	 */
	private void playAnime(){
		try{
			if (event.equals("Falling Player")){
				filePath = "Animations/FallingPlayer.gif";
			}
			else if (event.equals("Hungry Wumpus")){
				filePath = "Animations/HungryWumpusAnime.gif";
			}
			else if (event.equals("Dead Wumpus Classical")){
				filePath = "Animations/DeadWumpus_Classical.gif";
			}
			else if (event.equals("Dead Wumpus Space")){
				filePath = "Animations/DeadWumpus_Space.gif";
			}
			else if (event.equals("Dead Wumpus Western")){
				filePath = "Animations/DeadWumpus_Western.gif";
			}
			else if (event.equals("Arrow Shooting")){
				filePath = "Animations/ArrowAnimation.gif";
			}
			else if (event.equals("Bat Taking Player")){
				filePath = "Animations/BatTakingPlayerAnimation.gif";
			}
			else if (event.equals("Bullet Shooting")){
				filePath = "Animations/BulletAnimation.gif";
			}
			else if (event.equals("Dead Wumpus")){
				filePath = "Animations/DeadWumpusAnimation.gif";
			}
			else if (event.equals("Player Got Killed")){
				filePath = "Animations/PlayerGotKilled.gif";
			}
			else if (event.equals("Starved Player")){
				filePath = "Animations/StarvingPlayerAnimation.gif";
			}
			else if (event.equals("Laser Beam")){
				filePath = "Animations/LaserBeam.gif";
			}
			this.image = new ImageIcon(filePath);
			
			this.animeLabel.setIcon(image);
			m.validate();
		}
		catch(Exception exception){
			exception.printStackTrace();
		}
	}
	
	/*
	 * Exit out to game view.
	 */
	private void changeView(){
			m.changeView(Views.GAME, gameType, game);
	}
	
	/**
	 * Returns a string name for this class.
	 * 
	 * @return "PlayAnimation", a string
	 */
	public String toString() {
		return "PlayAnimation";
	}
}
