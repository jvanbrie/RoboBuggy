package com.roboclub.robobuggy.nodes;

import georegression.struct.homography.Homography2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.se.Se3_F64;

import java.awt.Color;
import java.awt.Dimension;
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

import boofcv.abst.feature.detect.interest.ConfigGeneralDetector;
import boofcv.abst.feature.tracker.PointTracker;
import boofcv.abst.segmentation.ImageSuperpixels;
import boofcv.abst.sfm.d2.ImageMotion2D;
import boofcv.abst.sfm.d2.MsToGrayMotion2D;
import boofcv.alg.filter.blur.GBlurImageOps;
import boofcv.alg.interpolate.TypeInterpolate;
import boofcv.alg.segmentation.ComputeRegionMeanColor;
import boofcv.alg.segmentation.ImageSegmentationOps;
import boofcv.alg.sfm.d2.StitchingFromMotion2D;
import boofcv.alg.sfm.overhead.CreateSyntheticOverheadView;
import boofcv.alg.sfm.overhead.CreateSyntheticOverheadViewMS;
import boofcv.alg.sfm.overhead.SelectOverheadParameters;
import boofcv.factory.feature.tracker.FactoryPointTracker;
import boofcv.factory.segmentation.ConfigFh04;
import boofcv.factory.segmentation.ConfigSlic;
import boofcv.factory.segmentation.FactoryImageSegmentation;
import boofcv.factory.segmentation.FactorySegmentationAlg;
import boofcv.factory.sfm.FactoryMotion2D;
import boofcv.gui.ListDisplayPanel;
import boofcv.gui.feature.VisualizeRegions;
import boofcv.gui.image.ImageGridPanel;
import boofcv.gui.image.ShowImages;
import boofcv.io.MediaManager;
import boofcv.io.UtilIO;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.SimpleImageSequence;
import boofcv.io.image.UtilImageIO;
import boofcv.io.wrapper.DefaultMediaManager;
import boofcv.misc.BoofMiscOps;
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
public class MosaicNode implements Node{

	int count = 0;
	Mat last;
	testOpenCvLinking t;
    //publisher for this cameras messages
	private Publisher msgPub;
	MultiSpectral<ImageFloat32> frame;
	StitchingFromMotion2D<MultiSpectral<ImageFloat32>,Homography2D_F64> stitch;
	boolean enlarged = false;
	boolean enlarged_ = false;
	ImageGridPanel gui;
	SimpleImageSequence<MultiSpectral<ImageFloat32>> video;
	
	
	public MosaicNode(SensorChannel sensor){
		msgPub = new Publisher(sensor.getMsgPath());
        t = new testOpenCvLinking();

		new Subscriber(SensorChannel.OVER_HEAD_IMAGE.getMsgPath(), new MessageListener() {
			@Override
			public void actionPerformed(String topicName, Message m) {
				updateVision((VisionMeasurement)m);
			}
		});
		
		 
		// Configure the feature detector
		ConfigGeneralDetector confDetector = new ConfigGeneralDetector();
		confDetector.threshold = 1;
		confDetector.maxFeatures = 300;
		confDetector.radius = 3;
 
		// Use a KLT tracker
		PointTracker<ImageFloat32> tracker = FactoryPointTracker.klt(new int[]{1,2,4,8},confDetector,3,
				ImageFloat32.class,ImageFloat32.class);
 
		// This estimates the 2D image motion
		// An Affine2D_F64 model also works quite well.
		ImageMotion2D<ImageFloat32,Homography2D_F64> motion2D =
				FactoryMotion2D.createMotion2D(220,3,2,30,0.6,0.5,false,tracker,new Homography2D_F64());
 
		// wrap it so it output color images while estimating motion from gray
		ImageMotion2D<MultiSpectral<ImageFloat32>,Homography2D_F64> motion2DColor =
				new MsToGrayMotion2D<ImageFloat32,Homography2D_F64>(motion2D,ImageFloat32.class);
 
		// This fuses the images together
				stitch = FactoryMotion2D.createVideoStitchMS(0.5, motion2DColor, ImageFloat32.class);
 
		// Load an image sequence
		MediaManager media = DefaultMediaManager.INSTANCE;
		//String fileName = UtilIO.pathExample("airplane01.mjpeg");
		video =
				media.openVideo("airplane01.mjpeg", ImageType.ms(3, ImageFloat32.class));
 

		
	}
	
	@Override
	public boolean shutdown() {
		// TODO Auto-generated method stub
		return false;
	}
	
