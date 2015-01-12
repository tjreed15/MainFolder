import acm.graphics.*;
import acm.program.*;

public class GShape extends GraphicsProgram{
	
	public GCompound triangle = new GCompound();
	public GCompound pentagon = new GCompound();
	public GCompound hexagon = new GCompound();
	public GCompound septagon = new GCompound();
	public GCompound octagon = new GCompound();
	
	
	
	public GShape(){
		GPolygon tri = new GPolygon(0, 0);
		tri.addEdge(0, 0);
		tri.addEdge(0, 0);
		tri.addEdge(0, 0);
		triangle.add(tri);
		pentagon.add(tri);
		hexagon.add(tri);
		septagon.add(tri);
		octagon.add(tri);
	}
	
	public GShape(double width, double height){
		GPolygon tri = new GPolygon(0, height);
		tri.addEdge(width, 0);
		tri.addEdge(-width/2, -height);
		tri.addEdge(-width/2, height);
		triangle.add(tri);
		
		GPolygon five = new GPolygon(0, height);
		five.addEdge(width, 0);
		five.addEdge(0, -height/2);
		five.addEdge(-width/2, -height/2);
		five.addEdge(-width/2, height/2);
		five.addEdge(0, height/2);
		pentagon.add(five);
		
		GPolygon six = new GPolygon(0, height/2);
		six.addEdge(width/3, height/2);
		six.addEdge(width/3, 0);
		six.addEdge(width/3, -height/2);
		six.addEdge(-width/3, -height/2);
		six.addEdge(-width/3, 0);
		six.addEdge(-width/3, height/2);
		hexagon.add(six);
		
		GPolygon sev = new GPolygon(width/4, height);
		sev.addEdge(width/2, 0);
		sev.addEdge(width/4, -height/4);
		sev.addEdge(0, -height/2);
		sev.addEdge(-width/2, -height/4);
		sev.addEdge(-width/2, height/4);
		sev.addEdge(0, height/2);
		sev.addEdge(width/4, height/4);
		septagon.add(sev);
		
		GPolygon oct = new GPolygon(width/4, height);
		oct.addEdge(width/2, 0);
		oct.addEdge(width/4, -height/4);
		oct.addEdge(0, -height/2);
		oct.addEdge(-width/4, -height/4);
		oct.addEdge(-width/2, 0);
		oct.addEdge(-width/4, height/4);
		oct.addEdge(0, height/2);
		oct.addEdge(width/4, height/4);
		octagon.add(oct);
	}
}
