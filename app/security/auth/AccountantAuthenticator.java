package security.auth;

import play.mvc.Result;
import common.Roles;
import controllers.routes;

public class AccountantAuthenticator extends BaseProposedSocietyAuthenticator {
    public AccountantAuthenticator() {
        super(Roles.ACCOUNTANT);
    }
    
    @Override
    public Result redirectTo(){
		return redirect(routes.UserController.login());
    }
}
