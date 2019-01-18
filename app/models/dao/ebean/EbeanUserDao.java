package models.dao.ebean;

import java.util.List;

import models.Address;
import models.Agent;
import models.Role;
import models.User;
import models.dao.UserDao;
import play.db.ebean.Model.Finder;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;

public class EbeanUserDao extends AbstractEbeanDao<String, User> implements
		UserDao {

	public EbeanUserDao() {
		super(new Finder<String, User>(String.class, User.class));
	}

	@Override
	public User findUniqueByEmail(String email) {
		return finder.where().eq("email", email).findUnique();
	}

	@Override
	public User findUniqueByMobile(String mobile) {
		return finder.where().eq("mobile", mobile).findUnique();
	}

	@Override
	public boolean verifyAuthCode(String email, String authcode) {
		return Ebean
				.createQuery(User.class,
						"select user where user.email = :email and user.authcode = :authcode")
				.setParameter("email", email)
				.setParameter("authcode", authcode).findUnique() != null;
	}

	@Override
	public boolean userExistsWithEmail(String email) {
		User found = findUniqueByEmail(email);
		if (found != null) {
			return true;
		}

		return false;
	}

	@Override
	public boolean userExistsWithMobile(String mobile) {
		User found = findUniqueByMobile(mobile);
		if (found != null) {
			return true;
		}

		return false;
	}

	@Override
	public List<User> getRecentVisitor() {
		 List<User> user = Ebean.find(User.class).order().desc("joiningDate").setMaxRows(100).findList();
		 return user;
	}

	@Override
	public User findByFormId(Long id) {
		return Ebean.find(User.class)
				.where()
				.eq("form_id",id).findUnique();
				
	}
	
	@Override
	public User findByAgentId(Long id) {
		String query = "find User fetch agent where agent.id = :agent_id";
        return Ebean.createQuery(User.class, query)
                .setParameter("agent_id", id).findUnique();
	}

	@Override
	public User findUniqueByResetPassword(String password) {
		return finder.where().eq("reset_password", password).findUnique();
	}

	//Search user list page  by email, mobile or role. 
	@Override
	public Page<User> foundUserPage(int pageSize, int pageNumber,String userAttribute) {
		Page<User> userList =  finder.where().disjunction()
				.ieq("email", userAttribute)
				.ieq("mobile",userAttribute)
				.ieq("roles.role", userAttribute)
				.endJunction().findPagingList(pageSize).getPage(pageNumber);
		return userList;
	}

	@Override
	public void removeRoles(User user) {
	    List<Role> role = Ebean.find(Role.class)
				 .where().eq("user.id", user.id).findList();
		Ebean.delete(role);		 							
	}

	@Override
	public Page<User> getIncompleteApplicantForm(int pageSize, int pageNumber,Agent agent) {
		Page<User> appFormList = null;
		if(agent !=null){
			appFormList = finder.where().ne("form.status","Complete").eq("form.agent.id",agent.id).findPagingList(pageSize).getPage(pageNumber);	
		}
		return appFormList;
	}

	@Override
	public Page<User> findUserInterestedInBecomeAnAgent(int pageSize,int pageNumber) {
		Page<User> userList = null;
		userList = finder.where().eq("interestedInAgent",true).order().desc("lastLogin").findPagingList(pageSize).getPage(pageNumber);
		return userList;
	}
	
	@Override
	public boolean verifyByApiKey(String apiKey){
		User userApiKey = Ebean.find(User.class).where().eq("api_key",apiKey).findUnique();
		if(userApiKey!= null)
			return true;
		else
			return false;
	}
	
	@Override
	public User findUniqueByApiKey(String apiKey){
		return finder.where().eq("api_key",apiKey).findUnique();
	}
	
}
