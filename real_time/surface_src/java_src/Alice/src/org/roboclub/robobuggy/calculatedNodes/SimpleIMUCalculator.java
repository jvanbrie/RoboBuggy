package org.roboclub.robobuggy.calculatedNodes;

import org.roboclub.robobuggy.messages.ImuMeasurement;

public class SimpleIMUCalculator implements NodeCalculator {

	@Override
	public ImuMeasurement calculator(long millis) {
		return new ImuMeasurement(112, 112, 112);
		// TODO Auto-generated method stub
	}
	
}
