package com.botw.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class Constants {

	public static final String CONFIGPATH = System.getProperty("user.dir")
			+ "\\src\\com\\botw\\config\\Config.properties";
	public static Properties configenv;
	public static String schedulerPath;
	public static Properties prop;
	public static String env;

	static {
		FileInputStream input;
		try {
			input = new FileInputStream(new File(CONFIGPATH));
			prop = new Properties();
			prop.load(input);

			env = prop.getProperty("Environment");
			System.out.println("Execution started in  " + env + "environment");
			configenv = new Properties();
			FileInputStream inputenv = new FileInputStream(new File(
					System.getProperty("user.dir") + "\\src\\com\\botw\\config\\Config_" + env + ".properties"));
			configenv.load(inputenv);

			if (prop.getProperty("Execution").equalsIgnoreCase("Regression")) {
				schedulerPath = System.getProperty("user.dir") + "\\Excels\\Scheduler.xlsx";
			} else if (prop.getProperty("Execution").equalsIgnoreCase("Sanity")) {
				schedulerPath = System.getProperty("user.dir") + "\\Excels\\Scheduler_Sanity.xlsx";
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static final String PROJECTPATH = System.getProperty("user.dir");
	public static final String RESULTPATH = PROJECTPATH + "\\Results\\";
	public static final String RESULTFILE = RESULTPATH + "RESULTS.xlsx";
	public static final String RESULT_RUNID = RESULTPATH + "RUN_SEQ.txt";
	public static final String UPLOAD_FILE_GENERATION_PATH=PROJECTPATH + "\\Excels\\UploadFileGeneration.xlsx";
	public static final String CSVCHKPR_REF_PATH=PROJECTPATH+"\\Uploads\\CSVCHKPRRef.txt";
	public static final String CSVCHKPR_BASE_PATH=PROJECTPATH+"\\Uploads\\BASECSVCHKPR.txt";
	public static String CURRENT_RESULT_PATH;
	public static String CURRENT_SUB_DIR;
	public static final String SEQFILEPATH=PROJECTPATH+"\\Uploads"+"SEQ.txt";
	public static final String SETENVFILE_PATH=PROJECTPATH+"Excels\\SetUp_"+env+".xlsx";
	public static final String TEMP_PATH=PROJECTPATH+"\\TEMP\\";
	public static final String DBSHEET=PROJECTPATH +"\\DBExcels\\DBSetup_ "+env+ ".xlsx";

}
