package birt;

import java.io.File;

import org.eclipse.birt.core.exception.BirtException;

public interface ReportEngine {
	
	void start() throws ReportEngineException;
	void stop();
	/**
	 * 
	 * @param designFilePath
	 * @param object  this object is given to report file by name 'object'
	 * @param returnFileName
	 * @return 
	 * @throws Exception 
	 */
	File generatePdf(String designFilePath,Object object,String returnFileName) throws ReportEngineException;
}
