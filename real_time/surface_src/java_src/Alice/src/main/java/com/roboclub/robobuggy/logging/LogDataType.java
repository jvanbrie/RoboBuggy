package com.roboclub.robobuggy.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

	private boolean upToDateOnServer = false;                // boolean encoding if a local change that has not been uploaded has been made
	private String logRefrenceOnServer = "";		         // Drive Folder ID
	private File logRefrenceOnComputer = null;               // Path to where the log is stored on the users computer
	private String LogNotes = "";						     // A short description of what happened in this log 
	private String folderName = "";						     // The name of the folder for the log 
	private Date lastModified = new Date(0);				 // The date that this log was last modified
	private Date recordedOn = new Date(0);                   // The date that the log was collected on (the start of log)
	private Date mostRecentlyUsed = new Date(0); 		     // The most recent use date on this computer will be epoch if log has never been used 
	private int logSize = 0;                                 // The number of megabytes that are needed to store this log in its entirety 

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
		return folderName;//for now folder names need to be unique 
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
		this.upToDateOnServer  = upToDateOnServer;  
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
		jObject.put("logRefrenceOnComptuer",getLogRefrenceOnComputer().toString().replace("/", "\\"));//so that the json does not error out
		jObject.put("LogNotes",getLogNotes());
		jObject.put("folderName",getFolderName());
		//is only for readability 
		jObject.put("lastModified_string", getLastModified().toString().replace(":", ";"));
		jObject.put("lastModified", getLastModified().getTime());
		//is only for readability 
		jObject.put("recordedOn_string",getRecordedOn().toString().replace(":", ";"));
		jObject.put("recordedOn",getRecordedOn().getTime());
		//is only for readability 
		jObject.put("mostRecentlyUsed_string",getMostRecentlyUsed().toString().replace(":", ";"));
		jObject.put("mostRecentlyUsed", getMostRecentlyUsed().getTime());
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
	 * constructor for logData type creates a new log ie not from a preexisting log
	 * It is ok if the given log data folder is empty  
	 * @param folder   the log files folder 
	 * @return
	 */
	public  LogDataType(File folder){
		if(isLog(folder)){
			//TOOD throw an error this is already a log you can not create a new log here use read log instead
		}
		this.folderName = folder.getName();
		this.logRefrenceOnComputer = folder;
	}
	
	/**
	 * 
	 * @param folder
	 * @return
	 * @throws FileNotFoundException 
	 */
	public static LogDataType readThisLogDataFromFolder(File folder) throws FileNotFoundException{
		LogDataType result = new LogDataType(folder);
		String logData_path = folder.getPath()+"/"+FILE_NAME;
		File logData_file = new File(logData_path);
		
		//read a line from the file 
		Scanner jsonScanner = new Scanner(logData_file);
		String content  = jsonScanner.nextLine();
		jsonScanner.close();
		
		//parse the json to populate result 
		JsonParser parser = new JsonParser();
		JsonObject jObject = (JsonObject)parser.parse(content);
		result.setUpToDateOnServer(Boolean.valueOf(jObject.get("upToDateOnServer").toString()));
		result.setLogRefrenceOnComputer(new File(jObject.get("logRefrenceOnServer").toString().replace("\\", "/")));
		result.setLogRefrenceOnServer(jObject.get("logRefrenceOnServer").toString().replace("\\", "/"));
		result.setLogNotes(jObject.get("LogNotes").toString());
		result.setMostRecentlyUsed(new Date(Long.parseLong(jObject.get("mostRecentlyUsed").toString())));
		result.setLastModified(new Date(Long.parseLong(jObject.get("lastModified").toString())));
		result.setRecordedOn(new Date(Long.parseLong(jObject.get("recordedOn").toString())));		
		result.setLogSize(Integer.parseInt(jObject.get("logSize").toString()));
		result.folderName = jObject.get("folderName").toString();
		
		//return resulting LogDatType 
		return result;
	}
	
	/**
	 * combines the otherLog into a new log
	 * @param log_one
	 * @param log_two
	 * @returns
	 */
	public static LogDataType merge(LogDataType log_one,LogDataType log_two){
		assert(log_one.folderName.equals(log_two.getFolderName()));
		//update last modified 
		if(log_one.lastModified.before(log_two.lastModified)){
			return log_two;
		}else{
			return log_one;
		}
	}
	

	/*
	 *******************************************************************************************
	 * private  functions for logData
	 ********************************************************************************************
	 */


}
