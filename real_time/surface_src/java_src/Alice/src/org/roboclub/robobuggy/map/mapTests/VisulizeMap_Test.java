package org.roboclub.robobuggy.map.mapTests;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.roboclub.robobuggy.map.Image;
import org.roboclub.robobuggy.map.MapObject;
import org.roboclub.robobuggy.map.Point;
import org.roboclub.robobuggy.map.Polygon;
import org.roboclub.robobuggy.map.VisulizeMap;
import org.roboclub.robobuggy.map.LinkedListMap;

/***
 * 
 * @author Trevor decker
 * A driver for testing and verfying that the map visulizer works correctly 
 */
public class VisulizeMap_Test {

	/***
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
	
		System.out.println("Starting VisulizeMap Tests");
		LinkedListMap thisMap = new LinkedListMap();
		
		//Point aPoint = new Point(20,20);
		/*
		Point bPoint = new Point(20,100);
		Point cPoint = new Point(60,60);
		Point dPoint = new Point(200,200);
		thisMap.AddObject(dPoint);
		ArrayList<Point> v = new ArrayList<Point>();
		v.add(aPoint);
		v.add(bPoint);
		v.add(cPoint);
		Polygon aPolygon = new Polygon(v);
		*/
		Image testImg = new Image();
		thisMap.AddObject(testImg);
		java.util.Date date= new java.util.Date();
		 //System.out.println(new Timestamp(date.getTime()));
		 //thisMap.AddObject(aPolygon);
		 
		 //TODO add test for a moving object 
		 //TODO add test for objects that have frames of refrence 
		 
		 
		//thisMap.AddObject(aPoint);
		//thisMap.AddObject(bPoint);
		//thisMap.AddObject(cPoint);
		/*
		for(int x= 0;x<20;x++){
			for(int y = 0;y<500;y++){
				thisMap.AddObject(new Point(x,y));
		}
		}*/
		
	//	VisulizeMap thisDisplay = new VisulizeMap(thisMap);
	//	java.util.Date date2= new java.util.Date();
		// System.out.println(new Timestamp(date2.getTime()));
		
		System.out.println("finished VisulizeMap Tests");
	}

}
