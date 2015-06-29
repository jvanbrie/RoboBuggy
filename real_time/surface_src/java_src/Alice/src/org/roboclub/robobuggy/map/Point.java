package org.roboclub.robobuggy.map;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import org.roboclub.robobuggy.linearAlgebra.ANGULAR_UNITS;
import org.roboclub.robobuggy.linearAlgebra.Angle;
import org.roboclub.robobuggy.linearAlgebra.DISTINCE_UNITS;
import org.roboclub.robobuggy.linearAlgebra.Distince;
import org.roboclub.robobuggy.main.LogicException;

import coordinateFrame.FrameOfRefrence;
import coordinateFrame.Pose;

 //GOAL get done by july 1

/***
 * @author Trevor Decker
 * @version 0.0
 * Representation of a point on the map 
 * for now this value will be stored as a 2d(x,y) pair 
 */
public class Point extends MapObject  {
	private FrameOfRefrence coordinateFrame; //where should we draw the point
	private Distince raidus; //How large should we draw the point
	Color backgroundColor;
	Color outlineColor;

	/***
	 * Constructor for point class.  
	 * @param x_ the x corrdinate of the point 
	 * @param y_ the y corrdinate of the point
	 * @param unit what units the corrdinates are in 
	 * @throws LogicException 
	 */
	public Point(Distince x_,Distince y_,Distince raidus) throws LogicException{
		this.raidus = raidus;
		coordinateFrame = new Pose(x_,y_,new Angle(ANGULAR_UNITS.DEGREES, 0.0));
		Distince x = coordinateFrame.getX().toMeters();
		Distince y = coordinateFrame.getY().toMeters();
		int thisScale = 1;//TODO add scale based on the current zoom 
		setBounds((int)x.getMeassurmentValue()-thisScale/2, (int)y.getMeassurmentValue()-thisScale/2, thisScale, thisScale);
	}
	
	/***
	 * A constructor for the point class, assumes that units are meters 
	 *   and that the radius of the point is 1 meter
	 * @param x_
	 * @param y_
	 * @throws LogicException 
	 */
	public Point(Distince x_, Distince y_) throws LogicException {
		this(x_,y_,new Distince(DISTINCE_UNITS.METERS, 1.0));
	}
	
	/**
	 * TODO documnet
	 */
	public FrameOfRefrence getCoordinateFrame(){
		return coordinateFrame;
	}
	
	/**
	 * TODO documnet 
	 */
	public void setCoordinateFrame(FrameOfRefrence newFrame){
		coordinateFrame = newFrame;
	}
	
	
	/***
	 * returns the units that this point uses for its corrdinates 
	 * @return Units
	 */
	public Distince getRadius(){
		return this.raidus;
	}
	
	
	/***
	 * sets the units that the point uses for its corrdiantes 
	 */
	public void setRadius(Distince newRadius)
	{
		this.raidus = newRadius; 
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
		//TODO use current view size 
		super.paintComponent(g);
        g.setColor(Color.GRAY);
        int thisScale = (int)scale;
        g.setColor(backgroundColor);
        g.fillOval(0, 0, thisScale, thisScale); 
        g.setColor(outlineColor);
        g.drawOval(0, 0, thisScale, thisScale);
        
        
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
	 * @throws LogicException 
	 */
	@Override
	boolean isGreater(Object obj) throws LogicException {
		//it does not make sense for objects of different types to be greater then the other
		if(obj.getClass() != this.getClass()){
			throw new LogicException("calling is Greater on an object that is of a non comparable class type");
		}
		Point other = (Point)obj;
		return this.coordinateFrame.isGreater(other.coordinateFrame);

	}

	@Override
	/***
	 * TODO document
	 * @throws LogicException
	 */
	boolean isLess(Object obj) throws LogicException {
		//it does not make sense for objects of different types to be greater then the other
		if(obj.getClass() != this.getClass()){
			throw new LogicException("calling is Less on an object that is of a non comparable class type");
		}
		Point other = (Point)obj;
		return this.coordinateFrame.isLess(other.coordinateFrame);
	
	}


}
