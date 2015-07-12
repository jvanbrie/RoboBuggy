package org.roboclub.robobuggy.calculatedNodes;

import org.roboclub.robobuggy.messages.ImuMeasurement;
import org.roboclub.robobuggy.messages.StateMessage;
import org.roboclub.robobuggy.ros.Publisher;
import org.roboclub.robobuggy.ros.SensorChannel;
import org.roboclub.robobuggy.sensors.SensorState;

public class CalculatedIMUNode extends BaseCalculatedNode {
	
	private Publisher statePub;
	private Publisher msgPub;
	private NodeCalculator calc;
	private int delay;
	private boolean enabled = true; // need an atomic boolean or something
	private int elapsed = 0;
	
	public CalculatedIMUNode (SensorChannel sensor, NodeCalculator calc, int delay) {
		msgPub = new Publisher(sensor.getMsgPath());
		statePub = new Publisher(sensor.getStatePath());
		statePub.publish(new StateMessage(SensorState.DISCONNECTED));
		statePub.publish(new StateMessage(SensorState.ON));
		this.calc = calc;
		this.delay = delay;
		
		msgPub.publish(new ImuMeasurement(250, 250, 250));
	}
	
	public void enable() {
		this.enabled = true;
	}
	
	public void disable() {
		this.enabled = false;
	}
	
	public void crunch () {
		new Thread(new Runnable() {
			public void run() {
				while(true) {
					//I'm sensing problems can arise here ... 
					if (enabled) {
						msgPub.publish((ImuMeasurement)calc.calculator(elapsed));
					}
									
					//let's try to not run for years and years on end?
					elapsed += delay;
					
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}
