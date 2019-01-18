package controllers;

import models.User;
import models.dao.DaoProvider;
import play.mvc.Result;
import play.mvc.Security;
import security.auth.AccountantAuthenticator;

import com.google.inject.Inject;

@Security.Authenticated(value = AccountantAuthenticator.class)
public class AccountantUserController extends AuthenticatedUserController{
		
		@Inject
		public AccountantUserController(DaoProvider provider) {
			super(provider.userDao());
		}
		
		public Result dashboard(){
			User user = currentUser();
			return ok(views.html.accountant.accountantDashboard.render(user));
		}
}
