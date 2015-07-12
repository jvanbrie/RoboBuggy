package org.roboclub.robobuggy.main;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.util.ArrayList;

import org.roboclub.robobuggy.calculatedNodes.SimpleEncoderCalculator;
import org.roboclub.robobuggy.fauxNodes.FauxEncoderNode;
import org.roboclub.robobuggy.fauxNodes.FauxGPSNode;
import org.roboclub.robobuggy.fauxNodes.FauxIMUNode;
import org.roboclub.robobuggy.fauxNodes.FauxNode;
import org.roboclub.robobuggy.fauxNodes.FauxSteeringNode;
import org.roboclub.robobuggy.logging.RobotLogger;
import org.roboclub.robobuggy.nodes.EncoderNode;
import org.roboclub.robobuggy.nodes.GpsNode;
import org.roboclub.robobuggy.nodes.ImuNode;
import org.roboclub.robobuggy.nodes.SteeringNode;
import org.roboclub.robobuggy.ros.Message;
import org.roboclub.robobuggy.ros.MessageListener;
import org.roboclub.robobuggy.ros.Node;
import org.roboclub.robobuggy.ros.SensorChannel;
import org.roboclub.robobuggy.ros.Subscriber;
import org.roboclub.robobuggy.sensors.SensorManager;
import org.roboclub.robobuggy.simulation.FauxRunner;
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
	
//	// Open a serial port
//	private static SerialPort connect(String portName) throws Exception
//    {
//        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
//        if ( portIdentifier.isCurrentlyOwned() )
//        {
//            System.out.println("Error: Port is currently in use");
//            return null;
//        }
//        else
//        {
//        	//TODO fix this so that it is not potato 
//            CommPort commPort = portIdentifier.open("potato", 2000);
//            
//            if ( commPort instanceof SerialPort )
//            {
//                SerialPort serialPort = (SerialPort) commPort;
//                serialPort.setSerialPortParams(57600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
//                return serialPort;
//            }
//            else
//            {
//                System.out.println("Error: Only serial ports are handled by this example.");
//            }
//        }
//		return null;
//    }	
	public static void bringup_sim() throws Exception {
//		ArrayList<Node> sensorList = new ArrayList<Node>();

		// Turn on logger!
		if(config.logging){
			System.out.println("Starting Logging");
			RobotLogger.getInstance();
		}

		Gui.EnableLogging();

// Fake sensors (simulated)
//		ArrayList<FauxNode> fauxSensors = new ArrayList<FauxNode>();
//		fauxSensors.add(new FauxIMUNode(SensorChannel.IMU));
//		fauxSensors.add(new FauxGPSNode(SensorChannel.GPS));
//		fauxSensors.add(new FauxEncoderNode(SensorChannel.ENCODER));
//		fauxSensors.add(new FauxSteeringNode(SensorChannel.DRIVE_CTRL));
//		String path = "C:\\Users\\Vasu\\Documents\\RoboClub\\RoboBuggy\\offline\\offline_tools\\java_src\\FauxArduino\\logs\\2015-04-12-06-22-37\\sensors.txt";
//		new Thread(new FauxRunner(fauxSensors, path)).start();
			
//		ImuNode imu = new ImuNode(SensorChannel.IMU);
//		GpsNode gps = new GpsNode(SensorChannel.GPS);
//		EncoderNode enc = new EncoderNode(SensorChannel.ENCODER);
//		SteeringNode drive_ctrl = new SteeringNode(SensorChannel.DRIVE_CTRL);
//		
		SensorManager sm = SensorManager.getInstance();
//		sm.newRealSensor(SensorChannel.IMU, "COM5");
//		sm.newRealSensor(SensorChannel.GPS, "COM2");
//		sm.newRealSensor(SensorChannel.ENCODER, "COM42");
//		sm.newRealSensor(SensorChannel.DRIVE_CTRL, "COM69");
		String path = "C:\\Users\\Vasu\\Documents\\RoboClub\\RoboBuggy\\offline\\offline_tools\\java_src\\FauxArduino\\logs\\2015-04-12-06-22-37\\sensors.txt";
		String path2 = "C:\\Users\\Vasu\\Documents\\RoboClub\\RoboBuggy\\offline\\offline_tools\\java_src\\FauxArduino\\logs\\2015-04-12-06-22-38\\sensors.txt";
//		String path2 = "C:\\Users\\Vasu\\Documents\\RoboClub\\RoboBuggy\\offline\\offline_tools\\java_src\\FauxArduino\\logs\\2015_slurpee_day\\dualTest.txt";
		sm.newSimulatedSensors(path
			//,SensorChannel.IMU,
			//,SensorChannel.ENCODER,
			,SensorChannel.GPS
			//,SensorChannel.DRIVE_CTRL
			);
		sm.newSimulatedSensors(path2
			,SensorChannel.IMU
			,SensorChannel.DRIVE_CTRL
			);
		
		//This will *effectively* disable it, but it seems that because it's subscribing to things
		//there are still parts of the GUI which think a sensor exists. This may or may not be an issue?
		//ISSUE: Seems like there's a delay in enabling / disabling it?
		//SOLUTION: It looks like the delay was caused by the call of newCalculatedSensor, which isn't instantaneous
		//		this may or may not be a problem? Will ask Trevor.
		sm.disableFauxNode(path2, SensorChannel.IMU);
		
		sm.newCalculatedSensor(SensorChannel.IMU, new SimpleEncoderCalculator());
		
		new Subscriber(SensorChannel.ENCODER.getMsgPath(), new MessageListener() {
			@Override
			public void actionPerformed(String topicName, Message m) {
				//System.out.println(m.toLogString());
			}
		});
		
		
	}
}
