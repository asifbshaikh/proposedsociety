package controllers;


import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.ApplicationForm;
import models.User;
import models.dao.ApplicationFormDao;
import models.dao.DaoProvider;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Security;
import security.auth.ManagerAuthenticator;

import com.google.inject.Inject;

@Security.Authenticated(value = ManagerAuthenticator.class)
public class ManagerUserController extends AuthenticatedUserController {
	private static final Logger LOG = LoggerFactory.getLogger(ApplicationFormController.class);
	private final ApplicationFormDao appFormDao;
	
	@Inject
	public ManagerUserController(DaoProvider provider) {
		super(provider.userDao());
		this.appFormDao =provider.applicationFormDao();
	}
	
	public Result dashboard() {
		return ok(views.html.manager.managerDashboard.render(currentUser()));
	}
	//All paid appilicant List for manager
	public Result applicantListForManager() {
		List<ApplicationForm> userList = appFormDao.getPaidApplicant();
		return ok(views.html.manager.applicantListForManager.render(currentUser(),userList));
	}
	public Result applicantRequirement(Long id){
		User user = userDao.findByFormId(id);
		return ok(views.html.manager.applicantRequirements.render(user.form,currentUser()));
	}
	public Result searchApplicantBy() {
		return ok(views.html.manager.searchBySuggestedArea.render(currentUser(),null));
	}
	public Result submitToSearch(){
		try{
			DynamicForm form = Form.form().bindFromRequest();
			String regex ="\\d+(\\.\\d+)?"; //Regular expression can accept min,max budget decimal long value. 
			if(form.data().get("Pincode") != null && form.data().get("minBudget") != null && form.data().get("maxBudget") != null && Pattern.matches(regex, form.data().get("minBudget")) && Pattern.matches(regex, form.data().get("maxBudget"))){
				String pincode = form.get("Pincode").substring(0,6);
				BigDecimal bd = new BigDecimal(form.get("minBudget"));
				Long minBudget = bd.longValue();
				Long maxBudget =Long.parseLong(form.get("maxBudget"));
				List<ApplicationForm> userList = appFormDao.searchBySuggestedArea(pincode,minBudget,maxBudget);
				return ok(views.html.manager.searchBySuggestedArea.render(currentUser(),userList));
			}else{
				LOG.error("data is incorrect form");
				return ok(views.html.manager.searchBySuggestedArea.render(currentUser(),null));
			}
		}catch(NumberFormatException e){
			LOG.warn("Min Max budget should be number format:",e);
			return ok(views.html.manager.searchBySuggestedArea.render(currentUser(),null));
		}
		
	}
}