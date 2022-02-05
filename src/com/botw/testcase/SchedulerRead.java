package com.botw.testcase;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.botw.utils.Constants;

public class SchedulerRead {
	FileInputStream input;
	XSSFWorkbook wb;
	XSSFSheet sheet;
	XSSFRow row;
	XSSFCell cell;

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

			}
		} catch (Exception e) {
		}

	}

}
