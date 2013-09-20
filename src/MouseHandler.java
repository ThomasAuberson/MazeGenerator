import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/*
 * Java Maze Generator
 * (Extracted from Template Library v0.1)
 * 
 * Author: Thomas Auberson
 * Version: 0.1
 * 
 * This class controls the mouse action listener	
 */

public class MouseHandler implements MouseListener, MouseWheelListener, MouseMotionListener {
	
	//FIELDS
	Display display;
	
	//CONSTRUCTOR
	public MouseHandler(Display display){
		this.display = display;
		System.out.println("Mouse Active");
	}
	
	//ACTION LISTENERS
	@Override
	public void mouseClicked(MouseEvent m) {
		// TODO Auto-generated method stub
		display.mouseClicked(m.getLocationOnScreen(), m.getButton());
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent m) {	
		System.out.println("Mouse Pressed");
		display.mousePressed(m.getLocationOnScreen(), m.getButton());
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent mw) {
		// TODO Auto-generated method stub
		display.mouseWheelMoved(mw.getWheelRotation());
	}

	@Override
	public void mouseDragged(MouseEvent m) {
		// TODO Auto-generated method stub
		display.mouseDragged(m.getPoint());
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
