package com.roboclub.robobuggy.messages;

import com.google.gson.JsonPrimitive;
import com.roboclub.robobuggy.ros.Message;
import com.roboclub.robobuggy.ros.SensorChannel;
import com.roboclub.robobuggy.utilities.RobobuggyDateFormatter;

import java.util.Date;

public class FingerPrintMessage extends BaseMessage implements Message
{
	public static final String fp_hash_key = "fp_hash";
	
	public FingerPrintMessage(Date tStamp, int hash) {
		super(SensorChannel.FP_HASH.getMsgPath(), RobobuggyDateFormatter.getRobobuggyDateAsString(tStamp));
		addParamToSensorData(fp_hash_key, new JsonPrimitive(hash));
	}
	
	public int getHash() {
		return getParamFromSensorData(fp_hash_key).getAsInt();
	}

}
