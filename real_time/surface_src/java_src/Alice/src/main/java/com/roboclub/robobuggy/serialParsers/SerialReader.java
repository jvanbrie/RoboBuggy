package com.roboclub.robobuggy.serialParsers;

import java.io.InputStream;

import com.roboclub.robobuggy.ros.Publisher;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/* Event handler base class for serial read messages */
public class SerialReader implements SerialPortEventListener, SerialParser {

	private InputStream in;
	private Publisher msgPub;
	private Publisher statePub;
	private byte[] buffer = new byte[1024]; //buffer size
	
	public SerialReader() {
	}
	
	public void parse() {
		System.out.println(buffer + "\n");
	}
	
	@Override
	public void serialEvent(SerialPortEvent evt) {
		switch(evt.getEventType()) {
			case SerialPortEvent.DATA_AVAILABLE:
				try {
					in.read(buffer);
					parse();
				} catch (Exception e) {
					System.out.println("Error reading from the port");
				}
				
				break;
			default:
				System.out.println("Shit");
				break;
		}
	}
	
	/* Setters */
	public void setInputStream(InputStream in) {
		this.in = in;
	}
	
	public void setMsgChannel(Publisher pub) {
		this.msgPub = pub;
	}
	
	public void setStateChannel(Publisher pub) {
		this.statePub = pub;
	}
}