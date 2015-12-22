package com.roboclub.robobuggy.ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.roboclub.robobuggy.main.Robot;
import com.roboclub.robobuggy.ros.SensorChannel;

public class ComputerVisionUI extends JFrame{

	static ComputerVisionUI instance = null;
	public static ComputerVisionUI getInstance() {
		// TODO Auto-generated method stub
		if(instance == null){
			instance = new ComputerVisionUI();
		}
		return instance;
	}
	
	private ComputerVisionUI(){
		this.setTitle("RoboBuggy Interface");
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		Container pane = this.getContentPane();
		pane.setLayout(new GridBagLayout());
		pane.setBackground(Color.DARK_GRAY);

		try {
			this.setIconImage(ImageIO.read(new File("images/rc_logo.png")));
		} catch (Exception e) {
			System.out.println("Unable to read icon image!");
		}
		
		// Close ports and close window upon exit
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				try {
					Robot.ShutDown();
				}
				catch(NullPointerException e) {
					e.printStackTrace();
				}
			}
		});
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 0.50;
		gbc.weighty = 1.0;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		ImagePanel leftPanel = new ImagePanel(600,600,SensorChannel.VISION.getMsgPath());
		pane.add(leftPanel, gbc);
		
		gbc.weightx = 0.50;
		gbc.gridx = 1;
		gbc.gridy = 0;
		ImagePanel rightPanel = new ImagePanel(600,600,SensorChannel.IMAGE_CLASSIFICATION.getMsgPath());
		pane.add(rightPanel, gbc);
		
		
		this.pack();
		this.setVisible(true);
	}

}
