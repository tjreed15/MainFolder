import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.awt.geom.*;

import javax.swing.*;

public class FrameTest {

	public static void main(String[] args){
		if (true){
			Frame f = new Frame("Frame Test");
			f.setMinimumSize(new Dimension(500, 500));
			//f.addWindowListener(new WindowAdapter());
			f.setVisible(true);
			f.setCursor(0);//1-13
			Rectangle rect = new Rectangle();
			rect.setSize(10, 10);
			rect.setLocation(10, 10);
			Canvas can = new Canvas();
			f.add(can);
		}
		else{
			FrameTest x = new FrameTest();
			MyFrame test = x.new MyFrame(100, "Test Frame");
			test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			test.setVisible(true);
		}
		
	}
	
	
	class MyFrame extends JFrame{

		private final int size;
		
		public MyFrame(int size, String title){
			this.size = size;
			setTitle(title);
			setSize(2 * size, 2 * size);
			Point2D p1 = new Point2D.Double(0, 0);
			Point2D p2 = new Point2D.Double(size, size);
			MyComponent component = new MyComponent(p1, p2);
			add(component);
		    repaint();
		}
	}
	
	class MyComponent extends JComponent{
		
		public MyComponent(Point2D p1, Point2D p2){
			
		}
		
		public void paintComponent(Graphics2D g2){
			//g2.draw(new Line2D.Double(new Point2D.Double(0, 0), new Point2D.Double(10, 10)));
			repaint();
		}
	}
}
