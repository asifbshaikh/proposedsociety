package birt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import models.ReportCsv;

public class PsCsvGenerator implements CsvGenerator {

	@Override
	public File genrate(List<ReportCsv> list) throws IOException {
		File file = File.createTempFile("report", ".csv");
		FileWriter fw = new FileWriter(file);
		
		String titles = 	"First Name" + ","+
				"Middle Name"+ ","+
				"Last Name" + ","+
				"DOB" + ","+
				"Mobile" + ","+
				"Email" + ","+
				"Sex" + ","+
				"Budget" + ","+
				"Property Type" + ","+
				"Sub Type" + ","+
				"Build Up Area" + ","+
				"Carpet Area" ;
		
		fw.write(titles);
		fw.write(System.getProperty( "line.separator" ));
		
		for(ReportCsv r: list){
			fw.write(constructLine(r));
			fw.write(System.getProperty( "line.separator" ));
		}
		file.deleteOnExit();
		fw.flush();
		fw.close();
		return file;
	}

	private String constructLine(ReportCsv r) {
		return 
				r.fName + ","+
				r.mName + ","+
				r.lName + ","+
				r.dob + ","+
				r.mobile + ","+
				r.email + ","+
				r.sex + ","+
				r.budget + ","+
				"FlatOrTerraceFlat" + ","+
				r.subType + ","+
				r.builtUpArea + ","+
				r.carpetArea 
				;
	
		
	}

	/*private void writeToCsv(List<ReportCsv> list, File file) {
		try {
			FileWriter fw = new FileWriter(file);
			for (ReportCsv r : list) {
				StringBuffer oneLine = new StringBuffer();
				oneLine.append(r.fName);oneLine.append(",");
				oneLine.append(r.mName);oneLine.append(",");
				oneLine.append(r.lName);oneLine.append(",");
				oneLine.append(r.dob);oneLine.append(",");
				oneLine.append(r.mobile);oneLine.append(",");
				oneLine.append(r.email);oneLine.append(",");
				oneLine.append(r.sex);oneLine.append(",");
				oneLine.append(r.budget);oneLine.append(",");
				oneLine.append(r.propertyType);oneLine.append(",");
				oneLine.append(r.subType);oneLine.append(",");
				oneLine.append(r.budget);oneLine.append(",");
				oneLine.append(r.builtUpArea);oneLine.append(",");
				oneLine.append(r.carpetArea);
				fw.flush();
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/

}
