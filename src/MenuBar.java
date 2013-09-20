import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;

/*
 * Java Maze Generator
 * (Extracted from Template Library v0.1)
 *  	
 * Author: Thomas Auberson
 * Version: 0.1
 *  
 * A JMenuBar which takes arrays of button names. Extracted from Template Library v0.1
 */

public class MenuBar extends JMenuBar implements ActionListener {

	private static final long serialVersionUID = 2973899995253691249L;

	// FIELDS
	private Display display;

	private int MENUS_NUMBER = 3;
	private String title1 = "File";
	private String title2 = "Controls";
	private String title3 = "Mouse Tools";
	private String[] menu1 = { "New", "Open", "Save", "***", "Map Size", "***",
			"Undo" , "***" , "About"};
	private String[] menu2 = { "Generate Random Maze", "***", "Generate Main Path","Generate Sub Path", "***", "Temp Tiles to Wall",
			"Path Tiles to Open" , "***" , "Center Maze Position" };
	private String[] menu3 = { "*rad*", "Wall/Open Tool", "Path/Temp Tool",
			"Start/End Tool", "Sub-Path Tool" };

	private JMenu[] menus = new JMenu[MENUS_NUMBER];
	private int menuIndex = 0;

	// CONSTRUCTOR
	public MenuBar(Display display) {
		this.display = display;
		loadMenu(title1, menu1);
		loadMenu(title2, menu2);
		loadMenu(title3, menu3);
	}

	public void loadMenu(String title, String[] subtitles) {
		JMenu menu = new JMenu(title);
		add(menu);
		boolean radioButtonMode = false;
		ButtonGroup group = new ButtonGroup();

		for (int i = 0; i < subtitles.length; i++) {
			if (subtitles[i].equals("***")) {
				menu.add(new JSeparator());
				radioButtonMode = false;
			} else if (subtitles[i].equals("*rad*")) {
				radioButtonMode = true;
				group = new ButtonGroup();
				
			} else {
				if (radioButtonMode) {
					menu.add(radioButton(subtitles[i],group));
				} else {
					menu.add(menuItem(subtitles[i]));
				}
			}
		}
		menus[menuIndex] = menu;
		menuIndex++;
	}

	public JMenuItem menuItem(String name) {
		JMenuItem m = new JMenuItem(name);
		m.addActionListener(this);
		m.setActionCommand(name);
		return m;
	}

	public JMenuItem radioButton(String name, ButtonGroup group) {
		JMenuItem m = new JRadioButtonMenuItem(name);
		m.addActionListener(this);
		m.setActionCommand(name);
		group.add(m);
		return m;
	}

	// ACTION LISTENER
	public void actionPerformed(ActionEvent e) {
		display.menuButtonClicked(e.getActionCommand());
	}
}
