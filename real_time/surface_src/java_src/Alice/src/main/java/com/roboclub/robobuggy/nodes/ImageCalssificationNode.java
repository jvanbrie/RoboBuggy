package com.roboclub.robobuggy.nodes;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.video.DenseOpticalFlow;

import com.roboclub.robobuggy.messages.VisionMeasurement;
import com.roboclub.robobuggy.ros.Message;
import com.roboclub.robobuggy.ros.MessageListener;
import com.roboclub.robobuggy.ros.Node;
import com.roboclub.robobuggy.ros.Publisher;
import com.roboclub.robobuggy.ros.SensorChannel;
import com.roboclub.robobuggy.ros.Subscriber;
import com.roboclub.robobuggy.vision.SuperPixels;
import com.roboclub.robobuggy.vision.VisualOdometry;
import com.roboclub.robobuggy.vision.testOpenCvLinking;
import com.sun.corba.se.impl.orbutil.DenseIntMapImpl;
import com.sun.prism.Image;

/**
 * 
 * @author Trevor Decker
 * This node will listen to an image topic and try to classify parts of the image into different classifications 
 *
 */
public class ImageCalssificationNode implements Node{

	int count = 0;
	Mat last;
	testOpenCvLinking t;
    //publisher for this cameras messages
	private Publisher msgPub;
	
	public ImageCalssificationNode(SensorChannel sensor){
		msgPub = new Publisher(sensor.getMsgPath());
        t = new testOpenCvLinking();

		new Subscriber(SensorChannel.VISION.getMsgPath(), new MessageListener() {
			@Override
			public void actionPerformed(String topicName, Message m) {
				updateVision((VisionMeasurement)m);
			}
		});
	}
	
	@Override
	public boolean shutdown() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    java.awt.Image tmp = img.getScaledInstance(newW, newH, BufferedImage.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}  


	
	//this is the callback that will occur every time that an image is updated
	private void updateVision(VisionMeasurement m) {
		BufferedImage input = m.getBuffredImage();
		
		
		//int[] data_int = input.getRGB(0, 0, input.getWidth(), input.getHeight(), null, 0, input.getWidth());

		
		
		//cascade detector
		/*
		// Create a face detector from the cascade file in the resources
	    // directory.
	    CascadeClassifier faceDetector = new CascadeClassifier("/Users/trevordecker/Desktop/myWork/roboBuggy3/RoboBuggy/real_time/surface_src/java_src/Alice/resources/lbpcascade_profileface.xml");
		Mat image = BufferedImageToMat(input);


	    // Detect faces in the image.
	    // MatOfRect is a special container class for Rect.
	    MatOfRect faceDetections = new MatOfRect();
	    faceDetector.detectMultiScale(image, faceDetections);

	    System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
	    Rect[] faces = faceDetections.toArray();
	    for(int i = 0;i<faces.length;i++){
	    	input = addDetectionBoxes(input,faces[i].x,faces[i].y,faces[i].width,faces[i].height);
	    }
		VisionMeasurement mOut = new VisionMeasurement(input,"", -1);
		msgPub.publish(mOut);
		*/
		
	
		//code for visual odomatry 
		VisualOdometry flow = new VisualOdometry(0);
		Mat thisMat = BufferedImageToMat(input);
		Mat output = Mat.ones( 3, 3, CvType.CV_8UC1 );
		if(count>1){
			flow.calc(thisMat, last, output);
			System.out.println("something happened");
		}
		count++;
		last = thisMat;
		
		
		
		//code for super pixels, need to work on makeing less blob like 
		/*
		 count++;
		 
		if(count% 2 == 0){
		//SuperPixels sp = new SuperPixels();
		///BufferedImage input = resize(m.getBuffredImage(), 200, 200);
		//BufferedImage input = m.getBuffredImage();
		//BufferedImage output = sp.segment(input, 20, 20);
		
		//TODO fancy classification happens here 
		VisionMeasurement mOut = new VisionMeasurement(output,"", -1);
		msgPub.publish(mOut);
		}
		*/
	}
	
	
	public Mat BufferedImageToMat(BufferedImage bf){
		byte[] data = new byte[bf.getWidth()*bf.getHeight()*3];
		for(int x = 0;x<bf.getWidth();x++){
			for(int y =0;y<bf.getHeight();y++){
				data[3*y*bf.getWidth()+3*x+0] =  ((byte) (bf.getRGB(x, y) &0xff));
				data[3*y*bf.getWidth()+3*x+1] =  ((byte) (bf.getRGB(x, y)>>8 &0xff));
				data[3*y*bf.getWidth()+3*x+2] =  ((byte) (bf.getRGB(x, y)>>16 &0xff));

			}
		}
		
		Mat mat = new Mat(bf.getHeight(), bf.getWidth(), CvType.CV_8UC3);
		mat.put(0, 0, data);
		return mat;
	}
	
	//TODO rework the way that we are passing the target into this function 	
	public BufferedImage addDetectionBoxes(BufferedImage input,double center_x,double center_y,double width,double height){
		
		int left_x   = (int) Math.max(Math.min(Math.floor(center_x - width/2),input.getWidth()-1),0);
		int top_y    = (int) Math.max(Math.min(Math.floor(center_y - height/2),input.getHeight()-1),0);
		int right_x  = (int) Math.max(Math.min(Math.floor(center_x + height/2), input.getWidth()-1), 0);
		int bottom_y = (int) Math.max(Math.min(Math.floor(center_y + height/2),input.getHeight()-1),0);
	
		//draw the top bottom border
	for(int i = left_x;i<right_x;i++){
		input.setRGB(i, top_y, 255);
		input.setRGB(i, bottom_y, 255);
	}
	
	//draw the left right border
	for(int i = top_y;i<bottom_y;i++){
		input.setRGB(left_x, i, 255);
		input.setRGB(right_x, i, 255);
	}
		
		//TODO add text label 
		return input;
	}
	

}
