package org.roboclub.robobuggy.messages;

import java.util.Date;

import org.roboclub.robobuggy.ros.Message;

public class ResetMessage implements Message {
	private Date timestamp;
	
	public ResetMessage() {
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
