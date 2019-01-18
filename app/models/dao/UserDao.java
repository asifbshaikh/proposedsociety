package models.dao;

import java.util.List;

import com.avaje.ebean.Page;

import models.Agent;
import models.User;

public interface UserDao extends Dao<String, User>{
	List<User> getRecentVisitor();
	User findUniqueByEmail(String email);
	User findUniqueByMobile(String mobile);
	boolean userExistsWithEmail(String email);
	boolean userExistsWithMobile(String mobile);
	boolean verifyAuthCode(String email, String authCode);
	User findByFormId(Long id);
	User findByAgentId(Long id);
	User findUniqueByResetPassword(String password);
	Page<User> foundUserPage(int pageSize, int pageNumber,String userAttribute);
	void removeRoles(User user);
	Page<User> getIncompleteApplicantForm(int pageSize,int pageNumber,Agent agent);
	Page<User> findUserInterestedInBecomeAnAgent(int pageSize, int pageNumber);
	boolean verifyByApiKey(String apiKey);
	User findUniqueByApiKey(String apiKey);
}