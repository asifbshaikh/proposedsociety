package controllers;

import java.io.File;
import java.util.Date;

import mail.HtmlMail;
import mail.Mailer;
import models.PaymentReceipt;
import models.Query;
import models.User;
import models.dao.DaoProvider;

import org.codehaus.jackson.JsonNode;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Http.Response;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import security.auth.VisitorAuthenticator;
import utils.Mail;
import utils.SmsGateway;
import utils.SmsGatewayException;
import utils.SmsTemplate;
import birt.ReportEngine;

import com.avaje.ebean.Ebean;
import com.google.inject.Inject;
import common.Roles;

@Authenticated(value = VisitorAuthenticator.class)
public class ApplicantController extends AuthenticatedUserController {
	private static final Logger LOG = LoggerFactory.getLogger(ApplicantController.class);
	private final ReportEngine reportEngine;
	private final Mailer mailer;
	private final SmsGateway smsGateway;
	@Inject
    public ApplicantController(DaoProvider provider, SmsGateway smsGateway, Mailer mailer,ReportEngine reportEngine) {
        super(provider.userDao());
        this.reportEngine = reportEngine;
        this.mailer = mailer;
        this.smsGateway = smsGateway;
    }

    public Result dashboard() {
        User user = currentUser();
        if (user != null && user.roles != null && user.roles.contains(Roles.MANAGER)) {
            return ok(views.html.manager.managementDashboard.render());
        }

        return ok(views.html.applicant.applicantDashboard.render(user));
    }
    
    public Result changePassword(String accessRole) {
        User user = currentUser();
        return ok(views.html.changePassword.render(user,null,null,accessRole));
    }
   
    public Result changePwd() {
        DynamicForm changePwd = Form.form().bindFromRequest();
        User user = currentUser();
        String oldPassword = changePwd.get("oldPassword");
        String newPassword = changePwd.get("newPassword");
        String confirmPassword = changePwd.get("confirmPassword");
        if (user.password != null && oldPassword !="" && confirmPassword != "" && newPassword != "") {
        	if (BCrypt.checkpw(oldPassword, user.password)) {
                if(oldPassword.equals(newPassword)){
                    return ok(views.html.changePassword.render(user,"New Password Should Not Be Same as Old Password","error","Admin"));
                }else if(newPassword.equals(confirmPassword)){
         			   user.password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
         			   userDao.save(user);  
         			   return ok(views.html.changePassword.render(user,"Password Changed Successfully","success","Admin"));
                		
         		 }else{
                 	   return ok(views.html.changePassword.render(user,"You Must Enter The Same Password Twice To Confirm It.","error","Admin"));
         		 }
         	 }else{
              	 return ok(views.html.changePassword.render(user,"Invalid Old Password","error","Admin"));
             }
          }else{
        	  	return ok(views.html.changePassword.render(user,"To Change Password, Enter Your Password ","error","Admin"));
          }

    }
    
    public Result changeApplicantPassword(String accessRole) {
        User user = currentUser();
        return ok(views.html.changeApplicantPassword.render(user,null,null,accessRole));
    }
   
    public Result changeApplicantPasswordSubmit() {
        DynamicForm changePwd = Form.form().bindFromRequest();
        User user = currentUser();
        String oldPassword = changePwd.get("oldPassword");
        String newPassword = changePwd.get("newPassword");
        String confirmPassword = changePwd.get("confirmPassword");
        if (user.applicantPassword == null || user.applicantPassword.isEmpty()) {
               return badRequest();
        }
        
        if (!BCrypt.checkpw(oldPassword, user.applicantPassword)) {
        	return ok(views.html.changePassword.render(user,"To Change Password, Enter Your correct oldPassword ","error","Admin"));
        }
        
        if(oldPassword.equals(newPassword)){
            return ok(views.html.changeApplicantPassword.render(user,"New Password Should Not Be Same as Old Password","error","Admin"));
        }
        
        if(!newPassword.equals(confirmPassword)){
        	return ok(views.html.changeApplicantPassword.render(user,"Password & Confirm Password Should be Same","error","Admin"));   
 		}
        
        user.applicantPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
		userDao.save(user);  
		return ok(views.html.changeApplicantPassword.render(user,"Password Changed Successfully","success","Admin"));
    }
    
