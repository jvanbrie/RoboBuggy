package com.roboclub.robobuggy.map.mapTests;

import com.roboclub.robobuggy.map.Point;
import com.roboclub.robobuggy.map.VisulizeMap;
import com.roboclub.robobuggy.map.LinkedListMap;

/***
 * 
 * @author trevordecker
 * A driver for testing and verfying that the map visulizer works correctly 
 */
public class VisulizeMap_Test {

	/***
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Starting VisulizeMap Tests");
		LinkedListMap thisMap = new LinkedListMap();
		Point aPoint = new Point(20,20);
		Point bPoint = new Point(100,100);
		Point cPoint = new Point(60,60);
		
		thisMap.AddObject(aPoint);
		thisMap.AddObject(bPoint);
		thisMap.AddObject(cPoint);
		VisulizeMap thisDisplay = new VisulizeMap(thisMap);
		
		
		System.out.println("finished VisulizeMap Tests");
	}

}
