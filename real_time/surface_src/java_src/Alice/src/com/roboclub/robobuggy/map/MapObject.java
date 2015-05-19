package com.roboclub.robobuggy.map;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JComponent;

/***
 * 
 * @author trevordecker
 *
 */
abstract class  MapObject extends  JComponent{
	
	
	/***	
	 * TODO document
	 * @param thisObject
	 * @return
	 */
	Point refrenceFrame;
	/**
	 * TODO document
	 */
	abstract MapObject mergeWith(MapObject thisObject);
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
	double getDistince(MapObject obj) {
		Point otherPoint;
		Point thisPoint;
		
		if(obj.getClass() == Point.class){
			otherPoint = (Point)obj;
		}else{
			otherPoint = obj.refrenceFrame;
		}
		
		if(this.getClass() == Point.class){
			thisPoint = (Point)this;
		}else{
			thisPoint = this.refrenceFrame;
		}
		double dx = otherPoint.getX_corr() - thisPoint.getX_corr();
		double dy = otherPoint.getY_corr() - thisPoint.getY_corr();
		return Math.sqrt(dx*dx + dy*dy);
	}

}
