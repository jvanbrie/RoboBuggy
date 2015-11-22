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
	final static int IMAGE_BYTE_SIZE =  2764800;  //TODO get ride of magic number
	static ColorModel CM;
	FileInputStream inputStream; 
	
	public VideoReader(String videoFile) throws FileNotFoundException{
		inputStream	= new FileInputStream(videoFile);	
	}

	
	
	//for reading images
		public  BufferedImage readImage(){
	    			byte[] b = new byte[IMAGE_BYTE_SIZE+8];
	    			int readBytes;
					try {
						//unpack the image 
						readBytes = inputStream.read(b, 0, IMAGE_BYTE_SIZE+8); //TODO make fault tolerant 
						//package is width, height, then image
						byte[] widthBytes = new byte[4];
						byte[] heightBytes = new byte[4];
						byte[] imageBytes = new byte[IMAGE_BYTE_SIZE];
						System.arraycopy(b, 0, widthBytes,0, widthBytes.length);
						System.arraycopy(b, 4, heightBytes,0, heightBytes.length);
						System.arraycopy(b, 8, imageBytes,0, IMAGE_BYTE_SIZE);
						int width = toInt(widthBytes);
						int height = toInt(heightBytes);
						ColorModel CM = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), false, false, 1, 0) ;



											
		    			DataBufferByte Db = new DataBufferByte(b, IMAGE_BYTE_SIZE);
		    			WritableRaster w = Raster.createInterleavedRaster(Db, width, height, 3 * width, 3, new int[]{0, 1, 2}, (Point) null);
		    			BufferedImage newImage = new BufferedImage(CM, w, false, null);
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
