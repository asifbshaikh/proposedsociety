package models.dao;

import models.User;
public interface ManagerDao extends Dao<Long, User> {
	User findById(User user);
}
