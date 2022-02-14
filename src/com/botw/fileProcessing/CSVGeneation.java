package com.botw.fileProcessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.botw.testcase.SchedulerRead;
import com.botw.utils.Constants;
import com.botw.utils.MopScript;
import com.botw.utils.SetEnv;
import com.botw.utils.StatusFlag;

public class CSVGeneation extends SchedulerRead {

	String TCID;
	String SNO;
	public static Map<String, String> readEnvExcel = new HashMap<>();

	public CSVGeneation() {
		this.TCID = TCID;
		this.SNO = SNO;
	}

	public static void main(String[] args) {
		CSVGeneation csvMain = new CSVGeneation();
		String File_Name = "";
		int No_Of_Recs = 1;

		String allData = "";
		String File_Type = "";

	}

	public String csvFileGenerator(String File_Name, int No_Of_Recs, String allData, String File_Type)
			throws NumberFormatException, IOException {
		MopScript mpScript = new MopScript();
		CSVGeneation csvGen = new CSVGeneation();
		StringBuilder sb = new StringBuilder();

		String ref_File_Data = null;
		String base_File_data = null;
		String delimiter = null;
		String fileExtension = null;
		String prefix = null;
		String seqNum = null;
		String path = null;
		String stAcnt = null;
		String erpID = null;
		String[] reqIDFields = { "PaymentRefID", "RQUID", "PaymentRefNo" };
		String[] dateFields = { "PrcDt", "Duedt", "CSVCHKPRdt" };

		DateFormat dateformat = null;
		DateFormat dateTimeaformat = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		Date date = new Date();

		// converting the date as per requirement
		String bodDate = readEnvExcel.get("BOD");
		String[] bodDates = bodDate.split("/");
		bodDate = bodDates[2] + "-" + bodDates[1] + "-" + bodDates[0];

		try {
			switch (File_Type.toLowerCase()) {
			case "CSV":
				ref_File_Data = Constants.CSVCHKPR_REF_PATH;
				base_File_data = Constants.CSVCHKPR_BASE_PATH;
				stAcnt = // from Properties file
						delimiter = ",";
				path = Constants.CURRENT_SUB_DIR;
				dateformat = new SimpleDateFormat("yyyy-MM-dd");
				fileExtension = ".csv";
				prefix = "SecureDirect_ALL";
				break;
			case "CSVCHKPR":
				ref_File_Data = Constants.CSVCHKPR_REF_PATH;
				base_File_data = Constants.CSVCHKPR_BASE_PATH;
				stAcnt = "";
				erpID = "";
				dateformat = new SimpleDateFormat("yyyy-MM-dd");
				seqNum = "005";
				fileExtension = ".txt";
				delimiter = ",";
				path = Constants.CURRENT_SUB_DIR;
				break;

			}
		} catch (Exception e) {
			System.out.println("Exception Occured while reading the reference files");
			return null;
		}

		if (ref_File_Data.equals("") || base_File_data.equals("")) {
			return "";
		}

		String refFiedValues[] = ref_File_Data.split(",");
		String baseFieldValues[] = base_File_data.split(delimiter, -1); // overload function to ensure empty space is
																		// not removed

		int NoOfFields = refFiedValues.length;

		if (allData.contains("stAcnt")) {
			if (allData.contains(",")) {
				String[] arr = allData.split(",");
				for (int i = 0; i < arr.length; i++) {
					if (arr[i].contains("stAcnt")) {
						stAcnt = arr[i].split(":")[1];
					}

				}
			} else {
				stAcnt = allData.split(":")[1];
			}

			if (allData.contains("erpID")) {
				if (allData.contains(",")) {
					String[] arr = allData.split(",");
					for (int i = 0; i < arr.length; i++) {
						if (arr[i].contains("erpID")) {
							erpID = arr[i].split(":")[1];
						}

					}
				} else {
					erpID = allData.split(":")[1];
				}
			}

			if (File_Name.isEmpty()) {
				if (Constants.env.equals("QA1")) {
					File_Name = erpID + "." + seqNum + "." + dateTimeaformat.format(date) + "." + stAcnt + ".ph"
							+ fileExtension;
				} else if (Constants.env.equals("QA2")) {
					File_Name = erpID + "." + seqNum + "." + dateTimeaformat.format(date) + "." + stAcnt + ".ph"
							+ fileExtension;
				} else
					File_Name = erpID + "." + seqNum + "." + dateTimeaformat.format(date) + "." + stAcnt + ".ph"
							+ fileExtension;
			}

			String[] fileData = allData.split(";");
			String sb1 = null;

			for (int recCount = 0; recCount < No_Of_Recs; recCount++) {
				String newData;
				if (recCount < fileData.length) {
					newData = fileData[recCount];
				} else
					newData = "";

				if (org.apache.commons.lang3.StringUtils.indexOfAny(newData, reqIDFields) == -1) {
					if (File_Type.toLowerCase().equals("csv")) {
						newData = "RqUID:" + String.format("%036d",
								Integer.parseInt(csvGen.reqID(Constants.SEQFILEPATH)) + "," + newData);
					} else if (File_Type.toLowerCase().equals("CSVCHKPR") || File_Type.toLowerCase().equals("CSVACH")) {
						newData = "PaymentRefNo:" + String.format("%035d",
								Integer.parseInt(csvGen.reqID(Constants.SEQFILEPATH)) + "," + newData);
					}
				}

				if (org.apache.commons.lang3.StringUtils.indexOfAny(newData, dateFields) == -1) {
					if (File_Type.toLowerCase().equals("csv")) {
						newData = "PrcDt:" + bodDate + "," + newData;
					} else if (File_Type.toLowerCase().equals("CSVCHKPR") || File_Type.toLowerCase().equals("CSVACH")) {
						newData = "CSVCHKPRdt:" + mpScript.fnAmpersand("M/D/Y", bodDate, dateformat);
					}
				}

				// Adding ERP ID if not provided for check Printing scenario

				if (File_Type.toLowerCase().equalsIgnoreCase("CSVCHKPR")) {
					newData = "erpID:" + erpID + "," + newData;
				}

				String[] fieldValue = new String[NoOfFields];
				fieldValue = newData.split(",");
				String[] outputValues = new String[NoOfFields];

				for (int refCount = 0; refCount < refFiedValues.length; refCount++) {
					int flag = 0;
					for (int fieldCount = 0; fieldCount < fieldValue.length; fieldCount++) {
						if (refFiedValues[refCount].contentEquals(
								fieldValue[fieldCount].substring(0, fieldValue[fieldCount].indexOf(':')))) {
							if (refFiedValues[refCount].contentEquals(fieldValue[fieldCount].substring(
									fieldValue[fieldCount].indexOf('!') + 1, fieldValue[fieldCount].length()))) {
								flag = flag + 1;
							} else if ((fieldValue[fieldCount].split(":").length > 1)
									&& fieldValue[fieldCount].split(":")[1].startsWith("%")) {
								outputValues[refCount] = mpScript.fnAdd(fieldValue[fieldCount].substring(
										fieldValue[fieldCount].indexOf(":") + 1, fieldValue[fieldCount].length()));
								flag = flag + 1;
							} else {
								if (StringUtils.indexOfAny(refFiedValues[refCount], dateFields) > 0
										&& fieldValue[fieldCount].substring(fieldValue[fieldCount].indexOf(':') + 1,
												fieldValue[fieldCount].length()).contains("D")) {
									outputValues[refCount] = mpScript.fnAmpersand(
											fieldValue[fieldCount].substring(fieldValue[fieldCount].indexOf(':') + 1,
													fieldValue[fieldCount].length()),
											bodDate, dateformat);
									flag = flag + 1;
								} else {
									outputValues[refCount] = fieldValue[fieldCount].substring(
											fieldValue[fieldCount].indexOf(':') + 1, fieldValue[fieldCount].length());
									flag = flag + 1;

								}
							}
						}

					}

					if (flag == 0) {
						outputValues[refCount] = baseFieldValues[refCount];
					}
				}
				for (int arrayCount = 0; arrayCount < outputValues.length; arrayCount++) {
					if (outputValues[arrayCount] != null) {
						sb.append(outputValues[arrayCount]);
						sb.append(delimiter).trimToSize();
					}

				}
				System.setProperty("line.separator", "\n");
				sb.append(System.getProperty("line.separator"));
				System.out.println(recCount);

			}

		}
		System.out.println(Constants.CURRENT_SUB_DIR + File_Name);
		csvGen.writeFile(Constants.CURRENT_SUB_DIR + File_Name, sb.toString().trim());
		return File_Name;
	}

