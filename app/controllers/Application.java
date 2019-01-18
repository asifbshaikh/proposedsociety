package controllers;

import static common.ApplicationConstants.SESSION_KEY_LAST_ACCESS_TIME;
import static common.ApplicationConstants.SESSION_KEY_ROLE;
import static common.ApplicationConstants.SESSION_KEY_USER;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import mail.HtmlMail;
import mail.Mailer;
import models.AgentAddress;
import models.AgentForm;
import models.ApplicationForm;
import models.Role;
import models.User;
import models.dao.AgentDao;
import models.dao.DaoProvider;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.data.DynamicForm;
import play.data.Form;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Pattern;
import play.data.validation.Constraints.Required;
import play.libs.F;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http.Response;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import security.auth.BaseProposedSocietyAuthenticator;
import security.auth.VisitorAuthenticator;
import utils.Mail;
import utils.SmsGateway;
import utils.SmsGatewayException;
import utils.SmsTemplate;
import validation.PSForm;
import views.html.applicantsTerms;
import views.html.apply;
import views.html.awards;
import views.html.becomeAnAgent;
import views.html.benefits;
import views.html.concept;
import views.html.contact;
import views.html.draw;
import views.html.faq;
import views.html.history;
import views.html.home;
import views.html.inauguration;
import views.html.newsAndPress;
import views.html.pageNotFound404;
import views.html.rules;
import views.html.success;
import views.html.videos;
import views.html.wholePersonalInfo;

import com.google.inject.Inject;
import common.ApplicationConstants;
import common.RestUtils;
import common.Roles;

import controllers.ApplicationFormController.DependentAndLoanForm;
import controllers.ApplicationFormController.DependentDetails;
import controllers.ApplicationFormController.LoanFormDetails;

public class Application extends AuthenticatedUserController {
	private static final Logger LOG = LoggerFactory.getLogger(Application.class);
	private final SmsGateway smsGateway;
	private final AgentDao agentDao;
	private final Mailer mailer;
	public static enum GenerateAuthCodeErrors {
	    VALIDATION_ERRORS, EMAIL_OR_MOBILE_EXISTS;
	}

	@Inject
	public Application(DaoProvider provider, SmsGateway smsGateway, Mailer mailer) {
		super(provider.userDao());
		this.agentDao = provider.agentDao();
		this.smsGateway = smsGateway;
		this.mailer = mailer;
	}
	
    public static class VisitorLogin {
        @Required(message = "Name is required") public String name;
        @Required(message = "Mobile is required") @Pattern(value = "(\\+)?[0-9\\-]+", message = "Invalid Mobile Number") public String mobile;
        @Required(message = "Email is required") @Email(message = "Invalid Email") public String email;
    }

    public static class UserLogin {
        public String mobile;
        public String email;
    }

    public Result index() {
        String lastLogin = session(ApplicationConstants.SESSION_KEY_LAST_ACCESS_TIME);
        User user = currentUser();

        if (user != null && !BaseProposedSocietyAuthenticator.isSessionValid(user.email, lastLogin, user.roles, Roles.VISITOR)) {
            session().clear();
        }

        LOG.warn("Found user: " + user);
        return ok(home.render(user));
    }
    
    public Result logout() {
        session().clear();
        return redirect(routes.Application.index());
    }

    @Authenticated(value = VisitorAuthenticator.class)
    public Result apply() {
        return ok(apply.render(currentUser()));
    }
 
