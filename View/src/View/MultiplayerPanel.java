package View;

import gameModel.HuntTheWumpus;
import gameModel.Player;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class MultiplayerPanel extends JPanel{
	JLabel playerName[];
	List<Player> playerList;
	
	public MultiplayerPanel(HuntTheWumpus game){
		playerList = game.getPlayerList();
		playerName = new JLabel[playerList.size()];			
		
		for(int i = 0; i < playerList.size(); i++){
			playerName[i] = new JLabel(playerList.get(i).getName());
			playerName[i].setFont(new Font("Arial", Font.PLAIN, 30));
			this.add(playerName[i]);
		}
	}
	
	// adds a new player/updates list
	public void addPlayer() {
		playerName = new JLabel[playerList.size()];			
		
		for(int i = 0; i < playerList.size(); i++){
			playerName[i] = new JLabel(playerList.get(i).getName());
			playerName[i].setFont(new Font("Arial", Font.PLAIN, 30));
			this.add(playerName[i]);
		}
		
		for(int i = 0; i < playerList.size(); i++){
			if (playerList.get(i).isDead()){
				playerName[i].setForeground(Color.GRAY);
			}
		}
	}
	
	public void updatePlayerList(){
		for(int i = 0; i < playerList.size(); i++){
			if (playerList.get(i).isDead()){
				playerName[i].setForeground(Color.GRAY);
			}
		}
	}
}