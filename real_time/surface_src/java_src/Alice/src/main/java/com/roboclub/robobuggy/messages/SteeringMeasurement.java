package com.roboclub.robobuggy.messages;

import java.util.Date;

import com.google.gson.JsonPrimitive;
import com.roboclub.robobuggy.ros.Message;
import com.roboclub.robobuggy.ros.SensorChannel;
import com.roboclub.robobuggy.utilities.RobobuggyDateFormatter;

public class SteeringMeasurement extends BaseMessage implements Message {
	
	public static final String angle_key = "angle";
	
	
	public SteeringMeasurement(Date timestamp, int angle) {
		super(SensorChannel.STEERING.getMsgPath(), RobobuggyDateFormatter.getRobobuggyDateAsString(timestamp));
		
		addParamToSensorData(angle_key, new JsonPrimitive(angle));
	}
	
	
	public int getSteering() {
		return getParamFromSensorData(angle_key).getAsInt();
	}
}
