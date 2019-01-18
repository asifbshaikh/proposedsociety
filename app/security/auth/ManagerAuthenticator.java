package security.auth;

import play.mvc.Result;
import common.Roles;
import controllers.routes;

public class ManagerAuthenticator extends BaseProposedSocietyAuthenticator {

	public ManagerAuthenticator() {
		super(Roles.MANAGER);
	}
	 @Override
	 public Result redirectTo(){
			return redirect(routes.UserController.login());
	  }
}
