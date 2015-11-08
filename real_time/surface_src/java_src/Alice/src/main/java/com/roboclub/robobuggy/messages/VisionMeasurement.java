package com.roboclub.robobuggy.messages;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

import org.opencv.core.Mat;

import com.roboclub.robobuggy.ros.Message;

public class VisionMeasurement extends BaseMessage implements Message  {
	Mat frame;
	String source;
	int frameId;  //is used for indexing this frame in a video representation of this vision source
	
	public VisionMeasurement(Mat frame,String source,int frameId) {
		this.frame = frame;
		this.source = source;
		this.frameId = frameId;
	}

	@Override
	public String toLogString() {
		return source+"\t"+frameId;
	}

	@Override
	public Message fromLogString(String str) {
		// TODO Auto-generated method stub
		return null;
	}
	
    public BufferedImage ToBufferedImage() {
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

}
