package controllers;

import models.User;
import models.dao.UserDao;
import play.mvc.Controller;
import common.ApplicationConstants;

public abstract class AuthenticatedUserController extends Controller {
	protected final UserDao userDao;
	
	public AuthenticatedUserController(UserDao userDao) {
		super();
		this.userDao = userDao;
	}

	public final User currentUser() {
		String id = session(ApplicationConstants.SESSION_KEY_USER);
		if (id != null) {
			return userDao.findUniqueByEmail(id);
		}

		return null;
	}
	
	public final User MobilecurrentUser() {
		String apiKey = request().getHeader("apiKey");
		if(apiKey != null){
			return userDao.findUniqueByApiKey(apiKey);
		}
		return null;
	}

}