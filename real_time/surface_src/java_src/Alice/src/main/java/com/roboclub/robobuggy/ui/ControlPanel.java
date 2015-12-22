package com.roboclub.robobuggy.ui;


import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import com.roboclub.robobuggy.logging.RobotLogger;
import com.roboclub.robobuggy.logging.automaticLogging.AutomaticLogging;
import com.roboclub.robobuggy.main.MessageLevel;
import com.roboclub.robobuggy.main.RobobuggyLogicException;
import com.roboclub.robobuggy.main.Robot;
import com.roboclub.robobuggy.main.config;
import com.roboclub.robobuggy.messages.GuiLoggingButtonMessage;
import com.roboclub.robobuggy.messages.TimeMessage;
import com.roboclub.robobuggy.ros.Message;
import com.roboclub.robobuggy.ros.MessageListener;
import com.roboclub.robobuggy.ros.Publisher;
import com.roboclub.robobuggy.ros.SensorChannel;
import com.roboclub.robobuggy.ros.Subscriber;
import com.roboclub.robobuggy.simulation.SensorPlayer;

/**
 * 
 * @author Trevor Decker
 * @author Kevin Brennan
 *
 * @version 0.5
 * 
 *          CHANGELOG: NONE
 * 
 *          DESCRIPTION: TODO
 */

public class ControlPanel extends JPanel {
	private static final long serialVersionUID = -924045896215455343L;
	private static final int TIME_ZONE_OFFSET = 18000000;//5 hours

	private static JButton play_btn;
	private static JButton loadLog_btn;
	private JFormattedTextField time_lbl;

	SensorSwitch gps_switch;
	SensorSwitch vision_switch;
	SensorSwitch encoders_switch;
	SensorSwitch imu_switch;
	SensorSwitch controls_switch;
	SensorSwitch autonomous_switch;
	JButton display;

	Publisher logging_button_pub;
	
	public ControlPanel() {
		logging_button_pub = new Publisher(Gui.GuiPubSubTopics.GUI_LOG_BUTTON_UPDATED.toString());
		
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setLayout(new GridBagLayout());
		this.setBackground(Color.DARK_GRAY);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.weighty = 0.5;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		addLoggingPanel(gbc);
		
		gbc.gridy = 1;
		addSensorSwitchPanel(gbc);
		
		
		//listens to clock messages to update the time
		new Subscriber(SensorChannel.CLOCK.getMsgPath(), new MessageListener() {
			@Override
			public void actionPerformed(String topicName, Message m) {
				updateClock((TimeMessage)m);
			}
		});		
		}
	
	private void updateClock(TimeMessage m){
		time_lbl.setValue(m.getTime() + TIME_ZONE_OFFSET);
	}

	private class PlayButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// inverts the state of the system every time the button is pressed
			config.active = !config.active;
			
