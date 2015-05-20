package com.roboclub.robobuggy.map;

import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;

/***
 * 
 * @author trevor decker <tdecker@andrew.cmu.edu>
 * computer repersentation of a line (a series of points) 
 * TODO add further documentation 
 */
public class Line extends MapObject {
	ArrayList<Point> points = new ArrayList<Point>();
	final static double ON_LINE_DISTANCE = .1; // meters
	boolean validLine = false;

	// if the line is not valid then all operations are not valid
	// must have at least two points
	/***
	 * TODO document
	 * TODO implement
	 * @param newPoints
	 */
	Line(ArrayList<Point> newPoints) {
		for (int i = 0; i < newPoints.size(); i++) {
			points.add(newPoints.get(i));
		}
		updatedValidLine();
	}

	/***
	 *  constructor for a line object which consists of a single line segment  
	 * 
	 * @param aStart 
	 * @param aEnd
	 */
	public Line(Point aStart, Point aEnd) {
		points = new ArrayList<>();
		points.add(aStart);
		points.add(aEnd);
		updatedValidLine();
	}

	/***
	 * Constructor for an empty line 
	 * NOTE: this will produce a line object but it will not be a valid line 
	 */
	public Line() {
		points = new ArrayList<Point>();
		updatedValidLine();
	}

	/***
	 * TODO document 
	 */
	private void updatedValidLine() {
		if (points.size() >= 2) {
			validLine = true;
		} else {
			validLine = false;
		}
	}

	/***
	 * TODO document
	 * @param newPoint
	 */
	public void addPointToLine(Point newPoint) {
		points.add(newPoint);
		updatedValidLine();
	}

	/***
	 * TODO document
	 * @param i
	 */
	public void removePoint_byIndex(int i) {
		points.remove(i);
		updatedValidLine();
	}

	/***
	 * TODO document
	 * @param pointToRemove
	 */
	public void removeEquivlentPoint(Point pointToRemove) {
		for (int i = 0; i < points.size(); i++) {
			if (points.get(i).equals(pointToRemove)) {
				points.remove(i);
				i--; // decrement point since we removed it
			}
		}
		updatedValidLine();
	}

	/***
	 * returns true if aPoint is on the line, false otherwise
	 * @param aPoint
	 * @return
	 */
	public boolean onLine(Point aPoint) {
		assert (validLine);
		return getDistance(aPoint) < ON_LINE_DISTANCE;
	}

	/***
	 * 	returns true if the two lines intersect, false otherwise
	 */
	public boolean intersect(Line aLine) {
		assert (validLine && aLine.validLine);
		// TODO
		return false;
	}

	// TODO abstract to work in higher Dimensions
	/***
	 *  TODO document 
	 * @param aStart
	 * @param aEnd
	 * @param bStart
	 * @param bEnd
	 * @return
	 */
	private boolean lineSegmentsIntersect(Point aStart, Point aEnd,
			Point bStart, Point bEnd) {
		assert (validLine);
		double aSlope = (aEnd.getY() - aStart.getY())
				/ (aEnd.getX() - aStart.getX());
		double bSlope = (bEnd.getY() - bStart.getY())
				/ (bEnd.getX() - bStart.getX());
		double aOffset = aStart.getY() - aStart.getX() * aSlope;
		double bOffset = bStart.getY() - bStart.getX() * bSlope;

		double xIntersect = (bOffset - aOffset) / (aSlope - bSlope);
		double yIntersect = aSlope * xIntersect + aOffset;
		Line aLine = new Line(aStart, aEnd);
		return aLine.onLine(new Point(xIntersect, yIntersect));
	}

	/***
	 * returns the distance from a point to the line
	 * returns in meters
	 * @param aPoint
	 * @return
	 */
	public double getDistance(Point aPoint) {
		assert (validLine);
		double minD = Double.MAX_VALUE;
		for (int i = 0; i < points.size() - 1; i++) {
			Point v = new Point(
					points.get(i).getY() - points.get(i + 1).getY(), points
							.get(i + 1).getX() - points.get(i).getX());
			Point r = new Point(points.get(i + 1).getX() - aPoint.getX(),
					points.get(i + 1).getY() - aPoint.getY());
			double d = v.dotProduct(r);
			minD = Math.min(minD, d);
		}
		return minD;
	}

	/***
	 * combines line_a and line_b into 1 continuous line
	 * Note will add a line segment between line_a and line_b
	 * @param line_a
	 * @param line_b
	 * @return
	 */
	public Line combineLine(Line line_a, Line line_b) {
		assert (line_a.validLine);
		assert (line_b.validLine);
		Line newLine = new Line();
		for (int i = 0; i < line_a.points.size(); i++) {
			newLine.addPointToLine(line_a.points.get(i));
		}
		for (int i = 0; i < line_b.points.size(); i++) {
			newLine.addPointToLine(line_b.points.get(i));
		}
		return newLine;
	}

	/***
	 * 	returns true if aLine is a subsection of the line
	 * @param thisLine
	 * @return
	 */
	public boolean isSubSection(Line thisLine) {
		assert (validLine && thisLine.validLine);
		for (int i = 0; i < thisLine.points.size(); i++) {
			if (!onLine(thisLine.points.get(i))) {
				return false;
			}
		}
		return true;
	}

	@Override
	/***
	 * TODO document 
	 */
	public boolean equals(Object thisObject) {
		if (!(thisObject instanceof Line)) {
			return false;
		}
		Line thisLine = (Line) thisObject;

		if (!thisLine.isSubSection(this)) {
			return false;
		}

		if (isSubSection(thisLine)) {
			return false;
		}

		// every point on either line is on the other line so they
		// must be equivlent lines
		return true;
	}

	@Override
	boolean Equals(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	boolean isGreater(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	boolean isLess(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}

}
