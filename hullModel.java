import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.*;

// class which represents the underlying data needed during the display of the hull
public class hullModel {

	private Point[] points;
	private int curp;
	private List<Point> upper;
	private List<Point> lower;
	private Line2D.Double sweepLine;
	
	public static enum scanDir{SCANLEFT,SCANRIGHT};
	public scanDir direct;
	public boolean upperDone=false;
	public boolean lowerDone=false;
	
	public hullModel(Point[] pts){
		points=pts;
		Arrays.sort(points,new Comparator<Point>(){
			public  int compare(Point p1,Point p2){
				return p1.x-p2.x;
			}
		});
		curp=0;
		upper=new ArrayList<Point>();
		upper.add(points[0]);
		lower=new ArrayList<Point>();
		sweepLine=new Line2D.Double(points[0].x,0,points[0].x,1000);
		direct=scanDir.SCANRIGHT;
	}
	
	//data accessors and mutators
	public Point[] getPoints(){
		return points;
	}
	
	public int getCurp(){
		return curp;
	}
	
	public void setCurp(int pos){
		curp=pos;
	}
	
	public List<Point> getUpper(){
		return upper;
	}
	
	public void upperAdd(Point p){
		upper.add(p);
	}
	
	public void upperRemove(int ind){
		upper.remove(ind);
	}
	
	public List<Point> getLower(){
		return lower;
	}
	
	public void lowerAdd(Point p){
		lower.add(p);
	}
	
	public void lowerRemove(int ind){
		lower.remove(ind);
	}
	
	public Line2D.Double getSweepLine(){
		sweepLine.setLine(points[curp].x,0,points[curp].x,1000);
		return sweepLine;
	}
	
	public void reset(){
		upper.clear();
		upper.add(points[0]);
		lower.clear();
		curp=0;
		sweepLine.setLine(points[0].x,0,points[0].x,1000);
		direct=scanDir.SCANRIGHT;
		upperDone=false;
		lowerDone=false;
	}
	
}
