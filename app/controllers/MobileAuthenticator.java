package controllers;


import models.dao.UserDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security.Authenticator;

public final class MobileAuthenticator extends Authenticator{
	//UserDao userdao;
	private static final Logger LOG = LoggerFactory.getLogger(ApplicationFormController.class);
	
	@Override
	public String getUsername(Context context){
		String apiKey = context.request().getHeader("apiKey");
		LOG.debug("API KEY: " + apiKey);
		boolean verifyUser = true; //userdao.verifyByApiKey(apiKey);
		if(verifyUser == true)
		{
			LOG.debug("User Authenticated Sucessfully!");
			return apiKey;
		}
		else
			return null;
	}
    
	@Override
    public final Result onUnauthorized(Context context) {
        LOG.error("User Authentication Fails.");
		return badRequest();
    }
	
}