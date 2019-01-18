package security.auth;

import play.mvc.Result;
import common.Roles;
import controllers.routes;

public class VisitorAuthenticator extends BaseProposedSocietyAuthenticator {
    public VisitorAuthenticator() {
        super(Roles.VISITOR);
    }

	@Override
	public Result redirectTo() {
		// TODO Auto-generated method stub
		return redirect(routes.Application.index());
	}
}
