package com.roboclub.robobuggy.messages;

import java.util.Date;

import com.google.gson.JsonPrimitive;
import com.roboclub.robobuggy.ros.Message;
import com.roboclub.robobuggy.ros.SensorChannel;
import com.roboclub.robobuggy.utilities.RobobuggyDateFormatter;

/**
 * Message for passing the desired commanded value of the steering
 */
public class DriveControlMessage extends BaseMessage implements Message {

	private static final String version_id = "drive_control_message";
	
	private static final String angle_key = "commanded_angle";
	
	/**
	 * Construct a new DriveControlMessage
	 * @param timestamp
	 * @param brakeEngagged
	 */
	public DriveControlMessage(Date timestamp, double angle) {
		super(SensorChannel.DRIVE_CTRL.getMsgPath(), RobobuggyDateFormatter.getRobobuggyDateAsString(timestamp));
		addParamToSensorData(angle_key, new JsonPrimitive(angle));
	}
	
	/**
	 * Returns the commanded angle of the steering as a double (in degrees)
	 * @return the commanded angle of the steering as a double (in degrees)
	 */
	public double getAngleDouble() {
		return getParamFromSensorData(angle_key).getAsDouble();
	}
	
	/**
	 * Returns the commanded angle of the steering as a short (in thousandths of degrees)
	 * @return the commanded angle of the steering as a short (in thousandths of degrees)
	 */
	public short getAngleShort() {
		return (short)(getAngleDouble()*1000.0);
	}
	

}
