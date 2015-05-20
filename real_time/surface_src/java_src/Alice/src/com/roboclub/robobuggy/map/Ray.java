package com.roboclub.robobuggy.map;

import java.awt.Component;
import java.awt.Graphics;

/* repersentation of a ray on the map 
 * for now this value will be sotred as a point and a direction */

/***
 * 
 * @author trevordecker
 *
 */
public class Ray extends MapObject {
    Point startPoint;   //meters
    double angle;       //radians
    
    /***
     * TODO document
     * TODO implement
     */
    public Ray(){
    	
    }
    
	@Override
	/***
	 * TODO document
	 * TODO impelment
	 */
	boolean Equals(Object obj) {
		// TODO Auto-generated method stub
		return false;
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


}
