package com.roboclub.robobuggy.map;

import java.io.File;
import java.util.ArrayList;

import com.roboclub.robobuggy.localization.FrameOfRefrence;

/***
 * 
 * @author Trevor Decker
 *	TODO
 */
public abstract class Map {

	/***
	 * evaluates to the number of elements currently on the map 
	 * 
	 */
	public abstract int getNumberOfElements();
	
	/**
	 * TODO document 
	 * @return
	 */
	FrameOfRefrence refrenceFrame;
	
	/***
	 * TODO document
	 * @param aPoint
	 * @return
	 */
	public abstract MapObject getClosestObject(Point aPoint);
	
	/***
	 * TODO document
	 * @param aPoint
	 * @return
	 */
	public abstract MapObject[] getClosestNObjects(Point aPoint,int numPoints);
	
	// @REQUIER num(Points) > 2
	/*** 
	 * TODO document
	 * @param Points
	 * @return
	 */
	public abstract ArrayList<MapObject> getPointsInRange(ArrayList<Point> Points);
	
	//@REQUIER num(Points) >2
	/***
	 * TODO document
	 * @param Points
	 * @return
	 */
	public abstract ArrayList<MapObject> getPointsOutSideRange(ArrayList<Point> Points);
	
	//@REQUIER num(Points) > 2
	/***
	 * TODO documnet
	 * @param Points
	 * @return
	 */
	public abstract ArrayList<MapObject> getPointsOnRange(ArrayList<Point> Points);
	/***
	 * TODO document
	 * @param anObject
	 * @return
	 */
	public abstract boolean isObjectOnMap(MapObject anObject);
	
	//Add Objects 
	/***
	 * TODO document
	 * @param anObject
	 */
	public abstract void AddObject(MapObject anObject);
	/***
	 * TODO document
	 * @param anObject
	 */
	public abstract void removeObject(MapObject anObject);
	
	/***
	 * TODO document 
	 * @param mapData
	 */
	public abstract void loadMap(File mapData);
	
	/***
	 * TODO document
	 * @param mapData
	 */
	public abstract void saveMap(File mapData);
	
	
}
