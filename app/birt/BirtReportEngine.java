package birt;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.core.internal.registry.RegistryProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controllers.ApplicantController;

public class BirtReportEngine implements ReportEngine {
	private static final Logger LOG = LoggerFactory.getLogger(BirtReportEngine.class);
	private IReportEngine engine;
    private EngineConfig config ;
	
    @Override
	public void start() throws ReportEngineException {
		config = new EngineConfig();  
        RegistryProviderFactory.releaseDefault();
		try {
			Platform.startup(config);
		} catch (BirtException e) {
			throw new ReportEngineException("Not able to start birtEngile");
		}
		
		IReportEngineFactory reportEngineFactory = (IReportEngineFactory) Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
		engine = reportEngineFactory.createReportEngine(config);       
		LOG.info("Created birt report engine");
	}
	
    @Override
	public void stop(){
		 engine.destroy();
		 Platform.shutdown();
		 LOG.info("Destroying birt reporting engine");
	}
	

	@Override
	public File generatePdf(String designFilePath,Object object, String returnFileName) throws ReportEngineException{
		
		URL url = ApplicantController.class.getClassLoader().getResource(designFilePath);
    	if(url == null){
    		LOG.info("not found report design file");
    		throw new ReportEngineException("Not found design file ");
    	}
    	
		// Open the report design
        IReportRunnable design;
        IRunAndRenderTask task;
        File outPutFile;
		try {
			design = engine.openReportDesign(url.openStream());
			task = engine.createRunAndRenderTask(design);
	        task.getAppContext().put("object", object);
	        
	        //Set pDF file options
	        PDFRenderOption pdfDesign = new PDFRenderOption();
	        
	        outPutFile = File.createTempFile(returnFileName, ".pdf");
	        outPutFile.deleteOnExit();
	        
	        pdfDesign.setOutputFileName(outPutFile.getAbsolutePath());
	        pdfDesign.setOutputFormat("pdf");
	        
	        task.setRenderOption(pdfDesign);
	        task.run();
	        task.close();
		} catch (EngineException e) {
			throw new ReportEngineException("Not able to open design file",e);
		} catch (IOException e) {
			throw new ReportEngineException("Not able to create output file",e);
		}
        
        
		return outPutFile;
	}


}
