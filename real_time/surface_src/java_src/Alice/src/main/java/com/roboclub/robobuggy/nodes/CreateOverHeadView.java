package com.roboclub.robobuggy.nodes;
import georegression.struct.point.Vector3D_F64;
import georegression.struct.se.Se3_F64;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;

import org.ejml.data.DenseMatrix64F;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.video.DenseOpticalFlow;

import boofcv.alg.interpolate.TypeInterpolate;
import boofcv.alg.sfm.overhead.CreateSyntheticOverheadView;
import boofcv.alg.sfm.overhead.CreateSyntheticOverheadViewMS;
import boofcv.alg.sfm.overhead.SelectOverheadParameters;
import boofcv.gui.image.ShowImages;
import boofcv.io.UtilIO;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.calib.IntrinsicParameters;
import boofcv.struct.calib.StereoParameters;
import boofcv.struct.image.ImageUInt8;
import boofcv.struct.image.MultiSpectral;

import com.roboclub.robobuggy.main.config;
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
import com.sun.corba.se.impl.orbutil.DenseIntMapImpl;
import com.sun.prism.Image;

/**
 * 
 * @author Trevor Decker
 * This node will listen to an image topic and do a homography so that it gives an overhead(birds eye view)
 *
 */
public class CreateOverHeadView implements Node{
	int count = 0;
	Mat last;
	testOpenCvLinking t;
    //publisher for this cameras messages
	private Publisher msgPub;
	
	public CreateOverHeadView(SensorChannel sensor){
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
		BufferedImage input = m.getBuffredImage();
		MultiSpectral<ImageUInt8> imageRGB = ConvertBufferedImage.convertFromMulti(input, null,true, ImageUInt8.class); 
		
		StereoParameters stereoParam = UtilIO.loadXML("stereo01.xml");
		IntrinsicParameters leftParams = new IntrinsicParameters(input.getWidth()/2, input.getHeight()/2, 0.0, input.getWidth()/2, input.getHeight()/2, input.getWidth(), input.getHeight());
		stereoParam.setLeft(leftParams);
		DenseMatrix64F R = new DenseMatrix64F(3, 3);
		R.set(0,0,config.FRONT_CAM_XX);
		R.set(0,1,config.FRONT_CAM_XY);
		R.set(0,2,config.FRONT_CAM_XZ);
		R.set(1,0,config.FRONT_CAM_YX);
		R.set(1,1,config.FRONT_CAM_YY);
		R.set(1,2,config.FRONT_CAM_YZ);
		R.set(2,0,config.FRONT_CAM_ZX);
		R.set(2,1,config.FRONT_CAM_ZY);
		R.set(2,2,config.FRONT_CAM_ZZ);
		Vector3D_F64 T = new Vector3D_F64(config.FRONT_CAM_X_TRANSLATION, config.FRONT_CAM_Y_TRANSLATION, config.FRONT_CAM_Z_TRANSLATION);
		Se3_F64 groundToLeft = new Se3_F64(R, T);
				 
		CreateSyntheticOverheadView<MultiSpectral<ImageUInt8>> generateOverhead =
				new CreateSyntheticOverheadViewMS<ImageUInt8>(TypeInterpolate.BILINEAR,3,ImageUInt8.class);
 
		// size of cells in the overhead image in world units
		double cellSize = 0.05;
 
		// You can use this to automatically select reasonable values for the overhead image
		SelectOverheadParameters selectMapSize = new SelectOverheadParameters(cellSize,20,0.5);
		selectMapSize.process(stereoParam.left,groundToLeft);
 
		int overheadWidth = selectMapSize.getOverheadWidth();
		int overheadHeight = selectMapSize.getOverheadHeight();
 
		MultiSpectral<ImageUInt8> overheadRGB =
				new MultiSpectral<ImageUInt8>(ImageUInt8.class,overheadWidth,overheadHeight,3);
		generateOverhead.configure(stereoParam.left,groundToLeft,
				selectMapSize.getCenterX(), selectMapSize.getCenterY(), cellSize,overheadRGB.width,overheadRGB.height);
 
		generateOverhead.process(imageRGB, overheadRGB);
 
		// note that the left/right values are swapped in the overhead image.  This is an artifact of the plane's
		// 2D coordinate system having +y pointing up, while images have +y pointing down.
		BufferedImage output = ConvertBufferedImage.convertTo(overheadRGB,null,true);
		
		VisionMeasurement mOut = new VisionMeasurement(output,"", -1);
		msgPub.publish(mOut);
		
	}
}
