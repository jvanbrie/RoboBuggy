package com.roboclub.robobuggy.nodes;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.roboclub.robobuggy.messages.BaseMessage;
import com.roboclub.robobuggy.messages.EncoderMeasurement;
import com.roboclub.robobuggy.messages.GpsMeasurement;
import com.roboclub.robobuggy.messages.GuiLoggingButton;
import com.roboclub.robobuggy.ros.Message;
import com.roboclub.robobuggy.ros.MessageListener;
import com.roboclub.robobuggy.ros.Node;
import com.roboclub.robobuggy.ros.SensorChannel;
import com.roboclub.robobuggy.ros.Subscriber;

// When logging begins, a new folder is created, and then logging begins to that folder
public class PathPlanningNode implements Node, Runnable {

	// Get the folder that we're going to use
	String waypointFile;

	// list of subscribers
	GpsMeasurement gps;
	EncoderMeasurement velocity;
	
	// list of subscribers 'just updated' flags
	boolean velocity_update;
	boolean gps_update;
	
	Thread thread;
	
	// TODO: array that stores waypoints
	
	public PathPlanningNode(String waypointFile) {
		this.waypointFile = waypointFile;
		
		// TODO: function: get the way points out of the waypoint file
		
		
		// spawn a new thread that listens to the subscribers, and runs continuously
		thread = new Thread(this);
		thread.start();
		

		
		/********* Do Path planning Computation ********/
		// Function: caculate Beizer curve
		
		// Function: find next closest waypoint to Beizer curve via cost function
		
		// Put waypoint onto publisher
		
		/*
		this.directoryPath  = directoryPath;
		// Start the subscriber
		s = new Subscriber(topicName, new MessageListener() {
			@Override
			public void actionPerformed(String topicName, Message m) {
				if(outputFile == null) {
					return;
				}
				
				try {
					outputFile.write(m.toLogString().getBytes());
					outputFile.write('\n');
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});*/
	
		
	}




	
	@Override
	public boolean shutdown() {
		// TODO: kill spawned thread
		return false;
	}

	@Override
	public void run() {
		// TODO: Complete the runnable thread
		
		/******* Add new subscribers ********/
		// add a new subscriber that listens to the velocity message
		Subscriber velocity_sub = new Subscriber(SensorChannel.ENCODER.getMsgPath(), new MessageListener(){
			@Override
			public void actionPerformed(String topicName, Message m){
				// do something with encoder data
				// throws a flag to show just updated
				velocity = (EncoderMeasurement)m;
				velocity_update = true;
			}
		});
		
		// add a new subscriber that listens to current position of buggy
		Subscriber gps_sub = new Subscriber(SensorChannel.GPS.getMsgPath(), new MessageListener(){
			@Override
			public void actionPerformed(String topicName, Message m){
				// do something with the GPS data
				gps = (GpsMeasurement)m;
				// throws a flag when just updated
				gps_update = true;
			}
		});
		
		while(true){
			// do stuff in the loop
			
		}
		
		/* TODO: in thread
		 * listen to subscribers and store values
		 * make control loop with constant time steps
		 * Use variables and unflag each subscriber last updated flag
		 *  - if not last updated value, extoporlate data based on previous values and 
		 * system mode
		 *  - if is last updated value, use value to update the model 
		 *  
		 * based of model, use bezier curve to determine next waypoint 
		 * 
		 * get next waypoint and publish to publisher
		 * 
		 */
		
	}

}
