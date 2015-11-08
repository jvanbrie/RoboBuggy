package com.roboclub.robobuggy.nodes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import com.roboclub.robobuggy.ros.Node;
import com.roboclub.robobuggy.ros.SensorChannel;
import com.roboclub.robobuggy.vision.testOpenCvLinking;

public class VisionNode extends JPanel implements Node{
    BufferedImage image;

	private static final String THREAD_NAME = "Vision";
	
	public VisionNode(SensorChannel sensor) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);  //note this can only be called once 

        
        testOpenCvLinking t = new testOpenCvLinking();
        VideoCapture camera = new VideoCapture(0);
        Mat frame = new Mat();
        camera.read(frame); 

        if(!camera.isOpened()){
            System.out.println("Error");
        }
        else {  
        	 BufferedImage image;

        	 int count = 0;
             JFrame w = null;
            while(true){        
            	System.out.println("in vision");
                if (camera.read(frame)){

                    if(count == 0){
                	image = t.MatToBufferedImage(frame);
                    w = t.window(image, "Original Image", 0, 0);
                    count++;
                    }else{
                    	image = t.MatToBufferedImage(frame);
                    		w.setContentPane(new testOpenCvLinking(image));
                    		w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    		w.setTitle("oldImage");
                    		w.setSize(image.getWidth(), image.getHeight() + 30);
                    		w.setLocation(0, 0);
                    		w.setVisible(true);
                    	       
                    }
                }
            }   
        }
        camera.release();

		
	}
	
	   @Override
	    public void paint(Graphics g) {
	        g.drawImage(image, 0, 0, this);
	    }


	    public  VisionNode(BufferedImage img) {
	        image = img;
	    }   

	    //Show image on window
	    public JFrame window(BufferedImage img, String text, int x, int y) {
	        JFrame frame0 = new JFrame();
	        frame0.getContentPane().add(new testOpenCvLinking(img));
	        frame0.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame0.setTitle(text);
	        frame0.setSize(img.getWidth()/2, img.getHeight()/2 + 30);
	        frame0.setLocation(x, y);
	        frame0.setVisible(true);
	        return frame0;
	    }

	    //Load an image
	    public BufferedImage loadImage(String file) {
	        BufferedImage img;

	        try {
	            File input = new File(file);
	            img = ImageIO.read(input);

	            return img;
	        } catch (Exception e) {
	            System.out.println("erro");
	        }

	        return null;
	    }

	    //Save an image
	    public void saveImage(BufferedImage img) {        
	        try {
	            File outputfile = new File("Images/new.png");
	            ImageIO.write(img, "png", outputfile);
	        } catch (Exception e) {
	            System.out.println("error");
	        }
	    }

	    //Grayscale filter
	    public BufferedImage grayscale(BufferedImage img) {
	        for (int i = 0; i < img.getHeight(); i++) {
	            for (int j = 0; j < img.getWidth(); j++) {
	                Color c = new Color(img.getRGB(j, i));

	                int red = (int) (c.getRed() * 0.299);
	                int green = (int) (c.getGreen() * 0.587);
	                int blue = (int) (c.getBlue() * 0.114);

	                Color newColor =
	                        new Color(
	                        red + green + blue,
	                        red + green + blue,
	                        red + green + blue);

	                img.setRGB(j, i, newColor.getRGB());
	            }
	        }

	        return img;
	    }

	    public BufferedImage MatToBufferedImage(Mat frame) {
	        //Mat() to BufferedImage
	        int type = 0;
	        if (frame.channels() == 1) {
	            type = BufferedImage.TYPE_BYTE_GRAY;
	        } else if (frame.channels() == 3) {
	            type = BufferedImage.TYPE_3BYTE_BGR;
	        }
	        BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
	        WritableRaster raster = image.getRaster();
	        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
	        byte[] data = dataBuffer.getData();
	        frame.get(0, 0, data);

	        return image;
	    }

	
	
	@Override
	public boolean shutdown() {
		// TODO Auto-generated method stub
		return false;
	}

}
