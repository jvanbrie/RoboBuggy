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
public class ImuMeasurement extends BaseMessage implements Message {

	public static final String version_id = "imuV0.0";

	public static final String yaw_key = "yaw";
	public static final String pitch_key = "pitch";
	public static final String roll_key = "roll";


	public ImuMeasurement(Date timestamp, double yaw, double pitch, double roll) {
		super(SensorChannel.IMU.getMsgPath(), RobobuggyDateFormatter.getRobobuggyDateAsString(timestamp));
		
		addParamToSensorData(yaw_key, new JsonPrimitive(yaw));
		addParamToSensorData(pitch_key, new JsonPrimitive(pitch));
		addParamToSensorData(roll_key, new JsonPrimitive(roll));
	}
	
	public double getYaw() {
		return getParamFromSensorData(yaw_key).getAsDouble();
	}
	
	public double getPitch() {
		return getParamFromSensorData(pitch_key).getAsDouble();
	}
	
	public double getRoll() {
		return getParamFromSensorData(roll_key).getAsDouble();
	}

}
