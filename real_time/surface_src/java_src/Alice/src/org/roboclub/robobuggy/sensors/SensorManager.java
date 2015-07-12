package org.roboclub.robobuggy.sensors;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.roboclub.robobuggy.fauxNodes.FauxEncoderNode;
import org.roboclub.robobuggy.fauxNodes.FauxGPSNode;
import org.roboclub.robobuggy.fauxNodes.FauxIMUNode;
import org.roboclub.robobuggy.fauxNodes.FauxNode;
import org.roboclub.robobuggy.fauxNodes.FauxSteeringNode;
import org.roboclub.robobuggy.nodes.EncoderNode;
import org.roboclub.robobuggy.nodes.GpsNode;
import org.roboclub.robobuggy.nodes.ImuNode;
import org.roboclub.robobuggy.nodes.SteeringNode;
import org.roboclub.robobuggy.ros.Node;
import org.roboclub.robobuggy.ros.SensorChannel;
import org.roboclub.robobuggy.simulation.FauxRunner;

public class SensorManager {
	
	//Need to manage real, simulated, and calculated sensors
	
	private LinkedHashMap<String, Node> realSensors;
	private LinkedHashMap<String, LinkedHashMap<SensorChannel, FauxNode>> simulatedSensors;
	private LinkedHashMap calculatedSensors;
	
	private SensorManager() {
		realSensors = new LinkedHashMap<String, Node>();
		simulatedSensors = new LinkedHashMap<String, LinkedHashMap<SensorChannel, FauxNode>>();
		calculatedSensors = new LinkedHashMap();	
	}

	// Open a serial port
	private static SerialPort connect(String portName) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() ) {
            System.out.println("Error: Port is currently in use");
            return null;
        } else {
        	//TODO fix this so that it is not potato 
            CommPort commPort = portIdentifier.open("potato", 2000);
            
            if ( commPort instanceof SerialPort ) {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(57600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                return serialPort;
            } else {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }
		return null;
    }	
	
	//TODO: Come up with a better way to determine which sensor to create
	public void newRealSensor(SensorChannel sensor, String port) throws Exception {
		if (realSensors.containsKey(port)) {
			throw new Exception("Trying to connect on a port which exists already!");
		}
		
		SerialPort sp = null;
		try {
			System.out.println("Initializing serial connection on port " + port);
			sp = connect(port);
			System.out.println("Connected on port " + port);
		} catch (Exception  e) {
			System.out.println("Unable to connect to " + sensor.toString() + " on port " + port);
			e.printStackTrace();
			throw new Exception("Device not found error");
		}

		switch (sensor) {
		case IMU:
			ImuNode imu = new ImuNode(sensor);
			imu.setSerialPort(sp);
			realSensors.put(port, imu);
			break;
		case GPS:
			GpsNode gps = new GpsNode(sensor);
			gps.setSerialPort(sp);
			realSensors.put(port, gps);			
			break;
		case ENCODER:
			EncoderNode enc = new EncoderNode(sensor);
			enc.setSerialPort(sp);
			realSensors.put(port, enc);
			break;
		case DRIVE_CTRL:
			SteeringNode steer = new SteeringNode(sensor);
			steer.setSerialPort(sp);
			realSensors.put(port, steer);
			break;			
		default:
			System.out.println("Invalid Sensor Type");
			throw new Exception("Stop trying to initialize sensors that don't exist!");
		}
	}
	
	//Due to how I wrote the code, you'll need to initialize all of the sensors you want at once.
	//Disable the ones you don't want later if you want, I guess?
	public void newSimulatedSensors(String path, SensorChannel... sensors) {
		//A pretty terrible solution for now, this will assume that there will only be one type of
		//simulated sensor per log file. This is an okay assumption for the data we are currently
		//outputting, but if we change the log file to log multiple sensors with the granularity
		//we are doing right now, this will need some updating
		LinkedHashMap<SensorChannel, FauxNode> fauxSensors = new LinkedHashMap<SensorChannel, FauxNode>();
		for (SensorChannel sensor : sensors) {
			switch (sensor) {
			case IMU:
				fauxSensors.put(sensor, new FauxIMUNode(sensor));
				break;
			case GPS:
				fauxSensors.put(sensor, new FauxGPSNode(sensor));
				break;
			case ENCODER:
				fauxSensors.put(sensor, new FauxEncoderNode(sensor));
				break;
			case DRIVE_CTRL:
				fauxSensors.put(sensor, new FauxSteeringNode(sensor));
				break;
			default:
				break;
			}
		}
		new Thread(new FauxRunner(new ArrayList<FauxNode>(fauxSensors.values()), path)).start();
		simulatedSensors.put(path, fauxSensors);
	}
	
	//Google says this is a good idea
	//but I really don't know anything about Java ...
	private static class Holder {
		static final SensorManager instance = new SensorManager();
	}
	
	public static SensorManager getInstance() {
		return Holder.instance;
	}
}