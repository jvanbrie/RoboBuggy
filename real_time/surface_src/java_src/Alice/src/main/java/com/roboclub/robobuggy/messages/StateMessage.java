package com.roboclub.robobuggy.messages;

import java.util.Date;

import com.google.gson.JsonPrimitive;
import com.roboclub.robobuggy.ros.Message;
import com.roboclub.robobuggy.ros.SensorChannel;
import com.roboclub.robobuggy.sensors.SensorState;
import com.roboclub.robobuggy.utilities.RobobuggyDateFormatter;

public class StateMessage extends BaseMessage implements Message {
	
	public static final String state_key = "state";
	
	public StateMessage(Date timestamp, SensorState state) {
		super(SensorChannel.SENSOR_STATE.getMsgPath(), RobobuggyDateFormatter.getRobobuggyDateAsString(timestamp));
		
		addParamToSensorData(state_key, new JsonPrimitive(state.toString()));
	}
	
	public SensorState getState() {
		return SensorState.valueOf(getParamFromSensorData(state_key).getAsString());
	}
}
