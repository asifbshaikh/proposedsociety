package controllers;

import play.mvc.Result;
import models.Agent;
import models.User;
import models.dao.AgentDao;
import models.dao.DaoProvider;

import com.google.inject.Inject;

public class AgentUserController extends AuthenticatedUserController{
	private final AgentDao agentDao ;
	@Inject
	public AgentUserController(DaoProvider provider) {
		super(provider.userDao());
		this.agentDao = provider.agentDao();
	}
	
	public Result dashboard(){
		User user = currentUser();
		Agent agent = agentDao.findByUser(user);
		return ok(views.html.agent.agentDashboard.render(null,agent));
	}
}
