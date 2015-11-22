package com.roboclub.robobuggy.vision;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import com.roboclub.robobuggy.logging.RobotLogger;
import com.roboclub.robobuggy.messages.StateMessage;
import com.roboclub.robobuggy.messages.VisionMeasurement;
import com.roboclub.robobuggy.sensors.SensorState;

public class testVideoSaving {

	public static void main(String[] args) throws FileNotFoundException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);  //note this can only be called once, is to allow open cv calls

		//create objects for getting and storing data
		VideoCapture camera = new VideoCapture(0);
		Mat frame = new Mat();
	    BufferedImage image;
		VideoWriter vWriterPlayer = new VideoWriter("testVideo");
		
		
		testOpenCvLinking t = new testOpenCvLinking();
		FileOutputStream outputStream;
		boolean logDirSet = false;

		if(!camera.isOpened()){
			  System.out.println("cam not open");
        	}else if (camera.read(frame)){
        		image = t.MatToBufferedImage(frame);
        		//save the image 
        		vWriterPlayer.CM = image.getColorModel(); //THIS IS A HACCK 
  			
        		//will only create a video file when logfile is started
  	        	try {
  	        		if(!logDirSet){
  	       			 outputStream = new FileOutputStream("testvideo", false);
  	       			 logDirSet = true;    	
  	       			 vWriterPlayer.writeImage(image); 
  	        		}
  	       			System.out.println("image added");
  	       		} catch (FileNotFoundException e) {
  	       			// TODO Auto-generated catch block
  	       			e.printStackTrace();
  	       		}//try catch
        	}//else if
		}//function

}//class
