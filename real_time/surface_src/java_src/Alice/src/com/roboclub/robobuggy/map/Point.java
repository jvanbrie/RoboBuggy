package com.roboclub.robobuggy.map;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

 

/***
 * Representation of a point on the map 
 * for now this value will be stored as a 2d(x,y) pair 
 * @author trevordecker
 *
 */
public class Point extends MapObject  {
	private double x;
	private double y;
	private Units pointUnits;

	/***
	 * TODO document
	 * @param x_
	 * @param y_
	 * @param unit
	 */
	public Point(double x_,double y_,Units unit){
		this.pointUnits = unit;
		x = x_;
		y = y_;
		setBounds((int)x_, (int)y_, 20, 20);//TODO make dynamic
	}
	
	/***
	 * TODO document
	 * @return
	 */
	public double getX_corr(){
		return x;
	}
	/***
	 * TODO document
	 * @return
	 */
	public double getY_corr(){
		return y;
	}
	/***
	 * TODO document 
	 * @param x
	 */
	public void setX_corr(double x){
		this.x = x;
	}
	/***
	 * TODO document 
	 * @param y
	 */
	public void setY_corr(double y){
		this.y = y;
	}
	
	
	/***
	 * TODO document 
	 * @param x_
	 * @param y_
	 */
	public Point(double x_, double y_) {
		this(x_,y_,Units.UNITLESS);
	}
	

	/***
	 * TODO document
	 * @return
	 */
	public Units getUnits(){
		return this.pointUnits;
	}
	
	
	/***
	 * TODO document 
	 */
	public void setUnits(Units newUnits)
	{
		this.pointUnits = newUnits; 
	}
	
	//TODO add a conversion function that will convert the point between different types of units for example miles to feet

	



	/* TODO figureout why this is nesassery */
	public void setX(float x){
		//TODO
	}

	//TODO 
	/*** 
	 * TODO document
	 * @param aPoint
	 * @return
	 */
	public double dotProduct(Point aPoint) {
		return this.x * aPoint.x + this.y * aPoint.y;
	}

	@Override
	/*** 
	 * TODO document 
	 */
	public MapObject mergeWith(MapObject thisObject) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	/**
	 * TODO documnet
	 * TODO implment
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
        g.setColor(Color.GRAY);
        g.fillOval(0, 0, 20, 20);
	}
	
	@Override
	/**
	 * TODO documnet
	 * TODO implment
	 */
public Dimension	getPreferredSize(){
		Dimension thisDimension = new Dimension();
		thisDimension.setSize(20, 20);//TODO make dynamic
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
