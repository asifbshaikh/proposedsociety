package controllers;

import java.util.List;
import java.util.concurrent.TimeUnit;

import models.ApplicationForm;
import models.dao.ApplicationFormDao;
import models.dao.DaoProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.mvc.Controller;

import com.google.inject.Inject;

public class Scheduler extends Controller implements Runnable  {
	private static final Logger LOG = LoggerFactory.getLogger(Scheduler.class);
	private   ApplicationFormDao applicationFormDao ;
	Thread scheduler ;
	/*public Scheduler(){
		scheduler = new Thread(this);
		scheduler.start();
	}*/
	
	@Inject
	public Scheduler(DaoProvider provider){
		this.applicationFormDao = provider.applicationFormDao();
	}
	
	@Override
	public void run() {
		List <ApplicationForm> list;
		try {
			
				while(true){
					LOG.info("Form scheduler started at time : " + System.currentTimeMillis());
					list = applicationFormDao.findAllFormByStatus(FormStatus.PENDING);
					TimeUnit.HOURS.sleep(24);
				}
				
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
}
