package controllers;

import static common.ApplicationConstants.SESSION_KEY_LAST_ACCESS_TIME;
import static common.ApplicationConstants.SESSION_KEY_ROLE;
import static common.ApplicationConstants.SESSION_KEY_USER;

import java.util.Date;
import java.util.List;

import models.Agent;
import models.Role;
import models.User;
import models.dao.AgentDao;
import models.dao.DaoProvider;

import org.codehaus.jackson.JsonNode;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Result;
import utils.Mail;
import utils.Notifier;
import views.html.enterResetPassword;
import views.html.home;
import views.html.resetPassword;
import views.html.agent.changePasswordTemplate;
import views.html.staff.login;

import com.google.inject.Inject;

import common.ApplicationConstants;
import common.ConfigUtils;
import common.Roles;

public class UserController extends AuthenticatedUserController {
	private final AgentDao agentDao;
	private final Logger LOG = LoggerFactory.getLogger(UserController.class); 
	
	private  final String CONFIG_REDIRECTION_DOMAIN = ApplicationConstants.CONFIG_REDIRECTION_DOMAIN;
	String redirectionDomain;
	
	@Inject
	public UserController(DaoProvider provider) {
		super(provider.userDao());
		this.agentDao = provider.agentDao();
		this.redirectionDomain = removeTrailingSlash(ConfigUtils.getConfigString(CONFIG_REDIRECTION_DOMAIN, true));
	}
	
	public Result login() {
		return ok(login.render(null));
	}
	
	public Result goToDashboard(){
		String userRole = session(common.ApplicationConstants.SESSION_KEY_LOGIN_USER_ROLE);
		if(userRole.equals(Roles.ACCOUNTANT)){
    		return redirect(routes.AccountantUserController.dashboard());
		}else if(userRole.equals(Roles.ADMIN)){
			return redirect(routes.AdminUserController.dashboard());
		}else if(userRole.equals(Roles.MANAGER)){
			return redirect(routes.ManagerUserController.dashboard());
		}else if(userRole.equals(Roles.BUERO)){
			return redirect(routes.BueroController.bueroDashboard());
		}else if(userRole.equals(Roles.AGENT)){
			return redirect(routes.AgentDashboardController.agentDashboard());
		}else{
			 return notFound();
		}
    }
	
	public Result loginSubmit() {
        DynamicForm form = Form.form().bindFromRequest();
        String userType = form.get("userType");
        String email = form.get("user");
        String password = form.get("password");
        
        password = password.trim();
        email = email.trim();
        LOG.info("User is going to login");
        if (email != null && password != null) {
        	
        	User user = userDao.findUniqueByEmail(email);
        	if (user != null && user.password != null && user.checkPassword(password) && user.hasRole(userType) ){
        	
        		// Maybe unnecessary.
	            session().clear();

	            // Ensure to set the cookie to ensure that subsequent requests find this user logged-in.
	            session(common.ApplicationConstants.SESSION_KEY_LOGIN_USER_ROLE,userType);
	            session(SESSION_KEY_USER, user.email);
	            session(SESSION_KEY_LAST_ACCESS_TIME, String.valueOf(System.currentTimeMillis()));
	            session(SESSION_KEY_ROLE,createRoleString(user.roles));
	            // Update the lastLogin time & record IP address.
	            user.lastLogin = new Date();
	            user.lastLoginIp = request().remoteAddress();
                
                LOG.info("Successful login from user: " + user.email + " from IP: " + request().remoteAddress());
                userDao.save(user);
                
                if(userType.equals(Roles.ACCOUNTANT)){
            		return redirect(routes.AccountantUserController.dashboard());
        		}else if(userType.equals(Roles.ADMIN)){
        			return redirect(routes.AdminUserController.dashboard());
        		}else if(userType.equals(Roles.MANAGER)){
        			return redirect(routes.ManagerUserController.dashboard());
        		}else if(userType.equals(Roles.BUERO)){
        			return redirect(routes.BueroController.bueroDashboard());
        		}else if(userType.equals(Roles.AGENT)){
        			return redirect(routes.AgentDashboardController.agentDashboard());
        		}else{
        			 return notFound();
        		}
        	}
        }
        
        return ok(login.render("Invalid id and/or password!"));
    }
    
