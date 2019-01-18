package common;

import java.util.Arrays;
import java.util.List;
import models.Role;

public class Roles {
	public static final String VISITOR = "visitor";
	public static final String MEMBER = "member";
	public static final String ADMIN = "admin";
	public static final String MANAGER = "manager";
    public static final String ACCOUNTANT = "accountant";
    public static final String AGENT = "agent";
    public static final String LEGAL = "legal";
    public static final String BUERO = "buero";
    public static final String APPLICANT = "applicant";
    //To use in view 
	public static final List<String> roles=Arrays.asList("visitor","member", "admin","manager","accountant","agent","legal","buero","applicant"); 
	
    private Roles() {
    	
	}
    // In Global.java all roles are added in lower case so couldn't use list.contains() method. 
    public static boolean containsCaseInsensitive(String role, List<Role> roles){
        for(Role r : roles){
            if(role.equalsIgnoreCase(r.role)){
                return true;
            }
        }
        return false;
    }
}