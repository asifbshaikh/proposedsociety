package controllers;

import models.Agent;
import models.User;
import models.dao.AgentDao;
import models.dao.DaoProvider;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import security.auth.AgentAuthenticator;

import com.avaje.ebean.Page;
import com.google.inject.Inject;

//@Authenticated(value = AgentAuthenticator.class)
public class AgentDashboardController extends AuthenticatedUserController {
	private final int PAGE_SIZE =5;
	private final AgentDao agentDao;
	
	@Inject
	public AgentDashboardController(DaoProvider provider) {
		super(provider.userDao());
		this.agentDao =provider.agentDao();
	}
	/*If agent is not approved then also we are showing dashboard to agent 
	so authentication dont need I need to add payment methods to this class  
	*/
	public Result agentDashboard(){
		User user = currentUser();
		
		if(user == null){
			return redirect(routes.Application.logout());
		}
		Agent agent =agentDao.findByUser(currentUser());
		
		if(agent == null){
			return redirect(routes.Application.logout());
		}
		
		Page<User> applicationFormList = userDao.getIncompleteApplicantForm(PAGE_SIZE,0,agent);
		return ok(views.html.agent.agentDashboard.render(applicationFormList,agent));
	}
}
