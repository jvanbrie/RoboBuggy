package com.roboclub.robobuggy.logging.autoLogging;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.commons.logging.Log;

import com.orsoncharts.util.json.JSONObject;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.ChildList;
import com.google.api.services.drive.model.ParentReference;
import com.roboclub.robobuggy.logging.LogDataType;

//TODO things to add in future versions: 
// When a user edits a file outside of this system it should be detected and updated on the server
// When a file is deleted on the server or locally it should be propagated to the server
// add logic for automatically removing logs that have not been used recently from local storage 

/**
 * 
 * The AutoLogging class manages the storage and availability of log files and log data
 * The system keeps track of when this computer last accessed each log file, and how often 
 * that file is used.
 * 
 * A heap is maintained of what log files to cache.  This way the total space used by log files does 
 * not take up too much space on a users  computer.  
 * 
 * 
 * When a new log file is created or a log file is changed then it is updated on the server
 * 
 * Known Problems: 
 *      A race condition exists when two computers attempt to upload data about a log change at the same time. 
 * 
 * Behaviors: 
 * 		B1: All log files that have not yet been uploaded to the server shall be uploaded to the server
 * 		B2: The system should be able to download log files from the server
 * 		B3: The system should have a list of all log files and statistics about what happened in that log file
 * 
 * @author Trevor Decker
 *
 */
public class autoLogging {
	boolean IN_OFFLINE_MODE = true;  //if true then try to connect to the server otherwise do not  
	
	private Hashtable<String, LogDataType> logData;
	
	//on google drive every file and folder is given a unique identifying string 
	private String DriveStorageFolder_id;
	private File localFolderPath;
	
	/**
	 *  The constructor for the auto logger, will load any local files 
	 * @param whereToSave
	 * @param DriveStorageFolder_id
	 * @throws IOException
	 */
	public autoLogging(File whereToSave,String DriveStorageFolder_id) throws IOException{
		logData = new Hashtable<String, LogDataType>();
		setWhereToSave(whereToSave);
		setServerFolderId(DriveStorageFolder_id);
		readLogData();
		downloadLogData(DriveStorageFolder_id, whereToSave);
		uploadLogs();
	}
	
