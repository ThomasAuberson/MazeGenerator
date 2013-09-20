import java.awt.Color;
import java.awt.Graphics;


public class TileSet {

	//FIELDS	
	public final int WALL = 0;
	public final int OPEN = 1;
	public final int PATH = 2;
	public final int TEMP = 5;
	public final int START = 7;
	public final int END = 8;
	
	private Color[] colors = new Color[10];	
	
	//CONSTRUCTOR
	public TileSet(){
		loadColors();
	}
	
	public void loadColors(){
		colors[WALL] = Color.black;
		colors[OPEN] = Color.white;
		colors[TEMP] = Color.gray;
		colors[PATH] = Color.pink;
		colors[END] = Color.red;
		colors[START] = Color.blue;
	}
		
	//RENDERING
	public void paint(Graphics g, int x, int y, int size, int id){
		g.setColor(colors[id]);
		g.fillRect(x,y,size,size);
		g.setColor(Color.black);
		g.drawRect(x,y,size,size);
	}
}
