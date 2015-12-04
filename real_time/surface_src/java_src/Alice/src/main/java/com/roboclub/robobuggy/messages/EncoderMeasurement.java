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
public class EncoderMeasurement extends BaseMessage implements Message {

	public static final String version_id = "encoderV0.0";
	
	public static final String dataWord_key = "dataWord";
	public static final String distance_key = "distance";
	public static final String velocity_key = "velocity";
	public static final String acceleration_key = "acceleration";
	

	// Makes an encoder measurement with the time of Now.
	public EncoderMeasurement(double distance, double velocity) {
		this(new Date(), distance, velocity);
	}
	
	public EncoderMeasurement(Date timestamp, double distance, double velocity) {
		this(timestamp, 0, distance, velocity, 0);
	}
	
	//logs the number of ticks received since last time as well
	public EncoderMeasurement(Date timestamp, double dataWord, double distance, double velocity, double accel) {
		super(SensorChannel.ENCODER.getMsgPath(), RobobuggyDateFormatter.getRobobuggyDateAsString(timestamp));
		addParamToSensorData(dataWord_key, new JsonPrimitive(dataWord));
		addParamToSensorData(distance_key, new JsonPrimitive(distance));
		addParamToSensorData(velocity_key, new JsonPrimitive(velocity));
		addParamToSensorData(acceleration_key, new JsonPrimitive(accel));
	}
	
	public double getDataWord() {
		return getParamFromSensorData(dataWord_key).getAsDouble();
	}
	
	public double getDistance() {
		return getParamFromSensorData(distance_key).getAsDouble();
	}
	
	public double getVelocity() {
		return getParamFromSensorData(velocity_key).getAsDouble();
	}
	
	public double getAcceleration() {
		return getParamFromSensorData(acceleration_key).getAsDouble();
	}

}