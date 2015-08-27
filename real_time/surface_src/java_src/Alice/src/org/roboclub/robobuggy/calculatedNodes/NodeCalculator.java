package org.roboclub.robobuggy.calculatedNodes;

import org.roboclub.robobuggy.messages.BaseMessage;

public interface NodeCalculator {
	
	//Should be a function which returns time based measurements
	public BaseMessage calculator(long millis);
}
