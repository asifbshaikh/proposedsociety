package models.dao.ebean;

import com.avaje.ebean.Ebean;
import models.RecurringDeposit;
import models.User;
import models.dao.RDDetailsDao;
import play.db.ebean.Model.Finder;

public class EbeanRDDetailsDao extends AbstractEbeanDao<Long, RecurringDeposit>
implements RDDetailsDao {

	protected EbeanRDDetailsDao() {
		super(new Finder<Long, RecurringDeposit>(Long.class, RecurringDeposit.class));

	}

	
	@Override
	public RecurringDeposit findById(Long id, User user) {
			String query = "find rd fetch form fetch form.user where id = :id and form.user.id = :user_id";
			return Ebean.createQuery(RecurringDeposit.class, query)
								.setParameter("id", id)
								.setParameter("user_id", user.id).findUnique();
	}
	
	
}