    @Authenticated(value = VisitorAuthenticator.class)
    public Result membershipForm() {
    	Form<ApplicationForm> form = PSForm.form(ApplicationForm.class);
    	Form<DependentAndLoanForm> data = Form.form(DependentAndLoanForm.class);
    	Form<DependentDetails> dd = Form.form(DependentDetails.class);
    	Form<LoanFormDetails> lfd = Form.form(LoanFormDetails.class);
    	
    	User user = currentUser();
    	boolean paymentStatus = false ;
    	if(user != null){
            if(user.form == null) {
                user.form = new ApplicationForm();
                user.form.status = FormStatus.PENDING;
                userDao.save(user);
            }
    	}
    	
    	if(user.form.invoice != null && user.form.invoice.paid){
    		paymentStatus = true;
    	}
    	
        ApplicationForm applForm = user.form;
    	if (applForm != null) {
    		form = form.fill(applForm);
    	}
        
    	return ok(wholePersonalInfo.render(form,data,dd,lfd,user.form.getApplicantDependents(),user.form.getCoApplicantDependents(),user.form.getApplicantLoanDetails(),user.form.getCoApplicantLoanDetails(),paymentStatus));
    }
    
    
    @Authenticated(value = VisitorAuthenticator.class)
    public Result becomeAnAgent() {
    	User user = currentUser();
    	user.interestedInAgent = true;
    	userDao.save(user);
    	LOG.info(user.interestedInAgent.toString() );
        return ok(becomeAnAgent.render(currentUser()));
    }
    
    public Result notFound404(String page) {
        return notFound(pageNotFound404.render(currentUser()));
    }
    
