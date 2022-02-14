package com.botw.utils;

import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.MonthDay;
import java.util.Calendar;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.botw.fileProcessing.CSVGeneation;
import com.botw.testcase.SchedulerRead;

public class MopScript extends SetEnv {

	public String fnAmpersand(String value, String bodDate, DateFormat dateFormat) {
		try {

			String[] splitBod = bodDate.split("-");

			int day = Integer.parseInt(splitBod[2]);
			int month = Integer.parseInt(splitBod[1]);
			int year = Integer.parseInt(splitBod[0]);

			int keyYear = 0;
			int keyMonth = 0;
			int keyDay = 0;

			String[] splitkeyDate;
			if (value.indexOf("/") != -1) {
				splitkeyDate = value.split("/");
			} else {
				splitkeyDate = value.split(",");

				for (String date : splitkeyDate) {
					if (date.contains("D")) {
						if (date.contains("+")) {
							keyDay = keyDay + Integer.parseInt(date.substring(date.indexOf("+") + 1));
						} else if (date.contains("-")) {
							keyDay = keyDay + Integer.parseInt(date.substring(date.indexOf("-") + 1));
						}
					} else if (date.contains("M")) {
						if (date.contains("+")) {
							keyMonth = keyMonth + Integer.parseInt(date.substring(date.indexOf("+") + 1));
						} else if (date.contains("-")) {
							keyMonth = keyMonth + Integer.parseInt(date.substring(date.indexOf("-") + 1));
						}

					} else if (date.contains("Y")) {
						if (date.contains("+")) {
							keyYear = keyYear + Integer.parseInt(date.substring(date.indexOf("+") + 1));
						} else if (date.contains("-")) {
							keyYear = keyYear + Integer.parseInt(date.substring(date.indexOf("-") + 1));
						}
					}

				}

				if (value.equalsIgnoreCase("D,M,Y") || value.equalsIgnoreCase("D/M/Y")) {
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				} else if (value.equalsIgnoreCase("M,D,Y") || value.equalsIgnoreCase("M/D/Y")
						|| value.equalsIgnoreCase("M" + keyMonth + ",D,Y")
						|| value.equalsIgnoreCase("M" + keyMonth + "D,Y")
						|| value.equalsIgnoreCase("M,D" + keyDay + ",Y") || value.equalsIgnoreCase("M/D/Y" + keyYear)
						|| value.equalsIgnoreCase("M,D,Y" + keyYear) || value.equalsIgnoreCase("M,D,Y" + keyYear)) {
					dateFormat = new SimpleDateFormat("MM-dd-yyyy");
				}

				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.DAY_OF_MONTH, day);
				cal.set(Calendar.MONTH, month - 1);
				cal.set(Calendar.YEAR, year);
				cal.set(Calendar.YEAR, keyYear);
				cal.set(Calendar.MONTH, keyMonth);
				cal.set(Calendar.DAY_OF_MONTH, keyDay);
				return dateFormat.format(cal.getTime());
			}
		} catch (Exception e) {
			System.out.println("Error Occured while calculating the date");
			e.printStackTrace();
		}
		return bodDate;
	}

	public String fnAdd(String Value) {
		if (Value.contains("#")) {
			Value = Value.substring(Value.indexOf("#") + 1);
			Value = readTempIP(Value);
		} else if (Value.contains("%")) {
			Value = Value.substring(Value.indexOf("%"));
			Value = DBIExcels(Value);
		} else if (Value.startsWith("&")) {
			Value = Value.substring(Value.indexOf("&") + 1);
			Value = fnAmpersand(Value,CSVGeneation.readEnvExcel.get("BOD"));
		}
		return Value;
	}

	public String fnAmpersand(String value, String bodDate) {
		String[] splitBod = bodDate.split("-");
		int day = Integer.parseInt(splitBod[2]);
		int month = Integer.parseInt(splitBod[1]);
		int year = Integer.parseInt(splitBod[0]);

		try {
			int keyYear = 0;
			int keyMonth = 0;
			int keyDay = 0;

			String[] splitkeyDate;
			if (value.indexOf("/") != -1) {
				splitkeyDate = value.split("/");
			} else {
				splitkeyDate = value.split(",");

				for (String date : splitkeyDate) {
					if (date.contains("D")) {
						if (date.contains("+")) {
							keyDay = keyDay + Integer.parseInt(date.substring(date.indexOf("+") + 1));
						} else if (date.contains("-")) {
							keyDay = keyDay + Integer.parseInt(date.substring(date.indexOf("-") + 1));
						}
					} else if (date.contains("M")) {
						if (date.contains("+")) {
							keyMonth = keyMonth + Integer.parseInt(date.substring(date.indexOf("+") + 1));
						} else if (date.contains("-")) {
							keyMonth = keyMonth + Integer.parseInt(date.substring(date.indexOf("-") + 1));
						}

					} else if (date.contains("Y")) {
						if (date.contains("+")) {
							keyYear = keyYear + Integer.parseInt(date.substring(date.indexOf("+") + 1));
						} else if (date.contains("-")) {
							keyYear = keyYear + Integer.parseInt(date.substring(date.indexOf("-") + 1));
						}
					}
				}
			}

			DateFormat dateFormat;
			if (value.contains("Y,M,D")) {
				dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			} else {
				dateFormat = new SimpleDateFormat("MM-dd-yyyy");
			}
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH, day);
			cal.set(Calendar.MONTH, month - 1);
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.YEAR, keyYear);
			cal.set(Calendar.MONTH, keyMonth);
			cal.set(Calendar.DAY_OF_MONTH, keyDay);
			return dateFormat.format(cal.getTime());

		} catch (Exception e) {
			System.out.println("Error Occured while calculating the date");
			e.printStackTrace();
		}
		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.YEAR, year);
		return dateFormat.format(cal.getTime());
	}

	public String DBIExcels(String Value) {
		int lastUsedColCount;
		int colNum = 0;
		int rowNum = 0;
		int cellNum = 0;

		String colName = "";
		String returnVal = "";
		if (Value.contains("-")) {
			colName = Value.substring(0, Value.indexOf("-"));
			cellNum = Integer.parseInt(Value.substring(Value.indexOf("-")));
		}
		try {
			setUpFile = new FileInputStream(Constants.DBSHEET);
			setUpBook = new XSSFWorkbook(setUpFile);
			setUpSheet = setUpBook.getSheetAt(0);

			XSSFRow row = setUpSheet.getRow(0);
			lastUsedColCount = row.getLastCellNum();

			for (int i = 0; i < lastUsedColCount; i++) {
				String colValue = row.getCell(i + 1).getStringCellValue();
				if (colValue.toLowerCase().equals(colName.toLowerCase())) {
					colNum = i + 1;
					try {
						XSSFRow row1 = setUpSheet.getRow(cellNum);
						XSSFCell cell = row1.getCell(colNum);
						cell.setCellType(CellType.STRING);
						returnVal = cell.getStringCellValue();
					} catch (Exception e) {
						System.err.println("INFO-----The Cell is empty" + cellNum);
						return returnVal;
					}
				}

			}
		} catch (Exception e) {
			System.err.println("TEMP file is not found ");
			System.out.println(e);
		}

		if (colNum == 0) {
			System.err.println("INFO-----The column is not found in DB set Up Sheet" + colName);
			return returnVal;
		}
		return returnVal;
	}

	public String readTempIP(String value) {
		FileInputStream readTempInputStream;
		XSSFWorkbook readWb;
		XSSFSheet readTempSheet;
		int lastusedColCount;
		int colNum = 0, rowNum = 0;
		String colName, rowName, returnVal = "";
		if (value.contains(",")) {
			rowName = value.substring(0, value.indexOf(","));
			colName = value.substring(value.indexOf(",") + 1);
		} else {
			colName = value;
			rowName = SchedulerRead.TCID;
		}
		try {
			File file = new File(Constants.TEMP_PATH + "TEMPID.xslx");
			readTempInputStream = new FileInputStream(file);
			readWb = new XSSFWorkbook(readTempInputStream);
			readTempSheet = readWb.getSheetAt(0);

			int lastUsedRowCount = readTempSheet.getLastRowNum();
			XSSFRow row = readTempSheet.getRow(0);
			if (lastUsedRowCount == 0) {
				lastusedColCount = 0;
			} else
				lastusedColCount = row.getLastCellNum();

			for (int i = 0; i < lastusedColCount; i++) {
				String colValue = row.getCell(i + 1).getStringCellValue();
				if (colValue.toLowerCase().equals(colName.toLowerCase())) {
					colNum = i + 1;
					for (int j = 0; j < lastUsedRowCount; j++) {
						try {
							XSSFRow row1 = readTempSheet.getRow(j);
							String rowValue = row1.getCell(0).getStringCellValue();

							if (rowValue.toLowerCase().equals(rowName.toLowerCase())) {
								rowNum = j;
								returnVal = readTempSheet.getRow(rowNum).getCell(colNum).getStringCellValue();
								StatusFlag.testResults.put("Spooled Data : " + value + " :" + returnVal, "PASS");
								return returnVal;
							}

						} catch (Exception e) {
							continue;
						}
						rowNum = 0;
						readTempInputStream.close();
					}
					if (rowNum == 0) {
						System.err.println("INFO------- The row is not found in Temp " + rowName);
						return returnVal;
					}
				}
			}

		} catch (Exception e) {
			System.err.println("TEMP file not found");
			System.out.println(e);
		}
		if (colNum == 0) {
			System.err.println("INFO------- The column is not found in Temp" + colName);
			return returnVal;
		}
		return returnVal;
	}

}
