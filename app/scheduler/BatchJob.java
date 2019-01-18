package scheduler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import models.ApplicationForm;
import models.ApplicationForm.PostPayment;
import models.User;
import models.dao.ApplicationFormDao;

import org.hibernate.validator.internal.engine.resolver.DefaultTraversableResolver;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import utils.Mail;
import utils.Notifier;
import validation.ProposedSocietyTraversableResolver;
import controllers.FormStatus;

public class BatchJob implements Job {
	private static final Logger LOG = LoggerFactory.getLogger(BatchJob.class);
	private  static final String PS_FROM_ADDR = "Proposed Society <noreply@proposedsociety.com>";
	public BatchJob(){
		
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Calendar calender = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		LOG.info("Batch Job started at time : " + dateFormat.format(calender.getTime()));	
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		ApplicationFormDao applicationFormDao =(ApplicationFormDao) jobDataMap.get(ApplicationFormDao.class.getName());
		batchTask(applicationFormDao);
	}
	
	private void batchTask(ApplicationFormDao applicationFormDao) {
		Set<ConstraintViolation<ApplicationForm>> validationErrors;
		List<ApplicationForm> list = applicationFormDao.findAllPaidForm();
		
		//Because of lazy loading Ebean
		for(ApplicationForm form : list){
			
		}
        SpringValidatorAdapter validator = new SpringValidatorAdapter(
                Validation.byDefaultProvider()
                .configure()
                .traversableResolver(new ProposedSocietyTraversableResolver(new DefaultTraversableResolver()))
                .buildValidatorFactory().getValidator());
        
        for(ApplicationForm appForm : list){
        	validationErrors = validator.validate(appForm, PostPayment.class);
        	if(!validationErrors.isEmpty()){
        		processForm(appForm, applicationFormDao);
        		
        	}else{
        		appForm.status = FormStatus.COMPLETED;
        		appForm.completed_date = new Date();
        		applicationFormDao.save(appForm);
        	}
        }
        
       
	}

	private  void processForm(ApplicationForm appForm,ApplicationFormDao applicationFormDao) {
		User user = applicationFormDao.findUserByFormId(appForm.id);
		int dayCount  = getDayCount(new Date(),appForm.filled_date);
		if(dayCount > 10){
			appForm.status = FormStatus.REJECTED;
			applicationFormDao.save(appForm);
		
  			Mail mail = new Mail();
			mail.setSender(Mail.PS_FROM_ADDR);
			mail.setRecipient(user.email);
			mail.setSubject("Form status from Proposed Society");
			mail.setBody("Your form has been rejected.");
  			Notifier.sendHtmlEmail(mail);
		}else{
			
  			Mail mail = new Mail();
			mail.setSender(Mail.PS_FROM_ADDR);
			mail.setRecipient(user.email);
			mail.setSubject("Form status from Proposed Society");
			mail.setBody((10 - dayCount) +" days remaining to complete your appliaction");
  			Notifier.sendHtmlEmail(mail);
  			
			appForm.status = FormStatus.INCOMPLETE;
			applicationFormDao.save(appForm);
		}
	}

	private int getDayCount(Date currentDate, Date formFilledDate) {
		long msDiff = currentDate.getTime()- formFilledDate.getTime() ;
		return Math.round(msDiff /(24*60*60*1000));
	}


}
