package com.roboclub.robobuggy.vision;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.Point;



//this class is based on the SLIC algorithm (supperpixel_PAMI2011-2)
//NOTE this class is Trevors implemention that not currently being used
public class SuperPixels {

	private double Normilisation_spacial;
	private double Normilisation_color;
	private float[][][] image = null;
	
	//segments the image into a w x h super pixels 
	public BufferedImage segment(BufferedImage input,int w,int h){
		//converts the input image to hsv
		image =  new float[input.getWidth()][input.getHeight()][3];
		for(int i = 0;i<input.getWidth();i++){
			for(int j = 0;j<input.getHeight();j++){
				float[] hsv = new float[3];
				int rgb = input.getRGB(i, j);
				Color.RGBtoHSB(rgb&0xff,(rgb>>8)&0xff,(rgb>>16)&0xff,hsv);
				image[i][j][0] = hsv[0];
				image[i][j][1] = hsv[1];
				image[i][j][2] = hsv[2];
			}
		}
		
		//input;
		double errorThreshold = 10.0;
		double error = Double.MAX_VALUE;
		int regionWidth = input.getWidth()/w;     //the width in pixels of each region
		int regionHeight = input.getHeight()/h;   //the height in pixels of each region
		int numPixels = w*h;
		 Normilisation_spacial = regionWidth; 
		 Normilisation_color  = .1; //a tunable value
		
		System.out.println("starting super pixel");
		//inits the centers of the clusters 
		//compute new cluster center
		double[] xs = new double[numPixels];
		double[] ys = new double[numPixels];
		double[] hs = new double[numPixels];
		double[] ss = new double[numPixels];
		double[] vs = new double[numPixels];
		int[] count = new int[numPixels];
			
		
		//sets the initial super pixels
		int centerIndex = 0;
		for(int x = 0;x<w;x++){
			for(int y = 0;y<h;y++){
				xs[centerIndex] = regionWidth/2 + x*regionWidth;
				ys[centerIndex] = regionHeight/2 + y*regionHeight;
		//		System.out.println("start"+xs[centerIndex]+":"+ys[centerIndex]);
				hs[centerIndex] = image[(int) xs[centerIndex]][(int) ys[centerIndex]][0];
				ss[centerIndex] = image[(int) xs[centerIndex]][(int) ys[centerIndex]][1];
				vs[centerIndex] = image[(int) xs[centerIndex]][(int) ys[centerIndex]][2];
				centerIndex++;	
			}
		}

		
		//set the initial values of each label
		double[][] d = new double[input.getWidth()][input.getHeight()];//distince of what super pixel each pixel belongs to 
		int[][] l = new int[input.getWidth()][input.getHeight()];//label of what super pixel each pixel belongs to
		for(int i = 0;i<input.getWidth();i++){
			for(int j =0;j<input.getHeight();j++){
				d[i][j] = Double.MAX_VALUE;
				l[i][j] = -1;
			}
		}
		
		
		//for loop will run for a fixed number of iterations
		//or the errorThreshold is reached, which ever is sooner 
		for(int i = 0;i<10||error <errorThreshold;i++){
			for(int clusterCenterIndex = 0; clusterCenterIndex < numPixels;clusterCenterIndex++){
				//note this piece of code could be parallelized pretty easily 
				for(int dx = -regionWidth;dx<regionWidth;dx++){	
					for(int dy = -regionHeight;dy<regionHeight;dy++){
						int x = (int) (xs[clusterCenterIndex]+dx);
						int y = (int) (ys[clusterCenterIndex]+dy);
						if(x >= 0 && x< input.getWidth() && y >= 0 && y<input.getHeight()){
							double D = calcDistince(xs[clusterCenterIndex],ys[clusterCenterIndex],hs[clusterCenterIndex],ss[clusterCenterIndex],vs[clusterCenterIndex],
												x,y,image[x][y][0],image[x][y][1],image[x][y][2]);
							if(D < d[x][y]){
								d[x][y] = D;
								l[x][y] = clusterCenterIndex;							
							}//end if
						}//endif
					}
				} //end pixel search
			} //end clusterCetner for
			
			
			//make sure that all values are initialized to 0
			for(int index = 0;index<numPixels;index++){
				xs[index] = 0;
				ys[index] = 0;
				hs[index] = 0;
				ss[index] = 0;
				vs[index] = 0;
				count[index] = 0;
			}
			
			for(int x = 0; x<input.getWidth();x++){
				for(int y = 0;y<input.getHeight();y++){
					if(l[x][y] > -1){
						xs[l[x][y]] += x;
						ys[l[x][y]] += y;
						hs[l[x][y]] += image[x][y][0];
						ss[l[x][y]] += image[x][y][1];//TODO change to hsv
						vs[l[x][y]] += image[x][y][2];//TODO change to hsv
						count[l[x][y]] += 1;
					}
				}
			}
			
			
			
			//find mean of values
			for(int index = 0;index<numPixels;index++){
				xs[index] = xs[index]/count[index];
				ys[index] = ys[index]/count[index];
				hs[index] = hs[index]/count[index];
				ss[index] = ss[index]/count[index];
				vs[index] = vs[index]/count[index];
			}
			//todo compute residual error to end early 

		}//end iteration loop
		System.out.println("ending super pixel");
	
		//assign super pixels
		BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());
		for(int i = 0;i<input.getWidth();i++){
			for(int j = 0;j<input.getHeight();j++){
				if(l[i][j] > -1){
				int x = (int)Math.floor(xs[l[i][j]]);
				int y = (int)Math.floor(ys[l[i][j]]);
				output.setRGB(i, j, input.getRGB(x,y));//input.getRGB(x,y));
				}
			}
		}

		return output;
	}
	
	
	
	public double calcDistince(double A_x,double A_y,double A_h,double A_s,double A_v,double B_x,double B_y,double B_h,double B_s,double B_v){

		double distince_color = Math.sqrt(Math.pow(A_h-B_h, 2)+Math.pow((A_s-B_s), 2)+Math.pow(A_v-B_v, 2));
		double distince_space = Math.sqrt(Math.pow(A_x -B_x,2) + Math.pow(A_y - B_y, 2));
		return Math.sqrt(Math.pow(distince_color,2) + Math.pow(distince_space/Normilisation_spacial,2)*Math.pow(Normilisation_color, 2));
	}
	
}