    public Result internalError505(){
    	return badRequest(views.html.error.internalServerError505.render());
    	//views.html.error.internalServerError505.render()
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result generateAuthCode() {
        JsonNode json = request().body().asJson();
        VisitorLogin login = Json.fromJson(json, VisitorLogin.class);
    
        javax.validation.Validator validator = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<VisitorLogin>> violations = validator.validate(login);

        if (!violations.isEmpty()) {
            return badRequest(RestUtils.createFailureResponse(violations, "errorcode", GenerateAuthCodeErrors.VALIDATION_ERRORS.name()));
        }

        User userByEmail = userDao.findUniqueByEmail(login.email);

        User userByMobile = userDao.findUniqueByMobile(login.mobile);

        if ((userByEmail != null ^ userByMobile != null)) {
            return badRequest(RestUtils.createFailureResponse("errorcode", GenerateAuthCodeErrors.EMAIL_OR_MOBILE_EXISTS.name()));
        }
        
        User user = userByEmail;
        
        if (user == null) {
            user = User.create(login.name, login.email, login.mobile, Roles.VISITOR);
            user.joiningDate = new Date();
        }

                
        String authcode = user.authcode;
        if (authcode == null) {
            authcode = User.generateAuthCode();
            user.authcode = authcode;
            user.authcode_hash = BCrypt.hashpw(authcode, BCrypt.gensalt());
        }

        userDao.save(user);
        
		HtmlMail mail = new HtmlMail();
		mail.setTo(user.email);
		mail.setFrom(Mail.PS_FROM_ADDR);
		mail.setSubject("Welcome to Proposed Society");
		mail.setHtmlBody(views.html.mails.visitorWelcome.render(user, authcode).toString());
		mailer.sentHtml(mail);
		
        try {
			smsGateway.send(user.mobile, SmsTemplate.SEND_AUTHCODE, user.name, authcode);
		} catch (SmsGatewayException e) {
			LOG.warn("Failed to send SMS.", e);
		}
        
        return ok(RestUtils.createSuccessResponse());
    }
    
    public Result sendAuthcodeToVisitor() {
        JsonNode json = request().body().asJson();
        JsonNode emailNode = json.get("email");
        String email = emailNode.getTextValue();
        User userByEmail = userDao.findUniqueByEmail(email);
        if ((userByEmail == null)) {
        	return badRequest(Json.stringify(RestUtils.createFailureResponse()));
        }else{
	        User user = userByEmail;
			HtmlMail mail = new HtmlMail();
			mail.setTo(user.email);
			mail.setFrom(Mail.PS_FROM_ADDR);
			mail.setSubject("Proposed Society- Auth Code");
			mail.setHtmlBody(views.html.mails.authCodeMail.render(user, user.authcode).toString());
			mailer.sentHtml(mail);
			
	        try {
				smsGateway.send(user.mobile, SmsTemplate.SEND_AUTHCODE, user.name, user.authcode);
			} catch (SmsGatewayException e) {
				LOG.warn("Failed to send SMS.", e);
			}
	        
	        return ok(Json.stringify(RestUtils.createSuccessResponse()));
        }
    }
    
    public Result sendPasswordToApplicant() {
        JsonNode json = request().body().asJson();
        JsonNode emailNode = json.get("email");
        String email = emailNode.getTextValue();
        User userByEmail = userDao.findUniqueByEmail(email);
        if ((userByEmail == null || userByEmail.applicantPassword == null || !userByEmail.hasRole(Roles.APPLICANT))) {
        	return badRequest(Json.stringify(RestUtils.createFailureResponse()));
        }else{
        	String applicantPassword = User.generatePassword();
	        User user = userByEmail;
	        user.applicantPassword = BCrypt.hashpw(applicantPassword, BCrypt.gensalt());
	        userDao.save(user);
	        LOG.info("Sending password to user.");
        	HtmlMail mail = new HtmlMail();
			mail.setTo(user.email);
			mail.setFrom(Mail.PS_FROM_ADDR);
			mail.setSubject("Proposed Society- Applicant Password");
			mail.setHtmlBody(views.html.mails.passwordMail.render(user,applicantPassword).toString());
			mailer.sentHtml(mail);
			
	        try {
	        	smsGateway.send(user.mobile, SmsTemplate.APPLICANT_PASSWORD, user.email, applicantPassword);
			} catch (SmsGatewayException e) {
				LOG.warn("Failed to send SMS.", e);
			}
	        
	        return ok(Json.stringify(RestUtils.createSuccessResponse()));
        }
    }
    
    public Result sendPasswordToAgent() {
        JsonNode json = request().body().asJson();
        JsonNode emailNode = json.get("email");
        String email = emailNode.getTextValue();
        User userByEmail = userDao.findUniqueByEmail(email);
        if ((userByEmail == null || userByEmail.password == null) || !userByEmail.hasRole(Roles.AGENT)) {
        	return badRequest(Json.stringify(RestUtils.createFailureResponse()));
        }else{
        	String password = User.generatePassword();
	        User user = userByEmail;
	        user.password = BCrypt.hashpw(password, BCrypt.gensalt());
	        userDao.save(user);
	        LOG.info("Sending password to user.");
        	HtmlMail mail = new HtmlMail();
			mail.setTo(user.email);
			mail.setFrom(Mail.PS_FROM_ADDR);
			mail.setSubject("Proposed Society- Agent Password");
			mail.setHtmlBody(views.html.mails.passwordMail.render(user,password).toString());
			mailer.sentHtml(mail);
	        return ok(Json.stringify(RestUtils.createSuccessResponse()));
        }
    }
    
    public Result authenticate(F.Function<User, String> f) {
        JsonNode json = request().body().asJson();
        JsonNode emailNode = json.get("email");
        JsonNode passwdNode = json.get("password");
        if (emailNode != null && passwdNode != null) {
            String email = emailNode.getTextValue();
            String authcode_hash  = passwdNode.getTextValue();
            if (email != null && authcode_hash  != null) {
                User user = userDao.findUniqueByEmail(email.trim());
                
                /// NOTE: Not trimming the authcode_hash , space maybe a valid leading / trailing character.
                try {
                    if (user != null && BCrypt.checkpw(authcode_hash, f.apply(user))) {
                        // Ensure to set the cookie to ensure that subsequent requests find this user logged-in.
                        session(SESSION_KEY_USER, user.email);
                        session(SESSION_KEY_LAST_ACCESS_TIME, String.valueOf(System.currentTimeMillis()));
                        session(SESSION_KEY_ROLE, createRoleString(user.roles));
    
                        // Update the lastLogin time & record IP address.
                        user.lastLogin = new Date();
                        user.lastLoginIp = request().remoteAddress();
                        
                        LOG.info("Successful login from user: " + user.email + " from IP: " + request().remoteAddress());
    
                        userDao.save(user);
                        
                        return ok(Json.stringify(RestUtils.createSuccessResponse()));
                    } else {
                        LOG.info("Login failed for user: " + email + " from IP: " + request().remoteAddress()
                                + " due to: " + (user == null? "No-existent user." : "Invalid password."));
                    }
                } catch (Throwable t) {
                    // Ignore
                    LOG.warn("Unexpected error", t);
                }
            }
        }
    
        return badRequest(Json.stringify(RestUtils.createFailureResponse()));
    }
    
    public Result mobileAuthenticate(F.Function<User, String> f) {
        JsonNode json = request().body().asJson();
        JsonNode emailNode = json.get("email");
        JsonNode passwdNode = json.get("password");
        ObjectNode response = Json.newObject();
        if (emailNode != null && passwdNode != null) {
            String email = emailNode.getTextValue();
            String authcode_hash  = passwdNode.getTextValue();
            if (email != null && authcode_hash  != null) {
                User user = userDao.findUniqueByEmail(email.trim());
                
                /// NOTE: Not trimming the authcode_hash , space maybe a valid leading / trailing character.
                try {
                    if (user != null && BCrypt.checkpw(authcode_hash, f.apply(user))) {
    
                        // Update the lastLogin time & record IP address.
                        user.lastLogin = new Date();
                        user.lastLoginIp = request().remoteAddress();
                        user.api_key = User.generateRandomApiKey();
                        LOG.info("Successful login from user: " + user.email + " from IP: " + request().remoteAddress());
                        userDao.save(user);
                        response.put("authcode", user.api_key.toString());	
                        return ok(response);
                    } else {
                        LOG.info("Login failed for user: " + email + " from IP: " + request().remoteAddress()
                                + " due to: " + (user == null? "No-existent user." : "Invalid password."));
                    }
                } catch (Throwable t) {
                    // Ignore
                    LOG.warn("Unexpected error", t);
                }
            }
        }
    
        return badRequest(Json.stringify(RestUtils.createFailureResponse()));
    }

    private static String createRoleString(List<Role> roles) {
        String rolesString = "";
        int size = roles.size();
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                rolesString += ",";
            }
            
            rolesString += roles.get(i).role;
        }
        
