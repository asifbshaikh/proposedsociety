package security.auth;

import play.mvc.Result;

import common.Roles;

import controllers.routes;

public class ApplicantAuthenticator extends BaseProposedSocietyAuthenticator{
	
	public ApplicantAuthenticator() {
        super(Roles.APPLICANT);
    }

	@Override
	public Result redirectTo() {
		// TODO Auto-generated method stub
		return redirect(routes.Application.index());
	}
	
}
