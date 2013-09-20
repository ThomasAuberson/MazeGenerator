import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.io.*;
import java.util.*;

import javax.swing.JOptionPane;

public class MazeGenerator {

	// FIELDS
	// Maze Map
	private int[][] mazeMap;
	private int x, y; // Dimensions - SHOULD BE ODD!
	private Point start, end;
	private Stack<int[][]> prevMaps = new Stack<int[][]>();
	
	// Display
	private TileSet t;
	private int tileSize;
	private Display display;
	private Point startPoint;
	private Point shiftPoint = new Point(0, 0);
	private Point prevShiftPoint = new Point(0, 0);
	private Point pressedPoint;

	// Directions
	private Direction[] directions = { new Direction("NORTH"),
			new Direction("EAST"), new Direction("SOUTH"),
			new Direction("WEST") };

	private Random random = new Random();
	private String saveFormat = "a000173";
	public String mouseTool = "Wall";

	// CONSTRUCTOR
	public MazeGenerator(Dimension d, TileSet t, int s, Display dis) {
		this.t = t;
		display = dis;
		tileSize = s;

		newMaze(d);
	}

	public void newMaze(Dimension d) {
		x = d.width;
		y = d.height;
		start = new Point(1, 1);
		end = new Point(x - 2, y - 2); // Max index is x/y-1 (but is wall)

		mazeMap = new int[x][y];

		generateFreshMaze();
		backUpMap();

		startPoint = new Point(((display.getWidth() - tileSize * x) / 2),
				((display.getHeight() - tileSize * y) / 2));

	}

	// SAVING/OPENING MAPS
	public void saveMap() {
		String name = JOptionPane.showInputDialog(display, "Type a save name:",
				"Save Maze Map", JOptionPane.PLAIN_MESSAGE);
		PrintStream prin;
		try {
			prin = new PrintStream("saves/" + name + ".map");

			prin.println(saveFormat);
			prin.print(x + " ");
			prin.print(y + " ");
			prin.println("line");
			for (int i = 0; i < x; i++) {
				for (int k = 0; k < y; k++) {
					prin.print(mazeMap[i][k] + " ");
				}
				prin.println("line");
			}

			Scanner scan = new Scanner(new File("saves/SaveList.set"));
			int n = Integer.parseInt(scan.nextLine());
			if (n == 0) {
				scan.close();
				return;
			}
			Object[] options = new Object[n + 1];
			for (int i = 0; i < n; i++) {
				options[i] = scan.nextLine();
			}
			options[n] = name;
			scan.close();
			prin.close();
			prin = new PrintStream("saves/SaveList.set");

			prin.println(n + 1);
			for (int i = 0; i < n + 1; i++) {
				prin.println(options[i]);
			}
			prin.close();
		} catch (FileNotFoundException e) {
		}
	}

