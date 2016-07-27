package View;

import javax.swing.JPanel;

/**
 * MasterViewPanel that extends JPanel. This is the super class for the various
 * classes used in the CardLayout in MasterView.
 * 
 * @author Salika Dunatunga, Sarah Lutjens, Jang Wang, Denise Werchan
 * 
 */

public class MasterViewPanel extends JPanel{
	
	protected MasterView m;
	
	/**
	 * Takes and saves an instance of MasterView where all of its subclasses appear.
	 * 
	 * @param m
	 * 			Instance of MasterView that contains this class.
	 */
	public MasterViewPanel(MasterView m){
		this.m = m;
	}

}
