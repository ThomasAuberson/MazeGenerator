import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*
 * Template Library
 * 
 * Author: Thomas Auberson
 * Version: 0.1
 * 
 * This class controls the key action listener
 */

public class KeyHandler implements KeyListener {

	// FIELDS
	Display display;

	// CONSTRUCTOR
	public KeyHandler(Display d){
		display = d;
		//System.out.println("Key Listener Active");
	}
	
	
	@Override
	public void keyPressed(KeyEvent k) {
		// TODO Auto-generated method stub
		display.keyPressed(k.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent k) {
		// TODO Auto-generated method stub
		// display.keyReleased(k.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent k) {
		// TODO Auto-generated method stub
		// display.keyTyped(k.getKeyChar());
	}

}
