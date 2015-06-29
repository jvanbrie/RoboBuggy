package org.roboclub.robobuggy.main;

/**
 * TODO document 
 * @author Trevor Decker
 * @version 0.0
 *
 */
public class LogicException extends Exception {
public LogicException(String error){
	System.out.println(error);
	assert(false);
}
}
