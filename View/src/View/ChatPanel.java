package View;

import gameModel.HuntTheWumpus;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * This class is the ChatPanel for multiplayer chatting.
 * 
 * @author Salika Dunatunga, Sarah Lutjens, Jane Wang, Denise Werchan
 * 
 */

public class ChatPanel extends JPanel {
	
	private JTextField inputBox;
	private JScrollPane logScroll;
	private JButton sendButton;
	private HuntTheWumpus htw;
	private GameView gameView;
	
	/**
	 * This is the constructor for the Chat Panel
	 * 
	 * @param HuntTheWumpus
	 * 				Takes an instance of the HuntTheWumpus game used in the chat
	 */
	public ChatPanel(HuntTheWumpus htw, GameView gv){
		
		this.gameView = gv;
		this.htw = htw;
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(2,2,2,2);
		
		MasterView.log.setEditable(false);
		MasterView.log.setVisible(true);

		logScroll = new JScrollPane(MasterView.log , JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		         JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		inputBox = new JTextField();
		inputBox.addActionListener(new SendListener());
		inputBox.setEnabled(true);

		sendButton = new JButton();
		sendButton.setFocusable(false);
		sendButton.setText("Send");
		sendButton.addActionListener(new SendListener());
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = .99;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.BOTH;
		this.add(logScroll, gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = .95;
		gbc.weighty = .01;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		this.add(inputBox, gbc);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = .05;
		gbc.weighty = .01;
		gbc.fill = GridBagConstraints.BOTH;
		this.add(sendButton, gbc);
		this.setVisible(true);
		inputBox.grabFocus();

	}

	// this is the listener for sending messages to the server
	private class SendListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			htw.processChat(htw.getPlayer().getName() + ": " + inputBox.getText());
			inputBox.setText("");
			inputBox.grabFocus();
			gameView.grabFocus();
		}
	}
	
	/**
	 * Adds text to the log and appends it with a newline character
	 */
	public void addText(String text) {
		MasterView.log.append(text + '\n');
		MasterView.log.setCaretPosition(MasterView.log.getDocument().getLength());
	}
}