			if (config.active) {
				System.out.println("System Started");
				play_btn.setBackground(Color.RED);
				play_btn.setText("STOP");
				Robot.isLogging = true;
				
				AutomaticLogging autoLoger = AutomaticLogging.getLogger();
				autoLoger.stopLogSync();
				RobotLogger.CreateLog();
				logging_button_pub.publish(new GuiLoggingButtonMessage(GuiLoggingButtonMessage.LoggingMessage.START));
			} else {
				System.out.println("System Paused");
				play_btn.setBackground(Color.GREEN);
				play_btn.setText("START");
				Robot.isLogging = false;
				
				RobotLogger.CloseLog();
				logging_button_pub.publish(new GuiLoggingButtonMessage(GuiLoggingButtonMessage.LoggingMessage.STOP));
			}
		}
	}
	
	
	private class LoadLogButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(config.active){
				new RobobuggyLogicException("tried to log an error when logging, isgnoreing command", MessageLevel.WARNING);
				return;
			}
			
			//have the user choose the log to play
			JFileChooser chooser = new JFileChooser();
		    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    chooser.setCurrentDirectory(new java.io.File(config.LOG_FILE_LOCATION));
		    int returnVal = chooser.showOpenDialog(getParent());
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		       System.out.println("You chose to open this file: " +
		            chooser.getSelectedFile().getName());
		       		String logFolder = chooser.getSelectedFile().getPath();
		       		SensorPlayer sp = new SensorPlayer(logFolder+"/sensors.txt");
		        	new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							sp.run();
						}
					}).start();
		       		//TODO test to see if selected folder is a log 
		       		//if it is then play it back
		       		//otherwise throw an error 
		        	//TODO remove sensor palyer when it is done 
		       
		    }else{
		    	new RobobuggyLogicException("You need to choose a log folder for play back", MessageLevel.WARNING);
		    }
		}
	}

	private void addLoggingPanel(GridBagConstraints gbc) {
		JPanel loggingPanel = new JPanel();
		loggingPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		loggingPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc.weightx = 1.0;
		
		//setup start stop button
		play_btn = new JButton("START");
		play_btn.setFont(new Font("serif", Font.PLAIN, 70));
		play_btn.addActionListener(new PlayButtonHandler());
		play_btn.setEnabled(false);
		play_btn.setBackground(Color.BLUE);
		
		//setup button for loading log files for playback 
		loadLog_btn = new JButton("Load Log");
		loadLog_btn.setFont(new Font("serif", Font.PLAIN, 70));
		loadLog_btn.addActionListener(new LoadLogButtonHandler());
		loadLog_btn.setEnabled(true);
		loadLog_btn.setBackground(Color.BLUE);
		
		JLabel filename_lbl = new JLabel("File: ",
				SwingConstants.CENTER);
		filename_lbl.setFont(new Font("sanserif", Font.PLAIN, 30));

		time_lbl = new JFormattedTextField(new SimpleDateFormat("HH:mm:ss.S"));
		time_lbl.setHorizontalAlignment(SwingConstants.CENTER);
		time_lbl.setFont(new Font("sanserif", Font.PLAIN, 50));
		time_lbl.setEditable(false);
		time_lbl.setColumns(7);

		gbc_panel.weightx = 1;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		gbc_panel.weighty = 0.5;
		loggingPanel.add(play_btn, gbc_panel);
		
		gbc_panel.gridy = 1;
		gbc_panel.weighty = 0.25;
		loggingPanel.add(loadLog_btn, gbc_panel);

		gbc_panel.gridy = 2;
		gbc_panel.weighty = 0.25;
		loggingPanel.add(time_lbl, gbc_panel);
		
		this.add(loggingPanel, gbc);
	}
	
	private void addSensorSwitchPanel(GridBagConstraints gbc) {
		JPanel switchPanel = new JPanel();
		switchPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		switchPanel.setLayout(new GridLayout(7,1));
		
		gps_switch = new SensorSwitch("GPS", SensorChannel.GPS);
		vision_switch = new SensorSwitch("VISION", SensorChannel.VISION);
		encoders_switch = new SensorSwitch("ENCODERS", SensorChannel.ENCODER);
		imu_switch = new SensorSwitch("IMU", SensorChannel.IMU);
		controls_switch = new SensorSwitch("CONTROLS", SensorChannel.DRIVE_CTRL);
		autonomous_switch = new SensorSwitch("AUTO", SensorChannel.AUTO);
		
		display = new JButton("DISPLAY");
		display.setBackground(Color.BLUE);
		display.setForeground(Color.WHITE);
		display.setEnabled(false);
		display.setFont(new Font("serif", Font.BOLD, 20));
		display.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		
		switchPanel.add(autonomous_switch);
		switchPanel.add(gps_switch);
		switchPanel.add(imu_switch);
		switchPanel.add(encoders_switch);
		switchPanel.add(controls_switch);
		switchPanel.add(vision_switch);
		switchPanel.add(display);
		
		this.add(switchPanel, gbc);
	}

	
	/*		Update Methods */
	public void enableLogging() {
		play_btn.setEnabled(true);
		play_btn.setBackground(Color.GREEN);
	}
}