	/**
	 * 
	 * @param newServerFolderId
	 */
	public void setServerFolderId(String newServerFolderId){
		DriveStorageFolder_id = newServerFolderId;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getServerFolderId(){
		return DriveStorageFolder_id;
	}
	
	/**
	 * should be called when the program is finished to save to server and save to local file 
	 * @return
	 * @throws IOException
	 */
	public boolean save() throws IOException{
		boolean localSaveState = saveLogDataToFolders();
		boolean serverSaveState = saveLogDataToServer();
		return localSaveState && serverSaveState;
	}
	
	/**
	 * returns a file reference of the local path of where log files are to be saved
	 * @return
	 */
	public File getWhereToSave(){
		return localFolderPath;
	}
	
	/**
	 * 
	 * @param whereToSave
	 */
	public void setWhereToSave(File whereToSave){
		//makes sure that the folder exists, if it does not create the folder
		if(!whereToSave.isDirectory()){	
			whereToSave.mkdir();
		}
		
		this.localFolderPath = whereToSave;
	}
	
	
	/**
	 * Evaluates to a list of all of the log files
	 * @return
	 */
	LogDataType[] getListOfLogFiles(){
		return (LogDataType[]) logData.values().toArray();
	}	
	
	/**
	 * will overwrite the current log file, should call readLogData before calling saveLogDataToFolder
	 * @return
	 * @throws IOException
	 */
	public boolean saveLogDataToFolders() throws IOException{
		boolean allLogsSavedCorrectly = true;
		LogDataType[] data = getListOfLogFiles();
		for (LogDataType thisLogData : data) {
			boolean thisLogSavedState = thisLogData.saveThisLogDataToFolder();
			if(!thisLogSavedState){
				allLogsSavedCorrectly = false;
			}
		}		
		return allLogsSavedCorrectly;
	}
	
	/**
	 * Assumes that any log files have already been loaded, ie can override any files on disk
	 * @param logFolder
	 * @return
	 * @throws IOException
	 */
	boolean saveLogDataLocally(LogDataType logData) throws IOException{
		File logFolder = logData.getLogRefrenceOnComputer();
		if(!logFolder.exists() ){
			logFolder.mkdirs();
		}
		if(!logFolder.isDirectory()){
			//TODO throw an error 
		}
		
		//if a file is already at location then it will be over written 
		File logDataFile = new File(logFolder.getPath()+"/"+LogDataType.FILE_NAME);
		System.out.println(logDataFile.getPath());
		logDataFile.delete();
		logData.saveThisLogDataToFolder();
		return true;
	}
	
	/**
	 * 
	 * @param logFolder
	 * @return
	 */
	LogDataType readALogData(File logFolder){
		LogDataType newLogData = LogDataType.readThisLogDataFromFolder(logFolder);
		String oldLogData_key = lookUpLog(newLogData);
		if(oldLogData_key == null){
			//this log is not already being tracked so we should start tracking it 
			logData.put(newLogData.getKey(), newLogData);
		}else{
			//this log is not new so we should update the log
			LogDataType mergedLogData = LogDataType.merge(logData.get(oldLogData_key), newLogData);
			logData.replace(oldLogData_key,mergedLogData);
			newLogData = mergedLogData;
		}
		return newLogData;
	}
	
	/**
	 * reads every folder inside whereToSave if it is a logfile then add it to the logDataType structure 
	 * @return
	 */
	boolean readLogData(){
		boolean result = true;
		for(File f : localFolderPath.listFiles()){
			if(LogDataType.isLog(f)){
				if(readALogData(f)== null){
					result = false;
				}
			}
		}
		return result;
	}

	/**
	 * adds logging data to a log folder that may or may not exist. 
	 * @param LogLocation
	 * @return
	 * @throws IOException
	 */
	boolean startTrackingLog(File newLogLocation) throws IOException{
		LogDataType newLog = new LogDataType(newLogLocation);	
		saveLogDataLocally(newLog);
		return uploadLog(newLog);
	}
	
	/**
	 * assumes that data on the server has been saved. This can cause race conditions
	 * @return
	 * @throws IOException
	 */
	public boolean saveLogDataToServer() throws IOException{
		boolean allLogsSavedCorrectly = true;
		LogDataType[] data = getListOfLogFiles();
		for (LogDataType thisLogData : data) {
			if(!thisLogData.isUpToDateOnServer()){
				if(!uploadLog(thisLogData)){
					allLogsSavedCorrectly = false;
				}
			}
		}		
		return allLogsSavedCorrectly;
	}
	
	/**
	 * assumes that most recent changes have been done locally, this can casue raceconditions and what is on the server will be over wirtten 
	 * @param thisLog
	 * @return
	 * @throws IOException
	 */
	boolean uploadLog(LogDataType thisLog) throws IOException{
		if(IN_OFFLINE_MODE){
			return false;
		}
		
		//check to see if log has already been uploaded, if it has been do nothing 
		if(thisLog.isUpToDateOnServer()){
			return true;
		}
		//if the log file has not been uploaded then uploaded it	
		ServerCommunication server = ServerCommunication.getInstance();
		if(!thisLog.exsitsOnServer()){
			//we need to create the folder to store this file
			String location = server.createFolder(thisLog.getFolderName(),DriveStorageFolder_id);
			thisLog.setLogRefrenceOnServer(location);
		}
		//we need to upload the Data inside the log so we delete the old data and upload new data 
		server.removeContentOfFolder(thisLog.getLogRefrenceOnServer());
		//now we upload the new data
		ChildList serverFiles = server.getFilesInFolder(thisLog.getLogRefrenceOnServer());
		server.uploadFolder(thisLog.getLogRefrenceOnComputer(),thisLog.getLogRefrenceOnServer());
		thisLog.setUpToDateOnServer(true);
		//now we need to update the internal refrence in the data structure
		logData.replace(thisLog.getKey(), thisLog);
		return true;
	}
	
	/**
	 * 
	 * @param thisLog
	 * @return
	 * @throws IOException
	 */
	boolean downloadLog(LogDataType thisLog) throws IOException{
		if(IN_OFFLINE_MODE){
			return false;
		}
		
		ServerCommunication server = ServerCommunication.getInstance();
		server.downloadFolder(thisLog.getLogRefrenceOnComputer(),thisLog.getLogRefrenceOnServer());
		return true;
	}
	
	/**
	 * evaluates to null if the given logData is not already being stored
	 * evaluates to a reference to that logData if an equivalent log is being stored 
	 * @param aLogData
	 * @return
	 */
	public String lookUpLog(LogDataType aLogData){
		System.out.println("key: "+aLogData.getKey());
		//incase the log has not been added to server yet 
		System.out.println("hereWeare"+aLogData.getKey());
		if(aLogData.getKey() == null || aLogData.getKey().equals("")){
			return null;
		}
	
		//makes sure that the key is actually inside the log has table
		if(logData.get(aLogData.getKey()) == null){
			return null;
		}
		return aLogData.getKey();	
	}
	
	/**
	 * goes to every folder inside the server folder and downloads the meta data for those logs  
	 * @param Folder_id
	 * @param whereToSave
	 * @return
	 * @throws IOException
	 */
	boolean downloadLogData(String Folder_id,File whereToSave) throws IOException{
		if(IN_OFFLINE_MODE){
			return false;
		}
		
		ServerCommunication server = ServerCommunication.getInstance();
		ArrayList<com.google.api.services.drive.model.File> folders = server.ListFolderMetaDataInDrive(Folder_id);
		for(com.google.api.services.drive.model.File f : folders){
			if(isLogFolderOnDrive(f)){
				//for each folder see if we already have its log data, if so merge data
				//if not then download and add the log data 
				LogDataType newLogData = readThisLogDataFromServerFolder(f, whereToSave);
				String key = lookUpLog(newLogData);
				if(key != null){
					LogDataType oldData = logData.get(key);
					logData.replace(key, LogDataType.merge(oldData, newLogData));
				}else{
					logData.put(key, newLogData);
				}
			}else{
				//if it is a folder then crate that folder locally and recru
				String newFolderString = whereToSave+"/"+f.getTitle();
				File newFolder = new File(newFolderString);
				newFolder.mkdir();
				downloadLogData(f.getId(),newFolder);
			}
		}
		return false;
	}

	/**
	 * 
	 * @param f
	 * @param parentFile
	 * @return
	 * @throws IOException
	 */
	private LogDataType readThisLogDataFromServerFolder(
			com.google.api.services.drive.model.File f,File parentFile) throws IOException {
		if(IN_OFFLINE_MODE){
			//throw an error for trying to connect to the server while in offline mode 
		}
		
		ServerCommunication server =  ServerCommunication.getInstance();
		 ArrayList<com.google.api.services.drive.model.File> files =  server.ListFileMetaDataInDrive(f.getId());
		 for(com.google.api.services.drive.model.File thisFile : files){
			 if(thisFile.getTitle() == LogDataType.FILE_NAME){
				 //download the file 
				 server.downloadFile(thisFile,parentFile);
				 return LogDataType.readThisLogDataFromFolder(parentFile);
			 }
		 }
		return null;
	}

	/**
	 * 
	 * @param f a local machine file path
	 * @return true if f is a folder that encodes a log 
	 * @throws IOException
	 */
	private boolean isLogFolderOnDrive(com.google.api.services.drive.model.File f) throws IOException {
		if(IN_OFFLINE_MODE){
			//TODO throw an error 
		}
		ServerCommunication server =  ServerCommunication.getInstance();
		 ArrayList<com.google.api.services.drive.model.File> files =  server.ListFileMetaDataInDrive(f.getId());
		 for(com.google.api.services.drive.model.File thisFile : files){
			 if(thisFile.getTitle() == LogDataType.FILE_NAME){
				 return true;
			 }
		 }
		return false;
	}

	/**
	 * attempts to upload all logs that have not been updated on ther server yet
	 * @return
	 * @throws IOException
	 */
	private boolean uploadLogs() throws IOException{
		for(LogDataType log : logData.values()){
			if(!log.isUpToDateOnServer()){
				uploadLog(log);
			}
		}
		return false;
	}
	
}
