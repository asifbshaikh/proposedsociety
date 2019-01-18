package controllers;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.Agent;
import models.AgentFeedbackFormFile;
import models.ApplicationForm;
import models.Buero;
import models.FileAttachment;
import models.InvestigationReport;
import models.ReportCsv;
import models.Role;
import models.User;
import models.dao.AgentDao;
import models.dao.AgentFeedbackFormFileDao;
import models.dao.ApplicationFormDao;
import models.dao.BueroDao;
import models.dao.DaoProvider;
import models.dao.FileAttachmentDao;
import models.dao.InvestigationReportDao;
import models.dao.PageDao;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.GlobalSettings;
import play.data.DynamicForm;
import play.data.Form;
import play.data.validation.Constraints.Required;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import security.auth.AdminAuthenticator;
import utils.Mail;
import utils.Notifier;
import birt.CsvGenerator;

import com.avaje.ebean.Page;
import com.google.inject.Inject;

import common.PSError;
import common.Roles;
import views.html.*;
import views.html.admin.*;
import views.html.admin.pages.*;

@Authenticated(value = AdminAuthenticator.class)
public class AdminUserController extends AuthenticatedUserController {
	private static final Logger LOG = LoggerFactory.getLogger(GlobalSettings.class);
	private static String searchUserBy = "";
	private final AgentDao agentDao;
	private final int PAGE_SIZE = 15; //Page contains number of agents
	private final BueroDao bueroDao;
	private final InvestigationReportDao investigationReportDao;
	private final PageDao pageDao;
	private final ApplicationFormDao appFormDao ;
	private final AgentFeedbackFormFileDao agentFeedbackFormFileDao;
	private final FileAttachmentDao attachmentDao;
	private final String MULTI_SELECT = "multiselect";
	private final CsvGenerator csvGenerator;
	private final Set<String> VALID_ROLES = new HashSet<String>(Arrays.asList(Roles.ACCOUNTANT,Roles.BUERO,Roles.ADMIN,Roles.MANAGER,Roles.AGENT,Roles.APPLICANT,Roles.VISITOR,Roles.MEMBER));
	
	@Inject
	public AdminUserController(DaoProvider provider,CsvGenerator csvGenerator) {
		super(provider.userDao());
		this.agentDao =provider.agentDao();
		this.bueroDao =provider.bueroDao();
		this.pageDao = provider.pageDao();
		this.attachmentDao = provider.fileAttachmentDao();
		this.investigationReportDao =provider.investigationReportDao();
		this.agentFeedbackFormFileDao = provider.agentFeedbackFormFileDao();
		this.appFormDao = provider.applicationFormDao();
		this.csvGenerator = csvGenerator;
	}
	
	public Result dashboard() {
		return  ok(adminDashboard.render( agentDao.getAgentStatistics(),currentUser()));
	}
	
	public Result createNewUserPage(){
		return ok(createNewUser.render(currentUser(),null));
	}
	
	public Result reportFilter(){
		return ok(reportFilter.render(currentUser()));
	}
	
	public static class ReportData{
		@Required public String state;
		@Required public String district;
		@Required public String taluka;
		@Required public String pincodeAndLocality;
		@Required public Long budget;
		@Required public String type;
		public String pincode;
		public String locality;
		
		@Override
		public String toString(){
			return  "State: " + state + " District: " + district + " Taluka: " + taluka + " Pincode: " + pincode;
		}
	}
	
	public Result generateReport(){
		Form<ReportData> form = Form.form(ReportData.class).bindFromRequest();
		if(form.hasErrors()) return badRequest();
		ReportData data = form.get();
		data.pincode = data.pincodeAndLocality.substring(0,6);
	
		List<ApplicationForm> list = appFormDao.findApplicationFormForReport(data);
		String output = new String() ;
		for(ApplicationForm t: list){
			output += t.id + ","; 
		}
		
		return ok(output.substring(0, output.length()-1));
		
	}
	
	public Result downloadReport(String ids){
		List<Long> idList = getFormIds(ids);
		File f; 
		try{
			f = csvGenerator.genrate(getForms(idList));
			
			response().setContentType("application/x-download");
			response().setHeader("Content-disposition","attachment; filename=" + "report.csv");
			return ok(f);
		}catch(Exception e){
			LOG.error("Found Exception",e);
			return badRequest();
		}
	}
	
