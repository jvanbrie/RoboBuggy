package com.roboclub.robobuggy.map;

import java.io.File;
import java.util.ArrayList;

/***
 * 
 * @author Trevor Decker
 *	TODO
 */
public interface Map {

	/***
	 * evaluates to the number of elements currently on the map 
	 * 
	 */
	public int getNumberOfElements();
	
	/***
	 * TODO document
	 * @param aPoint
	 * @return
	 */
	public MapObject getClosestObject(Point aPoint);
	
	/***
	 * TODO document
	 * @param aPoint
	 * @return
	 */
	public MapObject[] getClosestNObjects(Point aPoint,int numPoints);
	
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
	
	/***
	 * TODO document 
	 * @param mapData
	 */
	public void loadMap(File mapData);
	
	/***
	 * TODO document
	 * @param mapData
	 */
	public void saveMap(File mapData);
	
	
}
