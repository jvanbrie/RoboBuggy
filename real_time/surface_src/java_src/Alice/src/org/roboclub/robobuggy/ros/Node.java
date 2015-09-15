package org.roboclub.robobuggy.ros;

/**
 * TODO document 
 * @author Matt Sebek
 * @version 0.0
 *
 */
public interface Node {
	String name = "";
	boolean shutdown();
	
	default String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}
	
}