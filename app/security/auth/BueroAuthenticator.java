package security.auth;

import play.mvc.Result;
import common.Roles;
import controllers.routes;

public class BueroAuthenticator extends BaseProposedSocietyAuthenticator {

	public BueroAuthenticator() {
		super(Roles.BUERO);
	}
	@Override
	public Result redirectTo(){
		return redirect(routes.UserController.login());
	}
}

