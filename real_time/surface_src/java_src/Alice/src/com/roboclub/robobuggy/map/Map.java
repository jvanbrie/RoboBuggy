package com.roboclub.robobuggy.map;

import java.util.ArrayList;

public interface Map {
	// TODO
	
	public void displayMap();
	public MapObject getCLosestObject(Point aPoint);
	public ArrayList<MapObject> getClosestNObjects(Point aPoint);
	
	// @REQUIER num(Points) > 2
	public ArrayList<MapObject> getPointsInRange(ArrayList<Point> Points);
	
	//@REQUIER num(Points) >2
	public ArrayList<MapObject> getPointsOutSideRange(ArrayList<Point> Points);
	
	//@REQUIER num(Points) > 2
	public ArrayList<MapObject> getPointsOnRange(ArrayList<Point> Points);
	public boolean isObjectOnMap(MapObject anObject);
	
	//Add Objects 
	public void AddObject(MapObject anObject);
	public void removeObject(MapObject anObject);
	
}
