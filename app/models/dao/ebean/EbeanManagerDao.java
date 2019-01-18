package models.dao.ebean;

import com.avaje.ebean.Ebean;
import play.db.ebean.Model.Finder;
import models.User;
import models.dao.ManagerDao;

public class EbeanManagerDao  extends AbstractEbeanDao<Long, User> implements ManagerDao  {
	//private static final Logger LOG = LoggerFactory.getLogger(User.class);
	
	public EbeanManagerDao() {
        super(new Finder<Long, User>(Long.class, User.class));
    }

	@Override
    public User findById(User user) {
        String query = "find user fetch user where user.id = :user_id";
        return Ebean.createQuery(User.class, query)
                .setParameter("user_id", user.id).findUnique();
    }
	
}
