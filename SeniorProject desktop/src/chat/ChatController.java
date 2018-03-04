package chat;/**
 *
 * The controller portion of the MVC pattern.
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ChatController {
	private static ChatView view;
	private static ChatModel model;
	
	public static void main(String[] args) {
		view = new ChatView();
		model = new ChatModel();
		
		view.setSendListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// first get the message from the view
				final String message1 = view.getSendText();
				
				// now give the message to the model
				final String message2 = model.reverseText(message1);
				
				// now give it back to the model to be displayed
				view.setDisplayArea(message2);
			}
		});
		
		view.setExitListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		view.setSendKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent arg0) {
				/** not implemented */
				
			}

			public void keyPressed(KeyEvent arg0) {
	            if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
	            		view.setDisplayArea(model.reverseText(view.getSendText()));
	            }
			}

			public void keyReleased(KeyEvent arg0) {
				/** not implemented */
			}
		});
	}

}