        return rolesString;
    }
    
    @BodyParser.Of(BodyParser.Json.class)
    public Result authenticateMember() {
        return authenticate(new F.Function<User, String>() {
            @Override
            public String apply(User user) throws Throwable {
                return user.applicantPassword;
            }
        });
    }
    
    @BodyParser.Of(BodyParser.Json.class)
    public Result authenticateVisitor() {
        return authenticate(new F.Function<User, String>() {
            @Override
            public String apply(User user) throws Throwable {
                return user.authcode_hash;
            }
        });
    }
    
    @BodyParser.Of(BodyParser.Json.class)
    public Result mobileAuthenticateVisitor() {
        return mobileAuthenticate(new F.Function<User, String>() {
            @Override
            public String apply(User user) throws Throwable {
                return user.authcode_hash;
            }
        });
    }
    
    @BodyParser.Of(BodyParser.Json.class)
    public Result mobileAuthenticateApplicant() {
        return mobileAuthenticate(new F.Function<User, String>() {
            @Override
            public String apply(User user) throws Throwable {
                return user.applicantPassword;
            }
        });
    }

    

    public boolean isLoggedIn() {
        return session(SESSION_KEY_USER) != null;
    }

    @Authenticated(value = VisitorAuthenticator.class)
    public Result benefits() {
        return ok(benefits.render(currentUser()));
    }

    @Authenticated(value = VisitorAuthenticator.class)
    public Result awards() {
		return ok(awards.render(currentUser()));
    }
    
    @Authenticated(value = VisitorAuthenticator.class)
    public Result concept() {
        return ok(concept.render(currentUser()));
    }

    @Authenticated(value = VisitorAuthenticator.class)
    public Result rules() {
        return ok(rules.render(currentUser()));
    }
    
    @Authenticated(value = VisitorAuthenticator.class)
    public Result locateAgent() {
        return ok(views.html.agentLocator.render(currentUser()));
    }
    
    @Authenticated(value = VisitorAuthenticator.class)
    @BodyParser.Of(BodyParser.Json.class)
    public Result locateAgentSubmit() {	
    	JsonNode json = request().body().asJson();
    	AgentAddress address = new AgentAddress();
    	
    	address.taluka = json.get("taluka").getTextValue();
    	address.state = json.get("state").getTextValue();
    	address.district = json.get("district").getTextValue();
    	address.city = json.get("city").getTextValue();
    	
    	List<AgentForm> agentList = new ArrayList<AgentForm>();
    	if (address.city.equals("")) {
    		agentList = agentDao.findAgentByOfficeAddress(address.state, address.district, address.taluka);
    	}
    	else {
    		agentList = agentDao.findAgentByCity(address.city);
    	}
    	return ok(views.html.agentLocatorResponse.render(currentUser(), agentList));	
    }	

    @Authenticated(value = VisitorAuthenticator.class)
    public Result applicantsTerms() {
		return ok(applicantsTerms.render(currentUser()));
    }
    
    @Authenticated(value = VisitorAuthenticator.class)
    public Result videos() {
        return ok(videos.render(currentUser()));
    }
    
    @Authenticated(value = VisitorAuthenticator.class)
    public Result faq() {
        return ok(faq.render(currentUser()));
    }
    
    @Authenticated(value = VisitorAuthenticator.class)
    public Result history() {
        return ok(history.render(currentUser()));
    }

    @Authenticated(value = VisitorAuthenticator.class)
    public Result draw() {
        return ok(draw.render(currentUser()));
    }

    @Authenticated(value = VisitorAuthenticator.class)
    public Result contact() {
        return ok(contact.render(currentUser()));
    }
    @Authenticated(value = VisitorAuthenticator.class)
    public Result successStories() {
        return ok(success.render(currentUser()));
    }
    
    @Authenticated(value = VisitorAuthenticator.class)
    public Result newsAndPress() {
        return ok(newsAndPress.render(currentUser()));
    }
    @Authenticated(value = VisitorAuthenticator.class)
    public Result inauguration() {
        
		return ok(inauguration.render(currentUser()));
    }
    
    
    public Result agentLogin() {
        DynamicForm form = Form.form().bindFromRequest();
        String email = form.get("email");
        String password = form.get("password");
        password = password.trim();
        email = email.trim();
        LOG.info("User is going to login");
        if (email != null && password != null) {
        	User user = userDao.findUniqueByEmail(email);
        	if (user != null && user.password != null && user.checkPassword(password) && user.hasRole(Roles.AGENT) ){
        	
        		// Maybe unnecessary.
	            session().clear();

	            // Ensure to set the cookie to ensure that subsequent requests find this user logged-in.
	            session(common.ApplicationConstants.SESSION_KEY_LOGIN_USER_ROLE,Roles.AGENT);
	            session(SESSION_KEY_USER, user.email);
	            session(SESSION_KEY_LAST_ACCESS_TIME, String.valueOf(System.currentTimeMillis()));
	            session(SESSION_KEY_ROLE,createRoleString(user.roles));
	            // Update the lastLogin time & record IP address.
	            user.lastLogin = new Date();
	            user.lastLoginIp = request().remoteAddress();
                
                LOG.info("Successful login from user: " + user.email + " from IP: " + request().remoteAddress());
                userDao.save(user);
        			return redirect(routes.AgentDashboardController.agentDashboard());
        
        	}
        }
        
        return badRequest();
        
    }
    
    
    public Result MobileagentLogin() {
        DynamicForm form = Form.form().bindFromRequest();
        String email = form.get("email");
        String password = form.get("password");
        password = password.trim();
        email = email.trim();
        ObjectNode response = Json.newObject();
        LOG.info("User is going to login");
        if (email != null && password != null) {
        	User user = userDao.findUniqueByEmail(email);
        	if (user != null && user.password != null && user.checkPassword(password) && user.hasRole(Roles.AGENT) ){
        	
	            user.lastLogin = new Date();
	            user.lastLoginIp = request().remoteAddress();
                user.api_key = User.generateRandomApiKey();
                LOG.info("Successful login from user: " + user.email + " from IP: " + request().remoteAddress());
                userDao.save(user);
                response.put("authcode", user.api_key.toString());	
                return ok(response);
        	}
        }
        
        return badRequest();
        
    }
    
	public static Result returnCorsOptions(String path) {
		Response response = response();
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, x-cookie-value, Content-Type, Accept, Authorization, X-Auth-Token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		return ok();
	}
        
    
    
  }
