package com.roboclub.robobuggy.nodes;


import java.sql.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.roboclub.robobuggy.messages.EncoderMeasurement;
import com.roboclub.robobuggy.messages.GpsMeasurement;
import com.roboclub.robobuggy.ros.Message;
import com.roboclub.robobuggy.ros.MessageListener;
import com.roboclub.robobuggy.ros.Node;
import com.roboclub.robobuggy.ros.SensorChannel;
import com.roboclub.robobuggy.ros.Subscriber;

// When logging begins, a new folder is created, and then logging begins to that folder
public class PathPlanningNode implements Node, Runnable {

	// Get the folder that we're going to use
	String waypointFile;

	// Timestep (in ms)
	long time_step = 100;
	
	// list of subscribers
	GpsMeasurement gps;
	EncoderMeasurement velocity;
	
	// list of subscribers 'just updated' flags
	boolean velocity_update;
	boolean gps_update;
	Date last_update_time;
	
	// Spawned thread
	Thread thread;
	
	// Locks
	Lock l;
	
	// TODO: array that stores waypoints
	
	
	public PathPlanningNode(String waypointFile) {
		this.waypointFile = waypointFile;
		l = new ReentrantLock();
		
		// TODO: function: get the way points out of the waypoint file
			
		// add a new subscriber that listens to the velocity message
		Subscriber velocity_sub = new Subscriber(SensorChannel.ENCODER.getMsgPath(), new MessageListener(){
			@Override
			public void actionPerformed(String topicName, Message m){
				// do something with encoder data
				// throws a flag to show just updated
				l.lock();
				velocity = (EncoderMeasurement)m;
				l.unlock();
				velocity_update = true;
			}
		});
		
		// add a new subscriber that listens to current position of buggy
		Subscriber gps_sub = new Subscriber(SensorChannel.GPS.getMsgPath(), new MessageListener(){
			@Override
			public void actionPerformed(String topicName, Message m){
				// do something with the GPS data
				l.lock();
				gps = (GpsMeasurement)m;
				l.unlock();
				// throws a flag when just updated
				gps_update = true;
			}
		});
		
		// spawn a new thread that listens to the subscribers, and runs continuously
		thread = new Thread(this);
		thread.start();
		
	}

	@Override
	public boolean shutdown() {
		// TODO: kill spawned thread
		return false;
	}

	@Override
	public void run() {
		while(true){
			long start_time = System.currentTimeMillis();
			// Start of runnable loop
			if(gps_update == true && velocity_update == true){
				// new gps and velocity - update model 
				update_model();
			}else if(gps_update == true){
				// new gps data - update model
				update_model();
			}else if(velocity_update == true){
				// new velocity data - update model
				update_model();
			}
			
			/********* Do Path planning Computation ********/
			// Function: caculate Beizer curve
			
			// Function: find next closest waypoint to Beizer curve via cost function
			
			// Put waypoint onto publisher
			
			gps_update = false;
			velocity_update = false;
			// Do the timing control
			long stop_time = System.currentTimeMillis();
			long millis = stop_time - start_time;
			if((stop_time - start_time) < time_step){
				try {
					Thread.sleep(millis); // is this how we make a thread sleep?
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
			}
		}
		
		/* TODO: in thread
		 * listen to subscribers and store values (done)
		 * make control loop with constant time steps (done)
		 * Use variables and unflag each subscriber last updated flag (done)
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

	/* update_model
	 * This is an internal model of the buggy
	 * It is used to give a more 'current' state of the buggy to the controller because
	 * not all the sensors can be updated as frequently as needed
	 * So we try to take the next best estimate of state
	 */
	private void update_model(){
		// TODO: Complete buggy model
		// what is needed for buggy model?
		// position + time it was updated
		// velocity + time it was updated
		// steering angle + time it was updated
		// 
		
		
		return;
	}
}
