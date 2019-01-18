package security.auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import models.Role;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security.Authenticator;

import common.ApplicationConstants;

import controllers.routes;

public abstract class BaseProposedSocietyAuthenticator extends Authenticator {
    private static final long SESSION_TIMEOUT = TimeUnit.HOURS.toMillis(1);
    private final String role;

    public BaseProposedSocietyAuthenticator(String role) {
        super();
        this.role = role;
    }

    @Override
    public String getUsername(Context context) {
        String user = context.session().get(ApplicationConstants.SESSION_KEY_USER);
        String lastLogin = context.session().get(ApplicationConstants.SESSION_KEY_LAST_ACCESS_TIME);
        String roles = context.session().get(ApplicationConstants.SESSION_KEY_ROLE);
        
        if (isSessionValid(user, lastLogin, roles, role)) {        
            context.session().put(ApplicationConstants.SESSION_KEY_LAST_ACCESS_TIME, String.valueOf(System.currentTimeMillis()));
            return user;
        }
        
        return null;
    }

    private static boolean isSessionExpired(String user, String accessTimeStr) {
        if (user == null || accessTimeStr == null) {
            return true;
        }
        
        try {
            Long lastAccessTime = Long.parseLong(accessTimeStr);
            if (System.currentTimeMillis() - lastAccessTime > SESSION_TIMEOUT) {
                // Invalidate session
                return true;
            }
        } catch (NumberFormatException nfe) {
            // Invalidate session.
            return true;
        }
        
        return false;
    }

    public static boolean isSessionValid(String user, String accessTimeStr, List<Role> roles, String expectedRole) {
        if (roles == null || roles.isEmpty()) {
            return false;
        }
        
        List<String> roleStrings = new ArrayList<String>();
        for (Role r : roles) {
            roleStrings.add(r.role);
        }
        
        return !isSessionExpired(user, accessTimeStr) && roleStrings.contains(expectedRole);
    } 

    public static boolean isSessionValid(String user, String accessTimeStr, String roles, String expectedRole) {
        if (roles != null) {
            return !isSessionExpired(user, accessTimeStr) && Arrays.asList(roles.split(Pattern.quote(","))).contains(expectedRole);
        }

        return false;
    }

    @Override
    public final Result onUnauthorized(Context context) {
        context.session().clear();
        return redirectTo();
    }

	public abstract Result redirectTo() ;
}
