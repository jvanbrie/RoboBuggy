package com.roboclub.robobuggy.tests;

import java.util.Date;

import com.roboclub.robobuggy.messages.DriveControlMessage;
import com.roboclub.robobuggy.ros.Publisher;
import com.roboclub.robobuggy.ros.SensorChannel;

public class SerialMessagingTest {
	
	public static Publisher p;
	
	public static double[] steeringAngles = { -4.0, -2.0, -3.0, -1.0, 0, 1.0, 3.0, 2.0, 4.0 };

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub

		p = new Publisher(SensorChannel.DRIVE_CTRL.getMsgPath());
		
		for(double d : steeringAngles) {
			DriveControlMessage m = new DriveControlMessage(new Date(), d);
			Thread.sleep(2000);
			p.publish(m);
			System.out.println("tried to send " + d + "to the buggy");
		}
		
	}

}
