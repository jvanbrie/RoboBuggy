package org.roboclub.robobuggy.map;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EmptyStackException;

/**
 * TODO
 * @author Trevor Decker
 *
 */
public class Polygon extends MapObject {
	ArrayList<Point> vertices; //should be sorted
	Color backgroundColor;
	Color outlineColor;
	
	/***
	 * TODO document
	 * @param vertices
	 */
	public Polygon(ArrayList<Point> vertices){
		this.vertices = sortVertices(vertices); 
		this.refrenceFrame = vertices.get(0); //TODO change to average point
		setBounds(0,0,100,100); //TODO make dynamic 
		backgroundColor = Color.BLACK;
		outlineColor = Color.RED;
	}
	
	/***
	 * sorts the input vertices so that it is easier to work with output 
	 * this will allow equal comparison to happen in linear time, intersection log time
	 * @param input
	 * @return
	 */
	private ArrayList<Point> sortVertices(ArrayList<Point> input){
		//sort has side effects and returns void
		input.sort(new Comparator<Point>(){
		    public int compare(Point p1, Point p2) {
		    	if(p1.isLess(p2)){
		    		return -1;
		    	}else if(p1.Equals(p2)){
		    		return 0;
		    	}else if(p1.isGreater(p2)){
		    		return 1;
		    	}else{
		    		try {
						throw new LogicException("p1 is not less,equal or greater then p2");
					} catch (LogicException e) {
						e.printStackTrace();
					}
		    	}
				return -2;
		    }
		});
		return input;
	}

	@Override
	/***
	 * TODO document
	 */
	boolean Equals(Object obj) {
		if(obj.getClass() != this.getClass()){
			return false;
		}
		
		Polygon aPolygon = (Polygon)obj;
		if(aPolygon.vertices.size() != this.vertices.size()){
			return false;
		}
		
		//vertices are already sorted so they can be compared one to one 
		for(int i = 0;i<this.vertices.size();i++){
			if(!aPolygon.vertices.get(i).Equals(this)){
				return false;
			}
		}
		//has all of the same points so the same polygon is being described 
		return true;		
	}

	@Override
	/***
	 * TODO document
	 * TODO implement
	 */
	boolean isGreater(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	/***
	 * TODO document
	 * TODO implement
	 */
	boolean isLess(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	/***
	 * TODO document
	 * TODO implement
	 */
	public double getDistince(Point aPoint) {
		// TODO Auto-generated method stub
		if(isInside(aPoint)){
			return 0;
		}
		//TODO finish and calculate  the actual distince (I think it will invovle calulating the distince to each line segment and taking the min 
		return -1;
	}
	
	/**
	 * TODO make cleaner, readable and understandable 
	 * returns true if the Point is inside of the polygon 
	 * @param obj
	 * @return
	 */
	public boolean isInside(Point aPoint){
		//based on algorithm found at http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
		boolean inside = false;
		int i = 0;
		for(int j = vertices.size()-1;i<vertices.size();j=i++){
			Point iVertex = vertices.get(i);
			Point jVertex = vertices.get(j);
			if( ((iVertex.getY_corr() > aPoint.getY_corr()) != (jVertex.getY_corr() > aPoint.getY_corr())) && 
					(aPoint.getX_corr() <(jVertex.getX_corr() - iVertex.getX_corr())*(aPoint.getY_corr() - iVertex.getY_corr())/(jVertex.getY_corr()-iVertex.getY_corr()) + iVertex.getX_corr()) )
			{
				inside = !inside;
		}
		}
		return inside;
	}
	
	
	@Override
	/**
	 * drawing function for the point, will draw the object on the
	 *  jcomponent that the point is added to
	 */
	public void paintComponent(Graphics g){
		System.out.println("painting polygon");
		int nPoints = vertices.size();
		int[] xPoints = new int[nPoints];
		int [] yPoints = new int[nPoints];
		for(int i = 0;i<nPoints;i++){
			xPoints[i] = (int)vertices.get(i).getX_corr();
			yPoints[i] = (int)vertices.get(i).getY_corr();
		}
		super.paintComponent(g);
        g.setColor(backgroundColor);
        g.fillPolygon(xPoints, yPoints, nPoints); 
        g.setColor(outlineColor);
        g.drawPolygon(xPoints,yPoints,nPoints);
	}
	
	


}
