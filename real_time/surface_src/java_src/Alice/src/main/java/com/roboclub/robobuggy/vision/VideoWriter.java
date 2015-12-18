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
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.roboclub.robobuggy.main.MessageLevel;
import com.roboclub.robobuggy.main.RobobuggyLogicException;

/**
 * This is a custom native java video writer reader, it creates a output b
 * @author Trevor Decker
 *
 */
public class VideoWriter {
	static ColorModel CM;
	FileOutputStream outputStream;
	
	public VideoWriter(String fileName) throws FileNotFoundException{
		outputStream  = new FileOutputStream(fileName, false);
	}
	
	//this method writes the bufferedImage image to the video file 
	public void writeImage(BufferedImage image){
		
		int[][] thisImageRGB = new int[image.getWidth()][image.getHeight()];
		byte[] newimageData = new byte[4*image.getWidth()*image.getHeight()];
		for(int j = 0;j<image.getWidth();j++){
			for(int i = 0;i<image.getHeight();i++){
				thisImageRGB[j][i] = image.getRGB(j, i);
				int val = image.getRGB(j, i);
				byte[] vals = ByteBuffer.allocate(4).putInt(val).array();
				newimageData[i*4*image.getWidth()+j*4+0] = vals[0];
				newimageData[i*4*image.getWidth()+j*4+1] = vals[1];
				newimageData[i*4*image.getWidth()+j*4+2] = vals[2];
				newimageData[i*4*image.getWidth()+j*4+3] = vals[3];
			}
		}
		
		/*
		//function for converting byte stream of image to rgb ints
		for(int j = 0;j<image.getWidth();j++){
			for(int i = 0;i<image.getHeight();i++){
				int r = 0;
				int a = newimageData[i*4*image.getWidth()+j*4+0];
				int b = newimageData[i*4*image.getWidth()+j*4+1];
				int c = newimageData[i*4*image.getWidth()+j*4+2];
				int d = newimageData[i*4*image.getWidth()+j*4+3];
				
				//converts bytes form signed to unsigned
				if(a < 0){
					a = a + 256;
				}
				if(b < 0){
					b = b+256;
				}
				if(c < 0){
					c = c+256;
				}
				if(d < 0){
					d = d+256;
				}
				
				 r = r | d;
				 r = r | (c<<8);
				 r = r | (b<<16);
				 r = r | (a<<24);			
				if(thisImageRGB[j][i] != r){
					System.out.println("we have a problem");
					System.out.println(a +","+b+","+c+","+d);
					System.out.println(((thisImageRGB[j][i]>>24)&0xff) +","+((thisImageRGB[j][i]>>16)&0xff)+" "+((thisImageRGB[j][i]>>8)&0xff) +","+ ((thisImageRGB[j][i])&0xff));
					System.out.println(thisImageRGB[j][i]);
					System.out.println(r);
				}
			}
		}
		*/
		
		
		
		
		
		
		BufferedImage image_to_save2=new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
		image_to_save2.getGraphics().drawImage(image,0,0,null);
		image = image_to_save2;
		
		//for writing images
		WritableRaster raster = image.getRaster();
		DataBufferByte Db   = (DataBufferByte) raster.getDataBuffer();
		byte[] imageData = newimageData;//Db.getData();
		byte[] widthBytes = ByteBuffer.allocate(4).putInt(image.getWidth()).array();
		byte[] heightBytes = ByteBuffer.allocate(4).putInt(image.getHeight()).array();
		CM = image.getColorModel();
		
		//packs the image data for sending 
		byte[] sendData = new byte[widthBytes.length+heightBytes.length+imageData.length];
		//packs width, then height, then imageData
	 	System.arraycopy(widthBytes, 0, sendData,0, widthBytes.length);
		System.arraycopy(heightBytes, 0, sendData,4, heightBytes.length);
		System.arraycopy(imageData, 0, sendData,8, imageData.length);
		byte[] imageSize = ByteBuffer.allocate(4).putInt(imageData.length).array();

		try {
			outputStream.write(imageSize);
			outputStream.write(sendData);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	
	
	public void close(){
		 try {
				outputStream.close();
			} catch (IOException e) {
				new RobobuggyLogicException("Error closing Video Writer:"+e.toString(), MessageLevel.EXCEPTION);
			}
	}
	
}
