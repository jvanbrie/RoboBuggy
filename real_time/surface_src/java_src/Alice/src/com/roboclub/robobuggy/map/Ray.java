package com.roboclub.robobuggy.map;

/* repersentation of a ray on the map 
 * for now this value will be sotred as a point and a direction */

/***
 * 
 * @author trevordecker
 *
 */
public class Ray implements MapObject {
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

}
