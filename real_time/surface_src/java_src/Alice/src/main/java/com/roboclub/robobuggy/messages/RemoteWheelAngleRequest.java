package com.roboclub.robobuggy.messages;

import java.util.Date;

import com.roboclub.robobuggy.ros.Message;

// Represents raw measurement from the IMU
public class RemoteWheelAngleRequest implements Message {
	public static final String version_id = "rc_wheel_angleV0.1";

	public Date timestamp;
	public double angle;

	// Makes an encoder measurement with the time of Now.
	public RemoteWheelAngleRequest(double angle) {
		this.angle = angle;
		this.timestamp = new Date();
	}

	@Override
	public String toLogString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message fromLogString(String str) {
		// TODO Auto-generated method stub
		return null;
	}

}