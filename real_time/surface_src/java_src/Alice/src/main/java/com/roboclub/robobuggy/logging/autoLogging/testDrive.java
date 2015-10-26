package com.roboclub.robobuggy.logging.autoLogging;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.roboclub.robobuggy.ui.Gui;

public class testDrive {
	static ServerCommunication test;

	public static void main(String[] args) {
		File whereToSave = new File("LOG_FILES");
		String DriveStorageFolder_id ="";
		try {
			autoLogging autoLogger = new autoLogging(whereToSave, DriveStorageFolder_id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		try {
			final URL url = new URL("http://www.google.com");
			url.openConnection();
			ServerCommunication drive = ServerCommunication.getInstance();
			final java.io.File UPLOAD_FILE = new java.io.File("LOG_FILES");
			try {
				drive.addFolderToDrive(UPLOAD_FILE);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (MalformedURLException e1) {
			System.out.println("Can not connect to internet will try again later");
			e1.printStackTrace();
		} catch (IOException e) {
			System.out.println("Can not connect to internet will try again later");
			e.printStackTrace();
		}  
		*/
		
	}

}
