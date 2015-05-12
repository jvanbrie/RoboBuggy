package com.roboclub.robobuggy.map;

import java.util.ArrayList;

/***
 * 
 * @author trevordecker
 *	TODO
 */
public interface Map {


	/***
	 * TODO document
	 * @param aPoint
	 * @return
	 */
	public MapObject getCLosestObject(Point aPoint);
	
	/***
	 * TODO document
	 * @param aPoint
	 * @return
	 */
	public ArrayList<MapObject> getClosestNObjects(Point aPoint);
	
	// @REQUIER num(Points) > 2
	/*** 
	 * TODO document
	 * @param Points
	 * @return
	 */
	public ArrayList<MapObject> getPointsInRange(ArrayList<Point> Points);
	
	//@REQUIER num(Points) >2
	/***
	 * TODO document
	 * @param Points
	 * @return
	 */
	public ArrayList<MapObject> getPointsOutSideRange(ArrayList<Point> Points);
	
	//@REQUIER num(Points) > 2
	/***
	 * TODO documnet
	 * @param Points
	 * @return
	 */
	public ArrayList<MapObject> getPointsOnRange(ArrayList<Point> Points);
	/***
	 * TODO document
	 * @param anObject
	 * @return
	 */
	public boolean isObjectOnMap(MapObject anObject);
	
	//Add Objects 
	/***
	 * TODO document
	 * @param anObject
	 */
	public void AddObject(MapObject anObject);
	/***
	 * TODO document
	 * @param anObject
	 */
	public void removeObject(MapObject anObject);
}