	private List<ReportCsv> getForms(List<Long> idList) {
		List<ReportCsv> list = new ArrayList<ReportCsv>();
		ApplicationForm f ;
		ReportCsv t;
		for(Long id: idList){
			f = appFormDao.findById(id);
			if(f != null){
				t = new ReportCsv();
				t.budget = f.calculatedBudget;
				t.builtUpArea = f.requirements.get(0).builtupArea;
				t.carpetArea = f.requirements.get(0).builtupArea;
				t.dob = f.applicant.dob;
				t.email = f.applicant.email;
				t.fName = f.applicant.fname;
				t.lName = f.applicant.lname;
				t.mName = f.applicant.mname;
				t.mobile = f.applicant.off_phone1;
				t.propertyType = f.requirements.get(0).requirementType;
				t.subType = f.requirements.get(0).subType;
				t.sex = f.applicant.sex;
				list.add(t);
				
			}
		}
		LOG.info("Found Report CSV: " + list.get(0).toString());
		
		return list;
	}

	private List<Long> getFormIds(String ids) {
		List<Long> list = new ArrayList<Long>();
		String[] l = ids.split(",");
		if(l.length > 0){
			for(int i=0; i< l.length; i++){
				try{
					list.add(Long.parseLong(l[i]));
				}catch(Exception e){
				}
			}
		}
		return list;
	}

	public Result cmsPageList() {
		return ok(cmsPageList.render(pageDao.findAll(), currentUser()));
	}
	
	public Result cmsEditPage(Long id) {
		models.Page page = pageDao.findById(id);
		if (page == null) {
			return redirect(routes.AdminUserController.dashboard());
		}
		
		return ok(cmsEditPage.render(page, currentUser(), flash()));
	}
	
	public Result cmsEditPageSubmit() {
		Form<models.Page> form = Form.form(models.Page.class).bindFromRequest();
		if (form.hasErrors()) {
			// TODO: Handle errors.
			return badRequest("Form has errors, please correct.");
		}
		
		models.Page page = form.get();
		models.Page original = pageDao.findById(page.id);
		
		if (original == null) {
			flash("error", "Failed to save changes!");
			return redirect(routes.AdminUserController.cmsEditPage(page.id));
		}

		original.title = page.title;
		original.content = page.content;

		pageDao.save(original);
		
		flash("success", "Changes saved successfully!");
		
		return redirect(routes.AdminUserController.cmsEditPage(original.id));
	}
	
    //enabling administrator to create a user with multiple roles.

	public Result createNewUser(){
		 Form<User> form = Form.form(User.class).bindFromRequest();
		 
		 if(form.hasErrors()){
			 LOG.info("Unable to create new user: Errors" + form.errors());
			 return badRequest(createNewUser.render(currentUser(),PSError.VALIDATION_ERRORS));
		 }
		 User user = form.get();
		 if(userDao.userExistsWithEmail(user.email) || userDao.userExistsWithMobile(user.mobile)){
			 return badRequest(createNewUser.render(currentUser(),PSError.EMAIL_OR_MOBILE_EXISTS));
		 }
		 String passwd = User.generatePassword();
		 user.password = BCrypt.hashpw(passwd, BCrypt.gensalt());
		 Map<String, String[]> urlFormEncoded = play.mvc.Controller.request().body().asFormUrlEncoded();
		 user.roles = getRoles(urlFormEncoded,user);
		 user.joiningDate = new Date();
		 userDao.save(user);

		 Mail mail = new Mail();
		 mail.setSender(Mail.PS_FROM_ADDR);
		 mail.setRecipient(user.email);
		 mail.setSubject("Welcome to Proposed Society");
		 mail.setBody(views.html.mails.visitorPasswordMail.render(user,passwd).toString());
		 
		 Notifier.sendHtmlEmail(mail);
		 
		 return  ok(confirmationMessage.render(user, currentUser()));
		 
	}
	
