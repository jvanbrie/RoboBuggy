package org.roboclub.robobuggy.map;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JComponent;

/***
 * 
 * @author Trevor Decker
 *
 */
public abstract class  MapObject extends  JComponent{
	
	
	/***	
	 * TODO document
	 * @param thisObject
	 * @return
	 */
	Point refrenceFrame;
	/**
	 * TODO document
	 */
	abstract boolean Equals(Object obj);
	abstract boolean isGreater(Object obj);
	abstract boolean isLess(Object obj);
	/***
	 * TODO document 
	 * @param obj
	 * @return
	 */
	Order compare(Object obj){
		//if the two objects are of diffrent classes then it does not make sense to compare them 
		if(obj.getClass() != this.getClass()){
			return Order.NA;
		}
		if(Equals(obj)){
			return Order.EQUAL;
		}else if(isGreater(obj)){
			return Order.GREATER;
		}else{
			return Order.LESS;
		}
		
	}
	/**
	 * TODO document
	 * @param obj
	 * @return
	 */
	public double getDistince(Point aPoint) {
		Point thisPoint;
		
		if(this.getClass() == Point.class){
			thisPoint = (Point)this;
		}else{
			thisPoint = this.refrenceFrame;
		}
		double dx = aPoint.getX_corr() - thisPoint.getX_corr();
		double dy = aPoint.getY_corr() - thisPoint.getY_corr();
		return Math.sqrt(dx*dx + dy*dy);
	}

}
