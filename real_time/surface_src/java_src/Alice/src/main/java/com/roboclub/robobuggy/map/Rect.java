package com.roboclub.robobuggy.map;

/**
 * @author Trevor Decker
 * 
 * @deprecated use polygon
 * 
 * @version 0.5
 * 
 *          CHANGELOG: NONE
 * 
 *          DESCRIPTION: TODO
 */

public class Rect implements MapObject {
	private Point uR;
	private Point uL;
	private Point lL;

	public Rect(Point uR_, Point uL_, Point lL_) {
		this.uR = uR_;
		this.uL = uL_;
		this.lL = lL_;
	}

	public boolean within(Point marker) {
		if (marker != null) {
			return (marker.getX() >= this.uL.getX())
					&& (marker.getX() <= this.uR.getX())
					&& (marker.getY() >= this.lL.getY())
					&& (marker.getY() <= this.uL.getY());
		}

		return false;
	}
}
