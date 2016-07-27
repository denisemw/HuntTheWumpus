package gameModel;

import java.io.Serializable;

public class RoomNavigator implements Serializable {
	private Player player;
	private Point location;
	private Point miniLeeway;
	
	public RoomNavigator(Player p){
		this.player = p;
		this.location = null;
		this.setMiniLeeway(new Point (3,3));
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public void setLocation(Point p){
		location = p;
	}
	
	public Point getLocation(){
		return location;
	}
	
	public void setMiniLeeway(Point miniL){
		miniLeeway = miniL;
	}
	
	public Point getMiniLeeway(){
		return miniLeeway;
	}
}
