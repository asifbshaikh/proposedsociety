package scheduler;

import static org.quartz.DateBuilder.dateOf;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.HashMap;
import java.util.Map;

import models.dao.ApplicationFormDao;
import models.dao.DaoProvider;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.google.inject.Inject;

public class BatchProcessor {
	
	private final ApplicationFormDao applicationFormDao ;
	@Inject
	public BatchProcessor(DaoProvider provider){
		this.applicationFormDao = provider.applicationFormDao();
	}
	public  void start(){
		try {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put(ApplicationFormDao.class.getName(), applicationFormDao);
				JobDataMap jobDataMap = new JobDataMap(map);
				StdSchedulerFactory sf = new StdSchedulerFactory();
				Scheduler scheduler = sf.getScheduler();
							    
				JobDetail job = newJob(BatchJob.class)
								.usingJobData(jobDataMap)
				        		.withIdentity("BatchJob", "g1")
				        		.build();
				
				/**
				 * configured trigger (By using simpleTrigger)
				 * Trigger start  every day at 23:59:54 and its BatchJob
				 */
				
				Trigger trigger = newTrigger().withIdentity("trigger1", "g1")
												.startAt(dateOf(23, 59, 54)) 	
												.withSchedule(simpleSchedule()
												.withIntervalInHours(24)		
												.repeatForever())
												.build();
			/**
			 * scheduler.deleteJob(job.getKey());
			 * 
			 * Deleting previous jobs that assigned to scheduler with same property as above 
			 * NEED : Because in development ,every time page is reloaded ,job try start again even previous job is there
			 * Note : No need in production
			 */
				scheduler.deleteJob(job.getKey()); 
				
				scheduler.scheduleJob(job, trigger);
				scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
}
