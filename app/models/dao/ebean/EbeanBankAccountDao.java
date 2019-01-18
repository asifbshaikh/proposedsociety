package models.dao.ebean;

import com.avaje.ebean.Ebean;

import models.BankAccount;
import models.User;
import models.dao.BankAccountDao;
import play.db.ebean.Model.Finder;

public class EbeanBankAccountDao extends AbstractEbeanDao<Long, BankAccount>
		implements BankAccountDao {

	public EbeanBankAccountDao() {
		super(new Finder<Long, BankAccount>(Long.class, BankAccount.class));
	}

	@Override
	public BankAccount findById(Long id, User user) {
		String query = "find account fetch form fetch form.user where id = :id and form.user.id = :user_id";
		
		return Ebean.createQuery(BankAccount.class, query)
								.setParameter("id", id)
								.setParameter("user_id", user.id).findUnique();
	}
}
