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

    @Override
	/***
	 * TODO document 
	 */
	public MapObject mergeWith(MapObject thisObject) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	boolean Equals(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	boolean isGreater(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	boolean isLess(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	double getDistince(MapObject obj) {
		// TODO Auto-generated method stub
		return 0;
	}


}
