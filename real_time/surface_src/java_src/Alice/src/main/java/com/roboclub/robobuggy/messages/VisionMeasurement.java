package com.roboclub.robobuggy.messages;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.util.Date;

import org.opencv.core.Mat;

import com.roboclub.robobuggy.ros.Message;

public class VisionMeasurement extends BaseMessage implements Message  {
	BufferedImage frame;
	String source;
	Date timestamp;
	int frameId;  //is used for indexing this frame in a video representation of this vision source
	
	public VisionMeasurement(BufferedImage frame,String source,int frameId) {
		this.timestamp = new Date(); //TODO fix
		this.frame = frame;
		this.source = source;
		this.frameId = frameId;
	}

	
	@Override
	public String toLogString() {
		String s = super.formatter.format(timestamp);
		return s;
	}

	@Override
	public Message fromLogString(String str) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public BufferedImage getBuffredImage(){
		return frame;
	}
	
    public static BufferedImage ToBufferedImage(Mat thisFrame) {
        //Mat() to BufferedImage
        int type = 0;
        if (thisFrame.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (thisFrame.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        BufferedImage image = new BufferedImage(thisFrame.width(), thisFrame.height(), type);
        WritableRaster raster = image.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        thisFrame.get(0, 0, data);

        return image;
    }

}
