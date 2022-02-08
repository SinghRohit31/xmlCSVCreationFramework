package com.botw.fileProcessing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.botw.testcase.SchedulerRead;
import com.botw.utils.Constants;
import com.botw.utils.MopScript;
import com.botw.utils.SetEnv;
import com.botw.utils.StatusFlag;

public class CSVGeneation extends SchedulerRead {

	String TCID;
	String SNO;
	Map<String, String>  readEnvExcel=new HashMap<>();
	
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

	public String csvFileGenerator(String File_Name, int No_Of_Recs, String allData, String File_Type) throws NumberFormatException, IOException {
		MopScript mpScript= new MopScript();
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
		String[] reqIDFields= {"PaymentRefID","RQUID","PaymentRefNo"};
		String[] dateFields= {"PrcDt","Duedt","CSVCHKPRdt"};
		

		DateFormat dateformat = null;
		DateFormat dateTimeaformat = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		Date date = new Date();
		
		//converting the date as per requirement
		String bodDate=readEnvExcel.get("BOD");
		String[] bodDates=bodDate.split("/");
		bodDate= bodDates[2] +"-" +bodDates[1] +"-"+bodDates[0];

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
				
				if(org.apache.commons.lang3.StringUtils.indexOfAny(newData, reqIDFields)==-1) {
					if (File_Type.toLowerCase().equals("csv")) {
						newData="RqUID:" + String.format("%036d", Integer.parseInt(csvGen.reqID(Constants.SEQFILEPATH))+","+newData);
					}else if(File_Type.toLowerCase().equals("CSVCHKPR") ||File_Type.toLowerCase().equals("CSVACH")){
						newData="PaymentRefNo:" + String.format("%035d", Integer.parseInt(csvGen.reqID(Constants.SEQFILEPATH))+","+newData);
					}
				}
				
				if(org.apache.commons.lang3.StringUtils.indexOfAny(newData, dateFields)==-1) {
					if (File_Type.toLowerCase().equals("csv")) {
						newData="PrcDt:" + bodDate+"," + newData;
					}else if(File_Type.toLowerCase().equals("CSVCHKPR") ||File_Type.toLowerCase().equals("CSVACH")){
						newData="CSVCHKPRdt:" +  mpScript.fnAmpersand("M/D/Y", bodDate, dateformat);
					}
				}
				
				
				
				
			}
			
			

		}
	}
	
	public void getDate(String solution) {
		if(Constants.PROJECTPATH !=null) {
			SetEnv.setExcelFile(Constants.SETENVFILE_PATH, solution);
			readEnvExcel=SetEnv.storeSetUpData();
			
			SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/YYYY");
			Date date = new Date();
			System.out.println("INFO...BOD is " + dateFormat.format(date)+"in dd/MM/YYYY format..");
			readEnvExcel.put("BOD",dateFormat.format(date));
		}
	}
	
	public String reqID(String filename) throws IOException {
		CSVGeneation csvGenLocal= new CSVGeneation();
		String reqID=csvGenLocal.readFile(filename);
		csvGenLocal.writeFile(filename,String.valueOf(Integer.parseInt(reqID+1)));
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
