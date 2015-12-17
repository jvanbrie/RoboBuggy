package com.roboclub.robobuggy.nodes;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.swing.JFrame;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import com.orsoncharts.util.json.JSONObject;
import com.roboclub.robobuggy.logging.RobotLogger;
import com.roboclub.robobuggy.main.Robot;
import com.roboclub.robobuggy.messages.StateMessage;
import com.roboclub.robobuggy.messages.VisionMeasurement;
import com.roboclub.robobuggy.ros.Publisher;
import com.roboclub.robobuggy.ros.SensorChannel;
import com.roboclub.robobuggy.sensors.SensorState;
import com.roboclub.robobuggy.vision.VideoWriter;
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
    boolean logDirSet = false;
    
    FileOutputStream f;
    
    //publisher for this cameras messages
	public Publisher msgPub;
	//publisher for checking the status of this camera
	public Publisher statePub;

	VideoWriter vWriter;


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
	
	@Override
	public boolean shutdown() {
		System.out.println("Closing camera");
        camera.release();

       if(vWriter != null){
    	   vWriter.close();
       }
       
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
    			//save the image 
    			
    			//will only create a video file when logfile is started
    	    	if(RobotLogger.logFolder != null ){
    	        	try {
    	        		if(!logDirSet){
    	        			logDirSet = true;  
    	        			vWriter = new VideoWriter(RobotLogger.logFolder+"/video");
    	        		}
    	       			vWriter.writeImage(image);
    	       		} catch (FileNotFoundException e) {
    	       			// TODO Auto-generated catch block
    	       			e.printStackTrace();
    	       		}
    	        		
    	        	}
    	    	//to stop camera from fighting with play back video feed
    	    	if(!Robot.isPlayBack){
    	    		msgPub.publish(new VisionMeasurement(image,"cam",count));
    	    	}
          }
    	}
	

	
	public static JSONObject translatePeelMessageToJObject(String message) {
		// TODO Auto-generated method stub
		
		// message has it organized as yaw pitch roll
		JSONObject data = new JSONObject();
		JSONObject params = new JSONObject();
		String[] ypr = message.split(",");
		//0 and 1 will be the name and time
		data.put("timestamp", ypr[1]);
		data.put("name", "Vision");
		data.put("params", params);
		return data;
	}
	
}
