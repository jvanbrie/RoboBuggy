package com.roboclub.robobuggy.nodes;

import georegression.struct.se.Se3_F64;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;

import org.ddogleg.struct.FastQueue;
import org.ddogleg.struct.GrowQueue_I32;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.video.DenseOpticalFlow;

import boofcv.abst.segmentation.ImageSuperpixels;
import boofcv.alg.filter.blur.GBlurImageOps;
import boofcv.alg.interpolate.TypeInterpolate;
import boofcv.alg.segmentation.ComputeRegionMeanColor;
import boofcv.alg.segmentation.ImageSegmentationOps;
import boofcv.alg.sfm.overhead.CreateSyntheticOverheadView;
import boofcv.alg.sfm.overhead.CreateSyntheticOverheadViewMS;
import boofcv.alg.sfm.overhead.SelectOverheadParameters;
import boofcv.factory.segmentation.ConfigFh04;
import boofcv.factory.segmentation.ConfigSlic;
import boofcv.factory.segmentation.FactoryImageSegmentation;
import boofcv.factory.segmentation.FactorySegmentationAlg;
import boofcv.gui.ListDisplayPanel;
import boofcv.gui.feature.VisualizeRegions;
import boofcv.gui.image.ShowImages;
import boofcv.io.UtilIO;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.calib.StereoParameters;
import boofcv.struct.feature.ColorQueue_F32;
import boofcv.struct.image.ImageBase;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageSInt32;
import boofcv.struct.image.ImageType;
import boofcv.struct.image.ImageUInt8;
import boofcv.struct.image.MultiSpectral;

import com.roboclub.robobuggy.messages.SuperPixelMessage;
import com.roboclub.robobuggy.messages.VisionMeasurement;
import com.roboclub.robobuggy.ros.Message;
import com.roboclub.robobuggy.ros.MessageListener;
import com.roboclub.robobuggy.ros.Node;
import com.roboclub.robobuggy.ros.Publisher;
import com.roboclub.robobuggy.ros.SensorChannel;
import com.roboclub.robobuggy.ros.Subscriber;
import com.roboclub.robobuggy.vision.SuperPixels;
import com.roboclub.robobuggy.vision.VisualOdometry;
import com.roboclub.robobuggy.vision.testOpenCvLinking;
import com.roboclub.robobuggy.vision.visionUtil;
import com.sun.corba.se.impl.orbutil.DenseIntMapImpl;
import com.sun.prism.Image;

/**
 * 
 * @author Trevor Decker
 * This node will listen to an image topic and try to classify parts of the image into different classifications 
 *
 */
public class SuperPixelNode implements Node{

	int count = 0;
	Mat last;
	testOpenCvLinking t;
    //publisher for this cameras messages
	private Publisher msgPub;
	
	public SuperPixelNode(SensorChannel sensor){
		msgPub = new Publisher(sensor.getMsgPath());
        t = new testOpenCvLinking();

		new Subscriber(SensorChannel.OVER_HEAD_IMAGE.getMsgPath(), new MessageListener() {
			@Override
			public void actionPerformed(String topicName, Message m) {
				updateVision((VisionMeasurement)m);
			}
		});
	}
	
	@Override
	public boolean shutdown() {
		// TODO Auto-generated method stub
		return false;
	}
	
