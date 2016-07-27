package View;

import gameModel.Food;
import gameModel.HuntTheWumpus;
import gameModel.Item;
import gameModel.Silencer;
import gameModel.TeleportBlocker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * This JPanel sets up the layout for the inventory list and energy
 * levels of the player.
 * 
 * @author Salika Dunatunga, Sarah Lutjens, Jang Wang, Denise Werchan
 * 
 */
public class InventoryPanel extends JPanel{
	
	private JProgressBar energy;
	private JButton food;
	private JButton silencer;
	private JButton blocker;
	private JLabel foodNum;
	private JLabel silencerNum;
	private JLabel blockerNum;
	private HuntTheWumpus game;
	private ThemeGenerator themeGen;
	private GameView gameView;
	
	/**
	 * This is the constructor for InventoryPanel. It takes in three parameters
	 * to setup the layout for the InventoryPanel.
	 * 
	 * @param g
	 * 			instance of the main game model, HuntTheWumpus
	 * @param gv
	 * 			instance of the GameView where this panel is placed
	 * @param gt
	 * 			instance of the GameType (theme) selected by the user
	 */
	public InventoryPanel(HuntTheWumpus g, GameView gv, GameType gt){
		game = g;
		gameView = gv;
		themeGen = new ThemeGenerator(gt);
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(1,1,1,1);
		gbc.fill = GridBagConstraints.BOTH;
		
		String name;
		if (game.getPlayer().getName() == null || game.getPlayer().getName() == ""){
			name = "Guest";
		}
		else{
			name = game.getPlayer().getName();
		}
		JLabel playerName = new JLabel("Name:  " + name);
		playerName.setHorizontalAlignment(JLabel.CENTER);
		playerName.setVerticalAlignment(JLabel.BOTTOM);
		playerName.setFont(themeGen.inventoryFont);
		playerName.setForeground(themeGen.inventoryFontColor);
		
		
		
		JLabel playerStats = new JLabel("Energy Level:");
		playerStats.setFont(themeGen.inventoryFont);
		playerStats.setHorizontalAlignment(JLabel.CENTER);
		playerStats.setForeground(themeGen.inventoryFontColor);
		
		JLabel inventoryTitle = new JLabel("Inventory:");
		inventoryTitle.setFont(themeGen.inventoryFont);
		inventoryTitle.setHorizontalAlignment(JLabel.CENTER);
		inventoryTitle.setVerticalAlignment(JLabel.TOP);
		inventoryTitle.setForeground(themeGen.inventoryFontColor);
		
		JPanel inventoryPanel = new JPanel();
		inventoryPanel.setOpaque(false);
		inventoryPanel.setLayout(new GridLayout(2,3));
		food = new JButton("Food");
		food.setFocusable(false);
		food.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				LinkedList<Item> inventoryItems = game.getPlayer().getInventory();
				Item removedItem = null;
				int itemIndex = -1;
				for(int i = 0; i < inventoryItems.size(); i++){
					if(inventoryItems.get(i) instanceof Food){
						removedItem = inventoryItems.get(i);
						itemIndex = i;
					}
				}
				if(removedItem != null){
					removedItem.activateEffect(game.getPlayer());
					inventoryItems.remove(itemIndex);
				}
				InventoryPanel.this.repaint();
				

			}});
		silencer = new JButton("Silencer");
		silencer.setFocusable(false);
		silencer.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				LinkedList<Item> inventoryItems = game.getPlayer().getInventory();
				Item removedItem = null;
				int itemIndex = -1;
				for(int i = 0; i < inventoryItems.size(); i++){
					if(inventoryItems.get(i) instanceof Silencer){
						removedItem = inventoryItems.get(i);
						itemIndex = i;
					}
				}
				if(removedItem != null){
					removedItem.activateEffect(game.getPlayer());
					inventoryItems.remove(itemIndex);
				}
				InventoryPanel.this.repaint();
					
			}});
		blocker = new JButton("Blocker");
		blocker.setFocusable(false);
		blocker.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				LinkedList<Item> inventoryItems = game.getPlayer().getInventory();
				Item removedItem = null;
				int itemIndex = -1;
				for(int i = 0; i < inventoryItems.size(); i++){
					if(inventoryItems.get(i) instanceof TeleportBlocker){
						removedItem = inventoryItems.get(i);
						itemIndex = i;
					}
				}
				if(removedItem != null){
					removedItem.activateEffect(game.getPlayer());
					inventoryItems.remove(itemIndex);
				}
				InventoryPanel.this.repaint();
				gameView.paintRoom(); //repaint to show blocker
			}});
		foodNum = new JLabel();
		foodNum.setHorizontalAlignment(JLabel.CENTER);
		foodNum.setForeground(themeGen.inventoryFontColor);
		silencerNum = new JLabel();
		silencerNum.setHorizontalAlignment(JLabel.CENTER);
		silencerNum.setForeground(themeGen.inventoryFontColor);
		blockerNum = new JLabel();
		blockerNum.setHorizontalAlignment(JLabel.CENTER);
		blockerNum.setForeground(themeGen.inventoryFontColor);
		inventoryPanel.add(food);
		inventoryPanel.add(silencer);
		inventoryPanel.add(blocker);
		inventoryPanel.add(foodNum);
		inventoryPanel.add(silencerNum);
		inventoryPanel.add(blockerNum);
		updateInventoryCount();
		
		JLabel blankLabel = new JLabel();
		blankLabel.setPreferredSize(new Dimension(100,40));
		
		energy = new JProgressBar(0, 100);
		energy.setValue(game.getPlayer().getEnergy());
		energy.setStringPainted(true);
		energy.setForeground(Color.BLACK);
		energy.setPreferredSize(new Dimension(300,50));
		updateEnergy();

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = .5;
		this.add(playerName, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		gbc.weighty = .1;
		this.add(playerStats, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0;
		gbc.weighty = .1;
		gbc.fill = GridBagConstraints.NONE;
		this.add(energy, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 0;
		gbc.weighty = .1;
		gbc.fill = GridBagConstraints.BOTH;
		this.add(inventoryTitle, gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 0;
		gbc.weighty = .1;
		gbc.fill = GridBagConstraints.NONE;
		this.add(inventoryPanel, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.weightx = 0;
		gbc.weighty = .1;
		gbc.fill = GridBagConstraints.NONE;
		this.add(blankLabel, gbc);
		
	}
	
	
	/**
	 * This method overrides the JPanel paintComponent method to allow for repaint to be called
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(themeGen.inventoryPicture, 0, 0, this.getWidth(), this.getHeight(), null);
		updateEnergy();
		updateInventoryCount();
	}
	
	private void updateInventoryCount() {
		LinkedList<Item> inventory = game.getPlayer().getInventory();
		int foodCount = 0;
		int silencerCount = 0;
		int blockerCount = 0;
		
		for(Item item: inventory){
			if(item instanceof Food){
				foodCount++;
			}
			else if(item instanceof Silencer){
				silencerCount++;
			}
			else if(item instanceof TeleportBlocker){
				blockerCount++;
			}
			else{
				System.out.println("Unknown item type : " + item.toString());
			}
		}
		
		foodNum.setText("" + foodCount);
		silencerNum.setText("" + silencerCount);
		blockerNum.setText("" + blockerCount);
		updateButtons(foodCount, silencerCount, blockerCount);
	}
	
	private void updateButtons(int foodCount, int silencerCount, int blockerCount) {
		if (foodCount == 0){
			food.setEnabled(false);
		}
		else{
			food.setEnabled(true);
		}
		
		if (silencerCount == 0){
			silencer.setEnabled(false);
		}
		else{
			silencer.setEnabled(true);
		}
		
		if (blockerCount == 0){
			blocker.setEnabled(false);
		}
		else if (blockerCount != 0 && !game.getPlayer().getTeleportBlock()){
			blocker.setEnabled(true);
		}
		
	}

	/**
	 * Allows for an update to player energy levels and the button activation
	 * for the button associated with the TeleportBlocker
	 */
	public void updateEnergy() {
		energy.setValue(game.getPlayer().getEnergy());
		energy.setString(game.getPlayer().getEnergy() + "/100");
		
		if(game.getPlayer().getTeleportBlock()){
			blocker.setText("Activated...");
			blocker.setEnabled(false);
		}
		else{
			blocker.setText("Blocker");
			blocker.setEnabled(true);
		}
	}
}
