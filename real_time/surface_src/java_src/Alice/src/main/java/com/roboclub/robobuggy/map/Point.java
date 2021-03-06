package com.roboclub.robobuggy.map;

/* repersentation of a point on the map 
 * for now this value will be sotred as a 2d(x,y) pair */

public class Point implements MapObject {
	private double x;
	private double y;

	public Point(double x_, double y_) {
		this.x = x_;
		this.y = y_;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public void setX(float x_) {
		this.x = x_;
	}

	public void setY(float y_) {
		this.y = y_;
	}

	// ues L2 distance
	public double getDistance(Point closestPoint) {
		return Math.sqrt(x * x + y * y);
	}

	public double dotProduct(Point aPoint) {
		return this.x * aPoint.x + this.y * aPoint.y;
	}
}
