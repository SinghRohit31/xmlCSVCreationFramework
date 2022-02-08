package com.botw.utils;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SetEnv {
	static FileInputStream setUpFile;;
	static XSSFWorkbook setUpBook;
	static XSSFSheet setUpSheet;
	static Map<String, String> InitMemory= new HashMap<String, String>();
	
	
	public static  void setExcelFile(String path,String Sheetname) {
		try {
			setUpFile= new FileInputStream(path);
			setUpBook= new XSSFWorkbook(setUpFile);
			setUpSheet=setUpBook.getSheet(Sheetname);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static Map<String, String> storeSetUpData() {
		for (int i = 0; i < setUpSheet.getLastRowNum(); i++) {
			XSSFRow row= setUpSheet.getRow(i+1);
			
			CellType celltype=row.getCell(0).getCellType();
			CellType celltype1=row.getCell(1).getCellType();
			
			
			switch (celltype1) {
			case STRING: row.getCell(0).getStringCellValue(); row.getCell(1).getStringCellValue();
			InitMemory.put(row.getCell(0).getStringCellValue(), row.getCell(1).getStringCellValue());
			break;
			
			case NUMERIC:String value = Double.toString(row.getCell(1).getNumericCellValue());
			row.getCell(0).getStringCellValue();
			InitMemory.put(row.getCell(0).getStringCellValue(), value);
			break;
			}
			
		}
		System.out.println("INFO ------SetUp environment is read and Stored in Memory");			
		
		return InitMemory;
	}

}
