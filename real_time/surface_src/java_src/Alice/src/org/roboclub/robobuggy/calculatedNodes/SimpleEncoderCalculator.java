package org.roboclub.robobuggy.calculatedNodes;

import org.roboclub.robobuggy.messages.ImuMeasurement;

public class SimpleEncoderCalculator implements NodeCalculator {

	@Override
	public ImuMeasurement calculator(int millis) {
		return new ImuMeasurement(100, 100, 100);
		// TODO Auto-generated method stub
	}
	
}