	public void openMap() {
		try {
			File file = new File("saves/SaveList.set");
			Scanner scan = new Scanner(file);
			int n = Integer.parseInt(scan.nextLine());
			if (n == 0) {
				scan.close();
				return;
			}
			Object[] options = new Object[n];
			for (int i = 0; i < n; i++) {
				options[i] = scan.nextLine();
			}
			String s = (String) JOptionPane.showInputDialog(null,
					"Select a save:", "Open Maze Map",
					JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			scan = new Scanner(new File("saves/" + s + ".map"));
			if (!scan.nextLine().equals(saveFormat)) {
				scan.close();
				return;
			}
			int inx = scan.nextInt();
			int iny = scan.nextInt();
			scan.nextLine();
			newMaze(new Dimension(inx, iny));
			for (int i = 0; i < x; i++) {
				for (int k = 0; k < y; k++) {
					mazeMap[i][k] = scan.nextInt();
				}
				scan.nextLine();
			}
			scan.close();
		} catch (Exception e) {
		}
	}

	// GENERATOR METHODS
	public void generateFreshMaze() {
		for (int i = 0; i < x; i++) {
			for (int k = 0; k < y; k++) {
				int kMod = k % 2;
				int iMod = i % 2;
				boolean iIsOdd = (iMod == 1);
				boolean kIsOdd = (kMod == 1);
				if ((!iIsOdd && !kIsOdd) || (k == 0) || (i == 0)
						|| (k == (y - 1)) || (i == (x - 1))) {
					mazeMap[i][k] = t.WALL;
				} else if ((iIsOdd) && (kIsOdd)) {
					mazeMap[i][k] = t.OPEN;
				} else {
					mazeMap[i][k] = t.TEMP;
				}
			}
		}
		mazeMap[end.x][end.y] = t.END;
		mazeMap[start.x][start.y] = t.START;
	}

	public void generateRandomMaze() {
		// Generate Fresh Board
		newMaze(new Dimension(x, y));
		// Generate main path
		generateMainPath();
		// Generate Subpaths
		boolean b = true;
		while (b) {
			b = generateRandomSubpath();
		}
		// temp to wall
		setTempToWall();
		// path to open
		setPathToOpen();
	}

	public void generateMainPath() {
		boolean b = true;
		mazeMap[end.x][end.y] = t.OPEN;
		int trial = 0;
		while (b) {
			trial++;
			int[][] a = new int[x][y];
			for (int i = 0; i < x; i++) {
				a[i] = mazeMap[i].clone();
			}
			b = !generateMainPath(copyMap());
			if (trial > 20) {
				JOptionPane.showMessageDialog(null,
						"Main Path Generation Failed!", "ERROR",
						JOptionPane.WARNING_MESSAGE);
				break;
			}
		}
		mazeMap[end.x][end.y] = t.END;
	}

	public boolean generateMainPath(int[][] map) { // Return True if generation
													// is successful (and set
													// mazeMap)
		Point current = start; // Initialise Start Point
		// map[current.x][current.y] = t.PATH;
		// System.out.println("Start  " + start);
		// System.out.println("End  " + end);
		boolean success = false; // Successful path generation;

		while (true) {
			// System.out.println("Current  " + current);

			if (end.equals(current)) {
				success = true;
				System.out.println("Finished!!");
				break; // PATH GENERATION COMPLETE!!
			} else {
				current = generatePathTile(map, current);
				if (current == null)
					break;
			}
		}
		if (success) {
			mazeMap = map;
			return true;
		} else
			System.out.println("Fail");
		return false;
	}

	public boolean generateRandomSubpath() {
		Point p = findViablePathStart();
		if (p == null)
			return false;
		generateSubPath(mazeMap, p);
		return true;
	}

	public void generateSubPath(int[][] map, Point current) {
		while (true) {
			current = generatePathTile(map, current);
			if (current == null)
				break;
		}
	}

	public Point generatePathTile(int[][] map, Point current) {
		// Decide Options
		int index = 0;
		Direction options[] = new Direction[4];

		for (int i = 0; i < 4; i++) {
			Point p = viableTile(map, current, i);
			if (p == null)
				continue;
			else {
				options[index] = directions[i];
				index++;
			}
		}
		// Choose Option
		if (index == 0)
			return null; // PATH GENERATION FAILED!!
		Direction target = options[random.nextInt(index)];
		if (index == 2) {
			if ((options[0] == directions[0]) && (options[1] == directions[2])) {
				target = options[1];

			} else if ((options[0] == directions[1])
					&& (options[1] == directions[3])) {
				target = options[0];
			}
		}
		// Move
		Point tp1 = target.next(current, 1);
		map[tp1.x][tp1.y] = t.PATH;
		// System.out.println("Point TP1 "+tp1);
		Point tp2 = target.next(current, 2);
		map[tp2.x][tp2.y] = t.PATH;
		// System.out.println("Point TP2 "+tp2);
		return tp2;
	}

	public Point viableTile(int[][] map, Point current, int i) {
		if (i < 0)
			i = 3;
		if (i > 3)
			i = 0;
		Point p1 = directions[i].next(current, 1);
		if (map[p1.x][p1.y] != (t.TEMP)) {
			// System.out.println("Skip 1");
			return null; // Not Edge
		}
		Point p2 = directions[i].next(current, 2);
		if (map[p2.x][p2.y] != (t.OPEN)) {
			// System.out.println("Skip 2");
			return null; // Not path already
		}
		return p2;
	}

	// SCOUT VIABLE PATH TILES
	public Point findViablePathStart() {
		for (int i = 0; i < x; i++) {
			for (int k = 0; k < y; k++) {
				if (mazeMap[i][k] == t.OPEN) {
					Point p = new Point(i, k);
					int index = 0;
					Direction options[] = new Direction[4];
					for (int j = 0; j < 4; j++) {
						Point p2 = directions[j].next(p, 2);
						if ((p2.x < 0) || (p2.x >= x) || (p2.y < 0)
								|| (p2.y >= y))
							continue;
						if (mazeMap[p2.x][p2.y] == t.PATH) {
							options[index] = directions[j];
							index++;
						}
					}

					if (index == 0)
						continue; // PATH GENERATION FAILED!!
					Direction target = options[random.nextInt(index)];

					return target.next(p, 2);
				}
			}
		}
		return null;
	}

	// FIND OPEN TILE
	public Point findOpenTile() {
		for (int i = 0; i < x; i++) {
			for (int k = 0; k < y; k++) {
				if (mazeMap[i][k] == t.OPEN) {
					return new Point(i, k);
				}
			}
		}
		return null;
	}

	// SET TEMP TO WALL
	public void setTempToWall() {
		for (int i = 0; i < x; i++) {
			for (int k = 0; k < y; k++) {
				if (mazeMap[i][k] == t.TEMP)
					mazeMap[i][k] = t.WALL;
			}
		}
	}

	// SET PATHS TO OPEN
	public void setPathToOpen() {
		for (int i = 0; i < x; i++) {
			for (int k = 0; k < y; k++) {
				if ((mazeMap[i][k] == t.PATH) || (mazeMap[i][k] == t.PATH))
					mazeMap[i][k] = t.OPEN;
			}
		}
	}

	// MOUSE ACTION LISTENER
	public void mousePressed(Point p, int button) {
		if (button == 1) {
			pressedPoint = p;
			prevShiftPoint = (Point) shiftPoint.clone();
		}
	}

	public void mouseClicked(Point p, int button) {
		System.out.println("startPoint " + startPoint + "p " + p);
		System.out.println("tileSize: " + tileSize + "p.y Modulo tileSize"
				+ (p.y % tileSize));
		p = new Point(p.x - startPoint.x - shiftPoint.x
				- ((p.x - startPoint.x - shiftPoint.x) % tileSize), p.y
				- startPoint.y - shiftPoint.y
				- ((p.y - startPoint.y - shiftPoint.y) % tileSize));
		System.out.println("Stage 2: p " + p);
		int i = p.x / tileSize;
		int k = ((p.y) / tileSize);
		System.out.println("Mouse Y " + k + "Mouse X " + i);
		if (mouseTool.equals("Subpath")) {
			generateSubPath(mazeMap, new Point(i, k));
			backUpMap();
		} else if (mouseTool.equals("Wall")) {
			if (button == 1) {
				mazeMap[i][k] = t.WALL;
			} else if (button == 3) {
				mazeMap[i][k] = t.OPEN;
			}
			backUpMap();
		} else if (mouseTool.equals("Path")) {
			if (button == 1) {
				mazeMap[i][k] = t.PATH;
			} else if (button == 3) {
				mazeMap[i][k] = t.TEMP;
			}
			backUpMap();
		} else if (mouseTool.equals("Start")) {
			if (button == 1) {
				mazeMap[start.x][start.y] = t.OPEN;
				mazeMap[i][k] = t.START;
				start = new Point(i, k);
			} else if (button == 3) {
				mazeMap[end.x][end.y] = t.OPEN;
				mazeMap[i][k] = t.END;
				end = new Point(i, k);
			}
			backUpMap();
		}
	}

	// SCROLLING
	public void zoom(boolean in) { //True for zoom in
		if (in) {
			tileSize *= 2;
		} else {
			tileSize = (int) (tileSize / 2);
		}
		startPoint = new Point(((display.getWidth() - tileSize * x) / 2),
				((display.getHeight() - tileSize * y) / 2));
	}

	public void edgeScroll(Point p) {
		int x = p.x - pressedPoint.x + prevShiftPoint.x;
		int y = p.y - pressedPoint.y + prevShiftPoint.y;
		shiftPoint = new Point(x, y);
	}

	public void centerMazePosition() {
		shiftPoint = new Point(0, 0);
	}

	// UNDO FUNCTION
	public void backUpMap() {
		prevMaps.push(copyMap());
	}

	public void undo() {
		prevMaps.pop();
		mazeMap = prevMaps.peek();
	}

	public int[][] copyMap() {
		int[][] a = new int[x][y];
		for (int i = 0; i < x; i++) {
			a[i] = mazeMap[i].clone();
		}
		return a;
	}

	// RENDERING
	public void paint(Graphics g) {
		for (int i = 0; i < x; i++) {
			for (int k = 0; k < y; k++) {
				t.paint(g, i * tileSize + startPoint.x + shiftPoint.x, k
						* tileSize + startPoint.y + shiftPoint.y, tileSize,
						mazeMap[i][k]);
			}
		}
	}
}