	public String csvExtractor(String fieldName,String reqID,String fileType) throws IOException {
		String file_Name=""; String refFile=null; Boolean rowFound=null;String delimitor=null;
		String[] nonTrasactionBasedfiles= {"CSVCHKPRACK","CSVCHKPRNACK"};
		String uploadPath=Constants.UPLOAD_FILE_GENERATION_PATH;
		FileInputStream input = new FileInputStream(new File(uploadPath));
		XSSFWorkbook uploadWb= new XSSFWorkbook(input);
		XSSFSheet sheet= uploadWb.getSheetAt(0);
		String path=null;
		int rowCount=sheet.getLastRowNum();

		for (int i = 0; i < rowCount; i++) {
			XSSFRow row= sheet.getRow(i);
			String TCID=row.getCell(0).getStringCellValue();
			if(TCID.toUpperCase().equals(SchedulerRead.TCID.toUpperCase())) {
				if(row.getCell(8)!=null && !row.getCell(8).getStringCellValue().isEmpty()) {
					rowFound=true;
					file_Name=row.getCell(8).getStringCellValue();
					
				}
			}
			
		}
		if(uploadPath!=null) {
			input.close();
		}
		
		HashMap<String, String> transactionData= new HashMap<>();
		CSVGeneation csvGen= new CSVGeneation();
		
		try {
			switch (fileType.toLowerCase()) {
			case "CSV":
				refFile=csvGen.readFile(Constants.CSVCHKPR_REF_PATH);
				delimitor=",";
				path=Constants.CURRENT_SUB_DIR;
				break;
				
			case "CSVCHKPR":
				refFile=csvGen.readFile(Constants.CSVCHKPR_REF_PATH);
				delimitor=",";
				path=Constants.CURRENT_SUB_DIR;
				break;
			}
		} catch (Exception e) {
			System.out.println("Exception Occured while reading the reference files");
			return null;
		}
		
		if (refFile.equals("")) {
			return "";
		}
		
		String[] refFieldValues=refFile.split(",");
		
		// for Non transaction date files- like ACK and NACK
		
		if(StringUtils.indexOfAny(fileType.toLowerCase(),nonTrasactionBasedfiles)>=0) {
			String fileContent = csvGen.readFile(path+file_Name);
			String[] recordValues= fileContent.split(delimitor,-1);
			 for (int count = 0; count < refFieldValues.length; count++) {
				 transactionData.put(refFieldValues[count], recordValues[count]);
			}
			 String resultValue=transactionData.get(fieldName);
			 return resultValue;
		}
		
		String[] chkPRTransFields= {"TransCount","TotalAmt","Ccy"};
		FileReader fileReader= new FileReader(path+file_Name);
		BufferedReader br= new BufferedReader(fileReader);
		String sline=new String();
		while ((sline=br.readLine())!=null) {
			String[] transactionRecordValue=sline.split(delimitor,-1);
			
			if(transactionRecordValue.length==3 && StringUtils.indexOfAny(fieldName,chkPRTransFields)>=0 && fileType.toLowerCase().contentEquals("CSVCHKPRRES")) {
				String ref_File = csvGen.readFile(path+file_Name);// response reference file path
				refFieldValues= ref_File.split(delimitor,-1);
				 for (int count = 0; count < 3; count++) {
					 transactionData.put(refFieldValues[count], transactionRecordValue[count]);
				}
				 String resultValue=transactionData.get(fieldName);
				 br.close();
				 fileReader.close();
				 return resultValue;
			}else if(StringUtils.indexOfAny(fieldName, chkPRTransFields)==-1) {
				for (int count = 0; count < refFieldValues.length; count++) {
					transactionData.put(refFieldValues[count], transactionRecordValue[count]);
				}
				switch(fileType.toLowerCase()) {
				case "CSV" : if(reqID.contentEquals(transactionData.get("reqID"))){
									String resultVlue=transactionData.get(fieldName);
									br.close();
									fileReader.close();
									return resultVlue;}break;
				case "CSVCHKPR" : if(reqID.contentEquals(transactionData.get("PaymentRefNo"))){
								String resultVlue=transactionData.get(fieldName);
								br.close();
								fileReader.close();
								return resultVlue;}break;
									
				}
			}
			
		}
		br.close();
		fileReader.close();
		return "";
	}
	
	
	
	
	public static void getDate(String solution) {
		if (Constants.PROJECTPATH != null) {
			SetEnv.setExcelFile(Constants.SETENVFILE_PATH, solution);
			readEnvExcel = SetEnv.storeSetUpData();

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
			Date date = new Date();
			System.out.println("INFO...BOD is " + dateFormat.format(date) + "in dd/MM/YYYY format..");
			readEnvExcel.put("BOD", dateFormat.format(date));
		}
	}

	public String reqID(String filename) throws IOException {
		CSVGeneation csvGenLocal = new CSVGeneation();
		String reqID = csvGenLocal.readFile(filename);
		csvGenLocal.writeFile(filename, String.valueOf(Integer.parseInt(reqID + 1)));
		return reqID;

	}

	public String readFile(String fileName) throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
			String fileContent = br.readLine();
			return fileContent;
		} catch (Exception e) {
			System.out.println("Error occured while reading from a file" + fileName);
			e.printStackTrace();
			StatusFlag.testResults.put("Error occured while reading from a file : " + fileName
					+ " . Please check if file is available in the path", "ERROR");
			return "";
		} finally {
			br.close();
		}
	}

	public void writeFile(String fileName, String content) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(fileName, "UTF-8");
			System.setProperty("line.separator", "\n");
			writer.println(content);
		} catch (Exception e) {
			System.out.println("Error Occured while writing to a file : " + fileName);
			e.printStackTrace();
		} finally {
			writer.close();
		}

	}

}
