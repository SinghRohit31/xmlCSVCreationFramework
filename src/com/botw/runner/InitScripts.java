package com.botw.runner;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import com.botw.utils.Constants;
import com.botw.utils.ResultHelper;

public class InitScripts {

	public static HashMap<String, String> readExcels;
	public static String startTimestamp;

	public static void main(String[] args) {
		InitScripts init = new InitScripts();

		try {
			startTimestamp= new SimpleDateFormat("yyyy-mm-dd HH:mm:ss").format(Calendar.getInstance().getTime());
			
			System.out.println("Execution Start time  :" + startTimestamp);
			System.out.println("INFO.....Reading Scheduler file.");
			
			clearLatestRunResultsCopy();
			
			ResultHelper helper = new ResultHelper();
			helper.createRunFolder();
			
			
			
			
			
			
			
			
			
			
			
			
		} catch (Exception e) {
		}

	}

	public static void clearLatestRunResultsCopy() {
		try {
			File dir = new File(Constants.RESULTPATH + "LatestRunResultsCopy");
			for (File file : dir.listFiles()) {
				if(!(file.isDirectory())) {
					file.delete();
				}
			}
		} catch (NullPointerException e) {
			System.err.println("Warning--> Latest Results folder/Files not found for deleting..continuing execution..");
			e.printStackTrace();
		}
	}
	
}
