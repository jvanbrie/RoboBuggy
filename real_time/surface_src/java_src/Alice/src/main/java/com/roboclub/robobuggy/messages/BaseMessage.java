package com.roboclub.robobuggy.messages;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.roboclub.robobuggy.ros.Message;
import com.roboclub.robobuggy.utilities.RobobuggyDateFormatter;

public class BaseMessage implements Message {
	
	protected JsonObject sensor;
	
	public BaseMessage(String sensorName, String timestamp) {
		sensor = new JsonObject();
		sensor.addProperty("name", sensorName);
		sensor.addProperty("timestamp", timestamp);
		sensor.add("data", new JsonObject());
	}
	
	public BaseMessage() {
		this("Generic Message", new SimpleDateFormat(RobobuggyDateFormatter.ROBOBUGGY_DATE_FORMAT).format(new Date()));
	}
	
	public void addParamToSensorData(String key, JsonPrimitive value) {
		((JsonObject) sensor.get("data")).add(key, value);
	}
	
	public String toLogString() {
		return sensor.getAsString();
	}
	
	public static Message fromLogString(String logString) {
		JsonObject parsedSensor = new Gson().fromJson(logString, JsonObject.class);
		BaseMessage m = new BaseMessage();
		m.sensor = parsedSensor;
		return m;
	}
	
}
