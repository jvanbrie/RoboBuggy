package com.roboclub.robobuggy.vision;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javafx.util.Pair;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class visionUtil {
	public static  Mat  BufferedImageToMat(BufferedImage bf){
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
	public static BufferedImage addDetectionBoxes(BufferedImage input,double center_x,double center_y,double width,double height){
		
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
	
	public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    BufferedImage convertedImg = new BufferedImage(newW, newH, img.getType());
	    convertedImg.getGraphics().drawImage(img.getScaledInstance(newW, newH, BufferedImage.SCALE_SMOOTH), 0, 0, null);
	    return convertedImg;
	}  
	
	public static final int DESCRIPTOR_LENGTH = 3;
	
	public static double[] GenerateImageGroupDescriptor( ArrayList<Pair<Integer,Integer>> thisGroup,int[][] rgbs){
		//todo expand the size of the descriptor 
		

		float[] hsv = new float[DESCRIPTOR_LENGTH];
		double[] descriptor = new double[3];
		descriptor[0] = 0;
		descriptor[1] = 0;
		descriptor[2] = 0;
		for(int index = 0;index<thisGroup.size();index++){
		int y = thisGroup.get(index).getKey();
		int x = thisGroup.get(index).getValue();
		int rgb = rgbs[y][x];
		Color.RGBtoHSB(rgb&0xff,(rgb>>8)&0xff,(rgb>>16)&0xff,hsv);
		descriptor[0] += hsv[0]/thisGroup.size();
		descriptor[1] += hsv[1]/thisGroup.size();
		descriptor[2] += hsv[2]/thisGroup.size();
		}
		
		
		return descriptor;
	}


}
