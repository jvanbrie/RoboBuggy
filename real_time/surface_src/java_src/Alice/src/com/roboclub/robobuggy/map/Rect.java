package com.roboclub.robobuggy.map;

import java.awt.Component;
import java.awt.Graphics;

/***
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
public class Rect extends MapObject {
	private Point uR;
	private Point uL;
	private Point lL;

	/***
	 * TODO document
	 * @param uR_
	 * @param uL_
	 * @param lL_
	 */
	public Rect(Point uR_, Point uL_, Point lL_) {
		this.uR = uR_;
		this.uL = uL_;
		this.lL = lL_;
	}

	/*** 
	 * TODO document
	 * @param marker
	 * @return
	 */
	public boolean within(Point marker) {
		if (marker != null) {
			return (marker.getX() >= this.uL.getX())
					&& (marker.getX() <= this.uR.getX())
					&& (marker.getY() >= this.lL.getY())
					&& (marker.getY() <= this.uL.getY());
		}

		return false;
	}

	@Override
	/***
	 * TODO document
	 */
	public MapObject mergeWith(MapObject thisObject) {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	double getDistince(MapObject obj) {
		// TODO Auto-generated method stub
		return 0;
	}
}
