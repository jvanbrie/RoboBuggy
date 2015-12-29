package com.roboclub.robobuggy.nodes;

import georegression.struct.point.Vector3D_F64;
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

import boofcv.abst.feature.detect.interest.ConfigGeneralDetector;
import boofcv.abst.feature.tracker.PointTracker;
import boofcv.abst.segmentation.ImageSuperpixels;
import boofcv.abst.sfm.AccessPointTracks3D;
import boofcv.abst.sfm.d3.MonocularPlaneVisualOdometry;
import boofcv.alg.filter.blur.GBlurImageOps;
import boofcv.alg.interpolate.TypeInterpolate;
import boofcv.alg.segmentation.ComputeRegionMeanColor;
import boofcv.alg.segmentation.ImageSegmentationOps;
import boofcv.alg.sfm.overhead.CreateSyntheticOverheadView;
import boofcv.alg.sfm.overhead.CreateSyntheticOverheadViewMS;
import boofcv.alg.sfm.overhead.SelectOverheadParameters;
import boofcv.alg.tracker.klt.PkltConfig;
import boofcv.factory.feature.tracker.FactoryPointTracker;
import boofcv.factory.segmentation.ConfigFh04;
import boofcv.factory.segmentation.ConfigSlic;
import boofcv.factory.segmentation.FactoryImageSegmentation;
import boofcv.factory.segmentation.FactorySegmentationAlg;
import boofcv.factory.sfm.FactoryVisualOdometry;
import boofcv.gui.ListDisplayPanel;
import boofcv.gui.feature.VisualizeRegions;
import boofcv.gui.image.ShowImages;
import boofcv.io.UtilIO;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.calib.MonoPlaneParameters;
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
 * This node will listen to an image topic and attempt to track the motion of the image 
 *
 */
public class VisualOdomatryNode implements Node{

	int count = 0;
	Mat last;
	testOpenCvLinking t;
    //publisher for this cameras messages
	private Publisher msgPub;
	MonocularPlaneVisualOdometry<ImageUInt8> visualOdometry;
	
	
	public VisualOdomatryNode(SensorChannel sensor){
		msgPub = new Publisher(sensor.getMsgPath());
        t = new testOpenCvLinking();

		new Subscriber(SensorChannel.OVER_HEAD_IMAGE.getMsgPath(), new MessageListener() {
			@Override
			public void actionPerformed(String topicName, Message m) {
				updateVision((VisionMeasurement)m);
			}
		});
		
		// load camera description and the video sequence
		MonoPlaneParameters calibration = UtilIO.loadXML("mono_plane.xml");
 
		// specify how the image features are going to be tracked
		PkltConfig configKlt = new PkltConfig();
		configKlt.pyramidScaling = new int[]{1, 2, 4, 8};
		configKlt.templateRadius = 3;
		ConfigGeneralDetector configDetector = new ConfigGeneralDetector(600,3,1);
 
		PointTracker<ImageUInt8> tracker = FactoryPointTracker.klt(configKlt, configDetector, ImageUInt8.class, null);
 
		// declares the algorithm
		visualOdometry =
				FactoryVisualOdometry.monoPlaneInfinity(75, 2, 1.5, 200, tracker, ImageType.single(ImageUInt8.class));
 
		// Pass in intrinsic/extrinsic calibration.  This can be changed in the future.
		visualOdometry.setCalibration(calibration);
		
	}
	
	@Override
	public boolean shutdown() {
		// TODO Auto-generated method stub
		return false;
	}
	
	//this is the callback that will occur every time that an image is updated
	private void updateVision(VisionMeasurement m) {		
		// Process the video sequence and output the location plus number of inliers

		//int[] data_int = input.getRGB(0, 0, input.getWidth(), input.getHeight(), null, 0, input.getWidth());
		BufferedImage input = m.getBuffredImage();
		ImageUInt8 image = new ImageUInt8(input.getWidth(), input.getHeight());// =
		ConvertBufferedImage.convertFrom(input, image);
		 
		if( !visualOdometry.process(image) ) {
			System.out.println("Fault!");
			visualOdometry.reset();
		}

		Se3_F64 leftToWorld = visualOdometry.getCameraToWorld();
		Vector3D_F64 T = leftToWorld.getT();

		System.out.printf("Location %8.2f %8.2f %8.2f      inliers %s\n", T.x, T.y, T.z, inlierPercent(visualOdometry));
		
		
	}

	/**
	 * If the algorithm implements AccessPointTracks3D, then count the number of inlier features
	 * and return a string.
	 */
	public  String inlierPercent(MonocularPlaneVisualOdometry<ImageUInt8> visualOdometry) {
		if( !(visualOdometry instanceof AccessPointTracks3D))
			return "";
 
		AccessPointTracks3D access = (AccessPointTracks3D)visualOdometry;
 
		int count = 0;
		int N = access.getAllTracks().size();
		for( int i = 0; i < N; i++ ) {
			if( access.isInlier(i) )
				count++;
		}
 
		return String.format("%%%5.3f", 100.0 * count / N);
	}
	
	}
	

