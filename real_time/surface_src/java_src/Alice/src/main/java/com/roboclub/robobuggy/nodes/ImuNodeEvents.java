package com.roboclub.robobuggy.nodes;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

import com.roboclub.robobuggy.ros.Publisher;
import com.roboclub.robobuggy.ros.SensorChannel;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class ImuNodeEvents {
	private static final long SENSOR_TIME_OUT = 2000;
	
	private String port = "";
	
	public Publisher msgPub;
	
	public ImuNodeEvents(SensorChannel sensor) {
		msgPub = new Publisher(sensor.getMsgPath());
	}
	
	public void setSerialPort(String port) {
		this.port = port;
	}
	
	public int getBaudrate() {
		return 57600;
	}
	
	private class readerThread implements Runnable {

		public readerThread() {
			
		}
		
		public void connect() throws Exception {
			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(port);
			if (portIdentifier.isCurrentlyOwned()) {
				System.out.println("Error: port currently in use!");
			} else {
				CommPort commPort = portIdentifier.open("IMU Node", 2000); // connection timeout
				
				if (commPort instanceof SerialPort) {
					SerialPort serialPort = (SerialPort) commPort;
					serialPort.setSerialPortParams(getBaudrate(), SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
					
					InputStream in = serialPort.getInputStream();
					OutputStream out = serialPort.getOutputStream();
					
					serialPort.addEventListener((SerialPortEventListener) new SerialReader(in));
					serialPort.notifyOnDataAvailable(true);
				} else {
					System.out.println("Did we not connect to a serial port?");
				}
			}		
		}
	
		@Override
		public void run() {
			try {
				connect();
			} catch (Exception e) {
				System.out.println("Unable to read from serial port in new thread.");
			}
		}
	}
	
	private static class SerialReader implements SerialPortEventListener {
		private InputStream in;
		private byte[] buffer = new byte[1024]; //buffer size
		
		public SerialReader(InputStream in) {
			this.in = in;
		}
		
		public void serialEvent(SerialPortEvent evt) {
			switch(evt.getEventType()) {
				case SerialPortEvent.DATA_AVAILABLE:
					try {
						byte single = (byte)in.read();
					} catch (IOException ex) {
						System.err.println("Unable to read properly from the port");
					}
					break;
				default:
					System.out.println("Shit");
					break;
			}
		}
	}
}
