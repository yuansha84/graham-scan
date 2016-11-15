import java.awt.*;
import javax.swing.*;
import java.util.*;

public class hullGUI extends JFrame{
	private static final int radius=5;
	private static final BasicStroke searchStroke=new BasicStroke(1);
	private static final BasicStroke finishStroke=new BasicStroke(5);
	private static final Color pointColor=Color.black;
	private static final Color hullColor=Color.blue;
	private static final Color sweepLineColor=Color.red;

	JButton run;
	JButton step;
	JButton reset;
	JPanel controlPanel;
	hullModel model;
	hullDisp hull;
	hullControl control;
	
	public hullGUI(hullModel mod,hullControl ctl){
		// set up the GUI
		super("Convex Hull Presentation");
		setLayout(new BorderLayout());
		model=mod;
		control=ctl;
		controlPanel=new JPanel();
		controlPanel.setLayout(new FlowLayout());
		run=new JButton("run");
		run.addActionListener(control);
		step=new JButton("step");
		step.addActionListener(control);
		reset=new JButton("reset");
		reset.addActionListener(control);
		controlPanel.add(run);
		controlPanel.add(step);
		controlPanel.add(reset);
		reset.setEnabled(false);
		add(controlPanel,BorderLayout.SOUTH);
		hull=new hullDisp();
		add(hull,BorderLayout.CENTER);
		setSize(600,400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void repaint(){
		hull.repaint();
	}
	
	//the inner class which shows the change of the hull
	private class hullDisp extends JPanel{
		
		public void paintComponent(Graphics g){
			Graphics2D g2=(Graphics2D)g;
			g2.clearRect(0, 0, getWidth(), getHeight());
			g2.setColor(sweepLineColor);
			g2.draw(model.getSweepLine());
			drawUpper(g2);
			drawLower(g2);
			drawPoints(g2);
		}
		
		private void drawUpper(Graphics2D g2){
			if(model.upperDone)
				g2.setStroke(finishStroke);
				else g2.setStroke(searchStroke);
				g2.setColor(hullColor);
				ArrayList<Point> upper=(ArrayList<Point>)model.getUpper();
				for(int i=0;i<upper.size()-1;i++)
					g2.drawLine(upper.get(i).x,upper.get(i).y, upper.get(i+1).x, upper.get(i+1).y);
			}
			
			private void drawLower(Graphics2D g2){
				if(model.lowerDone)
				g2.setStroke(finishStroke);
				else g2.setStroke(searchStroke);
				g2.setColor(hullColor);
				ArrayList<Point> lower=(ArrayList<Point>)model.getLower();
				for(int i=0;i<lower.size()-1;i++)
					g2.drawLine(lower.get(i).x,lower.get(i).y, lower.get(i+1).x, lower.get(i+1).y);
			}
			
			private void drawPoints(Graphics2D g2){
				g2.setColor(pointColor);
				Point[] points=model.getPoints();
				for(int i=0;i<points.length;i++){
					g2.drawOval(points[i].x-radius, points[i].y-radius, 2*radius, 2*radius);
					g2.fillOval(points[i].x-radius, points[i].y-radius, 2*radius, 2*radius);
				}
			}
	}
	
}
