package com.roboclub.robobuggy.map;

 

/***
 * Representation of a point on the map 
 * for now this value will be stored as a 2d(x,y) pair 
 * @author trevordecker
 *
 */
public class Point implements MapObject {
	private double x;
	private double y;
	private Units pointUnits;

	/***
	 * TODO document
	 * @param x_
	 * @param y_
	 * @param unit
	 */
	public Point(double x_,double y_,Units unit){
		this.x = x_;
		this.y = y_;
		this.pointUnits = unit;
	}
	
	/***
	 * TODO document 
	 * @param x_
	 * @param y_
	 */
	public Point(double x_, double y_) {
		this.x = x_;
		this.y = y_;
		this.pointUnits = Units.UNITLESS;
	}
	

	/***
	 * TODO document
	 * @return
	 */
	public Units getUnits(){
		return this.pointUnits;
	}
	
	
	/***
	 * TODO document 
	 */
	public void setUnits(Units newUnits)
	{
		this.pointUnits = newUnits; 
	}
	
	//TODO add a conversion function that will convert the point between different types of units for example miles to feet
	


	/***
	 * TODO document
	 * @return
	 */
	public double getX() {
		return this.x;
	}

	/*** 
	 * TODO document
	 * @return
	 */
	public double getY() {
		return this.y;
	}

	/***
	 * TODO document
	 * @param x_
	 */
	public void setX(float x_) {
		this.x = x_;
	}

	/*** 
	 * TODO document
	 * @param y_
	 */
	public void setY(float y_) {
		this.y = y_;
	}

	// ues L2 distance
	/*** 
	 * TODO document
	 * @param closestPoint
	 * @return
	 */
	public double getDistance(Point closestPoint) {
		return Math.sqrt(x * x + y * y);
	}

	//TODO 
	/*** 
	 * TODO document
	 * @param aPoint
	 * @return
	 */
	public double dotProduct(Point aPoint) {
		return this.x * aPoint.x + this.y * aPoint.y;
	}

	@Override
	/*** 
	 * TODO document 
	 */
	public MapObject mergeWith(MapObject thisObject) {
		// TODO Auto-generated method stub
		return null;
	}
}
