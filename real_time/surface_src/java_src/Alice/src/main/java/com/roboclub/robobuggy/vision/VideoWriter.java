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
	
	public void writeImage(BufferedImage image){
		BufferedImage image_to_save2=new BufferedImage(image.getWidth(),image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		image_to_save2.getGraphics().drawImage(image,0,0,null);
		image = image_to_save2;
		
		//for writing images
		WritableRaster raster = image.getRaster();
		DataBufferByte Db   = (DataBufferByte) raster.getDataBuffer();
		byte[] imageData = Db.getData();
		byte[] widthBytes = ByteBuffer.allocate(4).putInt(image.getWidth()).array();
		byte[] heightBytes = ByteBuffer.allocate(4).putInt(image.getHeight()).array();
		CM = image.getColorModel();
		ColorSpace CS = CM.getColorSpace();
		
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
