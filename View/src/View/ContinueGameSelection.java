package View;

import gameModel.HuntTheWumpus;
import gameModel.Player;
import gameModel.WumpusMap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * This is the ContinueGameSelection class. It is the MasterViewPanel that
 * allows the user to load or delete a previously saved game.
 * 
 * @author Salika Dunatunga, Sarah Lutjens, Jang Wang, Denise Werchan
 * 
 */
public class ContinueGameSelection extends MasterViewPanel {

	private static final long serialVersionUID = -8758172189225185634L;
	private JScrollPane scrollList;
	private JList gameList;
	private ListSelectionModel gLModel;
	private String name;
	private File savedGames;
	private File[] listOfGames;
	private boolean deleted = false;
	private int timeSpace;
	private int dateSpace;
	private int nameSpace;
	private int gameSpace;

	/**
	 * Creates a new ContinueGameSelection panel in the desired JFrame.
	 * 
	 * @param MasterView
	 *            This is the instance of the MasterView (JFrame) used for this
	 *            game
	 */
	public ContinueGameSelection(MasterView m) {
		super(m);
		setUpLayout();
		setUpButtons();
		this.setFocusable(true);
		this.setVisible(true);
	}

	private void setUpLayout() {
		this.setLayout(new BorderLayout());
		JPanel continuePanel = new ContinuePanel();
		continuePanel.setBackground(Color.BLACK);
		continuePanel.setVisible(true);
		continuePanel.setLayout(null);
		continuePanel.setSize(new Dimension(m.getWidth() - (m.getWidth() / 10),
				m.getHeight() - (m.getHeight() / 10)));

		Font panelFont = new Font("Verdana", Font.PLAIN, 16);

		// get the distance from the edge of these labels
		timeSpace = m.getWidth() / 3 + 2 * m.getWidth() / 5;
		dateSpace = m.getWidth() / 2 + m.getWidth() / 10;
		nameSpace = m.getWidth() / 10;
		gameSpace = m.getWidth() / 2 - m.getWidth() / 9;

		savedGames = new File("SavedGames/");
		listOfGames = savedGames.listFiles();
		gameList = new JList();

		getListFiles();

		// use a fixed font in order to ensure that the spacing is correct all/most of the time
		Font fixedFont = new Font("Courier", Font.PLAIN, 12);

		gameList.setFont(fixedFont);
		gLModel = gameList.getSelectionModel();
		gLModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// add a selection listener to know what file was selected to load or delete
		gLModel.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				ListSelectionModel temp = (ListSelectionModel) arg0.getSource();

				if (deleted == true) {
					deleted = false;
					arg0 = null;
					name = null;
				}
				if (arg0 != null) {
					if (!arg0.getValueIsAdjusting()
							&& temp.getAnchorSelectionIndex() >= listOfGames.length)
						name = null;
					else if (!arg0.getValueIsAdjusting()) {
						name = listOfGames[temp.getAnchorSelectionIndex()]
								.getName();
					}
				}
			}
		});

		scrollList = new JScrollPane(gameList);
		continuePanel.add(scrollList, 0);

		scrollList.setSize(new Dimension(m.getWidth() - 200,
				m.getHeight() - 300));
		scrollList.setLocation(m.getWidth() / 10, m.getHeight() / 11);

		JLabel name = new JLabel("Name");
		name.setFont(panelFont);
		name.setForeground(Color.WHITE);
		name.setVisible(true);
		name.setSize(50, 18);
		continuePanel.add(name, 0);
		name.setLocation(nameSpace, m.getHeight() / 11 - 18);

		JLabel game = new JLabel("Game Type");
		game.setFont(panelFont);
		game.setForeground(Color.WHITE);
		game.setVisible(true);
		game.setSize(100, 18);
		continuePanel.add(game, 0);
		game.setLocation(gameSpace, m.getHeight() / 11 - 18);

		JLabel save = new JLabel("Date Saved");
		save.setFont(panelFont);
		save.setForeground(Color.WHITE);
		save.setVisible(true);
		save.setSize(200, 18);
		continuePanel.add(save, 0);
		save.setLocation(dateSpace, m.getHeight() / 11 - 18);

		JLabel time = new JLabel("Time Saved");
		time.setFont(panelFont);
		time.setForeground(Color.WHITE);
		time.setVisible(true);
		time.setSize(200, 18);
		continuePanel.add(time, 0);
		time.setLocation(timeSpace, m.getHeight() / 11 - 18);

		continuePanel.repaint();
		this.add(continuePanel, BorderLayout.CENTER);
	}

	private void setUpButtons() {
		JPanel bottomPane = new JPanel();
		bottomPane.setLayout(new GridLayout(1, 3));
		bottomPane.setBackground(Color.BLACK);
		bottomPane.setPreferredSize(new Dimension(m.getWidth(),
				m.getHeight() / 8));

		Font text = new Font("Verdana", Font.BOLD, 20);

		ImageIcon scrollImage = new ImageIcon(
				"Images/Classic/ScrollButtonEnd.png");

		JLabel load = new JLabel("Load Game");
		load.setFont(text);
		load.setIcon(scrollImage);
		load.setIconTextGap(-(scrollImage.getIconWidth() / 2 + 60));
		load.setOpaque(false);
		load.setVisible(true);
		load.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				if (name != null) {
					String[] options = new String[4];
					options[0] = "Classic";
					options[1] = "Western";
					options[2] = "Space";
					options[3] = "Cancel";
					int choice = JOptionPane.showOptionDialog(m,
							"Select a theme:", "Theme Selection",
							JOptionPane.DEFAULT_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options,
							options[0]);
					switch (choice) {
					case 0:
						m.changeView(Views.GAME, GameType.CLASSIC, loadGame());
						break;
					case 1:
						m.changeView(Views.GAME, GameType.WESTERN, loadGame());
						break;
					case 2:
						m.changeView(Views.GAME, GameType.SPACE, loadGame());
						break;
					case 3:
						break;
					default:
						m.changeView(Views.GAME, GameType.CLASSIC, loadGame());
						break;
					}
				} else
					JOptionPane.showMessageDialog(m,
							"Please select a game to load.");
			}
		});

		scrollImage = new ImageIcon("Images/Classic/ScrollButtonMid.png");

		JLabel delete = new JLabel("Delete Game");
		delete.setFont(text);
		delete.setIcon(scrollImage);
		delete.setIconTextGap(-(scrollImage.getIconWidth() / 2 + 60));
		// delete the selected file and then make sure that the list and pane are updated
		delete.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				if (name != null && !name.equals(null)) {
					File savedFile = new File("SavedGames/" + name);

					try {
						savedFile.delete();
						deleted = true;
						getListFiles();
						scrollList.validate();
						repaint();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else
					JOptionPane.showMessageDialog(m,
							"Please select a game to delete.");
			}
		});

		scrollImage = new ImageIcon("Images/Classic/ScrollButtonBeg.png");

		JLabel menu = new JLabel("Menu");
		menu.setFont(text);
		menu.setIcon(scrollImage);
		menu.setIconTextGap(-(scrollImage.getIconWidth() / 2 + 60));
		menu.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				m.changeView(Views.TITLE, null, null);
			}
		});
		menu.setVisible(true);

		bottomPane.add(menu);
		bottomPane.add(delete);
		bottomPane.add(load);
		this.add(bottomPane, BorderLayout.SOUTH);
	}

	/*
	 * A private method that takes the list of files in the SavedGames directory and then using the
	 * identified spacing, will print out the desired items (Player Name, Game Type, Date Saved, Time 
	 * Saved) under the appropriate labels.
	 */
	private void getListFiles() {
		listOfGames = savedGames.listFiles();
		String[] temp = new String[listOfGames.length];

		for (int i = 0; i < listOfGames.length; i++) {
			String aName = listOfGames[i].getName();
			String gameType = "Single Player";

			// don't try printing out that CVS folder that keeps on coming up >:(
			if (!aName.contentEquals("CVS")) {
				if (aName.substring(0, 3).contentEquals("_ZT")) {
					gameType = "Multiplayer Server";
				} else if (aName.substring(0, 3).contentEquals("_XT")) {
					gameType = "Multiplayer Client";
				}

				int spaceNum = (gameSpace - nameSpace) / 7;
				int spaceFromName = aName.substring(17, aName.length() - 7)
						.length();
				String spaces = "";

				for (int s = 0; s < spaceNum - spaceFromName; s++) {
					spaces = spaces + " ";
				}

				if (aName.substring(0, 3).contentEquals("_XT")
						|| aName.substring(0, 3).contentEquals("_ZT"))
					spaceNum = (dateSpace - gameSpace) / 18;
				else
					spaceNum = (dateSpace - gameSpace) / 12;
				String spaces2 = "";

				for (int s = 0; s < spaceNum; s++) {
					spaces2 = spaces2 + " ";
				}

				spaceNum = (timeSpace - dateSpace) / 15;
				String spaces3 = "";
				for (int s = 0; s < spaceNum; s++) {
					spaces3 = spaces3 + " ";
				}

				temp[i] = aName.substring(17, aName.length() - 7) + spaces
						+ gameType + spaces2 + aName.substring(7, 9) + "/"
						+ aName.substring(9, 11) + "/" + aName.substring(3, 7)
						+ spaces3 + aName.substring(11, 13) + ":"
						+ aName.substring(13, 15);

			}
		}
		gameList.setListData(temp);
	}

	/**
	 * Loads the game data so that the player can continue from the save point.
	 * Checks if the game is single player or multiplayer (client or server) and
	 * calls the appropriated HuntTheWumpus constructor and passes in the specs.
	 * 
	 * @return Returns a new HuntTheWumpus game with the loaded attributes
	 */
	@SuppressWarnings("unchecked")
	public HuntTheWumpus loadGame() {
		try {
			File playerFile = new File("SavedGames/" + name);

			FileInputStream fileStream = new FileInputStream(playerFile);
			ObjectInputStream objStream = new ObjectInputStream(fileStream);

			LinkedList<Player> sPlayers = null;
			Player sPlayer = null;

			if (name.substring(0, 3).contentEquals("_ZT"))
				sPlayers = (LinkedList<Player>) objStream.readObject();
			else
				sPlayer = (Player) objStream.readObject();

			WumpusMap sMap = (WumpusMap) objStream.readObject();

			int port = 0;
			String ipAdd = "";
			boolean client = false, pvp = false;

			if (name.substring(0, 3).contentEquals("_ZT")
					|| (name.substring(0, 3).contentEquals("_XT"))) {
				port = objStream.readInt();
				pvp = objStream.readBoolean();
				if (name.substring(0, 3).contentEquals("_XT")) {
					ipAdd = (String) objStream.readObject();
					client = true;
				}
			}

			objStream.close();
			if (name.substring(0, 3).contentEquals("_ZT"))
				return new HuntTheWumpus(sPlayers, sMap, port, pvp);
			else
				return new HuntTheWumpus(sPlayer, sMap, client, port, ipAdd,
						pvp);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * A private class since I (Salika) couldn't figure out how else to paint the image behind the scroll panel.
	 */
	private class ContinuePanel extends JPanel {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Image bgImage = new ImageIcon(
					"Images/Classic/continueBackground.png").getImage();
			g.drawImage(bgImage, 0, 0, this.getWidth(), this.getHeight(), null);
		}
	}
}