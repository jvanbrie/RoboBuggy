package com.roboclub.robobuggy.messages;

import java.util.Date;

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
public class BrakeMessage extends BaseMessage implements Message {

	public static final String brakesDown_key = "brakes_down";
	public static final String version_id = "brakeV0.1";


	public BrakeMessage(Date timestamp, boolean brake_is_down) {
		super(SensorChannel.BRAKE.getMsgPath(), RobobuggyDateFormatter.getRobobuggyDateAsString(timestamp));
	}
	
	public boolean getBrakesDown() {
		return getParamFromSensorData(brakesDown_key).getAsBoolean();
	}

}