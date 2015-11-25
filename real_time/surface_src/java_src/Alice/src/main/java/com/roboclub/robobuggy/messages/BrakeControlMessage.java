package com.roboclub.robobuggy.messages;

import java.util.Date;

import com.google.gson.JsonPrimitive;
import com.roboclub.robobuggy.ros.Message;
import com.roboclub.robobuggy.ros.SensorChannel;
import com.roboclub.robobuggy.utilities.RobobuggyDateFormatter;

/**
 * Message for passing the desired commanded value of the brakes
 */
public class BrakeControlMessage extends BaseMessage implements Message {

	private static final String version_id = "brake_control_message";
	
	public static final String brakeEngaged_key = "brake_engaged";
	
	/**
	 * Construct a new BrakeControlMessage
	 * @param timestamp
	 * @param brakeEngagged
	 */
	public BrakeControlMessage(Date timestamp, boolean brakeEngaged) {
		super(SensorChannel.BRAKE_CTRL.getMsgPath(), RobobuggyDateFormatter.getFormattedRobobuggyDateAsString(timestamp));
		addParamToSensorData(brakeEngaged_key, new JsonPrimitive(brakeEngaged));
	}
	
	/**
	 * Returns true iff the brake is commanded to be engaged
	 * @return true iff the brake is commanded to be engaged
	 */
	public boolean isBrakeEngaged() {
		return sensorData.get(brakeEngaged_key).getAsBoolean();
	}
	

}
