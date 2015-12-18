package com.roboclub.robobuggy.vision;

import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class VideoReader {
	//final static int IMAGE_BYTE_SIZE =  2764800;  //TODO get ride of magic number
	static ColorModel CM;
	FileInputStream inputStream; 
	
	public VideoReader(String videoFile) throws FileNotFoundException{
		inputStream	= new FileInputStream(videoFile);	
	}

	
	
	//for reading images
		public  BufferedImage readImage(){
	    			int readBytes = -1;
					try {
						//reads 4 bytes for the image size
						byte[]b1 = new byte[4];
						readBytes = inputStream.read(b1, 0, 4);
						int imageByteSize = toInt(b1);
		    			byte[] b = new byte[imageByteSize+8];
						//unpack the image 
						readBytes = inputStream.read(b, 0, imageByteSize+8); //TODO make fault tolerant 
						//package is width, height, then image
						byte[] widthBytes = new byte[4];
						byte[] heightBytes = new byte[4];
						byte[] imageBytes = new byte[imageByteSize];
						System.arraycopy(b, 0, widthBytes,0, widthBytes.length);
						System.arraycopy(b, 4, heightBytes,0, heightBytes.length);
						System.arraycopy(b, 8, imageBytes,0, imageByteSize);
						int width = toInt(widthBytes);
						int height = toInt(heightBytes);
//						ColorSpace.CS_sRGB
						ColorModel CM = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB), false, false, 1, 0) ;



											
		    			DataBufferByte Db = new DataBufferByte(b, imageByteSize);
		    			byte[] data = Db.getData();
		    			WritableRaster w = Raster.createInterleavedRaster(Db, width, height, 3 * width, 3, new int[]{0, 1, 2}, (Point) null);
		    			BufferedImage newImage = new BufferedImage(CM, w, false, null);
		    			for(int j = 0; j<width;j++){
		    				for(int i =0;i<height;i++){
		    					int rgb0 = data[i*4*width+j*4+0];
		    					int rgb1 = data[i*4*width+j*4+1];
		    					int rgb2 = data[i*4*width+j*4+2];
		    					int rgb3 = data[i*4*width+j*4+3];
		    					
		    					//converts bytes form signed to unsigned
		    					if(rgb0 < 0){
		    						rgb0 = rgb0 + 256;
		    					}
		    					if(rgb1 < 0){
		    						rgb1 = rgb1+256;
		    					}
		    					if(rgb2 < 0){
		    						rgb2 = rgb2+256;
		    					}
		    					if(rgb3 < 0){
		    						rgb3 = rgb3+256;
		    					}
		    					int r = 0;
		    					 r = r | rgb3;
		    					 r = r | (rgb2<<8);
		    					 r = r | (rgb1<<16);
		    					 r = r | (rgb0<<24);		
		    					
				    			newImage.setRGB(j, i, r);
		    				}
		    			}
		    			
		    			return newImage;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
		}
		
		private static int toInt( byte[] b )
		{
		    return (b[0] & 255) << 24 | (b[1] & 255) << 16 | (b[2] & 255) << 8 | (b[3] & 255);
		}
		
		public void close(){
			 try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
}
