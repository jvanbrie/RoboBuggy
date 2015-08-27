package org.roboclub.robobuggy.main;

import org.roboclub.robobuggy.calculatedNodes.CalculatedNodeEnum;
import org.roboclub.robobuggy.calculatedNodes.SimpleEncoderCalculator;
import org.roboclub.robobuggy.calculatedNodes.SimpleIMUCalculator;
import org.roboclub.robobuggy.logging.RobotLogger;
import org.roboclub.robobuggy.ros.Message;
import org.roboclub.robobuggy.ros.MessageListener;
import org.roboclub.robobuggy.ros.SensorChannel;
import org.roboclub.robobuggy.ros.Subscriber;
import org.roboclub.robobuggy.sensors.SensorManager;
import org.roboclub.robobuggy.ui.Gui;

public class mainFile {
	static Robot buggy;

	static int num = 0;
	
	public static void main(String args[]) {
		System.out.println("did we get here");
		//ArrayList<Integer> cameras = new ArrayList<Integer>();  //TODO have this set the cameras to use 
		config.getInstance();//must be run at least once
		
		for (int i = 0; i < args.length; i++) {
			if (args[i].equalsIgnoreCase("-g")) {
				config.GUI_ON = false;
			} else if (args[i].equalsIgnoreCase("+g")) {
				config.GUI_ON = true;
			} else if (args[i].equalsIgnoreCase("-r")) {
				config.active = false;
			} else if (args[i].equalsIgnoreCase("+r")) {
				config.active = true;
			}
		}
		
		if(config.GUI_ON){
			Gui.getInstance();
		}
		
		// Starts the robot
		if(config.DATA_PLAY_BACK_DEFAULT){
			try {
				bringup_sim();
			} catch (Exception e) {
				Gui.close();
				System.out.println("Unable to bringup simulated robot. Stacktrace omitted because it's really big.");
				e.printStackTrace();
				return;
			}
		} else {
			Robot.getInstance();
		}	
	}

public static void bringup_sim() throws Exception {
		// Turn on logger!
		if(config.logging){
			System.out.println("Starting Logging");
			RobotLogger.getInstance();
		}

		Gui.EnableLogging();

		SensorManager sm = SensorManager.getInstance();

		//		sm.newRealSensor(SensorChannel.IMU, "COM5");
//		sm.newRealSensor(SensorChannel.GPS, "COM2");
//		sm.newRealSensor(SensorChannel.ENCODER, "COM42");
//		sm.newRealSensor(SensorChannel.DRIVE_CTRL, "COM69");
		String path = "D:\\Vasu\\Documents\\RoboClub\\RoboBuggy\\offline\\offline_tools\\java_src\\FauxArduino\\logs\\2015-04-12-06-22-37\\sensors.txt";
		String path2 = "D:\\Vasu\\Documents\\RoboClub\\RoboBuggy\\offline\\offline_tools\\java_src\\FauxArduino\\logs\\2015-04-12-06-22-38\\sensors.txt";
//		String path2 = "C:\\Users\\Vasu\\Documents\\RoboClub\\RoboBuggy\\offline\\offline_tools\\java_src\\FauxArduino\\logs\\2015_slurpee_day\\dualTest.txt";
		sm.newFauxSensors(path
			//,SensorChannel.IMU
			,SensorChannel.ENCODER
			,SensorChannel.GPS
			//,SensorChannel.DRIVE_CTRL
			);
		sm.newFauxSensors(path2
			,SensorChannel.IMU
			,SensorChannel.DRIVE_CTRL
			);
		
		//This will *effectively* disable it, but it seems that because it's subscribing to things
		//there are still parts of the GUI which think a sensor exists. This may or may not be an issue?
		//ISSUE: Seems like there's a delay in enabling / disabling it?
		//SOLUTION: It looks like the delay was caused by the call of newCalculatedSensor, which isn't instantaneous
		//		this may or may not be a problem? Will ask Trevor.
		sm.disableFauxNode(path, SensorChannel.ENCODER);
		sm.disableFauxNode(path2, SensorChannel.IMU);
		
		//I've just discovered that sending values from different sensors doesn't break the entire system, but instead 
		//just causes a lot of jittery / jumpy behavior.
		sm.newCalculatedSensor(CalculatedNodeEnum.ENCODER, SensorChannel.ENCODER, new SimpleEncoderCalculator(), 100);
		sm.newCalculatedSensor(CalculatedNodeEnum.IMU, SensorChannel.IMU, new SimpleIMUCalculator(), 100);
		
		new Subscriber(SensorChannel.ENCODER.getMsgPath(), new MessageListener() {
			@Override
			public void actionPerformed(String topicName, Message m) {
				//System.out.println(m.toLogString());
			}
		});
	}
}
