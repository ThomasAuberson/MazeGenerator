import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
 * Maze Generator
 * (Extracted from Template Library v0.1)
 * 
 * Author: Thomas Auberson
 * Version: 0.3a
 * 
 * This class controls the display canvas (a JPanel)
 */

public class Display extends JPanel implements Runnable {

	private static final long serialVersionUID = -6054970217348311809L;
	// FIELDS
	public Thread thread = new Thread(this);
	private JFrame frame;
	private MenuBar menu;
	private MazeGenerator mazeGenerator;

	// DEFAULT MAP DIMENSIONS
	private int TILE_SIZE = 30;
	private int NUM_TILES = 21;

	private Dimension size = new Dimension(700, 700);
	private boolean resizable = true;
	private String title = "Maze Generator";

	// CONSTRUCTOR
	public Display() {
		// Initialise the JFrame
		frame = createFrame(title, size, resizable);
		frame.add(this);

		// Initilise Menu Bar
		menu = new MenuBar(this);
		frame.setJMenuBar(menu);

		frame.setVisible(true);

		// Initialise Tile Set
		TileSet t = new TileSet();

		// Initialise Maze Display
		mazeGenerator = new MazeGenerator(new Dimension(NUM_TILES, NUM_TILES),
				t, TILE_SIZE, this);

		// Initiliase Mouse Listeners
		MouseHandler mouse = new MouseHandler(this);
		this.addMouseListener(mouse);
		this.addMouseWheelListener(mouse);
		this.addMouseMotionListener(mouse);

		// Initialise Key Listener
		KeyHandler key = new KeyHandler(this);
		this.addKeyListener(key);
		requestFocusInWindow();

		// Override Window Closing
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				checkSaved();
			}
		});

		thread.start();
	}

	private JFrame createFrame(String title, Dimension size, boolean resizable) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(size);
		frame.setTitle(title);
		frame.setResizable(resizable);
		frame.setLocationRelativeTo(null); // Sets window in centre
		frame.setLayout(new GridLayout(1, 1, 0, 0));
		return frame;
	}

	public void checkSaved() {
		
	}

	// KEY ACTION LISTENER
	public void keyPressed(int button) {
		if (button == 107) { // "+"
			mazeGenerator.zoom(true);
		} else if (button == 109) { // "-"
			mazeGenerator.zoom(false);
		}
	}

	// MOUSE ACTION LISTENER
	public void mousePressed(Point p, int button) {
		requestFocusInWindow();
		SwingUtilities.convertPointFromScreen(p, this);
		mazeGenerator.mousePressed(p, button);
	}

	public void mouseClicked(Point p, int button) {
		SwingUtilities.convertPointFromScreen(p, this);
		mazeGenerator.mouseClicked(p, button);
	}

	public void mouseDragged(Point p) {
		// System.out.println("Dragged: "+x+" "+y);
		mazeGenerator.edgeScroll(p);
	}

	public void mouseWheelMoved(int distance) {
		mazeGenerator.zoom(distance < 0);
	}

	// MENU ACTION LISTENER
	public void menuButtonClicked(String button) {
		if (button.equals("Generate Main Path")) {
			mazeGenerator.generateMainPath();
			mazeGenerator.backUpMap();
		} else if (button.equals("Map Size")) {
			String s = JOptionPane.showInputDialog(null, "Type a size:", "",
					JOptionPane.PLAIN_MESSAGE);
			int n = Integer.parseInt(s);
			NUM_TILES = n;
		} else if (button.equals("New")) {
			mazeGenerator.newMaze(new Dimension(NUM_TILES, NUM_TILES));
		} else if (button.equals("Save")) {
			mazeGenerator.saveMap();
		} else if (button.equals("Open")) {
			mazeGenerator.openMap();
		} else if (button.equals("Temp Tiles to Wall")) {
			mazeGenerator.setTempToWall();
			mazeGenerator.backUpMap();
		} else if (button.equals("Path Tiles to Open")) {
			mazeGenerator.setPathToOpen();
			mazeGenerator.backUpMap();
		} else if (button.equals("Undo")) {
			mazeGenerator.undo();
		} else if (button.equals("Wall/Open Tool")) {
			mazeGenerator.mouseTool = "Wall";
		} else if (button.equals("Start/End Tool")) {
			mazeGenerator.mouseTool = "Start";
		} else if (button.equals("Sub-Path Tool")) {
			mazeGenerator.mouseTool = "Subpath";
		} else if (button.equals("Path/Temp Tool")) {
			mazeGenerator.mouseTool = "Path";
		} else if (button.equals("Center Maze Position")) {
			mazeGenerator.centerMazePosition();
		} else if (button.equals("Generate Sub Path")) {
			mazeGenerator.generateRandomSubpath();
			mazeGenerator.backUpMap();
		} else if (button.equals("Generate Random Maze")) {
			mazeGenerator.generateRandomMaze();
			mazeGenerator.backUpMap();
		} else if (button.equals("About")) {
			JOptionPane
					.showMessageDialog(
							null,
							"<HTML>"+title+"+<br>Version: "
									+ Main.version
									+ "<br>Author: Thomas Auberson<br><br>This program randomly generates square mazes.<br>Mazes can also be manually created and edited</HTML>",
							"", JOptionPane.PLAIN_MESSAGE);
		}
	}

	// RENDERING
	public void paintComponent(Graphics g) {
		// Display Background
		g.setColor(new Color(10, 10, 50));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		// Display Maze Zone
		mazeGenerator.paint(g);
	}

	// THREAD
	public void run() {
		while (true) {

			repaint();

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}

		}
	}
}