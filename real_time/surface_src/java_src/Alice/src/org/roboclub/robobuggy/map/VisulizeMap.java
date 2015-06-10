package org.roboclub.robobuggy.map;

import java.awt.Container;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

// TODO make dynamic when user resizes 
// TODO add scale to mapping 
// TODO add moving options
// TODO add save image 


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
		drawGUI();
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
		//	System.out.println(mapObjects[0].getClass());
		//	Image a = (Image) mapObjects[0];//.paintComponent(frame.getGraphics());
		//	a.paintComponent(frame.getGraphics());
			frame.add(thisMapObject);  //TODO update objects that are already on the frame 
		}
		frame.setSize(500, 500); //TODO make dynamic
	}
	
	public void drawGUI(){
		JButton testButton = new JButton("this is a test");
		int x = frame.getWidth() - 100;
		testButton.setBounds(x, 0, 100, 100);
		frame.add(testButton);
		
		
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