	private List<Role> getRoles(Map<String, String[]> urlFormEncoded,User user) {
		Set<String> roleSet = new HashSet<String>();
		List<Role> roles = new ArrayList<Role>();
		if (urlFormEncoded != null) {
			String[] value = urlFormEncoded.get(MULTI_SELECT);
				if(value !=null){
					for(String role : value){
						roleSet.add(role); 
					}
				}
        }
		roleSet.retainAll(VALID_ROLES);
		for(String role : roleSet){
			roles.add(new Role(user,role));
		}
		return roles;
	}	

	// apply pagination to recent visitor list also
	public Result recentVisitor() {
		List<User> userList = userDao.getRecentVisitor();
		return ok(recentVisitor.render(userList,currentUser()));
	}
	
	public Result getUserInterestedInBecomeAnAgent(int pageNumber) {
		return ok(intrestedInAgent.render(userDao.findUserInterestedInBecomeAnAgent(PAGE_SIZE, pageNumber),currentUser()));
	}
	
	public Result displayAgentsList(int pageNumber){
		User user =currentUser();
		List<Buero> bueroDetails = bueroDao.getInvestigationBueros();
		return ok(agentListForAdmin.render(agentDao.findPage(PAGE_SIZE, pageNumber),user,bueroDetails));
	}
	
	public Result displayAgentInfo(Long id){   //Display info of peticular id 
		Agent agent = agentDao.findById(id);
		if(agent.ib != null){
			Buero buero = bueroDao.findById(agent.ib.id);
			InvestigationReport form = investigationReportDao.getFeedbackForm(buero,agent);
			if(buero != null&& form != null){
				return ok(displayAgentInfo.render(agent,form));
			}
		}
		return ok(displayAgentInfo.render(agent,null));
	}
	
	public Result assignAgentToBuero(Long id, Long bueroId){
		List<Buero> bueroList = bueroDao.getInvestigationBueros();
		Agent agent = agentDao.findById(id);
		Buero buero =bueroDao.findById(bueroId);
		agent.ib = buero;
		agentDao.save(agent);
		if(bueroList != null){
			return ok(agentListForAdmin.render(agentDao.findPage(PAGE_SIZE,0),currentUser(),bueroList));
		}else{
			return ok(agentListForAdmin.render(null,currentUser(),null));
		}
	}
	
	
	public Result approveOrRejectAgentForm(Long id){
		DynamicForm bindedForm = Form.form().bindFromRequest();
		String approve = bindedForm.get("approve");
		String message = null;
		Agent agent = agentDao.findById(id);
		message = bindedForm.get("txtarea");
		
		if(approve.equals("A")){
			 agent.form.status = FormStatus.APPROVED;
			 agentDao.save(agent);
			 Mail mail = new Mail();
			 mail.setSender(Mail.PS_FROM_ADDR);
			 mail.setRecipient(agent.user.email);
			 mail.setSubject("Your application status from  Proposed Society");
			 mail.setBody(views.html.mails.agentApprovalMail.render(agent.user).toString());
			 Notifier.sendHtmlEmail(mail);
		}else{
			 agent.form.status = approve.equals("N")? FormStatus.REJECTED : FormStatus.ON_HOLD;
			 agentDao.save(agent);
			 Mail mail = new Mail();
			 mail.setSender(Mail.PS_FROM_ADDR);
			 mail.setRecipient(agent.user.email);
			 mail.setSubject("Your application status from  Proposed Society");
			 mail.setBody(views.html.mails.agentFormSatusMail.render(agent.user,message).toString());
			 Notifier.sendHtmlEmail(mail);
		}

		 
		return ok(agentConfirmationMessage.render(currentUser(),agent));
	}
	
	public Result approvedAgentList(int pageNumber){
		return  ok(approvedAgentList.render(agentDao.findApprovedAgentPage(PAGE_SIZE, pageNumber),currentUser()));
	}
	
	// Search Update Delete User
	@Authenticated(value = AdminAuthenticator.class)
	public Result searchUser(){
		return ok(searchUser.render(currentUser(),null));
	}
	
	@Authenticated(value = AdminAuthenticator.class)
	public Result searchUserSubmit(){
		DynamicForm bindedForm = Form.form().bindFromRequest();
		String elementValue =bindedForm.get("search");
		searchUserBy = elementValue;
		Page<User> users =userDao.foundUserPage(PAGE_SIZE,0,elementValue);
		return ok(searchUser.render(currentUser(),users));
	}
	