	//this is the callback that will occur every time that an image is updated
	private void updateVision(VisionMeasurement m) {				 
		//int[] data_int = input.getRGB(0, 0, input.getWidth(), input.getHeight(), null, 0, input.getWidth());
		
		BufferedImage input = m.getBuffredImage();
		BufferedImage output = new BufferedImage(200, 200, input.getType());
		output = input.getSubimage(200, 200, 200, 200);
		frame = ConvertBufferedImage.convertFromMulti(output, frame, false, ImageFloat32.class);
		if( !enlarged_ ) {
			enlarged_ = true;
			 System.out.println("frame:"+frame);
			// shrink the input image and center it
			Homography2D_F64 shrink = new Homography2D_F64(0.5,0,frame.width/4,0,0.5,frame.height/4,0,0,1);
			shrink = shrink.invert(null);
	 
			// The mosaic will be larger in terms of pixels but the image will be scaled down.
			// To change this into stabilization just make it the same size as the input with no shrink.
			stitch.configure(frame.width,frame.height,shrink);
			// process the first frame
			stitch.process(frame);
	 
			// Create the GUI for displaying the results + input image
			gui = new ImageGridPanel(1,2);
			gui.setImage(0,0,new BufferedImage(frame.width,frame.height,BufferedImage.TYPE_INT_RGB));
			gui.setImage(0,1,new BufferedImage(frame.width,frame.height,BufferedImage.TYPE_INT_RGB));
			gui.setPreferredSize(new Dimension(3*frame.width,frame.height*2));
	 
			ShowImages.showWindow(gui,"Example Mosaic", true); 
		}
		
		if( !stitch.process(frame) ){
		//	throw new RuntimeException("You should handle failures");
		}
			
		// if the current image is close to the image border recenter the mosaic
		StitchingFromMotion2D.Corners corners = stitch.getImageCorners(frame.width,frame.height,null);
		if( nearBorder(corners.p0,stitch) || nearBorder(corners.p1,stitch) ||
				nearBorder(corners.p2,stitch) || nearBorder(corners.p3,stitch) ) {
			stitch.setOriginToCurrent();

			// only enlarge the image once
			if( !enlarged ) {
				enlarged = true;
				// double the image size and shift it over to keep it centered
				int widthOld = stitch.getStitchedImage().width;
				int heightOld = stitch.getStitchedImage().height;

				int widthNew = widthOld*2;
				int heightNew = heightOld*2;

				int tranX = (widthNew-widthOld)/2;
				int tranY = (heightNew-heightOld)/2;

				Homography2D_F64 newToOldStitch = new Homography2D_F64(1,0,-tranX,0,1,-tranY,0,0,1);

				stitch.resizeStitchImage(widthNew, heightNew, newToOldStitch);
				gui.setImage(0, 1, new BufferedImage(widthNew, heightNew, BufferedImage.TYPE_INT_RGB));
			}
			corners = stitch.getImageCorners(frame.width,frame.height,null);
		}
		// display the mosaic
		ConvertBufferedImage.convertTo(frame,gui.getImage(0, 0),true);
		ConvertBufferedImage.convertTo(stitch.getStitchedImage(), gui.getImage(0, 1),true);

		// draw a red quadrilateral around the current frame in the mosaic
		Graphics2D g2 = gui.getImage(0,1).createGraphics();
		g2.setColor(Color.RED);
		g2.drawLine((int)corners.p0.x,(int)corners.p0.y,(int)corners.p1.x,(int)corners.p1.y);
		g2.drawLine((int)corners.p1.x,(int)corners.p1.y,(int)corners.p2.x,(int)corners.p2.y);
		g2.drawLine((int)corners.p2.x,(int)corners.p2.y,(int)corners.p3.x,(int)corners.p3.y);
		g2.drawLine((int)corners.p3.x,(int)corners.p3.y,(int)corners.p0.x,(int)corners.p0.y);

		gui.repaint();

		// throttle the speed just in case it's on a fast computer
		BoofMiscOps.pause(50);
	}

/**
 * Checks to see if the point is near the image border
 */
private static boolean nearBorder( Point2D_F64 p , StitchingFromMotion2D<?,?> stitch ) {
	int r = 10;
	if( p.x < r || p.y < r )
		return true;
	if( p.x >= stitch.getStitchedImage().width-r )
		return true;
	if( p.y >= stitch.getStitchedImage().height-r )
		return true;

	return false;
}

	}

