import java.awt.event.*;
import java.awt.*;
import java.util.*;

public class hullControl implements ActionListener{

	private hullGUI gui;
	private hullModel model;
	private int scanDir=1;//scan direction ,1 means scan right while 0 means scan left
	
	public hullControl(Point[] pts){
		model=new hullModel(pts);
		gui=new hullGUI(model,this);
	}
	
	public void actionPerformed(ActionEvent action){
		if(action.getSource()==gui.run){
			gui.run.setEnabled(false);
			gui.step.setEnabled(false);
			//gui.reset.setEnabled(false);
			run();
		}else if(action.getSource()==gui.step){
			gui.step.setEnabled(false);
			gui.run.setEnabled(false);
			//gui.reset.setEnabled(false);
			step();
		}else {//action.getSource()==gui.reset
			model.reset();
			gui.repaint();
			gui.step.setEnabled(true);
			gui.run.setEnabled(true);
			gui.reset.setEnabled(false);
		}
	}
	
	private void step(){
		Worker1 worker=new Worker1();
		worker.start();
	}
	
	private void run(){
		Worker2 worker2=new Worker2();
		worker2.start();
	}
	
	private class Worker1 extends Thread{
		public void run(){
		doWork();
	}
		protected void doWork(){
			int k,size;
			ArrayList<Point> upper,lower;
			Point[] pts=model.getPoints();
			upper=(ArrayList<Point>)model.getUpper();
			lower=(ArrayList<Point>)model.getLower();
			try{
			if(model.direct==hullModel.scanDir.SCANRIGHT){
				k=model.getCurp()+1;
				scanRight();
				while((size=upper.size())>1&&det(upper.get(size-2),upper.get(size-1),pts[k])>=0){
					upper.remove(size-1);
					EventQueue.invokeLater(new Runnable(){
						public void run(){
							gui.repaint();
						}
					});
					Thread.sleep(1000);
				}
				upper.add(pts[k]);
				EventQueue.invokeLater(new Runnable(){
					public void run(){
						gui.repaint();
					}
				});
				Thread.sleep(1000);	
				if(k==model.getPoints().length-1){
					model.direct=hullModel.scanDir.SCANLEFT;
					model.upperDone=true;
					EventQueue.invokeLater(new Runnable(){
						public void run(){
							gui.repaint();
						}
					});
					Thread.sleep(1000);
				}
				EventQueue.invokeLater(new Runnable(){
					public void run(){
					gui.step.setEnabled(true);
					}
				});
			}else if(model.getCurp()>0){
				if(model.getCurp()==pts.length-1)
					lower.add(pts[pts.length-1]);
				k=model.getCurp()-1;
				scanLeft();
				while((size=lower.size())>1&&det(lower.get(size-2),lower.get(size-1),pts[k])>=0){
					lower.remove(size-1);
					EventQueue.invokeLater(new Runnable(){
						public void run(){
							gui.repaint();
						}
					});
					Thread.sleep(1000);
				}
				lower.add(pts[k]);
				EventQueue.invokeLater(new Runnable(){
					public void run(){
						gui.repaint();
					}
				});
				Thread.sleep(1000);
				EventQueue.invokeLater(new Runnable(){
					public void run(){
					gui.step.setEnabled(true);
					}
				});
				if(model.getCurp()==0){
					model.lowerDone=true;
					EventQueue.invokeLater(new Runnable(){
						public void run(){
							gui.repaint();
							gui.step.setEnabled(false);
							gui.reset.setEnabled(true);
						}
					});
					Thread.sleep(1000);
				}
			}}catch(InterruptedException e){
				e.printStackTrace();
			}
			
		}
		private void scanRight(){
			try{
			int curp;
			if((curp=model.getCurp())<model.getPoints().length-1){
			model.setCurp(curp+1);
			EventQueue.invokeLater(new Runnable(){
				public void run(){
					gui.repaint();
				}
			});
			Thread.sleep(1000);
			}
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
		private void scanLeft(){
			try{
				int curp;
				if((curp=model.getCurp())>0){
				model.setCurp(curp-1);
				EventQueue.invokeLater(new Runnable(){
					public void run(){
						gui.repaint();
					}
				});
				Thread.sleep(1000);
				}
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
		//determinant used to judge whether p1,p2,p3 form a right turn
		public int det(Point p1,Point p2,Point p3){
			return (p2.x-p1.x)*(p3.y-p2.y)-(p3.x-p2.x)*(p2.y-p1.y);
		}
		
	}
	
	private class Worker2 extends Worker1{
		public void run(){
			int stepNum=(model.getPoints().length-1)*2;
			for(int i=0;i<stepNum;i++){
				doWork();
			}
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
		new hullControl(points);
	}
}
