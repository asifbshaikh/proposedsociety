package models.dao.ebean;

import com.avaje.ebean.Ebean;
import play.db.ebean.Model.Finder;
import models.FixedDeposit;
import models.User;
import models.dao.FDDetailsDao;

public class EbeanFDDetailsDao extends AbstractEbeanDao<Long, FixedDeposit>
implements FDDetailsDao{

	protected EbeanFDDetailsDao() {
		super(new Finder<Long, FixedDeposit>(Long.class, FixedDeposit.class));
		
	}

	@Override
	public FixedDeposit findById(Long id, User user) {
		
		String query = "find fd fetch form fetch form.user where id = :id and form.user.id = :user_id";
		return Ebean.createQuery(FixedDeposit.class, query)
								.setParameter("id", id)
								.setParameter("user_id", user.id).findUnique();
	}

}
