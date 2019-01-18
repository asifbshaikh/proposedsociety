package models;

import java.util.Date;

public class ReportCsv {
	public String fName;
	public String mName;
	public String lName;
	public String mobile;
	public String email;
	public String sex;
	public Date dob;
	public Long income;
	public Integer budget;
	public String propertyType;
	public Integer carpetArea;
	public Integer  builtUpArea;
	public String subType;
	
	@Override
	public String toString(){
		return "fName" + fName;
	}
}
