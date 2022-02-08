package com.botw.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MopScript {

	public String fnAmpersand(String value,String bodDate,DateFormat dateFormat) {
		try {
			
			String[] splitBod= bodDate.split("-");
			
			int day=Integer.parseInt(splitBod[2]);
			int month=Integer.parseInt(splitBod[1]);
			int year=Integer.parseInt(splitBod[0]);
			
			int keyYear=0;
			int keyMonth=0;
			int keyDay=0;
			
			String[] splitkeyDate;
			if(value.indexOf("/")!=-1) {
				splitkeyDate= value.split("/");
			}else {
				splitkeyDate=value.split(",");
				
				for (String date : splitkeyDate) {
					if(date.contains("D")) {
						if(date.contains("+")) {
							keyDay=keyDay+Integer.parseInt(date.substring(date.indexOf("+")+1));
						}else if(date.contains("-")) {
						keyDay=keyDay+Integer.parseInt(date.substring(date.indexOf("-")+1));
					}
				}else if(date.contains("M")) {
					if(date.contains("+")) {
						keyMonth=keyMonth+Integer.parseInt(date.substring(date.indexOf("+")+1));
					}else if(date.contains("-")) {
					 keyMonth=keyMonth+Integer.parseInt(date.substring(date.indexOf("-")+1));
				}
				
				}else if(date.contains("Y")) {
					if(date.contains("+")) {
						keyYear=keyYear+Integer.parseInt(date.substring(date.indexOf("+")+1));
					}else if(date.contains("-")) {
					 keyYear=keyYear+Integer.parseInt(date.substring(date.indexOf("-")+1));
				}
				}
					
				}
				
				if(value.equalsIgnoreCase("D,M,Y") || value.equalsIgnoreCase("D/M/Y")) {
					dateFormat=new SimpleDateFormat("dd-MM-yyyy");
				}else if(value.equalsIgnoreCase("M,D,Y") || value.equalsIgnoreCase("M/D/Y") ||
						value.equalsIgnoreCase("M" +keyMonth+",D,Y")||value.equalsIgnoreCase("M" + keyMonth+"D,Y") || value.equalsIgnoreCase("M,D"+keyDay+",Y")
						|| value.equalsIgnoreCase("M/D/Y"+keyYear) || value.equalsIgnoreCase("M,D,Y"+keyYear) || value.equalsIgnoreCase("M,D,Y" +keyYear)) {
					dateFormat= new SimpleDateFormat("MM-dd-yyyy");
				}
				
				Calendar cal= Calendar.getInstance();
				cal.set(Calendar.DAY_OF_MONTH, day);
				cal.set(Calendar.MONTH, month-1);
				cal.set(Calendar.YEAR, year);
				cal.set(Calendar.YEAR, keyYear);
				cal.set(Calendar.MONTH, keyMonth);
				cal.set(Calendar.DAY_OF_MONTH,keyDay);
				return dateFormat.format(cal.getTime());
			}
		} catch (Exception e) {
			System.out.println("Error Occured while calculating the date");
			e.printStackTrace();
		}
		return bodDate;
	}

}
