package com.roboclub.robobuggy.messages;

import java.util.Date;

import com.google.gson.JsonPrimitive;
import com.roboclub.robobuggy.ros.Message;
import com.roboclub.robobuggy.ros.SensorChannel;
import com.roboclub.robobuggy.utilities.RobobuggyDateFormatter;

/**
 * @author ?
 *
 * @version 0.5
 * 
 *          CHANGELOG: NONE
 * 
 *          DESCRIPTION: TODO
 */

// Represents raw measurement from the IMU
public class GuiLoggingButtonMessage extends BaseMessage implements Message {

	public enum LoggingMessage {
		START, STOP		
	}
	
	public static final String version_id = "gui_logging_buttonV0.0";

	public static final String loggingMessage_key = "logging_status"; 

	public GuiLoggingButtonMessage(Date timestamp, LoggingMessage lm) {
		super(SensorChannel.GUI_LOGGING_BUTTON.getMsgPath(), RobobuggyDateFormatter.getRobobuggyDateAsString(timestamp));
		
		addParamToSensorData(loggingMessage_key, new JsonPrimitive(lm.toString()));
	}
	
	public LoggingMessage getLoggingStatus() {
		return LoggingMessage.valueOf(getParamFromSensorData(loggingMessage_key).getAsString());
	}

}