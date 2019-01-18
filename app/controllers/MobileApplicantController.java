package controllers;

import java.util.Date;

import mail.HtmlMail;
import mail.Mailer;
import models.Query;
import models.User;
import models.dao.DaoProvider;

import org.codehaus.jackson.JsonNode;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.mvc.Result;
import play.mvc.Security.Authenticated;
import utils.Mail;
import utils.SmsGateway;
import utils.SmsGatewayException;
import utils.SmsTemplate;

import com.avaje.ebean.Ebean;
import com.google.inject.Inject;
@Authenticated(value = MobileAuthenticator.class)
public class MobileApplicantController extends AuthenticatedUserController{
	private static final Logger LOG = LoggerFactory.getLogger(ApplicantController.class);
	private final Mailer mailer;
	private final SmsGateway smsGateway;
	@Inject
	public MobileApplicantController(DaoProvider provider, Mailer mailer, SmsGateway smsGateway) {
		super(provider.userDao());
		this.mailer = mailer;
		this.smsGateway = smsGateway;
	}

	public Result sendMobileQuery() {
		User user = MobilecurrentUser();
		JsonNode json = request().body().asJson();

		if (json.isNull()) {
			return badRequest();
		}

		Query query = new Query();
		query.user = user;
		query.queryText = json.findValue("queryText").asText();
		query.timeStamp = new Date();
		Ebean.save(query);
		return ok("Query Successfully Submitted!");
	}
	
    public Result mobileReferFriend() {
    	User user = MobilecurrentUser();
    	JsonNode json = request().body().asJson();
    	if(json.isNull() || json.size() == 0){
    		return badRequest();
    	}
    	
 
			JsonNode emailNode = json.get("EmaillId");
			JsonNode mobileNode = json.get("MobileNo");
			JsonNode nameNode = json.get("Name");
			String email = emailNode.getTextValue();
			String mobile = mobileNode.getTextValue();
			String name = nameNode.getTextValue(); 
			
			if(email != null && !email.isEmpty()){
				HtmlMail mail = new HtmlMail();
				mail.setTo(email);
				mail.setFrom(Mail.PS_FROM_ADDR);
				mail.setSubject("Welcome to Proposed Society");
    			mail.setHtmlBody(views.html.mails.referFriendEmail.render(name, user.name).toString()); 
    			mailer.sentHtml(mail);
    			return ok("Refer friend Successfully");
			}
			
			if(mobile != null && !mobile.isEmpty()){
    	       try {
    				smsGateway.send(mobile, SmsTemplate.REFER_A_FRIEND, name, user.name);
    				return ok("Refer friend Successfully");
    			} catch (SmsGatewayException e) {
    				LOG.warn("Failed to send SMS.", e);
    			}
			}
 
        return ok();
    }
    
	public Result changeApplicantPasswordForMobile(){
		LOG.debug("Changing password");
		JsonNode json = request().body().asJson();
		JsonNode newPassWordJson = json.get("newPassword");
		JsonNode confirmPassWordJson = json.get("confirmPassword");
		String newPassWord = newPassWordJson.getTextValue();
		String confirmPassWord = confirmPassWordJson.getTextValue();
		confirmPassWord = confirmPassWord.trim();
		if(!newPassWord.equals(confirmPassWord)){
			return badRequest("New Password & Verified Password doesn't match");
		}
		JsonNode oldPasswordJson = json.get("oldPassword");
		String oldPassword = oldPasswordJson.getTextValue();
		if(isValidMobilePassword(oldPassword)){
			User user = MobilecurrentUser();
			user.applicantPassword = BCrypt.hashpw(newPassWord, BCrypt.gensalt());
			userDao.save(user);
			return ok("Password has Been Changed Successfully");
		}
		else{
			return badRequest("Invalid Old Password");
		}
			
	}
	
	
	private boolean isValidMobilePassword(String oldPassword){
		User user = MobilecurrentUser();
		return  BCrypt.checkpw(oldPassword,user.applicantPassword);
	}



}