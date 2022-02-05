package com.botw.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ResultHelper {
	
	String reqID;
	String screenshotPath;
	
	public String generateRunID() {
		String filename= Constants.RESULT_RUNID;
		BufferedReader br = null;
		try {
			br= new BufferedReader(new FileReader(filename));
			reqID=br.readLine();
		} catch (Exception e) {
			System.out.println("Error occured while reading from a file : " + filename);
			e.printStackTrace();
		}finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			PrintWriter writer= new PrintWriter(filename , "UTF-8");
			writer.println(String.valueOf(Integer.parseInt(reqID)+1));
			writer.close();
		} catch (Exception e) {
			System.out.println("Error occured while reading from a file : " + filename);
			e.printStackTrace();
		}
		
		return reqID;
	}
	
	public void createRunFolder() {
		String runID= generateRunID();
		DateFormat date = new SimpleDateFormat("MMM-dd-yyyy HH:mm:ss");
		String folder = date.format(new Date());
		
		File file = new File(Constants.RESULTPATH+"RUNID-" + runID+ " " +folder);
		try {
			Boolean filecreation= file.createNewFile();
			if (filecreation) {
				screenshotPath=file.getPath();
			}else {
				if (file.exists()) {
					screenshotPath= file.getPath();
				}
			}
		} catch (IOException e) {
			System.out.println("Exception while creating new file " +file);
			e.printStackTrace();
		}
		
		
	}
	

}
