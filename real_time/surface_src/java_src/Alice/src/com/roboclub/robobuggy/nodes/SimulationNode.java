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
public class SimulationNode implements Node, Runnable {

	// add any member variables for this node
	boolean path_planning_flag = false; // set to true when new path planning data comes in
	
	Lock l; // add a lock for the node to be used on any shared resources
	Thread thread;
	
	
	public SimulationNode(String waypointFile) {
		
		/* Simulation Node tasks
		 * 
		 * What does simulation node need?
		 * it needs fake sensor data to output to the path planner, maybe given by the user 
		 * in a file (only first set?)
		 * Does it need to subscribe to anything? - Path planner
		 * 
		 * TODO: Finish path planner message and add the message as a subscriber in this node
		 * 
		 * It needs a simulation model. For guessing where the robot will be after the processing
		 * 
		 * Should the next point in simulation be determined via simulation or fake sensor data?
		 * 
		 * It needs a publisher but not necessarily a thread? Because nothing should be running in a 
		 * control loop. It should be cut and paste essentially. 
		 * 
		 * Simulation will need a simulation model of the system. 
		 * Also shall have a way to add forces to the system such that when it updates, it accounts for 
		 * new forces. 
		 * 
		 * Plan of attack:
		 * When message comes in from path planning node, set a flag in a member variable
		 * Spawn a thread that runs whenever it sees the flag set in the member variable
		 * Thread updates the model based on the functions and model available
		 * Publishes the results of the new sensor data to the thread (with calculated position, 
		 * empirical position, speed, ...)
		 * 
		 */
		
		/* Interesting thought:
		 * 
		 * Compare the simulation and pathplanning results with the angles actually used by the 
		 * servo, collected by our data set. 
		 * 
		 * See how close those are and how much the variance of the data is (ie how 'good' as compared
		 * to a human the system is)
		 * hypothesis: not as good because the system updates position once every second, whereas a 
		 * human can update it faster. 
		 */
		
		l = new ReentrantLock(); // create lock for any subscribers if necessary
			
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
		
		// spawn a new thread that listens to the subscribers, and runs continuously
		thread = new Thread(this);
		thread.start();
		
	}

	@Override
	public boolean shutdown() {
		// TODO: kill spawned thread
		// note: chose not to kill. Instead it is set not to run anymore when it comes out of run
		// Java has a weird way of killing spawned threads and we determined this was the best 
		// solution for now. 
		return false;
	}

	@Override
	public void run() {
		while(path_planning_flag){
			// update model
			// set path planning flag false
			// display (draw) results	
			// publish results
			
		}
	}

	
	/* new_model_force
	 * This function allows a user to add a new force on the model given  the direction
	 * and magnitude of the force. This will then be added to the Euler-Lagrange model
	 * for the system and correspondingly affect the simulation. 
	 * 
	 * It is on the user to determine the final magnitude and direction to be fed into
	 * the function, that is updated to the model. 
	 * 
	 * TODO: maybe use a node instead. 
	 */
	public void new_model_force(long magnitude, long direction){
		// TODO: change type of direction
		// TODO: add force to the Euler-Lagrange Equations
		
	}
	
	
	
	/* MAY NOT BE NECESSARY: update_model
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
