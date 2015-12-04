package com.roboclub.robobuggy.messages;

import java.util.Date;

import com.google.gson.JsonPrimitive;
import com.roboclub.robobuggy.main.MessageLevel;
import com.roboclub.robobuggy.ros.Message;
import com.roboclub.robobuggy.ros.SensorChannel;
import com.roboclub.robobuggy.utilities.RobobuggyDateFormatter;

public class RobobuggyLogicExceptionMeasurment extends BaseMessage implements Message{
	
	public static final String message_key = "message";
	public static final String level_key = "level";
	
	public RobobuggyLogicExceptionMeasurment(Date timestamp, String message, MessageLevel level) {
		super(SensorChannel.LOGIC_EXCEPTION.getMsgPath(), RobobuggyDateFormatter.getRobobuggyDateAsString(timestamp));
		
		addParamToSensorData(message_key, new JsonPrimitive(message));
		addParamToSensorData(level_key, new JsonPrimitive(level.toString()));
	}
	
	public String getMessage() {
		return getParamFromSensorData(message_key).getAsString();
	}
	
	public MessageLevel getMessageLevel() {
		return MessageLevel.valueOf(getParamFromSensorData(level_key).getAsString());
	}

	

}