	@Authenticated(value = AdminAuthenticator.class)
	public Result showSearchedUserPage(int pageNUmber){
		Page<User> users =userDao.foundUserPage(PAGE_SIZE,pageNUmber, searchUserBy);
		return ok(searchUser.render(currentUser(),users));
	}
	
	@Authenticated(value = AdminAuthenticator.class)
	public Result updateUser(String encriptedEmail){
		String email = User.decryptEmail(encriptedEmail);
		User user =userDao.findUniqueByEmail(email);
		return ok(updateUser.render(user,currentUser()));
	}
	
	@Authenticated(value = AdminAuthenticator.class)
	public Result updateUserSubmit(String encriptedEmail){
		DynamicForm bindedForm = Form.form().bindFromRequest();
		if(encriptedEmail == null){
			return ok(searchUser.render(currentUser(),null));
		}else{
			String email = User.decryptEmail(encriptedEmail);
			User user =userDao.findUniqueByEmail(email);
				if(user == null){
					return ok(searchUser.render(currentUser(),null));
				}else{
					 String name = bindedForm.get("name");
					 user.name=name;
					 Map<String, String[]> urlFormEncoded = play.mvc.Controller.request().body().asFormUrlEncoded();
					 userDao.removeRoles(user);
					 user.roles = getRoles(urlFormEncoded,user);

					 if(!user.roles.contains(Roles.BUERO)){
						 LOG.info("Roles has buero");
						 if(bueroDao.findIfNotExist(user)){
							 LOG.info("buero doesnt exist");
							 Buero buero =new Buero(user);
					         bueroDao.save(buero);
					         LOG.info("User added as buero "+user.email);
						 }
					 }
					 user.save();
					 LOG.info("User Updated successfully updates roles are "+user.showRoles(user.roles));
					 return  ok(confirmationMessage.render(user, currentUser()));
				}
		}
	}

	public Result investigationFormUpload() {
		return ok(views.html.admin.investigationFormFileUpload.render(currentUser(), flash()));
	}
	
	public Result investigationFormUploadSubmit() {
		DynamicForm form = Form.form().bindFromRequest();
		String fileId = form.get("fileId");

		if (fileId == null) {
			flash("error", "Unable to save file");
			return redirect(routes.AdminUserController.investigationFormUpload());
		}
		
		FileAttachment attachment = attachmentDao.findByFilePath(fileId);
		
		if (attachment == null) {
			flash("error", "Unable to save file");
			return redirect(routes.AdminUserController.investigationFormUpload());
		}
		
		attachmentDao.save(attachment);
		AgentFeedbackFormFile feedbackFile = new AgentFeedbackFormFile();
		feedbackFile.uploadTime = new Date();
		feedbackFile.feedbackFormFile = attachment;
		
		agentFeedbackFormFileDao.save(feedbackFile);
		
		flash("success", "New investigation form saved successfully!");
		return redirect(routes.AdminUserController.investigationFormUpload());
	}
	
	/*//There are dependancy problems so we will do it later. I found attachment table makes problem.
	 All dummy users can delete easily. Agent & applicant can't delete , due to dependancy
	*/  
	//Should provide "Are you confirm" Yes/No message while clicking delete button.
	
	@Authenticated(value = AdminAuthenticator.class)
	public Result deleteUser(String encriptedEmail){
		
		if(encriptedEmail == null){
			return ok(searchUser.render(currentUser(),null));
		}else{
			String email = User.decryptEmail(encriptedEmail);
			User user =userDao.findUniqueByEmail(email);
			// If Admin role is assigned then that user can not be delete.Only can update by clicking update.
			if(user.hasRole(Roles.ADMIN)){
				return ok(searchUser.render(currentUser(),userDao.foundUserPage(PAGE_SIZE, 0, searchUserBy)));
			}else{
				user.delete();
				LOG.info(user.email+" has been deleted successfully");
				return ok(searchUser.render(currentUser(),userDao.foundUserPage(PAGE_SIZE, 0, searchUserBy)));
			}
		}
	}
}