package com.roboclub.robobuggy.nodes;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javafx.util.Pair;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.ml.*;

import com.roboclub.robobuggy.messages.SuperPixelMessage;
import com.roboclub.robobuggy.messages.VisionMeasurement;
import com.roboclub.robobuggy.ros.Message;
import com.roboclub.robobuggy.ros.MessageListener;
import com.roboclub.robobuggy.ros.Node;
import com.roboclub.robobuggy.ros.Publisher;
import com.roboclub.robobuggy.ros.SensorChannel;
import com.roboclub.robobuggy.ros.Subscriber;
import com.roboclub.robobuggy.vision.SuperpixelSupervisedTrainerFromLogs;
import com.roboclub.robobuggy.vision.testOpenCvLinking;
import com.roboclub.robobuggy.vision.visionUtil;

/**
 * 
 * @author Trevor Decker
 * This node will listen to an image topic and try to classify parts of the image into different classifications 
 *
 */
public class ImageCalssificationNode implements Node{
	SuperpixelSupervisedTrainerFromLogs tester = new SuperpixelSupervisedTrainerFromLogs();
	int count = 0;
	Mat last;
	testOpenCvLinking t;
    //publisher for this cameras messages
	private Publisher msgPub;
	
	public ImageCalssificationNode(SensorChannel sensor){
		msgPub = new Publisher(sensor.getMsgPath());
        t = new testOpenCvLinking();

		new Subscriber(SensorChannel.SUPER_PIXEL.getMsgPath(), new MessageListener() {
			@Override
			public void actionPerformed(String topicName, Message m) {
				updateVision((SuperPixelMessage)m);
			}
		});
	}
	
	@Override
	public boolean shutdown() {
		// TODO Auto-generated method stub
		return false;
	}
	
	//this is the callback that will occur every time that an image is updated
	private void updateVision(SuperPixelMessage m) {
		
		//will publish a buffered image of all of the yellow markings 
		 BufferedImage output = new BufferedImage(m.getWidth(), m.getHeight(), BufferedImage.TYPE_INT_RGB);
		 int[][] rgbs = m.getRgbs();
		 for(int groupId = 0;groupId < m.getNumGroups();groupId++){
			 ArrayList<Pair<Integer,Integer>> thisGroup = m.getGroup(groupId);
			 //TODO a more formal kmeans classification 	
			 //this is where the keep don't keep desiesion will be made
			 
			 double[] thisDescriptor = visionUtil.GenerateImageGroupDescriptor(thisGroup,rgbs);
			 
			 if(isYellowRoadLine(thisDescriptor)){
				 for(int index = 0;index <thisGroup.size();index++){
					int y = thisGroup.get(index).getKey();
					int x = thisGroup.get(index).getValue();
					int rgb = rgbs[y][x];
					output.setRGB(x, y, rgb);
				 }
			 }
		 }
		 
		 
		 
			//TODO fancy classification happens here 
			VisionMeasurement mOut = new VisionMeasurement(output,"", -1);
			msgPub.publish(mOut);
	}
	
	private boolean isYellowRoadLine(double[] descriptor){
		int descriptorLength = 3;
		Mat test = new Mat(1,descriptorLength,CvType.CV_32F);
		test.put(0, 0, descriptor[0]);
		test.put(0, 1, descriptor[1]);
		test.put(0, 2, descriptor[2]);

		
		double result = tester.isYellowSVM.predict(test);
		
		
		return result > 0;
		

	}
	
	
	/*
	//for white
	if(hsv[0] >= 0.0 && hsv[0] <= 1.0 && 
	   hsv[1] >= 0.0	 && hsv[1] <= 0.20 &&
	   hsv[2] >= 0.5 && hsv[2] <= 1.0){
	   output.setRGB(x, y, rgb);
	}
	*/
	
	/*
	//for yellow
	if(hsv[0] > 0.5 && hsv[0] < 0.625 && 
	   hsv[1] > 0.25 && hsv[1] < 1.0 &&
	   hsv[2] > 0.5 && hsv[2] < 1.0){
		output.setRGB(x, y, rgb);
	}
	*/
	
	
	
	


	 

	}
	