	//this is the callback that will occur every time that an image is updated
	private void updateVision(VisionMeasurement m) {				 
		//int[] data_int = input.getRGB(0, 0, input.getWidth(), input.getHeight(), null, 0, input.getWidth());
			
		//code for super pixels, need to work on makeing less blob like 
		 count++;
		 
		if(count% 1 == 0){
			BufferedImage image = m.getBuffredImage();
			//image = 	visionUtil.resize(image, 200, 200);
	 
			// you probably don't want to segment along the image's alpha channel and the code below assumes 3 channels
			image = ConvertBufferedImage.stripAlphaChannel(image);
	 
			// Select input image type.  Some algorithms behave different depending on image type
			ImageType<MultiSpectral<ImageFloat32>> imageType = ImageType.ms(3, ImageFloat32.class);
			ImageSuperpixels alg = FactoryImageSegmentation.fh04(new ConfigFh04(100,30), imageType);
	 

			// Segment and display results
			SuperPixelMessage output = performSegmentation(alg,image,imageType);
		
			//TODO fancy classification happens here 
			//VisionMeasurement mOut = new VisionMeasurement(output,"", -1);
			msgPub.publish(output);
		}
	}

	 
		/**
		 * Segments and visualizes the image
		 */
		public static <T extends ImageBase>
		SuperPixelMessage performSegmentation( ImageSuperpixels<T> alg , BufferedImage  image,ImageType<MultiSpectral<ImageFloat32>> imageType)
		{
			ImageBase color  = imageType.createImage(image.getWidth(),image.getHeight());;
			ConvertBufferedImage.convertFrom(image, color, true);

			
			// Segmentation often works better after blurring the image.  Reduces high frequency image components which
			// can cause over segmentation
			GBlurImageOps.gaussian(color, color, 0.5, -1, null);
			//note that we do the detection on color but return a message with the original non blurred values 
	 
			// Storage for segmented image.  Each pixel will be assigned a label from 0 to N-1, where N is the number
			// of segments in the image
			ImageSInt32 pixelToSegment = new ImageSInt32(color.width,color.height);
	 
			// Segmentation magic happens here
			alg.segment((T) color,pixelToSegment);			
			//TODO think about upsampling to regain pixels that were loss in down sampling 
			int[][] ids = new int[pixelToSegment.height][pixelToSegment.width];
			int[][] rgbs = new int[pixelToSegment.height][pixelToSegment.width];

			//transforms data from 1d to 2d
			for(int row = 0;row<pixelToSegment.height;row++){
				System.arraycopy(pixelToSegment.data, row*pixelToSegment.width, ids[row], 0, pixelToSegment.width);
						rgbs[row] = image.getRGB(0, row, color.width, 1, null, 0, color.width);	
			}
		
		
			SuperPixelMessage spM = new SuperPixelMessage(ids,rgbs,color.width,color.height,alg.getTotalSuperpixels()	);			
			return spM;
			// Displays the results
			//return visualize(pixelToSegment,color,alg.getTotalSuperpixels());
		}
	 
		/**
		 * Visualizes results three ways.  1) Colorized segmented image where each region is given a random color.
		 * 2) Each pixel is assigned the mean color through out the region. 3) Black pixels represent the border
		 * between regions.
		 */
		public static <T extends ImageBase>
		BufferedImage visualize( ImageSInt32 pixelToRegion , T color , int numSegments  )
		{
			// Computes the mean color inside each region
			ImageType<T> type = color.getImageType();
			ComputeRegionMeanColor<T> colorize = FactorySegmentationAlg.regionMeanColor(type);
	 
			FastQueue<float[]> segmentColor = new ColorQueue_F32(type.getNumBands());
			segmentColor.resize(numSegments);
	 
			GrowQueue_I32 regionMemberCount = new GrowQueue_I32();
			regionMemberCount.resize(numSegments);
	 
			ImageSegmentationOps.countRegionPixels(pixelToRegion, numSegments, regionMemberCount.data);
			colorize.process(color,pixelToRegion,regionMemberCount,segmentColor);
	 
			// Draw each region using their average color
			BufferedImage outColor = VisualizeRegions.regionsColor(pixelToRegion,segmentColor,null);
			

			
			
			// Draw each region by assigning it a random color
			BufferedImage outSegments = VisualizeRegions.regions(pixelToRegion, numSegments, null);
	 
			// Make region edges appear red
			BufferedImage outBorder = new BufferedImage(color.width,color.height,BufferedImage.TYPE_INT_RGB);
			ConvertBufferedImage.convertTo(color, outBorder, true);
			VisualizeRegions.regionBorders(pixelToRegion,0xFF0000,outBorder);
	
			return outColor;
		}
	}
	
