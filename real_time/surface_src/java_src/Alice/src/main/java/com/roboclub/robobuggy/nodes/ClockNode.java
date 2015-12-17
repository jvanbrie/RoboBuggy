package com.roboclub.robobuggy.nodes;

import java.util.Date;

import com.orsoncharts.util.json.JSONObject;
import com.roboclub.robobuggy.main.Robot;
import com.roboclub.robobuggy.messages.TimeMessage;
import com.roboclub.robobuggy.ros.Publisher;
import com.roboclub.robobuggy.ros.SensorChannel;

// is for timing work, so that the system can keep track of time properly in play back
// This is especial important for faster or slower then real time playback speed

//This node may be replaceable by looking at the time stamps on all the other messages 
public class ClockNode extends PeriodicNode{

	private Publisher msgPub;

	public ClockNode(SensorChannel sensor) {
		super(100); //updates in 1/10th of a second
		
		msgPub = new Publisher(sensor.getMsgPath());
	}

	@Override
	public boolean shutdown() {
		return false;
	}

	@Override
	protected void update() {
		//needs to be in if statement to stop the current clock from fighting with the palyback clock 
    	if(!Robot.isPlayBack){
    		msgPub.publish(new TimeMessage(new Date()));
    	}
		
	}

	public static JSONObject translatePeelMessageToJObject(String line) {
		JSONObject data = new JSONObject();
		JSONObject params = new JSONObject();
		String[] ypr = line.split(",");

		data.put("name", "clock");
		params.put("time", ypr[2]);
		data.put("params", params);
		data.put("timestamp",ypr[1]);



		return data;
	}

}
