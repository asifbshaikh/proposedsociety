package models.dao;

import models.Role;

public interface RoleDao extends Dao<Long, Role> {
	void removeRole(Role role);
	
}
