package com.roboclub.robobuggy.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

import com.orsoncharts.util.json.JSONObject;
import com.roboclub.robobuggy.logging.autoLogging.ServerCommunication;

//TODO handle corruption 
//TODO handle deadlock issues 

// This is an internal representation of a log file for internally representing information about the log
public class LogDataType {
	/*
	 *******************************************************************************************
	 * Static final defines for LOG DATA TYPE (should be publicly accessible outside of this file)
	 ******************************************************************************************
	 */

	public static final String FILE_NAME = "LOGDATA"; //the file name where this data is stored 
	
	/*
	 *******************************************************************************************
	 * Private variable for this logData
	 ********************************************************************************************
	 */

	private boolean upToDateOnServer = false;                  // boolean encoding if a local change that has not been uploaded has been made
	private String logRefrenceOnServer = "";		          // Drive Folder ID
	private File logRefrenceOnComputer = null;               // Path to where the log is stored on the users computer
	private String LogNotes = "";						          // A short description of what happened in this log 
	private String folderName = "";						          // The name of the folder for the log 
	private Date lastModified = new Date(0);						          // The date that this log was last modified
	private Date recordedOn = new Date(0);                                  // The date that the log was collected on (the start of log)
	private Date mostRecentlyUsed = new Date(0); 					          // The most recent use date on this computer will be epoch if log has never been used 
	private int logSize = 0;                                      // The number of megabytes that are needed to store this log in its entirety 

	/*
	 *******************************************************************************************
	 * public  functions for logData
	 ********************************************************************************************
	 */	
	
	/**
	 * 
	 * @return
	 */
	public  boolean exsitsOnServer() {
		if(getLogRefrenceOnServer() == null){
			return false;
		}
		ServerCommunication server = ServerCommunication.getInstance();
		return server.validFileId(getLogRefrenceOnServer());
		}

	/**
	 * 
	 * @return
	 */
	public String getLogRefrenceOnServer() {
		return logRefrenceOnServer;
	}

	/**
	 * 
	 * @param logRefrenceOnServer
	 */
	public void setLogRefrenceOnServer(String logRefrenceOnServer) {
		this.logRefrenceOnServer = logRefrenceOnServer;
	}

	/**
	 * 
	 * @return
	 */
	public File getLogRefrenceOnComputer() {
		return logRefrenceOnComputer;
	}

	/**
	 * 
	 * @param logRefrenceOnComputer
	 */
	public void setLogRefrenceOnComputer(File logRefrenceOnComputer) {
		this.logRefrenceOnComputer = logRefrenceOnComputer;
	}
	
	/**
	 * a unique identifier for each log 
	 * @return
	 */
	public String getKey(){
		return logRefrenceOnServer;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFolderName(){
		return folderName;
	}
	
	/**
	 * 
	 * @param otherLog
	 * @return
	 */
	public boolean isSameLog(LogDataType otherLog){
		if(otherLog.getKey() != null && this.getKey() != null && otherLog.getKey() == this.getKey()){
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param folder
	 * @return
	 */
	public static boolean isLog(File folder){
		File dataFile = new File(folder.getPath()+"/"+FILE_NAME);
		return dataFile.exists();
	}
	
	public boolean isUpToDateOnServer() {
		return upToDateOnServer;
	}

	public void setUpToDateOnServer(boolean upToDateOnServer) {
		this.upToDateOnServer = upToDateOnServer;
	}

	public String getLogNotes() {
		return LogNotes;
	}

	public void setLogNotes(String logNotes) {
		LogNotes = logNotes;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public Date getRecordedOn() {
		return recordedOn;
	}

	public void setRecordedOn(Date recordedOn) {
		this.recordedOn = recordedOn;
	}

	public Date getMostRecentlyUsed() {
		return mostRecentlyUsed;
	}

	public void setMostRecentlyUsed(Date mostRecentlyUsed) {
		this.mostRecentlyUsed = mostRecentlyUsed;
	}

	public int getLogSize() {
		return logSize;
	}

	public void setLogSize(int logSize) {
		this.logSize = logSize;
	}	
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public Boolean saveThisLogDataToFolder() throws IOException{
		File logDataFile = new File(getLogRefrenceOnComputer().getPath()+"/"+FILE_NAME);
		//makes sure that the log folder  exist, in case the folder was deleted or this is the first time that the log is saved 
		if(!logRefrenceOnComputer.exists()){
			logRefrenceOnComputer.mkdirs();
		} 
	
		JSONObject jObject = new JSONObject();
		jObject.put("upToDateOnServer",isUpToDateOnServer());
		jObject.put("logRefrenceOnServer", getLogRefrenceOnServer());
		jObject.put("logRefrenceOnComptuer",getLogRefrenceOnComputer());
		jObject.put("LogNotes",getLogNotes());
		jObject.put("folderName",getFolderName());
		jObject.put("lastModified", getLastModified());
		jObject.put("recordedOn",getRecordedOn());
		jObject.put("mostRecentlyUsed",getMostRecentlyUsed());
		jObject.put("logSize",getLogSize());
		String dataToWrite = jObject.toJSONString();

		logDataFile.delete();
		logDataFile.createNewFile();
		FileWriter output = new FileWriter(logDataFile);
		output.write(dataToWrite);
		output.close();
		return true;
	}
	
	/**
	 * constructor for logData type creates a new log ie not from a prexisting log
	 * It is ok if the given log data folder is empty  
	 * @param folder   the log files folder 
	 * @return
	 */
	public  LogDataType(File folder){
		if(isLog(folder)){
			//TOOD throw an error this is already a log you can not create a new log here use read log instead
		}
		this.logRefrenceOnComputer = folder;
	}
	
	
	/*************************************************************************************
	 * 
	 *  CODE below here needs to be finished 
	 * @throws IOException 
	 * 
	 ************************************************************************************/
	
	/**
	 * combines the otherLog into a new log
	 * @param log_one
	 * @param log_two
	 * @return
	 */
	public static LogDataType merge(LogDataType log_one,LogDataType log_two){
		//TODO
		return null;
	}
	
	/**
	 * 
	 * @param folder
	 * @return
	 */
	public static LogDataType readThisLogDataFromFolder(File folder){
		LogDataType result = new LogDataType(folder);
		JSONObject jObject = new JSONObject();
		//TODO read data from jason file 
		//result
 		//TDOO
		return result;
	}
	/*
	 *******************************************************************************************
	 * private  functions for logData
	 ********************************************************************************************
	 */


}
