package com.roboclub.robobuggy.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import com.roboclub.robobuggy.logging.automaticLogging.AutomaticLogging;
import com.roboclub.robobuggy.main.config;

/**
 * Logs data from the sensors
 * 
 * @author Joe Doyle 
 * @author Trevor Decker
 */
public final class RobotLogger {
	public  static Logger message;
	public static SensorLogger sensor;
	private static RobotLogger instance;
	public static File logDir;  //all of the log folder (aka parent to logfolder)
	public static File logFolder;  //this logs folder

	public static RobotLogger getInstance() {
		if (instance == null) {
			logDir = new File(config.LOG_FILE_LOCATION);
			
			logDir.mkdirs();
			
			try {
				instance = new RobotLogger(logDir);
			} catch (Exception e) {
				e.printStackTrace();
			}
			AutomaticLogging autoLoger = AutomaticLogging.startAutoLogger(config.LOCAL_FOLDER_STORAGE_FOLDER,config.DRIVE_STORAGE_FOLDER_ID);
			autoLoger.startLogSync();
		}
		return instance;

	}

	public static void CloseLog() {
		System.out.println("CLOSING LOG");
		
		if (instance != null) {
			Handler[] handlers = instance.message.getHandlers();

			for (int i = 0; i < handlers.length; i++) {
				handlers[i].close();
				instance.message.removeHandler(handlers[i]);
			}
		}
		AutomaticLogging autoLogger = AutomaticLogging.getLogger();
		try {
			if(logFolder != null){
				autoLogger.startTrackingLog(logFolder);
				autoLogger.saveLogDataToFolders();
				autoLogger.startLogSync();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// closes the current log and creates a new one
	public void startNewLog() {
		// TODO send signal to vision to start a new log also
		CloseLog();
		CreateLog();
	}

	public static void CreateLog() {
		getInstance();

		if (instance != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			logFolder = new File(logDir, df.format(new Date()));
			logFolder.mkdirs();

			File msgFile = new File(logFolder, "messages.log");
			try {
				sensor = new SensorLogger(logFolder, new Date());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				msgFile.createNewFile();
				// TODO fix Gui.UpdateLogName(msgFile.getName());

				Handler handler = new StreamHandler(new FileOutputStream(
						msgFile), new SimpleFormatter());
				instance.message.addHandler(handler);
				System.out.println("Created Log File: "
						+ msgFile.getAbsolutePath());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException("Could not open message log file ("
						+ msgFile + ")!");
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("Could not create new file ("
						+ msgFile + ")!");
			}
		}
	}

	private RobotLogger(File logdir) throws Exception {
		this.message = Logger.getLogger("RoboBuggy");
		this.sensor = null;
	}
}
