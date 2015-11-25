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
	
	public static final String name_key = "name";
	public static final String timestamp_key = "timestamp";
	public static final String data_key = "data";
	
	public BaseMessage(String sensorName, String timestamp) {
		sensor = new JsonObject();
		sensor.addProperty(name_key, sensorName);
		sensor.addProperty(timestamp_key, timestamp);
		sensor.add(data_key, new JsonObject());
	}
	
	public BaseMessage() {
		this("Generic Message", new SimpleDateFormat(RobobuggyDateFormatter.ROBOBUGGY_DATE_FORMAT).format(new Date()));
	}
	
	public void addParamToSensorData(String key, JsonPrimitive value) {
		((JsonObject) sensor.get("data")).add(key, value);
	}
	
	public JsonPrimitive getParamFromSensorData(String key) {
		return ((JsonObject)sensor.get("data")).getAsJsonPrimitive(key);
	}
	
	public Date getTimestamp() {
		return RobobuggyDateFormatter.getDatefromRobobuggyDateString(sensor.get(timestamp_key).getAsString());
	}
	
	public String getSensorName() {
		return sensor.get(name_key).getAsString();
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