    public Result referFriend() {
    	User user = currentUser();
    	JsonNode json = request().body().asJson();
    	if(json.isNull() || json.size() == 0){
    		return badRequest();
    	}
    	
		for(int i=0; i<json.size(); i++){
			String email = json.findValuesAsText("Emaill Id").get(i);
			String mobile = json.findValuesAsText("Mobile No.").get(i);
			String name = json.findValuesAsText("Name").get(i);
			
			if(email != null && !email.isEmpty()){
				HtmlMail mail = new HtmlMail();
				mail.setTo(email);
				mail.setFrom(Mail.PS_FROM_ADDR);
				mail.setSubject("Welcome to Proposed Society");
    			mail.setHtmlBody(views.html.mails.referFriendEmail.render(name, user.name).toString());
    			mailer.sentHtml(mail);
			}
			
			if(mobile != null && !mobile.isEmpty()){
    	       try {
    				//smsGateway.sendTextSms(mobile, views.html.sms.referFriendSms.render(name, user.name).toString());
    				smsGateway.send(mobile, SmsTemplate.REFER_A_FRIEND, name, user.name);
    			} catch (SmsGatewayException e) {
    				LOG.warn("Failed to send SMS.", e);
    			}
			}
		}
        return ok();
    }
    
    public Result sendQuery() {
    	User user = currentUser();
    	JsonNode json = request().body().asJson();
    	
    	if( json.isNull() ){
    		return badRequest();
    	}
    	
		Query query = new Query();
    	query.user = user;
		query.queryText = json.findValue("queryText").asText();
		query.timeStamp = new Date();
		Ebean.save(query);
        return ok();
    }

	public Result downloadPaymentReceipt(){
    	User user = currentUser();
    	
    	if(user == null || user.form == null || user.form.invoice == null) {
    		LOG.info("Processing generate payment receipt but either user or user.form or user.form.invoice is null");
    		return badRequest();
    	}
    	
    	
    	PaymentReceipt paymentReceipt = new PaymentReceipt();
    	paymentReceipt.setAmount(user.form.invoice.amount.toPlainString());
    	paymentReceipt.setAmountInWord("Five Hundred only");
    	paymentReceipt.setApplicantName(user.form.applicant.title+". "+ user.form.applicant.fname + " " + user.form.applicant.lname);
    	paymentReceipt.setApplicationNumber(user.form.formNumber);
    	paymentReceipt.setAuthorizationNumber(user.form.invoice.payments.get(user.form.invoice.payments.size()-1).transactionId);
    	paymentReceipt.setDescription(user.form.invoice.description);
    	paymentReceipt.setModeOfPayment(user.form.invoice.payments.get(user.form.invoice.payments.size()-1).paymentMode);
    	if(user.form.invoice.receiptId != null){
    		paymentReceipt.setReceiptNumber(user.form.invoice.receiptId.toString());
    	}else{
    		paymentReceipt.setReceiptNumber("NA");
    	}
    	paymentReceipt.setPaymentDate(user.form.filled_date);
    	File file = null;
    	try {
			file = reportEngine.generatePdf("report/applicant/paymentReceipt.rptdesign", paymentReceipt,"payment_receipt");
			response().setContentType("application/x-download");
			response().setHeader("Content-disposition","attachment; filename=" + "payment_receipt.pdf");
			return ok(file);
			
		} catch (Exception e) {
			LOG.error("Got exception while creating report", e);
			return badRequest();
		}finally{
			if(file != null) file.delete();
		}
    }
	
    
}
