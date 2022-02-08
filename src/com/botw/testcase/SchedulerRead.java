package com.botw.testcase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.botw.utils.Constants;
import com.botw.utils.ResultHelper;
import com.botw.utils.StatusFlag;

public class SchedulerRead {
	FileInputStream input;
	XSSFWorkbook wb;
	XSSFSheet sheet;
	XSSFRow row;
	XSSFCell cell;

	public static int currentTC, TCStart, icounter, rootSkip;
	public static String TCDesc, Flag, TCID, SeqNo, solutionName, currentSolution,SNO;
	public static int schedulerCount = 0;
	public static String rootSkipFlag = "";
	public static String rootSkipTCCount = "";
	public static int noOfFileRecords=0;

	public void readTC(String TestTypes) {
		try {
			input = new FileInputStream(new File(Constants.schedulerPath));
			wb = new XSSFWorkbook(input);
			sheet = wb.getSheet(TestTypes);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("ERR INFO ----> Scheduler file not found");
			System.exit(0);
		}

		try {
			for (int i = 0; i <= sheet.getLastRowNum(); i++) {
				int StoringTCStart = (int) sheet.getRow(i + 1).getCell(0).getNumericCellValue();
				if (StoringTCStart > 0) {
					TCStart = StoringTCStart;
				}
				TCDesc = sheet.getRow(i + 1).getCell(7).getStringCellValue();
				try {
					Flag = sheet.getRow(i + 1).getCell(7).getStringCellValue();
				} catch (Exception e) {
					if (Flag.equals(null)) {
						continue;
					}
				}

				try {
					if (Flag.toUpperCase().equalsIgnoreCase("Y")) {
						schedulerCount++;
						TCID = sheet.getRow(i + 1).getCell(2).getStringCellValue().trim();
						SNO= sheet.getRow(i + 1).getCell(3).getStringCellValue().trim();
						SeqNo = SNO;
						String menu = sheet.getRow(i + 1).getCell(4).getStringCellValue().trim();
						solutionName = sheet.getRow(i + 1).getCell(5).getStringCellValue().trim();
						icounter = 0;

						if (sheet.getRow(i + 1).getCell(7) != null) {
							TCDesc = sheet.getRow(i + 1).getCell(7).getStringCellValue().trim();
							icounter = 1;
						}
						if (icounter == 0) {
							TCDesc = "";

						}
						System.out.println("-->Started TC execution for Tesr Case ID -> " + TCID + " and Serial No"
								+ SNO + " <----");
						StatusFlag.testResults.put("TCID", TCID);
						StatusFlag.testResults.put("USERID", menu);
						StatusFlag.testResults.put("SNO", SNO);
						StatusFlag.testResults.put("TEST Desciption", TCDesc);

						String ProcessSolution = "";
						if (solutionName.indexOf("-") != -1) {
							ProcessSolution = solutionName.substring(0, solutionName.indexOf("-")).trim();
						} else {
							ProcessSolution = solutionName;
						}

						if (!solutionName.toUpperCase().contains("CORE")) {
							StatusFlag.testResults.put("Menu", solutionName);
						}

						StatusFlag.startTime();
						currentSolution = ProcessSolution;
						currentTC = TCStart;
						ResultHelper.createSubDirectory(TCID);
					}

					if (rootSkipFlag.equals("Y") && rootSkipTCCount.equals(Integer.toString(TCStart))) {
						rootSkip = 1;
						if (solutionName.toUpperCase().contains("CORE")) {
							StatusFlag.testResults.put("Menu", solutionName);
							StatusFlag.testResults.put("UserID", "User1");

						}
						StatusFlag.endTime();
						StatusFlag.writeTestResults();
						continue;
					} else {
						rootSkipFlag = "";
						rootSkipTCCount = "";
						rootSkip = 0;
					}
					
					switch (solutionName.toUpperCase(){
					case "PAYMENTS-FILE-GEN":
						File_Generation(TCID,SNO);
						break;
					case "PAYMENTS-FILE-UPLOAD":
						File_Upload(TCID,SNO);
						break;
					case "PAYMENTS-FILE-VAL":
						File_Val(TCID,SNO);
						break;	
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void File_Generation(String TCID,String SNO) throws IOException {
		String uploadFilePath=Constants.UPLOAD_FILE_GENERATION_PATH;
		
		
		File file = new File(uploadFilePath);
		FileInputStream uploadInput= new FileInputStream(file);
		XSSFWorkbook uploadWb = new XSSFWorkbook(uploadInput);
		XSSFSheet uploadSheet= uploadWb.getSheetAt(0);
		
		
		String fileName,allData,FileType = null,outFileName,Info=null;
		int NoOfRecs,rowNumber=0;
		Boolean rowFound=false;
		
		int rowCount=uploadSheet.getLastRowNum();
		
		
		for (int i = 1; i <= rowCount; i++) {
			XSSFRow row = uploadSheet.getRow(i);
			String testcaseName=row.getCell(0).getStringCellValue();
			String serNo = row.getCell(1).getStringCellValue();
			
			if (testcaseName.toUpperCase().equals(TCID.toUpperCase()) && serNo.toUpperCase().equals(SNO.toUpperCase())) {
				rowFound=true;
				rowNumber=i;
				
				System.out.println("INFO ----- The upload File Generation data found at row :  " +i);
				try {
					fileName=row.getCell(2).getStringCellValue();
				} catch (Exception e) {
					fileName="";
				}
				
				NoOfRecs=(int) row.getCell(3).getNumericCellValue();
				noOfFileRecords=NoOfRecs;
				
				FileType=row.getCell(4).getStringCellValue();
				allData=row.getCell(5).getStringCellValue();
				break;
			}
		}
		
		if (FileType.toLowerCase().equals("CSV") || FileType.toLowerCase().equals("CSVCHKPR")) {
			
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}

}
