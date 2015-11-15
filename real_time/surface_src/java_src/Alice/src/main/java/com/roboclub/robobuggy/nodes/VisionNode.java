package com.roboclub.robobuggy.nodes;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.scene.effect.ImageInput;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JFrame;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import com.roboclub.robobuggy.messages.StateMessage;
import com.roboclub.robobuggy.messages.VisionMeasurement;
import com.roboclub.robobuggy.ros.Publisher;
import com.roboclub.robobuggy.ros.SensorChannel;
import com.roboclub.robobuggy.sensors.SensorState;
import com.roboclub.robobuggy.vision.testOpenCvLinking;

/**
 * Reads Images from a camera 
 * @author Trevor Decker
 *
 */
public class VisionNode extends PeriodicNode  {
	final static int IMAGE_BYTE_SIZE =  2764800;

    BufferedImage image;
    private boolean setup = false;
	testOpenCvLinking t;
	VideoCapture camera;
	Mat frame;
	int count = 0;
    JFrame w = null;
    
    FileOutputStream f;
    
    //publisher for this cameras messages
	public Publisher msgPub;
	//publisher for checking the status of this camera
	public Publisher statePub;

	FileOutputStream outputStream;
	FileInputStream inputStream;

	public VisionNode(SensorChannel sensor) {
		super(100); //runs the update at 10Hz

		msgPub = new Publisher(sensor.getMsgPath());
		statePub = new Publisher(sensor.getStatePath());
		try {
			 outputStream = new FileOutputStream("video_out", false);
			 inputStream = new FileInputStream("video_in");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

      
	}	
		
	public boolean setup(){
        t = new testOpenCvLinking();
        camera = new VideoCapture(0);
        frame = new Mat();
        return true;
	}
	
	@Override
	public boolean shutdown() {
		System.out.println("Closing camera");
        camera.release();

        try {
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	@Override
	protected void update() {
    	if(!setup){
    		setup = setup();
    	}
    		
    	  if(!camera.isOpened()){
        		statePub.publish(new StateMessage(SensorState.ERROR));
          }else if (camera.read(frame)){
            	statePub.publish(new StateMessage(SensorState.ON));
    			image = t.MatToBufferedImage(frame);
    			//save the image 
    			writeImage(image,outputStream);
    			image = readImage(inputStream);

        		msgPub.publish(new VisionMeasurement(image,"cam",count));
          }
    	}
	
	public void writeImage(BufferedImage image,FileOutputStream outputStream){
		//for writing images
		WritableRaster raster = image.getRaster();
		DataBufferByte Db   = (DataBufferByte) raster.getDataBuffer();
		byte[] data = Db.getData();
		System.out.println(image.getWidth());   //TODO send in image format
		System.out.println(image.getHeight());  //TODO send in image format
		System.out.println(data.length);        //TODO send in image format
		try {
			outputStream.write(data);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	//for reading images
	public BufferedImage readImage(FileInputStream inputStream){
    			byte[] b = new byte[IMAGE_BYTE_SIZE];
    			int readBytes;
				try {
					readBytes = inputStream.read(b, 0, IMAGE_BYTE_SIZE); //TODO make fault tolerant 
	    			System.out.println("read:"+readBytes);
	    			DataBufferByte Db = new DataBufferByte(b, IMAGE_BYTE_SIZE);
	    			WritableRaster w = Raster.createInterleavedRaster(Db, image.getWidth(), image.getHeight(), 3 * image.getWidth(), 3, new int[]{0, 1, 2}, (Point) null);
	    			BufferedImage newImage = new BufferedImage(image.getColorModel(), w, false, null);
	    			return newImage;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
	}
	
}
