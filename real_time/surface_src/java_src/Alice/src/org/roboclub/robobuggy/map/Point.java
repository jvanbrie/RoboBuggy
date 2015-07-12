package org.roboclub.robobuggy.map;

import java.awt.Color;
import java.awt.Graphics;
import org.roboclub.robobuggy.linearAlgebra.DISTINCE_UNITS;
import org.roboclub.robobuggy.linearAlgebra.Distince;
import org.roboclub.robobuggy.linearAlgebra.Double_Number;
import org.roboclub.robobuggy.linearAlgebra.Number;
import org.roboclub.robobuggy.linearAlgebra.Vector;
import org.roboclub.robobuggy.main.LogicException;

import coordinateFrame.FrameOfRefrence;


/***
 * @author Trevor Decker
 * @version 0.0
 * Representation of a point on the map 
 */


public class Point extends MapObject{
	Color backgroundColor;
	Color outlineColor;
	private Distince radius = new Distince(DISTINCE_UNITS.METERS, new Double_Number(1.0)); // assuming the same in all directions
	
	/**
	 * TODO document
	 */
	Point(FrameOfRefrence orgin){
		this.refrenceFrame = orgin.toPose();
	}
	
	@Override
	/**
	 * Compares the object with another object to see if they are equivalent
	 * if the two objects are deemed to be equivalent by having the same x,y
	 *  coordinates and units then true is returned, otherwise false is returned 
	 */
	boolean Equals(Object obj) {
		if(this.getClass() != obj){
			return false;
		}
		//TODO
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
		return this.refrenceFrame.isGreater(other.refrenceFrame);

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
		return this.refrenceFrame.isLess(other.refrenceFrame);
	
	}
	
	//the x corrdinate of where this point should be drawn
	public int getX(){
		return 0;
		//TODO
	}
	
	
	//the y coordinate of where this point should be drawn
	public int getY(){
		return 0;
		//TODO
	}
	
	@Override
	/**
	 * drawing function for the Point, will draw the object on the
	 *  jcomponent that the Point is added to
	 *  TODO implement 
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Vector x_unit = this.view.getXvector(); //the x vector of the image plane
		Vector y_unit = this.view.getYvector();//the y vector of the image plane
		
		//TODO make sure that point is in the same order as x_unit and y_unit
		
		//project the point to image plane
		Number x = (Number)Double_Number.zero();
		for(int i = 0;i<x_unit.getLength();i++){
			try {
				x = x.add(this.refrenceFrame.getPosition().getDimensionIndex(i).mult(x_unit.getIndex(i)));
			} catch (LogicException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Number y = Double_Number.zero();
		for(int i = 0;i<x_unit.getLength();i++){
			try {
				y = y.add(this.refrenceFrame.getPosition().getDimensionIndex(i).mult(y_unit.getIndex(i)));
			} catch (LogicException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		int xint;
		try {
			xint = x.toInteger_Number().getValue();
			int yint = y.toInteger_Number().getValue();
	        g.setColor(backgroundColor);
			g.drawOval(xint, yint, this.radius.toInteger_Number().getValue(), this.radius.toInteger_Number().getValue());
	        g.setColor(outlineColor);
			g.fillOval(xint, yint, this.radius.toInteger_Number().getValue(), this.radius.toInteger_Number().getValue());

		} catch (LogicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	
	/**
	 * TODO documnet
	 */
	public FrameOfRefrence getCoordinateFrame(){
		return this.refrenceFrame;
	}
	
	/**
	 * TODO documnet 
	 */
	public void setCoordinateFrame(FrameOfRefrence newFrame){
		refrenceFrame = newFrame.toPose();
	}
	
	
	/***
	 * returns the units that this point uses for its corrdinates 
	 * @return Units
	 */
	public Distince getRadius(){
		return this.radius;
	}
	
	
	/***
	 * sets the units that the point uses for its corrdiantes 
	 */
	public void setRadius(Distince newRadius)
	{
		this.radius = newRadius; 
	}

}
