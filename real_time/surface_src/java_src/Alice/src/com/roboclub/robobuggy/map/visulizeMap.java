package com.roboclub.robobuggy.map;

import java.awt.Container;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/** 
 * library for visulizing the map
 * Author: Trevor Decker
 * 
 *  Class for creating a JPanel with a map displayed on it
 */
public class VisulizeMap{
	private JFrame frame = new JFrame();
	private Map thisMap; 
	
	//constructor for map visulizer
	/***
	 * TODO document
	 * TODO implment 
	 */
	public VisulizeMap(Map theMap){
		thisMap = theMap;
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true); //TODO move this to an option so that the jframe can be created even if it is not displayed 
		//call updateDisplay which will redraw the map
		updateDisplay();
	}
	
	/***
	 * updates all of the drawing based on the current state of the map 
	 * TODO implment
	 */
	public void updateDisplay(){
		//TODO consider only updateing the mapobjects that are currently in view 
		//TODO fix frame so that objects don't overlap 
		
		
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(null);
		
		Point orgin = new Point(0,0);
		MapObject[] mapObjects = thisMap.getClosestNObjects(orgin,thisMap.getNumberOfElements());
		for (MapObject thisMapObject : mapObjects) {
			frame.add(thisMapObject);  //TODO update objects that are already on the frame 
		}
	}
	
	
	//gets the JFrame for the map 
	/*** 
	 * TDOO document 
	 * TODO impment 
	 * @return
	 */
	public JFrame getJFrame(){
		return frame;
	}
	

	
	
}

