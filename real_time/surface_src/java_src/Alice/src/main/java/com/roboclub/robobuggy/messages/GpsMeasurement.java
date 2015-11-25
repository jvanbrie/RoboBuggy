package com.roboclub.robobuggy.messages;

import java.util.Date;

import com.google.gson.JsonPrimitive;
import com.roboclub.robobuggy.ros.Message;
import com.roboclub.robobuggy.ros.SensorChannel;
import com.roboclub.robobuggy.utilities.RobobuggyDateFormatter;

/**
 * @author Matt Sebek (msebek)
 *
 * @version 0.5
 * 
 *          CHANGELOG: NONE
 * 
 *          DESCRIPTION: TODO
 */

// gpsTimestamp is the UTC time in hours for the given position.
// See http://hemispheregnss.com/gpsreference/GPGGA.html for more information
public class GpsMeasurement extends BaseMessage implements Message {
	public static final String version_id = "gpsV0.2";

	public static final String gps_timestamp_key = "gps_timestamp";
	public static final String latitude_key = "latitude";
	public static final String longitude_key = "longitude";
	public static final String lat_dir_key = "lat_dir";
	public static final String lon_dir_key = "lon_dir";
	public static final String quality_val_key = "quality";
	public static final String num_satellites_key = "num_satellites";
	public static final String HDOP_key = "HDOP";
	public static final String antenna_altitude_key = "antenna_altitude";
	public static final String raw_latitude_key = "raw_gps_lat";
	public static final String raw_longitude_key = "raw_gps_lon";


	public GpsMeasurement(Date messageTimestamp, Date gpsTimestamp, double latitude, boolean north, double longitude,
			boolean west, int quality_value, int num_satellites, double horizontal_dilution_of_precision, double antenna_altitude, double rawGPSLat, double rawGPSLong) {
		
		super(SensorChannel.GPS.getMsgPath(), RobobuggyDateFormatter.getFormattedRobobuggyDateAsString(messageTimestamp));
		
		addParamToSensorData(gps_timestamp_key, new JsonPrimitive(RobobuggyDateFormatter.getFormattedRobobuggyDateAsString(gpsTimestamp)));
		addParamToSensorData(latitude_key, new JsonPrimitive(latitude));
		addParamToSensorData(longitude_key, new JsonPrimitive(longitude));
		addParamToSensorData(lat_dir_key, new JsonPrimitive(north));
		addParamToSensorData(lon_dir_key, new JsonPrimitive(west));
		addParamToSensorData(quality_val_key, new JsonPrimitive(quality_value));
		addParamToSensorData(num_satellites_key, new JsonPrimitive(num_satellites));
		addParamToSensorData(HDOP_key, new JsonPrimitive(horizontal_dilution_of_precision));
		addParamToSensorData(antenna_altitude_key, new JsonPrimitive(antenna_altitude));
		addParamToSensorData(raw_latitude_key, new JsonPrimitive(rawGPSLat));
		addParamToSensorData(raw_longitude_key, new JsonPrimitive(rawGPSLong));
	}
	
	public Date getGPSTimestamp() {
		return RobobuggyDateFormatter.getDatefromRobobuggyDateString(getParamFromSensorData(gps_timestamp_key).getAsString());
	}
	
	public double getLatitude() {
		return getParamFromSensorData(latitude_key).getAsDouble();
	}
	
	public double getLongitude() {
		return getParamFromSensorData(longitude_key).getAsDouble();
	}
	
	public String getLatitudeDir() {
		boolean north = getParamFromSensorData(lat_dir_key).getAsBoolean();
		if(north) {
			return "N";
		}
		return "S";
	}
	
	public String getLongitudeDir() {
		boolean west = getParamFromSensorData(lon_dir_key).getAsBoolean();
		if(west) {
			return "W";
		}
		return "E";
	}
	
	public int getQualityValue() {
		return getParamFromSensorData(quality_val_key).getAsInt();
	}
	
	public int getNumSatellites() {
		return getParamFromSensorData(num_satellites_key).getAsInt();
	}
	
	public double getHDOP() {
		return getParamFromSensorData(HDOP_key).getAsDouble();
	}
	
	public double getAntennaAltitude() {
		return getParamFromSensorData(antenna_altitude_key).getAsDouble();
	}
	
	public double getRawGPSOutput_Latitude() {
		return getParamFromSensorData(raw_latitude_key).getAsDouble();
	}
	
	public double getRawGPSOutput_Longitude() {
		return getParamFromSensorData(raw_longitude_key).getAsDouble();
	}
	
	
}