    public Result logout() {
        session().clear();
        return redirect(routes.UserController.login());
    }
    
    public Result resetPassword(){
    	return ok(resetPassword.render(null,null));
    }
    public Result enterPasswordToReset(String password){
    	return ok(enterResetPassword.render(null,null,password));
    }
    
    private String removeTrailingSlash(String url) {
        return url.trim().endsWith("/") ? url.trim().substring(0, url.length()) : url;
    }
    
    public Result resetPasswordSubmit(){
    	DynamicForm form = Form.form().bindFromRequest();
    	String emailId = form.get("emailId");
    	User user =userDao.findUniqueByEmail(emailId);
    	if(user == null){
    		return ok(resetPassword.render("Your email id is not registered with us.",false));
    	}else{
    		String rendomText  =user.generateResetPassword();
    		String message = "To reset your password " +"<a href="+redirectionDomain+routes.UserController.enterPasswordToReset(rendomText)+"> Click here</a>";
    		user.resetPassword = rendomText;
        	userDao.save(user);
        	
   			Mail mail = new Mail();
			mail.setSender(Mail.PS_FROM_ADDR);
			mail.setRecipient(user.email);
			mail.setSubject("Reset Password");
			mail.setBody(message);
   			Notifier.sendHtmlEmail(mail);
   			
        	return ok(resetPassword.render("Password reset link has been sent to your email id.",true));
    	}
    }

   
    
    public Result enterPasswordToResetSubmit(String password){
    	DynamicForm form = Form.form().bindFromRequest();
    	String newPassword = form.get("newPassword");
		String confirmPassword = form.get("confirmPassword");
		User user =userDao.findUniqueByResetPassword(password);
		
		if(user == null){
			return ok(enterResetPassword.render("Your reset request has expired.",false,password));
		}else{
			if(!BCrypt.checkpw( newPassword, user.password) && newPassword.equals(confirmPassword) ){
				user.password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
				user.resetPassword = null;
				userDao.save(user);
				LOG.info("Password has been reset successfully of user id"+user.id);
				return ok(home.render(currentUser()));
			}else{
				return ok(enterResetPassword.render("Password does not match Or same as old password",false,password));
			}
		}
    }
    
    
    public Result changePassword(){
		String id = session(ApplicationConstants.SESSION_KEY_USER);
		Agent agent = agentDao.findByUser(userDao.findUniqueByEmail(id));
		return ok(changePasswordTemplate.render(agent,null,"alert-info"));
	}
	public Result changePasswordSubmit(){
		DynamicForm form = Form.form().bindFromRequest();
		String email = session(ApplicationConstants.SESSION_KEY_USER);
		User user = userDao.findUniqueByEmail(email);
		String newPassword = form.get("newPassword");
		String confirmPassword = form.get("confirmPassword");
		newPassword = newPassword.trim();
		confirmPassword = confirmPassword.trim();
		
		if(!newPassword.equals(confirmPassword)){
			return ok(changePasswordTemplate.render(user.agent,"faile to confirm new password","alert-error"));
		}
		String oldPassword = form.get("oldPassword");
		oldPassword = oldPassword.trim();
		
		if(isValidPassword(oldPassword)){
	       	user.password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
	       	userDao.save(user);
			return ok(changePasswordTemplate.render(user.agent,"password changed successfully ","alert-info"));
		}else{
			return ok(changePasswordTemplate.render(user.agent,"Wrong old password","alert-error"));
		}
		
	}

	private boolean isValidPassword(String oldPassword) {
		User user = currentUser();
		return BCrypt.checkpw(oldPassword,user.password);
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
}
