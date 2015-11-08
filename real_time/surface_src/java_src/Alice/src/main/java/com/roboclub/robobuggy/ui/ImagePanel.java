package com.roboclub.robobuggy.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.roboclub.robobuggy.messages.VisionMeasurement;
import com.roboclub.robobuggy.ros.Message;
import com.roboclub.robobuggy.ros.MessageListener;
import com.roboclub.robobuggy.ros.SensorChannel;
import com.roboclub.robobuggy.ros.Subscriber;

public class ImagePanel extends JPanel{
	private boolean setup = false;
	BufferedImage img = null;
	int frameWidth = 200;
	int frameHeight = 200;
	
	
	public ImagePanel() {

	// Subscriber for vision updates
	new Subscriber(SensorChannel.VISION.getMsgPath(), new MessageListener() {
		@Override
		public void actionPerformed(String topicName, Message m) {
			VisionMeasurement vm = (VisionMeasurement)m;
			img = vm.ToBufferedImage();		
			repaint();
		}
	});
	}
	
	public boolean setup(){
		try {
			img = ImageIO.read(new File("images/rc_logo.png"));
		} catch (Exception e) {
			System.out.println("Unable to read icon image!");
		}
		return true;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (!setup){
			setup = setup();
		}
		Graphics2D g2d = (Graphics2D) g.create();
		g.drawImage(img, 0, 0, frameWidth, frameHeight, Color.black, null);		
		g2d.dispose();
	}
}
