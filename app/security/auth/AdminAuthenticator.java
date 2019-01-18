package security.auth;

import play.mvc.Result;
import common.Roles;
import controllers.routes;

public class AdminAuthenticator extends BaseProposedSocietyAuthenticator {
    public AdminAuthenticator() {
        super(Roles.ADMIN);
    }
    
    @Override
    public Result redirectTo(){
		return redirect(routes.UserController.login());
    }
}