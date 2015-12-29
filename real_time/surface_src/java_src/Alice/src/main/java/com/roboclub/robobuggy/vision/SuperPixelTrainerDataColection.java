package com.roboclub.robobuggy.vision;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javafx.util.Pair;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import com.roboclub.robobuggy.main.config;
import com.roboclub.robobuggy.messages.SuperPixelMessage;
import com.roboclub.robobuggy.messages.VisionMeasurement;
import com.roboclub.robobuggy.nodes.CreateOverHeadView;
import com.roboclub.robobuggy.nodes.SuperPixelNode;
import com.roboclub.robobuggy.ros.Message;
import com.roboclub.robobuggy.ros.MessageListener;
import com.roboclub.robobuggy.ros.Publisher;
import com.roboclub.robobuggy.ros.SensorChannel;
import com.roboclub.robobuggy.ros.Subscriber;
import com.roboclub.robobuggy.ui.ImagePanel;

public class SuperPixelTrainerDataColection extends JFrame{
	boolean ImageDone = true;
	int groupId = -1;
	Publisher msgClassificationPub = new Publisher(SensorChannel.IMAGE_CLASSIFICATION.getMsgPath());
	static final String VIDEO_FILE_NAME = "raceDay.mp4";
	int frameNumber = 0;
	
	public SuperPixelTrainerDataColection() {
		try {
			config.setupJNI();
		} catch (NoSuchFieldException | SecurityException
				| IllegalArgumentException | IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

			
		//start up the gui
		//JFrame frame = new JFrame("Super Pixel Description Trainer");
		this.setLayout(new GridBagLayout());
		
		//add an icon
		try {
			this.setIconImage(ImageIO.read(new File(config.LOGO_PATH)));
		} catch (Exception e) {
			System.out.println("Unable to read icon image!");
		}
		
		// Close ports and close window upon exit
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				try {
					//Robot.ShutDown();
				}
				catch(NullPointerException e) {
					e.printStackTrace();
				}
			}
		});
		
		//add image of the top view and image of the super pixel side by side 
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 0.5;
		gbc.weighty = 0.8;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		ImagePanel leftPanel = new ImagePanel(600,600,SensorChannel.OVER_HEAD_IMAGE.getMsgPath());
		this.add(leftPanel,gbc);
		
		gbc.weightx = 0.5;
		gbc.weighty = 0.8;
		gbc.gridx = 1;
		gbc.gridy = 0;
		ImagePanel rightPanel = new ImagePanel(600,600,SensorChannel.IMAGE_CLASSIFICATION.getMsgPath());
		this.add(rightPanel,gbc);
		
		
		//had to split up buttons to put them in different grid cells
		JPanel bottomPanel_1 = new JPanel();
		JPanel bottomPanel_2 = new JPanel();


		
		//Add a button for each label type
		SuperPixelLabels[] lableList = SuperPixelLabels.values();
		for(int i = 0;i<SuperPixelLabels.values().length/2;i++){
			JButton thisLabelsButton = new JButton(lableList[i].toString());
			thisLabelsButton.addActionListener(new labelButtonAction(lableList[i]));
			bottomPanel_1.add(thisLabelsButton);
		}
		for(int i = SuperPixelLabels.values().length/2;i<SuperPixelLabels.values().length;i++){
			JButton thisLabelsButton = new JButton(lableList[i].toString());
			bottomPanel_2.add(thisLabelsButton);
			thisLabelsButton.addActionListener(new labelButtonAction(lableList[i]));

		}
		
		gbc.weightx = 0.5;
		gbc.weighty = 0.1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		this.add(bottomPanel_1,gbc);
		
		gbc.weightx = 0.5;
		gbc.weighty = 0.1;
		gbc.gridx = 1;
		gbc.gridy = 1;
		this.add(bottomPanel_2,gbc);
		
		JButton skipButton = new JButton("skip");
		skipButton.addActionListener(new skipAction());
		
		gbc.weightx = 0.5;
		gbc.weighty = 0.1;
		gbc.gridx = 0;
		gbc.gridy = 2;
		this.add(skipButton,gbc);
		this.pack();
		this.setVisible(true);
		this.setSize(1200, 800);
		
		//start up video writing
		testOpenCvLinking t = new testOpenCvLinking();
		VideoCapture camera = new VideoCapture(VIDEO_FILE_NAME);
		Mat imageMat = new Mat();
	    BufferedImage image;
		Publisher msgPub = new Publisher(SensorChannel.VISION.getMsgPath());

		//register callback for when a new set of super_pixels are ready
		new Subscriber(SensorChannel.SUPER_PIXEL.getMsgPath(), new MessageListener() {
			@Override
			public void actionPerformed(String topicName, Message m) {
				superPixelCallback((SuperPixelMessage)m);
			}
		});
		

        //starts up nodes 
		CreateOverHeadView overHeadNode = new CreateOverHeadView(SensorChannel.OVER_HEAD_IMAGE);
		SuperPixelNode superPixels = new SuperPixelNode(SensorChannel.SUPER_PIXEL);
		
		//skips a preset number of frames 
		for(int i = 0;i<250;i++){
			camera.read(imageMat);
			frameNumber++;
		}
		
		while(camera.read(imageMat)){
 			image = t.MatToBufferedImage(imageMat);
 		BufferedImage image2 = visionUtil.resize(image, 200, 200);
 			//publish the image 
 	    		msgPub.publish(new VisionMeasurement(image2,"cam",0));
 	         groupId = 0;
			 ImageDone = false;
			 while(ImageDone == false){
				 try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			 frameNumber++;
			 //wait until we are done with this image
			 //TODO
		}
	}
	
	SuperPixelMessage mostRecentSuperPixels;
	public void superPixelCallback(SuperPixelMessage m){
		mostRecentSuperPixels = m;
		updateVision();
	}
	
	public void updateVision(){
	BufferedImage output = new BufferedImage(mostRecentSuperPixels.getWidth(), mostRecentSuperPixels.getHeight(), BufferedImage.TYPE_INT_RGB);
	if(groupId >= mostRecentSuperPixels.getNumGroups()){
		//we have reached the end of the super pixels so we should get the next image
		ImageDone = true;
	}else{
		int rgbs[][] = mostRecentSuperPixels.getRgbs();
		ArrayList<Pair<Integer,Integer>> thisGroup = mostRecentSuperPixels.getGroup(groupId);
		for(int i = 0;i<thisGroup.size();i++){
			Pair<Integer,Integer> thisPixel = thisGroup.get(i);
			output.setRGB(thisPixel.getValue(),thisPixel.getKey(),rgbs[thisPixel.getKey()][thisPixel.getValue()]);
		}
	
	}
	msgClassificationPub.publish(new VisionMeasurement(output, "test", 0));
	}
	
	public class labelButtonAction implements ActionListener{
		SuperPixelLabels thisLabel;
		
		public labelButtonAction(SuperPixelLabels label){
			this.thisLabel = label;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("The label button was pressed for label: "+thisLabel);
			groupId++;
			// will create a folder inside the trainingData folder
			// will store the image, the addresses of this super pixel, the label of this super pixel
			File dir = new File(config.SUPER_PIXEL_TRAINING_DATA_PATH+"/"+VIDEO_FILE_NAME+"/"+frameNumber);
			if(!dir.exists()){
				dir.mkdirs();
				//saves the image
				BufferedImage overHeadImage = new BufferedImage(mostRecentSuperPixels.getWidth(), mostRecentSuperPixels.getHeight(), BufferedImage.TYPE_INT_RGB);
				int[][] rgb = mostRecentSuperPixels.getRgbs();
				for(int i = 0;i<mostRecentSuperPixels.getWidth();i++){
					for(int j =0;j<mostRecentSuperPixels.getHeight();j++){
						overHeadImage.setRGB(i, j, rgb[j][i]);
					}
				}
				
				
				 File imagefile = new File(dir.getPath()+"/image.jpg");			 
				 try {
					ImageIO.write(overHeadImage, "jpg", imagefile);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}//end dir exist if
			ArrayList<Pair<Integer,Integer>> groupPixels = mostRecentSuperPixels.getGroup(groupId);
			
			PrintWriter writer;
			try {
				writer = new PrintWriter(dir.getPath()+"/superPixel"+groupId+".txt", "UTF-8");
				writer.print("x:");
				for(int i = 0;i< groupPixels.size();i++){
					writer.print("\t"+groupPixels.get(i).getKey());
				}
				writer.println();
				writer.print("y:");
				for(int i = 0;i< groupPixels.size();i++){
					writer.print("\t"+groupPixels.get(i).getValue());
				}
				writer.println();
				writer.println("label:"+thisLabel);
				writer.close();
			} catch (FileNotFoundException | UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updateVision();
		}
		
	}
	
	public class skipAction implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("The skip button was pressed");
		groupId++;
		updateVision();
	}
	}
}
