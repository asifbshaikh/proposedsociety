package utils;

import java.util.Date;

import java.text.SimpleDateFormat;

public final class DateFormatHelper {
	private DateFormatHelper(){}
	public static String format(Date date, String formate){
		if(date != null){
			return new SimpleDateFormat(formate).format(date).toString();
		}else{
			return "Not availabe";
		}
	}
}
