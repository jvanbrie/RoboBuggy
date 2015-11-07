package com.roboclub.robobuggy.serial;

import java.io.InputStream;
import java.io.OutputStream;

import com.roboclub.robobuggy.ros.Publisher;
import com.roboclub.robobuggy.ros.SensorChannel;
import com.roboclub.robobuggy.serialParsers.SerialReader;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

/* In general, our serial node will have everything needed to read
 * from a specific serial port and publish to a specified channel.
 * Every subclass is simply going to define an implementation of
 * peel which is able to parse the 
 */
public class SerialEventNode {
	
	private SensorChannel sensor;
	private String port = "";
	private SerialReader sr;
	private int baudRate;
	
	private Thread runner;
		
	public SerialEventNode(SensorChannel sensor, int baudRate, SerialReader sr) {
		this.sensor = sensor;
		this.sr = sr;
		this.baudRate = baudRate;
	}
	
	public void setPort(String port){
		this.port = port;
	}
	
	public void connect() throws Exception {
		/* If we have a port, try to connect to it, initialize our
		 * reader on a new thread, and go for it.
		 */
		if (port != "") {
			serialIORunner serialRunner = new serialIORunner(sensor, baudRate);
			serialRunner.setPort(port);
			serialRunner.setReader(sr);
			try {
				serialRunner.connect();
			} catch (Exception e) {
				throw e;
			}
			runner = new Thread(serialRunner);
			runner.setPriority(Thread.MAX_PRIORITY);
			runner.start();
		}
	}
	
	/* Manages both the reading and writing to a single COM port,
	 * and runs in a separate thread. Connect first to see if 
	 * there's a connection error, and then in the
	 * run method we'll finally attach the data handlers.
	 */
	private class serialIORunner implements Runnable {
		
		private static final int CONNECTION_TIME_OUT = 2000;
		private String port = "";
		private Publisher msgPub;
		private Publisher statePub;
		private SerialReader reader;
		private SerialPort sp;
		
		private int baudRate;
		
		public serialIORunner(SensorChannel sensor, int baudRate) {
			msgPub = new Publisher(sensor.getMsgPath());
			statePub = new Publisher(sensor.getStatePath());
			this.baudRate = baudRate;
		}
		
		public void setPort(String port){
			this.port = port;
		}
		
		public void setReader(SerialReader reader) {
			this.reader = reader;
			
			reader.setMsgChannel(msgPub);
			reader.setStateChannel(statePub);
		}
	
		/* Just initializes the connection. Does NOT actually start
		 * reading from the port -- that's done in run.
		 */
		public void connect() throws Exception {
			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(port);
			if (portIdentifier.isCurrentlyOwned()) {
				System.out.println("Error: port currently in use!");
			} else {
				//TODO: Find a better way to name the port identifier
				CommPort commPort = portIdentifier.open("IMU Node", CONNECTION_TIME_OUT);
				
				if (commPort instanceof SerialPort) {
					sp = (SerialPort) commPort;
					sp.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
					
				} else {
					System.out.println("Did we not connect to a serial port?");
				}
			}		
		}
	
		@Override
		public void run() {
			if (sp != null) {
				try {
					InputStream in = sp.getInputStream();
					OutputStream out = sp.getOutputStream();
				
					reader.setInputStream(in);
					
					sp.addEventListener(reader);
					sp.notifyOnDataAvailable(true);
				} catch (Exception e) {
					System.out.println("Unable to fetch input / output streams from port");
				}
			}
		}
	}
}