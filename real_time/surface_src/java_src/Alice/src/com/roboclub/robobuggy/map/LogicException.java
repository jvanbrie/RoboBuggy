package com.roboclub.robobuggy.map;

public class LogicException extends Exception {
public LogicException(String error){
	System.out.println(error);
	assert(false);
}
}
