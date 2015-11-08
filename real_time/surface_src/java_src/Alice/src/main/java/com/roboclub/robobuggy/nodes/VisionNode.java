package com.roboclub.robobuggy.nodes;

import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import com.roboclub.robobuggy.messages.StateMessage;
import com.roboclub.robobuggy.messages.VisionMeasurement;
import com.roboclub.robobuggy.ros.Publisher;
import com.roboclub.robobuggy.ros.SensorChannel;
import com.roboclub.robobuggy.sensors.SensorState;
import com.roboclub.robobuggy.vision.testOpenCvLinking;

/**
 * Reads Images from a camera 
 * @author Trevor Decker
 *
 */
public class VisionNode extends PeriodicNode  {
    BufferedImage image;
    private boolean setup = false;
	testOpenCvLinking t;
	VideoCapture camera;
	Mat frame;
	int count = 0;
    JFrame w = null;
    
    //publisher for this cameras messages
	public Publisher msgPub;
	//publisher for checking the status of this camera
	public Publisher statePub;
	
	public VisionNode(SensorChannel sensor) {
		super(100); //runs the update at 10Hz

		msgPub = new Publisher(sensor.getMsgPath());
		statePub = new Publisher(sensor.getStatePath());
       

      
	}	
	
	public boolean setup(){
        t = new testOpenCvLinking();
        camera = new VideoCapture(0);
        frame = new Mat();
        return true;
	}
	
	    //Show image on window
	    public JFrame window(BufferedImage img, String text, int x, int y) {
	        JFrame frame0 = new JFrame();
	        frame0.getContentPane().add(new testOpenCvLinking(img));
	        frame0.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame0.setTitle(text);
	        frame0.setSize(img.getWidth()/2, img.getHeight()/2 + 30);
	        frame0.setLocation(x, y);
	        frame0.setVisible(true);
	        return frame0;
	    }
	
	@Override
	public boolean shutdown() {
		System.out.println("Closing camera");
        camera.release();
		return false;
	}

	@Override
	protected void update() {
    	if(!setup){
    		setup = setup();
    	}
    		
    	  if(!camera.isOpened()){
        		statePub.publish(new StateMessage(SensorState.ERROR));
          }else if (camera.read(frame)){
            	statePub.publish(new StateMessage(SensorState.ON));
    			image = t.MatToBufferedImage(frame);
        		msgPub.publish(new VisionMeasurement(frame,"cam",count));
          }
    	}

}
