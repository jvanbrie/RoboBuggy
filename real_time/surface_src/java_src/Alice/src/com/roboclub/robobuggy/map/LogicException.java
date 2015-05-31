package com.roboclub.robobuggy.map;

/**
 * TODO document 
 * @author Trevor Decker
 *
 */
public class LogicException extends Exception {
public LogicException(String error){
	System.out.println(error);
	assert(false);
}
}
