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
public class InterestingPointExtractionNode implements Node{

	int count = 0;
	Mat last;
	testOpenCvLinking t;
    //publisher for this cameras messages
	private Publisher msgPub;

	
	
	public InterestingPointExtractionNode(SensorChannel sensor){
		msgPub = new Publisher(sensor.getMsgPath());
        t = new testOpenCvLinking();

		new Subscriber(SensorChannel.VISION.getMsgPath(), new MessageListener() {
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

	}
}

