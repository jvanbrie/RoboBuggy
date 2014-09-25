package com.roboclub.robobuggy.main;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class AnalyticsWindow extends JFrame{
	private static final long serialVersionUID = 1903486493950389240L;
	
	private static GpsPanel gpsPanel;
	private static ArduinoPanel arduinoPanel;
	private static ImuPanel imuPanel;
	public DataPanel data;
	
	public AnalyticsWindow() {
		this.setTitle("Data Analyitics");
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setLayout(new BorderLayout());
		
		JPanel rightPanel = new JPanel();
		rightPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		rightPanel.setLayout(new BorderLayout());
		data = new DataPanel(this.getContentPane());
		gpsPanel = new GpsPanel();
		rightPanel.add(gpsPanel, BorderLayout.NORTH);
		rightPanel.add(data, BorderLayout.CENTER);
		this.add(rightPanel, BorderLayout.EAST);
		
		// Initialize Panels for Window
		JPanel leftPanel = new JPanel();
		leftPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		leftPanel.setLayout(new BorderLayout());
		imuPanel = new ImuPanel();
		arduinoPanel = new ArduinoPanel();
		leftPanel.add(arduinoPanel, BorderLayout.SOUTH);
		leftPanel.add(imuPanel, BorderLayout.CENTER);
		this.add(leftPanel, BorderLayout.CENTER);
		
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	setHidden();
		    }
		});
		
		this.pack();
		this.setVisible(Gui.GetGraphState());
	}
	
	private void setHidden() {
		Gui.updateGraphToggle(false);
	}
	
	public void close() {
		if (gpsPanel != null && gpsPanel.isConnected()) {
			gpsPanel.closePort();
		}
		if (arduinoPanel != null && arduinoPanel.isConnected()) {
			arduinoPanel.closePort();
		}
		if (imuPanel != null && imuPanel.isConnected()) {
			imuPanel.closePort();
		}
	}
}
