package com.roboclub.robobuggy.messages;

import java.util.Date;

import com.roboclub.robobuggy.ros.Message;
import com.roboclub.robobuggy.sensors.SensorState;

public class StateMessage implements Message {
	private Date timestamp;
	private SensorState state;
	
	public StateMessage(SensorState state) {
		this.timestamp = new Date();
		this.state = state;
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

	public SensorState getState() {
		return this.state;
	}
}
