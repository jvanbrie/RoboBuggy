package com.roboclub.robobuggy.map;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

//TODO clean up point
//TODO add color 
//TODO clean up linkedList map
//TODO clean up visulizeMap
//TODO finish writeing test cases
//TODO fix travis ci
//TODO implment all of the other mapObject types 
//TODO merge into master and show the rest of the team
 //GOAL get done by july 1

/***
 * Representation of a point on the map 
 * for now this value will be stored as a 2d(x,y) pair 
 * @author trevordecker
 *
 */
public class Point extends MapObject  {
	private double x;
	private double y;
	private double scale; //the size to draw the point
	private Units pointUnits; 

	/***
	 * Constructor for point class.  
	 * @param x_ the x corrdinate of the point 
	 * @param y_ the y corrdinate of the point
	 * @param unit what units the corrdinates are in 
	 */
	public Point(double x_,double y_,Units unit){
		this.pointUnits = unit;
		x = x_;
		y = y_;
		scale = 20; //TODO make dynamic
		int thisScale = (int)scale;
		setBounds((int)x_-thisScale/2, (int)y_-thisScale/2, thisScale, thisScale);
	}
	
	/***
	 * A constructor for the point class, assumes that units are meters
	 * @param x_
	 * @param y_
	 */
	public Point(double x_, double y_) {
		this(x_,y_,Units.METERS);
	}
	
	/***
	 * @return
	 */
	public double getX_corr(){
		return x;
	}
	/***
	 * @return
	 */
	public double getY_corr(){
		return y;
	}
	/***
	 * @param x
	 */
	public void setX_corr(double x){
		this.x = x;
	}
	/***
	 * @param y
	 */
	public void setY_corr(double y){
		this.y = y;
	}
	
	/***
	 * returns the units that this point uses for its corrdinates 
	 * @return Units
	 */
	public Units getUnits(){
		return this.pointUnits;
	}
	
	
	/***
	 * sets the units that the point uses for its corrdiantes 
	 */
	public void setUnits(Units newUnits)
	{
		this.pointUnits = newUnits; 
	}
	
	//TODO add a conversion function that will convert the point between different types of units for example miles to feet

	



	/*** 
	 * evaluates to the cross product of this point and the point passed in (aPoint)
	 * @param aPoint
	 * @return
	 */
	public double dotProduct(Point aPoint) {
		return this.x * aPoint.x + this.y * aPoint.y;
	}


	@Override
	/**
	 * drawing function for the point, will draw the object on the
	 *  jcomponent that the point is added to
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
        g.setColor(Color.GRAY);
        int thisScale = (int)scale;
        g.fillOval(0, 0, thisScale, thisScale); 
	}
	
	@Override
	/**
	 * TODO documnet
	 */
public Dimension	getPreferredSize(){
		Dimension thisDimension = new Dimension();
		int thisScale = (int)scale;
		thisDimension.setSize(thisScale, thisScale);
	return thisDimension;
		
	}

	@Override
	/**
	 * Compares the object with another object to see if they are equivalent
	 * if the two objects are deemed to be equivalent by having the same x,y
	 *  coordinates and units then true is returned, otherwise false is returned 
	 */
	boolean Equals(Object obj) {
		if(obj.getClass() != this.getClass()){
			return false;
		}
		Point other = (Point)obj;
		if(this.x != other.x){
			return false;
		}
		
		if(this.y != other.y){
			return false;
		}
		
		if(this.pointUnits != other.pointUnits){
			return false;
		}
		
		return true;
	}

	/***
	 * TODO document 
	 */
	@Override
	boolean isGreater(Object obj) {
		//it does not make sense for objects of diffrent types to be greater then the other
		//TODO throw an error 
		//TODO think about units
		if(obj.getClass() != this.getClass()){
			return false;
		}
		Point other = (Point)obj;
		if(this.x > other.x){
			return true;
		}else if(this.y > other.y){
			return true;
		}else{
			return false;
		}		
	}

	@Override
	/***
	 * TODO document
	 */
	boolean isLess(Object obj) {
		//it does not make sense for objects of different types to be greater then the other
		//TODO throw an error 
		//TODO think about units
		if(obj.getClass() != this.getClass()){
			return false;
		}
		Point other = (Point)obj;
		if(this.x < other.x){
			return true;
		}else if(this.y < other.y){
			return true;
		}else{
			return false;
		}		
	}


}
