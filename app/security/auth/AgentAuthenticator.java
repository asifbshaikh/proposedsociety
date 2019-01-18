package security.auth;

import play.mvc.Result;
import common.Roles;
import controllers.routes;

public class AgentAuthenticator extends BaseProposedSocietyAuthenticator {

	public AgentAuthenticator() {
		super(Roles.AGENT);
	}
	@Override
	public Result redirectTo(){
		
		return redirect(routes.AgentFormController.agentForm());
		
	}

}
