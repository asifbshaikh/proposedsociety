package models.dao.ebean;

import models.Role;
import models.dao.RoleDao;
import play.db.ebean.Model.Finder;

import com.avaje.ebean.Ebean;

public class EbeanRoleDao extends AbstractEbeanDao<Long, Role> implements RoleDao  {

	protected EbeanRoleDao()  {
        super(new Finder<Long, Role>(Long.class, Role.class));
    }

	
	public void removeRole(Role role) {
			
		Ebean.delete(role);
	}

}
