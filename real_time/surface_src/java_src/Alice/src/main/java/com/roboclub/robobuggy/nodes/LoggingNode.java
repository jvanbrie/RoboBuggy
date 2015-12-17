package com.roboclub.robobuggy.nodes;

import java.io.BufferedOutputStream;
import com.orsoncharts.util.json.JSONObject;
import com.roboclub.robobuggy.messages.GuiLoggingButtonMessage;
import com.roboclub.robobuggy.ros.Message;
import com.roboclub.robobuggy.ros.MessageListener;
import com.roboclub.robobuggy.ros.Node;
import com.roboclub.robobuggy.ros.Publisher;
import com.roboclub.robobuggy.ros.SensorChannel;
import com.roboclub.robobuggy.ros.Subscriber;
import com.roboclub.robobuggy.ui.Gui;

// When logging begins, a new folder is created, and then logging begins to that folder
public class LoggingNode implements Node {

	static String directoryPath;
	String topicName;
	
	BufferedOutputStream outputFile = null;
	
	Subscriber s;
	Subscriber logging_button_sub;
	
	public Publisher loggingButtonPub;
	// Get the folder that we're going to use

	// TODO get folder name from file.
	public LoggingNode() {
		loggingButtonPub = new Publisher(SensorChannel.GUI_LOGGING_BUTTON.getMsgPath());

		
		logging_button_sub = new Subscriber(Gui.GuiPubSubTopics.GUI_LOG_BUTTON_UPDATED.toString(), new MessageListener() {
			@Override 
			public void actionPerformed(String topicName, Message m) {
				loggingButtonPub.publish(m);
			}
		});
	
		/*
        //starts auto logging in a new thread 
        autoLogging.startAutoLogger(config.LOCAL_FOLDER_STORAGE_FOLDER,config.DRIVE_STORAGE_FOLDER_ID);
		//TODO move to some place usefull and uncomment 
        autoLogging autoLogger = autoLogging.getLogger();
		try {
			autoLogger.startTrackingLog(new File(directoryPath));
			autoLogger.saveLogDataToFolders();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
	}
	

	@SuppressWarnings("unchecked")
	public static JSONObject translatePeelMessageToJObject(String message) {
		// TODO Auto-generated method stub
		JSONObject data = new JSONObject();
		JSONObject params = new JSONObject();
		if (message.contains(GuiLoggingButtonMessage.LoggingMessage.START.toString())) {
			params.put("logging_status", "start");
		}
		else {
			params.put("logging_status", "stop");
		}
		data.put("timestamp", message.split(",")[1]);
		data.put("name", "logging button");
		data.put("params", params);
		

		
		return data;

	}


	@Override
	public boolean shutdown() {
		// TODO Auto-generated method stub
		loggingButtonPub = null;
		logging_button_sub = null;
		return true;
	}
	
}
