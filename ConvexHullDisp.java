import java.awt.geom.*;
import java.awt.Point;
import java.awt.BasicStroke;
import javax.swing.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.*;

public class ConvexHullDisp extends JPanel{

	private Point[] points;
	private int curp;
	private List<Point> upper;
	private List<Point> lower;
	private Line2D.Double sweepLine;
	private boolean upperDone=false;
	private boolean lowerDone=false;
	
	private static final int radius=5;
	private static final BasicStroke searchStroke=new BasicStroke(1);
	private static final BasicStroke finishStroke=new BasicStroke(5);
	public Color pointColor;
	public Color hullColor;
	public Color sweepLineColor;

	public ConvexHullDisp(Point[] pts){
		points=pts;
		pointColor=Color.black;
		curp=0;
		hullColor=Color.blue;
		upper=new ArrayList<Point>();
		lower=new ArrayList<Point>();
		sweepLine=new Line2D.Double();
		sweepLineColor=Color.red;
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2=(Graphics2D)g;
		g2.clearRect(0, 0, getWidth(), getHeight());
		g2.setColor(sweepLineColor);
		sweepLine.setLine(points[curp].x,0,points[curp].x,1000);
		g2.draw(sweepLine);
		drawUpper(g2);
		drawLower(g2);
		g2.setColor(pointColor);
		for(int i=0;i<points.length;i++){
			g2.drawOval(points[i].x-radius, points[i].y-radius, 2*radius, 2*radius);
			g2.fillOval(points[i].x-radius, points[i].y-radius, 2*radius, 2*radius);
		}
	}
	
	public void drawUpper(Graphics2D g2){
		if(upperDone)
		g2.setStroke(finishStroke);
		else g2.setStroke(searchStroke);
		g2.setColor(hullColor);
		for(int i=0;i<upper.size()-1;i++)
			g2.drawLine(upper.get(i).x,upper.get(i).y, upper.get(i+1).x, upper.get(i+1).y);
	}
	
	public void drawLower(Graphics2D g2){
		if(lowerDone)
		g2.setStroke(finishStroke);
		else g2.setStroke(searchStroke);
		g2.setColor(hullColor);
		for(int i=0;i<lower.size()-1;i++)
			g2.drawLine(lower.get(i).x,lower.get(i).y, lower.get(i+1).x, lower.get(i+1).y);
	}
	
	public List<Point> Compute(){
		List<Point> result=new ArrayList<Point>();
		Arrays.sort(points,new Comparator<Point>(){
			public  int compare(Point p1,Point p2){
				return p1.x-p2.x;
			}
		});
		int i,j,k,size;
		if(points.length<3)
			System.out.println("Less than 3 points, no convex hull");
		else{
			try{
			//wait for 1 sec to show the initial state
			Thread.sleep(1000);
			upper.add(points[0]);
			scanRight();
			upper.add(points[1]);
			repaint();
			Thread.sleep(1000);
			for(k=2;k<points.length;k++){
				scanRight();
				while((size=upper.size())>1&&det(upper.get(size-2),upper.get(size-1),points[k])>=0){
					upper.remove(size-1);
					repaint();
					Thread.sleep(1000);
				}
				upper.add(points[k]);
				repaint();
				Thread.sleep(1000);
			}
			upperDone=true;
			repaint();
			Thread.sleep(1000);
			lower.add(points[points.length-1]);
			lower.add(points[points.length-2]);
			scanLeft();
			for(k=points.length-3;k>=0;k--){
				scanLeft();
				while((size=lower.size())>1&&det(lower.get(size-2),lower.get(size-1),points[k])>=0){
					lower.remove(size-1);
					repaint();
					Thread.sleep(1000);
				}
				lower.add(points[k]);
				repaint();
				Thread.sleep(1000);
			}
			lowerDone=true;
			repaint();
			Thread.sleep(1000);
			lower.remove(0);
			lower.remove(lower.size()-1);
			result.addAll(upper);
			result.addAll(lower);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		return result;
	}
	
	//determinant used to judge whether p1,p2,p3 form a right turn
	public int det(Point p1,Point p2,Point p3){
		return (p2.x-p1.x)*(p3.y-p2.y)-(p3.x-p2.x)*(p2.y-p1.y);
	}
	
	public void scanRight(){
		try{
		curp++;
		repaint();
		Thread.sleep(1000);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
	public void scanLeft(){
		try{
		curp--;
		repaint();
		Thread.sleep(1000);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		Point[] points={new Point(14,8),new Point(0,6),new Point(20,4),new Point(18,10),new Point(22,4),new Point(13,5),new Point(6,8),new Point(6,0),new Point(3,11)};
		for(int i=0;i<points.length;i++){
			points[i].x*=20;
			points[i].x+=50;
			points[i].y*=20;
			points[i].y+=50;
		}
		ConvexHullDisp display=new ConvexHullDisp(points);
		JFrame frame=new JFrame("Convex Hull Demo");
		frame.setSize(600,400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(display);
		frame.setVisible(true);
		List<Point> result=display.Compute();
		for(int i=0;i<result.size();i++)
			System.out.println(result.get(i).toString());
	}
}
