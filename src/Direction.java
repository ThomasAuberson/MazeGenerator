import java.awt.Point;

public class Direction {
	
	// FIELDS
	private String name;
	private boolean xPlane = false;
		
	// CONSTRUCTOR
	public Direction(String name){
		this.name = name;
		if((name.equals("WEST"))||(name.equals("EAST"))){
			xPlane = true;
		}
	}
	
	public Point next(Point p, int spaces){
		Point p2 = new Point(1,1);
		if(name.equals("NORTH")){
			p2.setLocation(p.x, ((p.y)-spaces));
			//System.out.println("North "+spaces+" = "+p2);
		} else if(name.equals("SOUTH")){
			p2.setLocation(p.x, ((p.y)+spaces));
			//System.out.println("South "+spaces+" = "+p2);
		} else if(name.equals("EAST")){
			p2.setLocation(((p.x)+spaces), p.y);
			//System.out.println("East "+spaces+" = "+p2);
		} else if(name.equals("WEST")){
			p2.setLocation(((p.x)-spaces),p.y);
			//System.out.println("West "+spaces+" = "+p2);
		}
		return p2;
	}
	
	public boolean getXPlane(){
		return xPlane;
	}
}
