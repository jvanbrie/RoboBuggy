package com.roboclub.robobuggy.map.mapTests;

import com.roboclub.robobuggy.map.Point;

public class Point_Test {

	public static void main(String[] args) {
		System.out.println("Starting to test Point Class \n ");
		int x_a = 0;
		int y_a = 0;
		int x_b = 1;
		int y_b = 0;
		Point a_point = new Point(x_a,y_a);
		Point b_point = new Point(x_b,y_b);
		
		//verify all points were created correctly 
		assert(x_a == a_point.getX());
		assert(y_a == a_point.getY());
		assert(x_b == b_point.getX());
		assert(y_b == b_point.getY());
		
		//distince between points is being returned correctly 
		assert(1 == a_point.getDistance(b_point));
		assert(a_point.getDistance(b_point) == b_point.getDistance(a_point));

		//distince between any point and itself is 0 
		assert(a_point.getDistance(a_point) == 0);
		assert(b_point.getDistance(b_point) == 0);

		//make sure that the point is drawn correctly TODO 
		
		
		//TODO finsih writing tests 
		
		System.out.println("All tests passed for Point Class \n");
	}

}
