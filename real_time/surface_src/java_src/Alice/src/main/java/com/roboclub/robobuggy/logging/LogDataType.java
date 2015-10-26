package com.roboclub.robobuggy.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

import com.orsoncharts.util.json.JSONObject;
import com.roboclub.robobuggy.logging.autoLogging.ServerCommunication;

// This is an internal representation of a log file for internally representing information about the log
public class LogDataType {
	public static final String FILE_NAME = "LOGDATA"; //the file name where this data is stored 
	public boolean upToDateOnServer;
	private String logRefrenceOnServer;// Drive Folder ID
	private File logRefrenceOnComputer;
	String LogNotes;
	String folderName;
	Date lastModified;
	Date recordedOn;
	Date mostRecentlyUsed; //The most recent use date on this computer will be epoch if log has never been used 
	int logSize;
	
	public Boolean saveThisLogDataToFolder() throws IOException{
		JSONObject jObject = new JSONObject();
		String dataToWrite = jObject.toJSONString();
		getLogRefrenceOnComputer().delete();
		getLogRefrenceOnComputer().createNewFile();
		FileWriter output = new FileWriter(getLogRefrenceOnComputer());
		output.write(dataToWrite);
		output.close();
		return true;
	}
	
	
	public  boolean exsitsOnServer() {
		if(getLogRefrenceOnServer() == null){
			return false;
		}
		ServerCommunication server = ServerCommunication.getInstance();
		return server.validFileId(getLogRefrenceOnServer());
		}

	public String getLogRefrenceOnServer() {
		return logRefrenceOnServer;
	}

	public void setLogRefrenceOnServer(String logRefrenceOnServer) {
		this.logRefrenceOnServer = logRefrenceOnServer;
	}

	public File getLogRefrenceOnComputer() {
		return logRefrenceOnComputer;
	}

	public void setLogRefrenceOnComputer(File logRefrenceOnComputer) {
		this.logRefrenceOnComputer = logRefrenceOnComputer;
	}
	
	//a unique identifier for each log 
	public String getKey(){
		return logRefrenceOnServer;
	}
	
	public String getTitle(){
		return folderName;
	}
	
	/*************************************************************************************
	 * 
	 *  CODE below here needs to be finished 
	 * @throws IOException 
	 * 
	 ************************************************************************************/
	public static boolean isLog(File folder){
		return false;
	}
	
	//combines the otherLog into a new log
	public static LogDataType merge(LogDataType log_one,LogDataType log_two){
		//TODO
		return null;
	}

	
	public boolean isSameLog(LogDataType otherLog){
		//TODO
		return false;
	}
	
	public static LogDataType readThisLogDataFromFolder(File folder){
		JSONObject jObject = new JSONObject();
 		//TDOO
		return null;
	}
	
	
}
