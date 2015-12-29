package com.roboclub.robobuggy.vision;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.util.Pair;

import javax.imageio.ImageIO;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.ml.Ml;
import org.opencv.ml.SVM;

import com.roboclub.robobuggy.main.MessageLevel;
import com.roboclub.robobuggy.main.RobobuggyLogicException;
import com.roboclub.robobuggy.main.config;

public class SuperpixelSupervisedTrainerFromLogs {
	public SVM isYellowSVM;
	
	public static void main(String [] args){
		try {
			config.setupJNI();
		} catch (NoSuchFieldException | SecurityException
				| IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SuperpixelSupervisedTrainerFromLogs test = new SuperpixelSupervisedTrainerFromLogs();
		Mat testVector = new Mat(1,3,CvType.CV_32F);
		testVector.put(0, 0, 0.0);
		testVector.put(0, 1, 0.0);
		testVector.put(0, 2, 0.0);
		float result = test.isYellowSVM.predict(testVector);
		System.out.println(result);
		testVector.put(0, 0, 0.6666666865348816);
		testVector.put(0, 1, 1.0);
		testVector.put(0, 2, 0.003921568859368563);
		result = test.isYellowSVM.predict(testVector);
		System.out.println(result);
		
	}
	
	public SuperpixelSupervisedTrainerFromLogs(){
	//read each file in the data folder structure 
	File dataFolder = new File(config.SUPER_PIXEL_TRAINING_DATA_PATH);
	File[] videoRecords = dataFolder.listFiles(new FilenameFilter() {
		  @Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).isDirectory();
		  }
		});
	

	ArrayList<Pair<double[],SuperPixelLabels>> DataPoints = new ArrayList<Pair<double[],SuperPixelLabels>>();
	for(int i = 0;i<videoRecords.length;i++){
		File thisVideoRecord = videoRecords[i];
		File[] imageRecords = thisVideoRecord.listFiles();
		for(int index = 0;index<imageRecords.length;index++){
			File thisImageRecord = imageRecords[index];
			File[] thisImageLabels = thisImageRecord.listFiles();
			if(thisImageLabels != null){
				for(int label_id = 0;label_id<thisImageLabels.length;label_id++){
					//if the file ends with .txt then it is a data point
					if(thisImageLabels[label_id].getName().endsWith(".txt")){
						Pair<double[],SuperPixelLabels> thisDataPoint = parseDataPoint(thisImageLabels[label_id]);
						if(thisDataPoint != null){
							DataPoints.add(thisDataPoint);
						}
					}
				}
			}
			
		}
	}
	
	//note that this function can be rewritten to be more memory efficient
	
	int numSamples = DataPoints.size();
	int descriptorLength = 3;
	isYellowSVM = SVM.create();
	Mat samples = new Mat(numSamples, descriptorLength, CvType.CV_32F);
	Mat responses = new Mat(numSamples,1,CvType.CV_32SC1);
	
	//populate samples and responses for training 
	for(int sampleNumber = 0;sampleNumber<numSamples;sampleNumber++){
		double[] thisDescriptor = DataPoints.get(sampleNumber).getKey();
		SuperPixelLabels thisLabel = DataPoints.get(sampleNumber).getValue();
		//populate descriptor 
		for(int i = 0;i<thisDescriptor.length;i++){
			samples.put(sampleNumber, i, thisDescriptor[i]);
		}
		
		//populate label 
		int response = 0;
		if(thisLabel == SuperPixelLabels.YELLOW_ROAD_MARKING){
			response = 1;
		}
	
		
		System.out.println(thisLabel+" "+response+" "+thisDescriptor[0]+" "+thisDescriptor[1]+" "+thisDescriptor[2]);
		responses.put(sampleNumber, 0, response);
		
		
	}
	
	
	
	//run this data point through the descriptor generator 
	//add data point to data structure
	//train
	System.out.println("Starting to train");
	isYellowSVM.train(samples, Ml.ROW_SAMPLE, responses);
	System.out.println("Finished to train");

	
	}
	
	private Pair<double[],SuperPixelLabels>  parseDataPoint(File aDataPoint){
		try {
			BufferedImage img = ImageIO.read(new File(aDataPoint.getParentFile().getPath()+"/image.jpg"));
			int[][] rgbs = new int[img.getWidth()][img.getHeight()];
			for(int i = 0;i<img.getWidth();i++){
				for(int j = 0;j<img.getHeight();j++){
					rgbs[i][j] = img.getRGB(i, j);
				}
			}
			
			Scanner in = new Scanner(new FileReader(aDataPoint));
			String Xs = in.nextLine();
			String Ys = in.nextLine();
			String label_str = in.nextLine().trim();
			label_str = label_str.substring(6, label_str.length());//removeing the header
			
			String[] Xs_ = Xs.split("\t");
			String[] Ys_ = Ys.split("\t");
			if(Xs_.length != Ys_.length){
				new RobobuggyLogicException("data point is corrupted in super pixel", MessageLevel.EXCEPTION);
			}
			ArrayList<Pair<Integer,Integer>> thisGroup = new ArrayList<Pair<Integer,Integer>>();
			
			//TODO do stuff with the data 
			for(int i = 1;i<Xs_.length;i++){
				int X = Integer.parseInt(Xs_[i]);
				int Y = Integer.parseInt(Ys_[i]);
				thisGroup.add(new Pair<Integer,Integer>(Y,X));
			}
			
			 double[] thisDescriptor = visionUtil.GenerateImageGroupDescriptor(thisGroup,rgbs);
			 SuperPixelLabels label = SuperPixelLabels.fromString(label_str);
			 return new Pair<double[], SuperPixelLabels>(thisDescriptor, label);

			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	
	}
	
}
