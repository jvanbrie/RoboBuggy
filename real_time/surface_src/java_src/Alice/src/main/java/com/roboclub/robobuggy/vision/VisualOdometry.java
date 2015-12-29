package com.roboclub.robobuggy.vision;

import georegression.struct.point.Vector3D_F64;
import georegression.struct.se.Se3_F64;

import org.opencv.video.DenseOpticalFlow;

import boofcv.abst.feature.detect.interest.ConfigGeneralDetector;
import boofcv.abst.feature.tracker.PointTracker;
import boofcv.abst.sfm.AccessPointTracks3D;
import boofcv.abst.sfm.d3.MonocularPlaneVisualOdometry;
import boofcv.alg.tracker.klt.PkltConfig;
import boofcv.factory.feature.tracker.FactoryPointTracker;
import boofcv.factory.sfm.FactoryVisualOdometry;
import boofcv.io.MediaManager;
import boofcv.io.UtilIO;
import boofcv.io.image.SimpleImageSequence;
import boofcv.io.wrapper.DefaultMediaManager;
import boofcv.struct.calib.MonoPlaneParameters;
import boofcv.struct.image.ImageType;
import boofcv.struct.image.ImageUInt8;

public class VisualOdometry extends DenseOpticalFlow{

	public VisualOdometry(long addr) {
		super(addr);
		// TODO Auto-generated constructor stub
 

	}
	
	
	


}
