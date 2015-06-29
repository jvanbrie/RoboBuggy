package org.roboclub.robobuggy.map;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
/***
 * 
 * @author Trevor Decker
 * @version 0.0
 * 
 * an implemention of a map that uses a linked list store the map nodes 
 */
//TODO change to a sorted linked list 
public class LinkedListMap extends Map{
	LinkedList<MapObject> map;
	
	/***
	 * TODO documnet
	 */
	 public LinkedListMap() {
		 map = new LinkedList<MapObject>();
	 }
	
	@Override
	/***
	 * evaluates to the number of elements currently on the map 
	 */
	public int getNumberOfElements(){
		return map.size();
	}

	
	@Override
	/***
	 * TODO document
	 *   if no closestObject exists ie the map is empty return null
	 */
	public MapObject getClosestObject(Point aPoint) {
		if(map.size() <1){
			return null;
		}
		
		int best_index = 0;
		double best_distince = map.get(0).getDistince(aPoint);
		for(int i = 1;i<map.size();i++){
			double this_distince = map.get(i).getDistince(aPoint);
			if(this_distince < best_distince){
				best_index = i;
				best_distince = this_distince;
			}
		}
		return map.get(best_index);
	}

	@Override
	/***
	 * TODO document
	 * TODO implement
	 */
	public MapObject[] getClosestNObjects(Point aPoint,int numPoints) {
		//a sorted array of mapObjects (sorted based on distance from aPoint)
		MapObject[] sortedObjects = new MapObject[numPoints];
		//sets all of the points to null incase not enogth objects exsits on the map we do not want 0 objects to be returned
		for(int i = 0;i<numPoints;i++){
			sortedObjects[i] = null;
		}
		//TODO make a binary search 
		for(int i = 0;i<map.size();i++)
		{
			//add this element to the sortedObjects list (if it is within range)
			int offset = 0;
			double mapObjectDistince = map.get(i).getDistince(aPoint); 
			while(offset < numPoints && sortedObjects[offset] != null && sortedObjects[offset].getDistince(aPoint) < mapObjectDistince)
			{
				offset++;
			}
			//we have reached the point in the list where the new element should be added 
			//shift all points after this one 
			for(int j = numPoints-1;j> offset;j--)
			{
				sortedObjects[j] = sortedObjects[j-1];
			}
			//add this point 
			if(offset < numPoints){
			sortedObjects[offset] = map.get(i);
			}
			
		}
		return sortedObjects;
	}

	@Override
	/***
	 * TODO document
	 * TODO implement
	 */
	public ArrayList<MapObject> getPointsInRange(ArrayList<Point> Points) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	/***
	 * TODO document
	 * TODO implement
	 */
	public ArrayList<MapObject> getPointsOutSideRange(ArrayList<Point> Points) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	/***
	 * TODO document
	 * TODO implement
	 */
	public ArrayList<MapObject> getPointsOnRange(ArrayList<Point> Points) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	/***
	 * TODO document
	 */
	public boolean isObjectOnMap(MapObject anObject) {
		for(int i = 0;i<map.size();i++){
			if(anObject.Equals(map.get(i))){
				return true;
			}
		}		
		return false;
	}

	@Override
	/***
	 * TODO document
	 */
	public void AddObject(MapObject anObject) {
		map.add(anObject);		
	}

	@Override
	/***
	 * TODO document
	 */
	public void removeObject(MapObject anObject) {		
		for(int i = 0;i< map.size();i++){
			if(map.get(i).equals(anObject)){
				map.remove(i);
			}
			i--; //To make up for the fact that we removed an object 
		}		
	}

	@Override
	/***
	 * TODO implment
	 * TODO documnet
	 */
	public void loadMap(File mapData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	/***
	 * TODO implment
	 * TODO documnet 
	 */
	public void saveMap(File mapData) {
		// TODO Auto-generated method stub
		
	}

}