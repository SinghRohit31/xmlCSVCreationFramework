package com.botw.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.NumberFormat.Style;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookType;

import com.botw.testcase.SchedulerRead;

public class StatusFlag {

	public static Date startTime;
	public static Date endTime;

	public static HashMap<String, String> testResults = new HashMap<String, String>();

	public static void startTime() {
		startTime = new Date();
		DateFormat format = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
		String executionStartTime = format.format(new Date());
		System.out.println(
				"Start Execution for TC ID : " + SchedulerRead.TCID + " Execution start time :" + executionStartTime);
		testResults.put("StartTIme", executionStartTime);
	}

	public static void endTime() {
		endTime = new Date();
		DateFormat format = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
		String executionEndTime = format.format(new Date());
		System.out.println(
				"end Execution for TC ID : " + SchedulerRead.TCID + " Execution end time :" + executionEndTime);
		testResults.put("EndTIme", executionEndTime);
	}

	public static String status() {
		int failCount = 0, passCount = 0, errorCount = 0;
		String TCStatus = "";
		if (SchedulerRead.rootSkip > 0) {
			TCStatus = "rootSkip";
			return TCStatus;
		}
		for (Map.Entry<String, String> map : testResults.entrySet()) {
			String status = map.getValue().toString();
			if (status.equalsIgnoreCase("SN_BOTW_PAYMENT_1")) {
				break;
			}

			if (status.toUpperCase().contains("FAIL")) {
				failCount=failCount+1;
			}
			if (status.toUpperCase().contains("PASS")) {
				passCount=passCount+1;
			}
			if (status.toUpperCase().contains("ERROR")) {
				errorCount= errorCount+1;
			}
		}
		
		if (failCount>=1) {
			TCStatus="FAIL";
			
		}else if (errorCount>=1) {
			TCStatus="ERROR";
		}else if (failCount==0 || passCount>=1) {
			TCStatus="PASS";
		}
		return TCStatus;
	}
	
	public static String validationDetails() {
		String validation="";
		if (SchedulerRead.rootSkip>0) {
			validation="Rootskipped Test case ..No Validation Performed";
			return validation;
		}
		
		for (Entry<String, String> map : testResults.entrySet()) {
			String key=map.getKey().toString();
			String value= map.getValue().toString();
			if(value.contains("PASS") || value.contains("Fail") || value.contains("Error")) {
				validation=validation+key+"\n";
			}
		}
		return validation;
	}
	
	public static String splooedData() {
		String data="";
		for (Map.Entry<String,String> map : testResults.entrySet()) {
			String key =map.getKey().toString();
			String value= map.getValue().toString();
			if (value.toUpperCase().contains("DATAS")) {
				data=data+"\n" +key;
			}
		}
		System.out.println(data);
		return data;
	}

	public static void writeTestResults() {
		String status =status();
		String validation= validationDetails();
		String spooled= splooedData();
		
		
		String path= Constants.CURRENT_RESULT_PATH+"//Results.xslx";
		File file = new File(path);
		
		FileInputStream input=null;
		XSSFWorkbook resultWb;
		XSSFSheet resultSheet;
		XSSFRow row;
		XSSFFont font,fontPass,fontFail;
		XSSFCellStyle style,style1,style2,style3,style4;
		
		try {
			if (file.exists()) {
				input=new FileInputStream(file);
				resultWb= new XSSFWorkbook(input);
				resultSheet=resultWb.getSheet("RESULTS");
			}else {
				resultWb= new XSSFWorkbook(input);
				resultSheet=resultWb.createSheet("RESULTS");
				row= resultSheet.createRow(0);
				row.createCell(0).setCellValue("TCID");
				row.createCell(1).setCellValue("SNO");
				row.createCell(2).setCellValue("STATUS");
				row.createCell(3).setCellValue("Payment Type");
				row.createCell(4).setCellValue("MENY");
				row.createCell(5).setCellValue("Start Execution time");
				row.createCell(6).setCellValue("End Execution time");
				row.createCell(7).setCellValue("Elapsed Time");
				row.createCell(8).setCellValue("Validations");
				row.createCell(9).setCellValue("FileTypes");
				row.createCell(10).setCellValue("Test Description");
				row.createCell(11).setCellValue("PO ID");
				row.createCell(12).setCellValue("Status");
				row.createCell(13).setCellValue("Failure Reason");
				row.createCell(14).setCellValue("DB Validation");
				row.createCell(15).setCellValue("Failure Reason");
				
				
				style=resultWb.createCellStyle();
				style.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
				style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				
				for (int i = 0; i < 16; i++) {
					XSSFCell cell = row.getCell(i);
					cell.setCellStyle(style);
				}
			}
			
			int lastRow=resultSheet.getLastRowNum();
			row=resultSheet.createRow(lastRow+1);
			
			style1=resultWb.createCellStyle();
			style2=resultWb.createCellStyle();
			style3=resultWb.createCellStyle();
			style4=resultWb.createCellStyle();
			
			
			style1.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
			style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			style2.setFillBackgroundColor(IndexedColors.RED.getIndex());
			style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			style3.setFillBackgroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
			style3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			style4.setFillBackgroundColor(IndexedColors.PINK.getIndex());
			style4.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			
			row.createCell(0).setCellValue(testResults.get("TCID").toString());
			row.createCell(1).setCellValue(testResults.get("SNO").toString());
			
			row.createCell(2).setCellValue(status);
			row.createCell(4).setCellValue(testResults.get("MENU").toString());
			
			row.createCell(5).setCellValue(testResults.get("StartTime").toString());
			row.createCell(6).setCellValue(testResults.get("endTime").toString());
			row.createCell(7).setCellValue(testResults.get("MENU").toString());
			row.createCell(8).setCellValue("TEXT(G"+(row.getRowNum()+1)+"-F"+(row.getRowNum()+1)+",\"hh:mm:ss\")");
			
			
			if(Constants.prop.getProperty("Mode").equals("Execution")) {
				row.createCell(3).setCellValue(testResults.get("USERID").toString().split("\\|")[0]);
				row.createCell(9).setCellValue(testResults.get("USERID").toString().split("\\|")[1]);
			}else {
				row.createCell(3).setCellValue("");
				row.createCell(9).setCellValue("");
			}
			   row.createCell(10).setCellValue(testResults.get("Test Description").toString());
			   
			   if(status.toUpperCase().equals("PASS")) {
				   for (int i = 0; i < 11; i++) {
					   XSSFCell cell = row.getCell(i);
					   cell.setCellStyle(style1);
				}
			   }else if(status.toUpperCase().equals("FAIL")) {
				   for (int i = 0; i < 11; i++) {
					   XSSFCell cell = row.getCell(i);
					   cell.setCellStyle(style2);
				}
			   }else if(SchedulerRead.rootSkip>0) {
				   for (int i = 0; i < 11; i++) {
					   XSSFCell cell = row.getCell(i);
					   cell.setCellStyle(style3);
				}
			   }else if(status.toUpperCase().equals("ERROR")) {
				   for (int i = 0; i < 11; i++) {
					   XSSFCell cell = row.getCell(i);
					   cell.setCellStyle(style4);
				}
			   }
			   
			   if(testResults!=null) {
				   testResults.clear();
			   }
			   
			FileOutputStream writeResults= new FileOutputStream(file);
			resultWb.write(writeResults);
			resultWb.close();
			
			  if(input==null) {
				   return;
			   }else input.close();
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
			System.err.println("not able to write test results in the Page");
		}
		
	}

}
