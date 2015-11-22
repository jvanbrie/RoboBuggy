package com.roboclub.robobuggy.ui;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.roboclub.robobuggy.map.Point;
import com.roboclub.robobuggy.messages.EncoderMeasurement;
import com.roboclub.robobuggy.messages.SteeringMeasurement;
import com.roboclub.robobuggy.ros.Message;
import com.roboclub.robobuggy.ros.MessageListener;
import com.roboclub.robobuggy.ros.SensorChannel;
import com.roboclub.robobuggy.ros.Subscriber;

public class GraphPanel extends JPanel {
	private static final long serialVersionUID = -5453262887347328140L;

	private AngleGraph steering;
	private AngleGraph roll;
	private LineGraph encoderLineGraph;
	private AngleGraph yaw;
	
	public GraphPanel() {
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setLayout(new GridLayout(1,4));
		
		steering = new AngleGraph("STEERING");
		roll = new AngleGraph("ROLL");
		encoderLineGraph = new LineGraph("Encoder distance", "Time", "Distance");
		yaw = new AngleGraph("YAW");
		
		this.add(steering);
		this.add(roll);
		this.add(encoderLineGraph);
		this.add(yaw);
		
		// Subscriber for drive control updates
		new Subscriber(SensorChannel.STEERING.getMsgPath(), new MessageListener() {
			@Override
			public void actionPerformed(String topicName, Message m) {
				steering.updateGraph(((SteeringMeasurement)m).angle);
			}
		});
		
		// Subscriber for Imu updates
		new Subscriber(SensorChannel.IMU.getMsgPath(), new MessageListener() {
			@Override
			public void actionPerformed(String topicName, Message m) {
				// TODO handle imu updates for graphs
			}
		});
		
		// Subscriber for encoder updates
		new Subscriber(SensorChannel.ENCODER.getMsgPath(), new MessageListener() {
			@Override
			public void actionPerformed(String topicName, Message m) {
				EncoderMeasurement em = (EncoderMeasurement)m;
				encoderLineGraph.updateGraph(new Point(em.timestamp.getTime(), em.distance));
			}
		});
	}
}
