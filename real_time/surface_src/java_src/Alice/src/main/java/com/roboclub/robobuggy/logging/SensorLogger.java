package com.roboclub.robobuggy.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.JsonObject;
import com.roboclub.robobuggy.messages.BaseMessage;
import com.roboclub.robobuggy.ros.Message;
import com.roboclub.robobuggy.ros.MessageListener;
import com.roboclub.robobuggy.ros.SensorChannel;
import com.roboclub.robobuggy.ros.Subscriber;
import com.roboclub.robobuggy.utilities.RobobuggyDateFormatter;

/**
 * Logs data from the sensors
 * 
 * @author Joe Doyle
 * @author Vivaan Bahl
 * @author Trevor Decker
 */
public final class SensorLogger {
	private final PrintStream _log;
	private final Queue<String> _logQueue;
	private final ArrayList<Subscriber> subscribers;

	private static final Queue<String> startLoggingThread(PrintStream stream) {
		final LinkedBlockingQueue<String> ret = new LinkedBlockingQueue<>();
		
		String name = "\"name\": \"Robobuggy Data Logs\",";
		String schema_version = "\"schema_version\": 1.0,";
		String date_recorded = "\"date_recorded\": \"" + RobobuggyDateFormatter.getRobobuggyDateAsString(new Date()) + "\",";
		String swVersion = "\"software_version\": \"" + getCurrentSoftwareVersion() + "\",";
		String sensorDataHeader = "\"sensor_data\": [";
		stream.println("{" + "\n    " + name + "\n    " + schema_version + "\n    " + date_recorded + "\n    " + swVersion + "\n    " + sensorDataHeader);
		
		Thread logging_thread = new Thread() {
			
			private Hashtable<SensorChannel, Integer> dataBreakdown;
			
			
			public void run() {
				
				while (true) {
					try {
						String line = ret.take();
						if (line == null) {
							break;
						}
						if (line.contains("STOP")) {
							stream.println("        " + line);
							logButtonHits++;
							stream.println("    ],\n    \"data_breakdown\" : " + getDataBreakdown() + "\n}");
							break;
						}
						stream.println("        " + line + ",");
					} catch (InterruptedException e) {
						//TODO add to messages 
						e.printStackTrace();
					}
				}
			}


			@SuppressWarnings("unchecked")
			private String getDataBreakdown() {
				// TODO Auto-generated method stub
				JsonObject dataBreakdownObj = new JSONObject();
				
				dataBreakdownObj.put("logging_button", logButtonHits);
				dataBreakdownObj.put("gps", gpsHits);
				dataBreakdownObj.put("IMU", imuHits);
				dataBreakdownObj.put("encoder", encoderHits);
				dataBreakdownObj.put("brake", brakeHits);
				dataBreakdownObj.put("steering", steeringHits);
				
				return dataBreakdownObj.toJSONString();
			}
		};
		// TODO this potentially prevents a problem where the logger gets CPU
		// starved.
		// the actual fix probably involved throttling the sensors to a
		// reasonable update
		// frequency
		logging_thread.setPriority(10);//TODO this is terible 
		logging_thread.start();
		return ret;
	};

	private static String getCurrentSoftwareVersion() {
		// TODO Auto-generated method stub
		//TODO update this to actually grab current sw version
		return "1.0.0";
	}

	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	public SensorLogger(File outputDir, Date startTime) throws Exception {
		if (outputDir == null) {
			throw new Exception("Output Directory was null!");
		} else if (!outputDir.exists()) {
			outputDir.mkdirs();
		}

		File logFile = new File(outputDir, "sensors.txt");
		System.out.println("FileCreated: " + logFile.getAbsolutePath());
		try {
			_log = new PrintStream(logFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("Cannot create sensor log file ("
					+ logFile + ")!");
		}
		_logQueue = startLoggingThread(_log);

		// Subscribe to ALL THE PUBLISHERS
		subscribers = new ArrayList<Subscriber>();
		for (SensorChannel channel : SensorChannel.values()) {
			subscribers.add(
				new Subscriber(channel.getMsgPath(), new MessageListener() {
					@Override
					public void actionPerformed(String topicName, Message m) {
						_logQueue.offer(((BaseMessage)m).toLogString());
					}
				}));
		}
	}
}
