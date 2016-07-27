package View;

import gameModel.HuntTheWumpus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;

/**
 * An extention of the MasterViewPanel class on which the beginning cutscenes
 * are played.
 * 
 * @author Salika Dunatunga, Sarah Lutjens, Jane Wang, Denise Werchan
 */
public class CutsceneView extends MasterViewPanel {
	private char[] charToDraw = {};
	private ImageIcon i;
	private GameType gt;
	private HuntTheWumpus htw;

	/**
	 * Sets up the CutsceneView using the desired parameters so that the
	 * beginning cutscene can be played.
	 * 
	 * @param m
	 *            the MasterView parent of this component
	 * @param gt
	 *            the GameType chosed by the player
	 * @param htw
	 *            the new HuntTheWumpus game as specified by the selections in
	 *            the NewGameSelection view
	 */
	public CutsceneView(MasterView m, GameType gt, HuntTheWumpus htw) {
		super(m);
		this.gt = gt;
		this.htw = htw;
		setUpLayout();
		this.setFocusable(true);
		this.setVisible(true);
		this.setUpAnimation(gt);
	}

	private void setUpLayout() {
		this.setLayout(new BorderLayout());
		
		// if player clicks with the mouse, start the game
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				beginGame();
			}
		});
		
		// if player releases or presses a key, start the game
		this.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				beginGame();
			}
			
			public void keyReleased(KeyEvent e) {
				beginGame();
			}
			
			public void keyTyped(KeyEvent e) {}
		});
	}

	private void setUpAnimation(GameType gt) {
		switch (gt) {
		case CLASSIC:
			Cutscene.CLASSIC_BEG.playCutscene(Cutscene.CLASSIC_BEG, this);
			break;
		case WESTERN:
			Cutscene.WESTERN_BEG.playCutscene(Cutscene.WESTERN_BEG, this);
			break;
		case SPACE:
			Cutscene.SPACE_BEG.playCutscene(Cutscene.SPACE_BEG, this);
			break;
		default:
			break;
		}
	}

	private void beginGame() {
		m.changeView(Views.GAME, gt, htw);
	}

	/**
	 * The paintComponent method that overrides the superclass's one.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(i.getImage(), 0, 0, m.getWidth(), m.getHeight(), null);
		int i=(m.getWidth()-10)/this.getFontMetrics(this.getFont()).charWidth(charToDraw[0]);
		int j=0;
		do {
			if (charToDraw.length - j * i >= i) {
				g.drawChars(charToDraw, j * i, i, 0, 35 + 30 * j);
			}
			else {
				g.drawChars(charToDraw, j * i, charToDraw.length - j * i, 0, 35 + 30 * j);
			}
			j++;
		} while (j * i<charToDraw.length);
		validate();
	}
	
	/**
	 * Draws the desired string message and image onto the panel, pertaining to
	 * the theme selected by the player.
	 * 
	 * @param story
	 *            the string message as determined by the Cutscene enum
	 * @param filePath
	 *            the filepath of the image to draw onto the panel
	 */
	public void draw(String story, String filePath) {
		i = new ImageIcon(filePath);
		Font font = new Font("Courier", Font.BOLD, 30);
		this.setFont(font);
		this.setForeground(Color.LIGHT_GRAY);
		charToDraw = story.toCharArray();
		
		this.update(this.getGraphics());
	}

	/**
	 * Returns a string name for this class.
	 * 
	 * @return "CutsceneView", a string
	 */
	public String toString() {
		return "CutsceneView";
	}
}